package com.usth.wikipedia;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.support.v7.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener{

    private ListView list;
    private ListViewAdapter adapter;
    private SearchView searchView;
    private String[] articleNameList;
    private Bitmap[] articleImageList;
    private String[] articleDescriptionList;
    private ArrayList<ArticleRepo> arrayList = new ArrayList<>(5);
    private String parseTitle; // Key exchange

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_search, container, false);
        // Locate the SearchView

        // ListView
        list = view.findViewById(R.id.list_article);

        final ArticleRepo example = new ArticleRepo("Example", "Ex", BitmapFactory.decodeResource(getResources(), R.drawable.no_img));
        arrayList.add(example);

        adapter = new ListViewAdapter(view.getContext(), arrayList);

        // Binds the Adapter to the ListView
        list.setAdapter(adapter);


        searchView = view.findViewById(R.id.search_view);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);

        return view;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        adapter.filter();
        SearchArticleTask process = new SearchArticleTask();
        process.execute(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.filter();
        return true;
    }




    private class SearchArticleTask extends AsyncTask<String, Void, Boolean> {
        private String parseContent;
        private URL parseImage, originURL;
        private Bitmap bmp;

        protected void onPreExecute() {
            articleNameList = new String[5];
            articleDescriptionList = new String[5];
            articleImageList = new Bitmap[5];
        }

        protected Boolean doInBackground(String... titles) {
            String articleTitle = titles[0];
            try {
                originURL = new URL("https://en.wikipedia.org/w/api.php?format=json&action=query&generator=search&gsrsearch=" + articleTitle + "&gsrlimit=5&prop=pageimages|description&pilimit=max&pithumbsize=600");
                HttpURLConnection httpURLConnection = (HttpURLConnection) originURL.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader((new InputStreamReader(inputStream)));
                String line = "";
                parseContent = "";
                while (line != null) {
                    line = bufferedReader.readLine();
                    parseContent = parseContent + line;
                }
                JSONObject JO = new JSONObject(parseContent);
                JSONObject query = JO.getJSONObject("query");
                JSONObject pages = query.getJSONObject("pages");
                Iterator<String> keys = pages.keys();
                for (int i = 0; i < pages.length(); i++) {
                    String id = keys.next();
                    JSONObject idJSONObject = pages.getJSONObject(id);
                    articleNameList[i] = idJSONObject.getString("title");
                    if (!idJSONObject.isNull("description")) {
                        articleDescriptionList[i] = idJSONObject.getString("description");
                    } else {
                        articleDescriptionList[i] = "None";
                    }
                    if (!idJSONObject.isNull("thumbnail")) {
                        JSONObject thumbnail = idJSONObject.getJSONObject("thumbnail");
                        parseImage = new URL(thumbnail.getString("source"));
                        bmp = BitmapFactory.decodeStream(parseImage.openConnection().getInputStream());
                    } else {
                        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.no_img);
                    }
                    articleImageList[i] = bmp;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        protected void onPostExecute(Boolean success) {
            if (!success) {
                Toast.makeText(getActivity(), "Error network!", Toast.LENGTH_SHORT).show();
            } else {
                int j = 0;
                while (j<5) {
                    ArticleRepo articleRepo = new ArticleRepo(articleNameList[j], articleDescriptionList[j], articleImageList[j]);
                    //Binds all object into an array
                    arrayList.add(articleRepo);
                    adapter.notifyDataSetChanged();
                    j++;
                }
            }
        }
    }
}


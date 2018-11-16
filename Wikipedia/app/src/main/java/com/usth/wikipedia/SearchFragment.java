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

import javax.net.ssl.HttpsURLConnection;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener {

    private ListView list;
    private ListViewAdapter adapter;
    private SearchView searchView;
    private String[] articleNameList;
    private Bitmap[] articleImageList;
    private String[] articleDescriptionList;
    private ArrayList<ArticleRepo> arrayList = new ArrayList<>(5);

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_search, container, false);



        // ListView
        list = view.findViewById(R.id.list_article);

        final ArticleRepo example = new ArticleRepo("Example", "Ex", BitmapFactory.decodeResource(getResources(), R.drawable.no_img));
        arrayList.add(example); // Add a temporary item in arrayList to prevent null array

        adapter = new ListViewAdapter(view.getContext(), arrayList);
        adapter.filter(); // Clear arrayList

        // Binds the Adapter to the ListView
        list.setAdapter(adapter);

        searchView = view.findViewById(R.id.search_view);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);

        return view;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        adapter.filter(); // Clear
        SearchArticleTask process = new SearchArticleTask(); // Search article in background
        process.execute(query);
        searchView.clearFocus();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.filter();
        return true;
    }


    private class SearchArticleTask extends AsyncTask<String, Void, Boolean> {
        private String parseContent;
        private URL originURL;

        private class Info {
            private String desc;
            private Bitmap bmp;

            public Info(Bitmap bmp, String desc) {
                this.desc = desc;
                this.bmp = bmp;
            }

            public Bitmap getBmp() {
                return bmp;
            }

            public String getDesc() {
                return desc;
            }
        }

        private Info parseInfo(String title) {
            Info info;
            String json, desc = "";
            URL originURL;
            URL imageUrl;
            Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.no_img);
            JSONObject idJSONObject;
            try {
                originURL = new URL("https://en.wikipedia.org/w/api.php?format=json&action=query&prop=pageimages|description&exintro&explaintext&redirects=1&piprop=thumbnail&pithumbsize=200&titles="+title);
                HttpURLConnection httpURLConnection =(HttpURLConnection) originURL.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader((new InputStreamReader(inputStream)));
                String line = "";
                json = "";
                while (line != null) {
                    line = bufferedReader.readLine();
                    json = json + line;
                }
                JSONObject JO = new JSONObject(json);
                JSONObject query = JO.getJSONObject("query");
                JSONObject pages = query.getJSONObject("pages");
                Iterator<String> keys = pages.keys();
                String id = keys.next();
                idJSONObject = pages.getJSONObject(id);
                if (!idJSONObject.isNull("thumbnail")) {
                    JSONObject thumbnail = idJSONObject.getJSONObject("thumbnail");
                    imageUrl = new URL(thumbnail.getString("source"));
                    image = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
                }

                if (!idJSONObject.isNull("description")) {
                    desc = idJSONObject.getString("description");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            info = new Info(image, desc);
            return info;
        }



        protected void onPreExecute() {
            articleNameList = new String[5];
            articleDescriptionList = new String[5];
            articleImageList = new Bitmap[5];
        }

        protected Boolean doInBackground(String... titles) {
            String articleTitle = titles[0];
            try {
                originURL = new URL("https://en.wikipedia.org/w/api.php?format=json&action=opensearch&limit=5&namespace=0&profile=engine_autoselect&search="+articleTitle);
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) originURL.openConnection();
//                httpsURLConnection.setRequestMethod("GET");
//                httpsURLConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:63.0) Gecko/20100101 Firefox/63.0");
                InputStream inputStream = httpsURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader((new InputStreamReader(inputStream)));
                String line = "";
                parseContent = "";
                while (line != null) {
                    line = bufferedReader.readLine();
                    parseContent = parseContent + line;
                }
                JSONArray JO = new JSONArray(parseContent);
                JSONArray title = JO.getJSONArray(1);
                for (int i = 0; i < title.length(); i++) {
                    articleNameList[i] = title.getString(i);
                    Info info = parseInfo(articleNameList[i]);
                    articleImageList[i] = info.getBmp();
                    articleDescriptionList[i] = info.getDesc();
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
                Toast.makeText(getActivity(), "No Result", Toast.LENGTH_SHORT).show();
            } else {
                int j = 0;
                while (j < 5) {
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


package com.usth.wikipedia;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class ArticleFragment extends Fragment {
    private String category;
    private TextView title;
    private TextView content, full_article;
    private ImageView image;
    private ImageButton save;
    private String titleTemp;
    private Calendar cal;
    private DateFormat dateFormat;
    private String today[], yesterday[];
    private String parseTitle; // Key exchange

    public ArticleFragment() {
        // Required empty public constructor
    }

    public static ArticleFragment newInstance(String category) {
        ArticleFragment articleFragment = new ArticleFragment();
        Bundle args = new Bundle();
        args.putString("category", category);
        articleFragment.setArguments(args);
        return articleFragment;
    }

    public static ArticleFragment newInstance(String category, String titleTemp) {
        ArticleFragment articleFragment = new ArticleFragment();
        Bundle args = new Bundle();
        args.putString("category", category);
        args.putString("titleTemp", titleTemp);
        articleFragment.setArguments(args);
        return articleFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.category = getArguments().getString("category");
        this.titleTemp = getArguments().getString("titleTemp");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_article, container, false);
        title = view.findViewById(R.id.title);
        content = view.findViewById(R.id.content);
        image = view.findViewById(R.id.image);
        full_article = view.findViewById(R.id.full_article);
        full_article.setText(R.string.full_article);

        ShowArticleTask process = new ShowArticleTask();
        process.execute(category, titleTemp);

        // Send Key to Detail Article Activity
        full_article.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                full_article.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                Intent intent = new Intent(view.getContext(), DetailArticleActivity.class);
                intent.putExtra(DetailArticleActivity.EXTRA_ARTICLETITLE, parseTitle );
                startActivity(intent);
            }
        });
        return view;
    }

    private class ShowArticleTask extends AsyncTask<String, Void, Boolean> {
        private String parseContent;
        private URL parseImage, originURL;
        private Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.no_img);
        private String title1, title2, title3;

        protected void FindTitle() {
            try {
                JSONObject JO = new JSONObject(parseContent);
                JSONArray items = JO.getJSONArray("items");
                JSONObject articlesObject = items.getJSONObject(0);
                JSONArray articlesArray = articlesObject.getJSONArray("articles");
                JSONObject Article1 = articlesArray.getJSONObject(984);
                JSONObject Article2 = articlesArray.getJSONObject(985);
                JSONObject Article3 = articlesArray.getJSONObject(986);

                title1 = Article1.getString("article");
                title2 = Article2.getString("article");
                title3 = Article3.getString("article");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        protected void ParseContent(URL url) {
            try {
                URLConnection httpURLConnection = url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                parseContent = "";
                while (line != null) {
                    line = bufferedReader.readLine();
                    parseContent = parseContent + line;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        protected Date getDate(int d) {
            cal = Calendar.getInstance();
            cal.add(Calendar.DATE, d);
            return cal.getTime();
        }

        protected void onPreExecute(){
            dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            today = dateFormat.format(getDate(-2)).split("/");
            yesterday = dateFormat.format(getDate(-3)).split("/");
        }

        protected Boolean doInBackground(String... articles) {
            String articleCategory = articles[0];
            String articleTitle = articles[1];
            String url = "https://en.wikipedia.org/w/api.php?format=json&action=query&prop=extracts|pageimages|description|pageterms|categories&exintro&explaintext&redirects=1&piprop=thumbnail&pithumbsize=600&titles=";
            try {
                switch (articleCategory) {
                    case "random":
                        originURL = new URL("https://en.wikipedia.org/w/api.php?%20format=json&action=query&prop=extracts|pageimages|pageterms|categories&exsentences=20&exintro&explaintext=&generator=random&grnnamespace=0&piprop=thumbnail&pithumbsize=600");
                        break;
                    case "today":
                        originURL = new URL("https://wikimedia.org/api/rest_v1/metrics/pageviews/top/en.wikipedia/all-access/" + today[0] + "/" + today[1] + "/" + today[2]);
                        ParseContent(originURL); // parse json content from origin url
                        FindTitle(); // find article's title in json content
                        if (articleTitle.equals("article1")) {
                            originURL = new URL(url + title1);
                        } else if (articleTitle.equals("article2")) {
                            originURL = new URL(url + title2);
                        } else if (articleTitle.equals("article3")) {
                            originURL = new URL(url + title3);
                        }
                        break;
                    case "yesterday":
                        originURL = new URL("https://wikimedia.org/api/rest_v1/metrics/pageviews/top/en.wikipedia/all-access/" + yesterday[0] + "/" + yesterday[1] + "/" + yesterday[2]);
                        ParseContent(originURL); // parse json content from origin url
                        FindTitle(); // find article's title in json content
                        if (articleTitle.equals("article1")) {
                            originURL = new URL(url + title1);
                        } else if (articleTitle.equals("article2")) {
                            originURL = new URL(url + title2);
                        } else if (articleTitle.equals("article3")) {
                            originURL = new URL(url + title3);
                        }
                }
                ParseContent(originURL);
                JSONObject JO = new JSONObject(parseContent);
                JSONObject query = JO.getJSONObject("query");
                JSONObject pages = query.getJSONObject("pages");
                Iterator<String> keys = pages.keys();
                String id = keys.next();
                JSONObject idJSONObject = pages.getJSONObject(id);

                if (!idJSONObject.isNull("thumbnail")) {
                    JSONObject thumbnail = idJSONObject.getJSONObject("thumbnail");
                    parseImage = new URL(thumbnail.getString("source"));
                    bmp = BitmapFactory.decodeStream(parseImage.openConnection().getInputStream());
                }

                if (!idJSONObject.isNull("title")) {
                    parseTitle = idJSONObject.getString("title");
                } else {
                    parseTitle = "No title";
                }

                if(!idJSONObject.isNull("extract")) {
                    parseContent = idJSONObject.getString("extract");
                } else {
                    parseContent = "There is no information";
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        protected void onPostExecute(Boolean success) {
            if (!success) {
                Toast.makeText(getActivity(), "Error network", Toast.LENGTH_SHORT).show();
            } else {
                title.setText(parseTitle);
                content.setText(parseContent);
                image.setImageBitmap(bmp);

                titleTemp = parseTitle;
            }
        }
    }


}

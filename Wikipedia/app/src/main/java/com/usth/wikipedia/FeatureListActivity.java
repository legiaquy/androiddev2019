package com.usth.wikipedia;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FeatureListActivity extends ListActivity {
    private String[] articleNameList;
    private List<String> articleList;
    private ArrayAdapter<String> listDataAdapter;
    private TextView header;
    public static final String EXTRA_FEATURE = "feature";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feature_list);
        header = findViewById(R.id.feature_name);
        header.setText(getIntent().getExtras().get(EXTRA_FEATURE).toString().toUpperCase(Locale.getDefault()));
        articleList = new ArrayList<>();
        String temp = "";
        articleList.add(temp); // Prevent null list
        listDataAdapter = new ArrayAdapter<>(this, R.layout.feature_item_list, R.id.feature_item_name, articleList);
        this.setListAdapter(listDataAdapter);
        new FindArticleTask().execute(getIntent().getExtras().get(EXTRA_FEATURE).toString());
    }

    @Override
    protected void onListItemClick(ListView listView, View v, int position, long id) {
        ListAdapter listAdapter = listView.getAdapter();
        Object selectItemObj = listAdapter.getItem(position);
        Intent intent = new Intent(FeatureListActivity.this, DetailArticleActivity.class);
        intent.putExtra(DetailArticleActivity.EXTRA_ARTICLETITLE, selectItemObj.toString());
        startActivity(intent);
    }

    private class FindArticleTask extends AsyncTask<String, Void, Boolean> {
        String parseContent;
        URL url;
        Calendar cal;
        DateFormat dateFormat;
        String today[], yesterday[];

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
            today = dateFormat.format(getDate(-1)).split("/");
            yesterday = dateFormat.format(getDate(-2)).split("/");
            articleNameList = new String[100];
            articleList.clear(); // Clear temp
        }

        protected Boolean doInBackground(String... articles) {
            String articleFeature = articles[0];

            try {
                switch (articleFeature) {
                    case "random":
                        url = new URL("https://en.wikipedia.org/w/api.php?format=json&action=query&list=random&rnlimit=100&rnnamespace=0");
                        break;
                    case "today":
                        url = new URL("https://wikimedia.org/api/rest_v1/metrics/pageviews/top/en.wikipedia/all-access/" + today[0] + "/" + today[1] + "/" + today[2]);
                        break;
                    case "yesterday":
                        url = new URL("https://wikimedia.org/api/rest_v1/metrics/pageviews/top/en.wikipedia/all-access/" + yesterday[0] + "/" + yesterday[1] + "/" + yesterday[2]);
                        break;
                }

                ParseContent(url);
                if(articleFeature.equals("random")) {
                    JSONObject JO = new JSONObject(parseContent);
                    JSONObject query = JO.getJSONObject("query");
                    JSONArray random = query.getJSONArray("random");
                    for (int i = 0; i < 100; i++) {
                        JSONObject temp = random.getJSONObject(i);
                        articleNameList[i] = temp.getString("title");
                    }
                } else {
                    JSONObject JO = new JSONObject(parseContent);
                    JSONArray items = JO.getJSONArray("items");
                    JSONObject list = items.getJSONObject(0);
                    JSONArray articlesArray = list.getJSONArray("articles");
                    for (int i = 0; i < 100; i++) {
                        JSONObject temp = articlesArray.getJSONObject(988 - i);
                        articleNameList[i] = temp.getString("article");
                    }
                }
            } catch (MalformedURLException e) {
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
                Toast.makeText(FeatureListActivity.this, "Error Network", Toast.LENGTH_LONG).show();
            } else {
                for (int i = 0; i < 100; i++) {
                    articleList.add(articleNameList[i].replace('_', ' '));
                }
                listDataAdapter.notifyDataSetChanged();
            }
        }
    }
}

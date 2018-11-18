package com.usth.wikipedia;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
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
import java.util.Iterator;


public class DetailArticleActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String EXTRA_ARTICLETITLE = "articleTitle";
    private EditText detail_content_article;
    private ImageButton edit_button;
    private ImageView detail_image_article;
    private TextView detail_title_article, detail_category_article, detail_alias_article, detail_description_article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_article);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        getWindow().setLayout(width, height);


        detail_title_article = findViewById(R.id.detail_title_article);
        detail_category_article = findViewById(R.id.detail_category_article);
        detail_alias_article = findViewById(R.id.detail_alias_article);
        detail_content_article = findViewById(R.id.detail_content_article);
        detail_description_article = findViewById(R.id.detail_description_article);
        detail_image_article = findViewById(R.id.detail_image_article);


        String articleTitle = (String) getIntent().getExtras().get(EXTRA_ARTICLETITLE); // Get article's row ID
        ShowArticleTask process = new ShowArticleTask();
        process.execute(articleTitle); // background thread to find and display detail article from database

        // Edit button

        detail_content_article.setEnabled(false);
        detail_description_article.setEnabled(false);
        edit_button = findViewById(R.id.edit_button);
        edit_button.setOnClickListener(this);

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

    // This method is called when edit button is clicked
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_button:
                enableDisableEditText();
                break;
            default:
                break;
        }
    }

    // Method for enable or disable edit text
    private void enableDisableEditText() {
        if (detail_content_article.isEnabled()) {
            detail_content_article.setEnabled(false);
            edit_button.setImageResource(R.drawable.ic_edit_black_24dp);
//            String articleTitle = (String) getIntent().getExtras().get(EXTRA_ARTICLETITLE);
//            new UpdateContentTask().execute(articleTitle);
        } else {
            detail_content_article.setEnabled(true);
            edit_button.setImageResource(R.drawable.ic_done_black_24dp);
        }
    }


    private class ShowArticleTask extends AsyncTask<String, Void, Boolean> {

        private String parseContent, parseDescription, parseTitle, parseCategory, parseAlias;
        private URL parseImage, originURL;
        private Bitmap bmp;

        protected Boolean doInBackground(String... articles) { // "Integer... articles": Array parameter
            String articleTitle = articles[0];
            try {
                // Connect to wikipedia API
                originURL = new URL("https://en.wikipedia.org/w/api.php?format=json&action=query&prop=extracts|pageimages|description|pageterms|categories&exsectionformat=raw&explaintext&redirects=1&piprop=thumbnail&pithumbsize=600&titles="+articleTitle);
                HttpURLConnection httpURLConnection = (HttpURLConnection) originURL.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                parseContent = "";
                while(line != null) {
                    line = bufferedReader.readLine();
                    parseContent = parseContent + line;
                }

                // Get content, image, description object
                JSONObject JO = new JSONObject(parseContent);
                JSONObject query = JO.getJSONObject("query");
                JSONObject pages = query.getJSONObject("pages");
                Iterator<String> keys = pages.keys();
                String id = keys.next();
                JSONObject idJSONObject = pages.getJSONObject(id);




                // Parse content, image, description
                parseTitle = idJSONObject.getString("title");
                parseContent = idJSONObject.getString("extract");
                if(!idJSONObject.isNull("thumbnail")) {
                    JSONObject thumbnail = idJSONObject.getJSONObject("thumbnail");
                    parseImage = new URL(thumbnail.getString("source"));
                    bmp = BitmapFactory.decodeStream(parseImage.openConnection().getInputStream());
                } else {
                    bmp = BitmapFactory.decodeResource(getResources(), R.drawable.no_img);
                }
                if(!idJSONObject.isNull("categories")) {
                    JSONArray categories = idJSONObject.getJSONArray("categories");
                    JSONObject catObject = categories.getJSONObject(0);
                    parseCategory = catObject.optString("title");
                } else {
                    parseCategory = "";
                }
                if(!idJSONObject.isNull("description")) {
                    parseDescription = idJSONObject.getString("description");
                } else {
                    parseDescription = "None";
                }
                JSONObject terms = idJSONObject.getJSONObject("terms");

                if(!terms.isNull("alias")) {
                    JSONArray alias = terms.getJSONArray("alias");
                    parseAlias = alias.getString(0);
                } else {
                    parseAlias = "";
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
            Toast.makeText(DetailArticleActivity.this, "Error network", Toast.LENGTH_SHORT).show();
            } else {
                detail_title_article.setText(parseTitle);

                detail_category_article.setText(parseCategory);

                detail_alias_article.setText(parseAlias);

                detail_content_article.setText(parseContent);

                detail_image_article.setImageBitmap(bmp);

                detail_description_article.setText(parseDescription);

            }
        }
    }


    // Update the database after editing
//    private class UpdateContentTask extends AsyncTask<String, Void, Boolean> {
//
//
//        protected void onPreExecute() {
//
//        }
//
//        protected Boolean doInBackground(String... articles) {
//            String articleTitle = articles[0];
//            try {
//
//            }
//        }
//
//        protected void onPostExecute(Boolean success) {
//            if (!success) {
//
//            }
//        }
//    }

    protected void onRestart() {
        super.onRestart();
    }

    protected void onDestroy() {
        super.onDestroy();
    }
}


package com.usth.wikipedia;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class DetailArticleActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String EXTRA_ARTICLENO = "articleNo";
    EditText detail_content_article;
    ImageButton edit_button;
    private Cursor articleCursor;
    private SQLiteDatabase db;
    private SQLiteOpenHelper wikipediaDatabaseHelper = new WikipediaDatabaseHelper(DetailArticleActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_article);

        int articleNo = (Integer) getIntent().getExtras().get(EXTRA_ARTICLENO); // Get article's row ID
        new ShowArticleTask().execute(articleNo); // background thread to find and display detail article from database

        // Edit button
        detail_content_article = findViewById(R.id.detail_content_article);
        detail_content_article.setEnabled(false);
        edit_button = findViewById(R.id.edit_button);
        edit_button.setOnClickListener(this);
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
            int articleNo = (Integer) getIntent().getExtras().get(EXTRA_ARTICLENO);
            new UpdateContentTask().execute(articleNo);
        } else {
            detail_content_article.setEnabled(true);
            edit_button.setImageResource(R.drawable.ic_done_black_24dp);
        }
    }


    // Background thread to find & display detail article from database
    private class ShowArticleTask extends AsyncTask<Integer, Void, Boolean> {

        protected Boolean doInBackground(Integer... articles) { // "Integer... articles": Array parameter
            int articleNo = articles[0];
            try {
                db = wikipediaDatabaseHelper.getReadableDatabase();

                // Use cursor to query related article
                articleCursor = db.query(
                                "ARTICLE",
                                new String[]{"TITLE", "CONTENT", "IMAGE_RESOURCE_ID"},
                                "_id = ?",
                                new String[] {Integer.toString(articleNo)},
                                null, null, null);

                // Although there is only one query, still need to navigate the cursor
                if (articleCursor.moveToFirst()) {

                    //Get the article's detail from the cursor
                    String titleText = articleCursor.getString(0);
                    String contentText = articleCursor.getString(1);
                    int imageId = articleCursor.getInt(2);

                    // Show the article's image
                    ImageView detail_image_article = findViewById(R.id.detail_image_article);
                    detail_image_article.setImageResource(imageId);
                    detail_image_article.setContentDescription(titleText);

                    // Show the article's title
                    TextView detail_title_article = findViewById(R.id.detail_title_article);
                    detail_title_article.setText(titleText);

                    // Show the article's content
                    EditText detail_content_article = findViewById(R.id.detail_content_article);
                    detail_content_article.setText(contentText);
                }
                db.close();
                return true;
            } catch (SQLiteException e) {
                return false;
            }
        }

        protected void onPostExecute(Boolean success) {
            if(!success) {
                Toast toast = Toast.makeText(DetailArticleActivity.this, "Get article from database unavailable", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    // Update the database after editing
    private class UpdateContentTask extends AsyncTask<Integer, Void, Boolean> {
        ContentValues articleValues;

        protected void onPreExecute() {
            EditText contentText = findViewById(R.id.detail_content_article);
            articleValues = new ContentValues();
            articleValues.put("CONTENT", contentText.getText().toString());
        }

        protected Boolean doInBackground(Integer... articles) {
            int articleNo = articles[0];
            try {
                db = wikipediaDatabaseHelper.getWritableDatabase();
                db.update("ARTICLE", articleValues, "_id = ?", new String[]{Integer.toString(articleNo)});
                return true;
            } catch (SQLiteException e) {
                return false;
            }
        }

        protected void onPostExecute(Boolean success) {
            if (!success) {
                Toast toast = Toast.makeText(DetailArticleActivity.this, "Update content in database unavailable", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    protected void onRestart() {
        super.onRestart();
    }

    protected void onDestroy() {
        super.onDestroy();
        db.close();
        articleCursor.close();
    }
}


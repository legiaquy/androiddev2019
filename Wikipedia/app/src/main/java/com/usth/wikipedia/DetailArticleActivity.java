package com.usth.wikipedia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class DetailArticleActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String EXTRA_ARTICLENO = "articleNo";
    EditText detail_content_article;
    ImageView detail_image_article;
    TextView detail_title_article;
    ImageButton edit_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_article);
        detail_content_article = findViewById(R.id.detail_content_article);
        detail_image_article = findViewById(R.id.detail_image_article);
        detail_title_article = findViewById(R.id.detail_title_article);
        edit_button = findViewById(R.id.edit_button);

        int articleNo = (Integer) getIntent().getExtras().get(EXTRA_ARTICLENO); // Get article's position
        ModelObject selectedArticle = ModelObject.values()[articleNo]; // Get the selected article from ModelObject

        detail_image_article.setImageResource(selectedArticle.getmImageResId());

        detail_title_article.setText(selectedArticle.getmTitleResId());

        switch (articleNo){
            case 0:
                detail_content_article.setText("Detail article 1");
                break;
            case 1:
                detail_content_article.setText("Detail article 2");
                break;
            case 2:
                detail_content_article.setText("Detail article 3");
        }

        detail_content_article.setEnabled(false);

        edit_button.setOnClickListener(this);
    }

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

    //method for enable or disable editttext
    private void enableDisableEditText() {
        if (detail_content_article.isEnabled()) {
            detail_content_article.setEnabled(false);
            edit_button.setImageResource(R.drawable.ic_edit_black_24dp);
        } else {
            detail_content_article.setEnabled(true);
            edit_button.setImageResource(R.drawable.ic_done_black_24dp);
        }
    }


}

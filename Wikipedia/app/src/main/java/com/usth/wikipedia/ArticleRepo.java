package com.usth.wikipedia;

import android.graphics.Bitmap;

public class ArticleRepo {
    private String articleName;
    private String articleDescription;
    private Bitmap articleImage;

    public ArticleRepo(String articleName, String articleDescription, Bitmap articleImage){
        this.articleName = articleName;
        this.articleDescription = articleDescription;
        this.articleImage = articleImage;
    }

    public String getName() {
        return this.articleName;
    }

    public String getDescription() {
        return this.articleDescription;
    }

    public Bitmap getImage() {
        return this.articleImage;
    }
}


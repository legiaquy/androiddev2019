package com.usth.wikipedia;

class ModelArticle {
    private String articleTitle;
    private int articleId;
    private int overview_layoutId;

    public ModelArticle(int articleId, String articleTitle, int overview_layoutId) {
        super();
        this.articleTitle = articleTitle;
        this.articleId = articleId;
        this.overview_layoutId = overview_layoutId;
    }

    public String getArticleTitle(){
        return articleTitle;
    }

    public int getArticleId(){
        return articleId;
    }

    public int getOverview_layoutId() {
        return overview_layoutId;
    }

    @Override
    public String toString() {
        return "Article@[Title = "+ articleTitle + ", ID=" + articleId +"]";
    }
}

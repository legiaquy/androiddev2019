package com.usth.wikipedia;

public enum ModelObject {
    ARTICLE1(R.string.title_article1, R.layout.article1, R.drawable.image_article1),
    ARTICLE2(R.string.title_article2, R.layout.article2, R.drawable.image_article2),
    ARTICLE3(R.string.title_article3, R.layout.article3, R.drawable.image_article3);

    private int mTitleResId;
    private int mLayoutResId;
    private int mImageResId;

    ModelObject(int titleResId, int layoutResId, int imageResId) {
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
        mImageResId = imageResId;
    }

    public int getmTitleResId() {
        return mTitleResId;
    }

    public int getmLayoutResId() {
        return mLayoutResId;
    }

    public int getmImageResId() { return mImageResId; }

}

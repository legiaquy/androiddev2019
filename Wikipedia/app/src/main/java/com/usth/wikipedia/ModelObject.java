package com.usth.wikipedia;

public enum ModelObject {
    TODAY(R.string.today, R.layout.view_today),
    YESTERDAY(R.string.yesterday, R.layout.view_yesterday),
    RANDOM_ARTICLE(R.string.random, R.layout.view_random_article);

    private int mTitleResId;
    private int mLayoutResId;

    ModelObject(int titleResId, int layoutResId) {
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
    }

    public int getmTitleResId() {
        return mTitleResId;
    }

    public int getmLayoutResId() {
        return mLayoutResId;
    }
}

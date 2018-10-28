package com.usth.wikipedia;

public enum ModelObject {
    ARTICLE1(R.string.article1, R.layout.article1),
    ARTICLE2(R.string.article2, R.layout.article2),
    ARTICLE3(R.string.article3, R.layout.article3);

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

package com.usth.wikipedia;

public enum ModelObject {
    ARTICLE1(R.string.title_article1, R.layout.world_series, R.drawable.world_series),
    ARTICLE2(R.string.title_article2, R.layout.animal, R.drawable.animal),
    ARTICLE3(R.string.title_article3, R.layout.statue_of_liberty, R.drawable.statue_of_liberty);

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

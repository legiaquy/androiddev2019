package com.usth.wikipedia;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class CustomPagerAdapter extends PagerAdapter {

    private Context mContext;
    private ArrayList<ModelArticle> mListArticle;
    public CustomPagerAdapter(Context context, ArrayList listArticle)
    {
        mContext = context;
        mListArticle = listArticle;
    }

    @Override
    public Object instantiateItem(final ViewGroup collection, final int position) {
        final ModelArticle article = mListArticle.get(position);
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(article.getOverview_layoutId(), collection, false);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailArticleActivity.class);
                intent.putExtra(DetailArticleActivity.EXTRA_ARTICLENO, article.getArticleId());
                v.getContext().startActivity(intent);
            }
        });
        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount(){
        return 3;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        ModelArticle customPagerEnum = mListArticle.get(position);
        return customPagerEnum.getArticleTitle();
    }
}

package com.usth.wikipedia;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<ArticleRepo> mArticleRepo;
    ArrayList<ArticleRepo> mArrayList;

    public ListViewAdapter(Context context, ArrayList<ArticleRepo> articleRepo) {
        mContext = context;
        mArticleRepo = articleRepo;
        mArrayList = new ArrayList<>();
        mArrayList.addAll(articleRepo);
    }


    @Override
    public int getCount() {
        return mArticleRepo.size();
    }

    @Override
    public ArticleRepo getItem(int position) {
        return mArticleRepo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        View row;
        if (view == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(R.layout.article_list, parent, false);
        } else {
            row = view;
        }
        //Set the results into ListView
        TextView mName = row.findViewById(R.id.list_title);
        TextView mDescription = row.findViewById(R.id.list_description);
        ImageView mImage = row.findViewById(R.id.list_image);
        mName.setText(mArticleRepo.get(position).getName());
        mDescription.setText(mArticleRepo.get(position).getDescription());
        mImage.setImageBitmap(mArticleRepo.get(position).getImage());
        return row;
    }
}

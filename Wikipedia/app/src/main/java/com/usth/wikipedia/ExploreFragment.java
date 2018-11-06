package com.usth.wikipedia;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;


public class ExploreFragment extends Fragment {

    private SearchView searchView;
    private ViewPager viewPager1, viewPager2, viewPager3;
    private ArrayList<ModelArticle> todayListArticle = new ArrayList<>();
    private ArrayList<ModelArticle> yesterdayListArticle = new ArrayList<>();
    private ArrayList<ModelArticle> randomListArticle = new ArrayList<>();
    private Cursor articleCursor;
    private SQLiteOpenHelper wikipediaDatabaseHelper;
    private SQLiteDatabase db;

    public ExploreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_explore, container, false);
        searchView = layout.findViewById(R.id.search_view);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(inflater.getContext(), query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Toast.makeText(inflater.getContext(), newText, Toast.LENGTH_LONG).show();
                return false;
            }
        });
        FindArticletask findArticletask = new FindArticletask();
        findArticletask.execute(); // background thread to find new Article

        viewPager1 = layout.findViewById(R.id.viewpager1);
        viewPager1.setAdapter(new CustomPagerAdapter(inflater.getContext(), todayListArticle));

        viewPager2 = layout.findViewById(R.id.viewpager2);
        viewPager2.setAdapter(new CustomPagerAdapter(inflater.getContext(), yesterdayListArticle));

        viewPager3 = layout.findViewById(R.id.viewpager3);
        viewPager3.setAdapter(new CustomPagerAdapter(inflater.getContext(), randomListArticle));


        return layout;
    }

    private class FindArticletask extends AsyncTask<Void, Void, Boolean> {

        protected Boolean doInBackground(Void ...params) {
            try {
                wikipediaDatabaseHelper = new WikipediaDatabaseHelper(getActivity());
                db = wikipediaDatabaseHelper.getReadableDatabase();
                String query = "SELECT _id, TITLE, OVERVIEW_LAYOUT_RESOURCE_ID from ARTICLE order by _id DESC limit 3";
                articleCursor = db.rawQuery(query,null);
                    if (articleCursor.moveToFirst()) {
                        ModelArticle article1 = new ModelArticle(articleCursor.getInt(0), articleCursor.getString(1), articleCursor.getInt(2));
                        todayListArticle.add(0,article1);
                        articleCursor.moveToNext();
                        ModelArticle article2 = new ModelArticle(articleCursor.getInt(0), articleCursor.getString(1), articleCursor.getInt(2));
                        todayListArticle.add(1,article2);
                        articleCursor.moveToNext();
                        ModelArticle article3 = new ModelArticle(articleCursor.getInt(0), articleCursor.getString(1), articleCursor.getInt(2));
                        todayListArticle.add(2,article3);

                        yesterdayListArticle.add(0, article2);
                        yesterdayListArticle.add(1, article3);
                        yesterdayListArticle.add(2, article1);
                    }
                    articleCursor.close();
                String random_query = "SELECT _id, TITLE, OVERVIEW_LAYOUT_RESOURCE_ID from ARTICLE order by RANDOM() LIMIT 3";
                articleCursor = db.rawQuery(random_query, null);
                if (articleCursor.moveToFirst()) {
                    ModelArticle article1 = new ModelArticle(articleCursor.getInt(0), articleCursor.getString(1), articleCursor.getInt(2));
                    randomListArticle.add(0,article1);
                    articleCursor.moveToNext();
                    ModelArticle article2 = new ModelArticle(articleCursor.getInt(0), articleCursor.getString(1), articleCursor.getInt(2));
                    randomListArticle.add(1,article2);
                    articleCursor.moveToNext();
                    ModelArticle article3 = new ModelArticle(articleCursor.getInt(0), articleCursor.getString(1), articleCursor.getInt(2));
                    randomListArticle.add(2,article3);
                }
                    articleCursor.close();
                    db.close();
                    return true;
                } catch (SQLiteException e) {
                    return false;
            }
        }

        protected void onPostExecute(Boolean success) {
            if(!success) {
                Toast toast = Toast.makeText(getActivity(), "Get article from database unavailable", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}

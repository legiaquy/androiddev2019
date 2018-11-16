package com.usth.wikipedia;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;


public class ExploreFragment extends Fragment {

    private ViewPager viewPager1, viewPager2, viewPager3;
    private FragmentPagerAdapter adapter1, adapter2, adapter3;
    private String[] articleNameList, articleDescriptionList;
    private Bitmap[] articleImageList;
    private ArrayList<ArticleRepo> arrayList = new ArrayList<>(5);
    private SearchView searchView;
    private SearchView.SearchAutoComplete searchAutoComplete;
    private SearchAdapter searchAdapter;

    public ExploreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            adapter1 = new ArticlePagerAdapter(getActivity().getSupportFragmentManager(), "today");
            adapter2 = new ArticlePagerAdapter(getActivity().getSupportFragmentManager(), "yesterday");
            adapter3 = new ArticlePagerAdapter(getActivity().getSupportFragmentManager(), "random");
        } else {

        }
        Toast.makeText(getActivity(), "Ready", Toast.LENGTH_SHORT).show();
        // adapter for the search dropdown auto suggest
    }

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View layout = inflater.inflate(R.layout.fragment_explore, container, false);

        searchView = layout.findViewById(R.id.auto_complete_search);
        searchAutoComplete = (SearchView.SearchAutoComplete) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setDropDownBackgroundResource(R.drawable.article_list_backgroundcolor);
        searchAutoComplete.setThreshold(1);
        searchAutoComplete.setDropDownAnchor(R.id.auto_complete_search);


        ArticleRepo temp = new ArticleRepo("");
        arrayList.add(temp);

        searchAdapter = new SearchAdapter(layout.getContext(), R.layout.explore_search_list, arrayList);

        searchAutoComplete.setAdapter(searchAdapter);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Intent intent = new Intent(layout.getContext(), DetailArticleActivity.class);
                ArticleRepo articleRepo = arrayList.get(0);
                intent.putExtra(DetailArticleActivity.EXTRA_ARTICLETITLE, articleRepo.getName());
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchAdapter.filter();
                new SearchArticleTask().execute(s);
                return false;
            }
        });
        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), DetailArticleActivity.class);
                ArticleRepo article = (ArticleRepo) parent.getItemAtPosition(position);
                intent.putExtra(DetailArticleActivity.EXTRA_ARTICLETITLE, article.getName());
                startActivity(intent);
            }
        });

        viewPager3 = layout.findViewById(R.id.viewpager3);
        viewPager3.setOffscreenPageLimit(2);
        viewPager3.setAdapter(adapter3);

        viewPager1 = layout.findViewById(R.id.viewpager1);
        viewPager1.setOffscreenPageLimit(2);
        viewPager1.setAdapter(adapter1);

        viewPager2 = layout.findViewById(R.id.viewpager2);
        viewPager2.setOffscreenPageLimit(2);
        viewPager2.setAdapter(adapter2);

        return layout;
    }


    // ViewPager's adapter
    public static class ArticlePagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 3;
        private String category;

        public ArticlePagerAdapter(FragmentManager fm, String category) {
            super(fm);
            this.category = category;
        }


        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            switch (category) {
                case "random":
                    switch (position) {
                        case 0:
                            return ArticleFragment.newInstance(category);
                        case 1:
                            return ArticleFragment.newInstance(category);
                        case 2:
                            return ArticleFragment.newInstance(category);
                        default:
                            return null;
                    }
                case "today":
                    switch (position) {
                        case 0:
                            return ArticleFragment.newInstance(category, "article1");
                        case 1:
                            return ArticleFragment.newInstance(category, "article2");
                        case 2:
                            return ArticleFragment.newInstance(category, "article3");
                        default:
                            return null;
                    }
                case "yesterday":
                    switch (position) {
                        case 0:
                            return ArticleFragment.newInstance(category, "article1");
                        case 1:
                            return ArticleFragment.newInstance(category, "article2");
                        case 2:
                            return ArticleFragment.newInstance(category, "article3");
                        default:
                            return null;
                    }
                default:
                    return null;
            }
        }

        @Override
        public long getItemId(int position) {
            return System.currentTimeMillis();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Article" + position;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        super.onSaveInstanceState(savedInstanceState);
    }

    // Search article
    private class SearchArticleTask extends AsyncTask<String, Void, Boolean> {
        private String parseContent;
        private URL originUrl;
        private URL parseImage;
        private Bitmap bmp;

        protected void onPreExecute() {
            articleNameList = new String[5];
            articleDescriptionList = new String[5];
        }

        protected Boolean doInBackground(String... titles) {
            String articleTitle = titles[0];
            try {
                originUrl = new URL("https://en.wikipedia.org/w/api.php?format=json&action=opensearch&limit=5&namespace=0&profile=engine_autoselect&search="+articleTitle);
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) originUrl.openConnection();
//                httpsURLConnection.setRequestMethod("GET");
//                httpsURLConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:63.0) Gecko/20100101 Firefox/63.0");
                InputStream inputStream = httpsURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader((new InputStreamReader(inputStream)));
                String line = "";
                parseContent = "";
                while (line != null) {
                    line = bufferedReader.readLine();
                    parseContent = parseContent + line;
                }
                JSONArray JO = new JSONArray(parseContent);
                JSONArray title = JO.getJSONArray(1);
                for (int i = 0; i < title.length(); i++) {
                    articleNameList[i] = title.getString(i);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }

        protected void onPostExecute(Boolean success) {
            if (!success) {
                Toast.makeText(getActivity(), "No result", Toast.LENGTH_SHORT).show();
            } else {
                for (int i = 0; i < articleNameList.length; i++) {
                    ArticleRepo articleRepo = new ArticleRepo(articleNameList[i]);
                    arrayList.add(articleRepo);
                    searchAdapter.notifyDataSetChanged();
                }
            }

        }
    }

    private class SearchAdapter extends ArrayAdapter<ArticleRepo> {
        private Context mContext;
        private int mResourceId;
        private List<ArticleRepo> mItems;

        public SearchAdapter(Context context, int resourceId, ArrayList<ArticleRepo> items) {
            super(context, resourceId, items);
            mContext = context;
            mItems = items;
            mResourceId = resourceId;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            try {
                if (convertView == null) {
                    LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
                    view = inflater.inflate(mResourceId, parent, false);
                }
                final ArticleRepo articleRepo = getItem(position);
                final TextView title = view.findViewById(R.id.explore_search_title);
//                LinearLayout item = view.findViewById(R.id.list_article);

                title.setText(articleRepo.getName());
//                item.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(v.getContext(), DetailArticleActivity.class);
//                        intent.putExtra(DetailArticleActivity.EXTRA_ARTICLETITLE, title.getText());
//                        startActivity(intent);
//                    }
//                });
            } catch (Exception e) {
                e.printStackTrace();
            }

            return view;
        }

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public ArticleRepo getItem(int position) {
            return mItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void filter() {
            mItems.clear();
            notifyDataSetChanged();
        }
    }
}


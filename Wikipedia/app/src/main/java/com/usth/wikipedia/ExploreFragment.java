package com.usth.wikipedia;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;


public class ExploreFragment extends Fragment {

    private SearchView searchView;
    private ViewPager viewPager1, viewPager2, viewPager3;
    private FragmentPagerAdapter adapter1, adapter2, adapter3;

    public ExploreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null ) {
            adapter1 = new ArticlePagerAdapter(getActivity().getSupportFragmentManager(), "today");
            adapter2 = new ArticlePagerAdapter(getActivity().getSupportFragmentManager(), "yesterday");
            adapter3 = new ArticlePagerAdapter(getActivity().getSupportFragmentManager(), "random");
        } else {

        }
        Toast.makeText(getActivity(), "Ready", Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View layout = inflater.inflate(R.layout.fragment_explore, container, false);
        // Inflate the layout for this fragment
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



    public static class ArticlePagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 3;
        private String category;

        public ArticlePagerAdapter(FragmentManager fm, String category){
            super(fm);
            this.category = category;
        }



        @Override
        public int getCount(){
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position){
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
        public long getItemId(int position){
            return System.currentTimeMillis();
        }

        @Override
        public CharSequence getPageTitle(int position){
            return "Article" + position;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        super.onSaveInstanceState(savedInstanceState);
    }
}

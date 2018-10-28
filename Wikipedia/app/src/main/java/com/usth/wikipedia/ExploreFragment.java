package com.usth.wikipedia;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;


public class ExploreFragment extends Fragment {

    SearchView searchView;
    ViewPager viewPager1, viewPager2, viewPager3;

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

        viewPager1 = layout.findViewById(R.id.viewpager1);
        viewPager1.setAdapter(new CustomPagerAdapter(inflater.getContext()));

        viewPager2 = layout.findViewById(R.id.viewpager2);
        viewPager2.setAdapter(new CustomPagerAdapter(inflater.getContext()));

        viewPager3 = layout.findViewById(R.id.viewpager3);
        viewPager3.setAdapter(new CustomPagerAdapter(inflater.getContext()));


        return layout;
    }

}

package com.usth.wikipedia;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExploreFragment extends Fragment {

    SearchView searchView;
    ViewPager viewPager;

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

        viewPager = layout.findViewById(R.id.viewpager);
        viewPager.setAdapter(new CustomPagerAdapter(inflater.getContext()));
        return layout;
    }

}

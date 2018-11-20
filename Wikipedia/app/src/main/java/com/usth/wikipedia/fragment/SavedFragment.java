package com.usth.wikipedia.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.usth.wikipedia.R;

import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class SavedFragment extends Fragment {
    String SHARED_PREFERENCES_NAME = "save_file";

    public SavedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);

        Map<String, ?> allEntries = sharedPreferences.getAll();
        for(Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Log.d("mhihi", entry.getKey());
        }

        return inflater.inflate(R.layout.fragment_saved, container, false);



//        new FindArticleTask().execute();
    }


}

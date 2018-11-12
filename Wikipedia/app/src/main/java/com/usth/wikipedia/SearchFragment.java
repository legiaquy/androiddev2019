package com.usth.wikipedia;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.zip.Inflater;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private CustomCursorAdapter cursorAdapter;
    private ListView listView;
    private Cursor cursor;
    private ArticleRepo articleRepo;
    private SearchView search;
    private final static String TAG = MainActivity.class.getName().toString();

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View layout = inflater.inflate(R.layout.fragment_search, container, false);
        articleRepo = new ArticleRepo(layout.getContext());
        cursorAdapter = new CustomCursorAdapter(layout.getContext(), cursor, 0 );
        listView = layout.findViewById(R.id.list_article);
        listView.setAdapter(cursorAdapter);
        SearchManager manager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        search = layout.findViewById(R.id.search_view);
        search.setSearchableInfo(manager.getSearchableInfo(getActivity().getComponentName()));
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String s) {
                cursor=articleRepo.getArticleListByKeyword(s);
                if(cursor==null){
                    Toast.makeText(layout.getContext(), "No records found!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(layout.getContext(), cursor.getCount() + "records found!", Toast.LENGTH_LONG).show();
                }

                cursorAdapter.swapCursor(cursor);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                cursor = articleRepo.getArticleListByKeyword(s);
                if(cursor!=null){
                    cursorAdapter.swapCursor(cursor);
                }
                return false;
            }
        });

        search.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                search.setQuery("", false);
                cursorAdapter.getFilter().filter(search.getQuery());
                cursorAdapter.notifyDataSetChanged();
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), DetailArticleActivity.class);
                TextView textId = view.findViewById(R.id.list_id);
                String articleId = textId.getText().toString();
//                intent.putExtra(DetailArticleActivity.EXTRA_ARTICLETITLE, Integer.parseInt(articleId));
                startActivity(intent);
            }
        });

        return layout;
    }

}

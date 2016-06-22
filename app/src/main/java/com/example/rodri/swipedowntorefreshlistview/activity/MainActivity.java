package com.example.rodri.swipedowntorefreshlistview.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.rodri.swipedowntorefreshlistview.R;
import com.example.rodri.swipedowntorefreshlistview.helper.Movie;
import com.example.rodri.swipedowntorefreshlistview.helper.SwipeListAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private String TAG = MainActivity.class.getSimpleName();

    private String URL_TOP_250_MOVIES = "http://api.androidhive.info/json/imdb_top_250.php?offset=";

    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView moviesListView;
    private SwipeListAdapter swipeListAdapter;
    private List<Movie> movies;

    // Starts with 0, but it will be increased when start parsing json data
    private int offSet = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeVariables();

        swipeRefreshLayout.setOnRefreshListener(this);

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);

                fetchMovies();
            }
        });
    }

    public void initializeVariables() {
        moviesListView = (ListView) findViewById(R.id.moviesList);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        movies = new ArrayList<>();
        swipeListAdapter = new SwipeListAdapter(this, 0, movies);
        moviesListView.setAdapter(swipeListAdapter);

    }

    /***
     *
     * This method will be called when swipe refresh is pulled down
     *
     */
    @Override
    public void onRefresh() {
        fetchMovies();
    }

    
}

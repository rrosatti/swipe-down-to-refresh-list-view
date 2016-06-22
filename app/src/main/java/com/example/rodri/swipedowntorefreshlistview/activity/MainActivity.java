package com.example.rodri.swipedowntorefreshlistview.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.rodri.swipedowntorefreshlistview.R;
import com.example.rodri.swipedowntorefreshlistview.app.MyApplication;
import com.example.rodri.swipedowntorefreshlistview.helper.Movie;
import com.example.rodri.swipedowntorefreshlistview.helper.SwipeListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    private void fetchMovies() {

        // set refresh animation before making http call
        swipeRefreshLayout.setRefreshing(true);

        // append offSet to the URL
        String url = URL_TOP_250_MOVIES + offSet;

        JsonArrayRequest arrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                if (response.length() > 0) {

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject movieObj = response.getJSONObject(i);

                            int rank = movieObj.getInt("rank");
                            String title = movieObj.getString("title");

                            Movie movie = new Movie(rank, title);

                            movies.add(0, movie);

                            // update offSet value
                            if (rank >= offSet) {
                                offSet = rank;
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "JSON parsing error: " + e.getMessage());
                        }
                    }

                    swipeListAdapter.notifyDataSetChanged();

                }

                // stop swipe refresh
                swipeRefreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Server error: " + error.getMessage());

                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();

                // stop swipe refresh
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(arrayRequest);

    }
}

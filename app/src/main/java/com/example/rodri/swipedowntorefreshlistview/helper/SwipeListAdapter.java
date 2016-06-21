package com.example.rodri.swipedowntorefreshlistview.helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.rodri.swipedowntorefreshlistview.R;

import java.util.List;

/**
 * Created by rodri on 6/21/2016.
 */
public class SwipeListAdapter extends ArrayAdapter<Movie> {

    private Activity activity;
    private List<Movie> movies;
    private LayoutInflater inflater = null;
    private String[] bgColors;

    public SwipeListAdapter(Activity activity, int textResourceViewId, List<Movie> movies) {
        super(activity, textResourceViewId, movies);
        try {
            this.activity = activity;
            this.movies = movies;
            this.bgColors = activity.getApplicationContext().getResources().getStringArray(R.array.movie_bg);

            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    public class ViewHolder {
        private TextView displaySerial;
        private TextView displayMovieTitle;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder = new ViewHolder();

        if (convertView == null) {
            v = inflater.inflate(R.layout.list_row, null);

            holder.displaySerial = (TextView) v.findViewById(R.id.txtSerial);
            holder.displayMovieTitle = (TextView) v.findViewById(R.id.txtMovieTitle);

            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        holder.displaySerial.setText(String.valueOf(movies.get(position).getId()));
        holder.displayMovieTitle.setText(movies.get(position).getTitle());

        String color = bgColors[position % bgColors.length];
        holder.displaySerial.setBackgroundColor(Color.parseColor(color));

        return v;
    }
}

package com.under_rated.popularmoviesretrofit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.under_rated.popularmoviesretrofit.Model.Movie;

import java.util.ArrayList;

/**
 * Created by dave on 7/18/15.
 */
public class MovieListAdapter extends ArrayAdapter<Movie> {

    private ArrayList<Movie> objects;

    public MovieListAdapter(Context context, int textViewResourceId, ArrayList<Movie> objects) {
        super(context, textViewResourceId, objects);
        this.objects = objects;
    }

    public ArrayList<Movie> getMovies() {
        return objects;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie = objects.get(position);
        View rowView = convertView;
        ViewHolder viewHolder;

        if (rowView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.grid_item_movie_fragment, null);
            viewHolder.list_item_movie_fragment_picture = (ImageView) rowView.findViewById(R.id.list_item_movie_fragment_picture);
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }

        Picasso.with(getContext()).load("http://image.tmdb.org/t/p/w500" +
                movie.getPoster_path()).into(viewHolder.list_item_movie_fragment_picture);


        return rowView;
    }


    public static class ViewHolder {
        public ImageView list_item_movie_fragment_picture;
    }
}



package com.under_rated.popularmoviesretrofit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.under_rated.popularmoviesretrofit.Model.Review;

import java.util.ArrayList;

/**
 * Created by dave on 9/24/15.
 */
public class ReviewListAdapter extends ArrayAdapter<Review>  {

    private ArrayList<Review> objects;

    public ReviewListAdapter(Context context, int textViewResourceId, ArrayList<Review> objects) {
        super(context, textViewResourceId, objects);
        this.objects = objects;
    }

    public ArrayList<Review> getReviews() {
        return objects;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Review review = objects.get(position);
        View rowView = convertView;
        ViewHolder viewHolder;

        if (rowView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.list_item_review_fragment, null);
            viewHolder.textview_review_text = (TextView) rowView.findViewById(R.id.textview_review_text);
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }

        viewHolder.textview_review_text.setText("Review by " + review.getAuthor());
        return rowView;
    }


    public static class ViewHolder {
        public TextView textview_review_text;
    }
}

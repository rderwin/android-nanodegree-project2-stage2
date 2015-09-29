package com.under_rated.popularmoviesretrofit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.under_rated.popularmoviesretrofit.Model.Video;

import java.util.ArrayList;

/**
 * Created by dave on 9/23/15.
 */
public class VideoListAdapter extends ArrayAdapter<Video> {

    private ArrayList<Video> objects;

    public VideoListAdapter(Context context, int textViewResourceId, ArrayList<Video> objects) {
        super(context, textViewResourceId, objects);
        this.objects = objects;
    }

    public ArrayList<Video> getVideos() {
        return objects;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Video video = objects.get(position);
        View rowView = convertView;
        ViewHolder viewHolder;

        if (rowView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.list_item_video_fragment, null);
            viewHolder.textview_trailer_text = (TextView) rowView.findViewById(R.id.textview_trailer_text);
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }

        viewHolder.textview_trailer_text.setText(video.getName());
        return rowView;
    }


    public static class ViewHolder {
        public TextView textview_trailer_text;
    }
}
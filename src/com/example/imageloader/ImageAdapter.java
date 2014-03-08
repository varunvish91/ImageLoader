package com.example.imageloader;


import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ImageAdapter extends BaseAdapter {
    
    private Context _activity;
    private List<ImageContent> _data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    
    public ImageAdapter(Context activity, List<ImageContent> data) {
        _activity = activity;
        _data = data;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = new ImageLoader(_activity);
    }

    public int getCount() {
        return _data.size();
    }

    public ImageContent getItem(int position) {
        return _data.get(position);
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView == null) { 
            vi = inflater.inflate(R.layout.item, null);
        }
        ImageView image = (ImageView)vi.findViewById(R.id.itemImage);
        imageLoader.displayImage(_data.get(position).getImage(), image);
        return vi;
    }
}
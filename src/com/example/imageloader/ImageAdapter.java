package com.example.imageloader;


import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

//custom adapter responsible for displaying data in the GridContent class
//uses ImageLoader to get the images (LazyList style)
public class ImageAdapter extends BaseAdapter {
    
    private Context _activity;
    private List<ImageContent> _data;
    private static LayoutInflater _inflater = null;
    public ImageLoader _imageLoader; 
    
    public ImageAdapter(Context activity) {
        _activity = activity;
        _inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        _imageLoader = new ImageLoader(_activity);
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
    
    //sets the data of the adapter
    public void addAll(List<ImageContent> items) {
        _data = items;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if(convertView == null) { 
            vi = _inflater.inflate(R.layout.item, null);
        }
        ImageView image = (ImageView)vi.findViewById(R.id.itemImage);
        _imageLoader.displayImage(_data.get(position).getImage(), image);
        return vi;
    }
}
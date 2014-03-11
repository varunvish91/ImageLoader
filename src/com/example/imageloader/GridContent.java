package com.example.imageloader;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

//Custom GridView. updates its state based on JSON data that comes via the update call
public class GridContent extends GridView implements ContentView {
    private List<ImageContent> _content = null;
    private ImageAdapter _adapter = null;
    private static String REQUEST_DATA = "responseData";
    private static String RESULTS = "results";
    private static String TITLE = "titleNoFormatting";
    private static String URL = "tbUrl";
    
    public GridContent(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public GridContent(Context context) {
        super(context);
    }
    
    public GridContent(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    public void init() {
        _content = new ArrayList<ImageContent>();
        _adapter = new ImageAdapter(getContext());
    }
    
    //Change state to represent JSON data sent via root view
    @Override
    public void update(final JSONObject content) {
        JSONObject responseData;
        try {
            responseData = content.getJSONObject(REQUEST_DATA);
            JSONArray resultsList = responseData.getJSONArray(RESULTS);
            for(int i = 0; i < resultsList.length(); i++){
                JSONObject imageData = resultsList.getJSONObject(i);
                String title = imageData.getString(TITLE);
                String url = imageData.getString(URL);
                ImageContent item = new ImageContent(title, url);
                _content.add(item);
            }
            _adapter.addAll(_content);
            _adapter.notifyDataSetChanged();
            setAdapter(_adapter);
        } 
        catch (JSONException e) {
            //keep the GridContent in its default state if some parsing error occurs
            e.printStackTrace();
        }
    }

    //clear the parsed data in the gridview
    //used when getting more data
    @Override
    public void clear() {
        _content.clear();
    }
}

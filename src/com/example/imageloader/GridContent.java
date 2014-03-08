package com.example.imageloader;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;


public class GridContent extends GridView implements ContentView, android.widget.AdapterView.OnItemClickListener {
    private StreamDecorator _outputStream; //use the stream decorator for writing to data
    private List<ImageContent> _content = null;
    private Context _context;
    private static String REQUEST_DATA = "responseData";
    private static String RESULTS = "results";
    private static String TITLE = "titleNoFormatting";
    private static String URL = "tbUrl";
    
    public GridContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        _context = context;
        _content = new ArrayList<ImageContent>();

    }
    public GridContent(Context context) {
        super(context);
        _context = context;
        _content = new ArrayList<ImageContent>();

    }
    public GridContent(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        _context = context;
        _content = new ArrayList<ImageContent>();

    }
    
    public void setStreamDecorator(StreamDecorator decorator) {
        _outputStream = decorator;
        
    }

    @Override
    public void update(final JSONObject content) {
        _content = new ArrayList<ImageContent>();
        //parse the content and create the arraylist
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
            ImageAdapter adapter = new ImageAdapter(_context, _content);
            this.setAdapter(adapter);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
    
    }
    
    //parses the json data into the content list via the messageHandler
    
}

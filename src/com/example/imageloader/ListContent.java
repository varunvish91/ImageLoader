package com.example.imageloader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListContent extends ListView implements ContentView {
    private static String RESPONSE_DATA = "responseData";
    private static String RESULTS = "results";
    public ListContent(Context context) {
        super(context);
        
    }
    public ListContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        
    }
    public ListContent(Context context, AttributeSet attrs, int what) {
        super(context, attrs, what);
    }
    @Override
    public void update(JSONObject content) {
        try { 
            JSONObject responseData = content.getJSONObject(RESPONSE_DATA);
            JSONArray searchResults = responseData.getJSONArray(RESULTS);
            String[] items = new String[searchResults.length()];
            for(int i = 0; i < searchResults.length(); i++){
                items[i] = searchResults.getString(i);
                
            }
            this.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, items));
        }
        catch(JSONException e){
            e.printStackTrace();
        }
    }
    @Override
    public void clear() {
        // TODO Auto-generated method stub
        
    }
    
    

}

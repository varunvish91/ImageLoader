package com.example.imageloader;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

//Text that will dynamically change based on search results
public class TextContent extends TextView implements ContentView {
    private static String RESULT_COUNT = "resultCount";
    private static String RESPONSE_DATA = "responseData";
    private static String RESULTS = "results";
    private static String CURSOR = "cursor";
    public TextContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        //initialize to a normal setting
        this.setText("Search for something");
    }


    @Override
    public void update(JSONObject content) {
        if(content == null) {
            setText("No results found");
        }
        else { 
            try {
                JSONObject results = content.getJSONObject(RESPONSE_DATA);
                JSONObject cursor = results.getJSONObject(CURSOR);
                JSONArray resultData = results.getJSONArray(RESULTS);
                String totalResultsFound = cursor.getString(RESULT_COUNT);
    
                int totalResults = resultData.length();
                this.setText("Displaying " + totalResults + " of " + totalResultsFound + " results");
                
                
            }
            catch(JSONException e) {
                setText("No results found");
            }
        }
        
        
        

    }
    

}

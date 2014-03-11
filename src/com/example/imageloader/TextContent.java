package com.example.imageloader;


import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

//Text that will dynamically change based on search results
public class TextContent extends TextView implements ContentView {
    private static String RESULT_COUNT = "resultCount";
    private static String RESPONSE_DATA = "responseData";
    private static String CURSOR = "cursor";
    
    public TextContent(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //initial text the user should see to help them during the app, should appear above all content
    public void initText(String initMessage) {
        setText(initMessage);
    }

    //update the state of the text based on the data receieved
    @Override
    public void update(JSONObject content) {
        if(content == null || content.length() == 0) {
            setText("No results found");
        }
        else { 
            try {
                JSONObject results = content.getJSONObject(RESPONSE_DATA);
                JSONObject cursor = results.getJSONObject(CURSOR);
                String totalResultsFound = cursor.getString(RESULT_COUNT);
                this.setText("Found " + totalResultsFound + " results");
                
                
            }
            catch(JSONException e) {
                setText("No results found");
            }
        }
    }

    @Override
    public void clear() {
        //don't reset this data
    }
    

}

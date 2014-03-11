package com.example.imageloader;


import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
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
    //this is called multiple times when new data comes. Since we only really want the results found no need of parsing objects again
    @Override
    public void update(JSONObject content) {
        if(content == null) {
            setText("No results found");
        }
        else if (content.length() == 0) {
            //don't do anything
        }
        else { 
            try {
                JSONObject results = content.getJSONObject(RESPONSE_DATA);
                JSONObject cursor = results.getJSONObject(CURSOR);
                String totalResultsFound = cursor.getString(RESULT_COUNT);
                setText("Found " + totalResultsFound + " results");
            }
            catch(JSONException e) {
                setText("No results found");
                Log.e("TextContent", content.toString());
            }
        }
    }

    @Override
    public void clear() {
        //don't reset this data
    }
    

}

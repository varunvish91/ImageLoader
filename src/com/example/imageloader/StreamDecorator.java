package com.example.imageloader;

import org.json.JSONArray;
import org.json.JSONObject;



//dynamically adds additional IOstream responsbilities to classes that implement this
//classes that implement streamdecorator must provide means of writing data and reading data
public interface StreamDecorator {
        
    public void writeToStream(JSONObject j);
    public void getFromStream(String query);

}

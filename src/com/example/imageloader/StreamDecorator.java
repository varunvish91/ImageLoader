package com.example.imageloader;

import org.json.JSONObject;



//dynamically adds additional IOstream responsibilities to classes that implement this
//classes that implement streamdecorator must provide means of writing data and reading data, to some external source
public interface StreamDecorator {
        
    public void writeToStream(JSONObject j);
    public void getFromStream(String query);
    
}

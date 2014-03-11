package com.example.imageloader;
import org.json.JSONObject;

// Custom View class. All views within an activity should implement this
// Responsible for updating the view state absed on JSONOBject data that comes throguh

public interface ContentView {
    public void update(JSONObject content);
    public void clear();
}

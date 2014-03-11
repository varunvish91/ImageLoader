package com.example.imageloader;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SearchView;

//main Search activity, users interactin with this activity can search for google images
//scrolling to the bottom will request more images
public class SearchHistory extends Activity implements StreamDecorator, OnItemClickListener {
    private static String QUERY = "query";
    private static String CUSTOM_ACTION = "historySearch";
    private ArrayList<ContentView> observers = null;
    private ActionBar actionBar = null;
    private DatabaseHandler _db = null;
    private static String RESULT_COUNT = "resultCount";
    private static String RESPONSE_DATA = "responseData";
    private static String RESULTS = "results";
    private static String CURSOR = "cursor";
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_history);
        this.actionBar = getActionBar();
        this.actionBar.setDisplayHomeAsUpEnabled(true);

        this.observers = new ArrayList<ContentView>();
        ListContent listContent = (ListContent)findViewById(R.id.searchHistory);
        listContent.setOnItemClickListener(this);
        
        this.observers.add((TextContent) findViewById(R.id.searchHistoryResults));
        this.observers.add(listContent);
        _db = new DatabaseHandler(this, null, null, 0);
        handleIntent(null);
    }
    
    private void handleIntent(Intent i) {
        String query = null;
           if(i != null ) {
               query = i.getStringExtra(SearchManager.QUERY);
           }
        getFromStream(query);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_activity_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            startActivity(new Intent(this, SearchActivity.class));
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    //open up the database and run the query
    //create JSONObject and send to children
    @Override
    public void getFromStream(String query) {
       List<String> previousQueries = _db.getQueries(query);
       JSONObject message = new JSONObject();
       JSONArray content = new JSONArray();
       JSONObject cursor = new JSONObject();
       JSONObject responseMessage = new JSONObject();
       try { 
           for(int i = 0; i < previousQueries.size(); i++) {
                content.put(i, previousQueries.get(i));
           }
           message.put(RESULTS, content);
           cursor.put(RESULT_COUNT, previousQueries.size());
           message.put(CURSOR,  cursor);
           responseMessage.put(RESPONSE_DATA, message);
           writeToStream(responseMessage);
       }
       catch(JSONException e) {
           //keep in default state if something goes wrong
           e.printStackTrace();
       }
    }

    //write to the children
    @Override
    public void writeToStream(JSONObject j) {
        for (ContentView view : observers) {
            view.update(j);
        }
    }
    
    //handle the activity state when user clicks on item
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String query = (String) parent.getItemAtPosition(position);
        Intent queryIntent = new Intent(this, SearchActivity.class);
        queryIntent.setAction(CUSTOM_ACTION);
        queryIntent.putExtra(QUERY, query);
        startActivity(queryIntent);
    }
}

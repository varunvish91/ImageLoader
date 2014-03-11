package com.example.imageloader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.SearchView;

public class SearchActivity extends Activity implements StreamDecorator, OnScrollListener {
    private static String CUSTOM_ACTION = "historySearch";
    private static String QUERY = "query";
    private ArrayList<ContentView> _observers = null;
    private ActionBar _actionBar = null;
    private DatabaseHandler _db = null;
    private int _page = 0;
    private int _maxIterations = 6; //default value to fill the page, increments as we scroll down
    private String _previousQuery = null;
    private String _currentQuery = null;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        
        //setup the action bar
        _actionBar = getActionBar();
        _actionBar.setDisplayHomeAsUpEnabled(true);

        //setup views
        _observers = new ArrayList<ContentView>();
        TextContent text = (TextContent) findViewById(R.id.searchResult);
        text.initText("Search for images!");
        
        GridContent gridContent = (GridContent)findViewById(R.id.gridContent);
        gridContent.setOnScrollListener(this);
        gridContent.init();
        
        _observers.add(text);
        _observers.add(gridContent);
        
        //setup local storage
        _db = new DatabaseHandler(this.getApplicationContext(), null, null, 0);
        handleIntent(getIntent());
    }
    
    //handle intents coming from different activities requesting a search
    //handle intents coming from search action in actionbar
    private void handleIntent(Intent intent) {
        String query =  null;
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
        }
        else if (CUSTOM_ACTION.equals(intent.getAction())) {
            Bundle b = intent.getExtras();
            query = b.getString(QUERY);
        }
        
        //get the data from defined stream, however activity chooses to use the decorator
        if(query != null) { 
            getFromStream(query);
        }
    }

    // writes the search result to local database
    // whenever its content requests it to write to stream it will
    // update the views
    @Override
    public void writeToStream(JSONObject j) {
        for (ContentView view : _observers) {
            view.update(j);
        }
    }

    // start an async task, which will pull data from google
    // on its post execuse message it will update the child views
    // if query is the same as the previous query then request more data
    @Override
    public void getFromStream(String query) {
        if(_currentQuery == null) {
            _currentQuery = query;
        }
        else if(query == _previousQuery) {
            _maxIterations++;
        }
        else {
            //new query so reset everything
            _previousQuery = _currentQuery;
            _currentQuery = query;
            _page = 0;
            _db.insertQuery(query);
            clearViews();
        }
        
        StreamReadRequest request = new StreamReadRequest(this, query);
        request.execute(this);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
        case R.id.history:
            startActivity(new Intent(this, SearchHistory.class));
            break;
        }
        
        return super.onMenuItemSelected(featureId, item);
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
    
    // helper class which will get data from Google Search API and writes the data to the StreamDecorator
    private class StreamReadRequest extends AsyncTask<StreamDecorator, Void, JSONObject> {
        private StreamDecorator _decorator = null;
        private Context _context = null;
        private ProgressDialog _dialog = null;
        private String _query = null;
        
        
        public StreamReadRequest(Context context, String query) {
            _context = context;
            _query = query;
        }
        
        //setup the progress dialog
        @Override
        protected void onPreExecute() {
            _dialog = new ProgressDialog(_context);
            _dialog.setTitle("Getting data");
            _dialog.setMessage("Please wait...");
            _dialog.setCancelable(false);
            _dialog.setIndeterminate(true);
            _dialog.show();
        }
        
        @Override
        protected JSONObject doInBackground(StreamDecorator... params) {
            JSONObject data = null;
            _decorator = params[0];
            StringBuilder builder = new StringBuilder();
            try {
                for(; _page < _maxIterations;_page++){ 
                    String line = new String();
                    URL url = new URL("https://ajax.googleapis.com/ajax/services/search/images?v=1.0&start=" + _page  +"&q=" + _query);
                    URLConnection conn = url.openConnection();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                }
                data = new JSONObject(builder.toString());
            } 
            catch (Exception e) {
                //any issues with pulling data, classify as failed attempt and return 0 objects
                e.printStackTrace();
                data = new JSONObject();
            } 
            return data;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            _decorator.writeToStream(result);
            _dialog.cancel();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        
    }


    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(firstVisibleItem + visibleItemCount >= totalItemCount){
            if(_currentQuery != null) { 
                getFromStream(_currentQuery);  
                
            }
        }
    }
    
    public void clearViews() {
        for (ContentView view : _observers) {
            view.clear();
        }
    }

}

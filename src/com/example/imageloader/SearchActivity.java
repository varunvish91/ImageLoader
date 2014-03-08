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
import android.widget.SearchView;

public class SearchActivity extends Activity implements StreamDecorator {
    private ArrayList<ContentView> observers = null;
    private ActionBar actionBar = null;
    private DatabaseHandler _db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        this.actionBar = getActionBar();
        this.actionBar.setDisplayHomeAsUpEnabled(true);

        this.observers = new ArrayList<ContentView>();
        this.observers.add((TextContent) findViewById(R.id.searchResult));
        this.observers.add((GridContent) findViewById(R.id.gridContent));
        _db = new DatabaseHandler(this.getApplicationContext(), null, null, 0);
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }
    
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            getFromStream(query);
            
        }
    }

    @Override
    public void writeToStream(JSONObject j) {
        // writes the search result to local database
        // whenever its content requests it to write to stream it will
        // update the views
        for (ContentView view : observers) {
            view.update(j);
        }
    }

    @Override
    public void getFromStream(String query) {
        // start an async task, which will pull data from google
        // on its post execuse message it will update the child views
        StreamReadRequest request = new StreamReadRequest(this, query);
        request.execute(this);
        
        //write the query to the DB
        _db.insertQuery(query);
        _db.getQueries(null);
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

    private class StreamReadRequest extends AsyncTask<StreamDecorator, Void, JSONObject> {
        private StreamDecorator _decorator = null;
        private Context _context = null;
        private ProgressDialog _dialog = null;
        private String _query = null;
        private int _pageCount = 0;
        private int MAX_ITERATORS = 4;
        
        
        public StreamReadRequest(Context context, String query) {
            _context = context;
            _query = query;
        }
        @Override
        protected void onPreExecute() {
            _dialog = new ProgressDialog(_context);
            _dialog.setTitle("Processing...");
            _dialog.setMessage("Please wait.");
            _dialog.setCancelable(false);
            _dialog.setIndeterminate(true);
            _dialog.show();
            
        }
        @Override
        protected JSONObject doInBackground(StreamDecorator... params) {
            JSONObject data = new JSONObject();
            _decorator = params[0];
            StringBuilder builder = new StringBuilder();
            
            try {
                int upperBound = _pageCount + MAX_ITERATORS;
                for(;_pageCount < upperBound; _pageCount++) { 
                    String line = new String();
                    URL url = new URL("https://ajax.googleapis.com/ajax/services/search/images?v=1.0&start=" + _pageCount  +"&q=" + _query + "&start=0&rsz=8");
                    URLConnection conn = url.openConnection();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                }
    
                data = new JSONObject(builder.toString());
            } 
            catch (Exception e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            _decorator.writeToStream(result);
            _dialog.cancel();
            //increment the page number for future requests
        }
    }
}

package com.example.imageloader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

//Database for writing to local storage
//stores past searches
public class DatabaseHandler extends SQLiteOpenHelper {
    private static int DATABASE_VERSION = 1;
    private static String DATABASE_NAME = "GooleImageSearch";
    private static String TABLE_NAME = "SearchHistory";
    private static String ID = "id";
    private static String QUERY = "query";
    private static String DATE_INSERTED = "DateInserted"; //used so we can sort
    
    public DatabaseHandler(Context context, String name, CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create the table
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + ID + " INTEGER PRIMARY KEY," + QUERY + " TEXT," +  DATE_INSERTED + " DATETIME" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }
    
    public void insertQuery(String query) {
        SimpleDateFormat entryDate = new SimpleDateFormat("ddMMyyyyhhmmss");
        String format = entryDate.format(new Date());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(QUERY, query);
        values.put(DATE_INSERTED, format);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }
    
    //returns all queries if null, or returns similiar search results for query paramater
    public List<String> getQueries(String query) {
        SQLiteDatabase db = getReadableDatabase();
        String[] selectionArgs = new String[] {query};
        ArrayList<String> results = new ArrayList<String>();
        Cursor cursor;
   
        if(query == null) {
            String sql = "SELECT DISTINCT " + QUERY + " FROM " + TABLE_NAME + " ORDER BY " + DATE_INSERTED + " DESC";
            cursor = db.rawQuery(sql, null);
        }
        else {
            String sql = "SELECT DISTINCT " + QUERY + " FROM " + TABLE_NAME + " WHERE " + QUERY + "=?" + " ORDER BY " + DATE_INSERTED + " DESC";
            cursor = db.rawQuery(sql, selectionArgs);
        }
        if(cursor.moveToFirst()) {
            do {
                String oldQuery = cursor.getString(0);
                results.add(oldQuery);
                
            } while(cursor.moveToNext());
        }
        return results;
    }
}

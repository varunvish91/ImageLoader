package com.example.imageloader;

import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {
    
    private static int DATABASE_VERSION = 1;
    
    //databbase name
    private static String DATABASE_NAME = "GooleImageSearch";
    
    //table name
    private static String TABLE_NAME = "SearchHistory";

    //columns
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
    public String[] getQueries(String query) {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = new String[] {QUERY};
        String[] selectionArgs = new String[] {query};
        Cursor cursor;
        if(query == null) {
            cursor = db.query(TABLE_NAME, columns, null, null, null, null, DATE_INSERTED + " DESC");
        }
        else {
            cursor = db.query(TABLE_NAME, columns, null, selectionArgs, null, null, DATE_INSERTED + "DESC");
        }
        if(cursor.moveToFirst()) {
            do {
                String oldQuery = cursor.getString(0);
                Log.e("VVISH", oldQuery);
                
            } while(cursor.moveToNext());
        }
        return null;
    }
    
}

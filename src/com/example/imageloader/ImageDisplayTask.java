package com.example.imageloader;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

//Task for downloading images via URL
public class ImageDisplayTask implements Runnable {
    private Context _context;
    private ImageView _reference;
    private Drawable _referenceContent;
    private String _url;
    private Handler _handler;
    
    public ImageDisplayTask(Context inContext, ImageView inReference, String inUrl, Handler inHandler) {
        _reference = inReference;
        _context = inContext;
        _url = inUrl;
        _handler = inHandler;
    }
    
    public ImageView getReference() {
        return _reference;
    }
    
    public Drawable getReferenceData() {
        return _referenceContent;
    }
    
    @Override
    public void run() {
        try{
            URL url = new URL(_url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            _referenceContent = new BitmapDrawable(_context.getResources(),myBitmap);
            Message msg = _handler.obtainMessage(1, this);
            _handler.sendMessage(msg);
            
        }
        catch(Exception th){
            th.printStackTrace();
        }
    
    }
    
    
}

package com.example.imageloader;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class ImageContent {
    private String caption;
    private String image;
    
    public ImageContent(String inCaption, String inImage) {
        this.caption = inCaption;
        this.image = inImage;
    }
    
    public String getcaption() {
        return this.caption;
    }
    
    public String getImage() {
        return this.image;
    }
    
    
}

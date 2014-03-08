package com.example.imageloader;


import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.ImageView;

public class ImageLoader {
    
    private Map<ImageView, Drawable> imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, Drawable>());
    private Context _context;
    private Handler _handler;
    
    public ImageLoader(Context context){
        _context = context;
        _handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                ImageDisplayTask task = (ImageDisplayTask) msg.obj;
                if(task != null) {
                    imageViews.put(task.getReference(), task.getReferenceData());
                    task.getReference().setImageDrawable(task.getReferenceData());
                }
                     
            };
        };
        
    }
    
    public void displayImage(String url, ImageView imageView) {
        if(imageViews.containsKey(imageView)) {
            imageView.setImageDrawable(imageViews.get(imageView));
        }
        else {
            queuePhoto(url, imageView);
            imageView.setImageResource(R.drawable.ic_launcher);
        }
    }
    private void queuePhoto(String url, final ImageView imageView) {
        ImageDisplayTask task = new ImageDisplayTask(_context, imageView, url, _handler);
        ThreadManager.getInstance().execute(task);
    }

}
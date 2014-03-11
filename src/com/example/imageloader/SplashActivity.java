package com.example.imageloader;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


//Can't put the search feature into the loader, this is just a splash screen
public class SplashActivity extends Activity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(this, SearchActivity.class));
        finish();
    }

}

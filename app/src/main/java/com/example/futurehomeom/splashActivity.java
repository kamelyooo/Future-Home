package com.example.futurehomeom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.backendless.Backendless;

import java.util.Locale;

public class splashActivity extends AppCompatActivity {
//    SharedPreferences preferencess;
//    int lang;
//    Locale locale;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//        preferencess = getSharedPreferences("lang", Context.MODE_PRIVATE);
//
//        lang = preferencess.getInt("lang", 0);
//        if (lang == 0) {
//            locale = new Locale("en");
//        } else if(lang == 1) {
//
//            locale = new Locale("ar");
//        }
////            locale = new Locale("ar");\
//        if (locale != null)
//            Locale.setDefault(locale);
//        Resources resources = getResources();
//        Configuration config = resources.getConfiguration();
//        config.setLocale(locale);
//        resources.updateConfiguration(config, resources.getDisplayMetrics());

        setContentView(R.layout.activity_splash);

        Backendless.initApp(this,"60B47F9E-3A43-C3DE-FF03-2E8296E1B600","0F012B1A-4D9C-47B2-91B4-4B476AC5BF93");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(splashActivity.this, MainActivity.class);

                startActivity(i);

                finish();
            }
        },1500);
    }
}
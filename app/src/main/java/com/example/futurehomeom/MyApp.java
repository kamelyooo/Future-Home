package com.example.futurehomeom;

import android.app.Application;

import com.backendless.Backendless;

public class MyApp extends Application {
    @Override
    public void onCreate() {


        super.onCreate();
        Backendless.initApp(this,"60B47F9E-3A43-C3DE-FF03-2E8296E1B600","0F012B1A-4D9C-47B2-91B4-4B476AC5BF93");
//
    }


}

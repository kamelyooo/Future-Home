package com.example.futurehomeom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import com.backendless.Backendless;
import com.crowdfire.cfalertdialog.CFAlertDialog;


import java.util.Locale;
import java.util.concurrent.TimeUnit;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class MainActivity extends AppCompatActivity {
    SmoothBottomBar buttonBarLayout;
    HomeFragment homeFragment;
    CartFragment cartFragment;
    ProfileFragment profileFragment;
    SharedPreferences preferencess;
    int lang;
    Locale locale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        preferencess = getSharedPreferences("lang", Context.MODE_PRIVATE);

        lang = preferencess.getInt("lang", 0);
        if (lang == 0) {
            locale = new Locale("en");
        } else if(lang == 1) {

            locale = new Locale("ar");
        }
//            locale = new Locale("ar");\
        if (locale != null)
            Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
        setContentView(R.layout.activity_main);

         getSupportActionBar().setTitle(" Future Home");

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_im);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        androidx.work.Constraints.Builder conBuilder=new androidx.work.Constraints.Builder();
        conBuilder.setRequiredNetworkType(NetworkType.CONNECTED);

        Constraints constraints = conBuilder.build();

        PeriodicWorkRequest.Builder workBuilder=new PeriodicWorkRequest.Builder(MyWork.class,15, TimeUnit.MINUTES);

        workBuilder.setConstraints(constraints);
        PeriodicWorkRequest request = workBuilder.build();
        WorkManager.getInstance(this).enqueue(request);

        buttonBarLayout = findViewById(R.id.bottomBar);

        homeFragment=new HomeFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.countaner, homeFragment).commit();


        buttonBarLayout.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int i) {
                if (i == 0){
                    if (homeFragment == null)
                        homeFragment=new HomeFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.countaner, homeFragment,"hometag").commit();

                }
                else if (i == 1){
                    if (cartFragment == null)
                        cartFragment=new CartFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.countaner, cartFragment,"carttag").commit();
                }
                else if (i == 2){
                    if (profileFragment == null)
                        profileFragment=new ProfileFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.countaner, profileFragment,"profiletag").commit();
                }

                return true;
            }

        });
    }

    @Override
    public void onBackPressed() {

        CFAlertDialog.Builder builder = new CFAlertDialog.Builder(this)
                .setDialogStyle(CFAlertDialog.CFAlertStyle.BOTTOM_SHEET)
                .setTitle(R.string.DoyouwanttoExite).setDialogBackgroundColor( Color.parseColor("#f8f1f1"))



                .addButton(getString(R.string.Exite), -1, Color.parseColor("#AF1313"), CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                    finish();
                }).addButton(getString(R.string.cancell),Color.parseColor("#212121"), Color.parseColor("#EEEEEE"), CFAlertDialog.CFAlertActionStyle.NEGATIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> dialog.dismiss());
                        builder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

}
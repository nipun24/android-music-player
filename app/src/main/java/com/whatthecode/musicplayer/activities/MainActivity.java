package com.whatthecode.musicplayer.activities;

import android.Manifest;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.whatthecode.musicplayer.R;
import com.whatthecode.musicplayer.adapters.HomePageAdapter;
import com.whatthecode.musicplayer.models.Track;
import com.whatthecode.musicplayer.services.MusicService;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // Music Service variables
    private MusicService musicService;
    private Intent musicIntent;
    private boolean serviceBound = false;
    // Permission return code. The number doesnt signify anything. Its just a random number.
    private static final int EXTERNAL_STORAGE_REQUEST_CODE = 892;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // for android version marshmellow+, permission is requested from the user
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_REQUEST_CODE);
        } else {
            Init();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (musicIntent == null) {
            musicIntent = new Intent(this, MusicService.class);
            bindService(musicIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(musicIntent);
        }
    }

    private ServiceConnection musicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            // get service
            musicService = binder.getService();
            serviceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };

    /**
     * Load the UI
     */
    private void Init() {
        // get the view pager reference
        ViewPager homeViewPager = (ViewPager) findViewById(R.id.home_pager);
        // make a new HomePageAdapter
        HomePageAdapter adapter = new HomePageAdapter(this, getSupportFragmentManager());
        homeViewPager.setAdapter(adapter);

        // Setup the tabs
        TabLayout tabLayout = (TabLayout) findViewById(R.id.home_tabs);
        tabLayout.setupWithViewPager(homeViewPager);
    }

    /**
     * Callback method after permission request
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case EXTERNAL_STORAGE_REQUEST_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay!
                    Init();

                } else {
                    // I QUIT
                    // home_pager, it doesnt matter anyway. read the docs
                    Snackbar.make(findViewById(R.id.home_pager), "Permission Denied. Bye Bye",
                            Snackbar.LENGTH_LONG)
                            .show();

                    // close the app after 1second
                    Handler h = new Handler();
                    h.postDelayed(
                            new Runnable() {
                                @Override
                                public void run() {
                                    finishAffinity();
                                }
                            }, 1000);
                }
                break;
        }
    }
}

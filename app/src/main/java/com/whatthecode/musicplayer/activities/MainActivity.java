package com.whatthecode.musicplayer.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.whatthecode.musicplayer.MusicPlayer;
import com.whatthecode.musicplayer.R;
import com.whatthecode.musicplayer.adapters.HomePageAdapter;

public class MainActivity extends AppCompatActivity {

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
        MusicPlayer.Init(getApplicationContext());
    }


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

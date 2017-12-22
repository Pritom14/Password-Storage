package com.nitsilchar.hp.passwordStorage.activity;

/**
 * Created by admin on 12/22/2017.
 */


import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.VideoView;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.crashlytics.android.Crashlytics;
import com.nitsilchar.hp.passwordStorage.R;
import com.nitsilchar.hp.passwordStorage.activity.LoginActivity;
import com.nitsilchar.hp.passwordStorage.activity.LoginRegistrationActivity;

import io.fabric.sdk.android.Fabric;


public class SplashActivity extends AppCompatActivity {

    VideoView video;

    public static String str_login_test;

    public static SharedPreferences sh;
    public static SharedPreferences.Editor editor;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        // getSupportActionBar().hide();

        sh = getSharedPreferences("myprefe", 0);
        editor = sh.edit();
        str_login_test = sh.getString("loginTest", null);

        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
            return;
        }

        video=(VideoView)findViewById(R.id.videoView);
        Uri videouri=Uri.parse("android.resource://" +getPackageName()+"/"+R.raw.splash_final);//location of the splash video
        video.setVideoURI(videouri);
        video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                // action which happens after completion of the video
                //*****************************************//

                if (str_login_test != null
                        && !str_login_test.toString().trim().equals("")) {
                    Intent send = new Intent(getApplicationContext(),
                            LoginActivity.class);
                    startActivity(send);
                }
                /*
                 * if user login test is false on oncreate then redirect the
                 * user to login & registration page
                 */
                else {

                    Intent send = new Intent(getApplicationContext(),
                            LoginRegistrationActivity.class);
                    startActivity(send);

                }

                //*****************************************************************//

            }
        });

        video.start();

        //if the user touches the screen while splash screen video is playing, it moves to the next activity as deignated

        video.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ////////////////////////////////////////////////////////////
                if (str_login_test != null
                        && !str_login_test.toString().trim().equals("")) {
                    Intent send = new Intent(getApplicationContext(),
                            LoginActivity.class);
                    startActivity(send);
                }
                /*
                 * if user login test is false on oncreate then redirect the
                 * user to login & registration page
                 */
                else {

                    Intent send = new Intent(getApplicationContext(),
                            LoginRegistrationActivity.class);
                    startActivity(send);

                }
                ////////////////////////////////////////////////////////////////
               // finish();
                return false;
            }
        });



    }

    public boolean containsPass(String str){

        return  sh.contains(str)?true:false;


    }

}

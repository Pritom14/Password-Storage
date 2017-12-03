package com.nitsilchar.hp.passwordStorage.activity;

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
    public static String str_login_test;

    public static SharedPreferences sh;
    public static SharedPreferences.Editor editor;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash);

        //ADDED BY ThisIsNSH
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }


        actionBar=getSupportActionBar();
        actionBar.hide();
        sh = getSharedPreferences("myprefe", 0);
        editor = sh.edit();
        str_login_test = sh.getString("loginTest", null);

        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
            return;
        }

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                /*
                 * if user login test is true on oncreate then redirect the user
                 * to result page
                 */

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
            }

        }, 3000);

    }

    public boolean containsPass(String str){

       return  sh.contains(str)?true:false;


    }

}

package com.nitsilchar.hp.passwordStorage.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.crashlytics.android.Crashlytics;
import com.nitsilchar.hp.passwordStorage.R;

import io.fabric.sdk.android.Fabric;

public class LoginRegistrationActivity extends AppCompatActivity implements View.OnClickListener {
    Button login, register;
    CardView cardView;
    boolean b=false;
    SplashActivity splashActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_login_registration);
        login = (Button) findViewById(R.id.btn_login);
        register = (Button) findViewById(R.id.btn_register);
        cardView=(CardView)findViewById(R.id.layout2);
        splashActivity=new SplashActivity();

        login.setOnClickListener(this);
        register.setOnClickListener(this);
        b=splashActivity.containsPass("password");

        if(b==true)
        {
            register.setVisibility(View.INVISIBLE);
            cardView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(login);
                break;
            default:
                Intent registeration = new Intent(getApplicationContext(),
                        RegistrationActivity.class);
                startActivity(registeration);
                break;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(LoginRegistrationActivity.this,
                    SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", true);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }

}

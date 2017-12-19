package com.nitsilchar.hp.passwordStorage.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nitsilchar.hp.passwordStorage.R;
import com.nitsilchar.hp.passwordStorage.model.AppStatus;

import java.util.Properties;

import io.fabric.sdk.android.Fabric;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    String str_Email;
    String str_Password, str_getEmail, str_getPass;
    EditText edt_Email, edt_Password;
    Button login, newuser;
    boolean b=false;
    Properties prop;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    SplashActivity splashActivity;
    AppStatus appStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_login);
        splashActivity=new SplashActivity();
        prop=new Properties();
        auth = FirebaseAuth.getInstance();
        appStatus=new AppStatus(getApplicationContext());
        str_getEmail = SplashActivity.sh.getString("email", null);
        str_getPass = SplashActivity.sh.getString("password", null);
        login = (Button) findViewById(R.id.btn_login);
        newuser = (Button) findViewById(R.id.newuser);
        edt_Email = (EditText) findViewById(R.id.edt_email);
        edt_Password = (EditText) findViewById(R.id.edt_password);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        b=splashActivity.containsPass("password");
        if(b==true){
        }
        login.setOnClickListener(this);
        newuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
            }
        });
    }
    @Override
    public void onClick(View v) {
        if (appStatus.isOnline()) {

            try {
                str_Email = edt_Email.getText().toString();
                str_Password = edt_Password.getText().toString();
                if (str_Email.length() == 0 & str_Password.length() == 0) {
                    Toast.makeText(getApplicationContext(),
                            R.string.login_enter_email_password,
                            Toast.LENGTH_LONG).show();
                } else if (str_Email.length() == 0) {
                    Toast.makeText(getApplicationContext(),
                            R.string.login_enter_emailid, Toast.LENGTH_LONG).show();
                } else if (str_Password.length() == 0) {
                    Toast.makeText(getApplicationContext(),
                            R.string.login_enter_password, Toast.LENGTH_LONG).show();
                } else if (str_getEmail.matches("") && str_getPass.matches("")) {
                    Toast.makeText(getApplicationContext(),
                            R.string.login_details_invalid,
                            Toast.LENGTH_LONG).show();
                } else if (!(str_Email.matches(str_getEmail))) {
                    Toast.makeText(getApplicationContext(),
                            R.string.login_email_password_invalid, Toast.LENGTH_LONG)
                            .show();
                } else if (!(str_getPass.matches(str_Password))) {
                    Toast.makeText(getApplicationContext(),
                            R.string.login_email_password_invalid, Toast.LENGTH_LONG)
                            .show();
                } else if ((str_getEmail.matches(str_Email))
                        && (str_getPass.matches(str_Password))) {

                    SplashActivity.editor.putString("loginTest", "true");
                    SplashActivity.editor.commit();
                    progressBar.setVisibility(View.VISIBLE);

                    auth.signInWithEmailAndPassword(str_getEmail, str_getPass)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this,
                                                R.string.login_authentication_failed, Toast.LENGTH_LONG).show();
                                    } else {

                                        checkIfEmailVerified();
                                    }
                                }
                            });
                }

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), R.string.login_email_not_found_in_device, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(),R.string.appstatus_no_connection,Toast.LENGTH_LONG).show();
        }
    }
    private void checkIfEmailVerified(){
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        if (user.isEmailVerified()){
            Toast.makeText(getApplicationContext(),
                    R.string.login_successful_login, Toast.LENGTH_SHORT).show();
            Intent sendToLogout = new Intent(getApplicationContext(),
                    MainActivity.class);
            startActivity(sendToLogout);
        } else {
            Toast.makeText(getApplicationContext(),
                    R.string.login_email_notverified,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(LoginActivity.this, SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", true);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }
}

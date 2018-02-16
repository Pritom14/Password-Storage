package com.nitsilchar.hp.passwordStorage.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.nitsilchar.hp.passwordStorage.R;
import com.nitsilchar.hp.passwordStorage.model.AppStatus;

import java.util.Properties;

import io.fabric.sdk.android.Fabric;


public class SettingsActivity extends AppCompatActivity {

    String str_old, str_email;
    String str_Old, str_New, str_Verify;
    EditText edt_old, edt_new, edt_verify;
    boolean b=false;
    Button change;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    SplashActivity splashActivity;
    Properties prop;
    AppStatus appStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_settings);
        change = (Button) findViewById(R.id.btn_change);
        edt_old = (EditText) findViewById(R.id.old_pass);
        edt_new = (EditText) findViewById(R.id.new_password);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        edt_verify = (EditText) findViewById(R.id.edt_password1);
        str_email = getIntent().getStringExtra("email");
        splashActivity=new SplashActivity();
        appStatus=new AppStatus(getApplicationContext());
        auth = FirebaseAuth.getInstance();
        prop=new Properties();
        str_old= SplashActivity.sh.getString("password", null);
        b=splashActivity.containsPass("password");
        if(b==false){

        }
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (appStatus.isOnline()) {
                    try {
                        str_Old = edt_old.getText().toString();
                        str_New = edt_new.getText().toString();
                        str_Verify = edt_verify.getText().toString();
                        if (str_Old.length() == 0) {
                            Toast.makeText(getApplicationContext(),
                                    R.string.login_enter_password, Toast.LENGTH_LONG).show();
                        } else if (str_New.length() == 0) {
                            Toast.makeText(getApplicationContext(),
                                    R.string.login_enter_password, Toast.LENGTH_LONG).show();
                        } else if (str_Verify.length() == 0) {
                            Toast.makeText(getApplicationContext(),
                                    R.string.login_enter_password, Toast.LENGTH_LONG).show();
                        } else if (!(str_Old.matches(str_old))) {
                            Toast.makeText(getApplicationContext(),
                                    R.string.no_match, Toast.LENGTH_LONG).show();
                        } else if (str_New.matches(str_Verify)) {
                            progressBar.setVisibility(View.VISIBLE);
                            auth.getCurrentUser().updatePassword(str_New).addOnCompleteListener(SettingsActivity.this, new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressBar.setVisibility(View.GONE);
                                    /*if (!task.isSuccessful()) {
                                        Toast.makeText(SettingsActivity.this,
                                                "Could not update password", Toast.LENGTH_LONG).show();
                                    } else {*/
                                        Toast.makeText(SettingsActivity.this,
                                                "Password update successful", Toast.LENGTH_LONG).show();
                                        SplashActivity.editor.putString("password", str_New);
                                        SplashActivity.editor.commit();
                                        edt_new.setText("");
                                        edt_old.setText("");
                                        edt_verify.setText("");
                                        onBackPressed();
                                   // }
                                }
                            }).addOnFailureListener(SettingsActivity.this, new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SettingsActivity.this, e.getLocalizedMessage().toString(), Toast.LENGTH_SHORT).show();
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
        });
    }
}

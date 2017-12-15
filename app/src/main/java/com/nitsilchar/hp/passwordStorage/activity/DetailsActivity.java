package com.nitsilchar.hp.passwordStorage.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.nitsilchar.hp.passwordStorage.R;
import com.nitsilchar.hp.passwordStorage.database.PasswordDatabase;

import io.fabric.sdk.android.Fabric;

public class DetailsActivity extends AppCompatActivity {
    TextView site_name,site_pass;
    PasswordDatabase db;
    EditText password;
    Button modify;
    Button showPassword;
    String getPass,s;
    String newPassword,pass;
    int getUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_details);
        db=new PasswordDatabase(getApplicationContext());
        site_name=(TextView)findViewById(R.id.displaySiteTextId);
        site_pass=(TextView)findViewById(R.id.displaySitePassId);
        modify=(Button)findViewById(R.id.modifyButton);
        showPassword = (Button) findViewById(R.id.showpassword);
        modify.setVisibility(View.INVISIBLE);
        showPassword.setVisibility(View.VISIBLE);
        s=getIntent().getStringExtra("Site");
        pass=db.getData(s);
        site_name.setText(s);
        site_pass.setText("**********");

        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialogBuilder=new AlertDialog.Builder(DetailsActivity.this);
                LayoutInflater inflater=DetailsActivity.this.getLayoutInflater();
                final View dialogView=inflater.inflate(R.layout.modify_password,null);
                dialogBuilder.setView(dialogView);
                final EditText newPass=(EditText)dialogView.findViewById(R.id.enterpassModify);
                dialogBuilder.setTitle(R.string.details_enter_password);
                dialogBuilder.setIcon(R.mipmap.icon);
                dialogBuilder.setPositiveButton(R.string.details_modify, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        newPassword=newPass.getText().toString();
                        getUpdate=db.modifyCredentials(s,newPassword);
                        if (getUpdate==1) {
                            dialog.dismiss();
                            site_pass.setText(newPassword);
                        }
                    }
                });
                dialogBuilder.setNegativeButton(R.string.details_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog b=dialogBuilder.create();
                b.show();
            }
        });
        showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getPass= SplashActivity.sh.getString("password", null);
                AlertDialog.Builder dialogBuilder=new AlertDialog.Builder(DetailsActivity.this);
                LayoutInflater inflater=DetailsActivity.this.getLayoutInflater();
                final View dialogView=inflater.inflate(R.layout.details_dialog,null);
                dialogBuilder.setView(dialogView);
                password=(EditText)dialogView.findViewById(R.id.passDialog);
                dialogBuilder.setTitle(R.string.details_show_password);
                dialogBuilder.setIcon(R.mipmap.icon);
                dialogBuilder.setPositiveButton(R.string.details_confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(getPass.equals(password.getText().toString())){
                            site_pass.setText(pass);
                            modify.setVisibility(View.VISIBLE);
                            showPassword.setVisibility(View.INVISIBLE);
                            dialog.dismiss();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),R.string.details_wrong_password,Toast.LENGTH_LONG).show();
                        }
                    }
                });
                dialogBuilder.setNegativeButton(R.string.details_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog b=dialogBuilder.create();
                b.show();
            }
        });
    }
}

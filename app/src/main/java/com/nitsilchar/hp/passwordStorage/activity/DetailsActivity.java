package com.nitsilchar.hp.passwordStorage.activity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.nitsilchar.hp.passwordStorage.database.PasswordDatabase;
import com.nitsilchar.hp.passwordStorage.R;

import io.fabric.sdk.android.Fabric;

public class DetailsActivity extends AppCompatActivity {
    TextView site_name,site_pass;
    PasswordDatabase db;
    CheckBox mCbShowPwd;
    EditText password;
    Button modify;
    String getPass,s;
    String newPassword,pass;
    int getUpdate;
    CardView cardView1,cardView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_details);
        db=new PasswordDatabase(getApplicationContext());
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        site_name=(TextView)findViewById(R.id.displaySiteTextId);
        site_pass=(TextView)findViewById(R.id.displaySitePassId);
        mCbShowPwd=(CheckBox)findViewById(R.id.showpaswword);
        mCbShowPwd.setVisibility(View.VISIBLE);
        modify=(Button)findViewById(R.id.modifyButton);
        cardView1=(CardView)findViewById(R.id.card1);
        cardView2=(CardView)findViewById(R.id.card2);
        modify.setVisibility(View.INVISIBLE);
        cardView2.setVisibility(View.INVISIBLE);
        s=getIntent().getStringExtra("Site");
        pass=db.getData(s);
        site_name.setText("Account : "+s);
        site_pass.setText("Password : **********");
        mCbShowPwd = (CheckBox) findViewById(R.id.showpaswword);

        // add onCheckedListener on checkbox
        // when user clicks on this checkbox, this is the handler.
        mCbShowPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // checkbox status is changed from uncheck to checked.
                getPass= SplashActivity.sh.getString("password", null);
                // show password
                if (!isChecked) {
                    site_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    // hide password
                    site_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });;

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
                            site_pass.setText("Password : "+newPassword);
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
        mCbShowPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                            site_pass.setText("Password : "+pass);
                            modify.setVisibility(View.VISIBLE);
                            cardView2.setVisibility(View.VISIBLE);
                            mCbShowPwd.setVisibility(View.INVISIBLE);
                            cardView1.setVisibility(View.INVISIBLE);
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

package com.nitsilchar.hp.passwordStorage.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.util.Linkify;
import android.widget.TextView;

import com.nitsilchar.hp.passwordStorage.R;

public class AboutUsActivity extends AppCompatActivity {
    TextView contributors_desc, app_report_issues_desc, app_license_info_desc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        contributors_desc = (TextView) findViewById(R.id.contributors_desc);
        app_report_issues_desc = (TextView) findViewById(R.id.app_report_issues_desc);
        app_license_info_desc = (TextView) findViewById(R.id.app_license_info_desc);
        onClickURL();
    }

    private void onClickURL(){
        MovementMethod contributors_descMovementMethod = contributors_desc.getMovementMethod();
        if ((contributors_descMovementMethod == null) || !(contributors_descMovementMethod instanceof LinkMovementMethod)) {
            if (contributors_desc.getLinksClickable()) {
                contributors_desc.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }
        MovementMethod app_report_issues_descMovementMethod = app_report_issues_desc.getMovementMethod();
        if ((app_report_issues_descMovementMethod == null) || !(app_report_issues_descMovementMethod instanceof LinkMovementMethod)) {
            if (app_report_issues_desc.getLinksClickable()) {
                app_report_issues_desc.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }
        MovementMethod app_license_info_descMovementMethod = app_license_info_desc.getMovementMethod();
        if ((app_license_info_descMovementMethod == null) || !(app_license_info_descMovementMethod instanceof LinkMovementMethod)) {
            if (app_license_info_desc.getLinksClickable()) {
                app_license_info_desc.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }
    }
}

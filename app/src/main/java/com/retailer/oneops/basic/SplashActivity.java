package com.retailer.oneops.basic;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.retailer.oneops.R;
import com.retailer.oneops.auth.activity.SelectLanguageActivity;
import com.retailer.oneops.dashboard.activity.DashboardActivity;
import com.retailer.oneops.util.AppSignatureHelper;
import com.retailer.oneops.util.Constant;
import com.retailer.oneops.util.Session;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity implements Constant {
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        activity = this;
        initialization();
    }

    private void initialization() {
        ArrayList<String> a = new AppSignatureHelper(activity).getAppSignatures();
        Log.d("App signature key: ", a.toString());

        new Handler().postDelayed(() -> {
            startActivity(new Intent(activity, new Session(activity).getString(IS_LOGIN).equals(YES) ? DashboardActivity.class : SelectLanguageActivity.class));
            finish();
        }, 3000);


    }
}

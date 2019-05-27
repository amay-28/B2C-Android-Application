package com.eb.onebandhan.basic;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.eb.onebandhan.R;
import com.eb.onebandhan.auth.activity.SelectLanguageActivity;
import com.eb.onebandhan.dashboard.activity.DashboardActivity;
import com.eb.onebandhan.util.AppSignatureHelper;
import com.eb.onebandhan.util.Constant;
import com.eb.onebandhan.util.Session;

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

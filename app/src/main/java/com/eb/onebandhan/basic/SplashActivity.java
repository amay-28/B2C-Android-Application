package com.eb.onebandhan.basic;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.eb.onebandhan.R;
import com.eb.onebandhan.auth.activity.SelectLanguageActivity;

public class SplashActivity extends AppCompatActivity {
    private Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        activity=this;
        initialization();
    }

    private void initialization() {
        new Handler().postDelayed(() -> {
            startActivity(new Intent(activity, SelectLanguageActivity.class));
            finish();
        },3000);
    }
}

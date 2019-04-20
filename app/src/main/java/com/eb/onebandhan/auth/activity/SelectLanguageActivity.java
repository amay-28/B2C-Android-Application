package com.eb.onebandhan.auth.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.eb.onebandhan.R;
import com.eb.onebandhan.databinding.ActivitySelectLanguageBinding;

public class SelectLanguageActivity extends AppCompatActivity {
    private Activity activity;
    private ActivitySelectLanguageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_select_language);
        initialization();
    }

    private void initialization() {

    }

    public void startLoginScreen(View v) {
        startActivity(new Intent(activity, LoginActivity.class));
    }

    public void startSignUpScreen(View view) {
        startActivity(new Intent(activity, SignUpInitialActivity.class));
    }
}

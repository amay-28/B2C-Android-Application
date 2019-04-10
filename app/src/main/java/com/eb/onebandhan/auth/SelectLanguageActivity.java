package com.eb.onebandhan.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.os.Bundle;

import com.eb.onebandhan.R;
import com.eb.onebandhan.databinding.ActivitySelectLanguageBinding;

public class SelectLanguageActivity extends AppCompatActivity {
   private Activity activity;
   private ActivitySelectLanguageBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=this;
        binding= DataBindingUtil.setContentView(activity,R.layout.activity_select_language);
        initialization();
    }

    private void initialization() {

    }
}

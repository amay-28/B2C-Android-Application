package com.eb.onebandhan.service;

import android.app.Activity;
import android.os.Bundle;
import com.eb.onebandhan.R;
import com.eb.onebandhan.databinding.ActivityAddServiceBinding;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class AddServiceActivity extends AppCompatActivity {
    private Activity activity;
    private ActivityAddServiceBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=this;
        binding= DataBindingUtil.setContentView(activity, R.layout.activity_add_service);
        initialization();

    }

    private void initialization() {

    }
}

package com.eb.onebandhan.dashboard.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.os.Bundle;

import com.eb.onebandhan.R;
import com.eb.onebandhan.databinding.ActivityEditProfileBinding;

public class EditProfileActivity extends AppCompatActivity {
    private Activity activity;
    private ActivityEditProfileBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=this;
        binding= DataBindingUtil.setContentView(activity,R.layout.activity_edit_profile);
        initialization();
    }

    private void initialization() {

    }
}

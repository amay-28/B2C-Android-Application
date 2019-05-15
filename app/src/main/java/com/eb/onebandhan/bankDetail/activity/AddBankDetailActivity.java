package com.eb.onebandhan.bankDetail.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.os.Bundle;

import com.eb.onebandhan.R;
import com.eb.onebandhan.databinding.ActivityAddBankDetailBinding;
import com.google.gson.JsonObject;

public class AddBankDetailActivity extends AppCompatActivity {
    private Activity activity;
    private ActivityAddBankDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_add_bank_detail);
        initialization();
    }

    private void initialization() {

    }

    private void addBankDetailRequestObject(String gender) {

    }
}

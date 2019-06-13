package com.retailer.oneops.settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.retailer.oneops.R;
import com.retailer.oneops.databinding.ActivitySetAmountBinding;
import com.retailer.oneops.settings.SettingActivity;

public class SetAmountActivity extends AppCompatActivity {
    private ActivitySetAmountBinding binding;
    public Activity mcontext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView ( R.layout.activity_set_amount );
        mcontext = this;
        binding = DataBindingUtil.setContentView(mcontext, R.layout.activity_set_amount);
        listeners();

    }

    private void listeners() {
        binding.ivCross.setOnClickListener(v -> mcontext.finish());
    }


}

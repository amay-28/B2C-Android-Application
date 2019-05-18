package com.eb.onebandhan.productListing;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.eb.onebandhan.R;
import com.eb.onebandhan.databinding.ActivityProductListingBinding;
import com.eb.onebandhan.util.CommonClickHandler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class ProductListingActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityProductListingBinding mBinding;
    private Context context;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_product_listing);
        activity = this;

        setupToolbar();
        initViews();
    }

    public void setupToolbar() {
        mBinding.header.setHandler(new CommonClickHandler(activity));
        mBinding.header.tvMainHeading.setText(R.string.product_listing);
    }

    public void initViews() {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit:
                break;

        }
    }
}

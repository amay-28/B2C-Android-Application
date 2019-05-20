package com.eb.onebandhan.productListing;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.eb.onebandhan.R;
import com.eb.onebandhan.databinding.ActivityProductListingBinding;
import com.eb.onebandhan.util.CommonClickHandler;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class ProductListingActivity extends AppCompatActivity {

    private ActivityProductListingBinding mBinding;
    private Context context;
    private Activity activity;
    private BottomSheetDialog bottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_product_listing);
        activity = this;

        setupToolbar();
        initViews();
        listener();
    }

    public void setupToolbar() {
        mBinding.header.setHandler(new CommonClickHandler(activity));
        mBinding.header.tvMainHeading.setText(R.string.product_listing);
    }

    public void initViews() {
        bottomSheetDialog = new BottomSheetDialog(this,R.style.TransparentDialogBackground);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_sort);
    }

    public void listener(){
        mBinding.rlSort.setOnClickListener(v -> bottomSheetDialog.show());
        bottomSheetDialog.findViewById(R.id.ivCross).setOnClickListener(v -> bottomSheetDialog.dismiss());

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}

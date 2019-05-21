package com.eb.onebandhan.productListing;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.eb.onebandhan.R;
import com.eb.onebandhan.databinding.ActivityProductListingBinding;
import com.eb.onebandhan.product.adapter.DialogListAdapter;
import com.eb.onebandhan.productListing.adapter.ProductListAdapter;
import com.eb.onebandhan.productListing.model.MProduct;
import com.eb.onebandhan.productListing.presenter.ProductListingPresenter;
import com.eb.onebandhan.productListing.viewinterface.ProductListingViewInterface;
import com.eb.onebandhan.util.CommonClickHandler;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ProductListingActivity extends AppCompatActivity implements ProductListingViewInterface {

    private ActivityProductListingBinding mBinding;
    private Context context;
    private Activity activity;
    private List<MProduct> productList = new ArrayList<>();
    private BottomSheetDialog bottomSheetDialog;
    private ProductListAdapter productListAdapter;
    private ProductListingPresenter productListingPresenter;

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
        bottomSheetDialog = new BottomSheetDialog(this, R.style.TransparentDialogBackground);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_sort);
        productListingPresenter = new ProductListingPresenter(this, activity);
        productListingPresenter.onProductListing();

        mBinding.rvProducts.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.VERTICAL, false));
        mBinding.rvProducts.setHasFixedSize(true);
        mBinding.rvProducts.setItemAnimator(new DefaultItemAnimator());
        productListAdapter = new ProductListAdapter(activity, productList);
        mBinding.rvProducts.setAdapter(productListAdapter);
    }

    public void listener() {
        mBinding.rlSort.setOnClickListener(v -> bottomSheetDialog.show());
        bottomSheetDialog.findViewById(R.id.ivCross).setOnClickListener(v -> bottomSheetDialog.dismiss());

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onSuccessfulProductListing(List<MProduct> mProductListing, String message) {
        this.productList.addAll(mProductListing);
        productListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFailedProductListing(String errorMessage) {
        Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show();
    }
}

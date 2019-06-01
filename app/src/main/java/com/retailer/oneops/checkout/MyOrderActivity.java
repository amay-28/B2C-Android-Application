package com.retailer.oneops.checkout;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.retailer.oneops.R;
import com.retailer.oneops.checkout.adapter.CheckoutAdapter;
import com.retailer.oneops.databinding.ActivityCheckoutBinding;
import com.retailer.oneops.productListing.model.MProduct;

import java.util.ArrayList;
import java.util.List;

public class MyOrderActivity extends AppCompatActivity implements CheckoutAdapter.CallBack {

    private Activity activity;
    private ActivityCheckoutBinding binding;
    private CheckoutAdapter checkoutAdapter;
    private List<MProduct> productList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_checkout);

        activity = MyOrderActivity.this;

        initialization();
        bindRecyclerView();
        listener();
    }

    private void initialization() {
    }

    public void bindRecyclerView() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity, RecyclerView.VERTICAL, false);
        binding.rvCartItems.setLayoutManager(mLayoutManager);
        binding.rvCartItems.setHasFixedSize(true);
        binding.rvCartItems.setItemAnimator(new DefaultItemAnimator());
        checkoutAdapter = new CheckoutAdapter(activity, productList,this);
        binding.rvCartItems.setAdapter(checkoutAdapter);
    }

    private void listener() {

    }


    @Override
    public void onProductItemClick(int position, MProduct mProduct) {

    }
}

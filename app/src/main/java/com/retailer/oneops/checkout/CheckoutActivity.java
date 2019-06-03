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
import com.retailer.oneops.checkout.model.MCart;
import com.retailer.oneops.checkout.model.MCartDetail;
import com.retailer.oneops.checkout.presenter.CheckoutPresenter;
import com.retailer.oneops.checkout.viewinterface.CheckoutViewInterface;
import com.retailer.oneops.dashboard.viewinterface.MyInventViewInterface;
import com.retailer.oneops.databinding.ActivityCheckoutBinding;
import com.retailer.oneops.databinding.MyInventoryFragmentBinding;
import com.retailer.oneops.myinventory.model.MInventory;
import com.retailer.oneops.productListing.adapter.ProductListAdapter;
import com.retailer.oneops.productListing.model.MProduct;
import com.retailer.oneops.productListing.presenter.ProductListingPresenter;
import com.retailer.oneops.productListing.viewinterface.ProductListingViewInterface;
import com.retailer.oneops.util.CommonClickHandler;

import java.util.ArrayList;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity implements CheckoutAdapter.CallBack, CheckoutViewInterface {

    private Activity activity;
    private ActivityCheckoutBinding binding;
    private CheckoutAdapter checkoutAdapter;
    private List<MCart> productList = new ArrayList<>();
    private CheckoutPresenter checkoutPresenter;
    private CheckoutViewInterface checkoutViewInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_checkout);

        activity = CheckoutActivity.this;
        checkoutViewInterface = this;

        initialization();
        bindRecyclerView();
        listener();
    }

    private void initialization() {
        binding.header.setHandler(new CommonClickHandler(activity));
        binding.header.tvMainHeading.setText(R.string.Checkout);

        checkoutPresenter = new CheckoutPresenter(checkoutViewInterface, activity);
        checkoutPresenter.getCartDetails();
    }

    public void bindRecyclerView() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity, RecyclerView.VERTICAL, false);
        binding.rvCartItems.setLayoutManager(mLayoutManager);
        binding.rvCartItems.setHasFixedSize(true);
        binding.rvCartItems.setItemAnimator(new DefaultItemAnimator());
        checkoutAdapter = new CheckoutAdapter(activity, productList, this);
        binding.rvCartItems.setAdapter(checkoutAdapter);
    }

    private void listener() {

    }


    @Override
    public void onSuccessfulListing(List<MInventory> mInventoryList, String message) {

    }

    @Override
    public void onSuccessfulDeleteItem() {

    }

    @Override
    public void onFailedListing(String errorMessage) {

    }

    @Override
    public void onProductItemClick(int position, MCartDetail mCartDetail) {

    }
}

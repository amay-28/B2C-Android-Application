package com.retailer.oneops.product;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.retailer.oneops.R;
import com.retailer.oneops.adapter.ViewPagerAdapter;
import com.retailer.oneops.databinding.ActivityProductDetailBinding;
import com.retailer.oneops.databinding.ActivityProductListingBinding;
import com.retailer.oneops.myinventory.AddToInventoryActivity;
import com.retailer.oneops.productListing.adapter.ProductListAdapter;
import com.retailer.oneops.productListing.model.MProduct;
import com.retailer.oneops.productListing.presenter.ProductListingPresenter;
import com.retailer.oneops.productListing.viewinterface.ProductListingViewInterface;
import com.retailer.oneops.util.CommonClickHandler;
import com.retailer.oneops.util.MyDialogProgress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import static com.retailer.oneops.util.Constant.COMING_SOON;
import static com.retailer.oneops.util.Constant.SORT_HIGH_TO_LOW;
import static com.retailer.oneops.util.Constant.SORT_LOW_TO_HIGH;
import static com.retailer.oneops.util.Constant.SORT_NEW_FIRST;

public class ProductDetailActivity extends AppCompatActivity {

    private ActivityProductDetailBinding binding;
    private Context context;
    private Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_detail);
        activity = this;
        setupToolbar();
        initViews();
        listener();
    }

    public void setupToolbar() {
        binding.header.setHandler(new CommonClickHandler(activity));
        binding.header.tvMainHeading.setText("");
    }

    public void initViews() {
        setupViewPager(binding.viewPager);
    }

    public void listener() {

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());


        /*if (bannerImageList != null) {
            for (String imageUrl : bannerImageList) {
                adapter.addFrag(AdvertismentFragment.newInstance(imageUrl), imageUrl);
            }

        }*/

        viewPager.setAdapter(adapter);
        binding.dotsIndicator.setViewPager(viewPager);
    }

}

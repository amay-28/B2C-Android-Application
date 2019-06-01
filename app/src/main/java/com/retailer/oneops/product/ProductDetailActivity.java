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
import com.retailer.oneops.checkout.CheckoutActivity;
import com.retailer.oneops.databinding.ActivityProductDetailBinding;
import com.retailer.oneops.databinding.ActivityProductListingBinding;
import com.retailer.oneops.fragment.ViewPagerFragment;
import com.retailer.oneops.myinventory.AddToInventoryActivity;
import com.retailer.oneops.product.model.MImage;
import com.retailer.oneops.product.presenter.ProductDetailPresenter;
import com.retailer.oneops.product.viewinterface.ProductDetailViewInterface;
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

public class ProductDetailActivity extends AppCompatActivity implements ProductDetailViewInterface {

    private ActivityProductDetailBinding binding;
    private Context context;
    private Activity activity;
    private Bundle bundle;
    private int productId;
    private boolean isFromInventory = false;
    private ProductDetailPresenter productDetailPresenter;
    private ProductDetailViewInterface productDetailViewInterface;
    private MProduct mProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_detail);
        activity = this;
        productDetailViewInterface = this;
        setupToolbar();
        initViews();
        listener();
    }

    public void setupToolbar() {
       /* binding.header.setHandler(new CommonClickHandler(activity));
        binding.header.tvMainHeading.setText("");*/
    }

    public void initViews() {
        productDetailPresenter = new ProductDetailPresenter(productDetailViewInterface, activity);

        bundle = getIntent().getExtras();

        if (bundle != null) {
            if (bundle.containsKey("productId")) {
                productId = bundle.getInt("productId");
                productDetailPresenter.getProductDetailTask(productId);
            }
            if (bundle.containsKey("isFromInventory")) {
                isFromInventory = bundle.getBoolean("isFromInventory");
                if (bundle.getBoolean("isFromInventory")) {
                    binding.btnAddToInventory.setText(getString(R.string.Add_to_Cart));
                }
            }
        }
    }

    public void listener() {
        if (isFromInventory) {
            binding.btnAddToInventory.setOnClickListener(v -> startActivity(new Intent(activity, CheckoutActivity.class)));
        } else {
            startActivity(AddToInventoryActivity.getIntent(activity, mProduct, null));
        }
    }

    private void setupViewPager(ViewPager viewPager, List<MImage> imageList) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        if (imageList != null) {
            for (MImage mImage : imageList) {
                adapter.addFrag(ViewPagerFragment.newInstance(mImage.getUrl()), mImage.getUrl());
            }

        }

        viewPager.setAdapter(adapter);
        binding.dotsIndicator.setViewPager(viewPager);
    }


    @Override
    public void onSuccessfullyGetDetail(MProduct mProduct, String message) {
        this.mProduct = mProduct;
        List<MImage> imageList = new ArrayList<>();
        for (int i = 0; i < mProduct.getImages().size(); i++) {
            imageList.add(mProduct.getImages().get(i));
        }
        setupViewPager(binding.viewPager, imageList);
    }

    @Override
    public void onFailToUpdate(String errorMessage) {

    }
}

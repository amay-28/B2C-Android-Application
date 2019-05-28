package com.retailer.oneops.productListing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.retailer.oneops.R;
import com.retailer.oneops.databinding.ActivityProductListingBinding;
import com.retailer.oneops.productListing.adapter.ProductListAdapter;
import com.retailer.oneops.productListing.model.MProduct;
import com.retailer.oneops.productListing.presenter.ProductListingPresenter;
import com.retailer.oneops.productListing.viewinterface.ProductListingViewInterface;
import com.retailer.oneops.util.CommonClickHandler;
import com.retailer.oneops.util.MyDialogProgress;
import com.google.android.material.bottomsheet.BottomSheetDialog;

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

import static com.retailer.oneops.util.Constant.COMING_SOON;
import static com.retailer.oneops.util.Constant.SORT_HIGH_TO_LOW;
import static com.retailer.oneops.util.Constant.SORT_LOW_TO_HIGH;
import static com.retailer.oneops.util.Constant.SORT_NEW_FIRST;

public class ProductListingActivity extends AppCompatActivity implements ProductListingViewInterface {

    private ActivityProductListingBinding mBinding;
    private Context context;
    private Activity activity;
    private List<MProduct> productList = new ArrayList<>();
    private BottomSheetDialog bottomSheetDialog;
    private ProductListAdapter productListAdapter;
    private ProductListingPresenter productListingPresenter;
    private ProductListingViewInterface productListingViewInterface;
    private int categoryId;
    private int visibleItemCount, totalItemCount, pastVisibleItems;
    LinearLayoutManager mLayoutManager;
    private boolean loading = true;
    private String sort_key;
    private int DEFAULT_OFFSET = 15, DEFAULT_LIMIT = 15;
    private boolean isFirstTime = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_product_listing);
        activity = this;
        productListingViewInterface = this;
        getIntentData();
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
        isFirstTime = true;
        callPresenterProductListing(DEFAULT_LIMIT, 0, false, sort_key, false);

        mLayoutManager = new LinearLayoutManager(activity, RecyclerView.VERTICAL, false);
        mBinding.rvProducts.setLayoutManager(mLayoutManager);
        mBinding.rvProducts.setHasFixedSize(true);
        mBinding.rvProducts.setItemAnimator(new DefaultItemAnimator());
        productListAdapter = new ProductListAdapter(activity, productList);
        mBinding.rvProducts.setAdapter(productListAdapter);

        mBinding.rvProducts.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = mLayoutManager.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();
                if (loading) {
                    if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                        loading = false;
                        if (!isFirstTime)
                            callPresenterProductListing(DEFAULT_LIMIT, productList.size(), false, "0", true);
                        isFirstTime = false;
                    }
                }
            }
        });

        final SwipeRefreshLayout pullToRefresh = mBinding.swipeToRefresh;
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loading =true;
                checkLimitOffsetConditionSort();
                //productList.clear();
                callPresenterProductListing(DEFAULT_LIMIT, 0, false, "0", false);
                pullToRefresh.setRefreshing(false);

            }
        });

        mBinding.btnContinue.setOnClickListener(v -> finish());
    }

    public void callPresenterProductListing(int limit, int offset, boolean isSort, String sort_key, boolean isPullRequest) {
        if (!loading)
            return;

        if (!MyDialogProgress.isOpen(activity)) {
            //    if (!isPullRequest && !MyDialogProgress.isOpen(activity)) {
            MyDialogProgress.open(activity);
            //   }
        }

        Map<String, String> map = new HashMap<>();
        map.put("categoryId", String.valueOf(categoryId));
        map.put("limit", String.valueOf(limit));
        map.put("eager", "images");
        if (offset < DEFAULT_OFFSET) {
            map.put("offset", String.valueOf(offset));
        } else {
            map.put("offset", String.valueOf(DEFAULT_OFFSET));
        }

        if (isSort)
            map.put("sort", sort_key);


        loading = false;

        if (!isPullRequest)
            productList.clear();

        productListingPresenter = new ProductListingPresenter(productListingViewInterface, activity);
        productListingPresenter.onProductListing(map);
    }

    public void listener() {
        mBinding.rlSort.setOnClickListener(v -> bottomSheetDialog.show());
        bottomSheetDialog.findViewById(R.id.ivCross).setOnClickListener(v -> bottomSheetDialog.dismiss());


        bottomSheetDialog.findViewById(R.id.tvWhatsNew).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            checkLimitOffsetConditionSort();
            callPresenterProductListing(DEFAULT_LIMIT, 0, true, SORT_NEW_FIRST, false);
        });

        bottomSheetDialog.findViewById(R.id.tvLowToHigh).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            checkLimitOffsetConditionSort();
            callPresenterProductListing(DEFAULT_LIMIT, 0, true, SORT_LOW_TO_HIGH, false);
        });
        bottomSheetDialog.findViewById(R.id.tvHighToLow).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            checkLimitOffsetConditionSort();
            callPresenterProductListing(DEFAULT_LIMIT, 0, true, SORT_HIGH_TO_LOW, false);
        });
        bottomSheetDialog.findViewById(R.id.tvPopularity).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            //  checkLimitOffsetConditionSort();
            Toast.makeText(activity, COMING_SOON, Toast.LENGTH_SHORT).show();
        });

        bottomSheetDialog.findViewById(R.id.tvDiscount).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            //  checkLimitOffsetCondition();
            Toast.makeText(activity, COMING_SOON, Toast.LENGTH_SHORT).show();
        });

        bottomSheetDialog.findViewById(R.id.tvDeliveryTime).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            //   checkLimitOffsetCondition();
            Toast.makeText(activity, COMING_SOON, Toast.LENGTH_SHORT).show();
        });
    }

    private void checkLimitOffsetCondition() {
        if (productList.size() < DEFAULT_LIMIT) {
            DEFAULT_LIMIT = 15;
            callPresenterProductListing(DEFAULT_LIMIT, DEFAULT_OFFSET, false, "0", false);
        } else {
            DEFAULT_LIMIT = productList.size();
            callPresenterProductListing(DEFAULT_LIMIT, DEFAULT_OFFSET, false, "0", false);
        }
    }


    private void checkLimitOffsetConditionSort() {
        if (productList.size() < DEFAULT_LIMIT) {
            DEFAULT_LIMIT = 15;
        } else {
            DEFAULT_LIMIT = productList.size();
        }
    }

    private void getIntentData() {
        categoryId = getIntent().getIntExtra("categoryId", 0);
    }

    public static Intent getIntent(Activity activity, int categoryId) {
        Intent intent = new Intent(activity, ProductListingActivity.class);
        intent.putExtra("categoryId", categoryId);
        return intent;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onSuccessfulProductListing(List<MProduct> mProductListing, String message) {
        MyDialogProgress.close(activity);
        DEFAULT_LIMIT = 15;
        this.productList.addAll(mProductListing);

        if (productList != null && productList.size() != 0) {
            mBinding.rlRoot.setVisibility(View.VISIBLE);
            mBinding.llNoRecordFind.setVisibility(View.GONE);
            MyDialogProgress.close(activity);
            productListAdapter.notifyDataSetChanged();
        } else {
            mBinding.rlRoot.setVisibility(View.GONE);
            MyDialogProgress.close(activity);

            if (productList.size() == 0)
                mBinding.llNoRecordFind.setVisibility(View.VISIBLE);
        }

        loading = true;
    }

    @Override
    public void onFailedProductListing(String errorMessage) {
        MyDialogProgress.close(activity);
        Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show();

        loading = false;
    }
}

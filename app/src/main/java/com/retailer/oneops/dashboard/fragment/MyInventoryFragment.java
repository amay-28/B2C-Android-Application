package com.retailer.oneops.dashboard.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.retailer.oneops.R;
import com.retailer.oneops.auth.activity.LoginActivity;
import com.retailer.oneops.auth.model.MUser;
import com.retailer.oneops.bankDetail.activity.AddBankDetailActivity;
import com.retailer.oneops.bankDetail.activity.BankDetailActivity;
import com.retailer.oneops.dashboard.activity.EditProfileActivity;
import com.retailer.oneops.dashboard.activity.MyProfileActivity;
import com.retailer.oneops.dashboard.adapter.MyInventoryAdapter;
import com.retailer.oneops.dashboard.presenter.MyInventoryPresenter;
import com.retailer.oneops.dashboard.viewinterface.MyInventViewInterface;
import com.retailer.oneops.databinding.MoreFragmentLayoutBinding;
import com.retailer.oneops.databinding.MyInventoryFragmentBinding;
import com.retailer.oneops.product.AddProductActivity;
import com.retailer.oneops.productListing.adapter.ProductListAdapter;
import com.retailer.oneops.productListing.model.MProduct;
import com.retailer.oneops.productListing.presenter.ProductListingPresenter;
import com.retailer.oneops.productListing.viewinterface.ProductListingViewInterface;
import com.retailer.oneops.util.DialogUtil;
import com.retailer.oneops.util.MyDialogProgress;
import com.retailer.oneops.util.OnDialogItemClickListener;
import com.retailer.oneops.util.Session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import static android.app.Activity.RESULT_OK;
import static com.retailer.oneops.util.Constant.COMING_SOON;
import static com.retailer.oneops.util.Constant.IS_LOGIN;
import static com.retailer.oneops.util.Constant.NO;
import static com.retailer.oneops.util.Constant.SORT_HIGH_TO_LOW;
import static com.retailer.oneops.util.Constant.SORT_LOW_TO_HIGH;
import static com.retailer.oneops.util.Constant.SORT_NEW_FIRST;

public class MyInventoryFragment extends Fragment implements MyInventViewInterface, MyInventoryAdapter.CallBack {
    private Activity activity;
    private MyInventoryFragmentBinding binding;
    private MUser loggedInUser;
    private List<MProduct> productList = new ArrayList<>();
    private BottomSheetDialog bottomSheetDialog;
    private MyInventoryAdapter myInventoryAdapter;
    private MyInventoryPresenter myInventoryPresenter;
    private MyInventViewInterface myInventViewInterface;
    private int visibleItemCount, totalItemCount, pastVisibleItems;
    LinearLayoutManager mLayoutManager;
    private boolean loading = true;
    private String sort_key;
    private int DEFAULT_OFFSET = 15, DEFAULT_LIMIT = 15;
    private boolean isFirstTime = false;
    private boolean isPhysicalInventory = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        activity = getActivity();
        binding = DataBindingUtil.inflate(inflater, R.layout.my_inventory_fragment, container, false);
        View view = binding.getRoot();
        loggedInUser = new Session(activity).getUserProfile();
        myInventViewInterface = this;
        initialization();
        listener();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        listener();
    }

    private void initialization() {
        bottomSheetDialog = new BottomSheetDialog(activity, R.style.TransparentDialogBackground);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_sort);
        isFirstTime = true;


        mLayoutManager = new LinearLayoutManager(activity, RecyclerView.VERTICAL, false);
        binding.rvProducts.setLayoutManager(mLayoutManager);
        binding.rvProducts.setHasFixedSize(true);
        binding.rvProducts.setItemAnimator(new DefaultItemAnimator());
        myInventoryAdapter = new MyInventoryAdapter(activity, productList, this);
        binding.rvProducts.setAdapter(myInventoryAdapter);

        binding.rvProducts.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = mLayoutManager.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();
                if (loading) {
                    if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                        loading = false;
                        if (!isFirstTime && isPhysicalInventory) {
                            callPhysicalListing(DEFAULT_LIMIT, productList.size(), false, "0", true);
                        }
                        isFirstTime = false;
                    }
                }
            }
        });

        final SwipeRefreshLayout pullToRefresh = binding.swipeToRefresh;
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loading = true;
                checkLimitOffsetConditionSort();
                //productList.clear();
                if (isPhysicalInventory)
                    callPhysicalListing(DEFAULT_LIMIT, 0, false, "0", false);
                pullToRefresh.setRefreshing(false);

            }
        });

    }

    private void listener() {
        binding.llVirtualInventory.setOnClickListener(v -> virtualInventoryUI());
        binding.llPhysicalInventory.setOnClickListener(v -> physicalInventoryUI());

        binding.rlSort.setOnClickListener(v -> bottomSheetDialog.show());
        bottomSheetDialog.findViewById(R.id.ivCross).setOnClickListener(v -> bottomSheetDialog.dismiss());


        bottomSheetDialog.findViewById(R.id.tvWhatsNew).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            checkLimitOffsetConditionSort();
            if (isPhysicalInventory)
                callPhysicalListing(DEFAULT_LIMIT, 0, true, SORT_NEW_FIRST, false);

        });

        bottomSheetDialog.findViewById(R.id.tvLowToHigh).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            checkLimitOffsetConditionSort();
            if (isPhysicalInventory)
                callPhysicalListing(DEFAULT_LIMIT, 0, true, SORT_LOW_TO_HIGH, false);
        });
        bottomSheetDialog.findViewById(R.id.tvHighToLow).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            checkLimitOffsetConditionSort();
            if (isPhysicalInventory)
                callPhysicalListing(DEFAULT_LIMIT, 0, true, SORT_HIGH_TO_LOW, false);
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

    private void checkLimitOffsetConditionSort() {
        if (productList.size() < DEFAULT_LIMIT) {
            DEFAULT_LIMIT = 15;
        } else {
            DEFAULT_LIMIT = productList.size();
        }
    }


    public void callPhysicalListing(int limit, int offset, boolean isSort, String sort_key, boolean isPullRequest) {
        if (!loading)
            return;

        if (!MyDialogProgress.isOpen(activity)) {
            MyDialogProgress.open(activity);
        }

        Map<String, String> map = new HashMap<>();
        map.put("limit", String.valueOf(limit));
        //map.put("eager", "images");
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

        myInventoryPresenter = new MyInventoryPresenter(myInventViewInterface, activity);
        myInventoryPresenter.onInventoryProductList(map);
    }

    public void virtualInventoryUI() {
        isPhysicalInventory = false;
        binding.llVirtualInventory.setBackground(getResources().getDrawable(R.drawable.curve_back_right_flat_blue));
        binding.tvVirtualInventory.setTextColor(getResources().getColor(R.color.white));
        binding.llPhysicalInventory.setBackground(getResources().getDrawable(R.drawable.curve_back_left_flat_grey));
        binding.tvPhysicalInventory.setTextColor(getResources().getColor(R.color.black));
    }

    public void physicalInventoryUI() {
        isPhysicalInventory = true;
        binding.llVirtualInventory.setBackground(getResources().getDrawable(R.drawable.curve_back_right_flat_grey));
        binding.tvVirtualInventory.setTextColor(getResources().getColor(R.color.black));
        binding.llPhysicalInventory.setBackground(getResources().getDrawable(R.drawable.curve_back_left_flat_blue));
        binding.tvPhysicalInventory.setTextColor(getResources().getColor(R.color.white));

        callPhysicalListing(DEFAULT_LIMIT, 0, false, sort_key, false);
    }

    @Override
    public void onSuccessfulProductListing(List<MProduct> mProductListing, String message) {
        MyDialogProgress.close(activity);
        DEFAULT_LIMIT = 15;
        this.productList.addAll(mProductListing);

        if (productList != null && productList.size() != 0) {
            binding.swipeToRefresh.setVisibility(View.VISIBLE);
            binding.llNoRecordFind.setVisibility(View.GONE);
            MyDialogProgress.close(activity);
            myInventoryAdapter.notifyDataSetChanged();
        } else {
            binding.swipeToRefresh.setVisibility(View.GONE);
            MyDialogProgress.close(activity);

            if (productList.size() == 0)
                binding.llNoRecordFind.setVisibility(View.VISIBLE);
        }

        loading = true;
    }

    @Override
    public void onFailedProductListing(String errorMessage) {
        MyDialogProgress.close(activity);
        Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show();

        loading = false;
    }

    @Override
    public void onProductItemClick(int position, MProduct mProduct) {

    }

    @Override
    public void onEditProduct(int position, MProduct mProduct) {

    }

    @Override
    public void onDeleteProduct(int position, MProduct mProduct) {

    }
}

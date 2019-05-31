package com.retailer.oneops.dashboard.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.retailer.oneops.R;
import com.retailer.oneops.auth.model.MUser;
import com.retailer.oneops.dashboard.adapter.PhysicalInventoryAdapter;
import com.retailer.oneops.dashboard.adapter.VirtualInventoryAdapter;
import com.retailer.oneops.dashboard.presenter.MyInventoryPresenter;
import com.retailer.oneops.dashboard.viewinterface.MyInventViewInterface;
import com.retailer.oneops.databinding.MyInventoryFragmentBinding;
import com.retailer.oneops.myinventory.AddToInventoryActivity;
import com.retailer.oneops.myinventory.model.MInventory;
import com.retailer.oneops.product.AddProductActivity;
import com.retailer.oneops.product.ProductDetailActivity;
import com.retailer.oneops.productListing.model.MProduct;
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
import static com.retailer.oneops.util.Constant.PHYSICAL_INVENTORY_LIST;
import static com.retailer.oneops.util.Constant.SORT_HIGH_TO_LOW;
import static com.retailer.oneops.util.Constant.SORT_LOW_TO_HIGH;
import static com.retailer.oneops.util.Constant.SORT_NEW_FIRST;
import static com.retailer.oneops.util.Constant.VIRTUAL_INVENTORY_LIST;

public class MyInventoryFragment extends Fragment implements MyInventViewInterface,
        PhysicalInventoryAdapter.CallBack, VirtualInventoryAdapter.CallBack, OnDialogItemClickListener {
    private Activity activity;
    private MyInventoryFragmentBinding binding;
    private MUser loggedInUser;
    private List<MProduct> productList = new ArrayList<>();
    private List<MInventory> virtualList = new ArrayList<>();
    private BottomSheetDialog bottomSheetDialog;
    private PhysicalInventoryAdapter physicalInventoryAdapter;
    private VirtualInventoryAdapter virtualInventoryAdapter;
    private MyInventoryPresenter myInventoryPresenter;
    private MyInventViewInterface myInventViewInterface;
    private int visibleItemCount, totalItemCount, pastVisibleItems;
    LinearLayoutManager mLayoutManager;
    private boolean loadingVirtual = true;
    private boolean loadingPhysical = true;
    private int DEFAULT_OFFSET = 15, DEFAULT_LIMIT = 15;
    private boolean isFirstTime = false;
    private boolean isPhysicalInventory = false;
    private String sort_key;
    private int OPEN_EDIT_VIRTUAL_INVENTORY = 100;
    private int deleteVirtualInventoryItemId;
    private int deletePhysicalProductId;
    private int deleteProductItemId;
    private int deletePosition;

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
        bindRecyclerViewForVirtual();
    }

    public void bindRecyclerViewForPhysical() {
        binding.rvProducts.setLayoutManager(mLayoutManager);
        binding.rvProducts.setHasFixedSize(true);
        binding.rvProducts.setItemAnimator(new DefaultItemAnimator());
        physicalInventoryAdapter = new PhysicalInventoryAdapter(activity, productList, this);
        binding.rvProducts.setAdapter(physicalInventoryAdapter);

        binding.rvProducts.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = mLayoutManager.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();
                if (loadingPhysical) {
                    if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                        loadingPhysical = false;
                        createPhysicalMapForAPICall(DEFAULT_LIMIT, productList.size(), false, "0", true, false);
                        isFirstTime = false;
                    }
                }
            }
        });

        final SwipeRefreshLayout pullToRefresh = binding.swipeToRefresh;
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadingPhysical = true;
                checkLimitOffsetConditionSort();
                createPhysicalMapForAPICall(DEFAULT_LIMIT, 0, false, "0", false, false);
                pullToRefresh.setRefreshing(false);

            }
        });

    }

    public void bindRecyclerViewForVirtual() {
        binding.rvProducts.setLayoutManager(mLayoutManager);
        binding.rvProducts.setHasFixedSize(true);
        binding.rvProducts.setItemAnimator(new DefaultItemAnimator());
        virtualInventoryAdapter = new VirtualInventoryAdapter(activity, virtualList, this);
        binding.rvProducts.setAdapter(virtualInventoryAdapter);

        createVirtualMapForAPICall(DEFAULT_LIMIT, 0, false, sort_key, false, true);

        binding.rvProducts.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = mLayoutManager.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();
                if (loadingVirtual) {
                    if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                        loadingVirtual = false;
                        createVirtualMapForAPICall(DEFAULT_LIMIT, productList.size(), false, "0", true, true);
                        isFirstTime = false;
                    }
                }
            }
        });

        final SwipeRefreshLayout pullToRefresh = binding.swipeToRefresh;
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadingVirtual = true;
                checkLimitOffsetConditionSort();
                //productList.clear();
                createVirtualMapForAPICall(DEFAULT_LIMIT, 0, false, "0", false, true);
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
                createPhysicalMapForAPICall(DEFAULT_LIMIT, 0, true, SORT_NEW_FIRST, false, false);
            else
                createVirtualMapForAPICall(DEFAULT_LIMIT, 0, true, SORT_NEW_FIRST, false, true);

        });

        bottomSheetDialog.findViewById(R.id.tvLowToHigh).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            checkLimitOffsetConditionSort();
            if (isPhysicalInventory)
                createPhysicalMapForAPICall(DEFAULT_LIMIT, 0, true, SORT_LOW_TO_HIGH, false, false);
            else
                createVirtualMapForAPICall(DEFAULT_LIMIT, 0, true, SORT_LOW_TO_HIGH, false, true);
        });
        bottomSheetDialog.findViewById(R.id.tvHighToLow).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            checkLimitOffsetConditionSort();
            if (isPhysicalInventory)
                createPhysicalMapForAPICall(DEFAULT_LIMIT, 0, true, SORT_HIGH_TO_LOW, false, false);
            else
                createVirtualMapForAPICall(DEFAULT_LIMIT, 0, true, SORT_HIGH_TO_LOW, false, true);
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

    public void createVirtualMapForAPICall(int limit, int offset, boolean isSort, String sort_key, boolean isPullRequest, boolean isVirtual) {
        if (!loadingVirtual)
            return;

        if (!MyDialogProgress.isOpen(activity)) {
            MyDialogProgress.open(activity);
        }

        Map<String, String> map = new HashMap<>();
        map.put("limit", String.valueOf(limit));
        map.put("eager", "images");
        if (offset < DEFAULT_OFFSET) {
            map.put("offset", String.valueOf(offset));
        } else {
            map.put("offset", String.valueOf(DEFAULT_OFFSET));
        }

        if (isSort)
            map.put("sort", sort_key);

        loadingVirtual = false;
        loadingPhysical = true;
        callInventoryListing(map, isVirtual, isPullRequest);
    }

    public void createPhysicalMapForAPICall(int limit, int offset, boolean isSort, String sort_key, boolean isPullRequest, boolean isVirtual) {
        if (!loadingPhysical)
            return;

        if (!MyDialogProgress.isOpen(activity)) {
            MyDialogProgress.open(activity);
        }

        Map<String, String> map = new HashMap<>();
        map.put("limit", String.valueOf(limit));
        // map.put("eager", "images");
        map.put("eager", "[images,category.parent.parent]");
        if (offset < DEFAULT_OFFSET) {
            map.put("offset", String.valueOf(offset));
        } else {
            map.put("offset", String.valueOf(DEFAULT_OFFSET));
        }

        if (isSort)
            map.put("sort", sort_key);

        loadingPhysical = false;
        loadingVirtual = true;
        callInventoryListing(map, isVirtual, isPullRequest);
    }

    public void callInventoryListing(Map<String, String> map, boolean isVirtualList, boolean isPullRequest) {
        if (isVirtualList) {
            if (!isPullRequest)
                virtualList.clear();
            myInventoryPresenter = new MyInventoryPresenter(myInventViewInterface, activity);
            myInventoryPresenter.onProductInventoryList(map, VIRTUAL_INVENTORY_LIST);
        } else {
            if (!isPullRequest)
                productList.clear();
            myInventoryPresenter = new MyInventoryPresenter(myInventViewInterface, activity);
            myInventoryPresenter.onProductInventoryList(map, PHYSICAL_INVENTORY_LIST);
        }
    }

    public void virtualInventoryUI() {
        isPhysicalInventory = false;
        binding.llVirtualInventory.setBackground(getResources().getDrawable(R.drawable.curve_back_right_flat_blue));
        binding.tvVirtualInventory.setTextColor(getResources().getColor(R.color.white));
        binding.llPhysicalInventory.setBackground(getResources().getDrawable(R.drawable.curve_back_left_flat_grey));
        binding.tvPhysicalInventory.setTextColor(getResources().getColor(R.color.black));
        bindRecyclerViewForVirtual();
        createVirtualMapForAPICall(DEFAULT_LIMIT, 0, false, sort_key, false, true);
    }

    public void physicalInventoryUI() {
        isPhysicalInventory = true;
        binding.llVirtualInventory.setBackground(getResources().getDrawable(R.drawable.curve_back_right_flat_grey));
        binding.tvVirtualInventory.setTextColor(getResources().getColor(R.color.black));
        binding.llPhysicalInventory.setBackground(getResources().getDrawable(R.drawable.curve_back_left_flat_blue));
        binding.tvPhysicalInventory.setTextColor(getResources().getColor(R.color.white));
        bindRecyclerViewForPhysical();
        createPhysicalMapForAPICall(DEFAULT_LIMIT, 0, false, sort_key, false, false);
    }

    @Override
    public void onSuccessfulProductListing(List<MProduct> mProductListing, String message) {
        MyDialogProgress.close(activity);
        DEFAULT_LIMIT = 15;
        this.virtualList.clear();
        this.productList.addAll(mProductListing);

        if (productList != null && productList.size() != 0) {
            binding.swipeToRefresh.setVisibility(View.VISIBLE);
            binding.llNoRecordFind.setVisibility(View.GONE);
            MyDialogProgress.close(activity);
            physicalInventoryAdapter.notifyDataSetChanged();
        } else {
            binding.swipeToRefresh.setVisibility(View.GONE);
            MyDialogProgress.close(activity);

            if (productList.size() == 0)
                binding.llNoRecordFind.setVisibility(View.VISIBLE);
        }

        loadingPhysical = true;
    }

    @Override
    public void onSuccessfulVirtualListing(List<MInventory> mInventoryList, String message) {
        MyDialogProgress.close(activity);
        DEFAULT_LIMIT = 15;
        this.productList.clear();
        this.virtualList.addAll(mInventoryList);

        if (virtualList != null && virtualList.size() != 0) {
            binding.swipeToRefresh.setVisibility(View.VISIBLE);
            binding.llNoRecordFind.setVisibility(View.GONE);
            MyDialogProgress.close(activity);
            virtualInventoryAdapter.notifyDataSetChanged();
        } else {
            binding.swipeToRefresh.setVisibility(View.GONE);
            MyDialogProgress.close(activity);

            if (virtualList.size() == 0)
                binding.llNoRecordFind.setVisibility(View.VISIBLE);
        }

        loadingVirtual = true;
    }

    @Override
    public void onSuccessfulDeleteItem() {
        if (isPhysicalInventory) {
            this.productList.remove(deletePosition);
            physicalInventoryAdapter.notifyDataSetChanged();

        } else {
            /*loadingVirtual = true;
            loadingPhysical = false;
            createVirtualMapForAPICall(DEFAULT_LIMIT, 0, false, sort_key, false, true);*/
            this.virtualList.remove(deletePosition);
            virtualInventoryAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onFailedProductListing(String errorMessage) {
        MyDialogProgress.close(activity);
        Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show();

        loadingVirtual = false;
        loadingPhysical = false;
    }


    @Override
    public void onVirtualItemClick(int position, MInventory mInventory) {
        startActivity(new Intent(activity, ProductDetailActivity.class)
                .putExtra("productId", mInventory.getProduct_id()));

    }

    @Override
    public void onEditVirtualProduct(int position, MInventory mInventory) {
        startActivityForResult(AddToInventoryActivity.getIntent(activity, null, mInventory), OPEN_EDIT_VIRTUAL_INVENTORY);
    }

    @Override
    public void onDeleteVirtualProduct(int position, MInventory mInventory) {
        deletePosition = position;
        deleteProductItemId = mInventory.getId();
        DialogUtil.showOkCancelDialog(activity, getString(R.string.delete_popup), this);
    }

    @Override
    public void onPhysicalItemClick(int position, MProduct mProduct) {
        startActivity(new Intent(activity, ProductDetailActivity.class)
                .putExtra("productId", mProduct.getId()));
    }

    @Override
    public void onEditPhysicalProduct(int position, MProduct mProduct) {
        startActivityForResult(AddProductActivity.getIntent(activity, mProduct), OPEN_EDIT_VIRTUAL_INVENTORY);
    }

    @Override
    public void onDeletePhysicalProduct(int position, MProduct mProduct) {
        deletePosition = position;
        deleteProductItemId = Integer.parseInt(mProduct.getId());
        DialogUtil.showOkCancelDialog(activity, getString(R.string.delete_popup), this);
    }

    @Override
    public void onDialogButtonClick(int i, int position) {
        if (isPhysicalInventory)
            myInventoryPresenter.onDeleteInventoryList(deleteProductItemId, PHYSICAL_INVENTORY_LIST);
        else
            myInventoryPresenter.onDeleteInventoryList(deleteProductItemId, VIRTUAL_INVENTORY_LIST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OPEN_EDIT_VIRTUAL_INVENTORY && resultCode == RESULT_OK) {
            loadingVirtual = true;
            createVirtualMapForAPICall(DEFAULT_LIMIT, 0, false, sort_key, false, true);
        }
    }


}

package com.retailer.oneops.product;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonObject;
import com.retailer.oneops.R;
import com.retailer.oneops.adapter.ViewPagerAdapter;
import com.retailer.oneops.checkout.CheckoutActivity;
import com.retailer.oneops.checkout.adapter.CheckoutAdapter;
import com.retailer.oneops.checkout.model.MCart;
import com.retailer.oneops.databinding.ActivityProductDetailBinding;
import com.retailer.oneops.databinding.ActivityProductListingBinding;
import com.retailer.oneops.fragment.ViewPagerFragment;
import com.retailer.oneops.myinventory.AddToInventoryActivity;
import com.retailer.oneops.product.adapter.ProductVariantAdapter;
import com.retailer.oneops.product.model.MImage;
import com.retailer.oneops.product.presenter.ProductDetailPresenter;
import com.retailer.oneops.product.viewinterface.ProductDetailViewInterface;
import com.retailer.oneops.productListing.adapter.ProductListAdapter;
import com.retailer.oneops.productListing.model.MProduct;
import com.retailer.oneops.productListing.model.MProductVariant;
import com.retailer.oneops.productListing.presenter.ProductListingPresenter;
import com.retailer.oneops.productListing.viewinterface.ProductListingViewInterface;
import com.retailer.oneops.util.CommonClickHandler;
import com.retailer.oneops.util.Constant;
import com.retailer.oneops.util.DialogUtil;
import com.retailer.oneops.util.MyDialogProgress;
import com.retailer.oneops.util.OnDialogItemClickListener;
import com.retailer.oneops.util.Session;
import com.retailer.oneops.util.Utils;

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
import static com.retailer.oneops.util.Constant.INVENTORY_TYPE;
import static com.retailer.oneops.util.Constant.SORT_HIGH_TO_LOW;
import static com.retailer.oneops.util.Constant.SORT_LOW_TO_HIGH;
import static com.retailer.oneops.util.Constant.SORT_NEW_FIRST;
import static com.retailer.oneops.util.Constant.VIRTUAL_INVENTORY;

public class ProductDetailActivity extends AppCompatActivity implements ProductDetailViewInterface, ProductVariantAdapter.CallBack,
        OnDialogItemClickListener {

    private ActivityProductDetailBinding binding;
    private Context context;
    private Activity activity;
    private Bundle bundle;
    private int productId;
    private boolean isFromInventory = false;
    private ProductDetailPresenter productDetailPresenter;
    private ProductDetailViewInterface productDetailViewInterface;
    private MProduct mProduct;
    private ProductVariantAdapter productVariantAdapter;
    private List<MProductVariant> productVariantList = new ArrayList<>();
    private int productVariantId = 0;
    private String inventoryType;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_detail);
        activity = this;
        context = this;
        productDetailViewInterface = this;
        session = new Session(context);
        setupToolbar();
        initViews();
        listener();
    }

    public void setupToolbar() {
        Utils.setUpToolbarWithColor(context, binding.incToolbar.toolbarActionbar, binding.incToolbar.toolbarTitle, "", android.R.color.transparent);
       /* binding.header.setHandler(new CommonClickHandler(activity));
        binding.header.tvMainHeading.setText("");*/
    }

    public void initViews() {
        session.getInventoryType();
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
            if (bundle.containsKey("isVirtualInventory")) {
                boolean isVirtualInventory = bundle.getBoolean("isVirtualInventory");
                if (isVirtualInventory) {
                    inventoryType = Constant.VIRTUAL_INVENTORY;
                } else {
                    inventoryType = Constant.PHYSICAL_INVENTORY;
                    binding.llProductVariant.setVisibility(View.GONE);
                }
            }


        }

        bindRecyclerView();
    }

    private void bindRecyclerView() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false);
        binding.rvProductVariant.setLayoutManager(mLayoutManager);
        binding.rvProductVariant.setHasFixedSize(true);
        binding.rvProductVariant.setItemAnimator(new DefaultItemAnimator());
        productVariantAdapter = new ProductVariantAdapter(activity, productVariantList, this);
        binding.rvProductVariant.setAdapter(productVariantAdapter);
    }

    public void listener() {
        binding.btnAddToInventory.setOnClickListener(v -> {
            if (isFromInventory) {
                if (checkInventoryInCart(inventoryType)) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("productId", mProduct.getId());

                    if (mProduct.getProduct_variant() != null) {
                        if (productVariantId != 0)
                            jsonObject.addProperty("productVariantId", productVariantId);
                        else
                            Toast.makeText(activity, getString(R.string.Please_select_a_variant), Toast.LENGTH_SHORT).show();
                    }
                    //jsonObject.addProperty("quantity",mProduct.ge);

                    productDetailPresenter.addToCartTask(jsonObject);

                } else {
                    DialogUtil.showInventoryChangeDialog(context, getString(R.string.TITLE_You_can_only_order_items_from),
                            getString(R.string.MESSAGE_clear_cart), this);
                    //Toast.makeText(context, getString(R.string.Product_added_with_another_inventory), Toast.LENGTH_SHORT).show();
                }

            } else {
                startActivity(AddToInventoryActivity.getIntent(activity, mProduct, null));
            }
        });

    }

    private boolean checkInventoryInCart(String inventoryType) {
        if (session.getInventoryType() != null) {
            if (session.getInventoryType().equalsIgnoreCase(inventoryType)) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }

    }

    private void setExistingData(MProduct mProduct) {
        strikeThroughText(binding.tvMrp);
        binding.tvProductName.setText(mProduct.getName());
        binding.tvProductDescription.setText(mProduct.getDescription());
        binding.tvProductDetail.setText(mProduct.getDescription());
        binding.tvSellingPrice.setText(mProduct.getPrice());
        binding.tvMrp.setText(mProduct.getCost_price());

        double actualPrice = Double.parseDouble(mProduct.getCost_price());
        double discountedPrice = Double.parseDouble(mProduct.getPrice());
        long discountPercent = calculateProfitPercent(actualPrice, discountedPrice);
        binding.tvDiscountPercent.setText(discountPercent + "% OFF");

        if (mProduct.getProduct_variant() != null) {
            this.productVariantList.addAll(mProduct.getProduct_variant());
            productVariantAdapter.notifyDataSetChanged();
        }

    }

    private void strikeThroughText(TextView textView) {
        textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    public long calculateProfitPercent(double actualPrice, double discountPrice) {
        double discountPercent = 0;
        if (actualPrice > discountPrice) {
            discountPercent = (actualPrice - discountPrice) / actualPrice * 100;
            discountPercent = Math.round(discountPercent);
        }
        return Math.round(discountPercent);
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

        setExistingData(mProduct);
    }

    @Override
    public void onSuccessfullyAddToCart(MCart mCart, String message) {
        if (session.getInventoryType() == null) {
            new Session(activity).setString(INVENTORY_TYPE, inventoryType);
        }

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccessfullyClearCart() {
        session.setInventoryType(null);
        Toast.makeText(context, getString(R.string.Your_cart_is_clear), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFailToUpdate(String errorMessage) {
    }

    @Override
    public void onVariantItemClick(int position, int productVariantId) {
        this.productVariantId = productVariantId;
    }

    @Override
    public void onDialogButtonClick(int i, int position) {
        productDetailPresenter.clearCartTask();
    }
}

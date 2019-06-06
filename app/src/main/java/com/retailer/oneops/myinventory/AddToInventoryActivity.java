package com.retailer.oneops.myinventory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.retailer.oneops.R;
import com.retailer.oneops.auth.model.MCategory;
import com.retailer.oneops.auth.model.MSignUp;
import com.retailer.oneops.auth.presenter.LoginPresenter;
import com.retailer.oneops.databinding.ActivityAddToInventoryDialogBinding;
import com.retailer.oneops.databinding.ActivityDialogBinding;
import com.retailer.oneops.myinventory.model.MInventory;
import com.retailer.oneops.myinventory.presenter.AddToInventoryPresenter;
import com.retailer.oneops.myinventory.viewinterface.AddToInventViewInterface;
import com.retailer.oneops.product.adapter.DialogListAdapter;
import com.retailer.oneops.productListing.model.MProduct;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AddToInventoryActivity extends AppCompatActivity implements AddToInventViewInterface {

    private static Intent intent;
    private String TAG = AddToInventoryActivity.class.getSimpleName();
    ActivityAddToInventoryDialogBinding binding;
    private Activity activity;
    private MProduct mProduct;
    private AddToInventoryPresenter presenter;
    private MInventory mInventory = new MInventory();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_to_inventory_dialog);

        activity = AddToInventoryActivity.this;
        if (intent != null)
            getIntentData();
        initialization();

        listener();
    }

    private void initialization() {
        presenter = new AddToInventoryPresenter(this, activity);
    }

    private void listener() {
        binding.ivCross.setOnClickListener(v -> finish());
        binding.btnSave.setOnClickListener(v -> {
            if (TextUtils.isEmpty(binding.etMargin.getText().toString().trim())) {
                Toast.makeText(activity, getResources().getString(R.string.Please_enter_your_margin), Toast.LENGTH_LONG).show();
            } else {
                if (mProduct != null) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("product_id", Integer.parseInt(mProduct.getId()));
                    jsonObject.addProperty("margin", Integer.parseInt(binding.etMargin.getText().toString().trim()));
                    presenter.performAddToInventoryTask(jsonObject);
                } else {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("margin", Integer.parseInt(binding.etMargin.getText().toString().trim()));
                    presenter.performEditInventoryTask(jsonObject, mInventory.getId());
                }

            }
        });
    }

    private void getIntentData() {
        mProduct = intent.getParcelableExtra("mProduct");
        mInventory = intent.getParcelableExtra("mInventory");

        if (mProduct != null)
            setExistingData(mProduct);
        else
            setExistingDataByInventory(mInventory);
    }

    public static Intent getIntent(Activity activity, MProduct productModel, MInventory inventoryModel) {
        intent = new Intent(activity, AddToInventoryActivity.class);
        intent.putExtra("mProduct", (Parcelable) productModel);
        intent.putExtra("mInventory", (Parcelable) inventoryModel);
        return intent;
    }

    public void setExistingData(MProduct mProduct) {
        binding.tvProductName.setText(mProduct.getName());
        binding.tvProductDescription.setText(mProduct.getDescription());
        binding.tvSellingPrice.setText(mProduct.getPrice());
        binding.tvPrice.setText(mProduct.getCost_price());

        if (mProduct.getImages() != null && mProduct.getImages().size() > 0) {
            Glide.with(activity)
                    .load(mProduct.getImages().get(0).getUrl())
                    .into(binding.ivProduct);
        }
    }

    public void setExistingDataByInventory(MInventory mInventory) {
        binding.btnSave.setText(getString(R.string.save));
        binding.tvProductName.setText(mInventory.getProduct().getName());
        binding.tvProductDescription.setText(mInventory.getProduct().getDescription());
        binding.tvSellingPrice.setText(mInventory.getProduct().getPrice());
        binding.tvPrice.setText(mInventory.getProduct().getCost_price());
        binding.etMargin.setText(String.valueOf(mInventory.getMargin()));

        if (mInventory.getProduct().getImages() != null) {
            Glide.with(activity)
                    .load(mInventory.getProduct().getImages().get(0).getUrl())
                    .into(binding.ivProduct);
        }
    }

    @Override
    public void onSuccessfullyAdd(MInventory mInventory, String message) {
        Toast.makeText(activity, getString(R.string.item_added_to_your_inventory), Toast.LENGTH_LONG).show();
        Intent data = new Intent();
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onFailToAdd(String errorMessage) {

    }
}

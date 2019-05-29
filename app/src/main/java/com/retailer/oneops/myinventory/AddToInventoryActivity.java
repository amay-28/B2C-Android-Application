package com.retailer.oneops.myinventory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.widget.Toast;

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
                mInventory.setProduct_id(Integer.parseInt(mProduct.getId()));
                mInventory.setMargin(Integer.parseInt(binding.etMargin.getText().toString().trim()));
                presenter.performAddToInventoryTask(mInventory);
            }
        });
    }

    private void getIntentData() {
        mProduct = getIntent().getParcelableExtra("mProduct");
        setExistingData(mProduct);
    }

    public static Intent getIntent(Activity activity, MProduct productModel) {
        Intent intent = new Intent(activity, AddToInventoryActivity.class);
        intent.putExtra("mProduct", (Parcelable) productModel);
        return intent;
    }

    public void setExistingData(MProduct mProduct) {
        binding.tvProductName.setText(mProduct.getName());
        binding.tvProductDescription.setText(mProduct.getDescription());
        binding.tvSellingPrice.setText(mProduct.getPrice());
        binding.tvPrice.setText(mProduct.getCost_price());
    }

    @Override
    public void onSuccessfullyAdd(MInventory mInventory, String message) {
        Intent data = new Intent();
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onFailToAdd(String errorMessage) {

    }
}

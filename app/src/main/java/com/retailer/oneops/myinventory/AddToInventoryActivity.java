package com.retailer.oneops.myinventory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.retailer.oneops.R;
import com.retailer.oneops.auth.model.MCategory;
import com.retailer.oneops.databinding.ActivityAddToInventoryDialogBinding;
import com.retailer.oneops.databinding.ActivityDialogBinding;
import com.retailer.oneops.product.adapter.DialogListAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AddToInventoryActivity extends AppCompatActivity {

    private String TAG = AddToInventoryActivity.class.getSimpleName();
    ActivityAddToInventoryDialogBinding binding;
    private Activity activity;
    private List<MCategory> superCategoryList = new ArrayList<>();
    private List<MCategory> categoryList = new ArrayList<>();
    private DialogListAdapter dialogListAdapter;
    private String selectedCategory;

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

    }

    private void listener() {
        binding.ivCross.setOnClickListener(v -> finish());
    }

    private void getIntentData() {
        categoryList = getIntent().getParcelableArrayListExtra("CategoryList");
        selectedCategory = getIntent().getStringExtra("selectedCategory");
    }

    public static Intent getIntent(Activity activity, List<MCategory> mCategoryList, String selectedCategory) {
        Intent intent = new Intent(activity, AddToInventoryActivity.class);
        intent.putParcelableArrayListExtra("CategoryList", (ArrayList<? extends Parcelable>) mCategoryList);
        intent.putExtra("selectedCategory", selectedCategory);
        return intent;
    }


}

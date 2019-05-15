package com.eb.onebandhan.product;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.eb.onebandhan.R;
import com.eb.onebandhan.auth.model.MCategory;
import com.eb.onebandhan.databinding.ActivityDialogBinding;
import com.eb.onebandhan.product.adapter.DialogListAdapter;
import com.eb.onebandhan.product.presenter.DialogPresenter;
import com.eb.onebandhan.product.viewinterface.DialogViewInterface;
import com.eb.onebandhan.util.ShowToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.eb.onebandhan.auth.util.Categoryutil.ZERO;

public class DialogActivity extends AppCompatActivity implements DialogListAdapter.CallBack {

    private String TAG = DialogActivity.class.getSimpleName();
    ActivityDialogBinding binding;
    private Activity activity;
    private List<MCategory> superCategoryList = new ArrayList<>();
    private List<MCategory> categoryList = new ArrayList<>();
    private DialogListAdapter dialogListAdapter;
    private String selectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dialog);

        activity = DialogActivity.this;
        getIntentData();
        initialization();

        listener();
    }

    private void initialization() {
        binding.rvRowItems.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.VERTICAL, false));
        binding.rvRowItems.setHasFixedSize(true);
        binding.rvRowItems.setItemAnimator(new DefaultItemAnimator());
        dialogListAdapter = new DialogListAdapter(activity, categoryList, this, selectedCategory);
        binding.rvRowItems.setAdapter(dialogListAdapter);
    }

    private void listener() {
        binding.ivCross.setOnClickListener(v -> finish());
    }

    private void getIntentData() {
        categoryList = getIntent().getParcelableArrayListExtra("CategoryList");
        selectedCategory = getIntent().getStringExtra("selectedCategory");
    }

    public static Intent getIntent(Activity activity, List<MCategory> mCategoryList, String selectedCategory) {
        Intent intent = new Intent(activity, DialogActivity.class);
        intent.putParcelableArrayListExtra("CategoryList", (ArrayList<? extends Parcelable>) mCategoryList);
        intent.putExtra("selectedCategory", selectedCategory);
        return intent;
    }


    @Override
    public void onCategoryClick(int position, MCategory mCategory) {
        Intent data = new Intent();
        data.putExtra("MCategory", mCategory);
        setResult(RESULT_OK, data);
        finish();
        overridePendingTransition(0, 0);
    }
}

package com.eb.onebandhan.dashboard.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.os.Bundle;

import com.eb.onebandhan.R;
import com.eb.onebandhan.auth.model.MCategory;
import com.eb.onebandhan.dashboard.adapter.SuperCategoryListAdapter;
import com.eb.onebandhan.dashboard.adapter.SuperCategoryListHomeAdapter;
import com.eb.onebandhan.databinding.ActivityCategoryBinding;
import com.eb.onebandhan.util.CommonClickHandler;
import com.eb.onebandhan.util.Constant;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity implements Constant,SuperCategoryListAdapter.CallBack {
    private Activity activity;
    private ActivityCategoryBinding binding;
    private List<MCategory> superCategoryList=new ArrayList<>();
    private SuperCategoryListAdapter superCategoryListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=this;
        binding= DataBindingUtil.setContentView(activity,R.layout.activity_category);
        initialization();
    }

    private void initialization() {
        binding.header.setHandler(new CommonClickHandler(activity));
        binding.header.tvMainHeading.setText(getResources().getString(R.string.categories));
        superCategoryList.addAll(getIntent().getParcelableArrayListExtra(ALL_CATEGORY_LIST));
        binding.rvSuperCategory.setLayoutManager(new LinearLayoutManager(activity));
        binding.rvSuperCategory.setHasFixedSize(true);
        binding.rvSuperCategory.setItemAnimator(new DefaultItemAnimator());
        superCategoryListAdapter = new SuperCategoryListAdapter(activity, superCategoryList, this);
        binding.rvSuperCategory.setAdapter(superCategoryListAdapter);
    }

}

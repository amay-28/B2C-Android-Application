package com.retailer.oneops.dashboard.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.os.Bundle;

import com.retailer.oneops.R;
import com.retailer.oneops.auth.model.MCategory;
import com.retailer.oneops.dashboard.adapter.ExpandableSuperCategoryAdapter;
import com.retailer.oneops.dashboard.adapter.SuperCategoryListAdapter;
import com.retailer.oneops.databinding.ActivityCategoryBinding;
import com.retailer.oneops.util.CommonClickHandler;
import com.retailer.oneops.util.Constant;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity1 extends AppCompatActivity implements Constant,ExpandableSuperCategoryAdapter.CallBack {
    private Activity activity;
    private ActivityCategoryBinding binding;
    private List<MCategory> superCategoryList=new ArrayList<>();
    private ExpandableSuperCategoryAdapter superCategoryListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=this;
        binding= DataBindingUtil.setContentView(activity,R.layout.activity_category);
        initialization();
    }

    private void initialization() {
        /*binding.header.setHandler(new CommonClickHandler(activity));
        binding.header.tvMainHeading.setText(getResources().getString(R.string.categories));
        superCategoryList.addAll(getIntent().getParcelableArrayListExtra(ALL_CATEGORY_LIST));
        superCategoryListAdapter = new ExpandableSuperCategoryAdapter(activity, superCategoryList, this);
        binding.rvSuperCategory.setAdapter(superCategoryListAdapter);*/
    }

}

package com.eb.onebandhan.dashboard.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eb.onebandhan.R;
import com.eb.onebandhan.auth.model.MCategory;
import com.eb.onebandhan.dashboard.activity.CategoryActivity;
import com.eb.onebandhan.dashboard.adapter.BannerListAdapter;
import com.eb.onebandhan.dashboard.adapter.CategoryListAdapter;
import com.eb.onebandhan.dashboard.adapter.CollectionListAdapter;
import com.eb.onebandhan.dashboard.adapter.SuperCategoryListAdapter;
import com.eb.onebandhan.databinding.HomeFragmentLayoutBinding;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

public class HomeFragment extends Fragment implements CollectionListAdapter.CallBack, BannerListAdapter.CallBack, SuperCategoryListAdapter.CallBack, CategoryListAdapter.CallBack {
    private Activity activity;
    private HomeFragmentLayoutBinding binding;
    private List<MCategory> superCategoryList = new ArrayList<>();
    private List<MCategory> categoryList = new ArrayList<>();
    private List<String> bannerList = new ArrayList<>();
    private List<String> collectionList = new ArrayList<>();
    private SuperCategoryListAdapter superCategoryListAdapter;
    private BannerListAdapter bannerListAdapter;
    private CategoryListAdapter categoryListAdapter;
    private CollectionListAdapter collectionListAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
        activity=getActivity();
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment_layout, container, false);
        View view = binding.getRoot();
        initialization();
        listner();
        return view;
    }



    private void initialization() {
        binding.rvSuperCategories.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        binding.rvSuperCategories.setHasFixedSize(true);
        binding.rvSuperCategories.setItemAnimator(new DefaultItemAnimator());
        superCategoryListAdapter = new SuperCategoryListAdapter(activity, superCategoryList, this);
        binding.rvSuperCategories.setAdapter(superCategoryListAdapter);

        // for banner
        binding.rvBanners.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        binding.rvBanners.setHasFixedSize(true);
        binding.rvBanners.setItemAnimator(new DefaultItemAnimator());
        bannerListAdapter = new BannerListAdapter(activity, bannerList, this);
        binding.rvBanners.setAdapter(bannerListAdapter);

        // for category
        binding.rvCategory.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        binding.rvCategory.setHasFixedSize(true);
        binding.rvCategory.setItemAnimator(new DefaultItemAnimator());
        categoryListAdapter = new CategoryListAdapter(activity, categoryList, this);
        binding.rvCategory.setAdapter(categoryListAdapter);

        // for collection
        binding.rvCollection.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        binding.rvCollection.setHasFixedSize(true);
        binding.rvCollection.setItemAnimator(new DefaultItemAnimator());
        collectionListAdapter = new CollectionListAdapter(activity, collectionList, this);
        binding.rvCollection.setAdapter(collectionListAdapter);
    }

    private void listner() {
        binding.tvViewAll.setOnClickListener(view -> startActivity(new Intent(activity, CategoryActivity.class)));
    }
}

package com.eb.onebandhan.dashboard.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eb.onebandhan.R;
import com.eb.onebandhan.auth.model.MCategory;
import com.eb.onebandhan.dashboard.activity.CategoryActivity;
import com.eb.onebandhan.dashboard.adapter.BannerListAdapter;
import com.eb.onebandhan.dashboard.adapter.CategoryListAdapter;
import com.eb.onebandhan.dashboard.adapter.CollectionListAdapter;
import com.eb.onebandhan.dashboard.adapter.SubCategoryListAdapter;
import com.eb.onebandhan.dashboard.adapter.SuperCategoryListHomeAdapter;
import com.eb.onebandhan.dashboard.model.MBanner;
import com.eb.onebandhan.dashboard.model.MCollection;
import com.eb.onebandhan.dashboard.presenter.HomePresenter;
import com.eb.onebandhan.dashboard.viewinterface.HomeViewInterface;
import com.eb.onebandhan.databinding.HomeFragmentLayoutBinding;
import com.eb.onebandhan.productListing.ProductListingActivity;
import com.eb.onebandhan.util.Constant;
import com.eb.onebandhan.util.ShowToast;

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

import static com.eb.onebandhan.auth.util.Categoryutil.ZERO;

public class HomeFragment extends Fragment implements Constant, HomeViewInterface, CollectionListAdapter.CallBack, SubCategoryListAdapter.CallBack,
        BannerListAdapter.CallBack, SuperCategoryListHomeAdapter.CallBack {
    private Activity activity;
    private HomeFragmentLayoutBinding binding;
    private List<MCategory> superCategoryList = new ArrayList<>();
    private List<MCategory> categoryList = new ArrayList<>();
    private List<MBanner> bannerList = new ArrayList<>();
    private List<MCollection> collectionList = new ArrayList<>();
    private SuperCategoryListHomeAdapter superCategoryListAdapter;
    private BannerListAdapter bannerListAdapter;
    private CategoryListAdapter categoryListAdapter;
    private CollectionListAdapter collectionListAdapter;
    private HomePresenter homePresenter;
    private Map<String, String> map = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        activity = getActivity();
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment_layout, container, false);
        View view = binding.getRoot();
        initialization();
        listner();
        return view;
    }


    private void initialization() {
        homePresenter = new HomePresenter(this, activity);
        map.put("level", ZERO);
        map.put("eager", "children.children");
        homePresenter.getBannerListTask();
        homePresenter.getCategoryListTask(map);

        Map<String, String> mapCollection = new HashMap<>();
        mapCollection.put("eager", "products");
        homePresenter.getCollectionListTask(mapCollection);
        binding.rvSuperCategories.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        binding.rvSuperCategories.setHasFixedSize(true);
        binding.rvSuperCategories.setItemAnimator(new DefaultItemAnimator());
        superCategoryListAdapter = new SuperCategoryListHomeAdapter(activity, superCategoryList, this);
        binding.rvSuperCategories.setAdapter(superCategoryListAdapter);

        // for banner
        binding.rvBanners.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        binding.rvBanners.setHasFixedSize(true);
        binding.rvBanners.setItemAnimator(new DefaultItemAnimator());
        bannerListAdapter = new BannerListAdapter(activity, bannerList, this);
        binding.rvBanners.setAdapter(bannerListAdapter);

        // for category
        binding.rvCategory.setLayoutManager(new LinearLayoutManager(activity));
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
        binding.tvViewAll.setOnClickListener(view -> startActivity(new Intent(activity, CategoryActivity.class).putParcelableArrayListExtra(ALL_CATEGORY_LIST, (ArrayList<? extends Parcelable>) superCategoryList)));
    }

    @Override
    public void onSucessfullyGetBannerList(List<MBanner> bannerList, String message) {
        this.bannerList.addAll(bannerList);
        bannerListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFailToGetBannerList(String errorMessage) {
        ShowToast.toastMsg(activity, errorMessage);

    }

    @Override
    public void onSucessfullyGetCategoryList(List<MCategory> categoryList, String message) {
        if (categoryList != null) {
            superCategoryList.addAll(categoryList);
            for (MCategory mCategory : superCategoryList) {
                if (mCategory.getChildren() != null)
                    this.categoryList.addAll(mCategory.getChildren());
            }
            superCategoryListAdapter.notifyDataSetChanged();
            if (this.categoryList != null) categoryListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onFailToGetCategoryList(String errorMessage) {
        ShowToast.toastMsg(activity, errorMessage);
    }

    @Override
    public void onFailToGetCollectionList(String message) {
        ShowToast.toastMsg(activity, message);
    }

    @Override
    public void onSucessfullyGetCollectionList(List<MCollection> collectionList, String message) {
        this.collectionList.addAll(collectionList);
        collectionListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCategoryClick(int position, String categoryId) {
        startActivity(ProductListingActivity.getIntent(activity, Integer.parseInt(categoryId)));
    }
}

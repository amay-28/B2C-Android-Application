package com.retailer.oneops.dashboard.viewinterface;

import com.retailer.oneops.auth.model.MCategory;
import com.retailer.oneops.dashboard.model.MBanner;
import com.retailer.oneops.dashboard.model.MCollection;

import java.util.List;

public interface HomeViewInterface {
    void onSucessfullyGetBannerList(List<MBanner> bannerList, String message);
    void onFailToGetBannerList(String errorMessage);
    void onSucessfullyGetCategoryList(List<MCategory> categoryList, String message);
    void onFailToGetCategoryList(String errorMessage);
    void onFailToGetCollectionList(String message);
    void onSucessfullyGetCollectionList(List<MCollection> data, String message);
}

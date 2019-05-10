package com.eb.onebandhan.dashboard.viewinterface;

import com.eb.onebandhan.auth.model.MCategory;
import com.eb.onebandhan.dashboard.model.MBanner;
import com.eb.onebandhan.dashboard.model.MCollection;

import java.util.List;

public interface HomeViewInterface {
    void onSucessfullyGetBannerList(List<MBanner> bannerList, String message);
    void onFailToGetBannerList(String errorMessage);
    void onSucessfullyGetCategoryList(List<MCategory> categoryList, String message);
    void onFailToGetCategoryList(String errorMessage);
    void onFailToGetCollectionList(String message);
    void onSucessfullyGetCollectionList(List<MCollection> data, String message);
}

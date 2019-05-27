package com.retailer.oneops.auth.presenterinterface;

import com.retailer.oneops.auth.model.MProfile;

import java.util.Map;

public interface SignUpDetailPresenterInterface {
    void loadCategoryTask(Map<String, String> map);
    void submitShopDetailTask(MProfile mProfile);
}

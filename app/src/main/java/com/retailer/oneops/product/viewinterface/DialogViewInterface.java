package com.retailer.oneops.product.viewinterface;

import com.retailer.oneops.auth.model.MCategory;

import java.util.List;

public interface DialogViewInterface {
    void onSuccessfullyGetCategoryList(List<MCategory> mCategoryList, String message);
    void onFailToGetCategoryList(String errorMessage);
}

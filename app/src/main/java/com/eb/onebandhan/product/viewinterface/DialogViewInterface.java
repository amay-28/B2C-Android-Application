package com.eb.onebandhan.product.viewinterface;

import com.eb.onebandhan.auth.model.MCategory;
import com.eb.onebandhan.auth.model.MUser;

import java.util.List;

public interface DialogViewInterface {
    void onSuccessfullyGetCategoryList(List<MCategory> mCategoryList, String message);
    void onFailToGetCategoryList(String errorMessage);
}

package com.retailer.oneops.auth.viewinterface;

import com.retailer.oneops.auth.model.MCategory;
import com.retailer.oneops.auth.model.MUser;

import java.util.List;

public interface SignUpDetailViewInterface {
    void onFailTogetCategories(String message);

    void onSucessfullygetCategories(List<MCategory> data, String message);

    void onSucessfullySubmitShopDetail(MUser data, String message);

    void onFailToSubmitShopDetail(String message);
    // have to change it
}

package com.eb.onebandhan.auth.viewinterface;

import com.eb.onebandhan.auth.model.MCategory;
import com.eb.onebandhan.auth.model.MUser;

import java.util.List;

public interface SignUpDetailViewInterface {
    void onFailTogetCategories(String message);

    void onSucessfullygetCategories(List<MCategory> data, String message);

    void onSucessfullySubmitShopDetail(MUser data, String message);

    void onFailToSubmitShopDetail(String message);
    // have to change it
}

package com.eb.onebandhan.auth.presenterinterface;

import com.eb.onebandhan.auth.model.MProfile;
import com.eb.onebandhan.auth.model.MSignUp;

import java.util.Map;

public interface SignUpDetailPresenterInterface {
    // have to change it
    void loadCategoryTask(Map<String, String> map);
    void submitShopDetailTask(MProfile mProfile);
}

package com.retailer.oneops.auth.viewinterface;

import com.retailer.oneops.auth.model.MUser;

public interface LoginViewInterface {
    void onSucessfullyLogin(MUser mUser, String message);
    void onFailToLogin(String errorMessage);
}

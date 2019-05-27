package com.retailer.oneops.auth.viewinterface;

import com.retailer.oneops.auth.model.MUser;

public interface SignUpViewInterface {
    void onSucessfullySignUp(MUser mUser, String message);
    void onFailToSignUp(String errorMessage);
}

package com.retailer.oneops.auth.presenterinterface;

import com.retailer.oneops.auth.model.MSignUp;

public interface LoginPresenterInterface {
    void performLoginTask(MSignUp mSignUp,String requestType);
}

package com.eb.onebandhan.auth.presenterinterface;

import com.eb.onebandhan.auth.model.MSignUp;

public interface LoginPresenterInterface {
    void performLoginTask(MSignUp mSignUp,String requestType);
}

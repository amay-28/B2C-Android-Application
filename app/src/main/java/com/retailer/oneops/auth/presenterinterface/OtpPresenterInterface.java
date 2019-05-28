package com.retailer.oneops.auth.presenterinterface;

import com.retailer.oneops.auth.model.MSignUp;

public interface OtpPresenterInterface {
    void performOtpVerificationTask(MSignUp mSignUp);
}

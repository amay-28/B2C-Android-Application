package com.retailer.oneops.auth.viewinterface;

public interface OtpViewInterface {
    void onSucessfullyVerified(String message);
    void onFailToVerified(String errorMessage);
}

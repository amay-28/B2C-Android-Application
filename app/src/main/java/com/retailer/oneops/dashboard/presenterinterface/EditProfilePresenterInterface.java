package com.retailer.oneops.dashboard.presenterinterface;

import com.retailer.oneops.auth.model.MProfile;

import okhttp3.MultipartBody;

public interface EditProfilePresenterInterface {
    void onUpdateProfile(MProfile mProfile);
    void onUpdateImage(MultipartBody.Part file);
}

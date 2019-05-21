package com.eb.onebandhan.dashboard.presenterinterface;

import android.net.Uri;

import com.eb.onebandhan.auth.model.MProfile;
import com.eb.onebandhan.auth.model.MUser;

import java.util.Map;

import okhttp3.MultipartBody;

public interface EditProfilePresenterInterface {
    void onUpdateProfile(MProfile mProfile);
    void onUpdateImage(MultipartBody.Part file);
}

package com.eb.onebandhan.dashboard.presenterinterface;

import android.net.Uri;

import com.eb.onebandhan.auth.model.MUser;

import java.util.Map;

import okhttp3.MultipartBody;

public interface EditProfilePresenterInterface {
    void onUpdateProfile(MUser mUser);
    void onUpdateImage(MultipartBody.Part file);
}

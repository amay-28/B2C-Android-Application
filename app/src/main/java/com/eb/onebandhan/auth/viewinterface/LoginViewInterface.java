package com.eb.onebandhan.auth.viewinterface;

import com.eb.onebandhan.auth.model.MUser;

public interface LoginViewInterface {
    void onSucessfullyLogin(MUser mUser, String message);
    void onFailToLogin(String errorMessage);
}

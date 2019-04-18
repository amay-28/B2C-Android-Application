package com.eb.onebandhan.auth.viewinterface;

import com.eb.onebandhan.auth.model.MUser;

public interface SignUpViewInterface {
    void onSucessfullySignUp(MUser mUser, String message);
    void onFailToSignUp(String errorMessage);
}

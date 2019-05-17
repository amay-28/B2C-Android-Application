package com.eb.onebandhan.dashboard.viewinterface;
import com.eb.onebandhan.auth.model.MUser;

public interface EditProfileViewInterface {
    void onSucessfullyUpdated(MUser mUser, String message);
    void onSucessfullyUpdatedImage(String value);
    void onFailToUpdate(String errorMessage);
}

package com.retailer.oneops.dashboard.viewinterface;
import com.retailer.oneops.auth.model.MUser;

public interface EditProfileViewInterface {
    void onSucessfullyUpdated(MUser mUser, String message);
    void onSucessfullyUpdatedImage(String value);
    void onFailToUpdate(String errorMessage);
}

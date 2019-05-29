package com.retailer.oneops.myinventory.viewinterface;

import com.retailer.oneops.auth.model.MUser;
import com.retailer.oneops.myinventory.model.MInventory;

public interface AddToInventViewInterface {
    void onSuccessfullyAdd(MInventory mInventory, String message);
    void onFailToAdd(String errorMessage);
}

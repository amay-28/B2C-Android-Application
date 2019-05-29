package com.retailer.oneops.myinventory.presenterinterface;

import com.google.gson.JsonObject;
import com.retailer.oneops.auth.model.MSignUp;
import com.retailer.oneops.myinventory.model.MInventory;

public interface AddToInventPresenterInterface {
    void performAddToInventoryTask(JsonObject inventoryObject);
}

package com.retailer.oneops.dashboard.presenterinterface;

import com.retailer.oneops.myinventory.model.MInventory;

import java.util.Map;

public interface MyInventPresenterInterface {
    void onProductInventoryList(Map<String, String> map,int requestType);
}

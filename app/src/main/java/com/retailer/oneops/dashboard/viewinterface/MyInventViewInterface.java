package com.retailer.oneops.dashboard.viewinterface;

import com.retailer.oneops.myinventory.model.MInventory;
import com.retailer.oneops.productListing.model.MProduct;

import java.util.List;

public interface MyInventViewInterface {

  void onSuccessfulProductListing(List<MProduct> mProductListing, String message);
  void onSuccessfulVirtualListing(List<MInventory> mInventoryList, String message);
  void onSuccessfulDeleteItem();
  void onFailedProductListing(String errorMessage);
}


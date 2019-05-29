package com.retailer.oneops.dashboard.viewinterface;

import com.retailer.oneops.productListing.model.MProduct;

import java.util.List;

public interface MyInventViewInterface {

  void onSuccessfulProductListing(List<MProduct> mProductListing, String message);
  void onFailedProductListing(String errorMessage);
}


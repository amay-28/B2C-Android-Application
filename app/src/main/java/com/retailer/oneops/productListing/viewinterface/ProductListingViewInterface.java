package com.retailer.oneops.productListing.viewinterface;

import com.retailer.oneops.productListing.model.MProduct;

import java.util.List;

public interface ProductListingViewInterface {

  void onSuccessfulProductListing(List<MProduct> mProductListing, String message);
  void onFailedProductListing(String errorMessage);
}


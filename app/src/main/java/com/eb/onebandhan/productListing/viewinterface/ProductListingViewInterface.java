package com.eb.onebandhan.productListing.viewinterface;

import com.eb.onebandhan.dashboard.model.MCollection;
import com.eb.onebandhan.productListing.model.MProduct;

import java.util.List;

public interface ProductListingViewInterface {

  void onSuccessfulProductListing(List<MProduct> mProductListing, String message);
  void onFailedProductListing(String errorMessage);
}


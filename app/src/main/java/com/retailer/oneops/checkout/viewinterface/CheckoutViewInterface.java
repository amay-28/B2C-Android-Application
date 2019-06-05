package com.retailer.oneops.checkout.viewinterface;

import com.retailer.oneops.checkout.model.MCartDetail;
import com.retailer.oneops.myinventory.model.MInventory;
import com.retailer.oneops.productListing.model.MProduct;

import java.util.List;

public interface CheckoutViewInterface {

  void onSuccessfulCartDetails(MCartDetail mCartDetail, String message);
  void onSuccessfulDeleteItem();
  void onFailedListing(String errorMessage);
}

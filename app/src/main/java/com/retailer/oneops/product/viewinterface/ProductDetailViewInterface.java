package com.retailer.oneops.product.viewinterface;

import com.retailer.oneops.checkout.model.MCart;
import com.retailer.oneops.product.model.MAddProduct;
import com.retailer.oneops.product.model.MImageServer;
import com.retailer.oneops.productListing.model.MProduct;

import java.util.List;

public interface ProductDetailViewInterface {
    void onSuccessfullyGetDetail(MProduct mProduct, String message);
    void onSuccessfullyAddToCart(MCart mCart, String message);
    void onFailToUpdate(String errorMessage);
}

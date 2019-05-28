package com.retailer.oneops.product.viewinterface;

import com.retailer.oneops.product.model.MAddProduct;
import com.retailer.oneops.product.model.MImageServer;

import java.util.List;

public interface AddProductViewInterface {
    void onSuccessfullyAddProduct(MAddProduct mAddProduct, String message);
    void onSucessfullyUpdatedImage(List<MImageServer> imageList);
    void onFailToUpdate(String errorMessage);
}

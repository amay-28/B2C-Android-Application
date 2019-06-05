package com.retailer.oneops.product.presenterinterface;

import com.google.gson.JsonObject;
import com.retailer.oneops.product.model.MAddProduct;
import com.retailer.oneops.productListing.model.MProduct;

import java.util.List;

import okhttp3.MultipartBody;

public interface ProductDetailPresenterInterface {
    void getProductDetailTask(int id);
    void addToCartTask(JsonObject jsonObject);
    void clearCartTask();
}

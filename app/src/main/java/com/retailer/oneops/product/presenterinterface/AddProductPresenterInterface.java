package com.retailer.oneops.product.presenterinterface;

import com.retailer.oneops.product.model.MAddProduct;

import java.util.List;

import okhttp3.MultipartBody;

public interface AddProductPresenterInterface {
    void addProductTask(MAddProduct mAddProduct);
    void onUpdateImage(List<MultipartBody.Part> files);
}

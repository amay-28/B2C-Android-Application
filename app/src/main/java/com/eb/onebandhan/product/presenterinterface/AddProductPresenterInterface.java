package com.eb.onebandhan.product.presenterinterface;

import com.eb.onebandhan.product.model.MAddProduct;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;

public interface AddProductPresenterInterface {
    void addProductTask(MAddProduct mAddProduct);
    void onUpdateImage(List<MultipartBody.Part> files);
}

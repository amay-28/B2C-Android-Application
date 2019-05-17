package com.eb.onebandhan.product.viewinterface;

import com.eb.onebandhan.auth.model.MCategory;
import com.eb.onebandhan.product.model.MAddProduct;

import java.util.List;

public interface AddProductViewInterface {
    void onSuccessfullyAddProduct(MAddProduct mAddProduct, String message);
    void onFailToAddProduct(String errorMessage);
}

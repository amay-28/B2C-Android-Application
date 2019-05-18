package com.eb.onebandhan.product.viewinterface;

import com.eb.onebandhan.auth.model.MCategory;
import com.eb.onebandhan.product.model.MAddProduct;
import com.eb.onebandhan.product.model.MImage;
import com.eb.onebandhan.product.model.MImageServer;

import org.json.JSONArray;

import java.util.List;

public interface AddProductViewInterface {
    void onSuccessfullyAddProduct(MAddProduct mAddProduct, String message);
    void onSucessfullyUpdatedImage(List<MImageServer> imageList);
    void onFailToUpdate(String errorMessage);
}

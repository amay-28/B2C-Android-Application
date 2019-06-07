package com.retailer.oneops.checkout.presenterinterface;

import com.retailer.oneops.checkout.model.MOrderRequest;

import org.json.JSONObject;

import java.util.Map;

public interface CheckoutPresenterInterface {
    void getCartDetails();
    void onDeleteProductItem(int id);
    void onPlaceOrderClick(MOrderRequest mOrderRequest);
    void onPlaceOrderClickNew(com.retailer.oneops.checkout.model.physical.MOrderRequest mOrderRequest);
}

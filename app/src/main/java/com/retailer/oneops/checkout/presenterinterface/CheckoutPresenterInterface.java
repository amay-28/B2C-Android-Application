package com.retailer.oneops.checkout.presenterinterface;

import java.util.Map;

public interface CheckoutPresenterInterface {
    void onCartList(Map<String, String> map, int requestType);
    void onDeleteProductItem(int inventoryId, int requestType);
}

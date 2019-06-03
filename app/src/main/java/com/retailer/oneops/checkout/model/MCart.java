package com.retailer.oneops.checkout.model;

import lombok.Data;

@Data
public class MCart {
    private int productId;
    private int quantity;
    private int cartId;
    private int id;
    private String created_at;
    private String updated_at;
}

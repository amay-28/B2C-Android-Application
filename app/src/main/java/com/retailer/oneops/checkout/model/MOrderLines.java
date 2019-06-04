package com.retailer.oneops.checkout.model;

import lombok.Data;

@Data
public class MOrderLines {
    private int id;
    private int orderId;
    private int productId;
    private int productVariantId;
    private int quantity;
    private int amount;
    private int total_discount;
    private int couponId;
    private int sellerId;
    private String created_at;
    private String updated_at;
}

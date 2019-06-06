package com.retailer.oneops.order.model;

import com.retailer.oneops.productListing.model.MProduct;

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
   private String couponId;
   private String sellerId;
   private String created_at;
   private String updated_at;
   private MProduct product;
}
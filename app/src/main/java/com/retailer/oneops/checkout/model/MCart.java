package com.retailer.oneops.checkout.model;

import com.retailer.oneops.productListing.model.MProduct;
import com.retailer.oneops.productListing.model.MProductVariant;

import java.util.List;

import lombok.Data;

@Data
public class MCart {
    private int productId;
    private int productVariantId;
    private int quantity;
    private int cartId;
    private int id;
    private String created_at;
    private String updated_at;
    private MProduct product;
    private MProductVariant product_variant;
}

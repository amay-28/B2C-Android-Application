package com.retailer.oneops.productListing.model;

import com.retailer.oneops.product.model.MImage;

import java.util.List;

import lombok.Data;

@Data
public class MProductVariant {
    private int id;
    private int productId;
    private int price_override;
    private int cost_price;
    private int quantity;
    private int quantity_allocated;
    private String name;
    private List<MAttributes> attributes;
    private String specifications;
    private String created_at;
    private String updated_at;
    private List<MImage> images;
}


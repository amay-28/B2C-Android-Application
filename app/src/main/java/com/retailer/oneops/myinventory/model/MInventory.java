package com.retailer.oneops.myinventory.model;

import com.retailer.oneops.productListing.model.MProduct;

import lombok.Data;

@Data
public class MInventory {
    private int product_id;
    private int margin;
    private int user_id;
    private int id;
    private MProduct product;

}

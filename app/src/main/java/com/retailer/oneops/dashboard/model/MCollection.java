package com.retailer.oneops.dashboard.model;

import com.retailer.oneops.productListing.model.MProduct;

import java.util.List;

import lombok.Data;

@Data
public class MCollection {
    private String id;
    private String name;
    private String imageUrl;
    private String created_at;
    private String updated_at;
    private List<MProduct> products;
}

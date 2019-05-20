package com.eb.onebandhan.dashboard.model;

import com.eb.onebandhan.productListing.model.MProduct;

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

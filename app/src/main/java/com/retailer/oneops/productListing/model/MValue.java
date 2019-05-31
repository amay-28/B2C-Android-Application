package com.retailer.oneops.productListing.model;

import lombok.Data;

@Data
public class MValue {
    private int id;
    private String name;
    private String slug;
    private String value;
    private String created_at;
    private String updated_at;
    private int sort_order;
    private int attributeId;

}

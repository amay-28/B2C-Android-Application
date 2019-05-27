package com.retailer.oneops.dashboard.model;

import com.retailer.oneops.auth.model.MCategory;

import lombok.Data;

@Data
public class MBanner {
    private String id;
    private String categoryId;
    private String name;
    private String entityType;
    private String entityId;
    private String imageUrl;
    private String created_at;
    private String updated_at;
    private MCategory category;
}

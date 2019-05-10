package com.eb.onebandhan.dashboard.model;

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

    @Data
    public static class MProduct {
        private String id;
        private String name;
        private String categoryId;
        private String price;
        private String cost_price;
        private String type;
        private Boolean is_published;
        private String product_attributes;
        private String variant_attributes;
        private String created_at;
        private String updated_at;
        private String description;
        private String specifications;
        private String user_id;
    }
}

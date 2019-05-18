package com.eb.onebandhan.product.model;

import com.eb.onebandhan.auth.model.MCategory;

import java.util.List;

import lombok.Data;

@Data
public class MAddProduct {
    private String id;
    private String user_id;
    private String name;
    private String categoryId;
    private String price;
    private String cost_price;
    private String type;
    private String is_published;
    private String created_at;
    private String updated_at;
    private String description;
    private String specifications;
    private MCategory category;
    private List<MImageServer> images;

}

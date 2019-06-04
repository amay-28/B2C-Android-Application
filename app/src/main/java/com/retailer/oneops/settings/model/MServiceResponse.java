package com.retailer.oneops.settings.model;

import com.retailer.oneops.auth.model.MCategory;

import java.util.ArrayList;

import lombok.Data;

@Data
public class MServiceResponse  {
    private String service_name,service_price,service_description,category_id,retailer_id,created_at,updated_at;
    private ArrayList<ServiceImage> images;
    private MCategory category;
}

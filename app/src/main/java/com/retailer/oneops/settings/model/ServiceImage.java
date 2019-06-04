package com.retailer.oneops.settings.model;

import lombok.Data;

@Data
public class ServiceImage {
    private String id;
    private String service_id;
    private String url, created_at, updated_at;
}

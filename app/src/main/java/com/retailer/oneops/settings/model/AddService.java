package com.retailer.oneops.settings.model;

import java.util.List;

import lombok.Data;

@Data
public class AddService {
   private String service_name,service_price,service_description,category_id;
   private List<MUrl> images;
}

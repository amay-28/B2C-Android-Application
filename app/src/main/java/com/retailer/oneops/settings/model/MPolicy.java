package com.retailer.oneops.settings.model;

import lombok.Data;

@Data
public class MPolicy {
    private int id;
    private int userId;
    private String policy;
    private boolean isTwoDay;
}
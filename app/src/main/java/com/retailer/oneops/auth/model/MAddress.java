package com.retailer.oneops.auth.model;

import lombok.Data;

@Data
public class MAddress {
    private String name;
    private String mobileNumber;
    private int pincode;
    private String addressLine1;
    private String addressLine2;
    private String locality;
    private String city;
    private String state;
}

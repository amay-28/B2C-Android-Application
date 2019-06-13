package com.retailer.oneops.auth.model;

import lombok.Data;

@Data
public class MAddress {
    private int id;
    private String name;
    private String mobileNumber;
    private String postalCode;
    private String addressLine1;
    private String addressLine2;
    private String locality;
    private String city;
    private String state;
}

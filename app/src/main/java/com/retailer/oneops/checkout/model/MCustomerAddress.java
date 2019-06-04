package com.retailer.oneops.checkout.model;

import lombok.Data;

@Data
public class MCustomerAddress {
    private String name;
    private String mobileNumber;
    private int pincode;
    private String addressLine1;
    private String addressLine2;
    private String locality;
    private String city;
    private String state;
}

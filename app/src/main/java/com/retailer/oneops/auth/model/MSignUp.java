package com.retailer.oneops.auth.model;

import lombok.Data;

@Data
public class MSignUp {
    private String name;
    private String mobileNumber;
    private String otp;
    private String deviceType;
    private String deviceToken;
}

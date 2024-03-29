package com.retailer.oneops.auth.model;

import java.util.List;

import lombok.Data;

@Data
public class MProfile {
    private String shopName;
    private List<MDealsIn> dealsIn;
    private List<MAddress> address;
    private String gstin;
    private String panNumber;
    private String email;
    private String addressLine1;
    private String addressLine2;
    private String landmark;
    private String city;
    private String state;
    private String postalCode;
    private String created_at;
    private String updated_at;
    private String aboutShop;
    private String gstPercent;
    private Boolean isComposite;

    private String name;
    private String imageUrl;
    private String mobileNumber;

    @Data
    public static class MDealsIn {
        private String id;
    }
}

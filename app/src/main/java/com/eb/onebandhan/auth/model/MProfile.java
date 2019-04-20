package com.eb.onebandhan.auth.model;

import lombok.Data;

@Data
public class MProfile {
    private String shopName;
    private String dealsInCategory;
    private String gstin;
    private String panNumber;
    private String email;
    private String addressLine1;
    private String addressLine2;
    private String landmark;
    private String city;
    private String state;
    private String postalCode;
}

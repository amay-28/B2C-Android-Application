package com.eb.onebandhan.auth.model;

import java.util.List;

import lombok.Data;

@Data
public class MProfile {
    private String shopName;
    private List<MDealsIn> dealsIn;
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
    private Boolean isComposite;

    @Data
    public static class MDealsIn {
        private String id;
    }
}

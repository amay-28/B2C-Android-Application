package com.retailer.oneops.auth.model;

import com.retailer.oneops.bankDetail.activity.model.MBankDetail;

import lombok.Data;

@Data
public class MUser {
    private String name;
    private String imageUrl;
    private String mobileNumber;
    private String userTypeId;
    private String id;
    private String email;
    private String ip;
    private Boolean isEmailVerified;
    private Boolean isMobileVerified;
    private String password;
    private Boolean isAdminVerified;
    private Boolean isActive;
    private String created_at;
    private String updated_at;
    private MUserType userType;
    //extra params for detailde info
    private MProfile retailerDetails;
    private MBankDetail bankDetails;

    @Data
    public static class MUserType {
        private String userType;
    }
}
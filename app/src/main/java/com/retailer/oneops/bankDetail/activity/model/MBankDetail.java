package com.retailer.oneops.bankDetail.activity.model;

import lombok.Data;

@Data
public class MBankDetail {
    private String accountHolderName;
    private String accountNumber;
    private String ifscCode;
    private String accountType;
}
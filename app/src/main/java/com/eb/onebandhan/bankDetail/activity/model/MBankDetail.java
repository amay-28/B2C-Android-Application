package com.eb.onebandhan.bankDetail.activity.model;

import lombok.Data;

@Data
public class MBankDetail {
    private String accountHolderName;
    private String accountNumber;
    private String ifscCode;
    private String accountType;
}
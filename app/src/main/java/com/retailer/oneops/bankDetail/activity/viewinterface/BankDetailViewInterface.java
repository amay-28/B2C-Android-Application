package com.retailer.oneops.bankDetail.activity.viewinterface;

import com.retailer.oneops.bankDetail.activity.model.MBankDetail;

public interface BankDetailViewInterface {

    void onSuccessfulUploadBankDetails(MBankDetail mBankDetail, String message);
    void onFailedUploadBankDetails(String errorMessage);

}

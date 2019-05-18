package com.eb.onebandhan.bankDetail.activity.viewinterface;

import com.eb.onebandhan.bankDetail.activity.model.MBankDetail;

public interface BankDetailViewInterface {

    void onSuccessfulUploadBankDetails(MBankDetail mBankDetail, String message);
    void onFailedUploadBankDetails(String errorMessage);

}

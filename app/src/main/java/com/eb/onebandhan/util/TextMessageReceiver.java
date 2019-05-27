package com.eb.onebandhan.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.eb.onebandhan.auth.activity.OtpVerificationActivity;
import com.eb.onebandhan.auth.presenter.LoginPresenter;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

public class TextMessageReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
            Bundle extras = intent.getExtras();
            Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

            switch (status.getStatusCode()) {
                case CommonStatusCodes.SUCCESS:
                    // Get SMS message contents
                    String message = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);
                    // Extract one-time code from the message and complete verification
                    // by sending the code back to your server.
                    /*<#> Hi! Your Oneops verification code is 2087
lQML2McDZyq*/
                    String sms = message.replace("<#> Your One Bandhan retailer account verification code is", "");
                    sms.replace("izl3Qjjpm0t", "");
                    //  OtpVerificationActivity.updateOtp(sms.trim());
                    Session session=new Session(context);

                    if (session.getSharedPreferencesBoolean(context, Constant.OTP_RECEIVE) && session.getIngerSharedPreferences(context, Constant.OTP_RECEIVE_CLASS) == 1) {
                        OtpVerificationActivity.updateOtp(sms.trim());
                        session.setSharedPreferenceBoolean(context, Constant.OTP_RECEIVE, false);
                    }
                    break;
                case CommonStatusCodes.TIMEOUT:
                    // Waiting for SMS timed out (5 minutes)
                    // Handle the error ...
                    break;
            }
        }
    }

}
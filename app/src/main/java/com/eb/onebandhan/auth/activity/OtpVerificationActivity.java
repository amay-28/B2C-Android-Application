package com.eb.onebandhan.auth.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.eb.onebandhan.R;
import com.eb.onebandhan.auth.model.MSignUp;
import com.eb.onebandhan.auth.model.MUser;
import com.eb.onebandhan.auth.presenter.LoginPresenter;
import com.eb.onebandhan.auth.presenter.OtpPresenter;
import com.eb.onebandhan.auth.viewinterface.LoginViewInterface;
import com.eb.onebandhan.auth.viewinterface.OtpViewInterface;
import com.eb.onebandhan.dashboard.activity.DashboardActivity;
import com.eb.onebandhan.databinding.ActivityOtpVerificationBinding;
import com.eb.onebandhan.util.Constant;

public class OtpVerificationActivity extends AppCompatActivity implements OtpViewInterface, LoginViewInterface, Constant {
    private Activity activity;
    private OtpPresenter otpPresenter;
    private LoginPresenter loginPresenter;
    private ActivityOtpVerificationBinding binding;
    private MSignUp mSignUp = new MSignUp();
    private String isFromSignUp = "";
    private Boolean isResendOtp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_otp_verification);
        initialization();
        listner();
    }

    private void listner() {
        binding.btnVerifyUser.setOnClickListener(view -> performOtpVerification());
        //binding.tvResendOtp.setOnClickListener(view -> resendOpt());

    }

    private void resendOpt() {
        binding.otpView.setText("");
        isResendOtp = true;
        loginPresenter.performLoginTask(mSignUp, TYPE_LOGIN_REQUEST_OTP);
    }

    private void performOtpVerification() {
        if (TextUtils.isEmpty(binding.otpView.getText().toString()))
            Toast.makeText(activity, getResources().getString(R.string.please_enter_otp), Toast.LENGTH_SHORT).show();
        else if (binding.otpView.getText().toString().length() != 4)
            Toast.makeText(activity, getResources().getString(R.string.please_enter_valid_otp), Toast.LENGTH_SHORT).show();
        else {
            mSignUp.setOtp(binding.otpView.getText().toString());
            if (isFromSignUp.equals(YES))
                otpPresenter.performOtpVerificationTask(mSignUp);
            else {
                isResendOtp = false;
                loginPresenter.performLoginTask(mSignUp, TYPE_LOGIN_ONLY);
            }
        }

    }

    private void initialization() {
        mSignUp.setMobileNumber(getIntent().getStringExtra(MOBILE_NO));
        isFromSignUp = getIntent().getStringExtra(IS_FROM_SIGNUP);
        otpPresenter = new OtpPresenter(this, activity);
        loginPresenter = new LoginPresenter(this, activity);
    }

    @Override
    public void onSucessfullyVerified(String message) {
        // do waht u want after signup
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(activity, SignUpDetailActivity.class));
    }

    @Override
    public void onFailToVerified(String errorMessage) {
        Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSucessfullyLogin(MUser mUser, String message) {
        startActivity(new Intent(activity, DashboardActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
//        if (isResendOtp)
    }

    @Override
    public void onFailToLogin(String errorMessage) {
        Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show();
    }
}
package com.eb.onebandhan.auth.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.eb.onebandhan.R;
import com.eb.onebandhan.auth.model.MSignUp;
import com.eb.onebandhan.auth.model.MUser;
import com.eb.onebandhan.auth.presenter.LoginPresenter;
import com.eb.onebandhan.auth.presenter.OtpPresenter;
import com.eb.onebandhan.auth.presenter.SignUpPresenter;
import com.eb.onebandhan.auth.viewinterface.LoginViewInterface;
import com.eb.onebandhan.auth.viewinterface.OtpViewInterface;
import com.eb.onebandhan.databinding.ActivityOtpVerificationBinding;
import com.eb.onebandhan.util.Constant;

public class OtpVerificationActivity extends AppCompatActivity implements OtpViewInterface, LoginViewInterface, Constant {
    private Activity activity;
    private OtpPresenter otpPresenter;
    private LoginPresenter loginPresenter;
    private ActivityOtpVerificationBinding binding;
    private MSignUp mSignUp = new MSignUp();
    private String isFromSignUp="";

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
        binding.tvResendOtp.setOnClickListener(view -> performOtpVerification());
    }

    private void performOtpVerification() {
        mSignUp.setMobileNumber("8109059062");
        mSignUp.setOtp("1234");
        if (isFromSignUp.equals(YES))
        otpPresenter.performOtpVerificationTask(mSignUp);
        else loginPresenter.performLoginTask(mSignUp,TYPE_LOGIN_ONLY);
    }

    private void initialization() {
        isFromSignUp=getIntent().getStringExtra(IS_FROM_SIGNUP);
        otpPresenter = new OtpPresenter(this, activity);
        loginPresenter = new LoginPresenter(this, activity);
    }

    @Override
    public void onSucessfullyVerified(String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailToVerified(String errorMessage) {
        Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSucessfullyLogin(MUser mUser, String message) {

    }

    @Override
    public void onFailToLogin(String errorMessage) {
        Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show();
    }
}
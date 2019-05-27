package com.eb.onebandhan.auth.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.eb.onebandhan.util.MyDialogProgress;
import com.eb.onebandhan.util.Session;
import com.eb.onebandhan.util.Utils;

import java.util.concurrent.TimeUnit;

public class OtpVerificationActivity extends AppCompatActivity implements OtpViewInterface, LoginViewInterface, Constant {
    private Activity activity;
    private OtpPresenter otpPresenter;
    private LoginPresenter loginPresenter;
    private static ActivityOtpVerificationBinding binding;
    private MSignUp mSignUp = new MSignUp();
    private String isFromSignUp = "";
    private Boolean isResendOtp = null;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_otp_verification);
        initialization();
        listener();
    }

    private void listener() {
        binding.btnVerifyUser.setOnClickListener(view -> performOtpVerification());
        binding.tvResendOtp.setOnClickListener(view -> resendOpt());

        counterClock();
    }

    private void resendOpt() {
        binding.otpView.setText("");
        isResendOtp = true;
        loginPresenter.performLoginTask(mSignUp, TYPE_RESEND_OTP);
    }

    private void performOtpVerification() {
        if (TextUtils.isEmpty(binding.otpView.getText().toString()))
            Toast.makeText(activity, getResources().getString(R.string.please_enter_otp), Toast.LENGTH_SHORT).show();
        else if (binding.otpView.getText().toString().length() != 4)
            Toast.makeText(activity, getResources().getString(R.string.please_enter_valid_otp), Toast.LENGTH_SHORT).show();
        else {
            mSignUp.setOtp(binding.otpView.getText().toString());
            if (isFromSignUp.equals(YES)) {
                MyDialogProgress.open(activity);
                otpPresenter.performOtpVerificationTask(mSignUp);
            } else {
                MyDialogProgress.open(activity);
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
        session = new Session(activity);
        session.setIntegerSharedPreference(activity, Constant.OTP_RECEIVE_CLASS, 1);
        session.setSharedPreferenceBoolean(activity, Constant.OTP_RECEIVE, true);
    }

    public static void updateOtp(String otp) {
        binding.otpView.setText(otp);
    }

    @Override
    public void onSucessfullyVerified(String message) {
        MyDialogProgress.close(activity);
        // do waht u want after signup
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(activity, SignUpDetailActivity.class));
    }

    @Override
    public void onFailToVerified(String errorMessage) {
        MyDialogProgress.close(activity);
        Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSucessfullyLogin(MUser mUser, String message) {
        MyDialogProgress.close(activity);
        if (!isResendOtp) {
            startActivity(new Intent(activity, DashboardActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
        } else {
            session.setIntegerSharedPreference(activity, Constant.OTP_RECEIVE_CLASS, 1);
            session.setSharedPreferenceBoolean(activity, Constant.OTP_RECEIVE, true);
            Utils.startSMSReceiver(activity);
        }
    }

    @Override
    public void onFailToLogin(String errorMessage) {
        MyDialogProgress.close(activity);
        Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show();
    }

    // Show resend otp clock counter for product2 mnt
    public void counterClock() {
        binding.tvResendOtp.setEnabled(false);
        new CountDownTimer(120000, 1000) {
            public void onTick(long millisUntilFinished) {
                binding.tvResendOtp.setText(getString(R.string.resend_OTP_with_time) + Constant.SPACE + String.format("%02d:%02d", 0, TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            @Override
            public void onFinish() {
                binding.tvResendOtp.setEnabled(true);
                binding.tvResendOtp.setText(getString(R.string.resend_otp));
            }
        }.start();
    }
}
package com.retailer.oneops.auth.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.retailer.oneops.R;
import com.retailer.oneops.auth.model.MSignUp;
import com.retailer.oneops.auth.model.MUser;
import com.retailer.oneops.auth.presenter.LoginPresenter;
import com.retailer.oneops.auth.viewinterface.LoginViewInterface;
import com.retailer.oneops.databinding.ActivityLoginBinding;
import com.retailer.oneops.util.Constant;
import com.retailer.oneops.util.Utils;

public class LoginActivity extends AppCompatActivity implements LoginViewInterface, Constant {
    private Activity activity;
    private ActivityLoginBinding binding;
    private LoginPresenter loginPresenter;
    private MSignUp mSignUp = new MSignUp();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_login);
        initialization();
        listner();
    }

    private void listner() {
        binding.tvSignUp.setOnClickListener(view -> {
            Intent intent = new Intent(activity, SignUpInitialActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        });
        binding.btnLogin.setOnClickListener(view -> {
            if (TextUtils.isEmpty(binding.etMobileNo.getText()))
                Toast.makeText(activity, getResources().getString(R.string.please_enter_mobile_no), Toast.LENGTH_SHORT).show();
            else if (binding.etMobileNo.getText().toString().length() < 10)
                Toast.makeText(activity, getResources().getString(R.string.please_enter_valid_mobile_no), Toast.LENGTH_SHORT).show();
            else {
                mSignUp.setMobileNumber(binding.etMobileNo.getText().toString());
                loginPresenter.performLoginTask(mSignUp, TYPE_LOGIN_REQUEST_OTP);
            }
        });
    }

    private void initialization() {
        loginPresenter = new LoginPresenter(this, activity);
    }

    @Override
    public void onSucessfullyLogin(MUser mUser, String message) {
        startActivity(new Intent(activity, OtpVerificationActivity.class).putExtra(IS_FROM_SIGNUP, NO).putExtra(MOBILE_NO, mUser.getMobileNumber()));
        Utils.startSMSReceiver(activity);
    }

    @Override
    public void onFailToLogin(String errorMessage) {
        Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show();
    }
}

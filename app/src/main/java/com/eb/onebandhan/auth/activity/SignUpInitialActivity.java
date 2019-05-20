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
import com.eb.onebandhan.auth.presenter.SignUpPresenter;
import com.eb.onebandhan.auth.viewinterface.SignUpViewInterface;
import com.eb.onebandhan.databinding.ActivitySignUpInitialBinding;
import com.eb.onebandhan.util.Constant;

public class SignUpInitialActivity extends AppCompatActivity implements SignUpViewInterface, Constant {
    private Activity activity;
    private ActivitySignUpInitialBinding binding;
    private SignUpPresenter signUpPresenter;
    private MSignUp mSignUp = new MSignUp();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_sign_up_initial);
        initialization();
        listner();
    }

    private void listner() {
        binding.btnSubmit.setOnClickListener(view -> {
                    if (checkValidate()) {
                        performSignUp();
                    }
                }
        );
    }

    private void performSignUp() {
        mSignUp.setName(binding.etName.getText().toString());
        mSignUp.setMobileNumber(binding.etMobileNo.getText().toString());
        signUpPresenter.performSignUpTask(mSignUp);

    }

    private boolean checkValidate() {
        if (TextUtils.isEmpty(binding.etName.getText())) {
            Toast.makeText(activity, getResources().getString(R.string.please_enter_name), Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(binding.etMobileNo.getText())) {
            Toast.makeText(activity, getResources().getString(R.string.please_enter_mobile_no), Toast.LENGTH_SHORT).show();
            return false;
        } else if (binding.etName.getText().toString().length() < 3) {
            Toast.makeText(activity, getResources().getString(R.string.please_enter_valid_name), Toast.LENGTH_SHORT).show();
            return false;
        } else if (binding.etMobileNo.getText().toString().length() < 10) {
            Toast.makeText(activity, getResources().getString(R.string.please_enter_valid_mobile_no), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private void initialization() {
        signUpPresenter = new SignUpPresenter(this, activity);
    }

    @Override
    public void onSucessfullySignUp(MUser mUser, String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(activity, OtpVerificationActivity.class).putExtra(IS_FROM_SIGNUP, YES).putExtra(MOBILE_NO, mUser.getMobileNumber()));
    }

    @Override
    public void onFailToSignUp(String errorMessage) {
        Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show();

    }
}

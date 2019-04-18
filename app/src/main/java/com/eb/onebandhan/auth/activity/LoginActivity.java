package com.eb.onebandhan.auth.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.eb.onebandhan.R;
import com.eb.onebandhan.auth.model.MSignUp;
import com.eb.onebandhan.auth.model.MUser;
import com.eb.onebandhan.auth.presenter.LoginPresenter;
import com.eb.onebandhan.auth.viewinterface.LoginViewInterface;
import com.eb.onebandhan.databinding.ActivityLoginBinding;
import com.eb.onebandhan.util.Constant;

public class LoginActivity extends AppCompatActivity implements LoginViewInterface , Constant {
  private Activity activity;
  private ActivityLoginBinding binding;
  private LoginPresenter  loginPresenter;
  private MSignUp mSignUp=new MSignUp();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=this;
        binding= DataBindingUtil.setContentView(activity,R.layout.activity_login);
        initialization();
        listner();
    }

    private void listner() {
        binding.btnLogin.setOnClickListener(view -> {
            mSignUp.setMobileNumber(binding.etMobileNo.getText().toString());
            loginPresenter.performLoginTask(mSignUp, TYPE_LOGIN_REQUEST_OTP);
        });
    }

    private void initialization() {
        loginPresenter=new LoginPresenter(this,activity);
    }

    @Override
    public void onSucessfullyLogin(MUser mUser, String message) {
    startActivity(new Intent(activity,OtpVerificationActivity.class).putExtra(IS_FROM_SIGNUP,NO));
    }

    @Override
    public void onFailToLogin(String errorMessage) {
        Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show();
    }
}

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
import com.eb.onebandhan.auth.presenter.SignUpPresenter;
import com.eb.onebandhan.auth.viewinterface.SignUpViewInterface;
import com.eb.onebandhan.databinding.ActivitySignUpInitialBinding;
import com.eb.onebandhan.util.Constant;

public class SignUpInitialActivity extends AppCompatActivity implements SignUpViewInterface, Constant {
        private Activity activity;
        private ActivitySignUpInitialBinding binding;
        private SignUpPresenter signUpPresenter;
        private MSignUp mSignUp=new MSignUp();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=this;
        binding= DataBindingUtil.setContentView(activity,R.layout.activity_sign_up_initial);
        initialization();
        listner();
    }

    private void listner() {
        binding.btnSubmit.setOnClickListener(view -> performSignUp());
//        binding.btnSubmit.setOnClickListener(view -> startActivity(new Intent(activity,SignUpDetailActivity.class)));
    }

    private void performSignUp() {
        mSignUp.setName("aaddiiii");
        mSignUp.setMobileNumber("9407072257");
        signUpPresenter.performSignUpTask(mSignUp);
    }

    private void initialization() {
        signUpPresenter = new SignUpPresenter(this,activity);
    }

    @Override
    public void onSucessfullySignUp(MUser mUser, String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(activity,OtpVerificationActivity.class).putExtra(IS_FROM_SIGNUP,YES));
        finish();
    }

    @Override
    public void onFailToSignUp(String errorMessage) {
        Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show();

    }
}

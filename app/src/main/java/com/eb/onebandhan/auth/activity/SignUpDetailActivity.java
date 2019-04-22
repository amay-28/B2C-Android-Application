package com.eb.onebandhan.auth.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.os.Bundle;

import com.eb.onebandhan.R;
import com.eb.onebandhan.auth.presenter.SignUpDetailPresenter;
import com.eb.onebandhan.auth.viewinterface.SignUpDetailViewInterface;
import com.eb.onebandhan.databinding.ActivitySignUpDetailBinding;

public class SignUpDetailActivity extends AppCompatActivity implements SignUpDetailViewInterface {
    private Activity activity;
    private ActivitySignUpDetailBinding binding;
    private SignUpDetailPresenter signUpDetailPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        binding= DataBindingUtil.setContentView(activity,R.layout.activity_sign_up_detail);
        initialization();
    }

    private void initialization() {
        signUpDetailPresenter=new SignUpDetailPresenter(this,activity);
    }
}

package com.eb.onebandhan.dashboard.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.eb.onebandhan.R;
import com.eb.onebandhan.auth.model.MUser;
import com.eb.onebandhan.dashboard.presenter.EditProfilePresenter;
import com.eb.onebandhan.dashboard.viewinterface.EditProfileViewInterface;
import com.eb.onebandhan.databinding.ActivityEditProfileBinding;
import com.eb.onebandhan.databinding.ActivityMyProfileBinding;
import com.eb.onebandhan.util.Constant;
import com.eb.onebandhan.util.Session;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class MyProfileActivity extends AppCompatActivity {
    private Activity activity;
    private ActivityMyProfileBinding binding;
    private MUser loggedInUser;
    private MUser mUser = new MUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_my_profile);
        loggedInUser = new Session(activity).getUserProfile();
        initialization();
        listeners();
    }

    private void initialization() {
        setDetails();
    }

    private void listeners() {
        binding.btnSubmit.setOnClickListener(v -> {
            startActivity(new Intent(activity, EditProfileActivity.class));
        });
    }

    private void setDetails() {
        if (loggedInUser != null) {
            binding.tvName.setText(loggedInUser.getName());
            binding.tvMobileNo.setText(loggedInUser.getMobileNumber());
            binding.tvEmail.setText(loggedInUser.getEmail());

            if (loggedInUser.getRetailerDetails() != null) {
                binding.tvShopName.setText(loggedInUser.getRetailerDetails().getShopName());
                binding.tvAddLine1.setText(loggedInUser.getRetailerDetails().getAddressLine1());
                binding.tvAddLine2.setText(loggedInUser.getRetailerDetails().getAddressLine2());
                binding.tvCity.setText(loggedInUser.getRetailerDetails().getCity());
                binding.tvState.setText(loggedInUser.getRetailerDetails().getState());
                //binding.etAboutShop.setText(loggedInUser.getRetailerDetails().get);
                binding.tvPanNumber.setText(loggedInUser.getRetailerDetails().getPanNumber());
                binding.tvGst.setText(loggedInUser.getRetailerDetails().getGstin());
                //binding.etGstPercent.setText(loggedInUser.getRetailerDetails().getGs);
            }
        }


    }

}

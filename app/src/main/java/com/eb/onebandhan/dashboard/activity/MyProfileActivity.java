package com.eb.onebandhan.dashboard.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.eb.onebandhan.R;
import com.eb.onebandhan.auth.model.MUser;
import com.eb.onebandhan.databinding.ActivityMyProfileBinding;
import com.eb.onebandhan.util.CommonClickHandler;
import com.eb.onebandhan.util.Session;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class MyProfileActivity extends AppCompatActivity {
    private Activity activity;
    private ActivityMyProfileBinding binding;
    private MUser mUser;
    private int OPEN_EDIT_FROM_MY_PROFILE = 100;

    @Override
    public void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mUser = new Session(activity).getUserProfile();
        setDetails(mUser);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_my_profile);
        initialization();
    }

    private void initialization() {
        binding.header.setHandler(new CommonClickHandler(activity));
        binding.header.tvMainHeading.setText(R.string.txt_profile);
        listeners();
    }

    private void listeners() {
        binding.btnSubmit.setOnClickListener(v -> {
            startActivityForResult(new Intent(activity, EditProfileActivity.class), OPEN_EDIT_FROM_MY_PROFILE);
        });
    }

    private void setDetails(MUser loggedInUser) {
        if (loggedInUser != null) {
            if (loggedInUser.getRetailerDetails().getImageUrl() != null && !loggedInUser.getRetailerDetails().getImageUrl().isEmpty())
                Glide.with(activity)
                        .load(loggedInUser.getRetailerDetails().getImageUrl())
                        .apply(new RequestOptions()
                                .placeholder(R.mipmap.avtar_gray)
                                .error(R.mipmap.avtar_gray))
                        .into(binding.ivProfile);

            binding.tvName.setText(loggedInUser.getName());
            binding.tvMobileNo.setText(loggedInUser.getMobileNumber());
            binding.tvEmail.setText(loggedInUser.getEmail());

            if (loggedInUser.getRetailerDetails() != null) {
                binding.tvShopName.setText(loggedInUser.getRetailerDetails().getShopName());
                binding.tvAddLine1.setText(loggedInUser.getRetailerDetails().getAddressLine1());
                binding.tvAddLine2.setText(loggedInUser.getRetailerDetails().getAddressLine2());
                binding.tvCity.setText(loggedInUser.getRetailerDetails().getCity());
                binding.tvState.setText(loggedInUser.getRetailerDetails().getState());
                binding.tvAboutShop.setText(loggedInUser.getRetailerDetails().getAboutShop());
                binding.tvPanNumber.setText(loggedInUser.getRetailerDetails().getPanNumber());
                binding.tvGst.setText(loggedInUser.getRetailerDetails().getGstin());
                binding.tvGstPercent.setText(loggedInUser.getRetailerDetails().getGstPercent());
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OPEN_EDIT_FROM_MY_PROFILE && resultCode == RESULT_OK) {
            mUser = new Session(activity).getUserProfile();
            setDetails(mUser);
        }
    }
}

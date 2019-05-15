package com.eb.onebandhan.dashboard.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.eb.onebandhan.R;
import com.eb.onebandhan.auth.model.MCategory;
import com.eb.onebandhan.auth.model.MProfile;
import com.eb.onebandhan.auth.model.MUser;
import com.eb.onebandhan.auth.presenter.LoginPresenter;
import com.eb.onebandhan.dashboard.presenter.EditProfilePresenter;
import com.eb.onebandhan.dashboard.viewinterface.EditProfileViewInterface;
import com.eb.onebandhan.databinding.ActivityEditProfileBinding;
import com.eb.onebandhan.util.Constant;
import com.eb.onebandhan.util.Session;
import com.eb.onebandhan.util.Utils;
import com.eb.onebandhan.util.ValidationUtil;

import java.util.ArrayList;
import java.util.List;

public class EditProfileActivity extends AppCompatActivity implements EditProfileViewInterface, Constant {
    private Activity activity;
    private ActivityEditProfileBinding binding;
    private MUser loggedInUser;
    private EditProfilePresenter editProfilePresenter;
    private MUser mUser = new MUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_edit_profile);
        loggedInUser = new Session(activity).getUserProfile();
        initialization();
        listeners();
    }

    private void initialization() {
        editProfilePresenter = new EditProfilePresenter(this, activity);
        setDetails();
    }

    private void listeners() {
        binding.tvEditGeneral.setOnClickListener(v -> generalEdit());
        binding.tvEditBusiness.setOnClickListener(v -> businessEdit());
        binding.btnSubmit.setOnClickListener(v -> {
            if (checkValidate()) editProfilePresenter.onUpdateProfile(mUser);
        });
    }

    private boolean checkValidate() {
        Resources resources = getResources();
        if (TextUtils.isEmpty(binding.etName.getText().toString())) {
            Toast.makeText(activity, resources.getString(R.string.please_enter_name), Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(binding.etShopName.getText().toString())) {
            Toast.makeText(activity, resources.getString(R.string.please_enter_shop_name), Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(binding.etAddLine1.getText().toString())) {
            Toast.makeText(activity, resources.getString(R.string.please_enter_address), Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(binding.etAddLine2.getText().toString())) {
            Toast.makeText(activity, resources.getString(R.string.please_enter_address), Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(binding.etCity.getText().toString())) {
            Toast.makeText(activity, resources.getString(R.string.please_enter_city), Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(binding.etState.getText().toString())) {
            Toast.makeText(activity, resources.getString(R.string.please_enter_state), Toast.LENGTH_SHORT).show();
            return false;
        }else if (TextUtils.isEmpty(binding.etAboutShop.getText().toString())) {
            Toast.makeText(activity, resources.getString(R.string.please_enter_about_the_shop), Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(binding.etPanNumber.getText().toString())) {
            Toast.makeText(activity, resources.getString(R.string.please_enter_pan_number), Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(binding.etGst.getText().toString())) {
            Toast.makeText(activity, resources.getString(R.string.please_enter_gst), Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(binding.etGstPercent.getText().toString())) {
            Toast.makeText(activity, resources.getString(R.string.please_enter_gst_percent), Toast.LENGTH_SHORT).show();
            return false;
        }
        prepareData();
        return true;
    }

    private void prepareData() {
        // have to add more fields
        mUser.setName(binding.etName.getText().toString());
        mUser.getRetailerDetails().setShopName(binding.etShopName.getText().toString());
        mUser.getRetailerDetails().setAddressLine1(binding.etAddLine1.getText().toString());
        mUser.getRetailerDetails().setAddressLine2(binding.etAddLine2.getText().toString());
        mUser.getRetailerDetails().setCity(binding.etCity.getText().toString());
        mUser.getRetailerDetails().setState(binding.etState.getText().toString());
        mUser.getRetailerDetails().setPanNumber(binding.etPanNumber.getText().toString());
        mUser.getRetailerDetails().setGstin(binding.etGst.getText().toString());
    }

    private void generalEdit() {
        binding.etName.setEnabled(true);
        binding.etName.setTextColor(getResources().getColor(R.color.black));
        binding.etName.requestFocus();


        binding.etShopName.setEnabled(false);
        binding.etShopName.setTextColor(getResources().getColor(R.color.colorHint));
        binding.etAddLine1.setEnabled(false);
        binding.etAddLine1.setTextColor(getResources().getColor(R.color.colorHint));
        binding.etAddLine2.setEnabled(false);
        binding.etAddLine2.setTextColor(getResources().getColor(R.color.colorHint));
        binding.etCity.setEnabled(false);
        binding.etCity.setTextColor(getResources().getColor(R.color.colorHint));
        binding.etState.setEnabled(false);
        binding.etState.setTextColor(getResources().getColor(R.color.colorHint));
        binding.etAboutShop.setEnabled(false);
        binding.etAboutShop.setTextColor(getResources().getColor(R.color.colorHint));
        binding.etPanNumber.setEnabled(false);
        binding.etPanNumber.setTextColor(getResources().getColor(R.color.colorHint));
        binding.etGst.setEnabled(false);
        binding.etGst.setTextColor(getResources().getColor(R.color.colorHint));
        binding.etGstPercent.setEnabled(false);
        binding.etGstPercent.setTextColor(getResources().getColor(R.color.colorHint));
    }

    private void businessEdit() {
        binding.etName.setEnabled(false);
        binding.etName.setTextColor(getResources().getColor(R.color.colorHint));

        binding.etShopName.setEnabled(true);
        binding.etShopName.setTextColor(getResources().getColor(R.color.black));
        binding.etShopName.requestFocus();

        binding.etAddLine1.setEnabled(true);
        binding.etAddLine1.setTextColor(getResources().getColor(R.color.black));
        binding.etAddLine2.setEnabled(true);
        binding.etAddLine2.setTextColor(getResources().getColor(R.color.black));
        binding.etCity.setEnabled(true);
        binding.etCity.setTextColor(getResources().getColor(R.color.black));
        binding.etState.setEnabled(true);
        binding.etState.setTextColor(getResources().getColor(R.color.black));
        binding.etAboutShop.setEnabled(true);
        binding.etAboutShop.setTextColor(getResources().getColor(R.color.black));
        binding.etPanNumber.setEnabled(true);
        binding.etPanNumber.setTextColor(getResources().getColor(R.color.black));
        binding.etGst.setEnabled(true);
        binding.etGst.setTextColor(getResources().getColor(R.color.black));
        binding.etGstPercent.setEnabled(true);
        binding.etGstPercent.setTextColor(getResources().getColor(R.color.black));

    }

    private void setDetails() {
        if (loggedInUser != null) {
            binding.etName.setText(loggedInUser.getName());
            binding.etMobileNo.setText(loggedInUser.getMobileNumber());
            binding.etEmail.setText(loggedInUser.getEmail());

            if (loggedInUser.getRetailerDetails() != null) {
                binding.etShopName.setText(loggedInUser.getRetailerDetails().getShopName());
                binding.etAddLine1.setText(loggedInUser.getRetailerDetails().getAddressLine1());
                binding.etAddLine2.setText(loggedInUser.getRetailerDetails().getAddressLine2());
                binding.etCity.setText(loggedInUser.getRetailerDetails().getCity());
                binding.etState.setText(loggedInUser.getRetailerDetails().getState());
                //binding.etAboutShop.setText(loggedInUser.getRetailerDetails().get);
                binding.etPanNumber.setText(loggedInUser.getRetailerDetails().getPanNumber());
                binding.etGst.setText(loggedInUser.getRetailerDetails().getGstin());
                //binding.etGstPercent.setText(loggedInUser.getRetailerDetails().getGs);
            }
        }


    }

    @Override
    public void onSucessfullyUpdated(MUser mUser, String message) {

    }

    @Override
    public void onFailToUpdate(String errorMessage) {

    }
}

package com.eb.onebandhan.dashboard.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.eb.onebandhan.R;
import com.eb.onebandhan.auth.model.MProfile;
import com.eb.onebandhan.auth.model.MUser;
import com.eb.onebandhan.dashboard.presenter.EditProfilePresenter;
import com.eb.onebandhan.dashboard.viewinterface.EditProfileViewInterface;
import com.eb.onebandhan.databinding.ActivityEditProfileBinding;
import com.eb.onebandhan.util.CommonClickHandler;
import com.eb.onebandhan.util.Constant;
import com.eb.onebandhan.util.Session;
import com.eb.onebandhan.util.Utils;
import com.eb.onebandhan.util.WebService;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import okhttp3.MultipartBody;

public class EditProfileActivity extends AppCompatActivity implements EditProfileViewInterface, Constant {
    private Activity activity;
    private ActivityEditProfileBinding binding;
    private EditProfilePresenter editProfilePresenter;
    private MUser loggedInUser;
    private MUser mUser = new MUser();
    MProfile retailerDetails = new MProfile();
    private Uri imageURI;
    private String profileImageURL = "";

    @Override
    public void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_edit_profile);
        initialization();
    }

    private void initialization() {
        loggedInUser = new Session(activity).getUserProfile();
        binding.header.setHandler(new CommonClickHandler(activity));
        binding.header.tvMainHeading.setText(R.string.txt_editprofile);
        editProfilePresenter = new EditProfilePresenter(this, activity);
        setDetails();
        listeners();
    }

    private void setDetails() {
        if (loggedInUser != null) {
            if (loggedInUser.getImageUrl() != null && !loggedInUser.getImageUrl().isEmpty())
                Glide.with(activity).load(loggedInUser.getImageUrl()).apply(new RequestOptions().placeholder(R.mipmap.avtar_gray).error(R.mipmap.avtar_gray)).into(binding.imgUser);

            binding.etName.setText(loggedInUser.getName());
            binding.etMobileNo.setText(loggedInUser.getMobileNumber());
            binding.etEmail.setText(loggedInUser.getEmail());

            if (loggedInUser.getRetailerDetails() != null) {
                binding.etShopName.setText(loggedInUser.getRetailerDetails().getShopName());
                binding.etAddLine1.setText(loggedInUser.getRetailerDetails().getAddressLine1());
                binding.etAddLine2.setText(loggedInUser.getRetailerDetails().getAddressLine2());
                binding.etCity.setText(loggedInUser.getRetailerDetails().getCity());
                binding.etState.setText(loggedInUser.getRetailerDetails().getState());
                binding.etAboutShop.setText(loggedInUser.getRetailerDetails().getAboutShop());
                binding.etPanNumber.setText(loggedInUser.getRetailerDetails().getPanNumber());
                binding.etGst.setText(loggedInUser.getRetailerDetails().getGstin());
                binding.etGstPercent.setText(loggedInUser.getRetailerDetails().getGstPercent());
            }
        }
    }

    private void listeners() {
        binding.layRelImage.setOnClickListener(v -> getProfileImage());
        binding.tvEditGeneral.setOnClickListener(v -> generalEdit());
        binding.tvEditBusiness.setOnClickListener(v -> businessEdit());
        binding.btnSubmit.setOnClickListener(v -> {
            if (checkValidate()) editProfilePresenter.onUpdateProfile(retailerDetails);
        });
    }

    private boolean checkValidate() {
        Resources resources = getResources();
        if (TextUtils.isEmpty(binding.etName.getText().toString().trim())) {
            Utils.ShowToast(activity, resources.getString(R.string.please_enter_name), 0);
            return false;
        } else if (binding.etName.getText().toString().trim().length() < 3) {
            Utils.ShowToast(activity, resources.getString(R.string.name_length), 0);
            return false;
        } else if (TextUtils.isEmpty(binding.etShopName.getText().toString().trim())) {
            Utils.ShowToast(activity, resources.getString(R.string.please_enter_shop_name), 0);
            return false;
        } else if (binding.etShopName.getText().toString().trim().length() < 3) {
            Utils.ShowToast(activity, resources.getString(R.string.shop_name_length), 0);
            return false;
        } else if (TextUtils.isEmpty(binding.etAddLine1.getText().toString().trim())) {
            Utils.ShowToast(activity, resources.getString(R.string.address_line1_blank), 0);
            return false;
        } else if (TextUtils.isEmpty(binding.etAddLine2.getText().toString().trim())) {
            Utils.ShowToast(activity, resources.getString(R.string.address_line2_blank), 0);
            return false;
        } else if (TextUtils.isEmpty(binding.etCity.getText().toString().trim())) {
            Utils.ShowToast(activity, resources.getString(R.string.please_enter_city), 0);
            return false;
        } else if (binding.etCity.getText().toString().trim().length() < 2) {
            Utils.ShowToast(activity, resources.getString(R.string.city_name_length), 0);
            return false;
        } else if (TextUtils.isEmpty(binding.etState.getText().toString().trim())) {
            Utils.ShowToast(activity, resources.getString(R.string.please_enter_state), 0);
            return false;
        } else if (binding.etState.getText().toString().trim().length() < 2) {
            Utils.ShowToast(activity, resources.getString(R.string.state_name_length), 0);
            return false;
        } else if (TextUtils.isEmpty(binding.etAboutShop.getText().toString().trim())) {
            Utils.ShowToast(activity, resources.getString(R.string.please_enter_about_the_shop), 0);
            return false;
        } else if (binding.etAboutShop.getText().toString().trim().length() < 3) {
            Utils.ShowToast(activity, resources.getString(R.string.about_shop_length), 0);
            return false;
        } else if (TextUtils.isEmpty(binding.etPanNumber.getText().toString().trim())) {
            Utils.ShowToast(activity, resources.getString(R.string.please_enter_pan_number), 0);
            return false;
        } else if (binding.etPanNumber.getText().toString().trim().length() < 2) {
            Utils.ShowToast(activity, resources.getString(R.string.pan_number_length), 0);
            return false;
        } else if (TextUtils.isEmpty(binding.etGst.getText().toString().trim())) {
            Utils.ShowToast(activity, resources.getString(R.string.please_enter_gst), 0);
            return false;
        } else if (binding.etGst.getText().toString().trim().length() < 2) {
            Utils.ShowToast(activity, resources.getString(R.string.gst_length), 0);
            return false;
        } else if (TextUtils.isEmpty(binding.etGstPercent.getText().toString().trim())) {
            Utils.ShowToast(activity, resources.getString(R.string.please_enter_gst_percent), 0);
            return false;
        }
        prepareData();
        return true;
    }

    private void prepareData() {

        /*if (!profileImageURL.isEmpty())
            mUser.setImageUrl(profileImageURL);*/

        //mUser.setName(binding.etName.getText().toString());


        if (!profileImageURL.isEmpty())
            retailerDetails.setImageUrl(profileImageURL);

        retailerDetails.setName(binding.etName.getText().toString());

        retailerDetails.setShopName(binding.etShopName.getText().toString());
        retailerDetails.setAddressLine1(binding.etAddLine1.getText().toString());
        retailerDetails.setAddressLine2(binding.etAddLine2.getText().toString());
        retailerDetails.setCity(binding.etCity.getText().toString());
        retailerDetails.setState(binding.etState.getText().toString());
        retailerDetails.setAboutShop(binding.etAboutShop.getText().toString());
        retailerDetails.setPanNumber(binding.etPanNumber.getText().toString());
        retailerDetails.setGstin(binding.etGst.getText().toString());
        retailerDetails.setGstPercent(binding.etGstPercent.getText().toString());
       // retailerDetails.setPostalCode("452003");

        //mUser.setRetailerDetails(retailerDetails);
    }

    private void getProfileImage() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            CropImage.activity()
                    .setActivityTitle(getString(R.string.app_name))
                    .setCropShape(CropImageView.CropShape.RECTANGLE)
                    .setActivityMenuIconColor(R.color.colorMobileProfile)
                    .setBorderLineColor(Color.WHITE)
                    .setGuidelinesColor(R.color.colorPrimary)
                    .setAspectRatio(1, 1)
                    .setFixAspectRatio(true)
                    .start((Activity) this);
        } else {
            CropImage.activity()
                    .setActivityTitle(getString(R.string.app_name))
                    .setCropShape(CropImageView.CropShape.OVAL)
                    .setActivityMenuIconColor(R.color.colorMobileProfile)
                    .setBorderLineColor(Color.WHITE)
                    .setGuidelinesColor(R.color.colorPrimary)
                    .setAspectRatio(1, 1)
                    .setFixAspectRatio(true)
                    .start((Activity) activity);
        }
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

    @Override
    public void onSucessfullyUpdated(MUser mUser, String message) {
       /* MUser mUser = new MUser();
        mUser.setName(mProfile.getName());
        mUser.setImageUrl(mProfile.getImageUrl());
        mUser.setMobileNumber(mProfile.getMobileNumber());
        mUser.setRetailerDetails(mProfile);*/

        new Session(activity).setUserProfile(mUser);

        Utils.ShowToast(activity, message, 0);
        finish();
    }

    @Override
    public void onSucessfullyUpdatedImage(String value) {
        if (!Utils.checkNull(value).isEmpty()) {
            profileImageURL = value;
            binding.imgUser.setImageURI(imageURI);
        }
    }

    @Override
    public void onFailToUpdate(String errorMessage) {
        Utils.ShowToast(activity, errorMessage, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            CreateFileForSend(result.getUri());
        }
    }

    // Crop image convert to file
    public void CreateFileForSend(Uri URI) {
        imageURI = URI;
        File image = Utils.compressURI(activity, URI, "");

        MultipartBody.Part file = null;
        if (image != null)
            file = Utils.getFileRequestBody_part(WebService.FILE, image);

        editProfilePresenter.onUpdateImage(file);
    }
}

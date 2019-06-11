package com.retailer.oneops.dashboard.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.retailer.oneops.R;
import com.retailer.oneops.auth.model.MProfile;
import com.retailer.oneops.auth.model.MUser;
import com.retailer.oneops.dashboard.presenter.EditProfilePresenter;
import com.retailer.oneops.dashboard.viewinterface.EditProfileViewInterface;
import com.retailer.oneops.databinding.ActivityEditProfileBinding;
import com.retailer.oneops.util.CommonClickHandler;
import com.retailer.oneops.util.Constant;
import com.retailer.oneops.util.CustomSpinnerAdapter;
import com.retailer.oneops.util.MarshmallowPermission;
import com.retailer.oneops.util.MyDialogProgress;
import com.retailer.oneops.util.Session;
import com.retailer.oneops.util.Utils;
import com.retailer.oneops.util.ValidationUtil;
import com.retailer.oneops.util.WebService;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import okhttp3.MultipartBody;

import static android.os.Build.VERSION_CODES.M;

public class EditProfileActivity extends AppCompatActivity implements EditProfileViewInterface, Constant {
    private Activity activity;
    private ActivityEditProfileBinding binding;
    private EditProfilePresenter editProfilePresenter;
    private MUser loggedInUser;
    private MUser mUser = new MUser();
    MProfile retailerDetails = new MProfile();
    private Uri imageURI;
    private String profileImageURL = "";
    private CustomSpinnerAdapter spinnerAdapter;
    private MarshmallowPermission marshmallowPermission;
    private String[] gstPercentageArray;

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
        gstPercentageArray = getResources().getStringArray(R.array.gst_type_array);
        marshmallowPermission = new MarshmallowPermission(activity);
        loggedInUser = new Session(activity).getUserProfile();
        binding.header.setHandler(new CommonClickHandler(activity));
        binding.header.tvMainHeading.setText(R.string.txt_editprofile);
        editProfilePresenter = new EditProfilePresenter(this, activity);

        listeners();
        bindSpinner();
        setDetails();
    }

    private void bindSpinner() {
        spinnerAdapter = new CustomSpinnerAdapter(activity, gstPercentageArray);
        binding.spinnerGstPercent.setAdapter(spinnerAdapter);
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
                binding.etAboutShop.setText(loggedInUser.getRetailerDetails().getAboutShop());
                binding.etPanNumber.setText(loggedInUser.getRetailerDetails().getPanNumber());
                binding.etGst.setText(loggedInUser.getRetailerDetails().getGstin());

                String gstPercent = loggedInUser.getRetailerDetails().getGstPercent();
                int selectedPosition = 0;
                for (int i = 0; i < gstPercentageArray.length; i++) {
                    if (gstPercentageArray[i].equals(gstPercent)) {
                        selectedPosition = i;
                    }
                }

                binding.spinnerGstPercent.setSelection(selectedPosition);

                if (loggedInUser.getRetailerDetails().getImageUrl() != null && !loggedInUser.getRetailerDetails().getImageUrl().isEmpty())
                    Glide.with(activity)
                            .load(loggedInUser.getRetailerDetails().getImageUrl())
                            .apply(new RequestOptions()
                                    .placeholder(R.mipmap.avtar_gray)
                                    .error(R.mipmap.avtar_gray))
                            .into(binding.imgUser);


            }
        }
    }

    private void listeners() {
        // binding.layRelImage.setOnClickListener(v -> getProfileImage());
        binding.layRelImage.setOnClickListener(v -> EnableRuntimePermission());
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
        } else if (!TextUtils.isEmpty(binding.etPanNumber.getText().toString().trim()) &&
                !ValidationUtil.isValidPAN(binding.etPanNumber.getText().toString().trim())) {
            Utils.ShowToast(activity, resources.getString(R.string.please_enter_valid_pan_number), 0);
            return false;
        } else if (TextUtils.isEmpty(binding.etGst.getText().toString().trim())) {
            Utils.ShowToast(activity, resources.getString(R.string.please_enter_gst), 0);
            return false;
        } else if (!ValidationUtil.isValidGST(binding.etGst.getText().toString().trim())) {
            Utils.ShowToast(activity, resources.getString(R.string.please_enter_valid_gst), 0);
            return false;
        } else if (binding.spinnerGstPercent.getSelectedItemPosition() == 0) {
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
        retailerDetails.setGstPercent(binding.spinnerGstPercent.getSelectedItem().toString().trim());
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
        //binding.etGstPercent.setEnabled(false);
        // binding.etGstPercent.setTextColor(getResources().getColor(R.color.colorHint));
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
        //binding.etGstPercent.setEnabled(true);
        //binding.etGstPercent.setTextColor(getResources().getColor(R.color.black));
    }

    public void EnableRuntimePermission() {
        if (Build.VERSION.SDK_INT >= M) {
            boolean result = marshmallowPermission.checkPermissionForWriteExternalStorage();
            if (result) {
                getProfileImage();
            } else {
                try {
                    marshmallowPermission.requestPermissionForWriteExternalStorage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            getProfileImage();
        }
    }

    /**
     * Callback for the result from requesting permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //Log.d(TAG, "requestCode---" + requestCode + "     " + "grantResults----" + grantResults);
        switch (requestCode) {
            case MarshmallowPermission.EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(activity, "Permission granted", Toast.LENGTH_SHORT).show();
                    getProfileImage();
                } else {
                    Toast.makeText(activity, "Permission denied", Toast.LENGTH_SHORT).show();
                    //code for deny
                }
        }
    }

    @Override
    public void onSucessfullyUpdated(MUser mUser, String message) {
        new Session(activity).setUserProfile(mUser);
        Utils.ShowToast(activity, message, 0);
        Intent data = new Intent();
        data.putExtra("isUpdated", true);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onSucessfullyUpdatedImage(String value) {
        MyDialogProgress.close(activity);
        if (!Utils.checkNull(value).isEmpty()) {
            profileImageURL = value;
            binding.imgUser.setImageURI(imageURI);
        }
    }

    @Override
    public void onFailToUpdate(String errorMessage) {
        MyDialogProgress.close(activity);
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
        MyDialogProgress.open(activity);
        imageURI = URI;
        File image = Utils.compressURI(activity, URI, "");

        MultipartBody.Part file = null;
        if (image != null)
            file = Utils.getFileRequestBody_part(WebService.FILE, image);

        editProfilePresenter.onUpdateImage(file);
    }
}

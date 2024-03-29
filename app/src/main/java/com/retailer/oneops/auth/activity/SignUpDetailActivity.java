package com.retailer.oneops.auth.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.retailer.oneops.R;
import com.retailer.oneops.auth.adapter.CategoryAdapter;
import com.retailer.oneops.auth.model.MAddress;
import com.retailer.oneops.auth.model.MCategory;
import com.retailer.oneops.auth.model.MProfile;
import com.retailer.oneops.auth.model.MUser;
import com.retailer.oneops.auth.presenter.SignUpDetailPresenter;
import com.retailer.oneops.auth.util.Categoryutil;
import com.retailer.oneops.auth.viewinterface.SignUpDetailViewInterface;
import com.retailer.oneops.databinding.ActivitySignUpDetailBinding;
import com.retailer.oneops.util.Constant;
import com.retailer.oneops.util.CustomSpinnerAdapter;
import com.retailer.oneops.util.ShowToast;
import com.retailer.oneops.util.ValidationUtil;
import com.google.android.material.chip.Chip;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignUpDetailActivity extends AppCompatActivity implements SignUpDetailViewInterface, Categoryutil, Constant {
    private Activity activity;
    private ActivitySignUpDetailBinding binding;
    private SignUpDetailPresenter signUpDetailPresenter;
    private Map<String, String> map = new HashMap<>();
    private List<MCategory> categoryList = new ArrayList<>();
    private CategoryAdapter categoryAdapter;
    private CustomSpinnerAdapter spinnerAdapter;
    private MProfile mProfile = new MProfile();
    private String[] gstPercentageArray;
    private Bundle bundle;
    private String mobileNumber;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_sign_up_detail);
        bundle = getIntent().getExtras();
        initialization();
        listner();
        bindSpinner();
    }

    private void initialization() {
        signUpDetailPresenter = new SignUpDetailPresenter(this, activity);
        gstPercentageArray = getResources().getStringArray(R.array.gst_type_array);
        map.put(LEVEL, ZERO);
        map.put(LIMIT, INFINITE_LIMIT);
        signUpDetailPresenter.loadCategoryTask(map);
        MCategory mCategory = new MCategory();
        mCategory.setId("00");
        mCategory.setName(getResources().getString(R.string.deals_in_category));
        categoryList.add(mCategory);
        categoryAdapter = new CategoryAdapter(activity, R.layout.row_spinner_text, categoryList);
        binding.spnCategory.setAdapter(categoryAdapter);

        if (bundle != null) {
            if (bundle.containsKey(MOBILE_NO)) {
                mobileNumber = bundle.getString(MOBILE_NO);
            }
            if (bundle.containsKey(NAME)) {
                username = bundle.getString(NAME);
            }

        }
    }

    private void listner() {
        binding.btnSubmit.setOnClickListener(view -> {
            if (validation()) signUpDetailPresenter.submitShopDetailTask(mProfile);
        });
        binding.spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                if (!categoryList.get(position).getIsSelected() && !categoryList.get(position).getId().equals("00")) {
                    Chip chip = new Chip(activity);
                    categoryList.get(position).setIsSelected(true);
                    chip.setText(categoryList.get(position).getName());
                    chip.setChipCornerRadius(3f);
                    chip.setTextColor(getResources().getColor(R.color.black));
                    chip.setCloseIconEnabled(true);
                    chip.setId(Integer.parseInt(categoryList.get(position).getId()));
                    chip.setOnCloseIconClickListener(view1 -> {
                        categoryList.get(position).setIsSelected(false);
                        binding.chipGroup.removeView(chip);
                    });
                    binding.chipGroup.addView(chip);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbYes) {
                    binding.etGstNo.setVisibility(View.VISIBLE);
                    binding.spinnerGstPercent.setVisibility(View.VISIBLE);
                    binding.etPanNo.setVisibility(View.VISIBLE);
                } else {
                    binding.etGstNo.setVisibility(View.GONE);
                    binding.spinnerGstPercent.setVisibility(View.GONE);
                    binding.etPanNo.setVisibility(View.GONE);
                }
            }
        });
    }


    private void bindSpinner() {
        spinnerAdapter = new CustomSpinnerAdapter(activity, gstPercentageArray);
        binding.spinnerGstPercent.setAdapter(spinnerAdapter);
    }

    private boolean validation() {
        Resources resources = getResources();
        if (TextUtils.isEmpty(binding.etShopName.getText().toString().trim())) {
            Toast.makeText(activity, resources.getString(R.string.please_enter_shop_name), Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(binding.etAddressOne.getText().toString().trim())) {
            Toast.makeText(activity, resources.getString(R.string.please_enter_address), Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(binding.etAddressTwo.getText().toString().trim())) {
            Toast.makeText(activity, resources.getString(R.string.please_enter_address), Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(binding.etCity.getText().toString().trim())) {
            Toast.makeText(activity, resources.getString(R.string.please_enter_city), Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(binding.etState.getText().toString().trim())) {
            Toast.makeText(activity, resources.getString(R.string.please_enter_state), Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(binding.etPincode.getText().toString().trim())) {
            Toast.makeText(activity, resources.getString(R.string.please_enter_pincode), Toast.LENGTH_SHORT).show();
            return false;
        } else if (binding.rbYes.isChecked() && TextUtils.isEmpty(binding.etGstNo.getText().toString().trim())) {
            Toast.makeText(activity, resources.getString(R.string.please_enter_gst), Toast.LENGTH_SHORT).show();
            return false;
        } else if (binding.rbYes.isChecked() && !ValidationUtil.isValidGST(binding.etGstNo.getText().toString().trim())) {
            Toast.makeText(activity, resources.getString(R.string.please_enter_valid_gst), Toast.LENGTH_SHORT).show();
            return false;
        } else if (binding.rbYes.isChecked() && (binding.spinnerGstPercent.getSelectedItemPosition() == 0)) {
            Toast.makeText(activity, resources.getString(R.string.select_valid_gst_percent), Toast.LENGTH_SHORT).show();
            return false;
        } else if (binding.rbYes.isChecked() &&
                !TextUtils.isEmpty(binding.etPanNo.getText().toString().trim()) &&
                !ValidationUtil.isValidPAN(binding.etPanNo.getText().toString().trim())) {
            Toast.makeText(activity, resources.getString(R.string.please_enter_valid_pan_number), Toast.LENGTH_SHORT).show();
            return false;
        } else if (!TextUtils.isEmpty(binding.etEmail.getText().toString().trim()) && !ValidationUtil.emailValidator(activity, binding.etEmail, "email")) {
            return false;
        }
        prepareData();
        return true;
    }

    private void prepareData() {
        List<MAddress> addressList = new ArrayList<>();
        MAddress mAddress = new MAddress();
        mAddress.setName(username);
        mAddress.setMobileNumber(mobileNumber);
        mAddress.setAddressLine1(binding.etAddressOne.getText().toString());
        mAddress.setAddressLine2(binding.etAddressTwo.getText().toString());
        mAddress.setCity(binding.etCity.getText().toString());
        mAddress.setState(binding.etState.getText().toString());
        mAddress.setPostalCode(binding.etPincode.getText().toString());
        addressList.add(mAddress);

        mProfile.setAddress(addressList);

        // have to add more fields
        mProfile.setShopName(binding.etShopName.getText().toString());
        mProfile.setEmail(binding.etEmail.getText().toString());
        mProfile.setGstin(binding.etGstNo.getText().toString());
        if (binding.spinnerGstPercent.getSelectedItemPosition() != 0)
            mProfile.setGstPercent(binding.spinnerGstPercent.getSelectedItem().toString().trim());
        mProfile.setPanNumber(binding.etPanNo.getText().toString());
        List<MProfile.MDealsIn> dealsInList = new ArrayList<>();
        if (!categoryList.isEmpty()) {
            for (MCategory mCategory : categoryList) {
                if (mCategory.getIsSelected()) {
                    MProfile.MDealsIn mDealsIn = new MProfile.MDealsIn();
                    mDealsIn.setId(mCategory.getId());
                    dealsInList.add(mDealsIn);
                }
            }
            mProfile.setDealsIn(dealsInList);
        }
    }

    @Override
    public void onFailTogetCategories(String message) {
        ShowToast.toastMsg(activity, message);
    }

    @Override
    public void onSucessfullygetCategories(List<MCategory> categoryList, String message) {
        this.categoryList.addAll(categoryList);
        categoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSucessfullySubmitShopDetail(MUser data, String message) {
        ShowToast.toastMsg(activity, getString(R.string.Retailer_account_created_successfully));
        startActivity(new Intent(this, ThankYouActivity.class));
    }

    @Override
    public void onFailToSubmitShopDetail(String message) {
        ShowToast.toastMsg(activity, message);
    }
}

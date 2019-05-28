package com.retailer.oneops.bankDetail.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.retailer.oneops.R;
import com.retailer.oneops.bankDetail.activity.model.MBankDetail;
import com.retailer.oneops.bankDetail.activity.presenter.BankDetailPresenter;
import com.retailer.oneops.auth.util.Categoryutil;
import com.retailer.oneops.bankDetail.activity.viewinterface.BankDetailViewInterface;
import com.retailer.oneops.dashboard.activity.DashboardActivity;
import com.retailer.oneops.databinding.ActivityAddBankDetailBinding;
import com.retailer.oneops.util.CommonClickHandler;
import com.retailer.oneops.util.Constant;
import com.retailer.oneops.util.CustomSpinnerAdapter;
import com.retailer.oneops.util.Session;

public class AddBankDetailActivity extends AppCompatActivity implements BankDetailViewInterface, Categoryutil, Constant {
    private Activity activity;
    private ActivityAddBankDetailBinding binding;
    private BankDetailPresenter bankDetailPresenter;
    private MBankDetail mBankDetail = new MBankDetail();
    private CustomSpinnerAdapter spinnerAdapter;
    private String accountTypeArray[];
    private MBankDetail savedBankDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_add_bank_detail);
        savedBankDetail = new Session(activity).getUserProfile().getBankDetails();

        accountTypeArray = getResources().getStringArray(R.array.account_type_array);
        //initialize Spinner
        bindSpinner();
        initialization();
        listner();
    }

    /**
     * Method to initialize Spinner.
     */
    private void bindSpinner() {
        spinnerAdapter = new CustomSpinnerAdapter(activity, accountTypeArray);
        binding.etAccountType.setAdapter(spinnerAdapter);
    }

    private void listner() {
        binding.btnUpload.setOnClickListener(view -> addBankDetailRequestObject());
    }

    private void initialization() {
        binding.header.setHandler(new CommonClickHandler(activity));
        binding.header.tvMainHeading.setText(R.string.Add_Bank_Details);
        bankDetailPresenter = new BankDetailPresenter(this
                , activity);

        if (savedBankDetail != null) {
            setExistingData();
        }
    }

    private void setExistingData() {
        binding.header.tvMainHeading.setText(R.string.Update_Bank_Details);
        binding.btnUpload.setText("Update");
        binding.etAccountHolderName.setText(savedBankDetail.getAccountHolderName());
        binding.etAccountNumber.setText(savedBankDetail.getAccountNumber());
        binding.etIFSCCode.setText(savedBankDetail.getIfscCode());

        if (accountTypeArray[1].equalsIgnoreCase(savedBankDetail.getAccountType())) {
            binding.etAccountType.setSelection(1);
        } else if (accountTypeArray[2].equalsIgnoreCase(savedBankDetail.getAccountType())) {
            binding.etAccountType.setSelection(2);
        }
    }

    /*private String returnAccountNumber(String account) {
        if (account.length() > 11)
            return "XXXX" + account.substring(11, account.length());

        return "";
    }*/

    private void addBankDetailRequestObject() {
        if (TextUtils.isEmpty(binding.etAccountHolderName.getText()))
            Toast.makeText(activity, getResources().getString(R.string.please_enter_valid_account_holder_name), Toast.LENGTH_SHORT).show();
        else if (binding.etAccountHolderName.getText().toString().length() < 3)
            Toast.makeText(activity, getResources().getString(R.string.please_enter_valid_account_holder_name), Toast.LENGTH_SHORT).show();
        else if (binding.etAccountNumber.getText().toString().length() != 15)
            Toast.makeText(activity, getResources().getString(R.string.please_enter_valid_account_no), Toast.LENGTH_SHORT).show();
        else if (binding.etIFSCCode.getText().toString().length() != 11)
            Toast.makeText(activity, getResources().getString(R.string.valid_IFSC), Toast.LENGTH_SHORT).show();
        else if (binding.etAccountType.getSelectedItemPosition() == 0)
            Toast.makeText(activity, getResources().getString(R.string.please_select_account_type), Toast.LENGTH_SHORT).show();
        else {
            mBankDetail.setAccountHolderName(binding.etAccountHolderName.getText().toString());
            mBankDetail.setAccountNumber(binding.etAccountNumber.getText().toString());
            mBankDetail.setIfscCode(binding.etIFSCCode.getText().toString());
            mBankDetail.setAccountType(binding.etAccountType.getSelectedItem().toString());
            bankDetailPresenter.uploadBankDetailTask(mBankDetail);
        }

    }


    @Override
    public void onSuccessfulUploadBankDetails(MBankDetail mBankDetail, String message) {
        Intent intent = new Intent(activity, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onFailedUploadBankDetails(String errorMessage) {
        Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show();

    }
}

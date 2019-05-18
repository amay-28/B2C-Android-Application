package com.eb.onebandhan.bankDetail.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.eb.onebandhan.R;
import com.eb.onebandhan.bankDetail.activity.model.MBankDetail;
import com.eb.onebandhan.bankDetail.activity.presenter.BankDetailPresenter;
import com.eb.onebandhan.auth.util.Categoryutil;
import com.eb.onebandhan.bankDetail.activity.viewinterface.BankDetailViewInterface;
import com.eb.onebandhan.databinding.ActivityAddBankDetailBinding;
import com.eb.onebandhan.util.CommonClickHandler;
import com.eb.onebandhan.util.Constant;
import com.eb.onebandhan.util.CustomSpinnerAdapter;

public class AddBankDetailActivity extends AppCompatActivity implements BankDetailViewInterface, Categoryutil, Constant {
    private Activity activity;
    private ActivityAddBankDetailBinding binding;
    private BankDetailPresenter bankDetailPresenter;
    private MBankDetail mBankDetail = new MBankDetail();
    private CustomSpinnerAdapter spinnerAdapter;
    private String account_type[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_add_bank_detail);
        account_type = getResources().getStringArray(R.array.account_type_array);
        //initialize Spinner
        bindSpinner();
        initialization();
        listner();
    }

    /**
     * Method to initialize Spinner.
     */
    private void bindSpinner() {
        spinnerAdapter = new CustomSpinnerAdapter(activity, account_type);
        binding.etAccountType.setAdapter(spinnerAdapter);
    }

    private void listner() {
        binding.btnUpload.setOnClickListener(view -> addBankDetailRequestObject());
//        binding.btnSubmit.setOnClickListener(view -> startActivity(new Intent(activity,SignUpDetailActivity.class)));

    }

    private void initialization() {
        binding.header.setHandler(new CommonClickHandler(activity));
        binding.header.tvMainHeading.setText(R.string.Add_Bank_Details);
        bankDetailPresenter = new BankDetailPresenter(this
                , activity);

    }

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
        finish();
    }

    @Override
    public void onFailedUploadBankDetails(String errorMessage) {
        Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show();

    }
}

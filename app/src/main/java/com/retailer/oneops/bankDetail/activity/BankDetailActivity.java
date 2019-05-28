package com.retailer.oneops.bankDetail.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.retailer.oneops.R;
import com.retailer.oneops.bankDetail.activity.model.MBankDetail;
import com.retailer.oneops.databinding.ActivityBankDetailBinding;
import com.retailer.oneops.util.CommonClickHandler;
import com.retailer.oneops.util.Constant;
import com.retailer.oneops.util.Session;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class BankDetailActivity extends AppCompatActivity implements Constant {
    private Activity activity;
    private ActivityBankDetailBinding binding;
    private MBankDetail mBankDetail = new MBankDetail();
    private MBankDetail savedBankDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_bank_detail);
        savedBankDetail = new Session(activity).getUserProfile().getBankDetails();

        initialization();
        listener();
    }


    private void listener() {
        binding.tvEdit.setOnClickListener(view -> startActivity(new Intent(activity, AddBankDetailActivity.class)));
    }

    private void initialization() {
        binding.header.setHandler(new CommonClickHandler(activity));
        binding.header.tvMainHeading.setText(R.string.Bank_Details);

        setExistingData();
    }

    private void setExistingData() {
        binding.tvAccountHolderName.setText(savedBankDetail.getAccountHolderName());
        binding.tvAccountNumber.setText(returnAccountNumber(savedBankDetail.getAccountNumber()));
        binding.tvIFSCCode.setText(savedBankDetail.getIfscCode());
        binding.tvAccountType.setText(savedBankDetail.getAccountType());
    }

    private String returnAccountNumber(String account) {
        if (account != null) {
            if (account.length() > 11)
                return "XXXX" + account.substring(11, account.length());
        }
        return "";
    }
}

package com.eb.onebandhan.bankDetail.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.eb.onebandhan.R;
import com.eb.onebandhan.auth.util.Categoryutil;
import com.eb.onebandhan.bankDetail.activity.model.MBankDetail;
import com.eb.onebandhan.bankDetail.activity.presenter.BankDetailPresenter;
import com.eb.onebandhan.bankDetail.activity.viewinterface.BankDetailViewInterface;
import com.eb.onebandhan.databinding.ActivityAddBankDetailBinding;
import com.eb.onebandhan.databinding.ActivityBankDetailBinding;
import com.eb.onebandhan.util.CommonClickHandler;
import com.eb.onebandhan.util.Constant;
import com.eb.onebandhan.util.CustomSpinnerAdapter;
import com.eb.onebandhan.util.Session;

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
        savedBankDetail = new Session(activity).getBankDetail();

        initialization();
        listner();
    }


    private void listner() {
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

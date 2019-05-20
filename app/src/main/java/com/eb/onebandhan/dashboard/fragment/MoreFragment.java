package com.eb.onebandhan.dashboard.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eb.onebandhan.R;
import com.eb.onebandhan.auth.model.MUser;
import com.eb.onebandhan.bankDetail.activity.AddBankDetailActivity;
import com.eb.onebandhan.bankDetail.activity.BankDetailActivity;
import com.eb.onebandhan.dashboard.activity.DashboardActivity;
import com.eb.onebandhan.dashboard.activity.EditProfileActivity;
import com.eb.onebandhan.dashboard.activity.MyProfileActivity;
import com.eb.onebandhan.databinding.MoreFragmentLayoutBinding;
import com.eb.onebandhan.product.AddProductActivity;
import com.eb.onebandhan.util.Session;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

public class MoreFragment extends Fragment {
    private Activity activity;
    private MoreFragmentLayoutBinding binding;
    private MUser loggedInUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        activity = getActivity();
        binding = DataBindingUtil.inflate(inflater, R.layout.more_fragment_layout, container, false);
        View view = binding.getRoot();
        loggedInUser = new Session(activity).getUserProfile();
        initialization();
        listner();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        listner();
    }

    private void listner() {
        binding.tvEdit.setOnClickListener(v -> startActivity(new Intent(getActivity(), EditProfileActivity.class)));
        binding.tvAddToInventory.setOnClickListener(v -> startActivity(new Intent(getActivity(), AddProductActivity.class)));
        binding.rlProfile.setOnClickListener(v -> startActivity(new Intent(getActivity(), MyProfileActivity.class)));

        if (new Session(activity).getBankDetail() != null) {
            binding.tvBankDetails.setOnClickListener(v -> startActivity(new Intent(getActivity(), BankDetailActivity.class)));
        } else {
            binding.tvBankDetails.setOnClickListener(v -> startActivity(new Intent(getActivity(), AddBankDetailActivity.class)));
        }
    }

    private void initialization() {
        binding.tvName.setText(loggedInUser.getName());
        binding.tvPhone.setText(loggedInUser.getMobileNumber());
    }
}

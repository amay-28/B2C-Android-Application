package com.retailer.oneops.dashboard.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.retailer.oneops.R;
import com.retailer.oneops.auth.activity.LoginActivity;
import com.retailer.oneops.auth.model.MUser;
import com.retailer.oneops.bankDetail.activity.AddBankDetailActivity;
import com.retailer.oneops.bankDetail.activity.BankDetailActivity;
import com.retailer.oneops.dashboard.activity.EditProfileActivity;
import com.retailer.oneops.dashboard.activity.MyProfileActivity;
import com.retailer.oneops.databinding.MoreFragmentLayoutBinding;
import com.retailer.oneops.databinding.MyInventoryFragmentBinding;
import com.retailer.oneops.product.AddProductActivity;
import com.retailer.oneops.util.DialogUtil;
import com.retailer.oneops.util.OnDialogItemClickListener;
import com.retailer.oneops.util.Session;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import static android.app.Activity.RESULT_OK;
import static com.retailer.oneops.util.Constant.IS_LOGIN;
import static com.retailer.oneops.util.Constant.NO;

public class MyInventoryFragment extends Fragment {
    private Activity activity;
    private MyInventoryFragmentBinding binding;
    private MUser loggedInUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        activity = getActivity();
        binding = DataBindingUtil.inflate(inflater, R.layout.my_inventory_fragment, container, false);
        View view = binding.getRoot();
        loggedInUser = new Session(activity).getUserProfile();
        initialization();
        listener();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        listener();
    }

    private void listener() {
       /* binding.tvEdit.setOnClickListener(v -> startActivityForResult(new Intent(getActivity(), EditProfileActivity.class), OPEN_EDIT_PROFILE));
        binding.tvAddToInventory.setOnClickListener(v -> startActivity(new Intent(getActivity(), AddProductActivity.class)));
        binding.rlProfile.setOnClickListener(v -> startActivity(new Intent(getActivity(), MyProfileActivity.class)));

        if (new Session(activity).getUserProfile().getBankDetails() != null) {
            binding.tvBankDetails.setOnClickListener(v -> startActivity(new Intent(getActivity(), BankDetailActivity.class)));
        } else {
            binding.tvBankDetails.setOnClickListener(v -> startActivity(new Intent(getActivity(), AddBankDetailActivity.class)));
        }
*/
      binding.llVirtualInventory.setOnClickListener(v -> virtualInventoryUI());
      binding.llPhysicalInventory.setOnClickListener(v -> physicalInventoryUI());
    }

    private void initialization() {
    }

    public void virtualInventoryUI(){
        binding.llVirtualInventory.setBackground(getResources().getDrawable(R.drawable.curve_back_right_flat_blue));
        binding.tvVirtualInventory.setTextColor(getResources().getColor(R.color.white));
        binding.llPhysicalInventory.setBackground(getResources().getDrawable(R.drawable.curve_back_left_flat_grey));
        binding.tvPhysicalInventory.setTextColor(getResources().getColor(R.color.black));
    }

    public void physicalInventoryUI(){
        binding.llVirtualInventory.setBackground(getResources().getDrawable(R.drawable.curve_back_right_flat_grey));
        binding.tvVirtualInventory.setTextColor(getResources().getColor(R.color.black));
        binding.llPhysicalInventory.setBackground(getResources().getDrawable(R.drawable.curve_back_left_flat_blue));
        binding.tvPhysicalInventory.setTextColor(getResources().getColor(R.color.white));
    }
}

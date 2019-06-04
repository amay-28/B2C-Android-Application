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
import com.retailer.oneops.product.AddProductActivity;
import com.retailer.oneops.settings.SettingActivity;
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

public class MoreFragment extends Fragment implements OnDialogItemClickListener {
    private Activity activity;
    private MoreFragmentLayoutBinding binding;
    private MUser loggedInUser;
    private int OPEN_EDIT_PROFILE = 100;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        activity = getActivity();
        binding = DataBindingUtil.inflate(inflater, R.layout.more_fragment_layout, container, false);
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
        binding.tvEdit.setOnClickListener(v -> startActivityForResult(new Intent(getActivity(), EditProfileActivity.class), OPEN_EDIT_PROFILE));
        binding.tvAddToInventory.setOnClickListener(v -> startActivity(new Intent(getActivity(), AddProductActivity.class)));
        binding.rlProfile.setOnClickListener(v -> startActivity(new Intent(getActivity(), MyProfileActivity.class)));
        binding.tvSettings.setOnClickListener(v -> startActivity(new Intent(getActivity(), SettingActivity.class)));

        if (new Session(activity).getUserProfile().getBankDetails() != null) {
            binding.tvBankDetails.setOnClickListener(v -> startActivity(new Intent(getActivity(), BankDetailActivity.class)));
        } else {
            binding.tvBankDetails.setOnClickListener(v -> startActivity(new Intent(getActivity(), AddBankDetailActivity.class)));
        }

       // binding.tvLogout.setOnClickListener(v -> performLogout());
    }

    private void initialization() {
        setExistingData();
    }

    private void setExistingData() {
        binding.tvName.setText(loggedInUser.getName());
        binding.tvPhone.setText(loggedInUser.getMobileNumber());
        if (loggedInUser != null && loggedInUser.getRetailerDetails() != null)
            Glide.with(activity)
                    .load(loggedInUser.getRetailerDetails().getImageUrl())
                    .apply(new RequestOptions()
                            .placeholder(R.mipmap.avtar_gray)
                            .error(R.mipmap.avtar_gray))
                    .into(binding.imgUserProfile);

    }

    private void performLogout() {
        DialogUtil.showOkCancelDialog(activity, getString(R.string.logout_popup), this);
           }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OPEN_EDIT_PROFILE && resultCode == RESULT_OK) {
            loggedInUser = new Session(activity).getUserProfile();
            setExistingData();
        }
    }

    @Override
    public void onDialogButtonClick(int i, int position) {
        Session session = new Session(activity);
        session.setUserProfile(null);
        session.setString(IS_LOGIN, NO);

        Intent in = new Intent(activity, LoginActivity.class);
        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        (activity).startActivity(in);
    }
}

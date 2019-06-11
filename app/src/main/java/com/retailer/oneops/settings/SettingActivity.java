package com.retailer.oneops.settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.retailer.oneops.R;
import com.retailer.oneops.agent.AgentListActivity;
import com.retailer.oneops.auth.activity.LoginActivity;
import com.retailer.oneops.databinding.ActivitySettingsBinding;
import com.retailer.oneops.product.AddProductActivity;
import com.retailer.oneops.service.activity.AddServiceActivity;
import com.retailer.oneops.util.CommonClickHandler;
import com.retailer.oneops.util.Constant;
import com.retailer.oneops.util.DialogUtil;
import com.retailer.oneops.util.OnDialogItemClickListener;
import com.retailer.oneops.util.Session;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import static com.retailer.oneops.util.Constant.IS_LOGIN;
import static com.retailer.oneops.util.Constant.NO;

public class SettingActivity extends AppCompatActivity implements OnDialogItemClickListener {
    private Activity mContext;
    private ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        //     setContentView(R.layout.activity_settings);
        mContext = this;
        binding = DataBindingUtil.setContentView(mContext, R.layout.activity_settings);
        initialization();
        listeners();
    }

    private void listeners() {
        binding.rlAddservice.setOnClickListener(v -> {
            startActivity(new Intent(mContext, AddServiceActivity.class));
        });

        binding.rlAddproducts.setOnClickListener(v -> startActivity(new Intent(mContext, AddProductActivity.class).putExtra(Constant.IS_SETTING,true)));

        /*binding.rlLogout.setOnClickListener(v -> {
            DialogUtil.showOkCancelDialog(mContext, getString(R.string.logout_popup), null);
        });*/

        binding.rlAgents.setOnClickListener ( v -> {
            startActivity ( new Intent ( mContext, AgentListActivity.class ) );
        } );

        binding.tvLogout.setOnClickListener(v -> performLogout());

        binding.rangeBar.setOnRangeSeekbarChangeListener((minValue, maxValue) -> binding.tvRangeKm.setText(String.valueOf(maxValue)));
    }

    private void initialization() {
        binding.header.setHandler(new CommonClickHandler(mContext));
        binding.header.tvMainHeading.setText(R.string.settings);
    }

    private void performLogout() {
        DialogUtil.showOkCancelDialog(mContext, getString(R.string.logout_popup), this);
    }

    @Override
    public void onDialogButtonClick(int i, int position) {
        Session session = new Session(mContext);
        session.setUserProfile(null);
        session.setString(IS_LOGIN, NO);

        Intent in = new Intent(mContext, LoginActivity.class);
        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        (mContext).startActivity(in);
    }
}


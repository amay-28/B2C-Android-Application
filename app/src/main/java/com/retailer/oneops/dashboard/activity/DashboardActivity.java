package com.retailer.oneops.dashboard.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.retailer.oneops.R;
import com.retailer.oneops.dashboard.fragment.HomeFragment;
import com.retailer.oneops.dashboard.fragment.MoreFragment;
import com.retailer.oneops.databinding.ActivityDashboardBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class DashboardActivity extends AppCompatActivity {
    private Activity activity;
    private TextView mTextMessage;
    private ActivityDashboardBinding binding;
    private Fragment fragment = null;
    private Class fragmentClass = null;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = item -> {
        switch (item.getItemId()) {
            case R.id.navMyInventory:
                //                    mTextMessage.setText(R.string.title_home);
                return true;
            case R.id.navHome:
                //                    mTextMessage.setText(R.string.title_dashboard);
                fragment = new HomeFragment();
                switchFragment(fragment, fragmentClass);
                return true;
            case R.id.navMore:
                //                    mTextMessage.setText(R.string.title_notifications);
                fragment = new MoreFragment();
                switchFragment(fragment, fragmentClass);
                return true;
        }
        return false;
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_dashboard);
        initialization();
        listner();
//        new Session(activity).getUserProfile().setRetailerDetails();
//        Toast.makeText(activity, "retailer: "+, Toast.LENGTH_SHORT).show();
    }

    private void initialization() {
        fragment = new HomeFragment();
        switchFragment(fragment, fragmentClass);
    }

    private void listner() {
        binding.navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void switchFragment(Fragment fragment, Class fragmentClass) {
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContentprovider, fragment).commit();
    }

}
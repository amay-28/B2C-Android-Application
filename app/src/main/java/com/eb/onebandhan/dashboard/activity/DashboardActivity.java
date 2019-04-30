package com.eb.onebandhan.dashboard.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.eb.onebandhan.R;
import com.eb.onebandhan.dashboard.fragment.HomeFragment;
import com.eb.onebandhan.databinding.ActivityDashboardBinding;
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
                return true;
            case R.id.navMore:
                //                    mTextMessage.setText(R.string.title_notifications);
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
    }

    private void initialization() {
        fragment = new HomeFragment();
        switchFragment(fragment,fragmentClass);
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
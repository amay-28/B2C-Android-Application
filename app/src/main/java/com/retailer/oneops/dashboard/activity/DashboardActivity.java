package com.retailer.oneops.dashboard.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.retailer.oneops.R;
import com.retailer.oneops.checkout.CheckoutActivity;
import com.retailer.oneops.dashboard.fragment.HomeFragment;
import com.retailer.oneops.dashboard.fragment.MoreFragment;
import com.retailer.oneops.dashboard.fragment.MyInventoryFragment;
import com.retailer.oneops.databinding.ActivityDashboardBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.retailer.oneops.notification.activity.NotificationListingActivity;

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
                fragment = new MyInventoryFragment();
                switchFragment(fragment, fragmentClass);
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
        listener();
    }

    private void initialization() {
        fragment = new HomeFragment();
        switchFragment(fragment, fragmentClass);
    }

    private void listener() {
        binding.navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        binding.header.imgBag.setOnClickListener(v -> startActivity(new Intent(activity, CheckoutActivity.class)));
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

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent = null;
        switch (id) {
            case R.id.imgSearch:
                Toast.makeText(getApplicationContext(), "Search Item Selected", Toast.LENGTH_LONG).show();
                return true;
            case R.id.imgNotification:
                startActivity(new Intent(DashboardActivity.this, NotificationListingActivity.class));
                //Toast.makeText(getApplicationContext(), "Contact Selected", Toast.LENGTH_LONG).show();
                return true;
            case R.id.imgBag:
                startActivity(new Intent(activity, CheckoutActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/
}
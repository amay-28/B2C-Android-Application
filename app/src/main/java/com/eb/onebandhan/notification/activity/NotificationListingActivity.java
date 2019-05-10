package com.eb.onebandhan.notification.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.os.Bundle;

import com.eb.onebandhan.R;
import com.eb.onebandhan.dashboard.adapter.CategoryListAdapter;
import com.eb.onebandhan.databinding.ActivityNotificationListingBinding;
import com.eb.onebandhan.notification.adapter.NotificationListAdapter;
import com.eb.onebandhan.util.CommonClickHandler;

import java.util.ArrayList;
import java.util.List;

public class NotificationListingActivity extends AppCompatActivity implements NotificationListAdapter.CallBack {
    private Activity activity;
    private ActivityNotificationListingBinding binding;
    private List<String> notificationList=new ArrayList<>();
    private NotificationListAdapter notificationListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=this;
        binding= DataBindingUtil.setContentView(activity,R.layout.activity_notification_listing);
        initialization();
        listner();
    }

    private void listner() {
    }

    private void initialization() {
        binding.header.setHandler(new CommonClickHandler(activity));
        binding.header.tvMainHeading.setText(getResources().getString(R.string.notification));
        binding.rvNotification.setLayoutManager(new LinearLayoutManager(activity));
        binding.rvNotification.setHasFixedSize(true);
        binding.rvNotification.setItemAnimator(new DefaultItemAnimator());
        notificationListAdapter = new NotificationListAdapter(activity, notificationList, this);
        binding.rvNotification.setAdapter(notificationListAdapter);
    }
}

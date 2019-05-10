package com.eb.onebandhan.notification.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eb.onebandhan.R;
import com.eb.onebandhan.databinding.ItemNotificationLayoutBinding;

import java.util.List;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.ViewHolder> {
    private Activity activity;
    private List<String> notificationList;
    private CallBack callBack;
    public NotificationListAdapter(Activity activity, List<String> notificationList, CallBack callBack) {
        this.activity=activity;
        this.notificationList=notificationList;
        this.callBack=callBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNotificationLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_notification_layout, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemNotificationLayoutBinding binding;
        public ViewHolder(@NonNull ItemNotificationLayoutBinding itemView) {
            super(itemView.getRoot());
            binding=itemView;
        }
    }

    public interface CallBack {
    }
}

package com.eb.onebandhan.dashboard.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.eb.onebandhan.R;
import com.eb.onebandhan.databinding.ItemSupercategoryLayoutBinding;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class BannerListAdapter extends RecyclerView.Adapter<BannerListAdapter.ViewHolder> {
    private Activity activity;
    private List<String> bannerList;
    private CallBack callBack;

    public BannerListAdapter(Activity activity, List<String> bannerList, CallBack callBack) {
        this.activity=activity;
        this.bannerList=bannerList;
        this.callBack=callBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSupercategoryLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_banner_layout, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return bannerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemSupercategoryLayoutBinding binding;
        public ViewHolder(@NonNull ItemSupercategoryLayoutBinding itemView) {
            super(itemView.getRoot());
            binding=itemView;
        }
    }

    public interface CallBack {
    }
}

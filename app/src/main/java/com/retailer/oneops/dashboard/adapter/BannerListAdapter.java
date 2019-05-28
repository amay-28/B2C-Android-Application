package com.retailer.oneops.dashboard.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.retailer.oneops.R;
import com.retailer.oneops.dashboard.model.MBanner;
import com.retailer.oneops.databinding.ItemBannerLayoutBinding;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class BannerListAdapter extends RecyclerView.Adapter<BannerListAdapter.ViewHolder> {
    private Activity activity;
    private List<MBanner> bannerList;
    private CallBack callBack;

    public BannerListAdapter(Activity activity, List<MBanner> bannerList, CallBack callBack) {
        this.activity=activity;
        this.bannerList=bannerList;
        this.callBack=callBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBannerLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_banner_layout, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MBanner mBanner = bannerList.get(position);
        Glide.with(activity).load(mBanner.getImageUrl()).apply(new RequestOptions().placeholder(R.mipmap.ic_dummy_banner).error(R.mipmap.ic_dummy_banner)).into(holder.binding.imgBanner);
    }

    @Override
    public int getItemCount() {
        return bannerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemBannerLayoutBinding binding;
        public ViewHolder(@NonNull ItemBannerLayoutBinding itemView) {
            super(itemView.getRoot());
            binding=itemView;
        }
    }

    public interface CallBack {
    }
}

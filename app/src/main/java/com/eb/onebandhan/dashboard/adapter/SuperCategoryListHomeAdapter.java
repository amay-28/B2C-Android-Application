package com.eb.onebandhan.dashboard.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.eb.onebandhan.R;
import com.eb.onebandhan.auth.model.MCategory;
import com.eb.onebandhan.databinding.ItemSupercategoryHomeLayoutBinding;
import com.eb.onebandhan.databinding.ItemSupercategoryLayoutBinding;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class SuperCategoryListHomeAdapter extends RecyclerView.Adapter<SuperCategoryListHomeAdapter.ViewHolder> {
    private Activity activity;
    private List<MCategory> superCategoryList;
    private CallBack callBack;

    public SuperCategoryListHomeAdapter(Activity activity, List<MCategory> superCategoryList, CallBack callBack) {
        this.activity = activity;
        this.superCategoryList = superCategoryList;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSupercategoryHomeLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_supercategory_home_layout, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MCategory mCategory = superCategoryList.get(position);
        holder.binding.tvSuperCatName.setText(mCategory.getName());
        Glide.with(activity).load(mCategory.getImage()).apply(new RequestOptions().placeholder(R.color.colorPrimary).error(R.color.colorPrimary)).into(holder.binding.imgSuperCat);
    }

    @Override
    public int getItemCount() {
        return superCategoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemSupercategoryHomeLayoutBinding binding;

        public ViewHolder(@NonNull ItemSupercategoryHomeLayoutBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }

    public interface CallBack {
    }
}

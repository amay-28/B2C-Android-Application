package com.retailer.oneops.dashboard.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.retailer.oneops.R;
import com.retailer.oneops.auth.model.MCategory;
import com.retailer.oneops.databinding.ItemSubcategoryLayoutBinding;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class SubCategoryListAdapter extends RecyclerView.Adapter<SubCategoryListAdapter.ViewHolder> {
    private Activity activity;
    private List<MCategory> superCategoryList;
    private CallBack callBack;

    public SubCategoryListAdapter(Activity activity, List<MCategory> superCategoryList, CallBack callBack) {
        this.activity = activity;
        this.superCategoryList = superCategoryList;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // have to change it tomorrow
        ItemSubcategoryLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_subcategory_layout, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MCategory mCategory = superCategoryList.get(position);
        holder.binding.llRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.onCategoryClick(position, mCategory.getId());
            }
        });
        holder.binding.tvSubCategory.setText(mCategory.getName());
        Glide.with(activity).load(mCategory.getImage()).apply(new RequestOptions().placeholder(R.mipmap.ic_dummy_banner).error(R.mipmap.ic_dummy_banner)).into(holder.binding.imgSubCat);
    }

    @Override
    public int getItemCount() {
        return superCategoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemSubcategoryLayoutBinding binding;

        public ViewHolder(@NonNull ItemSubcategoryLayoutBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }

    public interface CallBack {
        void onCategoryClick(int position, String categoryId);
    }
}
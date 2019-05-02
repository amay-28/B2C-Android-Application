package com.eb.onebandhan.dashboard.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.eb.onebandhan.R;
import com.eb.onebandhan.auth.model.MCategory;
import com.eb.onebandhan.databinding.ItemSupercategoryLayoutBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class SuperCategoryListAdapter extends RecyclerView.Adapter<SuperCategoryListAdapter.ViewHolder> {
    private Activity activity;
    private List<MCategory> superCategoryList;
    private CallBack callBack;
    private ExpandableCategoryAdapter expandableCategoryAdapter;

    public SuperCategoryListAdapter(Activity activity, List<MCategory> superCategoryList, CallBack callBack) {
        this.activity = activity;
        this.superCategoryList = superCategoryList;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSupercategoryLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_supercategory_layout, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MCategory mCategory = superCategoryList.get(position);
        Glide.with(activity).load(mCategory.getImage()).apply(new RequestOptions().placeholder(R.mipmap.ic_dummy_banner).error(R.mipmap.ic_dummy_banner)).into(holder.binding.imgSuperCat);
        holder.binding.imgSuperCat.setOnClickListener(view -> {
            // it represent category
            Map<String, List<MCategory>> categoryMap = new HashMap<>();
            for (MCategory mCategory1 :mCategory.getChildren()){
                if (categoryMap.containsKey(mCategory1.getId())){
                    categoryMap.get(mCategory1.getId()).add(mCategory1);
                }
                else {
                    List<MCategory> mSub = new ArrayList<>();
                    mSub.add(mCategory1);
                    categoryMap.put(mCategory1.getId(),mSub);
                }
            }

             expandableCategoryAdapter = new ExpandableCategoryAdapter(activity, mCategory.getChildren(), categoryMap, new ExpandableCategoryAdapter.CallBack() {
             });
            holder.binding.expandableListView.setAdapter(expandableCategoryAdapter);
        });
    }

    @Override
    public int getItemCount() {
        return superCategoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemSupercategoryLayoutBinding binding;

        public ViewHolder(@NonNull ItemSupercategoryLayoutBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }

    public interface CallBack {
    }
}

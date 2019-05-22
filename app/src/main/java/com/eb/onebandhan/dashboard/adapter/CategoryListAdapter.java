package com.eb.onebandhan.dashboard.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eb.onebandhan.R;
import com.eb.onebandhan.auth.model.MCategory;
import com.eb.onebandhan.databinding.ItemCategoryLayoutBinding;
import com.eb.onebandhan.databinding.ItemSupercategoryLayoutBinding;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.ViewHolder> {
    private Activity activity;
    private List<MCategory> CategoryList;
    private SubCategoryListAdapter.CallBack subCategoryCallback;
    private SubCategoryListAdapter subCategoryListAdapter;


    public CategoryListAdapter(Activity activity, List<MCategory> CategoryList, SubCategoryListAdapter.CallBack callBack) {
        this.activity = activity;
        this.CategoryList = CategoryList;
        this.subCategoryCallback = callBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCategoryLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_category_layout, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MCategory mCategory = CategoryList.get(position);
        holder.binding.tvCategory.setText(mCategory.getName());
        holder.binding.rvSubCategory.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        holder.binding.rvSubCategory.setHasFixedSize(true);
        holder.binding.rvSubCategory.setItemAnimator(new DefaultItemAnimator());
        subCategoryListAdapter = new SubCategoryListAdapter(activity, mCategory.getChildren(), subCategoryCallback);
        holder.binding.rvSubCategory.setAdapter(subCategoryListAdapter);

    }

    @Override
    public int getItemCount() {
        return CategoryList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemCategoryLayoutBinding binding;

        public ViewHolder(@NonNull ItemCategoryLayoutBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}

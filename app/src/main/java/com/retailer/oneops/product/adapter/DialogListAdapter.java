package com.retailer.oneops.product.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.retailer.oneops.R;
import com.retailer.oneops.auth.model.MCategory;
import com.retailer.oneops.dashboard.adapter.SubCategoryListAdapter;
import com.retailer.oneops.databinding.ItemDialogActivityBinding;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class DialogListAdapter extends RecyclerView.Adapter<DialogListAdapter.ViewHolder> implements SubCategoryListAdapter.CallBack {
    private Activity activity;
    private List<MCategory> CategoryList;
    private CallBack callBack;
    private String selectedCategory;

    public DialogListAdapter(Activity activity, List<MCategory> CategoryList, CallBack callBack, String selectedCategory) {
        this.activity = activity;
        this.CategoryList = CategoryList;
        this.callBack = callBack;
        this.selectedCategory = selectedCategory;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDialogActivityBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_dialog_activity, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MCategory mCategory = CategoryList.get(position);
        holder.binding.tvCategory.setText(mCategory.getName());
        if (selectedCategory.equalsIgnoreCase(mCategory.getName())) {
            holder.binding.ivTick.setVisibility(View.VISIBLE);
        }
        holder.binding.rlRoot.setOnClickListener(v -> {
            callBack.onCategoryClick(position, mCategory);
        });
    }

    @Override
    public int getItemCount() {
        return CategoryList.size();
    }

    @Override
    public void onCategoryClick(int position,String categoryId) {

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemDialogActivityBinding binding;

        public ViewHolder(@NonNull ItemDialogActivityBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }

    public interface CallBack {
        void onCategoryClick(int position, MCategory mCategory);
    }
}

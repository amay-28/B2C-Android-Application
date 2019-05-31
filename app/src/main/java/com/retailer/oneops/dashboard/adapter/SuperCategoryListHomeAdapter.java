package com.retailer.oneops.dashboard.adapter;

import android.app.Activity;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.retailer.oneops.R;
import com.retailer.oneops.auth.model.MCategory;
import com.retailer.oneops.databinding.ItemSupercategoryHomeLayoutBinding;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class SuperCategoryListHomeAdapter extends RecyclerView.Adapter<SuperCategoryListHomeAdapter.ViewHolder> {
    private Activity activity;
    private List<MCategory> superCategoryList;
    private CallBack callBack;
    private int selectedPos = 0;

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
        Resources resources = activity.getResources();
        MCategory mCategory = superCategoryList.get(position);
        holder.binding.tvSuperCatName.setText(mCategory.getName());
        holder.binding.ivBorderCircle.setVisibility(position == selectedPos ? View.VISIBLE : View.GONE);
        holder.binding.tvSuperCatName.setTextColor(position == selectedPos ? resources.getColor(R.color.colorPrimary) : resources.getColor(R.color.black));

        if (position == 0)
            holder.binding.imgSuperCat.setImageDrawable(resources.getDrawable(R.drawable.img_all_category));
        else
            Glide.with(activity).load(mCategory.getImage()).apply(new RequestOptions().placeholder(R.color.colorPrimary).error(R.color.colorPrimary)).into(holder.binding.imgSuperCat);

        holder.binding.rlRoot.setOnClickListener(v -> {
            selectedPos = position;
            holder.binding.ivBorderCircle.setVisibility(position == selectedPos ? View.VISIBLE : View.GONE);
            holder.binding.tvSuperCatName.setTextColor(position == selectedPos ? resources.getColor(R.color.colorPrimary) : resources.getColor(R.color.white));
            callBack.onCategoryItemClick(position, mCategory);
            notifyDataSetChanged();
        });
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
        void onCategoryItemClick(int position, MCategory mCategory);
    }
}

package com.eb.onebandhan.dashboard.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.eb.onebandhan.R;
import com.eb.onebandhan.auth.model.MCategory;
import com.eb.onebandhan.databinding.ItemSupercategoryLayoutBinding;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class SuperCategoryListAdapter extends RecyclerView.Adapter<SuperCategoryListAdapter.ViewHolder> {
    private Activity activity;
    private List<MCategory> superCategoryList;
    private CallBack callBack;
    private ExpandableCategoryAdapter expandableCategoryAdapter;
    private Integer selectedPos=null;

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
        holder.binding.tvSuperCategoryName.setText(mCategory.getName());
        Glide.with(activity).load(mCategory.getImage()).apply(new RequestOptions().placeholder(R.mipmap.ic_dummy_banner).error(R.mipmap.ic_dummy_banner)).into(holder.binding.imgSuperCat);
        holder.binding.imgSuperCat.setOnClickListener(view -> {
            selectedPos=position;
            // it represent category
             expandableCategoryAdapter = new ExpandableCategoryAdapter(activity, mCategory.getChildren(), new ExpandableCategoryAdapter.CallBack() {
             });
            holder.binding.expandableListView.setAdapter(expandableCategoryAdapter);
            notifyDataSetChanged();
        });
        // for setting height at runtime
        if (selectedPos!=null){
            ViewGroup.LayoutParams params = holder.binding.llCategory.getLayoutParams();
            if (selectedPos==position)params.height= ViewGroup.LayoutParams.MATCH_PARENT;
            else params.height=500;
            holder.binding.llCategory.setLayoutParams(params);
        }

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

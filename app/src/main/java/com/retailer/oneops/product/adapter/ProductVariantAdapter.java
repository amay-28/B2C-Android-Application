package com.retailer.oneops.product.adapter;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.retailer.oneops.R;
import com.retailer.oneops.auth.model.MCategory;
import com.retailer.oneops.dashboard.adapter.SubCategoryListAdapter;
import com.retailer.oneops.databinding.ItemDialogActivityBinding;
import com.retailer.oneops.databinding.ItemProductVariantBinding;
import com.retailer.oneops.productListing.model.MProductVariant;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class ProductVariantAdapter extends RecyclerView.Adapter<ProductVariantAdapter.ViewHolder> {
    private Activity activity;
    private List<MProductVariant> productVariantList;
    private CallBack callBack;
    private Integer selectedPos = null;

    public ProductVariantAdapter(Activity activity, List<MProductVariant> productVariantList, CallBack callBack) {
        this.activity = activity;
        this.productVariantList = productVariantList;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductVariantBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_product_variant, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Resources resources = activity.getResources();
        MProductVariant productVariant = productVariantList.get(position);

        if (productVariant.getAttributes() != null) {
            holder.binding.tvAttribute.setText(productVariant.getAttributes().get(0).getValue().getName());
            if (selectedPos != null) {
                if (selectedPos != position) {
                    holder.binding.rlRoot.setBackground(resources.getDrawable(R.drawable.grey_border_box));
                    holder.binding.tvAttribute.setTextColor(Color.BLACK);
                } else {
                    holder.binding.rlRoot.setBackground(resources.getDrawable(R.drawable.blue_border_box));
                    holder.binding.tvAttribute.setTextColor(Color.WHITE);
                }
            } else {
                holder.binding.rlRoot.setBackground(resources.getDrawable(R.drawable.grey_border_box));
                holder.binding.tvAttribute.setTextColor(Color.BLACK);
            }

        }

        holder.binding.rlRoot.setOnClickListener(v -> {
            selectedPos = position;
            holder.binding.rlRoot.setBackground(position == selectedPos ?
                    resources.getDrawable(R.drawable.blue_border_box) : resources.getDrawable(R.drawable.grey_border_box));
            holder.binding.tvAttribute.setTextColor(position == selectedPos ?
                    resources.getColor(R.color.white) : resources.getColor(R.color.black));
            callBack.onVariantItemClick(position, productVariant.getId());
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return productVariantList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemProductVariantBinding binding;

        public ViewHolder(@NonNull ItemProductVariantBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }

    public interface CallBack {
        void onVariantItemClick(int position, int productVariantId);
    }
}

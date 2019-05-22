package com.eb.onebandhan.productListing.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.eb.onebandhan.R;
import com.eb.onebandhan.auth.model.MCategory;
import com.eb.onebandhan.auth.model.MProfile;
import com.eb.onebandhan.dashboard.adapter.SubCategoryListAdapter;
import com.eb.onebandhan.databinding.ItemDialogActivityBinding;
import com.eb.onebandhan.databinding.ItemProductListingBinding;
import com.eb.onebandhan.productListing.model.MProduct;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> implements SubCategoryListAdapter.CallBack {
    private Activity activity;
    private List<MProduct> productList;
    private CallBack callBack;

    public ProductListAdapter(Activity activity, List<MProduct> productList) {
        this.activity = activity;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductListingBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_product_listing, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MProduct mProduct = productList.get(position);

        holder.binding.tvProductName.setText(mProduct.getName());
        holder.binding.tvProductDescription.setText(mProduct.getDescription());
        holder.binding.tvPrice.setText(mProduct.getPrice());
        holder.binding.tvSellingPrice.setText(mProduct.getCost_price());

        /*holder.binding.tvCategory.setText(mCategory.getName());
        if (selectedCategory.equalsIgnoreCase(mCategory.getName())) {
            holder.binding.ivTick.setVisibility(View.VISIBLE);
        }
        holder.binding.rlRoot.setOnClickListener(v -> {
            callBack.onCategoryClick(position, mCategory);
        });*/
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    @Override
    public void onCategoryClick(int position,String categoryId) {

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemProductListingBinding binding;

        public ViewHolder(@NonNull ItemProductListingBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }

    public interface CallBack {
        void onCategoryClick(int position, MCategory mCategory);
    }
}

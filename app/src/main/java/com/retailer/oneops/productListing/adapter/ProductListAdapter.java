package com.retailer.oneops.productListing.adapter;

import android.app.Activity;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.retailer.oneops.R;
import com.retailer.oneops.auth.model.MCategory;
import com.retailer.oneops.dashboard.adapter.SubCategoryListAdapter;
import com.retailer.oneops.databinding.ItemProductListingBinding;
import com.retailer.oneops.productListing.model.MProduct;

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

        strikeThroughText(holder.binding.tvMrp);
        holder.binding.tvProductName.setText(mProduct.getName());
        holder.binding.tvProductDescription.setText(mProduct.getDescription());
        holder.binding.tvSellingPrice.setText(mProduct.getPrice());
        holder.binding.tvMrp.setText(mProduct.getCost_price());

        double actualPrice = Double.parseDouble(mProduct.getCost_price());
        double discountedPrice = Double.parseDouble(mProduct.getPrice());
        long discountPercent = calculateProfitPercent(actualPrice, discountedPrice);
        holder.binding.tvDiscountPercent.setText(discountPercent + "% OFF");

        if (mProduct.getImages() != null){
            Glide.with(activity)
                    .load(mProduct.getImages().get(0).getUrl())
                    .into(holder.binding.ivImage);
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    @Override
    public void onCategoryClick(int position, String categoryId) {

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

    private void strikeThroughText(TextView textView) {
        textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    public long calculateProfitPercent(double actualPrice, double discountPrice) {
        double discountPercent = 0;
        if (actualPrice > discountPrice) {
            discountPercent = (actualPrice - discountPrice) / actualPrice * 100;
            discountPercent = Math.round(discountPercent);
            //Log.d("Adapter", "percent: " + Math.round(discountPercent));
        }
        return Math.round(discountPercent);
    }
}

package com.retailer.oneops.dashboard.adapter;

import android.app.Activity;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.retailer.oneops.R;
import com.retailer.oneops.databinding.ItemMyInventoryBinding;
import com.retailer.oneops.productListing.model.MProduct;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class PhysicalInventoryAdapter extends RecyclerView.Adapter<PhysicalInventoryAdapter.ViewHolder> {
    private Activity activity;
    private List<MProduct> productList;
    private CallBack callBack;

    public PhysicalInventoryAdapter(Activity activity, List<MProduct> productList, CallBack callBack) {
        this.activity = activity;
        this.productList = productList;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMyInventoryBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_my_inventory, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MProduct mProduct = productList.get(position);

        strikeThroughText(holder.binding.tvSellingPrice);
        holder.binding.tvProductName.setText(mProduct.getName());
        holder.binding.tvProductDescription.setText(mProduct.getDescription());
        holder.binding.tvPrice.setText("Rs. " + mProduct.getPrice());
        holder.binding.tvSellingPrice.setText("Rs. " + mProduct.getCost_price());

        if (mProduct.getCategory() != null
                && mProduct.getCategory().getParent() != null && mProduct.getCategory().getParent().getParent() != null) {
            holder.binding.tvCategory.setText(mProduct.getCategory().getParent().getParent().getName());
        } else if (mProduct.getCategory() != null && mProduct.getCategory().getParent() != null) {
            holder.binding.tvCategory.setText(mProduct.getCategory().getParent().getName());
        } else if (mProduct.getCategory() != null) {
            holder.binding.tvCategory.setText(mProduct.getCategory().getName());
        } else {
            holder.binding.tvCategory.setText("NA");
        }


        double actualPrice = Double.parseDouble(mProduct.getCost_price());
        double discountedPrice = Double.parseDouble(mProduct.getPrice());
        long discountPercent = calculateProfitPercent(actualPrice, discountedPrice);
        holder.binding.tvDiscountPercent.setText(discountPercent + "% OFF");

        if (mProduct.getImages() != null) {
            Glide.with(activity)
                    .load(mProduct.getImages().get(0).getUrl())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.ic_default_product)
                            .error(R.drawable.ic_default_product))
                    .into(holder.binding.ivProduct);
        }

        holder.binding.ivEdit.setOnClickListener(v -> callBack.onEditPhysicalProduct(position, mProduct));
        holder.binding.ivDelete.setOnClickListener(v -> callBack.onDeletePhysicalProduct(position, mProduct));
        holder.binding.cardViewRoot.setOnClickListener(v -> callBack.onPhysicalItemClick(position, mProduct));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemMyInventoryBinding binding;

        public ViewHolder(@NonNull ItemMyInventoryBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }

    public interface CallBack {
        void onPhysicalItemClick(int position, MProduct mProduct);

        void onEditPhysicalProduct(int position, MProduct mProduct);

        void onDeletePhysicalProduct(int position, MProduct mProduct);
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

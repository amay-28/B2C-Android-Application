package com.retailer.oneops.dashboard.adapter;

import android.app.Activity;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.retailer.oneops.R;
import com.retailer.oneops.databinding.ItemMyInventoryBinding;
import com.retailer.oneops.myinventory.model.MInventory;
import com.retailer.oneops.productListing.model.MProduct;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class VirtualInventoryAdapter extends RecyclerView.Adapter<VirtualInventoryAdapter.ViewHolder> {
    private Activity activity;
    private List<MInventory> productList;
    private CallBack callBack;

    public VirtualInventoryAdapter(Activity activity, List<MInventory> productList, CallBack callBack) {
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
        MInventory mInventory = productList.get(position);

        strikeThroughText(holder.binding.tvSellingPrice);
        holder.binding.tvProductName.setText(mInventory.getProduct().getName());
        holder.binding.tvProductDescription.setText(mInventory.getProduct().getDescription());
        holder.binding.tvPrice.setText("Rs. " + mInventory.getProduct().getPrice());
        holder.binding.tvSellingPrice.setText("Rs. " + mInventory.getProduct().getCost_price());
        holder.binding.tvCategory.setText(mInventory.getProduct().getCategory().getName());

        double actualPrice = Double.parseDouble(mInventory.getProduct().getCost_price());
        double discountedPrice = Double.parseDouble(mInventory.getProduct().getPrice());
        long discountPercent = calculateProfitPercent(actualPrice, discountedPrice);
        holder.binding.tvDiscountPercent.setText(discountPercent + "% OFF");

        if (mInventory.getProduct().getImages() != null && mInventory.getProduct().getImages().size() > 0) {
            Glide.with(activity)
                    .load(mInventory.getProduct().getImages().get(0).getUrl())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.ic_default_product)
                            .error(R.drawable.ic_default_product))
                    .into(holder.binding.ivProduct);
        }

        holder.binding.ivEdit.setVisibility(View.GONE);
        holder.binding.ivDelete.setOnClickListener(v -> callBack.onDeleteVirtualProduct(position, mInventory));
        holder.binding.cardViewRoot.setOnClickListener(v -> callBack.onVirtualItemClick(position, mInventory));
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
        void onVirtualItemClick(int position, MInventory mInventory);

        void onEditVirtualProduct(int position, MInventory mInventory);

        void onDeleteVirtualProduct(int position, MInventory mInventory);
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

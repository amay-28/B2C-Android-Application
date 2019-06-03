package com.retailer.oneops.checkout.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.retailer.oneops.R;
import com.retailer.oneops.checkout.model.MCart;
import com.retailer.oneops.checkout.model.MCartDetail;
import com.retailer.oneops.databinding.ItemMyInventoryBinding;
import com.retailer.oneops.databinding.ItemProductListingBinding;
import com.retailer.oneops.productListing.adapter.ProductListAdapter;
import com.retailer.oneops.productListing.model.MProduct;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.internal.queue.MpscLinkedQueue;

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private List <MCart> productList;
    private Context mContext;
    private Activity activity;
    private ItemMyInventoryBinding binding;
    private CallBack callBack;

    // data is passed into the constructor
    public CheckoutAdapter(Activity activity, List<MCart> productList, CheckoutAdapter.CallBack callBack) {
        this.activity = activity;
        this.productList = productList;
        this.callBack = callBack;
    }

    @androidx.annotation.NonNull
    @Override
    public ViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        ItemMyInventoryBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_my_inventory, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull CheckoutAdapter.ViewHolder holder, int position) {
        MCart mProduct = productList.get(position);
       /* MProduct mProduct = productList.get(position);

        strikeThroughText(holder.binding.tvMrp);
        holder.binding.tvProductName.setText(mProduct.getName());
        holder.binding.tvProductDescription.setText(mProduct.getDescription());
        holder.binding.tvSellingPrice.setText(mProduct.getPrice());
        holder.binding.tvMrp.setText(mProduct.getCost_price());

        double actualPrice = Double.parseDouble(mProduct.getCost_price());
        double discountedPrice = Double.parseDouble(mProduct.getPrice());
        long discountPercent = calculateProfitPercent(actualPrice, discountedPrice);
        holder.binding.tvDiscountPercent.setText(discountPercent + "% OFF");

        if (mProduct.getImages() != null && mProduct.getImages().size() > 0) {
            Glide.with(activity)
                    .load(mProduct.getImages().get(0).getUrl())
                    .into(holder.binding.ivImage);
        }

        holder.binding.tvAddToInventory.setOnClickListener(v -> callBack.onAddToInventoryClick(position, mProduct));
        holder.binding.cardViewRoot.setOnClickListener(v -> callBack.onProductItemClick(position, mProduct));*/
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
        void onProductItemClick(int position, MCartDetail mCartDetail);
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
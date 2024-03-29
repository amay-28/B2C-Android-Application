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
import com.bumptech.glide.request.RequestOptions;
import com.retailer.oneops.R;
import com.retailer.oneops.checkout.model.MCart;
import com.retailer.oneops.checkout.model.MCartDetail;
import com.retailer.oneops.databinding.ItemMyCartBinding;
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
    private List<MCart> cartList;
    private Context mContext;
    private Activity activity;
    private ItemMyCartBinding binding;
    private CallBack callBack;

    // data is passed into the constructor
    public CheckoutAdapter(Activity activity, List<MCart> cartList, CheckoutAdapter.CallBack callBack) {
        this.activity = activity;
        this.cartList = cartList;
        this.callBack = callBack;
    }

    @androidx.annotation.NonNull
    @Override
    public ViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        ItemMyCartBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_my_cart, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull CheckoutAdapter.ViewHolder holder, int position) {
        MCart mCart = cartList.get(position);
        MProduct mProduct = mCart.getProduct();

        strikeThroughText(holder.binding.tvSellingPrice);
        holder.binding.tvProductName.setText(mProduct.getName());
        holder.binding.tvProductDescription.setText(mProduct.getDescription());
        holder.binding.tvPrice.setText("Rs. " +mProduct.getPrice());
        holder.binding.tvSellingPrice.setText("Rs. " +mProduct.getCost_price());
        holder.binding.tvQty.setText("" + mCart.getQuantity());
        if (mCart.getProduct_variant() != null && mCart.getProduct_variant().getAttributes() != null) {
            holder.binding.tvAttributeHead.setText(mCart.getProduct_variant().getAttributes().get(0).getName() + ":");
            holder.binding.tvAttribute.setText(mCart.getProduct_variant().getAttributes().get(0).getValue().getName());
        }

        double actualPrice = Double.parseDouble(mProduct.getCost_price());
        double discountedPrice = Double.parseDouble(mProduct.getPrice());
        long discountPercent = calculateProfitPercent(actualPrice, discountedPrice);
        holder.binding.tvDiscountPercent.setText(discountPercent + "% OFF");

        if (mProduct.getImages() != null && mProduct.getImages().size() > 0) {
                Glide.with(activity)
                    .load(mProduct.getImages().get(0).getUrl())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.ic_default_product)
                            .error(R.drawable.ic_default_product))
                    .into(holder.binding.ivProduct);
        }

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

        holder.binding.cardViewRoot.setOnClickListener(v -> callBack.onProductItemClick(position, mProduct));
        holder.binding.ivDelete.setOnClickListener(v -> callBack.onDeleteItem(position, mCart.getId()));
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemMyCartBinding binding;

        public ViewHolder(@NonNull ItemMyCartBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }

    public interface CallBack {
        void onProductItemClick(int position, MProduct mProduct);

        void onDeleteItem(int position, int cartId);
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
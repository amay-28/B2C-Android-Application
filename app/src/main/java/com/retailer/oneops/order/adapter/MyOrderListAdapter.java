package com.retailer.oneops.order.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.retailer.oneops.R;
import com.retailer.oneops.databinding.ItemMyOrderBinding;
import com.retailer.oneops.order.activity.OrderDetailActivity;
import com.retailer.oneops.order.model.MOrders;
import com.retailer.oneops.settings.model.MServiceResponse;
import com.retailer.oneops.util.Constant;

import java.util.ArrayList;

public class MyOrderListAdapter extends RecyclerView.Adapter<MyOrderListAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<MOrders> mOrderList;

    public MyOrderListAdapter(Context mContext, ArrayList<MOrders> mOrderList) {
        this.mContext = mContext;
        this.mOrderList = mOrderList;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemMyOrderBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.getBinding(itemView);
            binding.rlMyorder.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemMyOrderBinding ItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_my_order, parent, false);
        View view = ItemBinding.getRoot();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        try {
            MOrders mOrders = mOrderList.get(position);
            strikeThroughText(holder.binding.tvSellingPrice);
            holder.binding.tvOrderNumber.setText(mOrders.getId() + "");
            //if(mOrders.getOrder_lines()!=null&&!mOrders.getOrder_lines().isEmpty()){
            holder.binding.tvProductName.setText(mOrders.getOrder_lines().get(0).getProduct().getName());
            holder.binding.tvProductDescription.setText(mOrders.getOrder_lines().get(0).getProduct().getDescription());

            holder.binding.tvPrice.setText("Rs " + mOrders.getOrder_lines().get(0).getProduct().getPrice());
            holder.binding.tvQuantity.setText("" + mOrders.getOrder_lines().get(0).getQuantity());
            holder.binding.tvSellingPrice.setText("Rs" + mOrders.getOrder_lines().get(0).getProduct().getCost_price());


            double actualPrice = Double.parseDouble(mOrders.getOrder_lines().get(0).getProduct().getCost_price());
            double discountedPrice = Double.parseDouble(mOrders.getOrder_lines().get(0).getProduct().getPrice());
            long discountPercent = calculateProfitPercent(actualPrice, discountedPrice);
            if (discountPercent > 0) {
                holder.binding.tvSellingPrice.setVisibility(View.VISIBLE);
                holder.binding.tvDiscountPercent.setVisibility(View.VISIBLE);
            } else {
                holder.binding.tvSellingPrice.setVisibility(View.GONE);
                holder.binding.tvDiscountPercent.setVisibility(View.GONE);
            }

            if (mOrders.getOrder_type() != null && mOrders.getOrder_type().equalsIgnoreCase(Constant.RETAILER_TO_CUSTOMER_PHYSICAL_INVENTORY)) {
                holder.binding.tvTag.setText(Constant.PHYSICAL_INVENTORY_);
                holder.binding.tvTag.setVisibility(View.VISIBLE);

            } else {
                holder.binding.tvTag.setVisibility(View.GONE);
            }
            holder.binding.tvDiscountPercent.setText(discountPercent + "% OFF");


            if (mOrders.getOrder_lines().get(0).getProduct().getImages() != null &&
                    mOrders.getOrder_lines().get(0).getProduct().getImages().size() > 0) {
                Glide.with(mContext)
                        .load(mOrders.getOrder_lines().get(0).getProduct().getImages().get(0).getUrl())
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.ic_default_product)
                                .error(R.drawable.ic_default_product))
                        .into(holder.binding.ivProduct);
            }

            setOrderStatus(holder.binding.tvOrderStatusHead, mOrders);

            holder.binding.tvOrderDetail.setOnClickListener(v ->
                    mContext.startActivity(OrderDetailActivity.getIntent(mContext, mOrders)));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setOrderStatus(TextView tvOrderStatusHead, MOrders mOrders) {
        String orderstatus = null;

        String orderStatus = mOrders.getStatus().replace("_", " ").toLowerCase();
        orderstatus = mContext.getString(R.string.order_status) + " " + orderStatus.substring(0, 1).toUpperCase() + orderStatus.substring(1);


        SpannableString styledText = new SpannableString(orderstatus);
        styledText.setSpan(new TextAppearanceSpan(mContext, R.style.CategorySpannableString1), 0, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        styledText.setSpan(new TextAppearanceSpan(mContext, R.style.CategorySpannableString2), 13, orderstatus.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvOrderStatusHead.setText(styledText);

    }

    @Override
    public int getItemCount() {
        return mOrderList.size();
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

    private void strikeThroughText(TextView textView) {
        textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

}

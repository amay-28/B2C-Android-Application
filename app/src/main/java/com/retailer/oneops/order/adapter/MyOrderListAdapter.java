package com.retailer.oneops.order.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.retailer.oneops.R;
import com.retailer.oneops.databinding.ItemMyOrderBinding;
import com.retailer.oneops.order.model.MOrders;

import java.util.ArrayList;

public class MyOrderListAdapter extends RecyclerView.Adapter<MyOrderListAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<MOrders> mOrderList;
    // private ApiInterface apiInterface;

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
            holder.binding.tvOrderNumber.setText(mOrders.getId() + "");
            //  holder.binding.tvOrderDetail.setOnClickListener(v -> mContext.startActivity(new Intent(mContext, OrderDetailActivity.class)));
            //holder.binding.tvProductName.setText(mOrders.get);
            // holder.binding.tvOrderDetail.setOnClickListener(v -> mContext.startActivity(new Intent(mContext, OrderDetailActivity.class)));
            holder.binding.tvProductName.setText(mOrders.getOrder_lines().get(0).getProduct().getName());
            holder.binding.tvProductDescription.setText(mOrders.getOrder_lines().get(0).getProduct().getDescription());
            /*holder.binding.tvCategory.setText(mOrders.getOrder_lines().get(0).getProduct().getCategory());*/
            holder.binding.tvPrice.setText("Rs " + mOrders.getOrder_lines().get(0).getProduct().getCost_price());
            holder.binding.tvCategory.setText(mOrders.getOrder_lines().get(0).getProduct().getCategory().getName());
            if (mOrders.getStatus().equalsIgnoreCase("WAITING_FOR_DISPATCH"))
                holder.binding.tvOrderStatus.setText("Ready To Ship");
            else {
                String orderStatus = mOrders.getStatus().replace("_", " ").toLowerCase();
                String upperString = orderStatus.substring(0, 1).toUpperCase() + orderStatus.substring(1);
                holder.binding.tvOrderStatus.setText(upperString);
            }

            if (mOrders.getOrder_lines().get(position).getProduct().getImages() != null &&
                    mOrders.getOrder_lines().get(position).getProduct().getImages().size() > 0) {
                Glide.with(mContext)
                        .load(mOrders.getOrder_lines().get(position).getProduct().getImages().get(0).getUrl())
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.ic_default_product)
                                .error(R.drawable.ic_default_product))
                        .into(holder.binding.ivProduct);
            }


            //holder.binding.tvSellingPrice.setText(mOrders.getOrder_lines().get(0).getProduct().getPrice());
            /*holder.binding.tvDiscountPercent.setText(mOrders.getOrder_lines().get(0).getProduct().g);*/
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return mOrderList.size();
    }
}

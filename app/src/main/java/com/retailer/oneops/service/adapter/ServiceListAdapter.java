package com.retailer.oneops.service.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Context;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.retailer.oneops.R;
import com.retailer.oneops.databinding.ItemServiceBinding;
import com.retailer.oneops.settings.model.MServiceResponse;

import java.util.ArrayList;
import java.util.Locale;

public class ServiceListAdapter extends RecyclerView.Adapter<ServiceListAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<MServiceResponse> mServiceList;
    // private ApiInterface apiInterface;

    public ServiceListAdapter(Context mContext, ArrayList<MServiceResponse> mServiceList) {
        this.mContext = mContext;
        this.mServiceList = mServiceList;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemServiceBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.getBinding(itemView);
            binding.rlService.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //  View view = LayoutInflater.from(parent.getmContext()).inflate(R.layout.item_event, parent, false);

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemServiceBinding ItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_service, parent, false);
        View view = ItemBinding.getRoot();
        return new ViewHolder(view);

    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        MServiceResponse mServiceResponse = mServiceList.get(position);
        holder.binding.tvServiceName.setText(mServiceResponse.getService_name());
        holder.binding.tvDescription.setText(mServiceResponse.getService_description());
        holder.binding.tvPrice.setText("Rs."+mServiceResponse.getService_price()+"/hr");
        holder.binding.tvCategory.setText(mServiceResponse.getCategory().getName());
        if (mServiceResponse.getImages() != null && mServiceResponse.getImages().size() > 0) {
            Glide.with(mContext)
                    .load(mServiceResponse.getImages().get(0).getUrl())
                    .into(holder.binding.ivServiceImage);
        }
        setCategory(holder.binding.tvCategory,mServiceResponse);
    }

    private void setCategory(TextView tvCategory, MServiceResponse mServiceResponse) {
       /* private void setTextFan(TextView view, String value, int length) {*/

            if (mServiceResponse.getCategory().getName() != null && !mServiceResponse.getCategory().getName().isEmpty()) {
                String values = mContext.getString(R.string.label_category) + " " + mServiceResponse.getCategory().getName();
                SpannableString styledText = new SpannableString(values);
                styledText.setSpan(new TextAppearanceSpan(mContext, R.style.CategorySpanableString), 0, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                styledText.setSpan(new TextAppearanceSpan(mContext, R.style.CategorySpanableString1), 9, values.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvCategory.setText(styledText);
            } else {
                String values = mContext.getString(R.string.label_category);
                SpannableString styledText = new SpannableString(values);
                styledText.setSpan(new TextAppearanceSpan(mContext, R.style.CategorySpanableString), 0, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvCategory.setText(styledText);
            }
    }


    @Override
    public int getItemCount() {
        return mServiceList.size();
        // return 2;
    }

}

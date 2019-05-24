package com.eb.onebandhan.product.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.bumptech.glide.Glide;
import com.eb.onebandhan.R;
import com.eb.onebandhan.databinding.ItemImageBinding;
import com.eb.onebandhan.product.model.MImage;
import com.eb.onebandhan.util.ImageUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Sumit Yadav on 2/11/18.
 */

public class ShowImagesAdapter extends RecyclerView.Adapter<ShowImagesAdapter.ViewHolder> {
    Activity activity;
    private int selectedPos = 0;
    private int maxValue = 3;
    private int maxType;
    List<MImage> AL;
    private ImageAdapter.CallBack callBack;

    public ShowImagesAdapter(Activity activity, List<MImage> imageList,
                             ImageAdapter.CallBack callBack) {
        this.activity = activity;
        this.AL = imageList;
        this.selectedPos = selectedPos;
        this.maxType = maxValue;
        this.callBack = callBack;
    }

    public void setDataChange(ArrayList<MImage> arraylist) {
        this.AL = arraylist;
        notifyDataSetChanged();
    }

    @Override
    public ShowImagesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemImageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_image, parent, false);
        View view = binding.getRoot();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ShowImagesAdapter.ViewHolder holder, final int position) {

        if (AL.get(position).getFile() != null || (AL.get(position).getUrl() != null && !AL.get(position).getUrl().isEmpty())) {

            if (AL.get(position).getFile() != null)
                //holder.binding.ivImage.setImageBitmap(AL.get(position).getBitmap());
                Glide.with(activity)
                        .load(AL.get(position).getUrl())
                        .into(holder.binding.ivImage);

            if (AL.get(position).getUrl() != null && !AL.get(position).getUrl().isEmpty()) {
                ImageUtils.setImage(activity, AL.get(position).getUrl(), holder.binding.ivImage);
            }
            holder.binding.ivDelete.setVisibility(View.VISIBLE);
        } else {
            holder.binding.ivImage.setImageResource(R.drawable.bg_add_image);
            holder.binding.ivDelete.setVisibility(View.GONE);
        }


        holder.binding.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AL.get(position).getFile() == null) {
                    //((AddProductActivity) mContext).checkPermission(position);
                    callBack.onAddImageClick(position);
                }
            }
        });

        holder.binding.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.onDeleteImage(position);
            }

        });
    }

    @Override
    public int getItemCount() {
        return AL.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemImageBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.getBinding(itemView);
        }
    }

    public interface CallBack {
        void onDeleteImage(int position);

        void onAddImageClick(int position);
    }
}
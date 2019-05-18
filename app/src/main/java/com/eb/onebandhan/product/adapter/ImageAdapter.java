package com.eb.onebandhan.product.adapter;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.eb.onebandhan.R;
import com.eb.onebandhan.auth.model.MCategory;
import com.eb.onebandhan.dashboard.adapter.SubCategoryListAdapter;
import com.eb.onebandhan.databinding.ItemDialogActivityBinding;
import com.eb.onebandhan.databinding.ItemImageBinding;
import com.eb.onebandhan.product.model.MImage;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> implements SubCategoryListAdapter.CallBack {
    private Activity activity;
    private List<MImage> imageList;
    private CallBack callBack;
    private String selectedCategory;

    public ImageAdapter(Activity activity, List<MImage> imageList, CallBack callBack) {
        this.activity = activity;
        this.imageList = imageList;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemImageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_image, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MImage mImage = imageList.get(position);
        if (mImage.isLocal()) {
            holder.binding.ivImage.setImageDrawable(activity.getDrawable(R.drawable.bg_add_image));
            holder.binding.ivDelete.setVisibility(View.GONE);
            holder.binding.ivImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack.onAddImageClick(position);
                }
            });
        } else {
            holder.binding.ivDelete.setVisibility(View.VISIBLE);
            holder.binding.ivImage.setClickable(false);
            Glide.with(activity)
                    .load(mImage.getUrl())
                    .into(holder.binding.ivImage);

            holder.binding.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack.onDeleteImage(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemImageBinding binding;

        public ViewHolder(@NonNull ItemImageBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }

    public interface CallBack {
        void onDeleteImage(int position);

        void onAddImageClick(int position);
    }
}

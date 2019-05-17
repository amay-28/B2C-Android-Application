package com.eb.onebandhan.product.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eb.onebandhan.R;
import com.eb.onebandhan.auth.model.MCategory;
import com.eb.onebandhan.dashboard.adapter.SubCategoryListAdapter;
import com.eb.onebandhan.databinding.ItemDialogActivityBinding;
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
        ItemDialogActivityBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_dialog_activity, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MImage mImage = imageList.get(position);
        if (mImage.isLocal()) {
            //TODO
        }
        //  String imageUrl = imageList.get(position);
        //holder.binding.tvCategory.setText(mCategory.getName());
       /* if (selectedCategory.equalsIgnoreCase(mCategory.getName())) {
            holder.binding.ivTick.setVisibility(View.VISIBLE);
        }*/


    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemDialogActivityBinding binding;

        public ViewHolder(@NonNull ItemDialogActivityBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }

    public interface CallBack {
        void onDeleteImage(int position);
    }
}

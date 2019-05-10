package com.eb.onebandhan.dashboard.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.eb.onebandhan.R;
import com.eb.onebandhan.dashboard.model.MCollection;
import com.eb.onebandhan.databinding.ItemCollectionLayoutBinding;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class CollectionListAdapter extends RecyclerView.Adapter<CollectionListAdapter.ViewHolder> {
    private Activity activity;
    private List<MCollection> collectionList;
    private CallBack callBack;

    public CollectionListAdapter(Activity activity, List<MCollection> collectionList, CallBack callBack) {
        this.activity=activity;
        this.collectionList =collectionList;
        this.callBack=callBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCollectionLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_collection_layout, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
      MCollection mCollection=  collectionList.get(position);
        Glide.with(activity).load(mCollection.getImageUrl()).apply(new RequestOptions().placeholder(R.mipmap.ic_dummy_banner).error(R.mipmap.ic_dummy_banner)).into(holder.binding.imgCollection);

    }

    @Override
    public int getItemCount() {
        return collectionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemCollectionLayoutBinding binding;
        public ViewHolder(@NonNull ItemCollectionLayoutBinding itemView) {
            super(itemView.getRoot());
            binding=itemView;
        }
    }

    public interface CallBack {
    }
}

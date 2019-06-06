package com.retailer.oneops.agent.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.retailer.oneops.agent.model.MAgentResponse;
import com.retailer.oneops.databinding.ItemAgentBinding;
import com.retailer.oneops.databinding.ItemServiceBinding;
import com.retailer.oneops.service.adapter.ServiceListAdapter;
import com.retailer.oneops.R;


import java.util.ArrayList;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class AgentListAdapter extends RecyclerView.Adapter <AgentListAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList <MAgentResponse> mAgentList;
    private int mFragmentType;
    // private ApiInterface apiInterface;

    public AgentListAdapter(Context mContext, ArrayList <MAgentResponse> mAgentList) {
        this.mContext = mContext;
        this.mAgentList = mAgentList;
        this.mFragmentType = mFragmentType;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemAgentBinding binding;

        public ViewHolder(View itemView) {
            super ( itemView );
            binding = DataBindingUtil.getBinding ( itemView );
            binding.rlAgent.setVisibility ( View.VISIBLE );
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //  View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);

        LayoutInflater layoutInflater = LayoutInflater.from ( parent.getContext () );
        ItemAgentBinding ItemBinding = DataBindingUtil.inflate ( layoutInflater, R.layout.item_agent, parent, false );
        View view = ItemBinding.getRoot ();
        return new ViewHolder ( view );


    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        MAgentResponse mAgent = mAgentList.get ( position );
        holder.binding.tvAgentName.setText ( mAgent.getName () );
        holder.binding.tvAgentMobileNo.setText ( mAgent.getMobileNumber () );

        Glide.with ( mContext )
                .load ( mAgent.getImageUrl () )
                .into ( holder.binding.ivAgentImage );


    }


    @Override
    public int getItemCount() {
        return mAgentList.size ();
        // return 2;
    }
}

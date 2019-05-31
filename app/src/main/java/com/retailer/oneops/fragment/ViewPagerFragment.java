package com.retailer.oneops.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.retailer.oneops.R;
import com.retailer.oneops.databinding.FragmentViewPagerBinding;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

public class ViewPagerFragment extends Fragment {
    private FragmentViewPagerBinding binding;
    private Context mContext;
    private String mImageUrl;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mImageUrl = bundle.getString("imageUrl");
        }

    }

    public static Fragment newInstance(String imageUrl) {

        Bundle args = new Bundle();
        args.putSerializable("imageUrl", imageUrl);

        ViewPagerFragment fragment = new ViewPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getActivity();
        initializeView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_view_pager, container, false);
        View view = binding.getRoot();
        mContext = getActivity();
        return view;
    }

    private void initializeView() {
        Glide.with(getActivity())
                .load(mImageUrl)
                .into(binding.ivImage);
    }
}

package com.eb.onebandhan.dashboard.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eb.onebandhan.R;
import com.eb.onebandhan.databinding.MoreFragmentLayoutBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

public class MoreFragment extends Fragment {
    private Activity activity;
    private MoreFragmentLayoutBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        activity = getActivity();
        binding = DataBindingUtil.inflate(inflater, R.layout.more_fragment_layout, container, false);
        View view = binding.getRoot();
        initialization();
        listner();
        return view;
    }

    private void listner() {

    }

    private void initialization() {

    }
}

package com.eb.onebandhan.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eb.onebandhan.R;


public class CustomSpinnerAdapter extends BaseAdapter {

    //Class variables
    Context context;
    String[] dataList;
    LayoutInflater inflater;

    //Constructor
    public CustomSpinnerAdapter(Context applicationContext, String[] _dataList) {
        this.context = applicationContext;
        this.dataList = _dataList;
        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return dataList.length;
    }

    @Override
    public String getItem(int i) {
        return String.valueOf(dataList[i]);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    /**
     * Method used to set view of adapter.
     */
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.custom_spinner_item, null);
        TextView names = (TextView) view.findViewById(R.id.textView);
        names.setText(dataList[i]);

        if (i == 0) {
            // Set the hint text color gray
            names.setTextColor(context.getResources().getColor(R.color.colorHint));
        } else {
            names.setTextColor(context.getResources().getColor(R.color.black));
        }

        return view;
    }

    @Override
    public boolean isEnabled(int position) {
        if (position == 0) {
            // Disable the first item from Spinner
            // First item will be use for hint
            return false;
        } else {
            return true;
        }

    }
}

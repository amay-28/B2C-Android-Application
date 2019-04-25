package com.eb.onebandhan.auth.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.eb.onebandhan.R;
import com.eb.onebandhan.auth.model.MCategory;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CategoryAdapter extends ArrayAdapter<MCategory> {
    private List<MCategory> categoryList;

    public CategoryAdapter(@NonNull Activity activity, int resource, List<MCategory> categoryList) {
        super(activity, resource,categoryList);
        this.categoryList = categoryList;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);//getLayoutInflater();
        View row = inflater.inflate(R.layout.row_spinner_text, parent, false);
        TextView label = row.findViewById(R.id.txt);
        label.setText(categoryList.get(position).getName());
        return row;
    }
}

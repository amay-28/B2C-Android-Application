package com.eb.onebandhan.dashboard.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.eb.onebandhan.R;
import com.eb.onebandhan.auth.model.MCategory;
import com.eb.onebandhan.databinding.CategoryExpandableItemBinding;
import com.eb.onebandhan.databinding.SubcategoryChildItemBinding;
import com.eb.onebandhan.productListing.ProductListingActivity;

import java.util.List;

import androidx.databinding.DataBindingUtil;

public class ExpandableCategoryAdapter extends BaseExpandableListAdapter {
    private Activity activity;
    private List<MCategory> categoryList;
    private CallBack callBack;
    private CategoryExpandableItemBinding bindingCategory;
    private SubcategoryChildItemBinding bindingSubCategory;
    private LayoutInflater mLayoutInflater;

    public ExpandableCategoryAdapter(Activity activity, List<MCategory> categoryList, CallBack callBack) {
        this.activity = activity;
        this.categoryList = categoryList;
        this.callBack = callBack;
        mLayoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getGroupCount() {
        return categoryList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return categoryList.get(i).getChildren() == null ? 0 : categoryList.get(i).getChildren().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return categoryList.size();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return categoryList.get(groupPosition).getChildren().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean b, View convertView, ViewGroup viewGroup) {
        MCategory mCategory = categoryList.get(groupPosition);
        if (convertView == null) {
            bindingCategory = DataBindingUtil.inflate(mLayoutInflater, R.layout.category_expandable_item, viewGroup, false);
            convertView = bindingCategory.getRoot();
        }
        Glide.with(activity).load(mCategory.getImage()).apply(new RequestOptions().placeholder(R.drawable.ic_t_shirt).error(R.drawable.ic_t_shirt)).into(bindingCategory.imgCategory);
        bindingCategory.tvCategory.setText(mCategory.getName());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup viewGroup) {
        List<MCategory> subCategories = categoryList.get(groupPosition).getChildren();
        if (subCategories != null) {
            MCategory subCategory = subCategories.get(childPosition);

            if (convertView == null) {
                bindingSubCategory = DataBindingUtil.inflate(mLayoutInflater, R.layout.subcategory_child_item, viewGroup, false);
                convertView = bindingSubCategory.getRoot();
            }
            Glide.with(activity).load(subCategory.getImage()).apply(new RequestOptions().placeholder(R.drawable.ic_t_shirt).error(R.drawable.ic_t_shirt)).into(bindingSubCategory.imgSubCategory);
            bindingSubCategory.tvSubCategory.setText(subCategory.getName());
            bindingSubCategory.llRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.startActivity(new Intent(activity, ProductListingActivity.class));
                }
            });
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    public interface CallBack {
    }
}

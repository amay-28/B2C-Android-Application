package com.eb.onebandhan.dashboard.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.eb.onebandhan.R;
import com.eb.onebandhan.auth.model.MCategory;
import com.eb.onebandhan.databinding.CategoryExpandableItemBinding;
import com.eb.onebandhan.databinding.SubcategoryChildItemBinding;
import java.util.List;
import java.util.Map;
import androidx.databinding.DataBindingUtil;

public class ExpandableCategoryAdapter extends BaseExpandableListAdapter {
    private Activity activity;
    private List<MCategory> categoryList;
    private  Map<String, List<MCategory>> categoryMap;
    private CallBack callBack;
    private CategoryExpandableItemBinding bindingCategory;
    private SubcategoryChildItemBinding bindingSubCategory;
    private  LayoutInflater  mLayoutInflater;

    public ExpandableCategoryAdapter(Activity activity, List<MCategory> categoryList, Map<String, List<MCategory>> categoryMap, CallBack callBack) {
        this.activity=activity;
        this.categoryList=categoryList;
        this.categoryMap=categoryMap;
        this.callBack=callBack;
    }

    @Override
    public int getGroupCount() {
        return categoryList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        List<MCategory> subCategories = categoryMap.get(categoryList.get(i).getId());
        return subCategories == null ? 0 : subCategories.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return categoryList.size();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
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
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.category_expandable_item, viewGroup, false);

               /* bindingSubCategory = DataBindingUtil.inflate(inflater, R.layout.subcategory_child_item, viewGroup, false);
                convertView = bindingSubCategory.getRoot();*/
        }
        ((TextView)convertView.findViewById(R.id.tvCategory)).setText(categoryList.get(groupPosition).getName());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup viewGroup) {
        List<MCategory> subCategories = categoryMap.get(categoryList.get(groupPosition).getId());
        MCategory subCategory = null;


        if (subCategories != null) {
            subCategory = subCategories.get(childPosition);

            if (convertView == null) {

                convertView = mLayoutInflater.inflate(R.layout.subcategory_child_item, viewGroup, false);

               /* bindingSubCategory = DataBindingUtil.inflate(inflater, R.layout.subcategory_child_item, viewGroup, false);
                convertView = bindingSubCategory.getRoot();*/

                 ((TextView)convertView.findViewById(R.id.tvSubCategory)).setText(subCategories.get(childPosition).getName());
            }
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    public interface CallBack {
    }
}

package com.eb.onebandhan.auth.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.eb.onebandhan.R;
import com.eb.onebandhan.auth.adapter.CategoryAdapter;
import com.eb.onebandhan.auth.model.MCategory;
import com.eb.onebandhan.auth.model.MProfile;
import com.eb.onebandhan.auth.model.MUser;
import com.eb.onebandhan.auth.presenter.SignUpDetailPresenter;
import com.eb.onebandhan.auth.util.Categoryutil;
import com.eb.onebandhan.auth.viewinterface.SignUpDetailViewInterface;
import com.eb.onebandhan.databinding.ActivitySignUpDetailBinding;
import com.eb.onebandhan.util.Constant;
import com.eb.onebandhan.util.ShowToast;
import com.eb.onebandhan.util.ValidationUtil;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignUpDetailActivity extends AppCompatActivity implements SignUpDetailViewInterface, Categoryutil, Constant {
    private Activity activity;
    private ActivitySignUpDetailBinding binding;
    private SignUpDetailPresenter signUpDetailPresenter;
    private Map<String, String> map = new HashMap<>();
    private List<MCategory> categoryList=new ArrayList<>();
    private CategoryAdapter categoryAdapter;
    private MProfile mProfile=new MProfile();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_sign_up_detail);
        initialization();
        listner();
    }

    private void initialization() {
        signUpDetailPresenter = new SignUpDetailPresenter(this, activity);
        map.put(LEVEL,ZERO);
        map.put(LIMIT,INFINITE_LIMIT);
        signUpDetailPresenter.loadCategoryTask(map);
        MCategory mCategory=new MCategory();
        mCategory.setId("00");
        mCategory.setName(getResources().getString(R.string.deals_in_category));
        categoryList.add(mCategory);
        categoryAdapter = new CategoryAdapter(activity, R.layout.row_spinner_text, categoryList);
        binding.spnCategory.setAdapter(categoryAdapter);
    }

    private void listner() {
   binding.btnSubmit.setOnClickListener(view -> {
       if (validation())signUpDetailPresenter.submitShopDetailTask(mProfile);
   });
        binding.spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (!categoryList.get(position).getIsSelected()){
                    Chip chip = new Chip(activity);
                    categoryList.get(position).setIsSelected(true);
                    chip.setText(categoryList.get(position).getName());
                    chip.setChipCornerRadius(3f);
                    chip.setTextColor(getResources().getColor(R.color.black));
                    chip.setCloseIconEnabled(true);
                    chip.setId(Integer.parseInt(categoryList.get(position).getId()));
                    chip.setOnCloseIconClickListener(view1 -> {
                        categoryList.get(position).setIsSelected(false);
                        binding.chipGroup.removeView(chip);
                    });
                    binding.chipGroup.addView(chip);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private boolean validation() {
        Resources resources=getResources();
        if (TextUtils.isEmpty(binding.etShopName.getText().toString())){
            Toast.makeText(activity,resources.getString(R.string.please_enter_shop_name) , Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(binding.etAddressOne.getText().toString())){
            Toast.makeText(activity,resources.getString(R.string.please_enter_address) , Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(binding.etAddressTwo.getText().toString())){
            Toast.makeText(activity,resources.getString(R.string.please_enter_address) , Toast.LENGTH_SHORT).show();
            return false;
        }else if (TextUtils.isEmpty(binding.etCity.getText().toString())){
            Toast.makeText(activity,resources.getString(R.string.please_enter_city) , Toast.LENGTH_SHORT).show();
            return false;
        }else if (TextUtils.isEmpty(binding.etState.getText().toString())){
            Toast.makeText(activity,resources.getString(R.string.please_enter_state) , Toast.LENGTH_SHORT).show();
            return false;
        }else if (!TextUtils.isEmpty(binding.etEmail.getText().toString())&& !ValidationUtil.emailValidator(activity,binding.etEmail,"email")){
            return false;
        }
         prepareData();
        return true;
    }

    private void prepareData() {
        // have to add more fields
        mProfile.setShopName(binding.etShopName.getText().toString());
        mProfile.setAddressLine1(binding.etAddressOne.getText().toString());
        mProfile.setAddressLine2(binding.etAddressTwo.getText().toString());
        mProfile.setCity(binding.etCity.getText().toString());
        mProfile.setState(binding.etState.getText().toString());
        mProfile.setEmail(binding.etEmail.getText().toString());
        List<MProfile.MDealsIn> dealsInList=new ArrayList<>();
        if (!categoryList.isEmpty()){
            for (MCategory mCategory : categoryList){
                if (mCategory.getIsSelected()){
                    MProfile.MDealsIn mDealsIn=new MProfile.MDealsIn();
                    mDealsIn.setId(mCategory.getId());
                    dealsInList.add(mDealsIn);
                }
            }
            mProfile.setDealsIn(dealsInList);
        }
    }

    @Override
    public void onFailTogetCategories(String message) {
        ShowToast.toastMsg(activity,message);
    }

    @Override
    public void onSucessfullygetCategories(List<MCategory> categoryList, String message) {
         this.categoryList.addAll(categoryList);
         categoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSucessfullySubmitShopDetail(MUser data, String message) {
        ShowToast.toastMsg(activity,message);
    }

    @Override
    public void onFailToSubmitShopDetail(String message) {
        ShowToast.toastMsg(activity,message);
    }
}

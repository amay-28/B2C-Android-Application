package com.eb.onebandhan.product;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.eb.onebandhan.R;
import com.eb.onebandhan.auth.model.MCategory;
import com.eb.onebandhan.dashboard.adapter.SuperCategoryListAdapter;
import com.eb.onebandhan.databinding.ActivityAddProductBinding;
import com.eb.onebandhan.product.adapter.DialogListAdapter;
import com.eb.onebandhan.product.adapter.ImageAdapter;
import com.eb.onebandhan.product.model.MAddProduct;
import com.eb.onebandhan.product.model.MImage;
import com.eb.onebandhan.product.presenter.AddProductPresenter;
import com.eb.onebandhan.product.presenter.DialogPresenter;
import com.eb.onebandhan.product.viewinterface.AddProductViewInterface;
import com.eb.onebandhan.product.viewinterface.DialogViewInterface;
import com.eb.onebandhan.util.CommonClickHandler;
import com.eb.onebandhan.util.ShowToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import static com.eb.onebandhan.auth.util.Categoryutil.INFINITE_LIMIT;
import static com.eb.onebandhan.auth.util.Categoryutil.LEVEL;
import static com.eb.onebandhan.auth.util.Categoryutil.LIMIT;
import static com.eb.onebandhan.auth.util.Categoryutil.ZERO;

public class AddProductActivity extends AppCompatActivity implements DialogViewInterface, AddProductViewInterface,
        ImageAdapter.CallBack {
    private Activity activity;
    private ActivityAddProductBinding binding;
    private int OPEN_DIALOG_FOR_CATEGORY = 1;
    private int OPEN_DIALOG_FOR_SUBCATEGORY = 2;
    private int OPEN_DIALOG_FOR_SUBSUBCATEGORY = 3;
    MCategory mCategory = new MCategory();
    MCategory mSubSubCategory = new MCategory();
    private DialogPresenter dialogPresenter;
    private AddProductPresenter addProductPresenter;
    private Map<String, String> map = new HashMap<>();
    private List<MCategory> superCategoryList = new ArrayList<>();
    private List<MCategory> subCategoryList = new ArrayList<>();
    private List<MCategory> subSubCategoryList = new ArrayList<>();
    private List<MCategory> categoryList = new ArrayList<>();
    private List<MImage> imageList = new ArrayList<>();
    private ImageAdapter imageAdapter;
    private MAddProduct mAddProduct = new MAddProduct();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_add_product);
        initialization();
        listeners();
    }

    private void initialization() {
        binding.header.setHandler(new CommonClickHandler(activity));
        dialogPresenter = new DialogPresenter(this, activity);
        map.put("level", ZERO);
        map.put("eager", "children.children");
        dialogPresenter.getCategoryListTask(map);

        addProductPresenter = new AddProductPresenter(this, activity);
        map.put(LEVEL, ZERO);
        map.put(LIMIT, INFINITE_LIMIT);

        binding.rvImages.setLayoutManager(new LinearLayoutManager(activity));
        binding.rvImages.setHasFixedSize(true);
        binding.rvImages.setItemAnimator(new DefaultItemAnimator());
        imageAdapter = new ImageAdapter(activity, getImageList(), this);
        binding.rvImages.setAdapter(imageAdapter);
    }

    public List<MImage> getImageList() {
         MImage mImage ;
        if (imageList != null && imageList.size() < 4) {
            mImage = new MImage();
            mImage.setLocal(true);
            imageList.add(mImage);
        }

        return imageList;
    }

    private void listeners() {
        String category = binding.etCategory.getText().toString().trim();
        binding.etCategory.setOnClickListener(v -> {
            startActivityForResult(
                    DialogActivity.getIntent(activity, categoryList, category), OPEN_DIALOG_FOR_CATEGORY);
            overridePendingTransition(0, 0);
        });


        binding.etSubCategory.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(binding.etCategory.getText().toString())) {
                startActivityForResult(
                        DialogActivity.getIntent(activity, subCategoryList, category), OPEN_DIALOG_FOR_SUBCATEGORY);
                overridePendingTransition(0, 0);
            } else {
                Toast.makeText(activity, getResources().getString(R.string.please_select_category_first), Toast.LENGTH_SHORT).show();
            }

        });

        binding.etSubSubCategory.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(binding.etSubCategory.getText().toString())) {
                startActivityForResult(
                        DialogActivity.getIntent(activity, subSubCategoryList, category), OPEN_DIALOG_FOR_SUBSUBCATEGORY);
                overridePendingTransition(0, 0);
            } else {
                Toast.makeText(activity, getResources().getString(R.string.please_select_sub_category_first), Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnSubmit.setOnClickListener(v -> {
            if (checkValidate()) addProductPresenter.addProductTask(mAddProduct);
        });
    }

    private boolean checkValidate() {
        Resources resources = getResources();
        if (TextUtils.isEmpty(binding.etProductName.getText().toString())) {
            Toast.makeText(activity, resources.getString(R.string.please_enter_product_name), Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(binding.etCategory.getText().toString())) {
            Toast.makeText(activity, resources.getString(R.string.please_select_category_first), Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(binding.etSubCategory.getText().toString())) {
            Toast.makeText(activity, resources.getString(R.string.please_select_sub_category_first), Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(binding.etSubSubCategory.getText().toString())) {
            Toast.makeText(activity, resources.getString(R.string.please_select_sub_sub_category_first), Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(binding.etSellingPrice.getText().toString())) {
            Toast.makeText(activity, resources.getString(R.string.please_enter_selling_price), Toast.LENGTH_SHORT).show();
            return false;
        } /*else if (TextUtils.isEmpty(binding.etPaymentOption.getText().toString())) {
            Toast.makeText(activity, resources.getString(R.string.please_enter_state), Toast.LENGTH_SHORT).show();
            return false;
        }*/ else if (TextUtils.isEmpty(binding.etDescription.getText().toString())) {
            Toast.makeText(activity, resources.getString(R.string.please_enter_description), Toast.LENGTH_SHORT).show();
            return false;
        }
        prepareData();

        return true;
    }

    private void prepareData() {
        mAddProduct.setName(binding.etProductName.getText().toString());
        mAddProduct.setCategory(mSubSubCategory);
        mAddProduct.setPrice(binding.etSellingPrice.getText().toString());
        mAddProduct.setCost_price(binding.etSellingPrice.getText().toString());
        mAddProduct.setDescription(binding.etDescription.getText().toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == OPEN_DIALOG_FOR_CATEGORY) {
                mCategory = data.getParcelableExtra("MCategory");
                binding.etCategory.setText(mCategory.getName());
                binding.etSubCategory.setText("");
                binding.etSubSubCategory.setText("");

                subCategoryList = getSubCategoryList(mCategory);

            }
            if (requestCode == OPEN_DIALOG_FOR_SUBCATEGORY) {
                mCategory = data.getParcelableExtra("MCategory");
                binding.etSubCategory.setText(mCategory.getName());

                subSubCategoryList = getSubCategoryList(mCategory);
            }
            if (requestCode == OPEN_DIALOG_FOR_SUBSUBCATEGORY) {
                mCategory = data.getParcelableExtra("MCategory");
                binding.etSubSubCategory.setText(mCategory.getName());

                mSubSubCategory = mCategory;
            }
        }
    }


    @Override
    public void onSuccessfullyGetCategoryList(List<MCategory> mCategoryList, String message) {
        if (mCategoryList != null) {
            superCategoryList.addAll(mCategoryList);
            for (MCategory mCategory : superCategoryList) {
                if (mCategory.getChildren() != null)
                    this.categoryList.add(mCategory);
            }
        }
    }

    public List<MCategory> getSubCategoryList(MCategory mCategory) {
        if (mCategory != null) {
            List<MCategory> mCategoryList = new ArrayList<>();
            mCategoryList.addAll(mCategory.getChildren());
            return mCategoryList;
        }

        return null;
    }

    @Override
    public void onFailToGetCategoryList(String errorMessage) {
        ShowToast.toastMsg(activity, errorMessage);
    }


    @Override
    public void onDeleteImage(int position) {

    }

    @Override
    public void onSuccessfullyAddProduct(MAddProduct mAddProduct, String message) {
        //Log.d("AddProduct", mAddProduct.getName());
        finish();
    }

    @Override
    public void onFailToAddProduct(String errorMessage) {

    }
}

package com.retailer.oneops.product;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.retailer.oneops.R;
import com.retailer.oneops.auth.model.MCategory;
import com.retailer.oneops.databinding.ActivityAddProductBinding;
import com.retailer.oneops.myinventory.AddToInventoryActivity;
import com.retailer.oneops.myinventory.model.MInventory;
import com.retailer.oneops.product.adapter.ImageAdapter;
import com.retailer.oneops.product.adapter.ShowImagesAdapter;
import com.retailer.oneops.product.model.MAddProduct;
import com.retailer.oneops.product.model.MImage;
import com.retailer.oneops.product.model.MImageServer;
import com.retailer.oneops.product.presenter.AddProductPresenter;
import com.retailer.oneops.product.presenter.DialogPresenter;
import com.retailer.oneops.product.viewinterface.AddProductViewInterface;
import com.retailer.oneops.product.viewinterface.DialogViewInterface;
import com.retailer.oneops.productListing.model.MProduct;
import com.retailer.oneops.util.CommonClickHandler;
import com.retailer.oneops.util.Constant;
import com.retailer.oneops.util.Session;
import com.retailer.oneops.util.ShowToast;
import com.retailer.oneops.util.Utils;
import com.retailer.oneops.util.WebService;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import okhttp3.MultipartBody;

import static com.retailer.oneops.auth.util.Categoryutil.INFINITE_LIMIT;
import static com.retailer.oneops.auth.util.Categoryutil.LEVEL;
import static com.retailer.oneops.auth.util.Categoryutil.LIMIT;
import static com.retailer.oneops.auth.util.Categoryutil.ZERO;

public class AddProductActivity extends AppCompatActivity implements DialogViewInterface, AddProductViewInterface,
        ImageAdapter.CallBack {
    private static Intent intent;
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
    private ShowImagesAdapter imageAdapter;
    private MAddProduct mAddProduct = new MAddProduct();
    private Uri imageURI;
    private String profileImageURL = "";
    private int imagePosition;
    private MProduct mProduct;
    private boolean isEditProduct = false;
    private int selectedCategoryId = 0;
    private Bundle bundle;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_add_product);
        bundle = getIntent().getExtras();
        initialization();

        listeners();
    }

    private void initialization() {
        binding.header.setHandler(new CommonClickHandler(activity));
        binding.header.tvMainHeading.setText(R.string.Add_Product);

        dialogPresenter = new DialogPresenter(this, activity);
        map.put("level", ZERO);
        map.put("eager", "children.children");
        dialogPresenter.getCategoryListTask(map);

        addProductPresenter = new AddProductPresenter(this, activity);
        map.put(LEVEL, ZERO);
        map.put(LIMIT, INFINITE_LIMIT);

        binding.rvImages.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        binding.rvImages.setHasFixedSize(true);
        binding.rvImages.setItemAnimator(new DefaultItemAnimator());
        imageAdapter = new ShowImagesAdapter(activity, setFirstImage(), this);
        binding.rvImages.setAdapter(imageAdapter);
        if (intent != null && !getIntent().hasExtra(Constant.IS_SETTING))
            getIntentData();
    }

    public void setExistingData(MProduct mProduct) {
        selectedCategoryId = Integer.parseInt(mProduct.getCategoryId());
        binding.etProductName.setText(mProduct.getName());
        binding.etCategory.setText(mProduct.getCategory().getName());
        binding.etDescription.setText(mProduct.getDescription());
        binding.etSellingPrice.setText(mProduct.getPrice());
        binding.etCostPrice.setText(mProduct.getCost_price());
        imageList.clear();

        if (mProduct.getImages() != null) {
            if (mProduct.getImages().size() >= 4) {
                imageList.clear();
            }
            imageList.addAll(mProduct.getImages());

            setFirstImage();
            imageAdapter.notifyDataSetChanged();
        }

        if (mProduct.getCategory() != null
                && mProduct.getCategory().getParent() != null && mProduct.getCategory().getParent().getParent() != null) {
            binding.etCategory.setText(mProduct.getCategory().getParent().getParent().getName());
            //prepareSubCategoryListFromCategory(mProduct.getCategory().getParent());

            if (mProduct.getCategory() != null && mProduct.getCategory().getParent() != null) {
                binding.etSubCategory.setText(mProduct.getCategory().getParent().getName());
               // prepareSubSubCategoryListFromCategory(mProduct.getCategory());

                if (mProduct.getCategory() != null) {
                    binding.etSubSubCategory.setText(mProduct.getCategory().getName());
                }

            }
        }
    }

    private void getIntentData() {
        if (intent.hasExtra("mProduct")) {
            binding.header.tvMainHeading.setText(R.string.Edit_Product);
            binding.btnSubmit.setText(R.string.Update_Product);
            isEditProduct = true;
            mProduct = intent.getParcelableExtra("mProduct");
            if (mProduct != null) {
                setExistingData(mProduct);
            }
        }
    }


    public static Intent getIntent(Activity activity, MProduct productModel) {
        intent = null;
        intent = new Intent(activity, AddProductActivity.class);
        intent.putExtra("mProduct", (Parcelable) productModel);
        return intent;
    }

    public List<MImage> setFirstImage() {
        MImage mImage;
        if (imageList != null && imageList.size() < 4) {
            mImage = new MImage();
            mImage.setLocal(true);
            imageList.add(mImage);
        }
        return imageList;
    }

    public List<MImage> setLocalImage() {
        MImage mImage;
        if (imageList != null && imageList.size() < 4) {
            mImage = new MImage();
            mImage.setLocal(true);
            imageList.add(mImage);
        }

        imageAdapter.notifyDataSetChanged();
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
            if (checkValidate()) {
                if (imageAdapter.getItemCount() != 0) {
                    int i = CreateFileForSend(imageList);

                    if (i == 0) {
                        mAddProduct.getCategory().setIsSelected(null);
                        mAddProduct.setCategoryId(String.valueOf(selectedCategoryId));
                        mAddProduct.setCategory(null);
                        if (isEditProduct)
                            addProductPresenter.updateProductTask(mAddProduct, Integer.parseInt(mProduct.getId()));
                    }
                } else {
                    if (checkValidate()) addProductPresenter.addProductTask(mAddProduct);
                }
            }
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
        } /*else if (TextUtils.isEmpty(binding.etSubCategory.getText().toString())) {
            Toast.makeText(activity, resources.getString(R.string.please_select_sub_category_first), Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(binding.etSubSubCategory.getText().toString())) {
            Toast.makeText(activity, resources.getString(R.string.please_select_sub_sub_category_first), Toast.LENGTH_SHORT).show();
            return false;
        }*/ else if (TextUtils.isEmpty(binding.etSellingPrice.getText().toString())) {
            Toast.makeText(activity, resources.getString(R.string.please_enter_selling_price), Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(binding.etCostPrice.getText().toString())) {
            Toast.makeText(activity, resources.getString(R.string.please_enter_cost_price), Toast.LENGTH_SHORT).show();
            return false;
        }/*else if (TextUtils.isEmpty(binding.etPaymentOption.getText().toString())) {
            Toast.makeText(activity, resources.getString(R.string.please_enter_state), Toast.LENGTH_SHORT).show();
            return false;
        }*/ else if (imageAdapter.getItemCount() <= 1) {
            Toast.makeText(activity, resources.getString(R.string.please_select_image), Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(binding.etDescription.getText().toString())) {
            Toast.makeText(activity, resources.getString(R.string.please_enter_description), Toast.LENGTH_SHORT).show();
            return false;
        }

       /* for (MImage mImage : imageList) {
            mAddProduct.setImages(imageList);
        }
*/
        prepareData();

        return true;
    }

    private void prepareData() {
        mAddProduct.setUser_id(new Session(activity).getUserProfile().getId());
        mAddProduct.setName(binding.etProductName.getText().toString());
        mAddProduct.setCategory(mSubSubCategory);
        mAddProduct.setPrice(binding.etSellingPrice.getText().toString());
        mAddProduct.setCost_price(binding.etCostPrice.getText().toString());
        mAddProduct.setDescription(binding.etDescription.getText().toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == OPEN_DIALOG_FOR_CATEGORY) {
                mCategory = data.getParcelableExtra("MCategory");
                prepareSubCategoryListFromCategory(mCategory);
            }

            if (requestCode == OPEN_DIALOG_FOR_SUBCATEGORY) {
                mCategory = data.getParcelableExtra("MCategory");
                prepareSubSubCategoryListFromCategory(mCategory);
            }

            if (requestCode == OPEN_DIALOG_FOR_SUBSUBCATEGORY) {
                mCategory = data.getParcelableExtra("MCategory");
                selectedCategoryId = Integer.parseInt(mCategory.getId());
                binding.etSubSubCategory.setText(mCategory.getName());

                mSubSubCategory = mCategory;
            }

            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                Uri resultUri = result.getUri();
                //CreateFileForSend(result.getUri());

               /* for (int i = 0; i < imageList.size(); i++) {
                    MImage mImage = imageList.get(i);
                    if (mImage.isLocal()) {
                        imageList.remove(i);
                    }
                }*/

                if (!Utils.checkNull(resultUri.getPath()).isEmpty()) {
                    profileImageURL = resultUri.getPath();
                    MImage mImage = new MImage();
                    mImage.setUrl(resultUri.getPath());
                    mImage.setLocal(false);
                    mImage.setFile(new File(resultUri.getPath()));
                    imageList.set(imagePosition, mImage);
                }

                if ((imageList.size() > 1 || imageList.get(0).getFile() != null) && imageList.size() < 4) {
                    setLocalImage();
                } else {
                    imageAdapter.notifyDataSetChanged();
                }

            }
        }
    }

    public void prepareSubCategoryListFromCategory(MCategory category) {
        selectedCategoryId = Integer.parseInt(category.getId());
        binding.etCategory.setText(category.getName());
        binding.etSubCategory.setText("");
        binding.etSubSubCategory.setText("");

        subCategoryList = getSubCategoryList(category);
    }

    public void prepareSubSubCategoryListFromCategory(MCategory category) {
        selectedCategoryId = Integer.parseInt(category.getId());
        binding.etSubCategory.setText(category.getName());

        subSubCategoryList = getSubCategoryList(category);
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
        imageList.remove(position);

        try {
            if (imageList.size() < 4 && (imageList.get(imageList.size() - 1).getFile() != null) || (imageList.get(imageList.size() - 1).getUrl() != null && !imageList.get(imageList.size() - 1).getUrl().isEmpty()))
                setLocalImage();

            imageAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAddImageClick(int position) {
        imagePosition = position;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            CropImage.activity()
                    .setActivityTitle(getString(R.string.app_name))
                    .setCropShape(CropImageView.CropShape.RECTANGLE)
                    .setActivityMenuIconColor(R.color.colorMobileProfile)
                    .setBorderLineColor(Color.WHITE)
                    .setGuidelinesColor(R.color.colorPrimary)
                    .setAspectRatio(1, 1)
                    .setFixAspectRatio(true)
                    .start((Activity) this);
        } else {
            CropImage.activity()
                    .setActivityTitle(getString(R.string.app_name))
                    .setCropShape(CropImageView.CropShape.RECTANGLE)
                    .setActivityMenuIconColor(R.color.colorMobileProfile)
                    .setBorderLineColor(Color.WHITE)
                    .setGuidelinesColor(R.color.colorPrimary)
                    .setAspectRatio(1, 1)
                    .setFixAspectRatio(true)
                    .start((Activity) activity);
        }
    }

    @Override
    public void onSuccessfullyAddProduct(MAddProduct mAddProduct, String message) {
        //Log.d("AddProduct", mAddProduct.getName());
        finish();
    }

    @Override
    public void onSucessfullyUpdatedImage(List<MImageServer> mImageList) {
        if (mAddProduct != null && mAddProduct.getImages() != null && mAddProduct.getImages().size() > 0) {
            mAddProduct.getImages().addAll(mImageList);
        } else
            mAddProduct.setImages(mImageList);

        if (checkValidate()) {
            mAddProduct.getCategory().setIsSelected(null);
            mAddProduct.setCategoryId(String.valueOf(selectedCategoryId));
            mAddProduct.setCategory(null);
            if (isEditProduct) {
                addProductPresenter.updateProductTask(mAddProduct, Integer.parseInt(mProduct.getId()));
            } else {
                addProductPresenter.addProductTask(mAddProduct);
            }

        }
    }

    @Override
    public void onFailToUpdate(String errorMessage) {

    }

    // Crop image convert to file
    public int CreateFileForSend(List<MImage> imageList) {
        List<MultipartBody.Part> files = new ArrayList<>();
        for (MImage mImage : imageList) {

            if (!mImage.isLocal() && mImage.getFile() != null) {
                imageURI = Uri.fromFile(mImage.getFile());
                //File image = Utils.compressURIForUpload(activity, imageURI, "");

                if (mImage.getFile() != null)
                    files.add(Utils.getFileRequestBody_part(WebService.FILES, mImage.getFile()));
            } else if (mImage != null && mImage.getUrl() != null && TextUtils.isEmpty(mImage.getUrl())) {
                files.add(Utils.getFileRequestBody_part(WebService.FILES, mImage.getFile()));
            }
        }

        addProductPresenter.onUpdateImage(files);
        Log.d("AddProduct", "file size: " + files.size());

        return files.size();
    }

/*    public String getPriceFromString(String inputString) {
        String price = "";
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(inputString);
        while (m.find()) {
            System.out.println(m.group());
            price = m.group();
        }

        return price;
    }*/
}

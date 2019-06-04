package com.retailer.oneops.service.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.retailer.oneops.R;
import com.retailer.oneops.apiCalling.APIClient;
import com.retailer.oneops.apiCalling.APIInterface;

import com.retailer.oneops.apiCalling.APIResponse;
import com.retailer.oneops.apiCalling.GenericResponse;
import com.retailer.oneops.apiCalling.Repository;
import com.retailer.oneops.auth.model.MCategory;
import com.retailer.oneops.dashboard.presenter.EditProfilePresenter;
import com.retailer.oneops.databinding.ActivityAddServiceBinding;
import com.retailer.oneops.interfaces.UpdateAccount;
import com.retailer.oneops.product.DialogActivity;
import com.retailer.oneops.product.adapter.ImageAdapter;
import com.retailer.oneops.product.model.MAddProduct;
import com.retailer.oneops.product.model.MImage;
import com.retailer.oneops.product.model.MImageServer;
import com.retailer.oneops.product.presenter.DialogPresenter;
import com.retailer.oneops.product.viewinterface.AddProductViewInterface;
import com.retailer.oneops.product.viewinterface.DialogViewInterface;
import com.retailer.oneops.settings.model.AddService;
import com.retailer.oneops.settings.model.MUrl;
import com.retailer.oneops.util.CommonClickHandler;
import com.retailer.oneops.util.Constant;
import com.retailer.oneops.util.MyDialogProgress;
import com.retailer.oneops.util.Session;
import com.retailer.oneops.util.ShowToast;
import com.retailer.oneops.util.Utils;
import com.retailer.oneops.util.WebService;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;

import static com.retailer.oneops.auth.util.Categoryutil.ZERO;
import static com.retailer.oneops.util.Constant.AUTHORIZATION_KEY;

public class AddServiceActivity extends AppCompatActivity implements DialogViewInterface, APIResponse {
    private Activity activity;
    private ActivityAddServiceBinding binding;
    private List<MCategory> categoryList = new ArrayList<>();
    private List<MCategory> superCategoryList = new ArrayList<>();
    private MCategory mCategory = new MCategory();
    private Uri imageURI;
    private DialogPresenter dialogPresenter;
    private Map<String, String> map = new HashMap<>();
    private String mImage = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_add_service);
        initialization();
        listeners();
    }

    private void listeners() {
        String category = binding.etCategory.getText().toString().trim();
        binding.etCategory.setOnClickListener(v -> {
            startActivityForResult(
                    DialogActivity.getIntent(activity, categoryList, category), Constant.OPEN_DIALOG_FOR_CATEGORY);
            overridePendingTransition(0, 0);
        });
    }

    private void initialization() {
        binding.header.setHandler(new CommonClickHandler(activity));
        binding.setAddService(this);
        dialogPresenter = new DialogPresenter(this, activity);
        map.put("level", ZERO);
        map.put("eager", "children.children");
        dialogPresenter.getCategoryListTask(map);
    }

    public void pickImage(View view) {
        CropImage.activity()
                .setActivityTitle(getString(R.string.app_name))
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .setActivityMenuIconColor(R.color.colorMobileProfile)
                .setBorderLineColor(Color.WHITE)
                .setGuidelinesColor(R.color.colorPrimary)
                .setAspectRatio(1, 1)
                .setFixAspectRatio(true)
                .start((Activity) this);
    }

    public void onSubmit(View view) {
        checkValidation();
    }

    private void checkValidation() {
        String serviceName = binding.etServiceName.getText().toString().trim();
        String serviceCategory = binding.etCategory.getText().toString().trim();
        String servicePrice = binding.etServicePrice.getText().toString().trim();
        String sellingPrice = binding.etSellingPrice.getText().toString().trim();
        String serviceDescription = binding.etDescription.getText().toString().trim();

        if (TextUtils.isEmpty(serviceName)) {
            Utils.showToast(activity, getString(R.string.error_pls_enter, getString(R.string.service_name).toLowerCase()));
            return;
        }
        if (TextUtils.isEmpty(serviceCategory)) {
            Utils.showToast(activity, getString(R.string.please_select_category_first));
            return;
        }

        if (TextUtils.isEmpty(servicePrice)) {
            Utils.showToast(activity, getString(R.string.error_pls_enter, getString(R.string.service_price).toLowerCase()));
            return;
        }

        if (Integer.parseInt(servicePrice) <= 0) {
            Utils.showToast(activity, getString(R.string.error_valid_amount, getString(R.string.service_price).toLowerCase()));
            return;
        }

        if (TextUtils.isEmpty(sellingPrice)) {
            Utils.showToast(activity, getString(R.string.error_pls_enter, getString(R.string.selling_price).toLowerCase()));
            return;
        }

        if (Integer.parseInt(sellingPrice) <= 0) {
            Utils.showToast(activity, getString(R.string.error_valid_amount, getString(R.string.selling_price).toLowerCase()));
            return;
        }

        if (TextUtils.isEmpty(mImage)) {
            Utils.showToast(activity, getString(R.string.please_select_service_image));
            return;
        }

        if (TextUtils.isEmpty(serviceDescription)) {
            Utils.showToast(activity, getString(R.string.error_pls_enter, getString(R.string.service_description).toLowerCase()));
            return;
        }

        AddService addService = new AddService();
        addService.setService_name(serviceName);
        addService.setService_price(servicePrice);
        addService.setService_description(serviceDescription);
        addService.setCategory_id(mCategory.getId());

        MUrl mUrl = new MUrl();
        mUrl.setUrl(mImage);
        ArrayList<MUrl> mUrlsList = new ArrayList<>();
        mUrlsList.add(mUrl);
        addService.setImages(mUrlsList);

        addService(Constant.ADD_SERVICE, addService, 0);
    }

    private void addService(int requestType, AddService jsonObject, int id) {
        Call<JsonElement> call = null;
        if (id == 0)
            call = APIClient.getClient(activity).create(APIInterface.class).addService(new Session(activity).getString(AUTHORIZATION_KEY), jsonObject);
        else
            call = APIClient.getClient(activity).create(APIInterface.class).editService(new Session(activity).getString(AUTHORIZATION_KEY), jsonObject, id);

        MyDialogProgress.open(activity);
        try {
            new Repository(this).callBaseURL(this, call, requestType);
        } catch (Exception e) {
            e.printStackTrace();
            Utils.showToastWentWrong(activity);
            MyDialogProgress.close(activity);
        }
    }

    // Crop image convert to file
    public void CreateFileForSend(Uri URI) {
        imageURI = URI;
        File image = Utils.compressURI(activity, URI, "");

        MultipartBody.Part file = null;
        if (image != null)
            file = Utils.getFileRequestBody_part(WebService.FILE, image);

        new Repository(activity).callProfileAPI(file, activity, new UpdateAccount() {
            @Override
            public void onSuccess(int position, Object object) {
                mImage = (String) object;
                binding.ivService.setImageURI(imageURI);
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constant.OPEN_DIALOG_FOR_CATEGORY) {
                mCategory = data.getParcelableExtra("MCategory");
                binding.etCategory.setText(mCategory.getName());
            }


            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                CreateFileForSend(result.getUri());
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


    @Override
    public void onFailToGetCategoryList(String errorMessage) {
        ShowToast.toastMsg(activity, errorMessage);
    }


    @Override
    public void onSuccess(JSONObject genericResponse, String msg, int ResponseOf) {
        if (ResponseOf == Constant.ADD_SERVICE) {
            Utils.showToast(activity, getString(R.string.service_added_successfully));
        } else if (ResponseOf == Constant.EDIT_SERVICE) {
            Utils.showToast(activity, getString(R.string.service_updated_successfully));
        }
        MyDialogProgress.close(activity);
        finish();
    }


    @Override
    public void onFailed(GenericResponse genericResponse, String msg, int ResponseOf) {
        MyDialogProgress.close(activity);
        Utils.showToast(activity, msg);
    }

    @Override
    public void onException(boolean type, String msg, int ResponseOf) {
        MyDialogProgress.close(activity);
        Utils.showToast(activity, msg);
    }
}

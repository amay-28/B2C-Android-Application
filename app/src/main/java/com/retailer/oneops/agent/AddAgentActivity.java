package com.retailer.oneops.agent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.retailer.oneops.agent.model.AddAgent;
import com.retailer.oneops.apiCalling.APIClient;
import com.retailer.oneops.apiCalling.APIInterface;
import com.retailer.oneops.apiCalling.APIResponse;

import com.retailer.oneops.apiCalling.GenericResponse;
import com.retailer.oneops.apiCalling.Repository;
import com.retailer.oneops.databinding.ActivityAddAgentBinding;
import com.retailer.oneops.interfaces.UpdateAccount;
import com.retailer.oneops.util.CommonClickHandler;
import com.retailer.oneops.util.Constant;
import com.retailer.oneops.util.ImageUtils;
import com.retailer.oneops.util.MyDialogProgress;
import com.retailer.oneops.util.Session;
import com.retailer.oneops.util.Utils;
import com.retailer.oneops.util.WebService;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.retailer.oneops.R;


import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;

import static com.retailer.oneops.util.Constant.AUTHORIZATION_KEY;

public class AddAgentActivity extends AppCompatActivity implements APIResponse {
    ActivityAddAgentBinding addAgentActivityBinding;
    Activity activity;
    private Uri imageURI;
    private String mImage = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        activity = this;
        addAgentActivityBinding = DataBindingUtil.setContentView ( activity, R.layout.activity_add_agent );

        intialization ();
        listeners();
    }

    private void listeners() {
        addAgentActivityBinding.ivUploadImage.setOnClickListener ( v -> pickImage () );
        addAgentActivityBinding.btnAddAgent.setOnClickListener ( v -> onSubmit ( ) );
    }

    private void intialization() {

        addAgentActivityBinding.header.setHandler ( new CommonClickHandler ( activity ) );
        addAgentActivityBinding.header.tvMainHeading.setText ( R.string.AddAgent );


    }

    public void pickImage() {
        CropImage.activity ()
                .setActivityTitle ( getString ( R.string.app_name ) )
                .setCropShape ( CropImageView.CropShape.RECTANGLE )
                .setActivityMenuIconColor ( R.color.colorMobileProfile )
                .setBorderLineColor ( Color.WHITE )
                .setGuidelinesColor ( R.color.colorPrimary )
                .setAspectRatio ( 1, 1 )
                .setFixAspectRatio ( true )
                .start ( (Activity) this );
    }

    public void onSubmit() {
        checkValidation ();
    }

    private void checkValidation() {
        String agentName = addAgentActivityBinding.etName.getText ().toString ().trim ();
        String mobileNo = addAgentActivityBinding.etMobileNo.getText ().toString ().trim ();


        if (TextUtils.isEmpty ( agentName )) {
            Toast.makeText ( activity, getResources ().getString ( R.string.please_enter_valid_name ), Toast.LENGTH_SHORT ).show ();

            return;
        } else if (addAgentActivityBinding.etMobileNo.getText ().toString ().length () != 10) {
            Toast.makeText ( activity, getResources ().getString ( R.string.please_enter_valid_mobile_no ), Toast.LENGTH_SHORT ).show ();
        }
        else if (TextUtils.isEmpty ( mImage )) {
            Utils.showToast ( activity, getString ( R.string.please_select_agent_image ) );
            return;
        }

        AddAgent addAgent = new AddAgent ();
        addAgent.setName ( agentName );
        addAgent.setMobileNumber ( mobileNo );
        addAgent.setImageUrl ( mImage );
      //  MUrl mUrl = new MUrl ();
       // mUrl.setUrl ( mImage );
       // ArrayList <MUrl> mUrlsList = new ArrayList <> ();
       // mUrlsList.add ( mUrl );
       // addAgent.setImageUrl ( mUrlsList );
        addAgent(Constant.ADD_AGENT, addAgent, 0);

    }

    // Crop image convert to file

    public void CreateFileForSend(Uri URI) {
        imageURI = URI;
        Bitmap bitmap = ImageUtils.getInstant().getCompressedBitmap(imageURI.getPath());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, bos);




        if (bitmap != null) {
            RequestBody mRBCover = RequestBody.create(MediaType.parse("image/jpeg"), bos.toByteArray());
            MultipartBody.Part mPartCover = MultipartBody.Part.createFormData(WebService.FILE, "file.jpg", mRBCover);


            new Repository(activity).callProfileAPI(mPartCover, activity, new UpdateAccount() {
                @Override
                public void onSuccess(int position, Object object) {
                    mImage = (String) object;
                    addAgentActivityBinding.ivUploadImage.setImageURI(imageURI);
                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult ( requestCode, resultCode, data );
        if (resultCode == RESULT_OK) {

            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
                CropImage.ActivityResult result = CropImage.getActivityResult ( data );
                CreateFileForSend ( result.getUri () );
            }
        }
    }

    private void addAgent(int requestType, AddAgent jsonObject, int id) {
        Call<JsonElement> call = null;
        if (id == 0)
            call = APIClient.getClient(activity).create(APIInterface.class).addAgent (new Session (activity).getString(AUTHORIZATION_KEY), jsonObject);
        else
            call = APIClient.getClient(activity).create(APIInterface.class).editAgent (new Session(activity).getString(AUTHORIZATION_KEY), jsonObject, id);

        MyDialogProgress.open(activity);
        try {
            new Repository(this).callBaseURL(this, call, requestType);
        } catch (Exception e) {
            e.printStackTrace();
            Utils.showToastWentWrong(activity);
            MyDialogProgress.close(activity);
        }
    }

    @Override
    public void onSuccess(JSONObject genericResponse, String msg, int ResponseOf) {
        if (ResponseOf == Constant.ADD_AGENT) {
            Utils.showToast(activity, getString(R.string.agent_added_successfully));
        } else if (ResponseOf == Constant.EDIT_AGENT) {
            Utils.showToast(activity, getString(R.string.agent_updated_successfully));
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
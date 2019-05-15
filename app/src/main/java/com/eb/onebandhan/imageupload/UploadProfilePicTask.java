package com.eb.onebandhan.imageupload;

import android.app.Activity;

import com.eb.onebandhan.apiCalling.APIClient;
import com.eb.onebandhan.apiCalling.APIInterface;
import com.eb.onebandhan.apiCalling.ResponseData;
import com.eb.onebandhan.util.Constant;
import com.eb.onebandhan.util.Session;
import com.eb.onebandhan.util.Utils;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadProfilePicTask implements Constant {
   private APIInterface apiInterface;
   private Activity activity;
   private CallBacks callBacks;
   private Session session;

   public void uploadProfilePicTask(Activity activity, File file, CallBacks callBacks) {
       this.activity = activity;
       this.callBacks = callBacks;
       session = new Session(activity);
       apiInterface = APIClient.getClient(activity).create(APIInterface.class);
       performUploadProfilePic(file);
   }

   private void performUploadProfilePic(File file) {

       /*Call<ResponseData> callUploadProfilePic = apiInterface.uploadProfilePic(isProfilePic?PROFILE_PIC_URL:COVER_PIC_URL,session.getString(AUTHORIZATION_KEY), Utils.getStringRequestBody(session.getUserProfile().getEmail()),Utils.getMultipartFile(FILE,file));

       callUploadProfilePic.enqueue(new Callback<ResponseData>() {
           @Override
           public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
               if (response.isSuccessful()) {
                   callBacks.onSuccessfullyUploadProfilePic(response.body().getMessage());

               } else try {
                   ResponseData<String> errorResponse = new Gson().fromJson(response.errorBody().string(), ResponseData.class);
                   callBacks.onFailedToUploadProfilePic(errorResponse.getMessage());
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }

           @Override

           public void onFailure(Call<ResponseData> call, Throwable t) {
               callBacks.onFailedToUploadProfilePic(t.getMessage());
               t.printStackTrace();
           }
       });*/
   }

   public interface CallBacks {

       void onFailedToUploadProfilePic(String message);

       void onSuccessfullyUploadProfilePic(String message);
   }
}
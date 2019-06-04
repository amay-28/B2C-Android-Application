package com.retailer.oneops.apiCalling;

import android.app.Activity;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.retailer.oneops.R;
import com.retailer.oneops.interfaces.UpdateAccount;
import com.retailer.oneops.util.Constant;
import com.retailer.oneops.util.MyDialogProgress;
import com.retailer.oneops.util.Session;
import com.retailer.oneops.util.Utils;
import com.retailer.oneops.util.WebService;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.retailer.oneops.util.Constant.AUTHORIZATION_KEY;

public class Repository {

    private Activity mContext;

    public Repository(Activity context) {
        this.mContext = context;
        //
    }


    public void callBaseURL(final APIResponse requestCallback, Call call, final int type) {
        try {
            if (call != null) {
                call.enqueue(new Callback<JsonElement>() {
                    @Override
                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                        //
                        String msg = "";
                        if (response.isSuccessful()) {

                            if (response.body() != null) {
                                try {
                                    JsonElement jsonElement = (JsonElement) response.body();
                                    JSONObject jsonObject = new JSONObject(jsonElement.toString());
                                    GenericResponse genericResponse = new Gson().fromJson(jsonElement.toString(), GenericResponse.class);
                                    requestCallback.onSuccess(jsonObject, genericResponse.getMessage(), type);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        } else {
                            try {
                                if (response.code() != Constant.UNAUTHORIZED_CODE) {
                                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                                    GenericResponse genericResponse = new Gson().fromJson(jObjError.toString(), GenericResponse.class);
                                    requestCallback.onFailed(genericResponse, genericResponse.getMessage(), type);
                                } else {
                                    MyDialogProgress.close(mContext);
                                    //   SessionExpireDialog.openDialog(mContext, 0, ""); TODO Add this line when added session expire code
                                }
                            } catch (Exception e) {
                                nullCase(false, requestCallback, type);
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<JsonElement> call, Throwable t) {
                        nullCase(true, requestCallback, type);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            nullCase(false, requestCallback, type);
        }

    }


    private void nullCase(boolean isNetwork, APIResponse request, int type) {
        if (isNetwork) {
            request.onException(isNetwork, mContext.getApplicationContext().getString(R.string.error_connection), type);
        } else {
            request.onException(isNetwork, mContext.getApplicationContext().getString(R.string.error_something_wrong), type);
        }
    }

    public static APIInterface getApiInterface(Activity activity) {
        return APIClient.getClient(activity).create(APIInterface.class);
    }


    public void callProfileAPI(MultipartBody.Part file, Activity mActivity, UpdateAccount updateAccount) {
        Call<JsonElement> call = getApiInterface(mActivity).uploadImageFile(new Session(mActivity).getString(AUTHORIZATION_KEY), file);
        MyDialogProgress.open(mActivity);
        try {
            new Repository(mActivity).callBaseURL(new APIResponse() {
                @Override
                public void onSuccess(JSONObject genericResponse, String msg, int ResponseOf) {

                    try {
                        String url = genericResponse.getJSONObject(Constant.IMAGE).getString(WebService.URL);
                        updateAccount.onSuccess(0, url);
                        MyDialogProgress.close(mActivity);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        MyDialogProgress.close(mActivity);
                    }
                }

                @Override
                public void onFailed(GenericResponse genericResponse, String msg, int ResponseOf) {
                    MyDialogProgress.close(mActivity);
                    Utils.showToast(mActivity, msg);
                }

                @Override
                public void onException(boolean type, String msg, int ResponseOf) {
                    MyDialogProgress.close(mActivity);
                    Utils.showToast(mActivity, msg);
                }
            }, call, Constant.UPLOAD_IMAGE);
        } catch (Exception e) {
            e.printStackTrace();
            Utils.showToastWentWrong(mActivity);
            MyDialogProgress.close(mActivity);
        }
    }
}

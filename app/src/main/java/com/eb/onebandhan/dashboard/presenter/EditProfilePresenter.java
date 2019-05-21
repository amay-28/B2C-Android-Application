package com.eb.onebandhan.dashboard.presenter;

import android.app.Activity;
import android.util.Log;

import com.eb.onebandhan.apiCalling.APIClient;
import com.eb.onebandhan.apiCalling.APIInterface;
import com.eb.onebandhan.apiCalling.ResponseData;
import com.eb.onebandhan.auth.model.MProfile;
import com.eb.onebandhan.auth.model.MUser;
import com.eb.onebandhan.dashboard.presenterinterface.EditProfilePresenterInterface;
import com.eb.onebandhan.dashboard.viewinterface.EditProfileViewInterface;
import com.eb.onebandhan.util.Constant;
import com.eb.onebandhan.util.Session;
import com.eb.onebandhan.util.Utils;
import com.eb.onebandhan.util.WebService;
import com.google.gson.JsonElement;

import org.json.JSONObject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import retrofit2.Response;
import retrofit2.adapter.rxjava2.HttpException;

public class EditProfilePresenter implements EditProfilePresenterInterface, Constant {
    private EditProfileViewInterface viewInterface;
    private Activity activity;

    public EditProfilePresenter(EditProfileViewInterface viewInterface, Activity activity) {
        this.viewInterface = viewInterface;
        this.activity = activity;
    }

    public DisposableObserver<Response<ResponseData<MUser>>> getObserver() {
        return new DisposableObserver<Response<ResponseData<MUser>>>() {
            @Override
            public void onNext(Response<ResponseData<MUser>> response) {
                viewInterface.onSucessfullyUpdated(response.body().getData(), response.body().getMessage());
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof HttpException)
                    viewInterface.onFailToUpdate(Utils.errorMessageParsing(e).getMessage());
            }

            @Override
            public void onComplete() {

            }
        };
    }

    public DisposableObserver<ResponseData<JsonElement>> getObserverImage() {
        return new DisposableObserver<ResponseData<JsonElement>>() {
            @Override
            public void onNext(ResponseData<JsonElement> value) {
                try {
                    JSONObject MainOBJ = new JSONObject(value.getImage().toString());
                    viewInterface.onSucessfullyUpdatedImage(MainOBJ.getString(WebService.URL));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof HttpException)
                    viewInterface.onFailToUpdate(Utils.errorMessageParsing(e).getMessage());
            }

            @Override
            public void onComplete() {

            }
        };
    }

    private <T> Observable getObservable(MProfile mProfile) {
        return APIClient.getClient(activity).create(APIInterface.class).updateProfile(new Session(activity).getString(AUTHORIZATION_KEY), mProfile, true).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    private <T> Observable getObservableImage(MultipartBody.Part file) {
        return APIClient.getClient(activity).create(APIInterface.class).uploadImage(new Session(activity).getString(AUTHORIZATION_KEY), file).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void onUpdateProfile(MProfile mProfile) {
        getObservable(mProfile).subscribeWith(getObserver());
    }

    @Override
    public void onUpdateImage(MultipartBody.Part file) {
        getObservableImage(file).subscribeWith(getObserverImage());
    }

}

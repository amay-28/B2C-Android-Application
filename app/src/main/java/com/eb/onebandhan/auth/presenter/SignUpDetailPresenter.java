package com.eb.onebandhan.auth.presenter;

import android.app.Activity;
import android.util.Log;

import com.eb.onebandhan.apiCalling.APIClient;
import com.eb.onebandhan.apiCalling.APIInterface;
import com.eb.onebandhan.apiCalling.ResponseData;
import com.eb.onebandhan.auth.model.MCategory;
import com.eb.onebandhan.auth.model.MProfile;
import com.eb.onebandhan.auth.model.MSignUp;
import com.eb.onebandhan.auth.model.MUser;
import com.eb.onebandhan.auth.presenterinterface.SignUpDetailPresenterInterface;
import com.eb.onebandhan.auth.presenterinterface.SignUpPresenterInterface;
import com.eb.onebandhan.auth.util.Categoryutil;
import com.eb.onebandhan.auth.viewinterface.SignUpDetailViewInterface;
import com.eb.onebandhan.auth.viewinterface.SignUpViewInterface;
import com.eb.onebandhan.util.Session;
import com.eb.onebandhan.util.Utils;
import com.eb.onebandhan.util.WebService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.adapter.rxjava2.HttpException;

public class SignUpDetailPresenter implements SignUpDetailPresenterInterface, Categoryutil {
    private SignUpDetailViewInterface viewInterface;
    private Activity activity;

    public SignUpDetailPresenter(SignUpDetailViewInterface viewInterface, Activity activity) {
        this.viewInterface = viewInterface;
        this.activity = activity;
    }


    public DisposableObserver<ResponseData<List<MCategory>>> getObserver() {
        return new DisposableObserver<ResponseData<List<MCategory>>>(){
            @Override
            public void onNext(ResponseData<List<MCategory>> response) {
                viewInterface.onSucessfullygetCategories(response.getData(),response.getMessage());
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof HttpException) viewInterface.onFailTogetCategories(Utils.errorMessageParsing(e).getMessage());
            }

            @Override
            public void onComplete() {

            }
        };
    }

    private <T> Observable getObservable(Map<String, String> map) {
        return  APIClient.getClient(activity).create(APIInterface.class).getCategoryRelatedData(map)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void loadCategoryTask(Map<String, String> map) {
        getObservable(map).subscribeWith(getObserver());
    }

    @Override
    public void submitShopDetailTask(MProfile mProfile) {
        getObservableForShop(mProfile).subscribeWith(getObserverForShop());

    }

    private DisposableObserver<ResponseData<MUser>> getObserverForShop() {
        return new DisposableObserver<ResponseData<MUser>>() {
            @Override
            public void onNext(ResponseData<MUser> response) {
                new Session(activity).getUserProfile().setRetailerDetails(response.getData().getRetailerDetails());

                viewInterface.onSucessfullySubmitShopDetail(response.getData(),response.getMessage());

            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof HttpException) viewInterface.onFailToSubmitShopDetail(Utils.errorMessageParsing(e).getMessage());
            }

            @Override
            public void onComplete() {

            }
        };
    }

    private <T> Observable getObservableForShop(MProfile mProfile) {
        return  APIClient.getClient(activity).create(APIInterface.class).updateShopDetail(mProfile)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}

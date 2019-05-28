package com.retailer.oneops.auth.presenter;

import android.app.Activity;
import android.content.Intent;

import com.retailer.oneops.apiCalling.APIClient;
import com.retailer.oneops.apiCalling.APIInterface;
import com.retailer.oneops.apiCalling.ResponseData;
import com.retailer.oneops.auth.activity.LoginActivity;
import com.retailer.oneops.auth.model.MCategory;
import com.retailer.oneops.auth.model.MProfile;
import com.retailer.oneops.auth.model.MUser;
import com.retailer.oneops.auth.presenterinterface.SignUpDetailPresenterInterface;
import com.retailer.oneops.auth.util.Categoryutil;
import com.retailer.oneops.auth.viewinterface.SignUpDetailViewInterface;
import com.retailer.oneops.util.Session;
import com.retailer.oneops.util.Utils;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
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
        return new DisposableObserver<ResponseData<List<MCategory>>>() {
            @Override
            public void onNext(ResponseData<List<MCategory>> response) {
                viewInterface.onSucessfullygetCategories(response.getData(), response.getMessage());
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof HttpException)
                    viewInterface.onFailTogetCategories(Utils.errorMessageParsing(e).getMessage());
            }

            @Override
            public void onComplete() {

            }
        };
    }

    private <T> Observable getObservable(Map<String, String> map) {
        return APIClient.getClient(activity).create(APIInterface.class).getCategoryRelatedData(map)
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
               /* Session session = new Session(activity);
                MUser mUser = session.getUserProfile();
                mUser.setRetailerDetails(response.getData().getRetailerDetails());
                session.setUserProfile(mUser);*/

                //new Session(activity).getUserProfile().setRetailerDetails(response.getData().getRetailerDetails());
                new Session(activity).setUserProfile(response.getData());

                viewInterface.onSucessfullySubmitShopDetail(response.getData(), response.getMessage());

            }

            @Override
            public void onError(Throwable e) {
                Intent intent = new Intent(activity, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                activity.startActivity(intent);
                /*if (e instanceof HttpException)
                    viewInterface.onFailToSubmitShopDetail(Utils.errorMessageParsing(e).getMessage());*/
            }

            @Override
            public void onComplete() {

            }
        };
    }

    private <T> Observable getObservableForShop(MProfile mProfile) {
        return APIClient.getClient(activity).create(APIInterface.class).updateShopDetail(new Session(activity).getString("AUTHORIZATION_KEY"),
                mProfile, true)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}

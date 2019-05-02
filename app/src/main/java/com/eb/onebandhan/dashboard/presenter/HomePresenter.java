package com.eb.onebandhan.dashboard.presenter;

import android.app.Activity;

import com.eb.onebandhan.apiCalling.APIClient;
import com.eb.onebandhan.apiCalling.APIInterface;
import com.eb.onebandhan.apiCalling.ResponseData;
import com.eb.onebandhan.auth.model.MCategory;
import com.eb.onebandhan.auth.model.MSignUp;
import com.eb.onebandhan.auth.model.MUser;
import com.eb.onebandhan.auth.presenterinterface.LoginPresenterInterface;
import com.eb.onebandhan.auth.viewinterface.LoginViewInterface;
import com.eb.onebandhan.dashboard.model.MBanner;
import com.eb.onebandhan.dashboard.presenterinterface.HomePresenterInterface;
import com.eb.onebandhan.dashboard.viewinterface.HomeViewInterface;
import com.eb.onebandhan.util.Constant;
import com.eb.onebandhan.util.Session;
import com.eb.onebandhan.util.Utils;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.adapter.rxjava2.HttpException;

public class HomePresenter implements HomePresenterInterface, Constant {
    private HomeViewInterface viewInterface;
    private Activity activity;

    public HomePresenter(HomeViewInterface viewInterface, Activity activity) {
        this.viewInterface = viewInterface;
        this.activity = activity;
    }

    public DisposableObserver<ResponseData<List<MBanner>>> getObserver() {
        return new DisposableObserver<ResponseData<List<MBanner>>>() {
            @Override
            public void onNext(ResponseData<List<MBanner>> response) {
                viewInterface.onSucessfullyGetBannerList(response.getData(), response.getMessage());
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof HttpException)
                    viewInterface.onFailToGetBannerList(Utils.errorMessageParsing(e).getMessage());
            }

            @Override
            public void onComplete() {

            }
        };
    }

    private <T> Observable getObservable() {
            return APIClient.getClient(activity).create(APIInterface.class).getAllBannerList(new Session(activity).getString(AUTHORIZATION_KEY)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
         }

    @Override
    public void getBannerListTask() {
        getObservable().subscribeWith(getObserver());
    }

    @Override
    public void getCategoryListTask(Map<String, String> map) {
        getObservableForCategory(map).subscribeWith(getObserverForCategory());
    }

    private DisposableObserver<ResponseData<List<MCategory>>> getObserverForCategory() {
        return new DisposableObserver<ResponseData<List<MCategory>>>() {
            @Override
            public void onNext(ResponseData<List<MCategory>> response) {
                viewInterface.onSucessfullyGetCategoryList(response.getData(),response.getMessage());
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof HttpException)
                    viewInterface.onFailToGetBannerList(Utils.errorMessageParsing(e).getMessage());
            }

            @Override
            public void onComplete() {

            }
        };
    }

    private <T> Observable getObservableForCategory(Map<String, String> map) {
        return APIClient.getClient(activity).create(APIInterface.class).getUserCategoryRelatedData(new Session(activity).getString(AUTHORIZATION_KEY),map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}

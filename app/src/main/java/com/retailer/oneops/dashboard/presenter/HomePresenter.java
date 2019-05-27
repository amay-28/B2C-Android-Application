package com.retailer.oneops.dashboard.presenter;

import android.app.Activity;

import com.retailer.oneops.apiCalling.APIClient;
import com.retailer.oneops.apiCalling.APIInterface;
import com.retailer.oneops.apiCalling.ResponseData;
import com.retailer.oneops.auth.model.MCategory;
import com.retailer.oneops.dashboard.model.MBanner;
import com.retailer.oneops.dashboard.model.MCollection;
import com.retailer.oneops.dashboard.presenterinterface.HomePresenterInterface;
import com.retailer.oneops.dashboard.viewinterface.HomeViewInterface;
import com.retailer.oneops.util.Constant;
import com.retailer.oneops.util.Session;
import com.retailer.oneops.util.Utils;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
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

    @Override
    public void getCollectionListTask(Map<String, String> map) {
        getObservableForCollection(map).subscribeWith(getObserverForCollection());
    }

    private DisposableObserver<ResponseData<List<MCollection>>> getObserverForCollection() {
        return new DisposableObserver<ResponseData<List<MCollection>>>() {
            @Override
            public void onNext(ResponseData<List<MCollection>> response) {
                viewInterface.onSucessfullyGetCollectionList(response.getData(), response.getMessage());
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof HttpException)
                    viewInterface.onFailToGetCollectionList(Utils.errorMessageParsing(e).getMessage());
            }

            @Override
            public void onComplete() {

            }
        };
    }

    private <T> Observable getObservableForCollection(Map<String, String> map) {
        return APIClient.getClient(activity).create(APIInterface.class).getCollectionData(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
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
                    viewInterface.onFailToGetCategoryList(Utils.errorMessageParsing(e).getMessage());
            }

            @Override
            public void onComplete() {

            }
        };
    }

    private <T> Observable getObservableForCategory(Map<String, String> map) {
        return APIClient.getClient(activity).create(APIInterface.class).getUserCategoryRelatedData(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}

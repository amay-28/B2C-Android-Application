package com.eb.onebandhan.product.presenter;

import android.app.Activity;

import com.eb.onebandhan.apiCalling.APIClient;
import com.eb.onebandhan.apiCalling.APIInterface;
import com.eb.onebandhan.apiCalling.ResponseData;
import com.eb.onebandhan.auth.model.MCategory;
import com.eb.onebandhan.product.presenterinterface.DialogPresenterInterface;
import com.eb.onebandhan.product.viewinterface.DialogViewInterface;
import com.eb.onebandhan.util.Constant;
import com.eb.onebandhan.util.Session;
import com.eb.onebandhan.util.Utils;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.adapter.rxjava2.HttpException;

public class DialogPresenter implements DialogPresenterInterface, Constant {
    private DialogViewInterface viewInterface;
    private Activity activity;

    public DialogPresenter(DialogViewInterface viewInterface, Activity activity) {
        this.viewInterface = viewInterface;
        this.activity = activity;
    }

    private DisposableObserver<ResponseData<List<MCategory>>> getObserverForCategory() {
        return new DisposableObserver<ResponseData<List<MCategory>>>() {
            @Override
            public void onNext(ResponseData<List<MCategory>> response) {
                viewInterface.onSuccessfullyGetCategoryList(response.getData(),response.getMessage());
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

    @Override
    public void getCategoryListTask(Map<String, String> map) {
        getObservableForCategory(map).subscribeWith(getObserverForCategory());
    }

}

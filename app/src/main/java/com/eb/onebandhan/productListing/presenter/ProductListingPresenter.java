package com.eb.onebandhan.productListing.presenter;

import android.app.Activity;
import android.util.Log;

import com.eb.onebandhan.apiCalling.APIClient;
import com.eb.onebandhan.apiCalling.APIInterface;
import com.eb.onebandhan.apiCalling.ResponseData;
import com.eb.onebandhan.productListing.model.MProduct;
import com.eb.onebandhan.productListing.presenterinterface.ProductListingPresenterInterface;
import com.eb.onebandhan.productListing.viewinterface.ProductListingViewInterface;
import com.eb.onebandhan.util.Constant;
import com.eb.onebandhan.util.Session;
import com.eb.onebandhan.util.Utils;

import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.adapter.rxjava2.HttpException;
import retrofit2.http.QueryMap;

public class ProductListingPresenter implements ProductListingPresenterInterface, Constant {
    private ProductListingViewInterface productListingViewInterface;
    private Activity activity;

    public ProductListingPresenter(ProductListingViewInterface productListingViewInterface, Activity activity) {
        this.productListingViewInterface = productListingViewInterface;
        this.activity = activity;
    }

    public DisposableObserver<ResponseData<List<MProduct>>> getObserver() {
        return new DisposableObserver<ResponseData<List<MProduct>>>() {
            @Override
            public void onNext(ResponseData<List<MProduct>> response) {
                productListingViewInterface.onSuccessfulProductListing(response.getData(), response.getMessage());
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof HttpException)
                    productListingViewInterface.onFailedProductListing(Utils.errorMessageParsing(e).getMessage());

            }

            @Override
            public void onComplete() {

            }
        };
    }

    @Override
    public void onProductListing(Map<String, String> map) {
        getObservable(map).subscribeWith(getObserver());
    }

    private Observable getObservable(Map<String, String> map) {
        return APIClient.getClient(activity).create(APIInterface.class).getAllProductList(new Session(activity).getString(AUTHORIZATION_KEY), map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

    }

}

package com.retailer.oneops.productListing.presenter;

import android.app.Activity;

import com.retailer.oneops.apiCalling.APIClient;
import com.retailer.oneops.apiCalling.APIInterface;
import com.retailer.oneops.apiCalling.ResponseData;
import com.retailer.oneops.productListing.model.MProduct;
import com.retailer.oneops.productListing.presenterinterface.ProductListingPresenterInterface;
import com.retailer.oneops.productListing.viewinterface.ProductListingViewInterface;
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

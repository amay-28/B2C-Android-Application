package com.retailer.oneops.checkout.presenter;

import android.app.Activity;

import com.retailer.oneops.apiCalling.APIClient;
import com.retailer.oneops.apiCalling.APIInterface;
import com.retailer.oneops.apiCalling.ResponseData;
import com.retailer.oneops.checkout.model.MCartDetail;
import com.retailer.oneops.checkout.presenterinterface.CheckoutPresenterInterface;
import com.retailer.oneops.checkout.viewinterface.CheckoutViewInterface;
import com.retailer.oneops.dashboard.presenterinterface.MyInventPresenterInterface;
import com.retailer.oneops.dashboard.viewinterface.MyInventViewInterface;
import com.retailer.oneops.myinventory.model.MInventory;
import com.retailer.oneops.productListing.model.MProduct;
import com.retailer.oneops.util.Constant;
import com.retailer.oneops.util.Session;
import com.retailer.oneops.util.Utils;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import retrofit2.adapter.rxjava2.HttpException;

public class CheckoutPresenter implements CheckoutPresenterInterface, Constant {
    private CheckoutViewInterface checkoutViewInterface;
    private Activity activity;

    public CheckoutPresenter(CheckoutViewInterface checkoutViewInterface, Activity activity) {
        this.checkoutViewInterface = checkoutViewInterface;
        this.activity = activity;
    }

    public DisposableObserver<ResponseData<MCartDetail>> getObserver() {
        return new DisposableObserver<ResponseData<MCartDetail>>() {
            @Override
            public void onNext(ResponseData<MCartDetail> response) {
                checkoutViewInterface.onSuccessfulCartDetails(response.getData(), response.getMessage());
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof HttpException)
                    checkoutViewInterface.onFailedListing(Utils.errorMessageParsing(e).getMessage());

            }

            @Override
            public void onComplete() {

            }
        };
    }

    public DisposableObserver<retrofit2.Response<String>> getDeleteObserver() {
        return new DisposableObserver<retrofit2.Response<String>>() {
            @Override
            public void onNext(Response<String> response) {
                checkoutViewInterface.onSuccessfulDeleteItem();
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof HttpException)
                    checkoutViewInterface.onFailedListing(Utils.errorMessageParsing(e).getMessage());
            }

            @Override
            public void onComplete() {

            }
        };
    }

    private Observable getCartDetailsObservable() {
        return APIClient.getClient(activity).create(APIInterface.class).getCartDetails(new Session(activity).getString(AUTHORIZATION_KEY)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    private Observable deleteObservable(int id) {
        return APIClient.getClient(activity).create(APIInterface.class).deleteCartItem(new Session(activity).getString(AUTHORIZATION_KEY), id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void getCartDetails() {
        getCartDetailsObservable().subscribeWith(getObserver());
    }

    @Override
    public void onDeleteProductItem(int id) {
        deleteObservable(id).subscribeWith(getDeleteObserver());
    }
}

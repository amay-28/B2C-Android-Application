package com.retailer.oneops.product.presenter;

import android.app.Activity;

import com.google.gson.JsonObject;
import com.retailer.oneops.apiCalling.APIClient;
import com.retailer.oneops.apiCalling.APIInterface;
import com.retailer.oneops.apiCalling.ResponseData;
import com.retailer.oneops.checkout.model.MCart;
import com.retailer.oneops.product.presenterinterface.ProductDetailPresenterInterface;
import com.retailer.oneops.product.viewinterface.ProductDetailViewInterface;
import com.retailer.oneops.productListing.model.MProduct;
import com.retailer.oneops.util.Constant;
import com.retailer.oneops.util.MyDialogProgress;
import com.retailer.oneops.util.Session;
import com.retailer.oneops.util.Utils;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import retrofit2.adapter.rxjava2.HttpException;

public class ProductDetailPresenter implements ProductDetailPresenterInterface, Constant {
    private ProductDetailViewInterface viewInterface;
    private Activity activity;

    public ProductDetailPresenter(ProductDetailViewInterface viewInterface, Activity activity) {
        this.viewInterface = viewInterface;
        this.activity = activity;
    }

    private DisposableObserver<Response<ResponseData<MProduct>>> getObserverToAddProduct() {
        return new DisposableObserver<Response<ResponseData<MProduct>>>() {
            @Override
            public void onNext(Response<ResponseData<MProduct>> response) {
                MyDialogProgress.close(activity);
                /*Gson gson = new Gson();
                String s = gson.toJson(response.getData());
                Type type = new TypeToken<MProduct>() {
                }.getType();
                MProduct mProduct = gson.fromJson(s, type);
*/
                if (response.body() != null)
                    viewInterface.onSuccessfullyGetDetail(response.body().getData(), response.message());

            }

            @Override
            public void onError(Throwable e) {
                MyDialogProgress.close(activity);
                if (e instanceof HttpException)
                    viewInterface.onFailToUpdate(Utils.errorMessageParsing(e).getMessage());
            }

            @Override
            public void onComplete() {

            }
        };
    }

    private DisposableObserver<Response<ResponseData<MCart>>> getObserverToAddCart() {
        return new DisposableObserver<Response<ResponseData<MCart>>>() {
            @Override
            public void onNext(Response<ResponseData<MCart>> response) {
                MyDialogProgress.close(activity);
                if (response.body() != null)
                    viewInterface.onSuccessfullyAddToCart(response.body().getData(), response.message());
            }

            @Override
            public void onError(Throwable e) {
                MyDialogProgress.close(activity);
                if (e instanceof HttpException)
                    viewInterface.onFailToUpdate(Utils.errorMessageParsing(e).getMessage());
            }

            @Override
            public void onComplete() {

            }
        };
    }

    public DisposableObserver<retrofit2.Response<String>> clearCartObserver() {
        return new DisposableObserver<retrofit2.Response<String>>() {
            @Override
            public void onNext(Response<String> response) {
                MyDialogProgress.close(activity);
                viewInterface.onSuccessfullyClearCart();
            }

            @Override
            public void onError(Throwable e) {
                MyDialogProgress.close(activity);
                if (e instanceof HttpException)
                    viewInterface.onFailToUpdate(Utils.errorMessageParsing(e).getMessage());
            }

            @Override
            public void onComplete() {

            }
        };
    }

    private <T> Observable getObservableProductDetail(int id) {
        return APIClient.getClient(activity).create(APIInterface.class).getProductDetail(new Session(activity).getString(AUTHORIZATION_KEY), id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    private <T> Observable getObservableAddCart(JsonObject jsonObject) {
        return APIClient.getClient(activity).create(APIInterface.class).addToCart(new Session(activity).getString(AUTHORIZATION_KEY), jsonObject).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    private <T> Observable getObservableClearCart() {
        return APIClient.getClient(activity).create(APIInterface.class).clearCart(new Session(activity).getString(AUTHORIZATION_KEY)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void getProductDetailTask(int id) {
        MyDialogProgress.open(activity);
        getObservableProductDetail(id).subscribeWith(getObserverToAddProduct());
    }

    @Override
    public void addToCartTask(JsonObject jsonObject) {
        if (!MyDialogProgress.isOpen(activity))
            MyDialogProgress.open(activity);
        getObservableAddCart(jsonObject).subscribeWith(getObserverToAddCart());
    }

    @Override
    public void clearCartTask() {
        MyDialogProgress.open(activity);
        getObservableClearCart().subscribeWith(clearCartObserver());
    }
}

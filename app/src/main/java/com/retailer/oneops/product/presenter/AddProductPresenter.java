package com.retailer.oneops.product.presenter;

import android.app.Activity;

import com.retailer.oneops.apiCalling.APIClient;
import com.retailer.oneops.apiCalling.APIInterface;
import com.retailer.oneops.apiCalling.ResponseData;
import com.retailer.oneops.product.model.MAddProduct;
import com.retailer.oneops.product.model.MImageServer;
import com.retailer.oneops.product.presenterinterface.AddProductPresenterInterface;
import com.retailer.oneops.product.viewinterface.AddProductViewInterface;
import com.retailer.oneops.util.Constant;
import com.retailer.oneops.util.Session;
import com.retailer.oneops.util.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import retrofit2.Response;
import retrofit2.adapter.rxjava2.HttpException;

public class AddProductPresenter implements AddProductPresenterInterface, Constant {
    private AddProductViewInterface viewInterface;
    private Activity activity;

    public AddProductPresenter(AddProductViewInterface viewInterface, Activity activity) {
        this.viewInterface = viewInterface;
        this.activity = activity;
    }

    private DisposableObserver<ResponseData<MAddProduct>> getObserverToAddProduct() {
        return new DisposableObserver<ResponseData<MAddProduct>>() {
            @Override
            public void onNext(ResponseData<MAddProduct> response) {
                Gson gson = new Gson();
                String s = gson.toJson(response.getData());
                Type type = new TypeToken<MAddProduct>() {
                }.getType();
                MAddProduct addProductModel = gson.fromJson(s, type);

                viewInterface.onSuccessfullyAddProduct(addProductModel, response.getMessage());

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

    public DisposableObserver<ResponseData<List<MImageServer>>> getObserverImage() {
        return new DisposableObserver<ResponseData<List<MImageServer>>>() {
            @Override
            public void onNext(ResponseData<List<MImageServer>> response) {
                try {
                    viewInterface.onSucessfullyUpdatedImage(response.getImage());
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

    private <T> Observable getObservableForAddProduct(MAddProduct mAddProduct) {
        return APIClient.getClient(activity).create(APIInterface.class).addProduct(new Session(activity).getString(AUTHORIZATION_KEY), mAddProduct).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    private <T> Observable getObservableForEditProduct(MAddProduct mAddProduct, int id) {
        return APIClient.getClient(activity).create(APIInterface.class).editProduct(new Session(activity).getString(AUTHORIZATION_KEY), mAddProduct, id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    private <T> Observable getObservableImage(List<MultipartBody.Part> files) {
        return APIClient.getClient(activity).create(APIInterface.class).uploadImages(new Session(activity).getString(AUTHORIZATION_KEY),
                files).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    private <T> Observable getDeleteInventoryObservable(int id) {
        return APIClient.getClient(activity).create(APIInterface.class)
                .deletePhysicalInventory(new Session(activity).getString(AUTHORIZATION_KEY), id)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void addProductTask(MAddProduct mAddProduct) {
        getObservableForAddProduct(mAddProduct).subscribeWith(getObserverToAddProduct());
    }

    @Override
    public void updateProductTask(MAddProduct mAddProduct, int id) {
        getObservableForEditProduct(mAddProduct, id).subscribeWith(getObserverToAddProduct());
    }

    @Override
    public void onUpdateImage(List<MultipartBody.Part> files) {
        getObservableImage(files).subscribeWith(getObserverImage());
    }

    /*@Override
    public void onDeleteInventoryItem(int inventoryId) {
        getDeleteInventoryObservable(inventoryId).subscribeWith(getDeleteObserver());
    }
*/
}

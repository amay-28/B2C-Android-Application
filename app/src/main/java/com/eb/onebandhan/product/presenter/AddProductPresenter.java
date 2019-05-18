package com.eb.onebandhan.product.presenter;

import android.app.Activity;

import com.eb.onebandhan.apiCalling.APIClient;
import com.eb.onebandhan.apiCalling.APIInterface;
import com.eb.onebandhan.apiCalling.ResponseData;
import com.eb.onebandhan.auth.model.MCategory;
import com.eb.onebandhan.product.model.MAddProduct;
import com.eb.onebandhan.product.model.MImage;
import com.eb.onebandhan.product.model.MImageServer;
import com.eb.onebandhan.product.presenterinterface.AddProductPresenterInterface;
import com.eb.onebandhan.product.presenterinterface.DialogPresenterInterface;
import com.eb.onebandhan.product.viewinterface.AddProductViewInterface;
import com.eb.onebandhan.product.viewinterface.DialogViewInterface;
import com.eb.onebandhan.util.Constant;
import com.eb.onebandhan.util.Session;
import com.eb.onebandhan.util.Utils;
import com.eb.onebandhan.util.WebService;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
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

    private <T> Observable getObservableImage(List<MultipartBody.Part> files) {
        return APIClient.getClient(activity).create(APIInterface.class).uploadImages(new Session(activity).getString(AUTHORIZATION_KEY),
                files).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void addProductTask(MAddProduct mAddProduct) {
        getObservableForAddProduct(mAddProduct).subscribeWith(getObserverToAddProduct());
    }

    @Override
    public void onUpdateImage(List<MultipartBody.Part> files) {
        getObservableImage(files).subscribeWith(getObserverImage());
    }

}

package com.retailer.oneops.myinventory.presenter;

import android.app.Activity;
import android.widget.Toast;

import com.retailer.oneops.apiCalling.APIClient;
import com.retailer.oneops.apiCalling.APIInterface;
import com.retailer.oneops.apiCalling.ResponseData;
import com.retailer.oneops.auth.model.MSignUp;
import com.retailer.oneops.auth.model.MUser;
import com.retailer.oneops.auth.presenterinterface.LoginPresenterInterface;
import com.retailer.oneops.auth.viewinterface.LoginViewInterface;
import com.retailer.oneops.myinventory.model.MInventory;
import com.retailer.oneops.myinventory.presenterinterface.AddToInventPresenterInterface;
import com.retailer.oneops.myinventory.viewinterface.AddToInventViewInterface;
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

public class AddToInventoryPresenter implements AddToInventPresenterInterface, Constant {
    private AddToInventViewInterface viewInterface;
    private Activity activity;

    public AddToInventoryPresenter(AddToInventViewInterface viewInterface, Activity activity) {
        this.viewInterface = viewInterface;
        this.activity = activity;
    }

    public DisposableObserver<Response<ResponseData<MInventory>>> getObserver() {
        return new DisposableObserver<Response<ResponseData<MInventory>>>() {
            @Override
            public void onNext(Response<ResponseData<MInventory>> response) {
                if (response.body() != null) {
                    viewInterface.onSuccessfullyAdd(response.body().getData(), response.body().getMessage());
                } else {
                    Toast.makeText(activity, Utils.getMessageFromErrorBody(response.errorBody()), Toast.LENGTH_LONG).show();
                }
            }


            @Override
            public void onError(Throwable e) {
                MyDialogProgress.close(activity);
                if (e instanceof HttpException)
                    viewInterface.onFailToAdd(Utils.errorMessageParsing(e).getMessage());
            }

            @Override
            public void onComplete() {

            }
        };
    }

    private <T> Observable getObservable(MInventory mInventory) {
        return APIClient.getClient(activity).create(APIInterface.class)
                .addToInventory(new Session(activity).getString(AUTHORIZATION_KEY),mInventory)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void performAddToInventoryTask(MInventory mInventory) {
        getObservable(mInventory).subscribeWith(getObserver());
    }
}

package com.retailer.oneops.auth.presenter;

import android.app.Activity;

import com.retailer.oneops.apiCalling.APIClient;
import com.retailer.oneops.apiCalling.APIInterface;
import com.retailer.oneops.apiCalling.ResponseData;
import com.retailer.oneops.auth.model.MSignUp;
import com.retailer.oneops.auth.model.MUser;
import com.retailer.oneops.auth.presenterinterface.SignUpPresenterInterface;
import com.retailer.oneops.auth.viewinterface.SignUpViewInterface;
import com.retailer.oneops.util.Utils;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.adapter.rxjava2.HttpException;

public class SignUpPresenter implements SignUpPresenterInterface {
    private SignUpViewInterface viewInterface;
    private Activity activity;

    public SignUpPresenter(SignUpViewInterface viewInterface,Activity activity) {
        this.viewInterface = viewInterface;
        this.activity = activity;
    }

    @Override
    public void performSignUpTask(MSignUp mSignUp) {
        getObservable(mSignUp).subscribeWith(getObserver());
    }

    public DisposableObserver<ResponseData> getObserver() {
        return new DisposableObserver<ResponseData>(){
            @Override
            public void onNext(ResponseData response) {
                viewInterface.onSucessfullySignUp((MUser) response.getData(),response.getMessage());
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof HttpException) viewInterface.onFailToSignUp(Utils.errorMessageParsing(e).getMessage());
            }

            @Override
            public void onComplete() {

            }
        };
    }

    private <T> Observable getObservable(MSignUp mSignUp) {
        return  APIClient.getClient(activity).create(APIInterface.class).signUp(mSignUp)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

}

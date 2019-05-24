package com.eb.onebandhan.auth.presenter;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.eb.onebandhan.apiCalling.APIClient;
import com.eb.onebandhan.apiCalling.APIInterface;
import com.eb.onebandhan.apiCalling.ResponseData;
import com.eb.onebandhan.auth.model.MSignUp;
import com.eb.onebandhan.auth.model.MUser;
import com.eb.onebandhan.auth.presenterinterface.LoginPresenterInterface;
import com.eb.onebandhan.auth.presenterinterface.SignUpPresenterInterface;
import com.eb.onebandhan.auth.viewinterface.LoginViewInterface;
import com.eb.onebandhan.auth.viewinterface.SignUpViewInterface;
import com.eb.onebandhan.util.Constant;
import com.eb.onebandhan.util.Session;
import com.eb.onebandhan.util.Utils;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import retrofit2.adapter.rxjava2.HttpException;

public class LoginPresenter implements LoginPresenterInterface, Constant {
    private LoginViewInterface viewInterface;
    private Activity activity;
    private String requestType = "";

    public LoginPresenter(LoginViewInterface viewInterface, Activity activity) {
        this.viewInterface = viewInterface;
        this.activity = activity;
    }

    public DisposableObserver<Response<ResponseData<MUser>>> getObserver() {
        return new DisposableObserver<Response<ResponseData<MUser>>>() {
            @Override
            public void onNext(Response<ResponseData<MUser>> response) {
                if (response.body() != null) {
                    if (requestType.equals(TYPE_LOGIN_ONLY)) {
                        new Session(activity).setString(IS_LOGIN, YES);
                        new Session(activity).setString(AUTHORIZATION_KEY, BEARER + response.headers().get("AuthToken"));
                        new Session(activity).setUserProfile(response.body().getData());
                    }
                    viewInterface.onSucessfullyLogin(response.body().getData(), response.body().getMessage());
                } else{

                    Toast.makeText(activity, Utils.getMessageFromErrorBody(response.errorBody()), Toast.LENGTH_SHORT).show();
                }

            }


            @Override
            public void onError(Throwable e) {
                if (e instanceof HttpException)
                    viewInterface.onFailToLogin(Utils.errorMessageParsing(e).getMessage());
            }

            @Override
            public void onComplete() {

            }
        };
    }

    private <T> Observable getObservable(MSignUp mSignUp, String requestType) {
        if (requestType.equals(TYPE_LOGIN_ONLY))
            return APIClient.getClient(activity).create(APIInterface.class).login(mSignUp).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        else
            return APIClient.getClient(activity).create(APIInterface.class).loginToSendOtp(mSignUp).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void performLoginTask(MSignUp mSignUp, String requestType) {
        this.requestType = requestType;
        getObservable(mSignUp, requestType).subscribeWith(getObserver());
    }
}

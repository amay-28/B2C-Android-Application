package com.retailer.oneops.auth.presenter;

import android.app.Activity;
import android.widget.Toast;

import com.retailer.oneops.apiCalling.APIClient;
import com.retailer.oneops.apiCalling.APIInterface;
import com.retailer.oneops.apiCalling.ResponseData;
import com.retailer.oneops.auth.model.MSignUp;
import com.retailer.oneops.auth.model.MUser;
import com.retailer.oneops.auth.presenterinterface.OtpPresenterInterface;
import com.retailer.oneops.auth.viewinterface.OtpViewInterface;
import com.retailer.oneops.util.MyDialogProgress;
import com.retailer.oneops.util.Session;
import com.retailer.oneops.util.Utils;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import retrofit2.adapter.rxjava2.HttpException;

import static com.retailer.oneops.util.Constant.AUTHORIZATION_KEY;
import static com.retailer.oneops.util.Constant.BEARER;

public class OtpPresenter implements OtpPresenterInterface {
    private OtpViewInterface viewInterface;
    private Activity activity;

    public OtpPresenter(OtpViewInterface viewInterface, Activity activity) {
        this.viewInterface = viewInterface;
        this.activity = activity;
    }

    public DisposableObserver<Response<ResponseData<MUser>>> getObserver() {
        return new DisposableObserver<Response<ResponseData<MUser>>>() {
            @Override
            public void onNext(Response<ResponseData<MUser>> value) {
                MyDialogProgress.close(activity);
                new Session(activity).setString(AUTHORIZATION_KEY, BEARER + value.headers().get("AuthToken"));
                if (value.body() != null)
                    viewInterface.onSucessfullyVerified(value.body().getMessage());
                else {
                    Toast.makeText(activity, Utils.getMessageFromErrorBody(value.errorBody()), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(Throwable e) {
                MyDialogProgress.close(activity);
                if (e instanceof HttpException){
                    viewInterface.onFailToVerified(Utils.errorMessageParsing(e).getMessage());
                }

            }

            @Override
            public void onComplete() {
                MyDialogProgress.close(activity);
            }
        };
    }

    private <T> Observable getObservable(MSignUp mSignUp) {
        return APIClient.getClient(activity).create(APIInterface.class).verifyUser(mSignUp)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void performOtpVerificationTask(MSignUp mSignUp) {
        getObservable(mSignUp).subscribeWith(getObserver());
    }
}

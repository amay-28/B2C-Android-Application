package com.eb.onebandhan.bankDetail.activity.presenter;

import android.app.Activity;
import android.util.Log;

import com.eb.onebandhan.apiCalling.APIClient;
import com.eb.onebandhan.apiCalling.APIInterface;
import com.eb.onebandhan.apiCalling.ResponseData;
import com.eb.onebandhan.auth.model.MUser;
import com.eb.onebandhan.bankDetail.activity.AddBankDetailActivity;
import com.eb.onebandhan.bankDetail.activity.model.MBankDetail;
import com.eb.onebandhan.bankDetail.activity.presenterinterface.BankDetailPresenterInterface;
import com.eb.onebandhan.bankDetail.activity.viewinterface.BankDetailViewInterface;
import com.eb.onebandhan.util.Session;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import static com.eb.onebandhan.util.Constant.AUTHORIZATION_KEY;

public class BankDetailPresenter implements BankDetailPresenterInterface {
    public BankDetailViewInterface bankDetailViewInterface;
    public Activity activity;

    public BankDetailPresenter(AddBankDetailActivity bankDetailViewInterface, Activity activity) {
        this.bankDetailViewInterface = bankDetailViewInterface;
        this.activity = activity;
    }

    public DisposableObserver<ResponseData<MBankDetail>> getObserver() {
        return new DisposableObserver<ResponseData<MBankDetail>>() {
            @Override
            public void onNext(ResponseData<MBankDetail> response) {
                Session session = new Session(activity);
                MUser mUser = session.getUserProfile();
                mUser.setBankDetails(response.getData());
                session.setUserProfile(mUser);
               // session.getUserProfile().getBankDetails();
                bankDetailViewInterface.onSuccessfulUploadBankDetails(response.getData(), response.getMessage());
                //session.setBankDetail(response.getData());
            }

            @Override
            public void onError(Throwable e) {
                //if (e instanceof HttpException) bankDetailViewInterface.onFailedUploadBankDetails (Utils.errorMessageParsing(e).getMessage(););
            }

            @Override
            public void onComplete() {

            }
        };
    }


    @Override
    public void uploadBankDetailTask(MBankDetail mBankDetail) {
        getObservable(mBankDetail).subscribeWith(getObserver());


    }

    private Observable getObservable(MBankDetail mBankDetail) {
        return APIClient.getClient(activity).create(APIInterface.class).getBankDetails(new Session(activity).getString(AUTHORIZATION_KEY), mBankDetail)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}

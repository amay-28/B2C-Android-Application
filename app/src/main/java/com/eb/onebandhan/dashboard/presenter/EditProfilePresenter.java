package com.eb.onebandhan.dashboard.presenter;

import android.app.Activity;

import com.eb.onebandhan.apiCalling.APIClient;
import com.eb.onebandhan.apiCalling.APIInterface;
import com.eb.onebandhan.apiCalling.ResponseData;
import com.eb.onebandhan.auth.model.MCategory;
import com.eb.onebandhan.auth.model.MProfile;
import com.eb.onebandhan.auth.model.MUser;
import com.eb.onebandhan.dashboard.activity.EditProfileActivity;
import com.eb.onebandhan.dashboard.model.MBanner;
import com.eb.onebandhan.dashboard.model.MCollection;
import com.eb.onebandhan.dashboard.presenterinterface.EditProfilePresenterInterface;
import com.eb.onebandhan.dashboard.presenterinterface.HomePresenterInterface;
import com.eb.onebandhan.dashboard.viewinterface.EditProfileViewInterface;
import com.eb.onebandhan.dashboard.viewinterface.HomeViewInterface;
import com.eb.onebandhan.util.Constant;
import com.eb.onebandhan.util.Session;
import com.eb.onebandhan.util.Utils;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.adapter.rxjava2.HttpException;

public class EditProfilePresenter implements EditProfilePresenterInterface, Constant {
    private EditProfileViewInterface viewInterface;
    private Activity activity;

    public EditProfilePresenter(EditProfileViewInterface viewInterface, Activity activity) {
        this.viewInterface = viewInterface;
        this.activity = activity;
    }

    public DisposableObserver<ResponseData<MUser>> getObserver() {
        return new DisposableObserver<ResponseData<MUser>>() {
            @Override
            public void onNext(ResponseData<MUser> response) {
                viewInterface.onSucessfullyUpdated(response.getData(), response.getMessage());
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

    private <T> Observable getObservable(MUser mUser) {
        return APIClient.getClient(activity).create(APIInterface.class).getAllBannerList(new Session(activity).getString(AUTHORIZATION_KEY)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void onUpdateProfile(MUser mUser) {
        getObservable(mUser).subscribeWith(getObserver());
    }
}

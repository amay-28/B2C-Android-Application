package com.eb.onebandhan.apiCalling;


import com.eb.onebandhan.auth.model.MCategory;
import com.eb.onebandhan.auth.model.MProfile;
import com.eb.onebandhan.auth.model.MSignUp;
import com.eb.onebandhan.auth.model.MUser;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface APIInterface {
    @POST("signup")
    Observable<ResponseData<MUser>> signUp(@Body MSignUp mSignUp);

    @POST("verify-otp")
    Observable<ResponseData> verifyUser(@Body MSignUp mSignUp);

    @POST("login")
    Observable<ResponseData<MUser>> login(@Body MSignUp mSignUp);

    @POST("login-send-otp")
    Observable<ResponseData<MUser>> loginToSendOtp(@Body MSignUp mSignUp);

    // here we are using dynamic url for fetching category
    @GET
    Observable<ResponseData<List<MCategory>>> getCategoryRelatedData(@Url String url, @QueryMap Map<String, String> map);

    @POST("update-profile")
    Observable<ResponseData<MUser>> updateShopDetail(@Body MProfile mProfile);
}
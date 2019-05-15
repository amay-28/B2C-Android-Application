package com.eb.onebandhan.apiCalling;


import com.eb.onebandhan.auth.model.MCategory;
import com.eb.onebandhan.auth.model.MProfile;
import com.eb.onebandhan.auth.model.MSignUp;
import com.eb.onebandhan.auth.model.MUser;
import com.eb.onebandhan.dashboard.model.MBanner;
import com.eb.onebandhan.dashboard.model.MCollection;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface APIInterface {
    @POST("retailer/signup")
    Observable<ResponseData<MUser>> signUp(@Body MSignUp mSignUp);

    @POST("retailer/verify-otp")
    Observable<ResponseData> verifyUser(@Body MSignUp mSignUp);

    @POST("retailer/login")
    Observable<Response<ResponseData<MUser>>> login(@Body MSignUp mSignUp);

    @POST("retailer/login-send-otp")
    Observable<Response<ResponseData<MUser>>> loginToSendOtp(@Body MSignUp mSignUp);

    // for withouth auth token
    @GET("categories")
    Observable<ResponseData<List<MCategory>>> getCategoryRelatedData(@QueryMap Map<String, String> map);

    @POST("retailer/update-profile")
    Observable<ResponseData<MUser>> updateShopDetail(@Body MProfile mProfile);

    @GET("banners")
    Observable<ResponseData<List<MBanner>>> getAllBannerList(@Header("Authorization") String token);

    // with auth token
    @GET("categories")
    Observable<ResponseData<List<MCategory>>> getUserCategoryRelatedData(@Header("Authorization") String token, @QueryMap Map<String, String> map);

    @GET("collections")
    Observable<ResponseData<List<MCollection>>> getCollectionData(@QueryMap Map<String, String> map);

    @POST("retailer/update-profile")
    Observable<ResponseData<MUser>> updateProfile(@Body MUser mUser);

}
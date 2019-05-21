package com.eb.onebandhan.apiCalling;


import com.eb.onebandhan.auth.model.MCategory;
import com.eb.onebandhan.auth.model.MProfile;
import com.eb.onebandhan.auth.model.MSignUp;
import com.eb.onebandhan.auth.model.MUser;
import com.eb.onebandhan.bankDetail.activity.model.MBankDetail;
import com.eb.onebandhan.dashboard.model.MBanner;
import com.eb.onebandhan.dashboard.model.MCollection;
import com.eb.onebandhan.product.model.MAddProduct;
import com.eb.onebandhan.product.model.MImage;
import com.eb.onebandhan.product.model.MImageServer;
import com.eb.onebandhan.productListing.model.MProduct;
import com.google.gson.JsonElement;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface APIInterface {
    @POST("retailer/signup")
    Observable<ResponseData<MUser>> signUp(@Body MSignUp mSignUp);

    @POST("retailer/verify-otp")
    Observable<Response<ResponseData<MUser>>> verifyUser(@Body MSignUp mSignUp);

    @POST("retailer/login")
    Observable<Response<ResponseData<MUser>>> login(@Body MSignUp mSignUp);

    @POST("retailer/login-send-otp")
    Observable<Response<ResponseData<MUser>>> loginToSendOtp(@Body MSignUp mSignUp);

    @POST("product")
    Observable<ResponseData> addProduct(@Header("Authorization") String token,
                                        @Body MAddProduct mAddProduct);

    // for withouth auth token
    @GET("categories")
    Observable<ResponseData<List<MCategory>>> getCategoryRelatedData(@QueryMap Map<String, String> map);

    @POST("retailer/update-profile")
    Observable<ResponseData<MUser>> updateShopDetail(@Header("Authorization") String token, @Body MProfile mProfile,@Query("isApp") boolean isApp);

    @GET("banners")
    Observable<ResponseData<List<MBanner>>> getAllBannerList(@Header("Authorization") String token);

    // with auth token
    @GET("categories")
    Observable<ResponseData<List<MCategory>>> getUserCategoryRelatedData(@Header("Authorization") String token,
                                                                         @QueryMap Map<String, String> map);

    @GET("collections")
    Observable<ResponseData<List<MCollection>>> getCollectionData(@QueryMap Map<String, String> map);

    @Multipart
    @POST("upload/file")
    Observable<ResponseData<JsonElement>> uploadImage(@Header("Authorization") String token,
                                                      @Part MultipartBody.Part file);

    @Multipart
    @POST("upload/files")
    Observable<ResponseData<List<MImageServer>>> uploadImages(@Header("Authorization") String token,
                                                              @Part List<MultipartBody.Part> files);


    @POST("retailer/update-profile")
    Observable<Response<ResponseData<MProfile>>> updateProfile(@Header("Authorization") String token,
                                                               @Body MProfile mProfile, @Query("isApp") boolean isApp);

    @POST("retailer/update-bank-details")
    Observable<ResponseData<MBankDetail>> getBankDetails(@Header("Authorization") String token,
                                                         @Body MBankDetail mBankDetail);

    @GET("products")
    Observable<ResponseData<List<MProduct>>> getAllProductList();

}
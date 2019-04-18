package com.eb.onebandhan.apiCalling;


import com.eb.onebandhan.auth.model.MSignUp;
import com.eb.onebandhan.auth.model.MUser;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APIInterface {
    @POST("signup")
    Observable<ResponseData<MUser>> signUp(@Body MSignUp mSignUp);

     @POST("verify-otp")
    Observable<ResponseData> verifyUser(@Body MSignUp mSignUp);

    @POST("login")
    Observable<ResponseData<MUser>> login(@Body MSignUp mSignUp);

    @POST("login-send-otp")
    Observable<ResponseData<MUser>> loginToSendOtp(@Body MSignUp mSignUp);

   /*@POST("update-profile")
    Observable<ResponseData<MUser>> loginToSendOtp(@Body MSignUp mSignUp);*/

}
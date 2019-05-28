package com.retailer.oneops.apiCalling;

import android.app.Activity;

import com.retailer.oneops.util.WebService;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.internal.JavaNetCookieJar;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {


    private static Retrofit retrofit = null;
//    private Activity activity;

    public static Retrofit getClient(Activity activity) {
        if (retrofit == null) {
            CookieManager cookieManager = new CookieManager();
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder oktHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(120, TimeUnit.MINUTES)
                    .writeTimeout(120, TimeUnit.MINUTES)
                    .readTimeout(120, TimeUnit.MINUTES)
                    .addInterceptor(new ConnectivityInterceptor(activity))
                    .cookieJar(new JavaNetCookieJar(cookieManager));

            oktHttpClient.addInterceptor(logging);

            retrofit = new Retrofit.Builder()
                    .baseUrl(WebService.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(oktHttpClient.build())
                    .build();
        }
        return retrofit;
    }
}
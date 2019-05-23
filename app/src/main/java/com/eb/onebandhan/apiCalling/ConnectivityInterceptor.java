package com.eb.onebandhan.apiCalling;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.eb.onebandhan.auth.activity.LoginActivity;
import com.eb.onebandhan.util.Session;
import com.eb.onebandhan.util.Utils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


public class ConnectivityInterceptor implements Interceptor {
    private Activity activity;


    public ConnectivityInterceptor(Activity context) {
        activity = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (!Utils.isConnectingToInternet(activity)) {
            throw new NoConnectivityException();
        } else {
            Request request = chain.request();
            Response response = chain.proceed(request);
            // have to check is it working or not
            // you can write other code here tooo
            if (response.code() == 500) {
                activity.runOnUiThread(() -> Toast.makeText(activity, "500 error", Toast.LENGTH_SHORT).show());
                return response;
            }
            if (response.code() == 412) {
                Toast.makeText(activity, response.message(), Toast.LENGTH_LONG).show();
                Intent in = new Intent(activity, LoginActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                (activity).startActivity(in);
                Session session = new Session(activity);
                session.setUserProfile(null);
            }
            /*Request.Builder builder = chain.request().newBuilder();
            return chain.proceed(builder.build());*/
            return response;
        }
    }

}

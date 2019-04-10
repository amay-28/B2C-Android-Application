package com.eb.onebandhan.apiCalling;

import android.app.Activity;
import android.widget.Toast;
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
            /*Request.Builder builder = chain.request().newBuilder();
            return chain.proceed(builder.build());*/
            return response;
        }
    }

}

package com.retailer.oneops.util;

import android.content.Context;
import android.widget.Toast;
import com.retailer.oneops.R;

/**
 * Created by Sumit Yadav on 26/2/19.
 */

public class ShowToast {
    public static void toastMsg(Context context, String msg) {
        if (msg != null || !msg.isEmpty())
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void toastNoConnection(Context context) {
        Toast.makeText(context, context.getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
    }

    public static void toastAuthFailed(Context context) {
        Toast.makeText(context, context.getString(R.string.error_auth_failed), Toast.LENGTH_SHORT).show();
    }

    public static void toastAuthCancelled(Context context) {
        Toast.makeText(context, context.getString(R.string.error_auth_cancelled), Toast.LENGTH_SHORT).show();
    }

    public static void toastWentWrongMsg(Context context) {
        Toast.makeText(context, context.getString(R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
    }

    public static void toastComingSoon(Context context) {
        Toast.makeText(context, context.getString(R.string.error_coming_soon), Toast.LENGTH_SHORT).show();
    }

}

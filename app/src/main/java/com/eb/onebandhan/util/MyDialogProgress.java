package com.eb.onebandhan.util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eb.onebandhan.R;

/**
 * Created by stuti on 3/12/15.
 */

public class MyDialogProgress {
    static Dialog dd = null;
    static ProgressDialog pDialog;

    public static Dialog open(Context context) {

        if (!isOpen(context)) {
            pDialog = new ProgressDialog(context, R.style.LoaderTheme);
            // pDialog.setMessage(context.getResources().getString(R.string.loading_text));
            //pDialog.setCancelable(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        return pDialog;
    }

    public static void close(Context context) {
        try {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isOpen(Context context) {
        if (dd != null && dd.isShowing()) {
            return true;
        }
        return false;
    }
}

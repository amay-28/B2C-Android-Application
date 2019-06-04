package com.retailer.oneops.util;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.retailer.oneops.R;
import com.retailer.oneops.auth.activity.LoginActivity;

import static com.retailer.oneops.util.Constant.IS_LOGIN;
import static com.retailer.oneops.util.Constant.NO;

public class DialogUtil {

    public static void showOkCancelDialog(Context context, String message, final OnDialogItemClickListener clickInAdapter) {
        final Dialog dialog = new Dialog(context, R.style.Dialog_No_Border);

        //Grab the window of the dialog, and change the width
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();

        lp.copyFrom(window.getAttributes());

        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        dialog.setContentView(R.layout.custom_ok_cancel_dialog);
        dialog.setCanceledOnTouchOutside(true);

        Button buttonPositive = (Button) dialog.findViewById(R.id.buttonPositive);
        Button buttonNegative = (Button) dialog.findViewById(R.id.buttonNegative);
        TextView textViewMessage = (TextView) dialog.findViewById(R.id.textViewMessage);

        textViewMessage.setText(message);
        buttonPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickInAdapter != null)
                    clickInAdapter.onDialogButtonClick(0, 0);
                else
                    sessionExpire(context);

                dialog.dismiss();

            }
        });

        buttonNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private static void sessionExpire(Context context) {
        Session session = new Session(context);
        session.setUserProfile(null);
        session.setString(IS_LOGIN, NO);

        Intent in = new Intent(context, LoginActivity.class);
        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        (context).startActivity(in);
    }


    @SuppressLint("NewApi")
    public static void showOkDialog(final String title, final String message, final Context context, final OnDialogItemClickListener clickInAdapter) {
        Dialog alertDialog = null;
        alertDialog = new Dialog(context, R.style.Dialog_No_Border);

        if (alertDialog != null && alertDialog.isShowing()) {
        } else {
            AlertDialog.Builder alertbox = new AlertDialog.Builder(context);
            alertbox.setMessage(message);
            alertbox.setTitle(title);
            alertbox.setCancelable(false);
            //	alertbox.setTitle(Gravity.CENTER);
            alertbox.setPositiveButton(context.getString(R.string.Ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    clickInAdapter.onDialogButtonClick(0, 0);
                    arg0.dismiss();
                }
            });


            alertDialog = alertbox.create();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            } else {
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }

            alertDialog.setCancelable(false);
            alertDialog.show();
        }
    }
}

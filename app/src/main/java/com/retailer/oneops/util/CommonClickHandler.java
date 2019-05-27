package com.retailer.oneops.util;

import android.app.Activity;
import android.view.View;

public class CommonClickHandler {
    private Activity activity;
    public CommonClickHandler(Activity activity) {
        this.activity=activity;
    }

    public void onCancelClick(View v) {
        Utils.hideKeyboard(activity);
        activity.onBackPressed();
    }
}

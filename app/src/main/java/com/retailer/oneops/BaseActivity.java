package com.retailer.oneops;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import androidx.appcompat.app.AppCompatActivity;
import com.retailer.oneops.util.MyDialogProgress;

/**
 * Created by Sumit Yadav on 15/10/18.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    public void finish() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        try {
            if (MyDialogProgress.isOpen(getApplicationContext()))
                MyDialogProgress.close(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.finish();
      //  overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


    public void onClicks(View view) {
       // if (mOnClickListener != null)
      //      mOnClickListener.onViewClick(view);
    }
}

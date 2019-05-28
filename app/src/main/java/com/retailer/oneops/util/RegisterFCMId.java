package com.retailer.oneops.util;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

public class RegisterFCMId extends AsyncTask<Void, Void, Void> {

    public Context context;
    public String fcm_id;
    public Session session;

    // Class constructor
    public RegisterFCMId(Context context) {
        this.context = context;
        session = new Session(context);
    }

    @Override
    protected Void doInBackground(Void... params) {
        fcm_id = FirebaseInstanceId.getInstance().getToken();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (fcm_id == null || fcm_id.equals("")) {
            new RegisterFCMId(context).execute();
        } else {
            session.setSharedPreference(context, Constant.FCMTOKENID, fcm_id);
            Log.d("token", fcm_id);
        }
        super.onPostExecute(aVoid);
    }
}

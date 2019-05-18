package com.eb.onebandhan.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.Toast;

import com.eb.onebandhan.apiCalling.ResponseData;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.adapter.rxjava2.HttpException;

public class Utils implements Constant {
    private static String newToken;

    public static void printHashKey(Activity activity) {

        try {
            PackageInfo info = activity.getPackageManager().getPackageInfo(
                    activity.getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    /*________________________________checking internet connection___________________________*/
    public static boolean isConnectingToInternet(Activity activity) {
        ConnectivityManager connectivity = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        if (info[i].isConnectedOrConnecting())
                            return true;
                    }
        }
        return false;
    }
   /* public static void userTokenExpire(Activity activity, String message) {
        Toast.makeText(activity, !TextUtils.isEmpty(message) ? "" : activity.getResources().getString(R.string.session_expire_msg), Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(() -> {
            new Session(activity).setString(IS_LOGIN, NO);
            activity.startActivity(new Intent(activity, WelcomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            activity.finish();
        }, 1500);

    }*/


    //    String request body
    public static RequestBody getStringRequestBody(String value) {
        MediaType MEDIA_TYPE_TEXT = MediaType.parse("text/plain");
        return RequestBody.create(MEDIA_TYPE_TEXT, value == null ? "" : value);
    }

    // File request body
    public static RequestBody getFileRequestBody(File file) {
        MediaType MEDIA_TYPE = MediaType.parse(getMimeType(file.getAbsolutePath()));
        return RequestBody.create(MEDIA_TYPE, file);
    }

    // Get mime type
    private static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    // File request body for part array
    public static MultipartBody.Part[] getFileRequestBodyPartmultiple(String key, List<File> FileArray) {

        MultipartBody.Part[] parts = new MultipartBody.Part[FileArray.size()];

        for (int i = 0; i < FileArray.size(); i++) {
            parts[i] = getFileRequestBody_part(key, FileArray.get(i));
        }

        return parts;
    }

    // File request body for part
    public static MultipartBody.Part getFileRequestBody_part(String key, File file) {
        RequestBody userPhotoRequestBody = RequestBody.create(MediaType.parse(getMimeType(file.getAbsolutePath())), file);
        MultipartBody.Part fileToUploadPart = MultipartBody.Part.createFormData(key, file.getName(), userPhotoRequestBody);
        return fileToUploadPart;
    }

    // File request multipart file only
    public static MultipartBody.Part getMultipartFile(String key, File file) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData(key, file.getName(), requestFile);
        return body;
    }

    public static String[] getSplitedString(String inputString, String splitKeyWord) {
        String[] separated = inputString.split(splitKeyWord);
        return separated;
    }
    /*___________________get date & time format according to the selected country________________*/

    public static String getFormatedDateTimeAccToCountry(Activity activity, String longValue) {
        try {

            long millisecond = Long.parseLong(longValue);
//            Session session = new Session(activity);
            String dateFormat;
            // or you already have long value of date, use this instead of milliseconds variable.
//        String dateString = DateFormat.format(dateFormat, new Date(millisecond)).toString();
//            if (session.getMongoUserProfile().getCtCountry().equalsIgnoreCase("in"))
            dateFormat = "dd/MM/yyyy";
//            else dateFormat = "MM/dd/yyyy";
            Date date = new Date(millisecond);
            SimpleDateFormat format = new SimpleDateFormat(dateFormat);
            format.setTimeZone(TimeZone.getDefault());
            String dateString = format.format(date);
            return dateString;
        } catch (Exception e) {
            e.printStackTrace();
            return longValue;
        }
    }

    //getting current date
    public static String getCurrentDateInString(Activity activity) {
        Calendar c = Calendar.getInstance();
        String dateTimeFormate;
        dateTimeFormate = "dd/MM/yyyy";
        SimpleDateFormat df = new SimpleDateFormat(dateTimeFormate);
        String formattedDate = df.format(c.getTime());

        return formattedDate;
    }

    /*_________________________________ caleder object ________________________________*/

    public static Calendar getCalenderobject(Activity activity, String dateTime) {
//        Session session = new Session(activity);
        Date date = null;
        String dateTimeFormate;
//        if (session.getMongoUserProfile().getCtCountry().equalsIgnoreCase("in"))
        dateTimeFormate = "dd/MM/yyyy";
//        else dateTimeFormate = "MM/dd/yyyy";
        SimpleDateFormat format = new SimpleDateFormat(dateTimeFormate);
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(format.parse(dateTime));
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(activity, "" + e, Toast.LENGTH_SHORT).show();
        }
        return calendar;
    }
    /*_________________________________ caleder object with format ________________________________*/

    public static Calendar getCalenderobjectWithFormat(Activity activity, String dateTime, String givenFormat) {
//        Session session = new Session(activity);
        Date date = null;
        String dateTimeFormate;
//        if (session.getMongoUserProfile().getCtCountry().equalsIgnoreCase("in"))
        dateTimeFormate = givenFormat;
//        else dateTimeFormate = "MM/dd/yyyy";
        SimpleDateFormat format = new SimpleDateFormat(dateTimeFormate);
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(format.parse(dateTime));
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(activity, "" + e, Toast.LENGTH_SHORT).show();
        }
        return calendar;
    }

    public static String getFormatedDateTime(String longValue, String dateFormat) {
        long millisecond = Long.parseLong(longValue);
        // or you already have long value of date, use this instead of milliseconds variable.
//        String dateString = DateFormat.format(dateFormat, new Date(millisecond)).toString();
        Date date = new Date(millisecond);
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        format.setTimeZone(TimeZone.getDefault());
        String dateString = format.format(date);
        return dateString;
    }

    /*______________________change one format of date to another using string params_________________________*/
    public static String getFromatedDateType(String dateString, String givenFormat, String requiredFormat) {
        DateFormat inputFormat = new SimpleDateFormat(givenFormat);
        DateFormat outputFormat = new SimpleDateFormat(requiredFormat);
        String inputDateStr = dateString;
        Date date = null;
        try {
            date = inputFormat.parse(inputDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String outputDateStr = outputFormat.format(date);
        return outputDateStr;
    }

    public static void openKeyboard(Activity activity, EditText editText) {
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public static ResponseData<String> errorMessageParsing(Throwable e) {
        try {
            return new Gson().fromJson(((HttpException) e).response().errorBody().string(), ResponseData.class);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    /*________________method to hide keyboard________________*/
    public static void hideKeyboard(Context context) {
        InputMethodManager inputManager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View v = ((Activity) context).getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    // Show Toast
    public static void ShowToast(Context context, String msg, int duration) {

        int gravity = Gravity.CENTER; // the position of toast
        int xOffset = 0; // horizontal offset from current gravity
        int yOffset = 0;

        Toast toast = Toast.makeText(context, msg, duration);
        toast.setGravity(gravity, xOffset, yOffset);
        toast.show();

    }

    // Uri compress method and return file
    public static File compressURI(Context context, Uri URI, String mili_second) {

        File imageFile = null;
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), URI);

            ByteArrayOutputStream datasecond = new ByteArrayOutputStream();

            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, datasecond);

            byte[] bitmapdata = datasecond.toByteArray();

            // write the bytes in file
            imageFile = new File(Environment.getExternalStorageDirectory() + File.separator + mili_second + "waitty_profile.jpeg");

            FileOutputStream fo = new FileOutputStream(imageFile);
            fo.write(bitmapdata);
            fo.close();

            return imageFile;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return imageFile;
    }

    // Uri compress method and return file
    public static File compressURIForUpload(Context context, Uri URI, String mili_second) {

        File imageFile = null;
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), URI);

            ByteArrayOutputStream datasecond = new ByteArrayOutputStream();

            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, datasecond);

            byte[] bitmapdata = datasecond.toByteArray();

            imageFile = new File(URI.getPath());
            FileOutputStream fo = new FileOutputStream(URI.getPath());
            fo.write(bitmapdata);
            fo.close();

            return imageFile;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return imageFile;
    }

    // Check value null and return
    public static String checkNull(String value) {
        return value == null ? "" : value;
    }
}

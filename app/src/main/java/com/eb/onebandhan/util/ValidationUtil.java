package com.eb.onebandhan.util;

import android.content.Context;
import android.widget.EditText;

import com.eb.onebandhan.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtil {
    public static boolean emailValidator(Context context, EditText password, String fieldName) {

        String email = password.getText().toString().trim();

        if (email.isEmpty()) {
            ShowToast.toastMsg(context,context.getString(R.string.error_please_enter, fieldName.toLowerCase()));
            return false;
        }

        if (email.startsWith(".") || email.startsWith("_") || email.contains("..") || email.contains("__")
                || email.contains("._") || email.contains("_.") || email.contains(".@") ||
                email.contains("_@") || email.contains("--") || email.contains(".-") || email.contains("-.") || email.contains("-@") || email.contains("@-")) {

            ShowToast.toastMsg(context,context.getString(R.string.error_please_enter_valid, fieldName.toLowerCase()));
            return false;
        }

        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9]+(\\.[_A-Za-z0-9]+)*@[-A-Za-z0-9]+(\\.[A-Za-z]{2,}){1,}$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);

        if (!matcher.matches()) {
            ShowToast.toastMsg(context,context.getString(R.string.error_please_enter_valid, fieldName.toLowerCase()));
            return false;
        }

        return true;
    }

    public static boolean passwordValidator(Context context, TextInputLayout inputLayoutMobile, TextInputEditText password, String fieldName) {
        String mPassword = password.getText().toString().trim();

        if (mPassword.isEmpty()) {
            inputLayoutMobile.setError(context.getString(R.string.error_please_enter, fieldName.toLowerCase()));
            return false;
        }

        if (mPassword.length() < 6) {
            inputLayoutMobile.setError(context.getString(R.string.error_password_short));
            return false;
        }

       /* Pattern pattern;
        Matcher matcher;
        // final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,}$";
        final String PASSWORD_PATTERN = "^(?=.*?[A-Za-z])(?=.*?[0-9]).{6,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(mPassword);
        boolean success = matcher.matches();

        if (!success) {
            inputLayoutMobile.setError(context.getString(R.string.error_valid_password, fieldName.toLowerCase()));
        }

        return success;*/
        return true;
    }


    public static boolean validateLastName(String lastName) {
        return lastName.matches("[a-zA-z]+([ '-][a-zA-Z]+)*");
    }

    public static boolean validateRC(String lastName) {
        return lastName.matches("^[A-Z]{2}[ -][0-9]{1,2}(?: [A-Z])?(?: [A-Z]*)? [0-9]{4}$");
    }

    public static boolean validateLicense(String lastName) {
        return lastName.matches("^[A-Z]{2}[-][0-9]{1,2}(?: [A-Z])?(?: [A-Z]*)? [0-9]{4}$");
    }

    public static boolean validateMobileNumber(Context context, TextInputLayout inputLayoutMobile, TextInputEditText number, String fieldName) {
        String mNumber = number.getText().toString().trim();

        if (mNumber.isEmpty()) {
            inputLayoutMobile.setError(context.getString(R.string.error_please_enter, fieldName.toLowerCase()));
            return false;
        }

        if (!mNumber.matches("^[6-9]\\d{9}$")) {
            inputLayoutMobile.setError(context.getString(R.string.error_valid, fieldName.toLowerCase()));
            return false;
        }

        return true;
    }


    public static boolean validateFirstName(Context context, TextInputLayout inputLayoutMobile, TextInputEditText number, String fieldName, int maxLength) {
        String mName = number.getText().toString().trim();

        if (mName.isEmpty()) {
            inputLayoutMobile.setError(context.getString(R.string.error_please_enter, fieldName.toLowerCase()));
            return false;
        }


        if (mName.length() < 3) {
            inputLayoutMobile.setError(context.getString(R.string.error_name_toshort, fieldName));
            return false;
        }

        if (!mName.matches("^[a-z A-Z]*")) {
            inputLayoutMobile.setError(context.getString(R.string.error_valid, fieldName.toLowerCase()));
            return false;
        }


        if (mName.length() > maxLength) {
            inputLayoutMobile.setError(context.getString(R.string.error_name_to_long, fieldName, maxLength));
            return false;
        }


        return true;
    }

    public static boolean validateField(Context context, TextInputLayout inputLayoutMobile, String mName, String fieldName, int maxLength) {


        if (mName.length() < 3) {
            inputLayoutMobile.setError(context.getString(R.string.error_name_toshort, fieldName));
            return false;
        }


        if (mName.length() > maxLength) {
            inputLayoutMobile.setError(context.getString(R.string.error_name_to_long, fieldName.toLowerCase(), maxLength));
            return false;
        }


        return true;
    }


    public static boolean validateCoordinate(String value) {
        String[] arrOfStr = value.split(" ");

        return arrOfStr[0].matches("^([0-8]?[0-9]|90)°([0-5]?[0-9]')?([0-5]?[0-9](.[0-9])?\")?([NEWSnews]{1})$");

    }


    public static boolean validateName(Context context, TextInputLayout inputLayoutMobile, TextInputEditText number, String fieldName, int minLength, int maxLength) {
        String mName = number.getText().toString().trim();
        fieldName = fieldName.toLowerCase();

        if (mName.isEmpty()) {
            inputLayoutMobile.setError(context.getString(R.string.error_please_enter, fieldName));
            return false;
        }


        if (mName.length() < minLength) {
            inputLayoutMobile.setError(capsFirstWord(context.getString(R.string.error_name_to_short, fieldName, minLength)));
            return false;
        }

      /*  if (!mName.matches("^[a-z A-Z]*")) {
            inputLayoutMobile.setError(context.getString(R.string.error_valid, fieldName.toLowerCase()));
            return false;
        }*/


        if (mName.length() > maxLength) {
            inputLayoutMobile.setError(capsFirstWord(context.getString(R.string.error_name_to_long, fieldName, maxLength)));
            return false;
        }

        return true;
    }

    public static String capsFirstWord(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public static boolean checkIfsc(Context context, TextInputLayout inputLayoutMobile, TextInputEditText number, String fieldName, int minLength, int maxLength) {
        String mName = number.getText().toString().trim();
        fieldName = fieldName.toLowerCase();
        if (!ValidationUtil.validateName(context, inputLayoutMobile, number, fieldName, 11, 11)) {
            return false;
        }

        if (!mName.matches("^[A-Za-z]{4}0[A-Z0-9a-z]{6}$")) {
            inputLayoutMobile.setError(context.getString(R.string.error_valid, fieldName.toLowerCase()));
            return false;
        }

        return true;
    }


    public static boolean validateField(Context context, TextInputEditText number, String fieldName, int minLength, int maxLength) {
        String mName = number.getText().toString().trim();
        fieldName = fieldName.toLowerCase();

        if (mName.isEmpty()) {
            ShowToast.toastMsg(context, context.getString(R.string.error_please_enter, fieldName));
            //  inputLayoutMobile.setError(context.getString(R.string.error_please_enter_, fieldName));
            return false;
        }


        if (mName.length() < minLength) {
            ShowToast.toastMsg(context, capsFirstWord(context.getString(R.string.error_name_to_short, fieldName, minLength)));
            //  inputLayoutMobile.setError(capsFirstWord(context.getString(R.string.error_name_to_short, fieldName, minLength)));
            return false;
        }


        if (mName.length() > maxLength) {
            ShowToast.toastMsg(context, capsFirstWord(context.getString(R.string.error_name_to_long, fieldName, minLength)));
            return false;
        }

        return true;
    }


    /*With Toast*/
    public static boolean emailValidatorWithToast(Context context, TextInputEditText password, String fieldName) {

        String email = password.getText().toString().trim();

        if (email.isEmpty()) {
            //  inputLayoutMobile.setError(context.getString(R.string.error_please_enter, fieldName.toLowerCase()));
            ShowToast.toastMsg(context, context.getString(R.string.error_please_enter, fieldName.toLowerCase()));
            return false;
        }

        if (email.startsWith(".") || email.startsWith("_") || email.contains("..") || email.contains("__")
                || email.contains("._") || email.contains("_.") || email.contains(".@") ||
                email.contains("_@") || email.contains("--") || email.contains(".-") || email.contains("-.") || email.contains("-@") || email.contains("@-")) {

            // inputLayoutMobile.setError(context.getString(R.string.error_please_enter_valid, fieldName.toLowerCase()));
            ShowToast.toastMsg(context, context.getString(R.string.error_please_enter_valid, fieldName.toLowerCase()));
            return false;
        }

        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9]+(\\.[_A-Za-z0-9]+)*@[-A-Za-z0-9]+(\\.[A-Za-z]{2,}){1,}$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);

        if (!matcher.matches()) {
            //  inputLayoutMobile.setError(context.getString(R.string.error_please_enter_valid, fieldName.toLowerCase()));
            ShowToast.toastMsg(context, context.getString(R.string.error_please_enter_valid, fieldName.toLowerCase()));
            return false;
        }

        return true;
    }

    public static boolean passwordValidatorWithToast(Context context, TextInputEditText password, String fieldName) {
        String mPassword = password.getText().toString().trim();

        if (mPassword.isEmpty()) {
            // inputLayoutMobile.setError(context.getString(R.string.error_please_enter, fieldName.toLowerCase()));
            ShowToast.toastMsg(context, context.getString(R.string.error_please_enter, fieldName.toLowerCase()));

            return false;
        }

        if (mPassword.length() < 6) {
            //  inputLayoutMobile.setError(context.getString(R.string.error_password_short));
            if (fieldName.equalsIgnoreCase(context.getString(R.string.password)))
                ShowToast.toastMsg(context, context.getString(R.string.error_password_short));
            else
                ShowToast.toastMsg(context, context.getString(R.string.error_confirm_password_short));
            return false;
        }

       /* Pattern pattern;
        Matcher matcher;
        // final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,}$";
        final String PASSWORD_PATTERN = "^(?=.*?[A-Za-z])(?=.*?[0-9]).{6,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(mPassword);
        boolean success = matcher.matches();

        if (!success) {
            inputLayoutMobile.setError(context.getString(R.string.error_valid_password, fieldName.toLowerCase()));
        }

        return success;*/
        return true;
    }

    public static boolean validateNameWithToast(Context context, TextInputEditText number, String fieldName, int minLength, int maxLength) {
        String mName = number.getText().toString().trim();
        fieldName = fieldName.toLowerCase();

        if (mName.isEmpty()) {
            // inputLayoutMobile.setError(context.getString(R.string.error_please_enter, fieldName));
            ShowToast.toastMsg(context, context.getString(R.string.error_please_enter, fieldName));
            return false;
        }


        if (mName.length() < minLength) {
            // inputLayoutMobile.setError(capsFirstWord(context.getString(R.string.error_name_to_short, fieldName, minLength)));
            ShowToast.toastMsg(context, capsFirstWord(context.getString(R.string.error_name_to_short, fieldName, minLength)));
            return false;
        }

      /*  if (!mName.matches("^[a-z A-Z]*")) {
            inputLayoutMobile.setError(context.getString(R.string.error_valid, fieldName.toLowerCase()));
            return false;
        }*/


        if (mName.length() > maxLength) {
            // inputLayoutMobile.setError(capsFirstWord(context.getString(R.string.error_name_to_long, fieldName, maxLength)));
            ShowToast.toastMsg(context, capsFirstWord(context.getString(R.string.error_name_to_long, fieldName, maxLength)));
            return false;
        }

        return true;
    }

    public static boolean isValidGST(String gst) {
        boolean isValidGst = false;
        String reggst = "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[0-9]{1}Z[0-9A-Z]{1}?$";
        CharSequence inputStr = gst;
        Pattern pattern = Pattern.compile(reggst, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValidGst = true;
        }
        return isValidGst;
    }

    public static boolean isValidPAN(String pan) {
        boolean isValidGst = false;
        String reggst = "[A-Z]{5}[0-9]{4}[A-Z]{1}";
        CharSequence inputStr = pan;
        Pattern pattern = Pattern.compile(reggst, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValidGst = true;
        }
        return isValidGst;
    }

}

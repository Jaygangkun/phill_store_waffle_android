package com.edelivery.store.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Settings;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.LayoutDirection;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.Toast;

import com.edelivery.store.component.CustomAlterDialog;
import com.edelivery.store.component.CustomCircularProgressView;
import com.edelivery.store.models.datamodel.Languages;
import com.edelivery.store.models.singleton.Language;
import com.elluminati.edelivery.store.BuildConfig;
import com.elluminati.edelivery.store.R;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;

/**
 * An Utilities class on 30-01-2017.
 */

public class Utilities {

    public static final boolean isDebug = BuildConfig.DEBUG;
    public static final String TAG = "Utils";
    private static CustomAlterDialog customAlterDialog;
    private static Dialog progressDialog;
    private static Dialog dialog;
    private static CustomCircularProgressView ivProgressBar, ivProgressBar2;

    public static void printLog(String tag, String msg) {
        if (isDebug) {
            android.util.Log.i(tag, msg + "");
        }
    }


    public static void handleException(String tag, Exception e) {
        if (isDebug) {
            android.util.Log.d(tag, e + "");
        }
    }

    public static final void handleThrowable(String tag, Throwable t) {
        if (isDebug) {
            if (t != null) {
                android.util.Log.d(tag, t + "");
            }
        }
    }

    public static void showToast(Context ctx, String msg) {

        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showHttpErrorToast(int code, Context context) {
        String msg;
        String errorCode = Constant.HTTP_ERROR_CODE_PREFIX + code;
        try {
            msg = context.getResources().getString(
                    context.getResources().getIdentifier(errorCode, Constant.STRING,
                            context.getPackageName()));

        } catch (Resources.NotFoundException e) {
            msg = errorCode;
            Utilities.printLog(TAG, msg);
            Utilities.handleException(TAG, e);
        }
        showToast(context, msg);

    }

    public static void closeKeyboard(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showProgressDialog(Context context) {
        if (progressDialog == null && checkInternet(context) && !((AppCompatActivity) context)
                .isFinishing()) {
            progressDialog = new Dialog(context, R.style.AppTheme);
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat
                    .getColor(context, android.R.color.transparent)));
            progressDialog.setContentView(R.layout.circuler_progerss_bar_two);
            progressDialog.setCancelable(false);

            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(progressDialog.getWindow().getAttributes());
            layoutParams.width = (WindowManager.LayoutParams.MATCH_PARENT);
            layoutParams.height = (WindowManager.LayoutParams.MATCH_PARENT);
            progressDialog.getWindow().setAttributes(layoutParams);

            ivProgressBar2 = (CustomCircularProgressView) progressDialog.findViewById(R.id
                    .ivProgressBarTwo);
            ivProgressBar2.startAnimation();
            progressDialog.show();

        }
    }

    public static void showCustomProgressDialog(Context context, boolean isCancel) {
        if (dialog != null && dialog.isShowing()) {
            return;
        }
        if (checkInternet(context) && !((AppCompatActivity) context).isFinishing()) {
            dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.circuler_progerss_bar_two);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            ivProgressBar = (CustomCircularProgressView) dialog.findViewById(R.id.ivProgressBarTwo);
            ivProgressBar.startAnimation();
            dialog.setCancelable(isCancel);
            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.height = WindowManager.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setAttributes(params);
            dialog.getWindow().setDimAmount(0);
            dialog.show();
        }
    }

    public static void hideCustomProgressDialog() {
        try {
            if (dialog != null && ivProgressBar != null) {
                dialog.dismiss();
                dialog = null;
            }
        } catch (Exception e) {
            Utilities.handleException(TAG, e);
        }
    }

    public static void removeProgressDialog() {
        try {
            if (progressDialog != null) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        } catch (Exception e) {
            Utilities.handleException("Exception", e);
        }
    }

    public static void showInternetDialog(final Activity context) {

        if (customAlterDialog != null && customAlterDialog.isShowing()) {
            return;
        }
        if (!context.isFinishing()) {
            customAlterDialog = new CustomAlterDialog(context, context.getResources()
                    .getString(R.string.text_internet), context.getResources().getString(R.string
                    .text_no_internet), true, context.getResources().getString(R
                    .string.text_ok), context.getResources().getString(R.string.text_cancel)) {
                @Override
                public void btnOnClick(int btnId) {
                    if (btnId == R.id.btnPositive) {
                        Intent intent = new Intent(
                                Settings.ACTION_SETTINGS);
                        context.startActivity(intent);
                    } else {
                        context.finishAffinity();
                        removeInternetDialog();
                    }

                }
            };
            customAlterDialog.setCancelable(false);
            customAlterDialog.show();
        }


    }

    public static void removeInternetDialog() {
        if (customAlterDialog != null && customAlterDialog.isShowing()) {
            customAlterDialog.dismiss();
            customAlterDialog = null;
        }
    }


    public static String setStausCode(Context context, String prefix, int index, boolean isOrderChange) {
        try {
            if(isOrderChange){
                return context.getResources().getString(R.string.msg_wait_for_orde_confirmation);
            }
            return context.getResources().getString(context.getResources().getIdentifier(prefix +
                    index, "string", context.getPackageName()));

        } catch (Exception e) {
            Utilities.printLog("tag", "Exception - Prasing Status Code" + e);
        }
        return null;
    }

    public static String getDayOfMonthSuffix(final int n) {
        if (n >= 11 && n <= 13) {
            return n + "th";
        }
        switch (n % 10) {
            case 1:
                return n + "st";
            case 2:
                return n + "nd";
            case 3:
                return n + "rd";
            default:
                return n + "th";
        }
    }

    public static boolean checkInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService
                (Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();

    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap
            // will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable
                    .getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static String minuteToHoursMinutesSeconds(double minute) {
        long seconds = (long) (minute * 60);
        return (seconds / 3600) + " : " + (seconds % 3600) / 60;
    }

    public static void hideSoftKeyboard(AppCompatActivity activity) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) activity.getSystemService(
                            Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static void setTagBackgroundRtlView(Context context, View view) {
        if (context.getResources().getConfiguration().getLayoutDirection() == LayoutDirection.RTL) {
            view.setBackground(AppCompatResources.getDrawable(context, R.drawable
                    .background_item_section_rtl));
        } else {
            view.setBackground(AppCompatResources.getDrawable(context, R.drawable
                    .background_item_section));
        }

    }

    public static boolean checkIMGSize(Uri uri, int maxWidth, int maxHeight, int minWidth, int
            minHeight, double ratio) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(uri.getPath(), options);
        double imageHeight = options.outHeight;
        double imageWidth = options.outWidth;
        Log.i("IMG_HEIGHT", imageHeight + "");
        Log.i("IMG_WIDTH", imageWidth + "");
        double ratio1 = imageWidth / imageHeight;
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        Log.i("IMG_RATIO", decimalFormat.format(ratio1));
        return imageHeight <= maxHeight && imageHeight >=
                minHeight && imageWidth <= maxWidth && imageWidth >= minWidth
                && Double.valueOf(decimalFormat.format(ratio1)) == ratio;

    }

    public static boolean checkImageFileType(Context context, List<String> fileTypes, Uri
            uri) {

        String selectedFileType;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            selectedFileType = MimeTypeMap.getSingleton().getExtensionFromMimeType(context
                    .getContentResolver().getType
                            (uri));
        } else {
            selectedFileType = uri.toString();
            selectedFileType = selectedFileType.substring
                    (selectedFileType.lastIndexOf(".") + 1).toUpperCase();
        }

        Utilities.printLog("IMAGE_FILE_TYPE_SELECTED", selectedFileType);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Please select ");
        for (String type : fileTypes) {
            String s = type.substring
                    (type.lastIndexOf("/") + 1).toUpperCase();
            stringBuilder.append(s);
            stringBuilder.append(" , ");
            Utilities.printLog("IMAGE_FILE_TYPE", s);
            selectedFileType.trim();
            s.trim();
            if (selectedFileType.equalsIgnoreCase(s)) {
                return true;
            }
        }
        stringBuilder.append("image file type.");
        Utilities.showToast(context, stringBuilder.toString());
        return false;
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html) {
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    public static boolean isDecimalAndGraterThenZero(String data) {
        try {
            if (Double.valueOf(data) <= 0) {

                return false;
            }
        } catch (NumberFormatException e) {

            return false;
        }
        return true;
    }

    public static boolean hasAnyPrefix(String number, String... prefixes) {
        if (number == null) {
            return false;
        }
        for (String prefix : prefixes) {
            if (number.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isBlank(String value) {
        return value == null || value.trim().length() == 0;
    }

    public static String generateRandomString() {
        char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
                .toCharArray();
        StringBuilder sb = new StringBuilder(20);
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }

    public static int dipToPx(Context c, float dipValue) {
        DisplayMetrics metrics = c.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }

    public static double roundDecimal(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();

    }

    public static void openWebPage(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, URLUtil.isValidUrl(url) ? Uri.parse(url) :
                Uri.parse("http://" + url));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    /**
     * This method is used for convert bitmap to base64 format.
     */
    public static String convertImageToBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.URL_SAFE);

        return encodedImage;
    }

    public static int getLangIndxex(String language, List<Languages> langs, boolean isCheckVisibility){
        int lang = 0;
        if(langs != null && !langs.isEmpty() ) {
            for (int j = 0; j < langs.size(); j++) {
                if(isCheckVisibility){
                    if (TextUtils.equals(language, langs.
                            get(j).getCode()) && langs.
                            get(j).isVisible()) {
                        lang = j;
                        break;
                    }
                }else {
                    if (TextUtils.equals(language, langs.
                            get(j).getCode())) {
                        lang = j;
                        break;
                    }
                }
            }
        }
        return lang;
    }

    public static String getDetailStringFromList(List<String> detailList, int index){

        if(detailList != null){
            if(index < detailList.size() && detailList.get(index) != null){
                return detailList.get(index);
            }else if(!detailList.isEmpty()  && detailList.get(0) != null) {
                return detailList.get(0);
            }else {
                return "";
            }
        }else {
            return "";
        }
    }
}

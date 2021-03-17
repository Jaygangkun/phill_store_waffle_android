package com.edelivery.store.parse;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.edelivery.store.utils.Constant;
import com.edelivery.store.utils.PreferenceHelper;
import com.edelivery.store.utils.Utilities;
import com.elluminati.edelivery.store.BuildConfig;
import com.elluminati.edelivery.store.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.elluminati.edelivery.store.BuildConfig.BASE_URL;

/**
 * Created by Elluminati 4/3/2017.
 */

public class ApiClient {


    public static final String Tag = "ApiClient";
    private static final int CONNECTION_TIMEOUT = 30; //seconds
    private static final int READ_TIMEOUT = 20; //seconds
    private static final int WRITE_TIMEOUT = 20; //seconds
    private static MediaType MEDIA_TYPE_TEXT = MediaType.parse("text/plain");
    private static MediaType MEDIA_TYPE_IMAGE = MediaType.parse("image/*");
    private static Retrofit retrofit = null;
    private static Retrofit sagpay_retrofit = null;
    private static Gson gson;
    private static String lang;
    private static String storeId;
    private static String subStoreId;
    private static String serverToken;

    public static Retrofit getClient() {
        if (retrofit == null) {
            OkHttpClient.Builder okHttpClient;
            okHttpClient = new OkHttpClient().newBuilder().connectTimeout
                    (CONNECTION_TIMEOUT,
                            TimeUnit.SECONDS)
                    .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS).writeTimeout(WRITE_TIMEOUT,
                            TimeUnit.SECONDS);
            okHttpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request =
                            chain.request().newBuilder()
                                    .addHeader(Constant.LANG, lang)
                                    .addHeader("storeid", TextUtils.isEmpty(storeId)
                                            ? "" : storeId)
                                    .addHeader("token",
                                            TextUtils.isEmpty(serverToken) ? "" : serverToken)
                                    .addHeader(Constant.TYPE, TextUtils.isEmpty(subStoreId) ?
                                            "0" : "1")
                                    .addHeader("id", TextUtils.isEmpty(subStoreId) ? "" :
                                            subStoreId)
                                    .build();
                    return chain.proceed(request);
                }
            });

            if (BuildConfig.DEBUG) {
                // development build
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                okHttpClient.addInterceptor(interceptor);
            }

            retrofit = new Retrofit.Builder()
                    .client(okHttpClient.build())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .build();
        }
        return retrofit;


    }

    public Retrofit sagpayApi(Context ctx,String sessionKey) {

        PreferenceHelper preferenceHelper = PreferenceHelper.getPreferenceHelper(ctx);
        OkHttpClient.Builder okHttpClient;
        okHttpClient = new OkHttpClient().newBuilder().connectTimeout(CONNECTION_TIMEOUT,
                TimeUnit.SECONDS).readTimeout(READ_TIMEOUT, TimeUnit.SECONDS).writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);
        okHttpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                String auth_string = "";
                if(sessionKey.trim().isEmpty()){

                    auth_string = Credentials.basic(preferenceHelper.getSagpay_Integration_Key(),
                            preferenceHelper.getSagpayIntegration_Password());
                }else {

                    auth_string = "Bearer "+sessionKey;
                }
                Request request = chain.request().newBuilder().addHeader("Authorization",  auth_string).build();

                Utilities.printLog("AUTH_STRING", auth_string);
                return chain.proceed(request);
            }
        });

        if (BuildConfig.DEBUG) {
            // development build
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpClient.addInterceptor(interceptor);
        }

        sagpay_retrofit = new Retrofit.Builder()
                .client(okHttpClient.build())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(preferenceHelper.getSagpay_baseurl())
                .build();

        return sagpay_retrofit;
    }


    @NonNull
    public static MultipartBody.Part makeMultipartRequestBody(Context context, String
            photoPath, String partName) {
        try {
            File file = new File(photoPath);
            RequestBody requestFile = RequestBody.create(MEDIA_TYPE_IMAGE, file);
            return MultipartBody.Part.createFormData(partName, context.getResources().getString(R
                            .string
                            .app_name),
                    requestFile);
        } catch (NullPointerException e) {
            Utilities.handleException(Tag, e);
            return null;
        }

    }

    @NonNull
    public static RequestBody makeTextRequestBody(Object stringData) {
        return RequestBody.create(MEDIA_TYPE_TEXT, String.valueOf(stringData));
    }

    @NonNull
    public static RequestBody makeGSONRequestBody(Object jsonObject) {
        if (gson == null) {
            gson = new Gson();
        }
        return RequestBody.create(MEDIA_TYPE_TEXT, gson.toJson(jsonObject));
    }

    @NonNull
    public static String JSONResponse(Object jsonObject) {
        if (gson == null) {
            gson = new Gson();
        }
        return gson.toJson(jsonObject);
    }

    @NonNull
    public static RequestBody makeJSONRequestBody(JSONObject jsonObject) {
        String params = jsonObject.toString();
        return RequestBody.create(MEDIA_TYPE_TEXT, params);
    }

    public static MultipartBody.Part[] addMultipleImage(ArrayList<String> imageList) {
        MultipartBody.Part[] partsImage = new MultipartBody.Part[imageList.size()];
        for (int i = 0; i < imageList.size(); i++) {
            File file = new File(imageList.get(i));

            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),
                    file);
            partsImage[i] = MultipartBody.Part.createFormData(Constant.IMAGE_URL, file.getName(),
                    requestBody);
        }
        return partsImage;
    }

    private static String getRealPathFromURI(Uri contentURI, Context context) {
        String result;
        Cursor cursor = context.getContentResolver().query(contentURI, null,
                null, null, null);

        if (cursor == null) { // Source is Dropbox or other similar local file
            // path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            try {
                int idx = cursor
                        .getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                result = cursor.getString(idx);
            } catch (Exception e) {

                Utilities.handleException(Tag, e);

                result = "";
            }
            cursor.close();
        }
        return result;
    }

    public static File getFromMediaUri(Context context, ContentResolver resolver, Uri uri) {
        String SCHEME_FILE = "file";
        String SCHEME_CONTENT = "content";
        if (uri == null)
            return null;

        if (SCHEME_FILE.equals(uri.getScheme())) {
            return new File(uri.getPath());
        } else if (SCHEME_CONTENT.equals(uri.getScheme())) {
            final String[] filePathColumn = {MediaStore.MediaColumns.DATA, MediaStore
                    .MediaColumns.DISPLAY_NAME};
            Cursor cursor = null;
            try {
                cursor = resolver.query(uri, filePathColumn, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    final int columnIndex = (uri.toString().startsWith("content://com.google" +
                            ".android.gallery3d")) ?
                            cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME) :
                            cursor.getColumnIndex(MediaStore.MediaColumns.DATA);
                    // Picasa images on API 13+
                    if (columnIndex != -1) {
                        String filePath = cursor.getString(columnIndex);
                        if (!TextUtils.isEmpty(filePath)) {
                            return new File(filePath);
                        }
                    }
                }
            } catch (IllegalArgumentException e) {
                // Google Drive images
            } catch (SecurityException ignored) {
                // Nothing we can do
            } finally {
                if (cursor != null) cursor.close();
            }
        }
        return null;
    }

    public static JSONArray JSONArray(Object jsonObject) {
        if (gson == null) {
            gson = new Gson();
        }
        try {
            return new JSONArray(String.valueOf(gson.toJsonTree(jsonObject)
                    .getAsJsonArray()));
        } catch (JSONException e) {
            Utilities.handleException(Tag, e);
        }
        return null;
    }

    public static JSONObject JSONObject(Object jsonObject) {
        if (gson == null) {
            gson = new Gson();
        }
        try {
            return new JSONObject(String.valueOf(gson.toJsonTree(jsonObject)
                    .getAsJsonObject()));
        } catch (JSONException e) {
            Utilities.handleException(Tag, e);
        }
        return null;
    }

    public Retrofit changeApiBaseUrl(String newApiBaseUrl) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().connectTimeout
                (CONNECTION_TIMEOUT,
                        TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS).writeTimeout(WRITE_TIMEOUT,
                        TimeUnit.SECONDS).addInterceptor(interceptor).build();

        return new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(newApiBaseUrl).build();
    }

    public static void setLanguage(String lang) {
        ApiClient.lang = lang;
        retrofit = null;
    }

    public static void setStoreId(String storeId) {
        ApiClient.storeId = storeId;
        retrofit = null;
    }

    public static void setServerToken(String serverToken) {
        ApiClient.serverToken = serverToken;
        retrofit = null;
    }

    public static void setSubStoreId(String subStoreId) {
        ApiClient.subStoreId = subStoreId;
        retrofit = null;
    }
}

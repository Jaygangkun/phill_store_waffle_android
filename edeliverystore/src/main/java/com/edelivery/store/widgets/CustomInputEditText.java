package com.edelivery.store.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import com.google.android.material.textfield.TextInputEditText;
import android.util.AttributeSet;

import com.edelivery.store.utils.Utilities;
import com.elluminati.edelivery.store.R;

/**
 * 19-10-2016.
 */
public class CustomInputEditText extends TextInputEditText {

    private Typeface typeface;
    public static final String TAG = "CustomInputEditText";

    public CustomInputEditText(Context context) {
        super(context);
    }

    public CustomInputEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    public CustomInputEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context, attrs);
    }

    private void setCustomFont(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.app);
        String customFont = a.getString(R.styleable.app_customFont);
        setCustomFont(ctx, customFont);
        a.recycle();
    }

    private boolean setCustomFont(Context context, String asset) {
        try {
            if (typeface == null) {
                // Log.i(TAG, "asset:: " + "fonts/" + asset);
                typeface = Typeface.createFromAsset(context.getAssets(),
                        "fonts/ClanPro-News.otf");
            }

        } catch (Exception e) {
            Utilities.handleException(TAG, e);
            // Log.e(TAG, "Could not get typeface: " + e.getMessage());
            return false;
        }

        setTypeface(typeface);
        return true;
    }
}

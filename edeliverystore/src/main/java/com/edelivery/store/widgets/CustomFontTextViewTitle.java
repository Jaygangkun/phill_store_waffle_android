package com.edelivery.store.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.edelivery.store.utils.Utilities;
import com.elluminati.edelivery.store.R;


/**
 * @author Elluminati elluminati.in
 */
public class CustomFontTextViewTitle extends TextView {

//	private static final String TAG = "TextView";

    private Typeface typeface;
    public static final String TAG = "CustomFontTextViewTitle";

    public CustomFontTextViewTitle(Context context) {
        super(context);
    }

    public CustomFontTextViewTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    public CustomFontTextViewTitle(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context, attrs);
    }

    private void setCustomFont(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.app);
        String customFont = a.getString(R.styleable.app_customFont);
        setCustomFont(ctx, customFont);
        a.recycle();
    }

    private boolean setCustomFont(Context ctx, String asset) {
        try {
            if (typeface == null) {
                // Log.i(TAG, "asset:: " + "fonts/" + asset);
                typeface = Typeface.createFromAsset(ctx.getAssets(),
                        "fonts/ClanPro-Medium.otf");
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
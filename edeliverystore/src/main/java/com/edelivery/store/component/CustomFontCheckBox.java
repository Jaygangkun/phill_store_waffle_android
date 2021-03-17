package com.edelivery.store.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.CheckBox;

import com.edelivery.store.utils.Utilities;
import com.elluminati.edelivery.store.R;


/**
 * @author Elluminati elluminati.in
 */
public class CustomFontCheckBox extends CheckBox {

//	private static final String TAG = "TextView";

    private Typeface typeface;
    public static final String TAG = "CustomFontTextView";

    public CustomFontCheckBox(Context context) {
        super(context);
    }

    public CustomFontCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    public CustomFontCheckBox(Context context, AttributeSet attrs, int defStyle) {
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
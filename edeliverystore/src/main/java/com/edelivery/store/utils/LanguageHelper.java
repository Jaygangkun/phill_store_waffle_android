package com.edelivery.store.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.text.TextUtils;

import com.edelivery.store.models.singleton.Language;
import com.elluminati.edelivery.store.R;

import java.util.Locale;

public class LanguageHelper extends ContextWrapper {


    public LanguageHelper(Context base) {
        super(base);
    }

    @SuppressWarnings("deprecation")
    public static ContextWrapper wrapper(Context context, String language) {
        Resources res = context.getResources();
        Configuration config = res.getConfiguration();
        Locale sysLocale = config.locale;
        if (!sysLocale.getLanguage().equals(language)) {
            if (TextUtils.isEmpty(language)) {
                language = "en";
                TypedArray typedArray = context.getResources().obtainTypedArray(R.array
                        .language_code);
                for (int i = 0; i < typedArray.length(); i++) {
                    if (TextUtils.equals(typedArray.getString(i), sysLocale.getLanguage())) {
                        language = sysLocale.getLanguage();
                        PreferenceHelper.getPreferenceHelper(context).putLanguageCode(language);
                        Language.getInstance().setAdminLanguageIndex(Utilities.
                                getLangIndxex(language, Language.getInstance().getAdminLanguages(),
                                        false));
                        Language.getInstance().setStoreLanguageIndex(Utilities.
                                getLangIndxex(language, Language.getInstance().getStoreLanguages(),
                                        true));
                    }

                }
            }

            Locale locale = new Locale(language);
            Locale.setDefault(locale);
            config.locale = locale;
            config.setLayoutDirection(locale);
            res.updateConfiguration(config, res
                    .getDisplayMetrics());
        }
        return new LanguageHelper(context);
    }
}
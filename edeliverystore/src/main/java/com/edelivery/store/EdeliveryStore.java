package com.edelivery.store;

import android.app.Application;

import com.edelivery.store.models.singleton.Language;
import com.edelivery.store.parse.ApiClient;
import com.edelivery.store.utils.PreferenceHelper;

/**
 * Created by Ravi Bhalodi on 10,January,2020 in Elluminati
 */
public class EdeliveryStore extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ApiClient.setLanguage(String.valueOf(Language.getInstance().getAdminLanguageIndex()));
    }
}

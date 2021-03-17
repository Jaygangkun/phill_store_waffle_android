package com.edelivery.store.sunmi;

import android.app.Application;

import com.edelivery.store.models.singleton.Language;
import com.edelivery.store.observers.ObservableSingleton;
import com.edelivery.store.parse.ApiClient;
import com.edelivery.store.sunmi.utils.AidlUtil;


/**
 * Created by Administrator on 2017/4/27.
 */

public class BaseApp extends Application {
    private boolean isAidl;

    public boolean isAidl() {
        return isAidl;
    }

    public void setAidl(boolean aidl) {
        isAidl = aidl;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ApiClient.setLanguage(String.valueOf(Language.getInstance().getAdminLanguageIndex()));
        AidlUtil.getInstance().connectPrinterService(this);
//        isAidl = true;
        initSingleton();

    }

    private void initSingleton(){
        ObservableSingleton.initInstance();
    }
}

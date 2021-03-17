package com.edelivery.store.models.singleton;

import com.edelivery.store.models.datamodel.Languages;
import com.edelivery.store.parse.ApiClient;

import java.util.ArrayList;

/**
 * Created by Ravi Bhalodi on 13,January,2020 in Elluminati
 */
public class Language {
    private ArrayList<Languages> storeLanguages;
    private ArrayList<Languages> adminLanguages;
    private int adminLanguageIndex;
    private int storeLanguageIndex;

    private static Language language;


    public static Language getInstance() {
        if (language == null) {
            language = new Language();
        }
        return language;
    }

    public ArrayList<Languages> getStoreLanguages() {
        return storeLanguages != null ? storeLanguages : new ArrayList<Languages>();
    }

    public void setStoreLanguages(ArrayList<Languages> storeLanguages) {
        this.storeLanguages = storeLanguages;
    }

    public ArrayList<Languages> getAdminLanguages() {
        return adminLanguages != null ? adminLanguages : new ArrayList<Languages>();
    }

    public void setAdminLanguages(ArrayList<Languages> adminLanguages) {
        this.adminLanguages = adminLanguages;
    }

    public int getAdminLanguageIndex() {
        return adminLanguageIndex;
    }

    public void setAdminLanguageIndex(int adminLanguageIndex) {
        this.adminLanguageIndex = adminLanguageIndex;
        ApiClient.setLanguage(String.valueOf(adminLanguageIndex));
    }

    public int getStoreLanguageIndex() {
        return storeLanguageIndex;
    }

    public void setStoreLanguageIndex(int storeLanguageIndex) {
        this.storeLanguageIndex = storeLanguageIndex;
    }
}

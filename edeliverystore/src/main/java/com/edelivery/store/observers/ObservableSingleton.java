package com.edelivery.store.observers;


/**
 * Created by Sureshkumar on 03-11-2014.
 */
public class ObservableSingleton {

    private static Observable mObservable;
    public static void initInstance() {
        if(mObservable==null){
            mObservable = new ObservableImpl();
        }
    }

    public static Observable getInstance(){
        return mObservable;
    }
}
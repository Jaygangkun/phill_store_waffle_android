package com.edelivery.store.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.View;

import com.edelivery.store.PaymentActivity;


/**
 * Created by elluminati on 14-06-2016.
 */
public abstract class BasePaymentFragments extends Fragment implements View.OnClickListener{

    protected PaymentActivity paymentActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        paymentActivity = (PaymentActivity) getActivity();
    }
}

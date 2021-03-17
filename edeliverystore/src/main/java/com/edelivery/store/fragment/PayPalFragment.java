package com.edelivery.store.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elluminati.edelivery.store.R;


/**
 * Created by elluminati on 01-Apr-17.
 */

public class PayPalFragment extends BasePaymentFragments {
    private String Tag = PayPalFragment.class.getName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View stripeFragView = inflater.inflate(R.layout.fragment_pay_pal, container, false);
        return stripeFragView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View view) {

    }


}

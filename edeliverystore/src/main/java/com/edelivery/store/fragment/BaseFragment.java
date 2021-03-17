package com.edelivery.store.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.View;

import com.edelivery.store.HomeActivity;

/**
 * BaseFragment class on 08-02-2017.
 */

public class BaseFragment extends Fragment implements View.OnClickListener{

    protected HomeActivity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (HomeActivity) getActivity();
    }

    @Override
    public void onClick(View v) {

    }
}
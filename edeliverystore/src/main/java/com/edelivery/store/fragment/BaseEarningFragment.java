package com.edelivery.store.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.View;

import com.edelivery.store.EarningActivity;


/**
 * Created by elluminati on 27-Jun-17.
 */

public abstract class BaseEarningFragment extends Fragment implements View.OnClickListener {

    protected EarningActivity earningActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        earningActivity = (EarningActivity) getActivity();
    }
}

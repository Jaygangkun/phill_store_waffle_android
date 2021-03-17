package com.edelivery.store.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.View;

import com.edelivery.store.HistoryDetailActivity;

/**
 * Created by elluminati on 29-Dec-17.
 */

public class BaseHistoryFragment extends Fragment implements View.OnClickListener{
    protected HistoryDetailActivity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (HistoryDetailActivity) getActivity();
    }

    @Override
    public void onClick(View v) {

    }
}

package com.edelivery.store.adapter;

import android.content.Context;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.edelivery.store.WithdrawalActivity;
import com.edelivery.store.models.datamodel.BankDetail;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;

import java.util.List;

/**
 * Created by elluminati on 28-Oct-17.
 */

public class BankSpinnerAdapter extends ArrayAdapter {
    private LayoutInflater inflater;
    private List<BankDetail> bankDetails;
    private WithdrawalActivity withdrawalActivity;

    public BankSpinnerAdapter(@NonNull WithdrawalActivity withdrawalActivity, @LayoutRes int
            resource,
                              @NonNull List<BankDetail> bankDetails) {
        super(withdrawalActivity, resource, bankDetails);
        this.withdrawalActivity = withdrawalActivity;
        this.bankDetails = bankDetails;
        inflater = (LayoutInflater) withdrawalActivity .getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup
            parent) {
        return getCustomView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        View view = inflater.inflate(R.layout.item_spinner_bank, parent, false);


        BankDetail bankDetail = bankDetails.get(position);

        CustomTextView tvBankName = (CustomTextView) view.findViewById(R.id.tvBankName);
        tvBankName.setText(bankDetail.getAccountNumber());

        return view;
    }
}

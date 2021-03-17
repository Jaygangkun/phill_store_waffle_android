package com.edelivery.store.adapter;

import android.annotation.SuppressLint;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.edelivery.store.fragment.WalletHistoryFragment;
import com.edelivery.store.models.datamodel.WalletHistory;
import com.edelivery.store.parse.ParseContent;
import com.edelivery.store.utils.Constant;
import com.edelivery.store.utils.Utilities;
import com.edelivery.store.widgets.CustomFontTextViewTitle;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by elluminati on 01-Nov-17.
 */

public class WalletHistoryAdapter extends RecyclerView.Adapter<WalletHistoryAdapter
        .WalletHistoryHolder> {
    private ArrayList<WalletHistory> walletHistories;
    private WalletHistoryFragment walletHistoryFragment;

    public WalletHistoryAdapter(WalletHistoryFragment walletHistoryFragment,
                                ArrayList<WalletHistory> walletHistories) {
        this.walletHistories = walletHistories;
        this.walletHistoryFragment = walletHistoryFragment;
    }

    @Override
    public WalletHistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout
                .item_wallet_history, parent, false);
        return new WalletHistoryHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(WalletHistoryHolder holder, int position) {

        WalletHistory walletHistory = walletHistories.get
                (position);
        try {
            Date date = ParseContent.getParseContentInstance().webFormat.parse(walletHistory
                    .getCreatedAt
                            ());
            holder.tvTransactionDate.setText(ParseContent.getParseContentInstance().dateFormat3
                    .format(date));
            holder.tvTransactionTime.setText(ParseContent.getParseContentInstance().timeFormat_am
                    .format(date));
            holder.tvWithdrawalId.setText(walletHistoryFragment.getResources().getString(R.string
                    .text_id) + " " + walletHistory.getUniqueId());

            holder.tvTransactionState.setText(walletComment(walletHistory.getWalletCommentId
                    ()));

            switch (walletHistory.getWalletStatus()) {
                case Constant.Wallet.ADD_WALLET_AMOUNT:
                    holder.ivWalletStatus.setBackgroundColor(ResourcesCompat
                            .getColor(walletHistoryFragment.getResources(), R.color
                                    .color_app_wallet_added, null));
                    holder.tvTransactionAmount.setTextColor(ResourcesCompat
                            .getColor(walletHistoryFragment.getResources(), R.color
                                    .color_app_wallet_added, null));
                    holder.tvTransactionAmount.setText("+" + ParseContent.getParseContentInstance()
                            .decimalTwoDigitFormat
                            .format(walletHistory
                                    .getAddedWallet()) + " " + walletHistory.getToCurrencyCode());
                    break;
                case Constant.Wallet.REMOVE_WALLET_AMOUNT:
                    holder.ivWalletStatus.setBackgroundColor(ResourcesCompat
                            .getColor(walletHistoryFragment.getResources(), R.color
                                    .color_app_wallet_deduct, null));
                    holder.tvTransactionAmount.setTextColor(ResourcesCompat
                            .getColor(walletHistoryFragment.getResources(), R.color
                                    .color_app_wallet_deduct, null));
                    holder.tvTransactionAmount.setText("-" + ParseContent.getParseContentInstance()
                            .decimalTwoDigitFormat
                            .format(walletHistory
                                    .getAddedWallet()) + " " + walletHistory.getFromCurrencyCode());

                    break;


            }


        } catch (ParseException e) {
            Utilities.handleException(WalletHistoryAdapter.class.getName(), e);
        }

    }

    @Override
    public int getItemCount() {
        return walletHistories.size();
    }

    private String walletComment(int id) {
        String comment;
        switch (id) {
            case Constant.Wallet.ADDED_BY_ADMIN:
                comment = walletHistoryFragment.getResources().getString(R.string
                        .text_wallet_status_added_by_admin);
                break;
            case Constant.Wallet.ADDED_BY_CARD:
                comment = walletHistoryFragment.getResources().getString(R.string
                        .text_wallet_status_added_by_card);
                break;
            case Constant.Wallet.ADDED_BY_REFERRAL:
                comment = walletHistoryFragment.getResources().getString(R.string
                        .text_wallet_status_added_by_referral);
                break;
            case Constant.Wallet.ORDER_REFUND:
                comment = walletHistoryFragment.getResources().getString(R.string
                        .text_wallet_status_order_refund);
                break;
            case Constant.Wallet.ORDER_PROFIT:
                comment = walletHistoryFragment.getResources().getString(R.string
                        .text_wallet_status_order_profit);
                break;
            case Constant.Wallet.ORDER_CANCELLATION_CHARGE:
                comment = walletHistoryFragment.getResources().getString(R.string
                        .text_wallet_status_order_cancellation_charge);
                break;
            case Constant.Wallet.ORDER_CHARGED:
                comment = walletHistoryFragment.getResources().getString(R.string
                        .text_wallet_status_order_charged);
                break;
            case Constant.Wallet.WALLET_REQUEST_CHARGE:
                comment = walletHistoryFragment.getResources().getString(R.string
                        .text_wallet_status_wallet_request_charge);
                break;


            default:
                // do with default
                comment = "NA";
                break;
        }
        return comment;
    }


    protected class WalletHistoryHolder extends RecyclerView.ViewHolder {

        CustomFontTextViewTitle tvTransactionState, tvTransactionAmount;
        CustomTextView tvTransactionDate, tvWithdrawalId,
                tvTransactionTime;
        ImageView ivTransactionType;
        View ivWalletStatus;

        public WalletHistoryHolder(View itemView) {
            super(itemView);
            tvWithdrawalId = (CustomTextView) itemView.findViewById(R.id.tvWithdrawalID);
            tvTransactionAmount = (CustomFontTextViewTitle) itemView.findViewById(R.id
                    .tvTransactionAmount);
            tvTransactionDate = (CustomTextView) itemView.findViewById(R.id.tvTransactionDate);
            tvTransactionState = (CustomFontTextViewTitle) itemView.findViewById(R.id
                    .tvTransactionState);
            ivTransactionType = (ImageView) itemView.findViewById(R.id.ivTransactionType);
            tvTransactionTime = (CustomTextView) itemView.findViewById(R.id.tvTransactionTime);
            ivWalletStatus = itemView.findViewById(R.id.ivWalletStatus);
        }
    }
}

package com.edelivery.store.adapter;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.edelivery.store.fragment.WalletTransactionFragment;
import com.edelivery.store.models.datamodel.WalletRequestDetail;
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

public class WalletTransactionAdapter extends RecyclerView.Adapter<WalletTransactionAdapter
        .WalletTransactionHolder> {
    private ArrayList<WalletRequestDetail> walletRequestDetails;
    private WalletTransactionFragment walletTransactionFragment;

    public WalletTransactionAdapter(WalletTransactionFragment walletTransactionFragment,
                                    ArrayList<WalletRequestDetail> walletHistoryItems) {
        this.walletRequestDetails = walletHistoryItems;
        this.walletTransactionFragment = walletTransactionFragment;
    }

    @Override
    public WalletTransactionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout
                .item_wallet_transection, parent, false);
        return new WalletTransactionHolder(view);
    }

    @Override
    public void onBindViewHolder(WalletTransactionHolder holder, int position) {
        WalletRequestDetail requestDetailItem = walletRequestDetails.get
                (position);
        try {
            Date date = ParseContent.getParseContentInstance().webFormat.parse(requestDetailItem
                    .getCreatedAt
                            ());
            holder.tvTransactionDate.setText(ParseContent.getParseContentInstance().dateFormat3
                    .format(date));
            holder.tvTransactionTime.setText(ParseContent.getParseContentInstance().timeFormat_am
                    .format(date));
            holder.tvWithdrawalId.setText(walletTransactionFragment.getResources().getString(R
                    .string
                    .text_id) + " " + requestDetailItem.getUniqueId());
            String amount = ParseContent.getParseContentInstance()
                    .decimalTwoDigitFormat
                    .format(requestDetailItem.getWalletStatus() == Constant
                            .Wallet
                            .WALLET_STATUS_COMPLETED || requestDetailItem.getWalletStatus() ==
                            Constant.Wallet.WALLET_STATUS_TRANSFERRED ? requestDetailItem
                            .getApprovedRequestedWalletAmount() : requestDetailItem
                            .getRequestedWalletAmount()) + " " +
                    requestDetailItem
                            .getAdminCurrencyCode();
            holder.tvTransactionAmount.setText(amount);
            holder.tvTransactionState.setText(walletSate(requestDetailItem.getWalletStatus()));
            if (requestDetailItem.isPaymentModeCash()) {
                holder.ivTransactionType.setImageDrawable(AppCompatResources.getDrawable
                        (walletTransactionFragment.getContext(), R.drawable.ic_cash_2));
            } else {
                holder.ivTransactionType.setImageDrawable(AppCompatResources.getDrawable
                        (walletTransactionFragment.getContext(), R.drawable.ic_bank_building_24dp));
            }
            if (requestDetailItem.getWalletStatus() == Constant.Wallet
                    .WALLET_STATUS_CANCELLED || requestDetailItem.getWalletStatus() == Constant
                    .Wallet
                    .WALLET_STATUS_TRANSFERRED || requestDetailItem.getWalletStatus() == Constant
                    .Wallet
                    .WALLET_STATUS_COMPLETED) {

                holder.tvCancelWalletRequest.setVisibility(View.GONE);
            } else {
                holder.tvCancelWalletRequest.setVisibility(View.VISIBLE);
            }


        } catch (ParseException e) {
            Utilities.handleException(WalletHistoryAdapter.class.getName(), e);
        }

    }

    @Override
    public int getItemCount() {
        return walletRequestDetails.size();
    }

    private String walletSate(int id) {
        String comment;
        switch (id) {
            case Constant.Wallet.WALLET_STATUS_CREATED:
                comment = walletTransactionFragment.getResources().getString(R.string
                        .text_wallet_status_created);
                break;
            case Constant.Wallet.WALLET_STATUS_ACCEPTED:
                comment = walletTransactionFragment.getResources().getString(R.string
                        .text_wallet_status_accepted);
                break;
            case Constant.Wallet.WALLET_STATUS_TRANSFERRED:
                comment = walletTransactionFragment.getResources().getString(R.string
                        .text_wallet_status_transferred);
                break;
            case Constant.Wallet.WALLET_STATUS_COMPLETED:
                comment = walletTransactionFragment.getResources().getString(R.string
                        .text_wallet_status_completed);
                break;
            case Constant.Wallet.WALLET_STATUS_CANCELLED:
                comment = walletTransactionFragment.getResources().getString(R.string
                        .text_wallet_status_cancelled);
                break;

            default:
                // do with default
                comment = "NA";
                break;
        }
        return comment;
    }

    protected class WalletTransactionHolder extends RecyclerView.ViewHolder implements View
            .OnClickListener {

        CustomFontTextViewTitle tvTransactionState, tvTransactionAmount;
        CustomTextView tvTransactionDate, tvCancelWalletRequest,
                tvTransactionTime, tvWithdrawalId;
        LinearLayout llProduct;
        ImageView ivTransactionType;

        public WalletTransactionHolder(View itemView) {
            super(itemView);
            tvWithdrawalId = (CustomTextView) itemView.findViewById(R.id.tvWithdrawalID);
            tvTransactionAmount = (CustomFontTextViewTitle) itemView.findViewById(R.id
                    .tvTransactionAmount);
            tvTransactionDate = (CustomTextView) itemView.findViewById(R.id.tvTransactionDate);
            tvTransactionState = (CustomFontTextViewTitle) itemView.findViewById(R.id
                    .tvTransactionState);
            ivTransactionType = (ImageView) itemView.findViewById(R.id.ivTransactionType);
            tvCancelWalletRequest = (CustomTextView) itemView.findViewById(R.id
                    .tvCancelWalletRequest);
            tvTransactionTime = (CustomTextView) itemView.findViewById(R.id.tvTransactionTime);
            llProduct = (LinearLayout) itemView.findViewById(R.id.llProduct);
            tvCancelWalletRequest.setOnClickListener(this);
            llProduct.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.llProduct:
                    walletTransactionFragment.goToWalletTransactionActivity(walletRequestDetails
                            .get(getAdapterPosition()));
                    break;
                case R.id.tvCancelWalletRequest:
                    walletTransactionFragment.openCancelWithdrawalRequestDialog
                            (walletRequestDetails
                                    .get(getAdapterPosition()).getId());
                    break;
                default:
                    // do with default
                    break;
            }
        }
    }

}

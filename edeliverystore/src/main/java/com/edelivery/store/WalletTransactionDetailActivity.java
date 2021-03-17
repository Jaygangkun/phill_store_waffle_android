package com.edelivery.store;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.edelivery.store.models.datamodel.WalletRequestDetail;
import com.edelivery.store.parse.ParseContent;
import com.edelivery.store.utils.Constant;
import com.edelivery.store.utils.Utilities;
import com.edelivery.store.widgets.CustomFontTextViewTitle;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;

import java.text.ParseException;
import java.util.Date;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;

public class WalletTransactionDetailActivity extends BaseActivity {


    private CustomTextView tvTransactionDate, tvWithdrawalID, tvTransactionTime;
    private CustomFontTextViewTitle tvWalletRequestAmount, tvApproveByAdmin,
            tvTotalWalletAmount, tvMode;
    private WalletRequestDetail walletRequestDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_trans_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar(toolbar, R.drawable.ic_back, R.color.color_app_theme);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utilities.hideSoftKeyboard(WalletTransactionDetailActivity.this);
                onBackPressed();
            }
        });
        ((TextView) findViewById(R.id.tvToolbarTitle)).setText(getString(R.string
                .text_wallet_transaction_detail));
        tvTotalWalletAmount = (CustomFontTextViewTitle) findViewById(R.id.tvTotalWalletAmount);
        tvWalletRequestAmount = (CustomFontTextViewTitle) findViewById(R.id.tvWalletRequestAmount);
        tvApproveByAdmin = (CustomFontTextViewTitle) findViewById(R.id.tvApproveByAdmin);
        tvMode = (CustomFontTextViewTitle) findViewById(R.id.tvMode);
        tvWithdrawalID = (CustomTextView) findViewById(R.id.tvWithdrawalID);
        tvTransactionDate = (CustomTextView) findViewById(R.id.tvTransactionDate);
        tvTransactionTime = (CustomTextView) findViewById(R.id.tvTransactionTime);
        getExtraData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        setToolbarEditIcon(false, 0);
        setToolbarCameraIcon(false);
        return true;
    }

    private void getExtraData() {
        if (getIntent().getExtras() != null) {
            walletRequestDetail = getIntent().getExtras().getParcelable(Constant.BUNDLE);
            ((TextView) findViewById(R.id.tvToolbarTitle)).setText(walletSate(walletRequestDetail
                    .getWalletStatus()));
            tvApproveByAdmin.setText(ParseContent.getParseContentInstance().decimalTwoDigitFormat
                    .format
                            (walletRequestDetail
                                    .getApprovedRequestedWalletAmount()) + " " +
                    walletRequestDetail
                            .getAdminCurrencyCode());

            tvTotalWalletAmount.setText(parseContent.decimalTwoDigitFormat.format
                    (Constant.Wallet.WALLET_STATUS_COMPLETED == walletRequestDetail.getWalletStatus
                            () ? walletRequestDetail
                            .getAfterTotalWalletAmount() : walletRequestDetail
                            .getTotalWalletAmount()) + " " +
                    walletRequestDetail
                            .getAdminCurrencyCode
                                    ());
            String amount = ParseContent.getParseContentInstance()
                    .decimalTwoDigitFormat
                    .format(walletRequestDetail.getWalletStatus() == Constant
                            .Wallet
                            .WALLET_STATUS_COMPLETED || walletRequestDetail.getWalletStatus() ==
                            Constant.Wallet.WALLET_STATUS_TRANSFERRED ? walletRequestDetail
                            .getApprovedRequestedWalletAmount() : walletRequestDetail
                            .getRequestedWalletAmount()) + " " +
                    walletRequestDetail
                            .getAdminCurrencyCode();
            tvWalletRequestAmount.setText(amount);
            tvWithdrawalID.setText(getResources().getString(R.string
                    .text_id) + " " + walletRequestDetail.getUniqueId());
            try {
                Date date = ParseContent.getParseContentInstance().webFormat.parse
                        (walletRequestDetail
                                .getCreatedAt
                                        ());
                tvTransactionDate.setText(ParseContent.getParseContentInstance().dateFormat3
                        .format(date));
                tvTransactionTime.setText(ParseContent.getParseContentInstance().timeFormat_am
                        .format(date));
            } catch (ParseException e) {
                Utilities.handleException(WalletDetailActivity.class.getName(), e);
            }
            if (walletRequestDetail.isPaymentModeCash()) {
                tvMode.setText(getResources().getString(R.string
                        .text_cash_payment));
                tvMode.setCompoundDrawablesRelativeWithIntrinsicBounds(AppCompatResources
                        .getDrawable
                                (this, R.drawable.ic_cash_2), null, null, null);
            } else {
                tvMode.setText(getResources().getString(R.string
                        .text_bank_payment));
                tvMode.setCompoundDrawablesRelativeWithIntrinsicBounds(AppCompatResources
                        .getDrawable
                                (this, R.drawable.ic_bank_building_24dp), null, null, null);
            }
        }

    }

    private String walletSate(int id) {
        String comment;
        switch (id) {
            case Constant.Wallet.WALLET_STATUS_CREATED:
                comment = getResources().getString(R.string
                        .text_wallet_status_created);
                break;
            case Constant.Wallet.WALLET_STATUS_ACCEPTED:
                comment = getResources().getString(R.string
                        .text_wallet_status_accepted);
                break;
            case Constant.Wallet.WALLET_STATUS_TRANSFERRED:
                comment = getResources().getString(R.string
                        .text_wallet_status_transferred);
                break;
            case Constant.Wallet.WALLET_STATUS_COMPLETED:
                comment = getResources().getString(R.string
                        .text_wallet_status_completed);
                break;
            case Constant.Wallet.WALLET_STATUS_CANCELLED:
                comment = getResources().getString(R.string
                        .text_wallet_status_cancelled);
                break;

            default:
                // do with default
                comment = "NA";
                break;
        }
        return comment;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}

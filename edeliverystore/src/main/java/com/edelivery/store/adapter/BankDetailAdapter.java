package com.edelivery.store.adapter;

import android.os.Build;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.edelivery.store.BankDetailActivity;
import com.edelivery.store.models.datamodel.BankDetail;
import com.edelivery.store.utils.Utilities;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;

import java.util.List;

/**
 * Created by elluminati on 27-Oct-17.
 */

public class BankDetailAdapter extends RecyclerView.Adapter<BankDetailAdapter.BankItemView> {
    private BankDetailActivity bankDetailActivity;
    private List<BankDetail> bankDetails;

    public BankDetailAdapter(BankDetailActivity bankDetailActivity, List<BankDetail> bankDetails) {
        this.bankDetailActivity = bankDetailActivity;
        this.bankDetails = bankDetails;
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String source) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(source);
        }
    }

    @Override
    public BankItemView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bank_detail,
                parent, false);
        return new BankItemView(view);
    }

    @Override
    public void onBindViewHolder(BankItemView holder, int position) {
        BankDetail bankDetail = bankDetails.get(position);
        Utilities.setTagBackgroundRtlView(bankDetailActivity, holder.tvAccountHolderName);
        holder.tvBankAccountNumber.setText(fromHtml(getColoredSpanned(bankDetailActivity
                .getResources().getString(R.string.text_account_no), bankDetail
                .getAccountNumber())));
        holder.tvAccountHolderName.setText(bankDetail.getBankAccountHolderName());
        holder.tvRoutingNumber.setText(fromHtml(getColoredSpanned(bankDetailActivity
                .getResources().getString(R.string.text_personal_id_number), bankDetail
                .getRoutingNumber())));
        holder.ivSelected.setVisibility(bankDetail.isSelected() ? View.VISIBLE : View.GONE);

    }

    @Override
    public int getItemCount() {
        return bankDetails.size();
    }

    private String getColoredSpanned(String text1, String text2) {
        String input = "<font color=" + "#707070" + ">" + text1 + " : " + "</font>" + "<font " +
                "color=" +
                "#1a1a19"
                + ">" +
                text2 + "</font>";
        return input;
    }

    protected class BankItemView extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView ivDeleteBankDetail, ivSelected;
        private CustomTextView tvBankAccountNumber,
                tvAccountHolderName, tvRoutingNumber;

        public BankItemView(View itemView) {
            super(itemView);
            tvBankAccountNumber = itemView.findViewById(R.id
                    .tvBankAccountNumber);
            tvAccountHolderName = itemView.findViewById(R.id
                    .tvAccountHolderName);
            ivDeleteBankDetail = (ImageView) itemView.findViewById(R.id.ivDeleteBankDetail);
            tvRoutingNumber = itemView.findViewById(R.id.tvRoutingNumber);
            ivSelected = itemView.findViewById(R.id.ivSelected);
            ivDeleteBankDetail.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ivDeleteBankDetail:
                    bankDetailActivity.showVerificationDialog(bankDetails.get(getAdapterPosition
                            ()));
                    break;
                default:
                    bankDetailActivity.selectBankDetail(bankDetails.get(getAdapterPosition
                            ()));
                    break;
            }
        }
    }
}

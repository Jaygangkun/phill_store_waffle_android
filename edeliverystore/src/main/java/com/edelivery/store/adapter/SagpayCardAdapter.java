package com.edelivery.store.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.edelivery.store.fragment.SagpayFragment;
import com.edelivery.store.models.datamodel.Card;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;

import java.util.ArrayList;

/**
 * Created by elluminati on 01-Apr-17.
 */

public class SagpayCardAdapter extends RecyclerView.Adapter<SagpayCardAdapter.CardViewHolder> {

    private ArrayList<Card> cardList;
    private SagpayFragment sagpayFragment;
    public SagpayCardAdapter(SagpayFragment sagpayFragment, ArrayList<Card> cardList) {
        this.cardList = cardList;
        this.sagpayFragment = sagpayFragment;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent,
                false);

        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {

        Card card = cardList.get(position);
        String cardLastFour = "****" + card.getLastFour();
        holder.tvCardNumber.setText(cardLastFour);
        if (card.isIsDefault()) {
            holder.ivSelected.setVisibility(View.VISIBLE);
        } else {
            holder.ivSelected.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    protected class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CustomTextView tvCardNumber;
        ImageView ivSelected;
        LinearLayout llCard;
        TextView ivDeleteCard;


        public CardViewHolder(View itemView) {
            super(itemView);
            tvCardNumber = (CustomTextView) itemView.findViewById(R.id.tvCardNumber);
            ivDeleteCard = itemView.findViewById(R.id.ivDeleteCard);
            ivSelected = (ImageView) itemView.findViewById(R.id.ivSelected);
            llCard = (LinearLayout) itemView.findViewById(R.id.llCard);
            llCard.setOnClickListener(this);
            ivDeleteCard.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.llCard:
                    sagpayFragment.selectCreditCard(getAdapterPosition());
                    break;
                case R.id.ivDeleteCard:
                    sagpayFragment.openDeleteCard(getAdapterPosition());
                    break;
                default:
                    // do with default
                    break;
            }

        }
    }
}

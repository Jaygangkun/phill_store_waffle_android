package com.edelivery.store.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edelivery.store.fragment.StripeFragment;
import com.edelivery.store.models.datamodel.Card;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by elluminati on 01-Apr-17.
 */

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private ArrayList<Card> cardList;
    private StripeFragment stripeFragment;

    public CardAdapter(StripeFragment stripeFragment, ArrayList<Card> cardList) {
        this.cardList = cardList;
        this.stripeFragment = stripeFragment;
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
                    stripeFragment.selectCreditCard(getAdapterPosition());
                    break;
                case R.id.ivDeleteCard:
                    stripeFragment.openDeleteCard(getAdapterPosition());
                    break;
                default:
                    // do with default
                    break;
            }

        }
    }
}

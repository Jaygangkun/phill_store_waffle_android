package com.edelivery.store.models.responsemodel;

import com.edelivery.store.models.datamodel.Card;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CardsResponse {

    @SerializedName("cards")
    @Expose
    private List<Card> cards;

    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("message")
    @Expose
    private int message;

    @SerializedName("payment_method")
    private String paymentMethod;

    @SerializedName("client_secret")
    private String clientSecret;

    @SerializedName("merchantSessionKey")
    private String merchantSessionKey;

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setMessage(int message) {
        this.message = message;
    }

    public int getMessage() {
        return message;
    }

    @SerializedName("card")
    @Expose
    private Card card;

    public void setCard(Card card) {
        this.card = card;
    }

    public Card getCard() {
        return card;
    }

    @SerializedName("error_code")
    @Expose
    private int errorCode;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getMerchantSessionKey() {
        return merchantSessionKey;
    }

    public void setMerchantSessionKey(String merchantSessionKey) {
        this.merchantSessionKey = merchantSessionKey;
    }
}
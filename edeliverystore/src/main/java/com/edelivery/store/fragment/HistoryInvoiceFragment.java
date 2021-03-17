package com.edelivery.store.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.edelivery.store.adapter.InvoiceAdapter;
import com.edelivery.store.models.datamodel.Invoice;
import com.edelivery.store.models.datamodel.InvoicePayment;
import com.edelivery.store.models.datamodel.OrderPaymentDetail;
import com.edelivery.store.parse.ParseContent;
import com.edelivery.store.utils.Utilities;
import com.edelivery.store.widgets.CustomFontTextViewTitle;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;

import java.util.ArrayList;

/**
 * Created by elluminati on 29-Dec-17.
 */

public class HistoryInvoiceFragment extends BaseHistoryFragment {

    private OrderPaymentDetail orderPaymentDetail;
    private CustomFontTextViewTitle tvInvoiceTotal;
    private ParseContent parseContent;
    private LinearLayout invoiceDistance, invoicePayment;
    private RecyclerView rcvInvoice;
    private String currency;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_invoice, container, false);
        parseContent = ParseContent.getParseContentInstance();
        invoiceDistance = (LinearLayout) view.findViewById(R.id.invoiceDistance);
        invoicePayment = (LinearLayout) view.findViewById(R.id.invoicePayment);
        rcvInvoice = (RecyclerView) view.findViewById(R.id.rcvInvoice);
        tvInvoiceTotal = (CustomFontTextViewTitle) view.findViewById(R.id.tvInvoiceTotal);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        orderPaymentDetail = activity.detailsResponse.getOrder().getOrderPaymentDetail();
        currency = activity.preferenceHelper.getCurrency();
        setInvoiceData();
        setInvoiceDistanceAndTime();
        setInvoicePayments();

    }

    private void setInvoiceDistanceAndTime() {

        String unit = orderPaymentDetail.isIsDistanceUnitMile() ? this.getResources()
                .getString(R.string
                        .unit_mile) : this.getResources().getString(R.string.unit_km);

        ArrayList<InvoicePayment> invoicePayments = new ArrayList<>();
        invoicePayments.add(loadInvoiceImage(this.getResources().getString(R.string.text_time_h_m),
                Utilities.minuteToHoursMinutesSeconds(orderPaymentDetail.getTotalTime()), R.drawable
                        .ic_wall_clock));
        invoicePayments.add(loadInvoiceImage(this.getResources().getString(R.string
                        .text_distance),
                appendString(orderPaymentDetail.getTotalDistance(), unit), R.drawable.ic_route));

        invoicePayments.add(loadInvoiceImage(this.getResources().getString(R.string.text_pay),
                activity.detailsResponse.getPaymentGatewayName(), orderPaymentDetail
                        .isIsPaymentModeCash() ? R.drawable
                        .ic_cash : R.drawable.ic_credit_card_2));

        int size = invoicePayments.size();
        for (int i = 0; i < size; i++) {
            LinearLayout currentLayout = (LinearLayout) invoiceDistance.getChildAt(i);
            ImageView imageView = (ImageView) currentLayout.getChildAt(0);
            imageView.setImageDrawable
                    (AppCompatResources.getDrawable
                            (activity, invoicePayments.get(i).getImageId()));
            LinearLayout currentSubSubLayout = (LinearLayout) currentLayout.getChildAt(1);
            ((CustomTextView) currentSubSubLayout.getChildAt(0)).setText(invoicePayments.get
                    (i).getTitle());
            ((CustomTextView) currentSubSubLayout.getChildAt(1)).setText(invoicePayments.get
                    (i).getValue());
        }


    }

    private void setInvoicePayments() {


        ArrayList<InvoicePayment> invoicePayments = new ArrayList<>();
        invoicePayments.add(loadInvoiceImage(this.getResources().getString(R.string.text_wallet),
                appendString(currency, orderPaymentDetail
                        .getWalletPayment()), R
                        .drawable
                        .ic_wallet));
        if (orderPaymentDetail.isIsPaymentModeCash()) {
            invoicePayments.add(loadInvoiceImage(this.getResources().getString(R.string.text_cash),
                    appendString(currency,
                            orderPaymentDetail
                                    .getCashPayment()),
                    R.drawable
                            .ic_cash));
        } else {
            invoicePayments.add(loadInvoiceImage(this.getResources().getString(R.string.text_card),
                    appendString(currency,
                            orderPaymentDetail
                                    .getCardPayment()),
                    R.drawable
                            .ic_credit_card_2));
        }

        if (orderPaymentDetail.getPromoPayment() > 0) {
            invoicePayment.addView(LayoutInflater.from(activity).inflate(R.layout
                            .include_invoice_data,
                    null));
            invoicePayments.add(loadInvoiceImage(getResources().getString(R.string.text_promo),
                    appendString(currency, orderPaymentDetail.getPromoPayment()), R.drawable
                            .ic_promo_code));
        }
        int size = invoicePayments.size();
        for (int i = 0; i < size; i++) {
            LinearLayout currentLayout = (LinearLayout) invoicePayment.getChildAt(i);
            ImageView imageView = (ImageView) currentLayout.getChildAt(0);
            imageView.setImageDrawable
                    (AppCompatResources.getDrawable
                            (activity, invoicePayments.get(i).getImageId()));
            LinearLayout currentSubSubLayout = (LinearLayout) currentLayout.getChildAt(1);
            ((CustomTextView) currentSubSubLayout.getChildAt(0)).setText(invoicePayments.get
                    (i).getTitle());
            ((CustomTextView) currentSubSubLayout.getChildAt(1)).setText(invoicePayments.get
                    (i).getValue());
        }

        tvInvoiceTotal.setText(currency + parseContent
                .decimalTwoDigitFormat.format
                        (orderPaymentDetail.getTotal()));
    }

    private void setInvoiceData() {

        String unit = orderPaymentDetail.isIsDistanceUnitMile() ? this.getResources().
                getString(R.string.unit_mile) : this.getResources().getString(R.string.unit_km);

        ArrayList<ArrayList<Invoice>> arrayListsInvoices = new ArrayList<>();
        ArrayList<Invoice> invoices = new ArrayList<>();
        if (orderPaymentDetail.getTotalBasePrice() > 0) {
            invoices.add(loadInvoiceData(this.getResources().getString(R.string.text_base_price),
                    orderPaymentDetail.getTotalBasePrice(), currency,
                    orderPaymentDetail
                            .getBasePrice(), currency,
                    orderPaymentDetail
                            .getBasePriceDistance(), unit, ""));
        }
        if (orderPaymentDetail.getDistancePrice() > 0) {
            invoices.add(loadInvoiceData(this.getResources().getString(R.string
                            .text_distance_price),
                    orderPaymentDetail.getDistancePrice(), currency,
                    orderPaymentDetail
                            .getPricePerUnitDistance(), currency,
                    0.0,
                    unit, ""));
        }
        if (orderPaymentDetail.getTotalTimePrice() > 0) {
            invoices.add(loadInvoiceData(this.getResources().getString(R.string.text_time_cost),
                    orderPaymentDetail.getTotalTimePrice(), currency,
                    orderPaymentDetail
                            .getPricePerUnitTime(), currency, 0.0,
                    this.getResources().getString(R.string.unit_mins), ""));
        }

        if (orderPaymentDetail.getTotalServicePrice() > 0) {
            invoices.add(loadInvoiceData(this.getResources().getString(R.string
                            .text_service_price),
                    orderPaymentDetail.getTotalServicePrice(), currency, 0.0,
                    "", 0.0,
                    "", ""));
        }
        if (orderPaymentDetail.getTotalAdminTaxPrice() > 0) {
            invoices.add(loadInvoiceData(this.getResources().getString(R.string.text_service_tax),
                    orderPaymentDetail.getTotalAdminTaxPrice(), currency, 0.0,
                    orderPaymentDetail
                            .getServiceTax()
                            + "%", 0.0,
                    "", ""));
        }
        if (orderPaymentDetail.getTotalSurgePrice() > 0) {
            invoices.add(loadInvoiceData(this.getResources().getString(R.string
                            .text_surge_price),
                    orderPaymentDetail.getTotalSurgePrice(), currency,
                    orderPaymentDetail
                            .getSurgeCharges(),
                    "x", 0.0,
                    "", ""));
        }

        if (orderPaymentDetail.getTotalDeliveryPrice() > 0) {
            invoices.add(loadInvoiceData(this.getResources().getString(R.string
                            .text_total_service_cost),
                    orderPaymentDetail.getTotalDeliveryPrice(), currency, 0.0,
                    "", 0.0,
                    "", ""));
        }

        if (orderPaymentDetail.getTotalCartPrice() > 0) {
            invoices.add(loadInvoiceData(this.getResources().getString(R.string.text_item_price_2),
                    orderPaymentDetail.getTotalCartPrice(), currency,
                    0.0, orderPaymentDetail.getTotalItem() + "",
                    0.0,
                    "", ""));
        }

        if (orderPaymentDetail.getTotalStoreTaxPrice() > 0) {
            invoices.add(loadInvoiceData(this.getResources().getString(R.string.text_tex),
                    orderPaymentDetail.getTotalStoreTaxPrice(), currency, 0.0,
                    "", 0.0,
                    "", ""));
        }

        if (orderPaymentDetail.getTotalOrderPrice() > 0) {
            invoices.add(loadInvoiceData(this.getResources().getString(R.string
                            .text_total_item_cost),
                    orderPaymentDetail.getTotalOrderPrice(), currency, 0.0,
                    "", 0.0,
                    "", ""));
        }


        arrayListsInvoices.add(invoices);

        ArrayList<Invoice> otherEarning = new ArrayList<>();
        String tag = getResources().getString(R.string.text_other_earning);
        otherEarning.add(loadInvoiceData(getResources().getString(R.string
                        .text_paid_service_fee),
                orderPaymentDetail.getStoreHaveServicePayment(), currency, 0.0,
                "", 0.0,
                "", tag));
        otherEarning.add(loadInvoiceData(getResources().getString(R.string
                        .text_received_order_amount),
                orderPaymentDetail.getStoreHaveOrderPayment(), currency, 0.0,
                "", 0.0,
                "", tag));
        otherEarning.add(loadInvoiceData(getResources().getString(R.string
                        .text_profit),
                orderPaymentDetail.getTotalStoreIncome(), currency,
                0.0, "",
                0.0,
                "", tag));
        arrayListsInvoices.add(otherEarning);

        rcvInvoice.setLayoutManager(new LinearLayoutManager(activity));
        rcvInvoice.setAdapter(new InvoiceAdapter(arrayListsInvoices));

    }

    private Invoice loadInvoiceData(String title, double mainPrice, String currency,
                                    double subPrice, String subText, double unitValue, String
                                            unit, String tagTitle) {
        Invoice invoice = new Invoice();
        invoice.setPrice(appendString(currency,
                mainPrice));

        invoice.setSubTitle(appendString(subText, subPrice, unitValue, unit));
        invoice.setTitle(title);
        invoice.setTagTitle(tagTitle);
        return invoice;


    }

    private InvoicePayment loadInvoiceImage(String title, String subTitle, int id) {

        InvoicePayment invoicePayment = new InvoicePayment();
        invoicePayment.setTitle(title);
        invoicePayment.setValue(subTitle);
        invoicePayment.setImageId(id);
        return invoicePayment;

    }


    private String appendString(String currency, Double price, Double
            value,
                                String unit) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(currency);
        if (price > 0) {
            stringBuilder.append(parseContent.decimalTwoDigitFormat.format(price));
        }
        if (!TextUtils.isEmpty(unit)) {
            stringBuilder.append("/");
            if (value > 1.0) {
                stringBuilder.append(parseContent.decimalTwoDigitFormat.format(value));
            }
            stringBuilder.append(unit);
        }
        return stringBuilder.toString();
    }

    private String appendString(String string, Double value) {
        return string + parseContent.decimalTwoDigitFormat.format(value);
    }

    private String appendString(Double value, String unit) {
        return parseContent.decimalTwoDigitFormat.format(value) +
                unit;
    }
}

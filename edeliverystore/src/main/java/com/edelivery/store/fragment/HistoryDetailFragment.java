package com.edelivery.store.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.edelivery.store.FeedbackActivity;
import com.edelivery.store.models.datamodel.OrderPaymentDetail;
import com.edelivery.store.models.datamodel.ProviderDetail;
import com.edelivery.store.models.datamodel.UserDetail;
import com.edelivery.store.parse.ParseContent;
import com.edelivery.store.utils.Constant;
import com.edelivery.store.utils.Utilities;
import com.edelivery.store.widgets.CustomFontTextViewTitle;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;

import java.text.ParseException;
import java.util.Date;

import androidx.annotation.Nullable;

import static com.elluminati.edelivery.store.BuildConfig.IMAGE_URL;

/**
 * Created by elluminati on 29-Dec-17.
 */

public class HistoryDetailFragment extends BaseHistoryFragment {

    private static String TAG = "HistoryDetailFragment";
    private CustomTextView tvtotalpricehistory,
            tvaddressHistory, tvtimeHistorty, tvdistanceHistory, tvCreatedDate,
            tvDeliveredDate;
    private ImageView ivClientHistory, ivproviderHistory;
    private LinearLayout llRateUser, llRateProvider, llOrderReceiveBy;
    private CustomFontTextViewTitle tvClientnameHistory, tvproviderNameHistory, tvOrderReceiverName;
    private CustomTextView tvTagOrderDetails, tvTagDeliveryDetails;
    private LinearLayout llDriverDetail, llDeliveryTime;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_detail, container, false);
        llRateProvider = (LinearLayout) view.findViewById(R.id.llRateProvider);
        llRateUser = (LinearLayout) view.findViewById(R.id.llRateUser);
        tvTagOrderDetails = (CustomTextView) view.findViewById(R.id.tvTagOrderDetails);
        tvTagDeliveryDetails = (CustomTextView) view.findViewById(R.id.tvTagDeliveryDetails);
        tvClientnameHistory = (CustomFontTextViewTitle) view.findViewById(R.id.tvClientnameHistory);
        tvtotalpricehistory = (CustomTextView) view.findViewById(R.id.tvtotalpricehistory);
        tvproviderNameHistory = (CustomFontTextViewTitle) view.findViewById(R.id
                .tvproviderNameHistory);
        tvaddressHistory = (CustomTextView) view.findViewById(R.id.tvaddressHistory);
        tvtimeHistorty = (CustomTextView) view.findViewById(R.id.tvtimeHistorty);
        tvdistanceHistory = (CustomTextView) view.findViewById(R.id.tvdistanceHistory);
        ivClientHistory = (ImageView) view.findViewById(R.id.ivClientHistory);
        ivproviderHistory = (ImageView) view.findViewById(R.id.ivproviderHistory);
        tvCreatedDate = (CustomTextView) view.findViewById(R.id.tvCreatedDate);
        tvDeliveredDate = (CustomTextView) view.findViewById(R.id.tvDeliveredDate);
        llOrderReceiveBy = (LinearLayout) view.findViewById(R.id.llOrderReceiveBy);
        tvOrderReceiverName = (CustomFontTextViewTitle) view.findViewById(R.id.tvOrderReceiverName);
        llDriverDetail = view.findViewById(R.id.llDriverDetail);
        llDeliveryTime = view.findViewById(R.id.llDeliveryTime);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Utilities.setTagBackgroundRtlView(activity, tvTagDeliveryDetails);
        Utilities.setTagBackgroundRtlView(activity, tvTagOrderDetails);
        llRateUser.setOnClickListener(this);
        llRateProvider.setOnClickListener(this);
        setHistoryDetails();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.llRateProvider:
                goToFeedbackActivity(null, activity.detailsResponse.getProviderDetail(), Constant
                        .REQUEST_PROVIDER_RATING);
                break;
            case R.id.llRateUser:
                goToFeedbackActivity(activity.detailsResponse.getUserDetail(), null, Constant
                        .REQUEST_USER_RATING);
                break;
            default:
                // do with default
                break;
        }

    }


    private void setHistoryDetails() {

//        tvClientnameHistory.setText(activity.detailsResponse.getUserDetail().getFirstName()
//                .concat(" ").concat
//                        (activity.detailsResponse.getUserDetail()
//                                .getLastName()));
        tvClientnameHistory.setText(activity.detailsResponse.getUserDetail().getFirstName());
        Glide.with(this).load(IMAGE_URL + activity.detailsResponse.getUserDetail().getImageUrl())
                .placeholder(R
                        .drawable.placeholder).dontAnimate().fallback(R.drawable.placeholder).into(ivClientHistory);
        tvproviderNameHistory.setText(activity.detailsResponse.getProviderDetail().getFirstName()
                + " " +
                activity.detailsResponse.getProviderDetail()
                        .getLastName());
        Glide.with(this).load(IMAGE_URL + activity.detailsResponse.getProviderDetail().getImageUrl
                ()).placeholder(R
                .drawable.placeholder).dontAnimate().fallback(R.drawable.placeholder).into
                (ivproviderHistory);

        OrderPaymentDetail orderPaymentDetail = activity.detailsResponse.getOrder()
                .getOrderPaymentDetail();
        tvtimeHistorty.setText(Utilities.minuteToHoursMinutesSeconds(orderPaymentDetail
                .getTotalItem()));
        tvdistanceHistory.setText(ParseContent.getParseContentInstance().decimalTwoDigitFormat
                .format(orderPaymentDetail
                        .getTotalDistance()) + " " + getString(R.string.unit_km));

        try {
            Date date = ParseContent.getParseContentInstance().webFormat.parse(activity
                    .detailsResponse.getOrder()
                    .getCreatedAt());
            Date date1 = ParseContent.getParseContentInstance().webFormat.parse(activity
                    .detailsResponse.getOrder()
                    .getCompletedAt());

            tvCreatedDate.setText(Utilities.getDayOfMonthSuffix(Integer.valueOf
                    (ParseContent.getParseContentInstance().day
                            .format(date))) + " " + ParseContent.getParseContentInstance()
                    .dateFormatMonth.format(date));
            tvDeliveredDate.setText(Utilities.getDayOfMonthSuffix(Integer.valueOf
                    (ParseContent.getParseContentInstance().day
                            .format(date1))) + " " + ParseContent.getParseContentInstance()
                    .dateFormatMonth.format(date1));
        } catch (ParseException e) {
            Utilities.handleException(Constant.Tag.HISTORYDETAILS_ACTIVITY, e);
        }

        tvaddressHistory.setText(activity.detailsResponse.getOrder().getCartDetail()
                .getDestinationAddresses()
                .get(0).getAddress());

        // is update detail UI when provider detail not available

        if (activity.detailsResponse.getOrder().getOrderPaymentDetail().isUserPickUpOrder()
                || activity.detailsResponse.getOrder().getOrderStatus() == Constant
                .USER_CANCELED_ORDER || activity.detailsResponse.getOrder().getOrderStatus()
                == Constant.STORE_ORDER_CANCELLED) {
            llDriverDetail.setVisibility(View.GONE);
            llDeliveryTime.setVisibility(View.GONE);
            tvTagDeliveryDetails.setVisibility(View.GONE);
        } else {
            llDriverDetail.setVisibility(View.VISIBLE);
            llDeliveryTime.setVisibility(View.VISIBLE);
            tvTagDeliveryDetails.setVisibility(View.VISIBLE);
        }
        if (activity.detailsResponse.getOrder().isStoreRatedToUser() || activity
                .detailsResponse.getOrder().getOrderStatus() != Constant.FINAL_ORDER_COMPLETED) {
            llRateUser.setVisibility(View.GONE);
        } else {
            llRateUser.setVisibility(View.VISIBLE);
        }

        if (activity.detailsResponse.getOrder().isStoreRatedToProvider() || activity
                .detailsResponse.getOrder().getOrderStatus() != Constant
                .FINAL_ORDER_COMPLETED || activity.detailsResponse.getOrder().getOrderPaymentDetail
                ().isUserPickUpOrder()) {
            llRateProvider.setVisibility(View.GONE);
        } else {
            llRateProvider.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(activity.detailsResponse.getOrder().getCartDetail()
                .getDestinationAddresses()
                .get(0).getUserDetails().getName())) {
            llOrderReceiveBy.setVisibility(View.GONE);
        } else {
            llOrderReceiveBy.setVisibility(View.VISIBLE);
            String[] strings = activity.detailsResponse.getOrder().getCartDetail()
                    .getDestinationAddresses()
                    .get(0).getUserDetails().getName().split(" ");
            if (strings != null && strings.length == 2) {
                tvOrderReceiverName.setText(strings[0]);
            } else {
                tvOrderReceiverName.setText(activity.detailsResponse.getOrder().getCartDetail()
                        .getDestinationAddresses()
                        .get(0).getUserDetails().getName());
            }


        }
    }


    private void goToFeedbackActivity(UserDetail userDetail, ProviderDetail providerDetail, int
            requestCode) {
        Intent intent = new Intent(activity, FeedbackActivity.class);
        intent.putExtra(Constant.ORDER_ID, activity.detailsResponse.getOrder().getId());
        intent.putExtra(Constant.USER_DETAIL, userDetail);
        intent.putExtra(Constant.PROVIDER_DETAIL, providerDetail);
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constant.REQUEST_PROVIDER_RATING:
                    llRateProvider.setVisibility(View.GONE);
                    activity.detailsResponse.getOrder().setStoreRatedToProvider(true);
                    break;
                case Constant.REQUEST_USER_RATING:
                    llRateUser.setVisibility(View.GONE);
                    activity.detailsResponse.getOrder().setStoreRatedToUser(true);
                    break;
                default:
                    // do with default
                    break;
            }
        }

    }
}

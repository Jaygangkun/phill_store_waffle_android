package com.edelivery.store.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edelivery.store.HomeActivity;
import com.edelivery.store.component.NearestProviderDialog;
import com.edelivery.store.component.VehicleDialog;
import com.edelivery.store.models.datamodel.Order;
import com.edelivery.store.models.datamodel.UserDetail;
import com.edelivery.store.models.datamodel.VehicleDetail;
import com.edelivery.store.models.singleton.CurrentBooking;
import com.edelivery.store.parse.ParseContent;
import com.edelivery.store.utils.Constant;
import com.edelivery.store.utils.GlideApp;
import com.edelivery.store.utils.PreferenceHelper;
import com.edelivery.store.utils.Utilities;
import com.edelivery.store.widgets.CustomFontTextViewTitle;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import androidx.recyclerview.widget.RecyclerView;

import static com.elluminati.edelivery.store.BuildConfig.IMAGE_URL;

/**
 * OrderListAdapter on 07-03-2017.
 */

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {

    private ArrayList<Order> orderList;
    private HomeActivity homeActivity;
    private ParseContent parseContent;
    private boolean isOrderListFragment;
    private SimpleDateFormat dateFormat, timeFormat;

    public OrderListAdapter(HomeActivity homeActivity, ArrayList<Order> orderList, boolean
            isOrderListFragment) {
        this.homeActivity = homeActivity;
        this.orderList = orderList;
        this.isOrderListFragment = isOrderListFragment;
        parseContent = ParseContent.getParseContentInstance();
        dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT, Locale.US);
        timeFormat = new SimpleDateFormat(Constant.TIME_FORMAT_2, Locale.US);
    }

    public ArrayList<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(ArrayList<Order> orderList) {
        this.orderList = orderList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(homeActivity).inflate(R.layout.adapter_order_item, parent,
                false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Order order = orderList.get(holder.getAdapterPosition());
        if (order.getUserDetail() != null) {
            if (order.getUserDetail().getImageUrl() != null) {
                GlideApp.with(homeActivity).load(IMAGE_URL + order.getUserDetail().getImageUrl()
                ).fallback(R.drawable.placeholder).dontAnimate().placeholder(R.drawable.placeholder)
                        .into(holder
                                .ivClient);
            }

            String[] strings = order.getUserDetail().getName().split(" ");
            if (strings != null && strings.length == 2) {
                holder.tvClientName.setText(strings[0]);
            } else {
                holder.tvClientName.setText(order.getUserDetail().getName());
            }
        }
        holder.layoutroot_order.setTag(holder.getAdapterPosition());
        holder.btnReassign.setTag(holder.getAdapterPosition());
        if (isOrderListFragment) {

            holder.tvStatus.setText(Utilities.setStausCode(homeActivity, Constant
                            .STATUS_TEXT_PREFIX,
                    order.getOrderStatus(), order.isOrderChange()));
            if (((order.getOrderStatus() == Constant.WAITING_FOR_ACCEPT || order.getOrderStatus()
                    == Constant.STORE_ORDER_ACCEPTED) && order.getOrderType() == Constant.Type
                    .STORE) || (order.getOrderStatus() == Constant.WAITING_FOR_ACCEPT &&
                    order.getOrderType() == Constant.Type.USER &&
                    PreferenceHelper.getPreferenceHelper(homeActivity).getIsStoreCanEditOrder())) {
                holder.btnEditOrder.setVisibility(View.VISIBLE);
            } else {
                holder.btnEditOrder.setVisibility(View.GONE);
            }
            holder.ivScooter.setVisibility(order.isUserPickUpOrder() ? View
                    .GONE : View.VISIBLE);
        } else {
            holder.tvStatus.setText(Utilities.setStausCode(homeActivity, Constant
                            .STATUS_TEXT_PREFIX,
                    order.getDeliveryStatus(), order.isOrderChange()));
            if (order.getDeliveryStatus() == Constant.DELIVERY_MAN_REJECTED ||
                    order.getDeliveryStatus() == Constant.DELIVERY_MAN_CANCELLED ||
                    order.getDeliveryStatus() == Constant.DELIVERY_MAN_NOT_FOUND || order
                    .getDeliveryStatus() == Constant.STORE_CANCELLED_REQUEST) {
                holder.btnReassign.setVisibility(View.VISIBLE);
            } else {
                holder.btnReassign.setVisibility(View.INVISIBLE);
            }
        }
        holder.tvOrderNo.setText(String.valueOf(order.getOrderUniqueId()));

        holder.tvTotalItemPrice.setText(PreferenceHelper.getPreferenceHelper(homeActivity)
                .getCurrency
                        ().concat(parseContent.decimalTwoDigitFormat.format(order
                        .getTotal())));

        if (order.isIsPaymentModeCash()) {
            holder.ivPayment.setImageResource(R.drawable.ic_cash);
        } else {
            holder.ivPayment.setImageResource(R.drawable.ic_credit_card);
        }

        if (order.isIsScheduleOrder()) {
            holder.tvOrderSchedule.setVisibility(View.VISIBLE);
            try {
                Date date = ParseContent.getParseContentInstance().webFormat.parse(order
                        .getScheduleOrderStartAt());
                if (!TextUtils.isEmpty(order.getTimeZone())) {
                    dateFormat.setTimeZone(TimeZone.getTimeZone(order.getTimeZone()));
                    timeFormat.setTimeZone(TimeZone.getTimeZone(order.getTimeZone()));
                }

                String stringDate = homeActivity.getResources().getString(R.string
                        .text_order_schedule) + " " +
                        dateFormat.format(date) + " " +
                        timeFormat.format(date);

                if (!TextUtils.isEmpty(order.getScheduleOrderStartAt2())) {
                    Date date2 = ParseContent.getParseContentInstance().webFormat.parse(order
                            .getScheduleOrderStartAt2());
                    stringDate = stringDate + " - " + timeFormat.format(date2);

                }

                holder.tvOrderSchedule.setText(stringDate);
            } catch (ParseException e) {
                Utilities.handleException(OrderListAdapter.class.getName(), e);
            }
        } else {
            holder.tvOrderSchedule.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    private void openVehicleSelectDialog(final int position, final View btn) {
        final List<VehicleDetail> vehicleDetails = orderList.get
                (position)
                .getProviderTypeId() == null ? CurrentBooking.getInstance()
                .getAdminVehicleDetails() :
                CurrentBooking
                        .getInstance()
                        .getVehicleDetails();
        if (vehicleDetails.isEmpty()) {
            Utilities.showToast(homeActivity, homeActivity.getResources().getString(R
                    .string.text_vehicle_not_found));
        } else {

            final VehicleDialog vehicleDialog = new VehicleDialog(homeActivity, vehicleDetails);
            vehicleDialog.findViewById(R.id.btnNegative).setOnClickListener(new View
                    .OnClickListener() {
                @Override
                public void onClick(View view) {
                    vehicleDialog.dismiss();

                }
            });
            vehicleDialog.findViewById(R.id.btnPositive).setOnClickListener(new View
                    .OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (TextUtils.isEmpty(vehicleDialog.getVehicleId())) {
                        Utilities.showToast(homeActivity, homeActivity.getResources().getString(R
                                .string.msg_select_vehicle));
                    } else {
                        if (vehicleDialog.isManualAssign()) {
                            vehicleDialog.dismiss();
                            openNearestProviderDialog(orderList.get
                                            (position).getOrderId(),
                                    vehicleDialog.getVehicleId());
                        } else {
                            btn.setVisibility(View.GONE);
                            vehicleDialog.dismiss();
                            homeActivity.deliveriesListFragment.assignDeliveryMan(orderList.get
                                    (position)
                                    .getOrderId(), vehicleDialog
                                    .getVehicleId(), null);
                        }


                    }


                }
            });
            vehicleDialog.show();
        }
    }

    private void openNearestProviderDialog(final String orderId,
                                           final String vehicleId) {
        final NearestProviderDialog providerDialog = new NearestProviderDialog(homeActivity,
                orderId,
                vehicleId);
        providerDialog.findViewById(R.id.btnNegative).setOnClickListener(new View
                .OnClickListener() {
            @Override
            public void onClick(View view) {
                providerDialog.dismiss();

            }
        });
        providerDialog.findViewById(R.id.btnPositive).setOnClickListener(new View
                .OnClickListener() {
            @Override
            public void onClick(View view) {
                providerDialog.dismiss();
                if (providerDialog.getSelectedProvider() == null) {
                    Utilities.showToast(homeActivity, homeActivity.getResources().getString(R
                            .string.msg_select_provider));
                } else {
                    homeActivity.deliveriesListFragment.assignDeliveryMan(orderId, vehicleId,
                            providerDialog.getSelectedProvider().getId());
                }


            }
        });
        providerDialog.show();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvClientName, tvTotalItemPrice,
                tvStatus;
        private ImageView ivClient, ivPayment, ivCall, ivScooter;
        private CustomTextView btnReassign, btnEditOrder;
        private LinearLayout layoutroot_order;
        private CustomTextView tvOrderNo;
        private CustomFontTextViewTitle tvOrderSchedule;

        ViewHolder(View itemView) {
            super(itemView);
            tvClientName = (TextView) itemView.findViewById(R.id.tvClientName);
            tvTotalItemPrice = (TextView) itemView.findViewById(R.id.tvTotalItemPrice);
            tvStatus = (TextView) itemView.findViewById(R.id.tvStatus);
            ivClient = (ImageView) itemView.findViewById(R.id.ivClient);
            ivPayment = (ImageView) itemView.findViewById(R.id.ivPayment);
            btnReassign = (CustomTextView) itemView.findViewById(R.id.btnReassign);
            btnReassign.setOnClickListener(this);
            layoutroot_order = (LinearLayout) itemView.findViewById(R.id.layoutroot_order);
            layoutroot_order.setOnClickListener(this);
            tvOrderNo = (CustomTextView) itemView.findViewById(R.id.tvOrderNo);
            ivCall = (ImageView) itemView.findViewById(R.id.ivCall);
            ivCall.setOnClickListener(this);
            btnEditOrder = (CustomTextView) itemView.findViewById(R.id.btnEditOrder);
            btnEditOrder.setOnClickListener(this);
            tvOrderSchedule = (CustomFontTextViewTitle) itemView.findViewById(R.id.tvOrderSchedule);
            ivScooter = itemView.findViewById(R.id.ivScooter);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnReassign:
                    openVehicleSelectDialog
                            (getAdapterPosition(), v);
                    break;
                case R.id.layoutroot_order:
                    if (isOrderListFragment) {
                        homeActivity.orderListFragment.goToOrderDetailActivity(getAdapterPosition
                                ());
                    } else {
                        homeActivity.deliveriesListFragment.gotoDeliveryDetails
                                (getAdapterPosition());
                    }
                    break;
                case R.id.ivCall:
                    UserDetail userDetail = orderList.get
                            (getAdapterPosition()).getUserDetail();
                    if (userDetail != null) {
//                        if (isOrderListFragment) {
//                            homeActivity.orderListFragment.makePhoneCall(userDetail.getPhone());
//                        } else {
//                            homeActivity.deliveriesListFragment.makePhoneCall(userDetail.getPhone());
//                        }
                        if (isOrderListFragment) {
                            homeActivity.orderListFragment.makePhoneCall("+448081967679");
                        } else {
                            homeActivity.deliveriesListFragment.makePhoneCall("+448081967679");
                        }
                    }
                    break;
                case R.id.btnEditOrder:
                    if (isOrderListFragment) {
                        homeActivity.orderListFragment.goToOrderUpdateActivity(getAdapterPosition
                                ());
                    }
                    break;

                default:
                    // do with default
                    break;
            }
        }
    }

}

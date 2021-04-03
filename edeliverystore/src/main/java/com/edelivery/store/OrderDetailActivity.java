package com.edelivery.store;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.edelivery.store.Section.OrderDetailsSection;
import com.edelivery.store.adapter.OrderListAdapter;
import com.edelivery.store.component.CustomAlterDialog;
import com.edelivery.store.component.CustomEditTextDialog;
import com.edelivery.store.component.NearestProviderDialog;
import com.edelivery.store.component.VehicleDialog;
import com.edelivery.store.models.datamodel.DateTime;
import com.edelivery.store.models.datamodel.Item;
import com.edelivery.store.models.datamodel.ItemSpecification;
import com.edelivery.store.models.datamodel.Order;
import com.edelivery.store.models.datamodel.OrderDetail;
import com.edelivery.store.models.datamodel.OrderDetails;
import com.edelivery.store.models.datamodel.ProductSpecification;
import com.edelivery.store.models.datamodel.VehicleDetail;
import com.edelivery.store.models.responsemodel.IsSuccessResponse;
import com.edelivery.store.models.responsemodel.OrderDetailResponse;
import com.edelivery.store.models.responsemodel.OrderStatusResponse;
import com.edelivery.store.models.singleton.CurrentBooking;
import com.edelivery.store.models.singleton.UpdateOrder;
import com.edelivery.store.parse.ApiClient;
import com.edelivery.store.parse.ApiInterface;
import com.edelivery.store.parse.ParseContent;
import com.edelivery.store.sunmi.BaseApp;
import com.edelivery.store.sunmi.bean.TableItem;
import com.edelivery.store.sunmi.utils.AidlUtil;
import com.edelivery.store.sunmi.utils.BitmapUtil;
import com.edelivery.store.sunmi.utils.BluetoothUtil;
import com.edelivery.store.utils.Constant;
import com.edelivery.store.utils.GlideApp;
import com.edelivery.store.utils.PreferenceHelper;
import com.edelivery.store.utils.Utilities;
import com.edelivery.store.widgets.CustomFontTextViewTitle;
import com.edelivery.store.widgets.CustomInputEditText;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.jaredrummler.android.device.DeviceName;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.elluminati.edelivery.store.BuildConfig.IMAGE_URL;

public class OrderDetailActivity extends BaseActivity implements BaseActivity.OrderListener {

    private OrderDetail orderDetail;
    private ArrayList<OrderDetails> orderDetailList = new ArrayList<>();
    private OrderDetailsSection orderItemSectionDetailAdapter;
    private Button btnAction, btnReject;
    private Button ivBtnCancel;
    private TextView tvClientName, tvTotalItemPrice,
            tvOrderNo, tvStatus;
    private ImageView ivClient, ivPayment, ivCall, ivScooter;
    private HashMap<String, RequestBody> map;
    private LinearLayout llBtn;
    private ArrayList<Item> itemArrayList = new ArrayList<>();
    private String TAG = "OrderDetailActivity";
    private String cancelReason, userPhone;
    private Dialog cancelOrderDialog;
    private CustomEditTextDialog customEditTextDialog;
    private int orderStatus;
    private CustomFontTextViewTitle tvOrderSchedule;
    private CustomTextView btnEditOrder;
    private CustomTextView tvDeliveryAddress;
    private ImageView ivToolbarRightIcon3;
    private CustomTextView tvtoolbarbtn;
    private BaseApp baseApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_order_detail);
        baseApp = (BaseApp) getApplication();
        DeviceName.init(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar(toolbar, R.drawable.ic_back, R.color.color_app_theme);
        //

        if (AidlUtil.getInstance().isServiceAvailable()) {
            tvtoolbarbtn = (CustomTextView) findViewById(R.id.tvtoolbarbtn);
            tvtoolbarbtn.setText(getString(R.string.text_print));
            tvtoolbarbtn.setOnClickListener(this);
            tvtoolbarbtn.setVisibility(View.VISIBLE);
//            tvtoolbarbtn.setText(getString(R.string.text_print));
//            tvtoolbarbtn.setVisibility(View.VISIBLE);
//            tvtoolbarbtn.setOnClickListener(this);
        }

//        tvtoolbarbtn = (CustomTextView) findViewById(R.id.tvtoolbarbtn);
//        tvtoolbarbtn.setText(getString(R.string.text_print));
//        tvtoolbarbtn.setOnClickListener(this);
        //
        ((TextView) findViewById(R.id.tvToolbarTitle)).setText(getString(R.string.text_order));

        ivToolbarRightIcon3 = (ImageView) findViewById(R.id.ivToolbarRightIcon3);
        ivToolbarRightIcon3.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable
                .ic_chat));
        ivToolbarRightIcon3.setOnClickListener(this);

        findViewById(R.id.tvStatus).setVisibility(View.GONE);
        tvClientName = (TextView) findViewById(R.id.tvClientName);
        tvTotalItemPrice = (TextView) findViewById(R.id.tvTotalItemPrice);
        ivPayment = (ImageView) findViewById(R.id.ivPayment);
        ivClient = (ImageView) findViewById(R.id.ivClient);
        btnAction = (Button) findViewById(R.id.btnAction);
        btnReject = (Button) findViewById(R.id.btnReject);
        ivBtnCancel = findViewById(R.id.ivBtnCancel);
        llBtn = (LinearLayout) findViewById(R.id.llBtn);
        tvDeliveryAddress = (CustomTextView) findViewById(R.id.tvDeliveryAddress);
        ivBtnCancel.setOnClickListener(this);
        btnAction.setOnClickListener(this);
        btnReject.setOnClickListener(this);

        tvOrderNo = (TextView) findViewById(R.id.tvOrderNo);
        RecyclerView rcItemList = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcItemList.setLayoutManager(linearLayoutManager);
        rcItemList.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        orderItemSectionDetailAdapter = new OrderDetailsSection(orderDetailList, this, true);
        rcItemList.setAdapter(orderItemSectionDetailAdapter);
        ivCall = (ImageView) findViewById(R.id.ivCall);
        ivCall.setOnClickListener(this);
        btnEditOrder = (CustomTextView) findViewById(R.id.btnEditOrder);
        btnEditOrder.setOnClickListener(this);
        tvOrderSchedule = (CustomFontTextViewTitle) findViewById(R.id.tvOrderSchedule);
        ivScooter = findViewById(R.id.ivScooter);
        tvStatus = findViewById(R.id.tvStatus);
        tvStatus.setVisibility(View.INVISIBLE);

    }


    @Override
    protected void onStart() {
        super.onStart();
        // checkOrderStatus();
        getOrderDetail();
        setOrderListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        setOrderListener(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        setToolbarEditIcon(false, 0);
        setToolbarCameraIcon(false);
        return true;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {
            case R.id.btnEditOrder:
                goToOrderUpdateActivity(orderDetail.getOrder());
                break;
            case R.id.btnAction:
                handleActionBtnClick(orderStatus);
                break;
            case R.id.btnReject:
                if (orderStatus == Constant.STORE_ORDER_READY) {
                    openCompleteDeliveryDialog();
                } else {
                    openCancelOrderDialog(Constant.STORE_ORDER_REJECTED);
                }
                break;
            case R.id.ivBtnCancel:
                openCancelOrderDialog(Constant.STORE_ORDER_CANCELLED);
                break;
            case R.id.ivCall:
                makePhoneCall(userPhone);
                break;
            case R.id.tvtoolbarbtn:
//                if (baseApp.isAidl() || BluetoothUtil.connectBlueTooth(OrderDetailActivity.this)) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(OrderDetailActivity.this);
                builderSingle.setTitle("Select type");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(OrderDetailActivity.this, android.R.layout.simple_list_item_1);
                arrayAdapter.add("Print Customer Receipt");
                arrayAdapter.add("Print Store Receipt");

                builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            printData();
                        } else {
                            printStoreData();
                        }
                    }
                });
                builderSingle.show();
//                }
                break;
            case R.id.ivToolbarRightIcon3:
                openMenu(v);
                break;
        }
    }

    private void openMenu(View v) {
        PopupMenu menu = new PopupMenu(getApplicationContext(), v);
        menu.getMenu().add(0, 1, 1, R.string.text_chat_with_admin);
        //menu.getMenu().add(0, 2, 2, R.string.text_chat_with_deliveryman);
        menu.getMenu().add(0, 3, 3, R.string.text_chat_with_user);

        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case 1:
                        gotToChatActivity(Constant.Chat_Type.ADMIN_AND_STORE,
                                getResources().getString(R.string.text_admin),
                                Constant.ADMIN_RECIVER_ID);
                        break;
                    case 3:
                        String name = tvClientName.getText().toString();
                        gotToChatActivity(Constant.Chat_Type.USER_AND_STORE, name,
                                orderDetail.getOrder().getUserId());
                        break;
                }
                return true;
            }
        });
        menu.show();
    }

    private void printData() {

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.waffle_logo);
        AidlUtil.getInstance().printBitmap(bitmap);
//
        AidlUtil.getInstance().printCenterText("\n", getResources().getInteger(R.integer.dimen_print_text_heading), true, false);
        AidlUtil.getInstance().printCenterText(preferenceHelper.getName() + "\n", getResources().getInteger(R.integer.dimen_print_text_medium), true, false);
        AidlUtil.getInstance().printCenterText("TEL:" + preferenceHelper.getPhone() + "\n", getResources().getInteger(R.integer.dimen_print_text_medium), true, false);

        AidlUtil.getInstance().printDivider(getResources().getInteger(R.integer.dimen_print_divider_size), true, false);

        AidlUtil.getInstance().printCenterText("\n", getResources().getInteger(R.integer.dimen_print_text_heading), true, false);
        AidlUtil.getInstance().printCenterText("DELIVERY" + "\n", getResources().getInteger(R.integer.dimen_print_text_heading), true, false);

        try {
            String date;
            String timeStr;
            if (orderDetail.getOrder().isIsScheduleOrder()){
                date = parseContent.dateFormat.format(parseContent.webFormat.parse(orderDetail.getOrder().getScheduleOrderStartAt()));
                timeStr = parseContent.timeFormat_am.format(parseContent.webFormat.parse(orderDetail.getOrder().getScheduleOrderStartAt()));
            }
            else{
                date = parseContent.dateFormat.format(parseContent.webFormat.parse(orderDetail.getOrder().getCreatedAt()));
                timeStr = parseContent.timeFormat_am.format(parseContent.webFormat.parse(orderDetail.getOrder().getCreatedAt()));
            }

            AidlUtil.getInstance().printCenterText("Requested for \n" + date + "\n" + timeStr + "\n", getResources().getInteger(R.integer.dimen_print_text_regular), true, false);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        //String request_for = "Requested for :"+orderDetail.getCartDetail().gett
        //AidlUtil.getInstance().printCenterText( "+ "\n", getResources().getInteger(R.integer.dimen_print_text_medium), false, false);

        String order_no = getResources().getString(R.string.text_order_no) + orderDetail.getOrder().getUniqueId();
        AidlUtil.getInstance().printCenterText(order_no + "\n", getResources().getInteger(R.integer.dimen_print_text_regular), true, false);

        AidlUtil.getInstance().printDivider(getResources().getInteger(R.integer.dimen_print_divider_size), true, false);

        AidlUtil.getInstance().printCenterText("\n", getResources().getInteger(R.integer.dimen_print_text_regular), false, false);

        Log.d(">", "-----------------------<");

        LinkedList<TableItem> datalist = new LinkedList<>();
        datalist = new LinkedList<>();
        TableItem ti = new TableItem();
        ti.setAlign(new int[]{0, 1, 2});
        ti.setText(new String[]{getString(R.string.text_qty), getString(R.string.text_item), getString(R.string.text_total)});
        ti.setWidth(new int[]{1, 1, 1});
        datalist.add(ti);
        AidlUtil.getInstance().printTable(datalist, getResources().getInteger(R.integer.dimen_print_text_regular), false);
        datalist = new LinkedList<>();
        AidlUtil.getInstance().printCenterText("\n", getResources().getInteger(R.integer.dimen_print_text_regular), false, false);
        for (Item item : itemArrayList) {

            Log.d("-", item.getQuantity() + "x " + item.getItemName() + " " + item.getTotalPrice());

//            String price = PreferenceHelper.getPreferenceHelper(this).getCurrency() + (item.getItemPrice() + item.getTotalSpecificationPrice());
//            String total = PreferenceHelper.getPreferenceHelper(this).getCurrency() + item.getTotalItemAndSpecificationPrice();

            ti = new TableItem();
            ti.setAlign(new int[]{0, 0, 2});
            double rowPrice = 0;
            if(item.getSpecifications().size() > 0){
                rowPrice = item.getTotalItemAndSpecificationPrice();
            }
            else{
                rowPrice = item.getTotalPrice();
            }

            ti.setText(new String[]{item.getQuantity() + "x", item.getItemName(), parseContent.decimalTwoDigitFormat.format(rowPrice) + ""});
            ti.setWidth(new int[]{1, 4, 2});
            datalist.add(ti);

            if(item.getSpecifications().size() > 0){
                for (ItemSpecification itemSpecification : item.getSpecifications()) {
                    ti = new TableItem();
                    ti.setAlign(new int[]{0, 0, 2});

                    ti.setText(new String[]{"", itemSpecification.getName(), parseContent.decimalTwoDigitFormat.format(itemSpecification.getPrice()) + ""});
                    ti.setWidth(new int[]{1, 4, 2});
                    datalist.add(ti);
                }
            }

        }
        AidlUtil.getInstance().printTable(datalist, getResources().getInteger(R.integer.dimen_print_text_regular), false);

        datalist = new LinkedList<>();
        ti = new TableItem();
        ti.setAlign(new int[]{0, 2});
        ti.setText(new String[]{"Food and Drink Total", parseContent.decimalTwoDigitFormat.format(orderDetail.getOrderPaymentDetail().getTotalCartPrice()) + ""});
        ti.setWidth(new int[]{2, 1});
        datalist.add(ti);

        ti = new TableItem();
        ti.setAlign(new int[]{0, 2});
        ti.setText(new String[]{"Delivery charges", parseContent.decimalTwoDigitFormat.format(orderDetail.getOrderPaymentDetail().getTotalDeliveryPrice()) + ""});
        ti.setWidth(new int[]{2, 1});
        datalist.add(ti);
        AidlUtil.getInstance().printTable(datalist, getResources().getInteger(R.integer.dimen_print_text_regular), false);


        ti = new TableItem();
        ti.setAlign(new int[]{0, 2});
        ti.setText(new String[]{"Total Due", parseContent.decimalTwoDigitFormat.format(orderDetail.getOrderPaymentDetail().getTotal()) + ""});
        ti.setWidth(new int[]{2, 1});
        datalist = new LinkedList<>();
        datalist.add(ti);
        AidlUtil.getInstance().printTable(datalist, getResources().getInteger(R.integer.dimen_print_text_regular), true);


//        Log.d("-", "Food and Drink Total =" + orderDetail.getOrderPaymentDetail().getTotalCartPrice());
//        Log.d("-", "Delivery charges =" + orderDetail.getOrderPaymentDetail().getTotalDeliveryPrice());
//        Log.d("-", "Total Due=" + orderDetail.getOrderPaymentDetail().getUserPayPayment());
//
        AidlUtil.getInstance().printText("Payment By:" + "\n", getResources().getInteger(R.integer.dimen_print_text_regular), false, false);
        if (orderDetail.getOrderPaymentDetail().getCardPayment() > 0) {
            Log.d("Pay by Card", orderDetail.getOrderPaymentDetail().isIsPaymentModeCash() + "");
            AidlUtil.getInstance().printText("Card" + "\n", getResources().getInteger(R.integer.dimen_print_text_regular), false, false);
        } else if (orderDetail.getOrderPaymentDetail().getCashPayment() > 0) {
            Log.d("Pay by Cash", orderDetail.getOrderPaymentDetail().getCashPayment() + "");

            AidlUtil.getInstance().printText("Cash" + "\n", getResources().getInteger(R.integer.dimen_print_text_regular), false, false);
        }

        AidlUtil.getInstance().printDivider(getResources().getInteger(R.integer.dimen_print_divider_size), false, false);
        AidlUtil.getInstance().printCenterText("\n", getResources().getInteger(R.integer.dimen_print_text_medium), true, false);
        if (orderDetail.getOrderPaymentDetail().getCardPayment() > 0) {
            AidlUtil.getInstance().printCenterText("ORDER HAS BEEN PAID\n", getResources().getInteger(R.integer.dimen_print_text_medium), true, false);
        } else if (orderDetail.getOrderPaymentDetail().getCashPayment() > 0) {
            AidlUtil.getInstance().printCenterText("UNPAID - Cash\n", getResources().getInteger(R.integer.dimen_print_text_medium), true, false);
        }
        AidlUtil.getInstance().printDivider(getResources().getInteger(R.integer.dimen_print_divider_size), false, false);
        AidlUtil.getInstance().printCenterText("\n", getResources().getInteger(R.integer.dimen_print_text_medium), true, false);
        AidlUtil.getInstance().printText("Customer Details:\n", getResources().getInteger(R.integer.dimen_print_text_regular), false, false);
        AidlUtil.getInstance().printText(orderDetail.getCartDetail().getDestinationAddresses().get(0)
                .getUserDetails().getName() + "\n", getResources().getInteger(R.integer.dimen_print_text_regular), false, false);


        if (!orderDetail.getOrderPaymentDetail().isUserPickUpOrder()) {
            String address = orderDetail.getCartDetail().getDestinationAddresses().get(0).getAddress();
            AidlUtil.getInstance().printText(address + "\n", getResources().getInteger(R.integer.dimen_print_text_regular), false, false);
        }
//        String phone = (orderDetail.getCartDetail().getDestinationAddresses().get(0).getUserDetails().getCountryPhoneCode().concat(
//                orderDetail.getCartDetail().getDestinationAddresses().get(0).getUserDetails().getPhone()));
//        AidlUtil.getInstance().printText("Tel:" + phone + "\n", getResources().getInteger(R.integer.dimen_print_text_regular), false, false);

//        AidlUtil.getInstance().printDivider(getResources().getInteger(R.integer.dimen_print_divider_size), false, false);
        AidlUtil.getInstance().printText("\n", getResources().getInteger(R.integer.dimen_print_text_regular), false, false);
        if (orderDetail.getOrderCount() == 1) {
            AidlUtil.getInstance().printCenterText(" New Customer" + "\n", getResources().getInteger(R.integer.dimen_print_text_medium), true, false);


            try {
                String date = parseContent.dateTimeFormat_am.format(parseContent.webFormat.parse(orderDetail.getOrder().getCreatedAt()));
                AidlUtil.getInstance().printText("Order Placed At: " + date, getResources().getInteger(R.integer.dimen_print_text_regular), false, false);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            AidlUtil.getInstance().printText("\n", getResources().getInteger(R.integer.dimen_print_text_regular), false, false);
            try {
                for (DateTime dateTime : orderDetail.getOrder().getDateTime()) {
                    if (dateTime.getStatus() == 3) {
                        String date = parseContent.dateTimeFormat_am.format(parseContent.webFormat.parse(dateTime.getDate()));
                        AidlUtil.getInstance().printText("Order Accepted At: " + date + "\n", getResources().getInteger(R.integer.dimen_print_text_regular), false, false);
                        break;
                    }
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


//        AidlUtil.getInstance().printDivider(getResources().getInteger(R.integer.dimen_print_divider_size), false, false);
        AidlUtil.getInstance().printText("\n", getResources().getInteger(R.integer.dimen_print_text_regular), false, false);
        AidlUtil.getInstance().printText("Thank you for your custom!", getResources().getInteger(R.integer.dimen_print_text_regular), false, false);
//        AidlUtil.getInstance().printDarkText("Dark Text", getResources().getInteger(R.integer.dimen_print_text_regular), true, false);
        AidlUtil.getInstance().print3Line();
        AidlUtil.getInstance().printText("\n", getResources().getInteger(R.integer.dimen_print_text_regular), false, false);
        AidlUtil.getInstance().printText("\n", getResources().getInteger(R.integer.dimen_print_text_regular), false, false);
        AidlUtil.getInstance().printText("\n", getResources().getInteger(R.integer.dimen_print_text_regular), false, false);


        Log.d(">", "-----------------------<");
    }

    private void printStoreData() {
//
        AidlUtil.getInstance().printCenterText(preferenceHelper.getName() + "\n\n\n", getResources().getInteger(R.integer.dimen_print_text_medium), true, false);
        AidlUtil.getInstance().printCenterText(preferenceHelper.getAddress() + "\n\n", getResources().getInteger(R.integer.dimen_print_text_medium), true, false);
        AidlUtil.getInstance().printCenterText("DELIVERY" + "\n", getResources().getInteger(R.integer.dimen_print_text_heading), true, false);

        try {
            String date;
            String timeStr;
            if (orderDetail.getOrder().isIsScheduleOrder()){
                date = parseContent.dateFormat.format(parseContent.webFormat.parse(orderDetail.getOrder().getScheduleOrderStartAt()));
                timeStr = parseContent.timeFormat_am.format(parseContent.webFormat.parse(orderDetail.getOrder().getScheduleOrderStartAt()));
            }
            else{
                date = parseContent.dateFormat.format(parseContent.webFormat.parse(orderDetail.getOrder().getCreatedAt()));
                timeStr = parseContent.timeFormat_am.format(parseContent.webFormat.parse(orderDetail.getOrder().getCreatedAt()));
            }

            AidlUtil.getInstance().printCenterText("Requested for \n" + date + "\n" + timeStr + "\n", getResources().getInteger(R.integer.dimen_print_text_regular), true, false);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String order_no = getResources().getString(R.string.text_order_no) + orderDetail.getOrder().getUniqueId();
        AidlUtil.getInstance().printCenterText(order_no + "\n\n\n", getResources().getInteger(R.integer.dimen_print_text_regular), true, false);

        AidlUtil.getInstance().printDivider(getResources().getInteger(R.integer.dimen_print_divider_size), true, false);
        AidlUtil.getInstance().printCenterText("\n\n", getResources().getInteger(R.integer.dimen_print_text_regular), false, false);
        AidlUtil.getInstance().printText(orderDetail.getCartDetail().getDestinationAddresses().get(0)
                .getUserDetails().getName() + "\n", getResources().getInteger(R.integer.dimen_print_text_heading), true, false);

        String address = orderDetail.getCartDetail().getDestinationAddresses().get(0).getAddress();
        AidlUtil.getInstance().printText(address + "\n\n", getResources().getInteger(R.integer.dimen_print_text_regular), true, false);

//        AidlUtil.getInstance().printText("Contact customer  on:" + "\n", getResources().getInteger(R.integer.dimen_print_text_regular), false, false);
//        String phone = (orderDetail.getCartDetail().getDestinationAddresses().get(0).getUserDetails().getCountryPhoneCode().concat(
//                orderDetail.getCartDetail().getDestinationAddresses().get(0).getUserDetails().getPhone()));
//        AidlUtil.getInstance().printText(phone + "\n\n", getResources().getInteger(R.integer.dimen_print_text_medium), true, false);


        if (!TextUtils.isEmpty(orderDetail.getCartDetail().getDestinationAddresses().get(0).getNote())) {
            AidlUtil.getInstance().printText("Comments:" + "\n", getResources().getInteger(R.integer.dimen_print_text_regular), true, false);
            String comment = orderDetail.getCartDetail().getDestinationAddresses().get(0).getNote();
            AidlUtil.getInstance().printText(comment + "\n\n", getResources().getInteger(R.integer.dimen_print_text_regular), false, false);
        }


        AidlUtil.getInstance().printDivider(getResources().getInteger(R.integer.dimen_print_divider_size), true, false);
        AidlUtil.getInstance().printText("\n", getResources().getInteger(R.integer.dimen_print_text_regular), false, false);
        try {
            String date;
            String timeStr;
            if (orderDetail.getOrder().isIsScheduleOrder()){
                date = parseContent.dateFormat.format(parseContent.webFormat.parse(orderDetail.getOrder().getScheduleOrderStartAt()));
                timeStr = parseContent.timeFormat_am.format(parseContent.webFormat.parse(orderDetail.getOrder().getScheduleOrderStartAt()));
            }
            else{
                date = parseContent.dateFormat.format(parseContent.webFormat.parse(orderDetail.getOrder().getCreatedAt()));
                timeStr = parseContent.timeFormat_am.format(parseContent.webFormat.parse(orderDetail.getOrder().getCreatedAt()));
            }

            AidlUtil.getInstance().printCenterText("Requested for\n" + date + "\n" + timeStr + "\n", getResources().getInteger(R.integer.dimen_print_text_regular), true, false);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        AidlUtil.getInstance().printDivider(getResources().getInteger(R.integer.dimen_print_divider_size), true, false);

        AidlUtil.getInstance().printCenterText("\n", getResources().getInteger(R.integer.dimen_print_text_medium), true, false);


        Log.d(">", "-----------------------<");

        LinkedList<TableItem> datalist = new LinkedList<>();
        datalist = new LinkedList<>();
        TableItem ti = new TableItem();
        ti.setAlign(new int[]{0, 1, 2});
        ti.setText(new String[]{getString(R.string.text_qty), getString(R.string.text_item), getString(R.string.text_total)});
        ti.setWidth(new int[]{1, 1, 1});
        datalist.add(ti);
        AidlUtil.getInstance().printTable(datalist, getResources().getInteger(R.integer.dimen_print_text_regular), false);
        datalist = new LinkedList<>();
        AidlUtil.getInstance().printCenterText("\n", getResources().getInteger(R.integer.dimen_print_text_regular), false, false);
        for (Item item : itemArrayList) {

            Log.d("-", item.getQuantity() + "x " + item.getItemName() + " " + item.getTotalPrice());

//            String price = PreferenceHelper.getPreferenceHelper(this).getCurrency() + (item.getItemPrice() + item.getTotalSpecificationPrice());
//            String total = PreferenceHelper.getPreferenceHelper(this).getCurrency() + item.getTotalItemAndSpecificationPrice();

            ti = new TableItem();
            ti.setAlign(new int[]{0, 0, 2});
            double rowPrice = 0;
            if(item.getSpecifications().size() > 0){
                rowPrice = item.getTotalItemAndSpecificationPrice();
            }
            else{
                rowPrice = item.getTotalPrice();
            }

            ti.setText(new String[]{item.getQuantity() + "x", item.getItemName(), parseContent.decimalTwoDigitFormat.format(rowPrice) + ""});
            ti.setWidth(new int[]{1, 4, 2});
            datalist.add(ti);

            if(item.getSpecifications().size() > 0){
                for (ItemSpecification itemSpecification : item.getSpecifications()) {
                    ti = new TableItem();
                    ti.setAlign(new int[]{0, 0, 2});

                    ti.setText(new String[]{"", itemSpecification.getName(), parseContent.decimalTwoDigitFormat.format(itemSpecification.getPrice()) + ""});
                    ti.setWidth(new int[]{1, 4, 2});
                    datalist.add(ti);
                }
            }

        }
        AidlUtil.getInstance().printTable(datalist, getResources().getInteger(R.integer.dimen_print_text_regular), false);
//        AidlUtil.getInstance().printDivider(getResources().getInteger(R.integer.dimen_print_divider_size), true, false);
        AidlUtil.getInstance().printCenterText("\n", getResources().getInteger(R.integer.dimen_print_text_regular), false, false);


        datalist = new LinkedList<>();
        ti = new TableItem();
        ti.setAlign(new int[]{0, 2});
        ti.setText(new String[]{"Delivery charges", parseContent.decimalTwoDigitFormat.format(orderDetail.getOrderPaymentDetail().getTotalDeliveryPrice()) + ""});
        ti.setWidth(new int[]{2, 1});
        datalist.add(ti);
        AidlUtil.getInstance().printTable(datalist, getResources().getInteger(R.integer.dimen_print_text_regular), false);
        AidlUtil.getInstance().printCenterText("\n", getResources().getInteger(R.integer.dimen_print_text_regular), false, false);
//        AidlUtil.getInstance().printDivider(getResources().getInteger(R.integer.dimen_print_divider_size), true, false);
        AidlUtil.getInstance().printCenterText("\n", getResources().getInteger(R.integer.dimen_print_text_regular), false, false);
        AidlUtil.getInstance().printCenterText("Total = " + parseContent.decimalTwoDigitFormat.format(orderDetail.getOrderPaymentDetail().getTotal()), getResources().getInteger(R.integer.dimen_print_text_heading), true, false);
        AidlUtil.getInstance().printCenterText("\n\n", getResources().getInteger(R.integer.dimen_print_text_regular), false, false);

        // print comments

        if (orderDetail.getOrderPaymentDetail().isIsPaymentPaid()) {
            AidlUtil.getInstance().printCenterText("UNPAID - Cash" + "\n\n", getResources().getInteger(R.integer.dimen_print_text_heading), true, false);
        } else if (orderDetail.getOrderPaymentDetail().getCashPayment() > 0) {
            AidlUtil.getInstance().printCenterText("Unpaid - Cash" + "\n\n", getResources().getInteger(R.integer.dimen_print_text_heading), true, false);
        }

        if (orderDetail.getOrderPaymentDetail().getCardPayment() > 0) {
            Log.d("Pay by card", orderDetail.getOrderPaymentDetail().isIsPaymentModeCash() + "");
//            AidlUtil.getInstance().printText("Payment method: Card" + "\n", getResources().getInteger(R.integer.dimen_print_text_regular), false, false);
        } else if (orderDetail.getOrderPaymentDetail().getCashPayment() > 0) {
            Log.d("Pay by Cash", orderDetail.getOrderPaymentDetail().getCashPayment() + "");
//            AidlUtil.getInstance().printText("Payment method: Cash" + "\n", getResources().getInteger(R.integer.dimen_print_text_regular), false, false);

        }

        try {
            String date;
            String timeStr;
            if (orderDetail.getOrder().isIsScheduleOrder()){
                date = parseContent.dateFormat.format(parseContent.webFormat.parse(orderDetail.getOrder().getScheduleOrderStartAt()));
                timeStr = parseContent.timeFormat_am.format(parseContent.webFormat.parse(orderDetail.getOrder().getScheduleOrderStartAt()));
            }
            else {
                date = parseContent.dateFormat.format(parseContent.webFormat.parse(orderDetail.getOrder().getCreatedAt()));
                timeStr = parseContent.timeFormat_am.format(parseContent.webFormat.parse(orderDetail.getOrder().getCreatedAt()));
            }


            AidlUtil.getInstance().printCenterText("" + date + "\n" + timeStr + "\n", getResources().getInteger(R.integer.dimen_print_text_regular), true, false);
        } catch (ParseException e) {
            e.printStackTrace();
        }

//        AidlUtil.getInstance().printDivider(getResources().getInteger(R.integer.dimen_print_divider_size), true, false);
        AidlUtil.getInstance().printText("\n", getResources().getInteger(R.integer.dimen_print_text_regular), false, false);
        AidlUtil.getInstance().printText("Thank you for your custom!", getResources().getInteger(R.integer.dimen_print_text_regular), false, false);
        AidlUtil.getInstance().print3Line();
        AidlUtil.getInstance().printText("\n", getResources().getInteger(R.integer.dimen_print_text_regular), false, false);
        AidlUtil.getInstance().printText("\n", getResources().getInteger(R.integer.dimen_print_text_regular), false, false);
        AidlUtil.getInstance().printText("\n", getResources().getInteger(R.integer.dimen_print_text_regular), false, false);

        Log.d(">", "-----------------------<");
    }

    private void makePhoneCall(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone));
        startActivity(intent);
    }

    /**
     * this method call a webservice for reject or cancel order
     *
     * @param rejectOrCancelStatus
     * @param context
     * @param orderItemId
     */
    private void rejectOrCancelOrder(int rejectOrCancelStatus, final Context context, String
            orderItemId, String cancelReason) {
        Utilities.showProgressDialog(this);
        map = getCommonParam(orderItemId);
        map.put(Constant.ORDER_STATUS, ApiClient.makeTextRequestBody(String.valueOf
                (rejectOrCancelStatus)));
        map.put(Constant.CANCEL_REASON, ApiClient.makeTextRequestBody(cancelReason));
        Call<IsSuccessResponse> call = ApiClient.getClient().create(ApiInterface.class)
                .CancelOrRejectOrder
                        (map);
        call.enqueue(new Callback<IsSuccessResponse>() {
            @Override
            public void onResponse(Call<IsSuccessResponse> call, Response<IsSuccessResponse>
                    response) {
                Utilities.removeProgressDialog();
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        onBackPressed();
                    } else {
                        ParseContent.getParseContentInstance().showErrorMessage(context, response
                                .body().getErrorCode(), false);
                    }
                } else {
                    Utilities.showHttpErrorToast(response.code(), context);
                }
            }

            @Override
            public void onFailure(Call<IsSuccessResponse> call, Throwable t) {
                Utilities.printLog("rejectOrCancelOrder", t.getMessage());
            }
        });
    }


    private void openCancelOrderDialog(final int status) {

        if (cancelOrderDialog != null && cancelOrderDialog.isShowing()) {
            return;
        }

        RadioGroup radioGroup;
        final RadioButton rbReasonOne, rbReasonTwo, rbReasonOther;
        final CustomInputEditText etOtherReason;
        cancelOrderDialog = new Dialog(this);
        cancelOrderDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        cancelOrderDialog.setContentView(R.layout.dialog_cancel_order);
        rbReasonOne = (RadioButton) cancelOrderDialog.findViewById(R.id.rbReasonOne);
        rbReasonTwo = (RadioButton) cancelOrderDialog.findViewById(R.id.rbReasonTwo);
        rbReasonOther = (RadioButton) cancelOrderDialog.findViewById(R.id.rbReasonOthers);
        radioGroup = (RadioGroup) cancelOrderDialog.findViewById(R.id.radioGroup);
        etOtherReason = (CustomInputEditText) cancelOrderDialog.findViewById(R.id.etOthersReason);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rbReasonOne:
                        etOtherReason.setVisibility(View.GONE);
                        cancelReason = rbReasonOne.getText().toString();
                        break;
                    case R.id.rbReasonTwo:
                        etOtherReason.setVisibility(View.GONE);
                        cancelReason = rbReasonTwo.getText().toString();
                        break;
                    case R.id.rbReasonOthers:
                        etOtherReason.setVisibility(View.VISIBLE);
                        break;
                    default:
                        // do with default
                        break;
                }
            }
        });
        cancelOrderDialog.findViewById(R.id.btnPositive).setOnClickListener(new View.OnClickListener
                () {
            @Override
            public void onClick(View view) {
                if (rbReasonOther.isChecked()) {
                    cancelReason = etOtherReason.getText().toString();
                }
                if (!TextUtils.isEmpty(cancelReason)) {
                    rejectOrCancelOrder(status, OrderDetailActivity.this,
                            orderDetail.getOrder().getId(), cancelReason);
                    cancelOrderDialog.dismiss();
                } else {
                    Utilities.showToast(OrderDetailActivity.this, getResources().getString(R
                            .string.msg_plz_give_valid_reason));
                }
            }
        });
        cancelOrderDialog.findViewById(R.id.btnNegative).setOnClickListener(new View
                .OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelOrderDialog.dismiss();
                cancelReason = "";
            }
        });
        WindowManager.LayoutParams params = cancelOrderDialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        cancelOrderDialog.setCancelable(false);
        cancelOrderDialog.show();

    }

    private void setOrderDetails(OrderDetail orderDetail) {
//        userPhone = orderDetail.getCartDetail().getDestinationAddresses().get(0)
//                .getUserDetails()
//                .getCountryPhoneCode() + orderDetail.getCartDetail().getDestinationAddresses().get(0)
//                .getUserDetails()
//                .getPhone();
        userPhone = "+448081967679";
        orderStatus = orderDetail.getOrder().getOrderStatus();
        orderDetailList.clear();
        itemArrayList.clear();
        orderDetailList.addAll(orderDetail.getCartDetail().getOrderDetails());
        for (OrderDetails orderDetail1 : orderDetailList) {
            itemArrayList.addAll(orderDetail1.getItems());
        }

        orderItemSectionDetailAdapter.notifyDataSetChanged();
        //orderItemDetailAdapter.notifyDataSetChanged();

        tvTotalItemPrice.setText(PreferenceHelper.getPreferenceHelper(this).getCurrency()
                + parseContent.decimalTwoDigitFormat.format(orderDetail
                .getOrderPaymentDetail()
                .getTotal
                        ()));

        tvOrderNo.setText(String.valueOf(orderDetail.getOrderPaymentDetail().getOrderUniqueId()));
        if (orderDetail.getOrderPaymentDetail().isIsPaymentModeCash()) {
            ivPayment.setImageResource(R.drawable.ic_cash);
        } else {
            ivPayment.setImageResource(R.drawable.ic_credit_card);
        }
        tvDeliveryAddress.setText(orderDetail.getCartDetail().getDestinationAddresses().get(0)
                .getAddress());
        String[] strings = orderDetail.getCartDetail().getDestinationAddresses().get(0)
                .getUserDetails().getName().split(" ");
        if (strings != null && strings.length == 2) {
            tvClientName.setText(strings[0]);
        } else {
            tvClientName.setText(orderDetail.getCartDetail().getDestinationAddresses().get(0)
                    .getUserDetails().getName());
        }
        GlideApp.with(this).load(IMAGE_URL + orderDetail.getCartDetail().getDestinationAddresses().get(0)
                .getUserDetails().getImageUrl()).placeholder(R
                .drawable.placeholder).dontAnimate().fallback(R.drawable.placeholder)
                .into(ivClient);
        if (((orderStatus == Constant.WAITING_FOR_ACCEPT || orderStatus
                == Constant.STORE_ORDER_ACCEPTED) && orderDetail.getOrder().getOrderType() == Constant.Type
                .STORE) || (orderStatus == Constant.WAITING_FOR_ACCEPT &&
                orderDetail.getOrder().getOrderType() == Constant.Type.USER &&
                preferenceHelper.getIsStoreCanEditOrder())) {
            btnEditOrder.setVisibility(View.VISIBLE);
        } else {
            btnEditOrder.setVisibility(View.GONE);
        }
        if (orderDetail.getOrder().isIsScheduleOrder()) {
            tvOrderSchedule.setVisibility(View.VISIBLE);
            try {
                Date date = ParseContent.getParseContentInstance().webFormat.parse(orderDetail
                        .getOrder().getScheduleOrderStartAt());
                SimpleDateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT,
                        Locale.US);
                SimpleDateFormat timeFormat = new SimpleDateFormat(Constant.TIME_FORMAT_2,
                        Locale.US);
                if (!TextUtils.isEmpty(orderDetail.getOrder().getTimeZone())) {
                    dateFormat.setTimeZone(TimeZone.getTimeZone(orderDetail.getOrder().getTimeZone()));
                    timeFormat.setTimeZone(TimeZone.getTimeZone(orderDetail.getOrder().getTimeZone()));
                }
                String stringDate = getResources().getString(R.string
                        .text_order_schedule) + " " +
                        dateFormat.format(date) + " " +
                        timeFormat.format(date);

                if (!TextUtils.isEmpty(orderDetail.getOrder().getScheduleOrderStartAt2())) {
                    Date date2 = ParseContent.getParseContentInstance().webFormat.parse(orderDetail
                            .getOrder().getScheduleOrderStartAt2());
                    stringDate = stringDate + " - " + timeFormat.format(date2);
                }
                tvOrderSchedule.setText(stringDate);
            } catch (ParseException e) {
                Utilities.handleException(OrderListAdapter.class.getName(), e);
            }
        } else {
            tvOrderSchedule.setVisibility(View.GONE);
        }
        ivScooter.setVisibility(orderDetail.getOrderPaymentDetail().isUserPickUpOrder() ? View
                .GONE : View.VISIBLE);
        setBtnTextBasedOnStatus(orderStatus);
    }

    private void setBtnTextBasedOnStatus(int status) {

        switch (status) {
            case Constant.DEFAULT_STATUS:
                btnAction.setText(getString(R.string.text_accept));
                llBtn.setVisibility(View.VISIBLE);
                btnReject.setVisibility(View.VISIBLE);
                ivBtnCancel.setVisibility(View.GONE);
                break;

            case Constant.WAITING_FOR_ACCEPT:
                btnAction.setText(getString(R.string.text_accept));
                llBtn.setVisibility(View.VISIBLE);
                btnReject.setVisibility(View.VISIBLE);
                ivBtnCancel.setVisibility(View.GONE);
                break;

            case Constant.STORE_ORDER_ACCEPTED:
                btnAction.setText(getString(R.string.text_preparing_order));
                llBtn.setVisibility(View.VISIBLE);
                btnReject.setVisibility(View.GONE);
                ivBtnCancel.setVisibility(View.VISIBLE);
                break;

            case Constant.STORE_ORDER_PREPARING:
                btnAction.setText(getString(R.string.text_order_ready));
                llBtn.setVisibility(View.VISIBLE);
                btnReject.setVisibility(View.GONE);
                ivBtnCancel.setVisibility(View.VISIBLE);
                break;
            case Constant.STORE_ORDER_READY:
                findViewById(R.id.dive).setVisibility(View.GONE);
                llBtn.setVisibility(View.VISIBLE);
                btnReject.setVisibility(View.GONE);
                ivBtnCancel.setVisibility(View.GONE);
                btnAction.setText(getString(R.string.text_assign_provider));
                if (orderDetail.getOrderPaymentDetail().isUserPickUpOrder()) {
                    btnAction.setText(getString(R.string.text_complete_order));
                } else if (!TextUtils.isEmpty(orderDetail.getOrder().getRequestId())) {
                    onBackPressed();
                } else if (preferenceHelper.getIsStoreCanCompleteOrder() && !preferenceHelper
                        .getIsStoreCanAddProvider()) {
                    btnReject.setText(getString(R.string.text_complete_order));
                    btnAction.setVisibility(View.GONE);
                    btnReject.setVisibility(View.VISIBLE);
                } else if (preferenceHelper.getIsStoreCanCompleteOrder() && preferenceHelper
                        .getIsStoreCanAddProvider()) {
                    btnReject.setText(getString(R.string.text_complete_order));
                    btnAction.setVisibility(View.VISIBLE);
                    btnReject.setVisibility(View.VISIBLE);
                } else {
                    btnAction.setVisibility(View.VISIBLE);
                    btnReject.setVisibility(View.GONE);
                }

                break;
            case Constant.USER_CANCELED_ORDER:
                openOrderCanceledByUserDialog();
                break;
            default:
                break;
        }
    }


    private void handleActionBtnClick(int status) {

        switch (status) {
            case Constant.DEFAULT_STATUS:
                setOrderStatus(Constant.STORE_ORDER_ACCEPTED);
                break;

            case Constant.WAITING_FOR_ACCEPT:
                setOrderStatus(Constant.STORE_ORDER_ACCEPTED);
                break;

            case Constant.STORE_ORDER_ACCEPTED:
                if (!orderDetail.getOrderPaymentDetail().isUserPickUpOrder() && preferenceHelper
                        .getIsAskForEstimatedTimeForOrderReady() && TextUtils.isEmpty(orderDetail.getOrder().getRequestId())) {
                    openOrderDeliveryEstimatedDialog();
                } else {
                    setOrderStatus(Constant.STORE_ORDER_PREPARING);
                }
                break;
            case Constant.STORE_ORDER_PREPARING:
                setOrderStatus(Constant.STORE_ORDER_READY);
                break;

            case Constant.STORE_ORDER_READY:
                if (orderDetail.getOrderPaymentDetail().isUserPickUpOrder()) {
                    openCompleteDeliveryDialog();
                } else {
                    openVehicleSelectDialog(0);

                }
                break;

            default:
                break;
        }
    }

    /**
     * this method call webservice for set order status (accepted,rejected,prepare etc.)
     *
     * @param orderStatus
     */
    private void setOrderStatus(final int orderStatus) {
        Utilities.showProgressDialog(this);
        map = getCommonParam(orderDetail.getOrder().getId());
        map.put(Constant.ORDER_STATUS, ApiClient.makeTextRequestBody(String.valueOf(orderStatus)));
        Call<OrderStatusResponse> call = ApiClient.getClient().create(ApiInterface.class)
                .setOrderStatus(map);
        call.enqueue(new Callback<OrderStatusResponse>() {
            @Override
            public void onResponse(Call<OrderStatusResponse> call, Response<OrderStatusResponse>
                    response) {
                Utilities.removeProgressDialog();
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {

                        OrderDetailActivity.this.orderStatus = response.body().getOrder()
                                .getOrderStatus();
                        setBtnTextBasedOnStatus(OrderDetailActivity.this.orderStatus);
                    } else {
                        ParseContent.getParseContentInstance().showErrorMessage
                                (OrderDetailActivity.this, response.body().getErrorCode(), false);
                        if (662 == response.body().getErrorCode()) {
                            onBackPressed();
                        }

                    }
                } else {
                    Utilities.showHttpErrorToast(response.code(), OrderDetailActivity.this);
                }
            }

            @Override
            public void onFailure(Call<OrderStatusResponse> call, Throwable t) {
                Utilities.printLog(TAG, t.getMessage());
            }
        });
    }

    /**
     * this method update UI based on store setting
     */
    private void checkAskForEstimatedTimeForOrderReady(final int orderStatus) {
        if (!orderDetail.getOrderPaymentDetail().isUserPickUpOrder() && preferenceHelper
                .getIsAskForEstimatedTimeForOrderReady() && orderStatus ==
                Constant.STORE_ORDER_PREPARING && TextUtils.isEmpty(orderDetail.getOrder().getRequestId())) {
            OrderDetailActivity.this.orderStatus = Constant.STORE_ORDER_ACCEPTED;
            setBtnTextBasedOnStatus(OrderDetailActivity.this.orderStatus);
            openOrderDeliveryEstimatedDialog();
        }
    }

    /*  *//**
     * this method call a webservice for getOrderStatus (accepted,rejected,prepare etc.)
     *//*
    private void checkOrderStatus() {
        if (order != null) {
            Utilities.showProgressDialog(this);
            map = new HashMap<>();
            map = getCommonParam(order.getId());
            Call<OrderStatusResponse> call = ApiClient.getClient().create(ApiInterface.class)
                    .checkOrderStatus
                            (map);
            call.enqueue(new Callback<OrderStatusResponse>() {
                @Override
                public void onResponse(Call<OrderStatusResponse> call, Response<OrderStatusResponse>
                        response) {
                    Utilities.removeProgressDialog();
                    if (response.isSuccessful()) {
                        if (response.body().isSuccess()) {
                            isOrderCompletedCodeRequired = response.body().getOrder()
                                    .isConfirmationCodeRequiredAtCompleteDelivery();
                            orderStatus = response.body().getOrder().getOrderStatus();
                            setBtnTextBasedOnStatus(orderStatus);
                        } else {
                            ParseContent.getParseContentInstance().showErrorMessage
                                    (OrderDetailActivity.this, response.body().getErrorCode(),
                                            false);
                        }
                    } else {
                        Utilities.showHttpErrorToast(response.code(), OrderDetailActivity.this);
                    }
                }

                @Override
                public void onFailure(Call<OrderStatusResponse> call, Throwable t) {
                    Utilities.printLog(TAG, t.getMessage());
                }
            });
        }
    }
*/

    /**
     * this method call webservice for create a order pickUp request to delivery man
     */
    private void assignDeliveryMan(int estimatedTime, String vehicleId, String providerId) {
        Utilities.showProgressDialog(this);
        map = getCommonParam(orderDetail.getOrder().getId());
        map.put(Constant.ESTIMATED_TIME_FOR_READY_ORDER, ApiClient.makeTextRequestBody
                (estimatedTime));
        map.put(Constant.VEHICLE_ID, ApiClient.makeTextRequestBody
                (vehicleId));
        if (!TextUtils.isEmpty(providerId)) {
            map.put(Constant.PROVIDER_ID, ApiClient.makeTextRequestBody
                    (providerId));
        }
        Call<OrderStatusResponse> call = ApiClient.getClient().create(ApiInterface.class)
                .assignProvider(map);
        call.enqueue(new Callback<OrderStatusResponse>() {
            @Override
            public void onResponse(Call<OrderStatusResponse> call, Response<OrderStatusResponse>
                    response) {
                Utilities.removeProgressDialog();
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        onBackPressed();
                    } else {
                        Utilities.printLog("tag", new Gson().toJson(response.body().getErrorCode
                                ()));
                        ParseContent.getParseContentInstance().showErrorMessage
                                (OrderDetailActivity.this, response.body().getErrorCode(), false);
                    }
                    onBackPressed();
                } else {
                    Utilities.showHttpErrorToast(response.code(), OrderDetailActivity.this);
                }
            }

            @Override
            public void onFailure(Call<OrderStatusResponse> call, Throwable t) {
                Utilities.printLog(TAG, t.getMessage());
            }
        });
    }


    /**
     * this method call webservice for complete a active or running  order
     */
    private void completeOrder() {
        Utilities.showCustomProgressDialog(this, false);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.STORE_ID, preferenceHelper
                    .getStoreId());
            jsonObject.put(Constant.SERVER_TOKEN, preferenceHelper
                    .getServerToken());
            jsonObject.put(Constant.ORDER_ID, orderDetail.getOrder().getId());
        } catch (JSONException e) {
            Utilities.handleException(TAG, e);
        }

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<IsSuccessResponse> responseCall = apiInterface.completeOrder(ApiClient
                .makeJSONRequestBody(jsonObject));
        responseCall.enqueue(new Callback<IsSuccessResponse>() {
            @Override
            public void onResponse(Call<IsSuccessResponse> call,
                                   Response<IsSuccessResponse> response) {
                Utilities.hideCustomProgressDialog();
                if (parseContent.isSuccessful(response)) {
                    if (response.body().isSuccess()) {
                        parseContent.showMessage(
                                OrderDetailActivity.this, response.body().getMessage());
                        onBackPressed();
                    } else {

                        parseContent.showErrorMessage(OrderDetailActivity.this, response.body()
                                .getErrorCode(), false);
                    }
                }


            }

            @Override
            public void onFailure(Call<IsSuccessResponse> call, Throwable t) {
                Utilities.handleThrowable(TAG, t);
            }
        });

    }


    private void openCompleteDeliveryDialog() {
        if (orderDetail.isConfirmationCodeRequiredAtCompleteDelivery()) {

            if (customEditTextDialog != null && customEditTextDialog.isShowing()) {
                return;
            }

            customEditTextDialog = new CustomEditTextDialog
                    (this, getResources().getString(R.string.text_complete_delivery),
                            getResources().getString(R.string.msg_enter_delivery_code),
                            getResources()
                                    .getString(R.string.text_submit), getResources().getString(R
                            .string.text_cancel), 1) {
                @Override
                public void btnOnClick(int btnId, TextInputEditText etSMSOtp, TextInputEditText
                        etEmailOtp) {
                    switch (btnId) {
                        case R.id.btnPositive:
                            if (TextUtils.equals(etEmailOtp.getText().toString(), String.valueOf
                                    (orderDetail.getOrder().getConfirmationCodeForCompleteDelivery()))) {
                                completeOrder();
                                dismiss();
                            } else {
                                etEmailOtp.setError(getResources().getString(R.string
                                        .error_660));
                                etEmailOtp.requestFocus();
                            }
                            break;
                        case R.id.btnNegative:
                            dismiss();
                            break;
                        default:
                            // do with default
                            break;
                    }


                }

            };
            customEditTextDialog.show();
            customEditTextDialog.textInputLayoutEmailOtp.setHint(getResources().getString(R
                    .string.text_confirmation_code));
        } else {
            completeOrder();
        }

    }

    private void openOrderCanceledByUserDialog() {

        CustomAlterDialog customAlterDialog = new CustomAlterDialog(this, getResources()
                .getString(R.string.text_order_canceled_user), getResources()
                .getString(R.string.msg_order_canceled_by_user), false, getResources
                ().getString(R.string.text_ok), getResources
                ().getString(R.string.text_no)) {
            @Override
            public void btnOnClick(int btnId) {

                if (btnId == R.id.btnPositive) {
                    OrderDetailActivity.this.onBackPressed();
                }
                dismiss();

            }
        };
        customAlterDialog.setCancelable(false);
        customAlterDialog.show();

    }

    private void openOrderDeliveryEstimatedDialog() {

        CustomEditTextDialog editTextDialog = new CustomEditTextDialog(this,
                getResources()
                        .getString(R.string.text_order_estimated_time), getResources()
                .getString(R.string.msg_order_prepare_estimated_time), getResources()
                .getString(R.string.text_submit), getResources().getString(R.string
                .text_cancel), -1) {


            @Override
            public void btnOnClick(int btnId, TextInputEditText etSMSOtp,
                                   TextInputEditText etEmail) {
                if (btnId == R.id.btnPositive) {
                    if (TextUtils.isEmpty(etEmail.getText().toString())) {
                        Utilities.showToast(OrderDetailActivity.this, getResources().getString(R
                                .string.msg_plz_enter_data));
                    } else {
                        try {
                            openVehicleSelectDialog(Integer.valueOf(etEmailOtp.getText().toString()));
                            dismiss();
                        } catch (NumberFormatException e) {
                            etEmailOtp.setError(getResources().getString(R.string.msg_plz_enter_valid_time));
                        }


                    }

                } else {
                    dismiss();
                }
            }
        };
        editTextDialog.setCancelable(false);
        editTextDialog.show();
        editTextDialog.textInputLayoutEmailOtp.setHint(getResources().getString(R.string
                .text_hint_estimated_time));
        editTextDialog.etEmailOtp.setInputType(InputType.TYPE_CLASS_NUMBER);
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(3);
        editTextDialog.etEmailOtp.setFilters(FilterArray);

    }

    private void goToOrderUpdateActivity(Order order) {
        UpdateOrder.getInstance().setOrderId(order.getId());
        Intent intent = new Intent(this, UpdateOrderActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.enter, R.anim.exit);
        finish();
    }

    @Override
    public void onOrderReceive() {
        getOrderDetail();
    }


    private void openVehicleSelectDialog(final int estimatedTime) {
        List<VehicleDetail> vehicleDetails = orderDetail
                .getOrderPaymentDetail()
                .getDeliveryPriceUsedType() == Constant.VEHICLE_TYPE ? CurrentBooking
                .getInstance()
                .getVehicleDetails() : CurrentBooking.getInstance().getAdminVehicleDetails();
        if (vehicleDetails.isEmpty()) {
            Utilities.showToast(this, getResources().getString(R
                    .string.text_vehicle_not_found));
        } else {
            final VehicleDialog vehicleDialog = new VehicleDialog(this, vehicleDetails);
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
                        Utilities.showToast(OrderDetailActivity.this, getResources().getString(R
                                .string.msg_select_vehicle));
                    } else {
                        if (vehicleDialog.isManualAssign()) {
                            vehicleDialog.dismiss();
                            openNearestProviderDialog(estimatedTime, orderDetail.getOrder().getId(),
                                    vehicleDialog.getVehicleId());
                        } else {

                            vehicleDialog.dismiss();
                            assignDeliveryMan(estimatedTime, vehicleDialog.getVehicleId(), null);
                        }
                    }
                }
            });
            vehicleDialog.show();
        }
    }

    private void openNearestProviderDialog(final int estimatedTime, String orderId,
                                           final String vehicleId) {
        final NearestProviderDialog providerDialog = new NearestProviderDialog(this, orderId,
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
                    Utilities.showToast(OrderDetailActivity.this, getResources().getString(R
                            .string.msg_select_provider));
                } else {
                    assignDeliveryMan(estimatedTime, vehicleId,
                            providerDialog.getSelectedProvider().getId());
                }


            }
        });
        providerDialog.show();
    }

    private void gotToChatActivity(int chat_type, String title, String receiver_id) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(Constant.ORDER_ID, orderDetail.getOrder().getId());
        intent.putExtra(Constant.TYPE, String.valueOf(chat_type));
        intent.putExtra(Constant.TITLE, title);
        intent.putExtra(Constant.RECEIVER_ID, receiver_id);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }


    private void getOrderDetail() {
        if (getIntent() != null && getIntent().getParcelableExtra(Constant.ORDER_DETAIL) != null) {
            Order order = getIntent().getParcelableExtra(Constant.ORDER_DETAIL);
            Utilities.showCustomProgressDialog(this, false);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(Constant.STORE_ID, preferenceHelper
                        .getStoreId());
                jsonObject.put(Constant.SERVER_TOKEN, preferenceHelper
                        .getServerToken());
                jsonObject.put(Constant.ORDER_ID, order.getId());
            } catch (JSONException e) {
                Utilities.handleException(TAG, e);
            }

            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<OrderDetailResponse> responseCall = apiInterface.getOrderDetail(ApiClient
                    .makeJSONRequestBody(jsonObject));
            responseCall.enqueue(new Callback<OrderDetailResponse>() {
                @Override
                public void onResponse(Call<OrderDetailResponse> call,
                                       Response<OrderDetailResponse> response) {
                    Utilities.hideCustomProgressDialog();
                    if (parseContent.isSuccessful(response)) {
                        if (response.body().isSuccess()) {
                            orderDetail = response.body().getOrderDetail();
                            setOrderDetails(orderDetail);

                            try {
                                for (DateTime dateTime : orderDetail.getOrder().getDateTime()) {
                                    if (dateTime.getStatus() == 3) {
                                        String date = parseContent.dateTimeFormat_am.format(parseContent.webFormat.parse(dateTime.getDate()));
                                        System.out.println("Order Accepted At: " + date + "\n");
                                        break;
                                    }
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
//                            tvtoolbarbtn.setVisibility(View.VISIBLE);
                        } else {
                            parseContent.showErrorMessage(OrderDetailActivity.this, response.body()
                                    .getErrorCode(), false);
                        }
                    }


                }

                @Override
                public void onFailure(Call<OrderDetailResponse> call, Throwable t) {
                    Utilities.handleThrowable(TAG, t);
                }
            });


        }
    }
}

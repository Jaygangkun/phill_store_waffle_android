package com.edelivery.store;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.edelivery.store.Section.OrderDetailsSection;
import com.edelivery.store.component.CustomAlterDialog;
import com.edelivery.store.component.NearestProviderDialog;
import com.edelivery.store.component.VehicleDialog;
import com.edelivery.store.models.datamodel.Order;
import com.edelivery.store.models.datamodel.OrderDetail;
import com.edelivery.store.models.datamodel.OrderDetails;
import com.edelivery.store.models.datamodel.ProviderDetail;
import com.edelivery.store.models.datamodel.UserDetail;
import com.edelivery.store.models.datamodel.VehicleDetail;
import com.edelivery.store.models.responsemodel.IsSuccessResponse;
import com.edelivery.store.models.responsemodel.OrderDetailResponse;
import com.edelivery.store.models.responsemodel.OrderStatusResponse;
import com.edelivery.store.models.singleton.CurrentBooking;
import com.edelivery.store.parse.ApiClient;
import com.edelivery.store.parse.ApiInterface;
import com.edelivery.store.parse.ParseContent;
import com.edelivery.store.utils.Constant;
import com.edelivery.store.utils.GlideApp;
import com.edelivery.store.utils.LatLngInterpolator;
import com.edelivery.store.utils.LocationHelper;
import com.edelivery.store.utils.PinnedHeaderItemDecoration;
import com.edelivery.store.utils.PreferenceHelper;
import com.edelivery.store.utils.Utilities;
import com.edelivery.store.widgets.CustomEventMapView;
import com.edelivery.store.widgets.CustomFontTextViewTitle;
import com.edelivery.store.widgets.CustomInputEditText;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.elluminati.edelivery.store.BuildConfig.IMAGE_URL;

public class DeliveryDetailActivity extends BaseActivity implements OnMapReadyCallback,
        LocationHelper
                .OnLocationReceived, View.OnClickListener, BaseActivity.OrderListener {

    private TextView tvClientName, tvTotalItemPrice;
    private ImageView ivClient, ivPayment;
    private OrderDetail orderDetail;
    private ProviderDetail providerDetail;
    private TextView tvStatus;
    private RelativeLayout llDriverDetail;
    private CustomTextView btnReassign, tvRate, btnGetCode, tvPickupCode, btnCancelRequest;
    private CustomFontTextViewTitle tvProviderName;
    private HashMap<String, RequestBody> map;
    private String TAG = "DeliveryDetailActivity";
    private CustomEventMapView mapView;
    private GoogleMap googleMap;
    private LocationHelper locationHelper;
    private Marker currentMarker, providerMarker, deliveryMarker;
    private ArrayList<LatLng> markerList;
    private ImageView imgTargetLocation, ivProviderImage, ivCall, ivCallProvider, ivOrderDetail;
    private CustomTextView tvOrderNo;
    private ScheduledExecutorService updateLocationAndOrderSchedule;
    private boolean isScheduledStart, isPaymentModeCash, isOrderPaymentStatusSetByStore;
    private Handler handler;
    private float cameraBearing = 0;
    private CustomAlterDialog cancelRequest;
    private VehicleDetail vehicleDetail;
    private CustomTextView tvDeliveryAddress;
    private CustomFontTextViewTitle tvOrderSchedule;
    private String cancelReason;
    private Dialog cancelOrderDialog;
    private CustomTextView tvtoolbarbtn;
    private Dialog cartDetailDialog;
    private ArrayList<OrderDetails> orderDetailsList = new ArrayList<>();
    private boolean isCameraIdeal = true;
    private int deliveryPriceUsedType;
    private ImageView ivToolbarRightIcon3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_details);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar(toolbar, R.drawable.ic_back, R.color.color_app_theme);
        ((TextView) findViewById(R.id.tvToolbarTitle)).setText(getString(R.string.text_order));
        tvtoolbarbtn = (CustomTextView) findViewById(R.id.tvtoolbarbtn);
        tvtoolbarbtn.setText(getString(R.string.text_cancel_order));
        tvtoolbarbtn.setOnClickListener(this);

        ivToolbarRightIcon3 = (ImageView) findViewById(R.id.ivToolbarRightIcon3);
        ivToolbarRightIcon3.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable
                .ic_chat));
        ivToolbarRightIcon3.setOnClickListener(this);
        ivToolbarRightIcon3.setVisibility(View.GONE);

        mapView = (CustomEventMapView) findViewById(R.id.mapView);

        mapView.getMapAsync(this);
        mapView.onCreate(savedInstanceState);

        locationHelper = new LocationHelper(this);
        locationHelper.setLocationReceivedLister(this);
        tvClientName = (TextView) findViewById(R.id.tvClientName);
        imgTargetLocation = (ImageView) findViewById(R.id.imgTargetLocation);
        tvTotalItemPrice = (TextView) findViewById(R.id.tvTotalItemPrice);
        ivPayment = (ImageView) findViewById(R.id.ivPayment);
        tvOrderNo = (CustomTextView) findViewById(R.id.tvOrderNo);
        ivClient = (ImageView) findViewById(R.id.ivClient);
        imgTargetLocation.setOnClickListener(this);
        tvStatus = (TextView) findViewById(R.id.tvStatus);
        btnReassign = (CustomTextView) findViewById(R.id.btnReassign);
        btnReassign.setOnClickListener(this);
        markerList = new ArrayList<>();
        ivProviderImage = (ImageView) findViewById(R.id.ivProviderImage);
        ivCallProvider = (ImageView) findViewById(R.id.ivCallProvider);
        llDriverDetail = (RelativeLayout) findViewById(R.id.llDriverDetail);
        tvProviderName = (CustomFontTextViewTitle) findViewById(R.id.tvProviderName);
        tvDeliveryAddress = (CustomTextView) findViewById(R.id.tvDeliveryAddress);
        tvRate = (CustomTextView) findViewById(R.id.tvRate);
        btnGetCode = (CustomTextView) findViewById(R.id.btnGetCode);
        tvPickupCode = (CustomTextView) findViewById(R.id.tvPickupCode);
        ivCallProvider.setOnClickListener(this);
        btnGetCode.setOnClickListener(this);
        ivCall = (ImageView) findViewById(R.id.ivCall);
        ivCall.setOnClickListener(this);
        btnCancelRequest = (CustomTextView) findViewById(R.id.btnCancelRequest);
        btnCancelRequest.setOnClickListener(this);
        tvOrderSchedule = (CustomFontTextViewTitle) findViewById(R.id.tvOrderSchedule);
        ivOrderDetail = findViewById(R.id.ivOrderDetail);
        ivOrderDetail.setOnClickListener(this);
        ivOrderDetail.setVisibility(View.VISIBLE);
        initHandler();
        getDeliveryDetail();
        checkPermission();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        setToolbarEditIcon(false, R.drawable.ic_icon_info);
        setToolbarCameraIcon(false);
        return true;
    }


    /**
     * this method set order data in view
     */
    private void setOrderDetailsList(OrderDetail orderDetail) {

        UserDetail userDetail = orderDetail.getRequestDetail().getUserDetail();

        String[] strings =userDetail.getName().split(" ");
        if (strings != null && strings.length == 2) {
            tvClientName.setText(strings[0]);
        } else {
            tvClientName.setText(userDetail.getName());
        }

        tvDeliveryAddress.setText(orderDetail.getRequestDetail().getDestinationAddresses().get(0)
                .getAddress());
        isPaymentModeCash = orderDetail.getOrderPaymentDetail().isIsPaymentModeCash();
        isOrderPaymentStatusSetByStore = orderDetail.getOrderPaymentDetail()
                .isOrderPaymentStatusSetByStore();

        tvTotalItemPrice.setText(PreferenceHelper.getPreferenceHelper(this).getCurrency()
                .concat(parseContent.decimalTwoDigitFormat.format(orderDetail
                        .getOrderPaymentDetail()
                        .getTotal())));
        tvOrderNo.setText(String.valueOf(orderDetail.getOrderPaymentDetail().getOrderUniqueId()));
        orderDetailsList.clear();
        orderDetailsList.addAll(orderDetail.getCartDetail().getOrderDetails());

        if (orderDetail.getOrderPaymentDetail().isIsPaymentModeCash()) {
            ivPayment.setImageResource(R.drawable.ic_cash);
        } else {
            ivPayment.setImageResource(R.drawable.ic_credit_card);
        }

        GlideApp.with(this).load(IMAGE_URL + orderDetail.getOrder().getUserDetail().getImageUrl()).placeholder(R
                .drawable.placeholder).dontAnimate().fallback(R.drawable.placeholder)
                .into(ivClient);

        if (orderDetail.getProviderDetail() == null) {
            llDriverDetail.setVisibility(View.GONE);
        } else {
            providerDetail = orderDetail.getProviderDetail();
            setProviderDetail(orderDetail.getRequestDetail().getDeliveryStatus());
            updateUIForPickupCode(orderDetail.getRequestDetail().getDeliveryStatus());
        }
        if (orderDetail.getOrder().getOrderDetail() != null && orderDetail.getOrder().getOrderDetail().isScheduleOrder()) {
            tvOrderSchedule.setVisibility(View.VISIBLE);
            try {
                Date date =
                        ParseContent.getParseContentInstance().webFormat.parse(orderDetail.getOrder().getOrderDetail()
                                .getScheduleOrderStartAt());
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

                if (!TextUtils.isEmpty(orderDetail.getOrder().getOrderDetail().getScheduleOrderStartAt2())) {
                    Date date2 =
                            ParseContent.getParseContentInstance().webFormat.parse(orderDetail.getOrder().getOrderDetail()
                                    .getScheduleOrderStartAt2());
                    stringDate = stringDate + " - " + timeFormat.format(date2);
                }
                tvOrderSchedule.setText(stringDate);
            } catch (ParseException e) {
                Utilities.handleException(DeliveryDetailActivity.class.getName(), e);
            }
        } else {
            tvOrderSchedule.setVisibility(View.GONE);
        }
        setViewBasedOnOrderStatus(orderDetail.getRequestDetail().getDeliveryStatus());
        deliveryPriceUsedType = orderDetail.getOrderPaymentDetail().getDeliveryPriceUsedType();
    }


    private void setProviderDetail(int deliveryStatus) {
        if (providerDetail != null && deliveryStatus != Constant
                .STORE_CANCELLED_REQUEST && deliveryStatus != Constant
                .DELIVERY_MAN_NOT_FOUND && deliveryStatus != Constant.DELIVERY_MAN_CANCELLED) {
            llDriverDetail.setVisibility(View.VISIBLE);
            tvProviderName.setText(providerDetail.getFirstName().concat(" ").concat
                    (providerDetail.getLastName()));
            Glide.with(this).load(IMAGE_URL + providerDetail.getImageUrl
                    ()).into
                    (ivProviderImage);
            tvRate.setText(String.valueOf(providerDetail.getRate()));

        } else {
            llDriverDetail.setVisibility(View.GONE);
            btnGetCode.setVisibility(View.GONE);
        }
    }


    private void setViewBasedOnOrderStatus(int status) {

        switch (status) {
            case Constant.STORE_ORDER_READY:
                tvStatus.setText(getString(R.string.text_assign_provider));
                updateUIForRequestButton();
                tvtoolbarbtn.setVisibility(View.VISIBLE);
                break;
            case Constant.WAITING_FOR_DELIVERY_MAN:
                tvStatus.setText(getString(R.string.text_status9));
                btnReassign.setVisibility(View.GONE);
                btnCancelRequest.setVisibility(View.VISIBLE);
                tvtoolbarbtn.setVisibility(View.VISIBLE);
                ivToolbarRightIcon3.setVisibility(View.GONE);
                break;
            case Constant.DELIVERY_MAN_ACCEPTED:
                tvStatus.setText(getString(R.string.text_status11));
                btnReassign.setVisibility(View.GONE);
                btnCancelRequest.setVisibility(View.VISIBLE);
                tvtoolbarbtn.setVisibility(View.VISIBLE);
                ivToolbarRightIcon3.setVisibility(View.GONE);
                break;
            case Constant.DELIVERY_MAN_COMING:
                tvStatus.setText(getString(R.string.text_status13));
                btnReassign.setVisibility(View.GONE);
                btnCancelRequest.setVisibility(View.VISIBLE);
                tvtoolbarbtn.setVisibility(View.VISIBLE);
                ivToolbarRightIcon3.setVisibility(View.GONE);
                break;
            case Constant.DELIVERY_MAN_ARRIVED:
                tvStatus.setText(getString(R.string.text_status15));
                btnCancelRequest.setVisibility(View.GONE);
                tvtoolbarbtn.setVisibility(View.GONE);
                ivToolbarRightIcon3.setVisibility(View.VISIBLE);
                break;
            case Constant.DELIVERY_MAN_PICKED_ORDER:
                tvStatus.setText(getString(R.string.text_status17));
                btnCancelRequest.setVisibility(View.GONE);
                tvtoolbarbtn.setVisibility(View.GONE);
                ivToolbarRightIcon3.setVisibility(View.VISIBLE);
                break;
            case Constant.DELIVERY_MAN_STARTED_DELIVERY:
                tvStatus.setText(getString(R.string.text_status19));
                btnCancelRequest.setVisibility(View.GONE);
                tvtoolbarbtn.setVisibility(View.GONE);
                ivToolbarRightIcon3.setVisibility(View.VISIBLE);

                break;
            case Constant.DELIVERY_MAN_ARRIVED_AT_DESTINATION:
                btnCancelRequest.setVisibility(View.GONE);
                tvStatus.setText(getString(R.string.text_status21));
                ivToolbarRightIcon3.setVisibility(View.VISIBLE);
                break;
            case Constant.FINAL_ORDER_COMPLETED:
                btnCancelRequest.setVisibility(View.GONE);
                tvStatus.setText(getString(R.string.text_status23));
                tvtoolbarbtn.setVisibility(View.GONE);
                ivToolbarRightIcon3.setVisibility(View.VISIBLE);
                goToHomeActivity();
                break;
            case Constant.DELIVERY_MAN_NOT_FOUND:
                tvtoolbarbtn.setVisibility(View.VISIBLE);
                stopSchedule();
                tvStatus.setText(getString(R.string.text_status109));
                updateUIForRequestButton();
                break;
            case Constant.DELIVERY_MAN_CANCELLED:
                tvtoolbarbtn.setVisibility(View.VISIBLE);
                stopSchedule();
                tvStatus.setText(getString(R.string.text_status112));
                updateUIForRequestButton();
                break;
            case Constant.STORE_CANCELLED_REQUEST:
                tvtoolbarbtn.setVisibility(View.VISIBLE);
                stopSchedule();
                tvStatus.setText(getString(R.string.text_status105));
                updateUIForRequestButton();
                break;
            case Constant.USER_CANCELED_ORDER:
                tvtoolbarbtn.setVisibility(View.VISIBLE);
                stopSchedule();
                tvStatus.setText(getString(R.string.text_status101));
                updateUIForRequestButton();
                onBackPressed();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();

    }

    @Override
    protected void onStart() {
        super.onStart();
        checkPermission();
        setOrderListener(this);
        if (orderDetail != null && !TextUtils.isEmpty(orderDetail.getRequestDetail().getId())) {
            startSchedule();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopSchedule();
        locationHelper.onStop();
        setOrderListener(null);
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.imgTargetLocation:
                if (!markerList.isEmpty()) {
                    setLocationBounds(false, markerList);
                }
                break;
            case R.id.ivCallProvider:
                makePhoneCallToProvider();
                break;
            case R.id.btnReassign:
                openVehicleSelectDialog();
                break;
            case R.id.btnCancelRequest:
                openCancelRequestDialog();
                break;
            case R.id.btnGetCode:
                openAdvancePayForCashOnDeliveryDialog();
                break;
            case R.id.ivCall:
//                makePhoneCall(orderDetail.getRequestDetail().getUserDetail().getPhone());
                makePhoneCall("+448081967679");
                break;
            case R.id.tvtoolbarbtn:
                openCancelOrderDialog(Constant.STORE_ORDER_CANCELLED);
                break;
            case R.id.ivOrderDetail:
                openOrderDetailDialog();
                break;
            case R.id.ivToolbarRightIcon3:
                openMenu(v);
                break;
            default:
                /*Default case*/
                break;
        }
    }

    private void openMenu(View v) {
        PopupMenu menu = new PopupMenu(getApplicationContext(), v);
        menu.getMenu().add(0, 1, 1, R.string.text_chat_with_admin);
        menu.getMenu().add(0, 2, 2, R.string.text_chat_with_deliveryman);
        menu.getMenu().add(0, 3, 3, R.string.text_chat_with_user);

        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case 1:
                        gotToChatActivity(Constant.Chat_Type.ADMIN_AND_STORE,
                                getResources().getString(R.string.text_admin)
                                , Constant.ADMIN_RECIVER_ID);
                        break;
                    case 2:
                        String provider_name =
                                providerDetail.getFirstName() + providerDetail.getLastName();
                        gotToChatActivity(Constant.Chat_Type.PROVIDER_AND_STORE, provider_name,
                                orderDetail.getRequestDetail().getProviderId());
                        break;
                    case 3:
                        String name = tvClientName.getText().toString();
                        gotToChatActivity(Constant.Chat_Type.USER_AND_STORE, name,
                                orderDetail.getRequestDetail().getUserId());
                        break;
                }
                return true;
            }
        });
        menu.show();
    }

    private void makePhoneCall(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone));
        startActivity(intent);
    }

    /**
     * this method call a webservice for getOrderStatus (accepted,rejected,prepare etc.)
     */
    private void checkRequestStatus() {
        Utilities.printLog(TAG, "---checkRequestStatus called");
        map = new HashMap<>();
        map = getCommonParam(orderDetail.getOrder().getId());
        map.put(Constant.REQUEST_ID,
                ApiClient.makeTextRequestBody(orderDetail.getRequestDetail().getId()));
        Call<OrderStatusResponse> call = ApiClient.getClient().create(ApiInterface.class)
                .checkRequestStatus
                        (map);
        call.enqueue(new Callback<OrderStatusResponse>() {
            @Override
            public void onResponse(Call<OrderStatusResponse> call, Response<OrderStatusResponse>
                    response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        Order order = response.body().getOrderRequest();
                        providerDetail = response.body().getProviderDetail();
                        vehicleDetail = response.body().getVehicleDetail();
                        Utilities.printLog("ORDER_ITEM", ApiClient.JSONResponse(response.body()));
                        setViewBasedOnOrderStatus(order.getDeliveryStatus());

                        List<Double> providerArray = providerDetail.getProviderLocation();
                        List<Double> srcArray = order.getPickupAddresses().get(0)
                                .getLocation();
                        List<Double> destArray = order.getDestinationAddresses().get
                                (0).getLocation();
                        LatLng providerLatLng = null, destLatLng, srcLatLng;
                        srcLatLng = new LatLng(srcArray.get(0),
                                srcArray.get(1));
                        destLatLng = new LatLng(destArray.get(0),
                                destArray.get(1));
                        if (providerArray != null && !providerArray.isEmpty()) {
                            providerLatLng = new LatLng(providerArray.get(0),
                                    providerArray.get(1));
                            setMarkerOnLocation(srcLatLng, providerLatLng, destLatLng,
                                    providerDetail
                                            .getBearing() - cameraBearing, order
                                            .getDeliveryStatus());
                            updateCamera(providerDetail.getBearing(),
                                    providerLatLng);
                        }

                        updateUIForPickupCode(order.getDeliveryStatus());
                        Utilities.printLog("PROVIDER_LOCATION", providerDetail.getProviderLocation()
                                + "");
                        setProviderDetail(order.getDeliveryStatus());
                    } else {
                        ParseContent.getParseContentInstance().showErrorMessage
                                (DeliveryDetailActivity.this, response.body().getErrorCode(),
                                        false);
                    }
                } else {
                    Utilities.showHttpErrorToast(response.code(), DeliveryDetailActivity.this);
                }
            }

            @Override
            public void onFailure(Call<OrderStatusResponse> call, Throwable t) {
                Utilities.printLog(TAG, t.getMessage());
            }
        });
    }


    /**
     * this method call webservice for create a order pickUp request to delivery man
     */
    private void assignDeliveryMan(String vehicleId, String providerId) {
        Utilities.showProgressDialog(this);
        map = getCommonParam(orderDetail.getOrder().getId());
        map.put(Constant.VEHICLE_ID, ApiClient.makeTextRequestBody(vehicleId));
        if (!TextUtils.isEmpty(providerId)) {
            map.put(Constant.PROVIDER_ID, ApiClient.makeTextRequestBody(providerId));
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
                        Order order = response.body().getOrderRequest();
                        providerDetail = response.body().getProviderDetail();
                        setViewBasedOnOrderStatus(order.getDeliveryStatus());
                        btnReassign.setVisibility(View.INVISIBLE);
                        setProviderDetail(order.getDeliveryStatus());
                        updateUIForPickupCode(order.getDeliveryStatus());
                        startSchedule();
                    } else {
                        Utilities.printLog("tag", new Gson().toJson(response.body().getErrorCode
                                ()));
                        ParseContent.getParseContentInstance().showErrorMessage
                                (DeliveryDetailActivity.this, response.body().getErrorCode(),
                                        false);
                    }
                } else {
                    Utilities.showHttpErrorToast(response.code(), DeliveryDetailActivity.this);
                }
            }

            @Override
            public void onFailure(Call<OrderStatusResponse> call, Throwable t) {
                Utilities.printLog(TAG, t.getMessage());
            }
        });
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission
                    .ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, Constant
                    .PERMISSION_FOR_LOCATION);
        } else {
            //Do the stuff that requires permission...

            locationHelper.onStart();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constant.PERMISSION_FOR_LOCATION:
                checkPermission();
                break;
            default:
                //do with default
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0) {
            switch (requestCode) {
                case Constant.PERMISSION_FOR_LOCATION:
                    goWithLocationPermission(grantResults);
                    break;
                default:
                    //do with default
                    break;
            }
        }

    }


    private void openLocationPermissionDialog() {

        CustomAlterDialog customAlterDialog = new CustomAlterDialog(DeliveryDetailActivity.this,
                getResources()
                        .getString(R
                                .string
                                .text_attention), getResources().getString(R
                .string
                .msg_reason_for_permission_location), true,
                getString(R.string.text_re_try), getString(R.string.text_i_am_sure)) {
            @Override
            public void btnOnClick(int btnId) {
                if (btnId == R.id.btnPositive) {
                    ActivityCompat.requestPermissions(DeliveryDetailActivity.this, new
                                    String[]{Manifest.permission
                                    .ACCESS_FINE_LOCATION, Manifest.permission
                                    .ACCESS_COARSE_LOCATION},
                            Constant.PERMISSION_FOR_LOCATION);
                }
                dismiss();
            }


        };
        customAlterDialog.show();
    }

    /**
     * this method will make decision according to permission result
     *
     * @param grantResults set result from system or OS
     */
    private void goWithLocationPermission(int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //Do the stuff that requires permission...
            locationHelper.onStart();

        } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest
                    .permission.ACCESS_COARSE_LOCATION) && ActivityCompat
                    .shouldShowRequestPermissionRationale(this, Manifest
                            .permission.ACCESS_FINE_LOCATION)) {
                openLocationPermissionDialog();
            }
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        setUpMap();
    }

    private void setUpMap() {
        this.googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        this.googleMap.getUiSettings().setMapToolbarEnabled(false);
        this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        this.googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            boolean doNotMoveCameraToCenterMarker = true;

            public boolean onMarkerClick(Marker marker) {
                return doNotMoveCameraToCenterMarker;
            }
        });

    }


    @Override
    public void onConnected(Bundle bundle) {
        moveCameraFirstMyLocation(false);
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    /**
     * this method move a map camera at your current location on map
     *
     * @param isAnimate
     */
    public void moveCameraFirstMyLocation(final boolean isAnimate) {
        locationHelper.getLastLocation(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    LatLng latLngOfMyLocation = new LatLng(location
                            .getLatitude(),
                            location.getLongitude());
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(latLngOfMyLocation).zoom(17).build();

                    if (isAnimate) {
                        googleMap.animateCamera(CameraUpdateFactory
                                .newCameraPosition(cameraPosition));
                    } else {
                        googleMap.moveCamera(CameraUpdateFactory
                                .newCameraPosition(cameraPosition));
                    }
                    locationHelper.setOpenGpsDialog(false);
                }

            }
        });

    }

    private void openCancelRequestDialog() {
        if (cancelRequest != null && cancelRequest.isShowing()) {
            return;
        }

        cancelRequest = new CustomAlterDialog(this, getResources()
                .getString(R.string.text_cancel_request), getResources()
                .getString(R.string.text_are_you_sure), true, getResources
                ().getString(R.string.text_yes), getResources
                ().getString(R.string.text_no)) {
            @Override
            public void btnOnClick(int btnId) {

                if (btnId == R.id.btnPositive) {
                    cancelDeliveryRequest();
                }
                dismiss();

            }
        };
        cancelRequest.show();
    }

    private void openAdvancePayForCashOnDeliveryDialog() {

        String message = orderDetail.isConfirmationCodeRequiredAtPickupDelivery() ? getResources()
                .getString(R.string.msg_advance_pay_for_order) : getResources()
                .getString(R.string.msg_advance_pay_for_order_with_out_code);
        CustomAlterDialog customAlterDialog = new CustomAlterDialog(this, getResources()
                .getString(R.string.text_order_payment), message,
                true, getResources
                ().getString(R.string.text_yes), getResources
                ().getString(R.string.text_no)) {
            @Override
            public void btnOnClick(int btnId) {

                if (btnId == R.id.btnPositive) {
                    orderPaymentPaidBy(false);
                } else {
                    orderPaymentPaidBy(true);
                }
                dismiss();

            }
        };
        customAlterDialog.show();

    }

    private void orderPaymentPaidBy(boolean isPayStore) {
        Utilities.showCustomProgressDialog(this, false);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.SERVER_TOKEN, preferenceHelper.getServerToken());
            jsonObject.put(Constant.STORE_ID, preferenceHelper.getStoreId());
            jsonObject.put(Constant.IS_ORDER_PRICE_PAID_BY_STORE, isPayStore);
            jsonObject.put(Constant.ORDER_PAYMENT_ID, orderDetail.getOrder().getOrderPaymentId());

            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<IsSuccessResponse> responseCall = apiInterface.setOrderPaymentPaidBy
                    (ApiClient.makeJSONRequestBody(jsonObject));
            responseCall.enqueue(new Callback<IsSuccessResponse>() {
                @Override
                public void onResponse(Call<IsSuccessResponse> call, Response<IsSuccessResponse>
                        response) {
                    Utilities.hideCustomProgressDialog();
                    isOrderPaymentStatusSetByStore = response.body().isSuccess();
                    updateUIForPickupCode(orderDetail.getRequestDetail().getDeliveryStatus());

                }

                @Override
                public void onFailure(Call<IsSuccessResponse> call, Throwable t) {
                    Utilities.printLog(TAG, t.getMessage());
                }
            });

        } catch (JSONException e) {
            Utilities.printLog(TAG, e + "");
        }
    }


    /**
     * this method is used to set marker on map
     *
     * @param storeLatLng    LatLng
     * @param providerLatLng LatLng
     */
    private void setMarkerOnLocation(LatLng storeLatLng, LatLng providerLatLng, LatLng
            deliveryLatLng, float bearing, int deliveryStatus) {
        BitmapDescriptor bitmapDescriptor;
        boolean isBounce = false;

        if (storeLatLng != null) {
            if (currentMarker == null) {
                bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(Utilities.drawableToBitmap
                        (AppCompatResources.getDrawable(this, R.drawable.ic_pin_pickup)));
                currentMarker = googleMap.addMarker(new MarkerOptions().position(storeLatLng)
                        .title(getResources().getString(R.string.text_store))
                        .icon(bitmapDescriptor));
            } else {
                currentMarker.setPosition(storeLatLng);
            }
        }
        if (deliveryLatLng != null) {
            if (deliveryMarker == null) {
                bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(Utilities.drawableToBitmap
                        (AppCompatResources.getDrawable(this, R.drawable.ic_pin_delivery)));
                deliveryMarker = googleMap.addMarker(new MarkerOptions().position(deliveryLatLng)
                        .title(getResources().getString(R.string.text_delivery))
                        .icon(bitmapDescriptor));
                isBounce = true;
            } else {
                deliveryMarker.setPosition(deliveryLatLng);
            }
        }
        if (providerLatLng != null && deliveryStatus != Constant
                .DELIVERY_MAN_NOT_FOUND) {
            if (providerMarker == null) {
                providerMarker = googleMap.addMarker(new MarkerOptions().position(providerLatLng)
                        .title(getResources().getString(R.string.text_delivery_man)));
                downloadVehiclePin();
                providerMarker.setAnchor(0.5f, 0.5f);

            } else {

                animateMarkerToGB(providerMarker, providerLatLng, new LatLngInterpolator.Linear(),
                        bearing);
            }
            markerList.add(providerLatLng);
        }
        if (deliveryStatus == Constant.WAITING_FOR_DELIVERY_MAN) {
            if (storeLatLng != null) {
                markerList.add(storeLatLng);
            }
            if (deliveryLatLng != null) {
                markerList.add(deliveryLatLng);
            }
        } else if (deliveryStatus <= Constant.DELIVERY_MAN_COMING) {
            if (storeLatLng != null) {
                markerList.add(storeLatLng);
            }
        } else {
            if (deliveryLatLng != null) {
                markerList.add(deliveryLatLng);
            }

        }
        if (isBounce) {
            try {
                setLocationBounds(false, markerList);
            } catch (Exception e) {
                Utilities.handleException(DeliveryDetailActivity.class.getName(), e);

            }
        }
    }

    /**
     * this method used to animate marker on map smoothly
     *
     * @param marker             Marker
     * @param finalPosition      LatLag
     * @param latLngInterpolator LatLngInterpolator
     * @param bearing            float
     */
    private void animateMarkerToGB(final Marker marker, final LatLng finalPosition, final
    LatLngInterpolator latLngInterpolator, final float bearing) {

        if (marker != null) {

            final LatLng startPosition = marker.getPosition();
            final LatLng endPosition = new LatLng(finalPosition.latitude, finalPosition
                    .longitude);

            final float startRotation = marker.getRotation();
            final LatLngInterpolator interpolator = new LatLngInterpolator.LinearFixed();

            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(3000); // duration 3 second
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    try {
                        float v = animation.getAnimatedFraction();
                        LatLng newPosition = latLngInterpolator.interpolate(v, startPosition,
                                endPosition);
                        marker.setPosition(newPosition);
                        marker.setAnchor(0.5f, 0.5f);


                    } catch (Exception ex) {
                        //I don't care atm..
                    }
                }
            });
            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);


                }
            });
            valueAnimator.start();
        }
    }

    /**
     * this method update camera for map
     *
     * @param bearing        float
     * @param positionLatLng LatLng
     */
    private void updateCamera(float bearing, LatLng positionLatLng) {
        if (isCameraIdeal) {
            isCameraIdeal = false;
            CameraPosition oldPos = googleMap.getCameraPosition();

            cameraBearing = bearing;
            CameraPosition pos = CameraPosition.builder(oldPos).bearing(bearing).target
                    (positionLatLng).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(pos), 3000,
                    new GoogleMap.CancelableCallback() {

                        @Override
                        public void onFinish() {
                            isCameraIdeal = true;
                        }

                        @Override
                        public void onCancel() {
                            isCameraIdeal = true;
                        }
                    });
        }
    }

    private void setLocationBounds(boolean isCameraAnim, ArrayList<LatLng> markerList) {
        LatLngBounds.Builder bounds = new LatLngBounds.Builder();
        int driverListSize = markerList.size();
        for (int i = 0; i < driverListSize; i++) {
            bounds.include(markerList.get(i));
        }
        //Change the padding as per needed
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds.build(), getResources()
                .getDimensionPixelSize(R.dimen.dimen_map_bounce));

        if (isCameraAnim) {
            googleMap.animateCamera(cu);
        } else {
            googleMap.moveCamera(cu);
        }
    }

    /*public float getBearing(LatLng begin, LatLng end) {
        float bearing = 0;
        if (begin != null && end != null) {
            Location startLocation = new Location("start");
            startLocation.setLatitude(begin.latitude);
            startLocation.setLongitude(begin.longitude);

            Location endLocation = new Location("end");
            endLocation.setLatitude(end.latitude);
            endLocation.setLongitude(end.longitude);
            bearing = startLocation.bearingTo(endLocation);
            Utilities.printLog("BEARING", String.valueOf(bearing));

        }


        return bearing;
    }*/
    private float getBearing(LatLng begin, LatLng end) {
        double lat = Math.abs(begin.latitude - end.latitude);
        double lng = Math.abs(begin.longitude - end.longitude);

        if (begin.latitude < end.latitude && begin.longitude < end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)));
        else if (begin.latitude >= end.latitude && begin.longitude < end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 90);
        else if (begin.latitude >= end.latitude && begin.longitude >= end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)) + 180);
        else if (begin.latitude < end.latitude && begin.longitude >= end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 270);
        return -1;
    }

    public void startSchedule() {

        if (!isScheduledStart && preferenceHelper.isApproved()) {

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    Message message = handler.obtainMessage();
                    handler.sendMessage(message);
                }
            };
            updateLocationAndOrderSchedule = Executors.newSingleThreadScheduledExecutor();
            updateLocationAndOrderSchedule.scheduleWithFixedDelay(runnable, 0,
                    Constant.DELIVERY_SCHEDULED, TimeUnit
                            .SECONDS);
            Utilities.printLog(HomeActivity.class.getName(), "Schedule Start");
            isScheduledStart = true;
        }
    }

    public void stopSchedule() {
        if (isScheduledStart) {
            Utilities.printLog(HomeActivity.class.getName(), "Schedule Stop");
            updateLocationAndOrderSchedule.shutdown(); // Disable new tasks from being submitted
            // Wait a while for existing tasks to terminate
            try {
                if (!updateLocationAndOrderSchedule.awaitTermination(60, TimeUnit.SECONDS)) {
                    updateLocationAndOrderSchedule.shutdownNow(); // Cancel currently executing
                    // tasks
                    // Wait a while for tasks to respond to being cancelled
                    if (!updateLocationAndOrderSchedule.awaitTermination(60, TimeUnit.SECONDS))
                        Utilities.printLog(HomeActivity.class.getName(), "Pool did not " +
                                "terminate");

                }
            } catch (InterruptedException e) {
                Utilities.handleException(HomeActivity.class.getName(), e);
                // (Re-)Cancel if current thread also interrupted
                updateLocationAndOrderSchedule.shutdownNow();
                // Preserve interrupt status
                Thread.currentThread().interrupt();
            }
            isScheduledStart = false;
        }

    }

    private void initHandler() {
        /**
         * This handler receive a message from  requestStatusScheduledService and update provider
         * location and order status
         */
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                checkRequestStatus();

            }

        };

    }

    private void cancelDeliveryRequest() {
        Utilities.showCustomProgressDialog(this, false);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.SERVER_TOKEN, preferenceHelper.getServerToken());
            jsonObject.put(Constant.STORE_ID, preferenceHelper.getStoreId());
            jsonObject.put(Constant.REQUEST_ID, orderDetail.getRequestDetail().getId());
            jsonObject.put(Constant.PROVIDER_ID,
                    orderDetail.getRequestDetail().getCurrentProvider());
        } catch (JSONException e) {
            Utilities.handleException(DeliveryDetailActivity.class.getName(), e);
        }
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<OrderStatusResponse> responseCall = apiInterface.cancelDeliveryRequest(
                (ApiClient.makeJSONRequestBody(jsonObject)));
        responseCall.enqueue(new Callback<OrderStatusResponse>() {
            @Override
            public void onResponse(Call<OrderStatusResponse> call, Response<OrderStatusResponse>
                    response) {
                Utilities.hideCustomProgressDialog();
                if (response.body().isSuccess()) {
                    setViewBasedOnOrderStatus(response.body().getDeliveryStatus());
                } else {
                    stopSchedule();
                    parseContent.showErrorMessage(DeliveryDetailActivity.this, response.body()
                            .getErrorCode(), false);
                }
            }

            @Override
            public void onFailure(Call<OrderStatusResponse> call, Throwable t) {
                Utilities.printLog(DeliveryDetailActivity.class.getName(), t + "");
            }
        });

    }

    private void updateUIForPickupCode(int deliveryStatus) {
        if (isPaymentModeCash) {
            if (deliveryStatus
                    == Constant
                    .DELIVERY_MAN_ARRIVED || deliveryStatus
                    == Constant
                    .DELIVERY_MAN_PICKED_ORDER || deliveryStatus
                    == Constant
                    .DELIVERY_MAN_STARTED_DELIVERY || deliveryStatus
                    == Constant
                    .DELIVERY_MAN_ARRIVED_AT_DESTINATION || deliveryStatus
                    == Constant
                    .DELIVERY_MAN_COMPLETE_DELIVERY) {
                if (!isOrderPaymentStatusSetByStore) {
                    btnGetCode.setVisibility(View.VISIBLE);
                    if (orderDetail.isConfirmationCodeRequiredAtPickupDelivery()) {
                        btnGetCode.setText(getResources().getString(R.string.text_get_code));
                    } else {
                        btnGetCode.setText(getResources().getString(R.string.text_who_pay));

                    }
                    tvPickupCode.setVisibility(View.GONE);
                } else {
                    btnGetCode.setVisibility(View.GONE);
                    if (orderDetail.isConfirmationCodeRequiredAtPickupDelivery()) {
                        tvPickupCode.setVisibility(View.VISIBLE);
                        tvPickupCode.setText(getResources().getString(R.string.text_pik_up_code)
                                + " " +
                                "" + orderDetail.getOrder().getConfirmationCodeForPickUpDelivery());
                    } else {
                        tvPickupCode.setVisibility(View.GONE);
                    }
                }
            } else {
                btnGetCode.setVisibility(View.GONE);
                tvPickupCode.setVisibility(View.GONE);
            }

        } else {
            btnGetCode.setVisibility(View.GONE);
            if (orderDetail.isConfirmationCodeRequiredAtPickupDelivery()) {
                tvPickupCode.setVisibility(View.VISIBLE);
                tvPickupCode.setText(getResources().getString(R.string.text_pik_up_code)
                        + " " +
                        "" + orderDetail.getOrder().getConfirmationCodeForPickUpDelivery());
            } else {
                tvPickupCode.setVisibility(View.GONE);
            }
        }


    }

    private void updateUIForRequestButton() {
        btnReassign.setVisibility(View.VISIBLE);
        btnCancelRequest.setVisibility(View.GONE);
        llDriverDetail.setVisibility(View.GONE);
        if (providerMarker != null) {
            providerMarker.remove();
            providerMarker = null;
        }
    }

    public void makePhoneCallToProvider() {
        if (providerDetail != null) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + providerDetail
                    .getCountryPhoneCode()
                    + providerDetail.getPhone()));
            startActivity(intent);
        }


    }

    public void downloadVehiclePin() {
        if (!TextUtils.isEmpty(vehicleDetail.getMapPinImageUrl())) {
            GlideApp.with(this).asBitmap()
                    .load(IMAGE_URL + vehicleDetail.getMapPinImageUrl())

                    .diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable
                    .driver_car)
                    .listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                    Target<Bitmap> target,
                                                    boolean isFirstResource) {
                            Utilities.handleException(getClass().getSimpleName(), e);
                            Utilities.printLog("VEHICLE_PIN", "Download failed");
                            if (providerMarker != null) {
                                providerMarker.setIcon(BitmapDescriptorFactory.fromBitmap
                                        (Utilities.drawableToBitmap
                                                (AppCompatResources.getDrawable
                                                        (DeliveryDetailActivity
                                                                .this, R.drawable.driver_car))));
                            }
                            return true;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model,
                                                       Target<Bitmap> target,
                                                       DataSource dataSource,
                                                       boolean isFirstResource) {
                            if (providerMarker != null) {
                                providerMarker.setIcon(BitmapDescriptorFactory.fromBitmap
                                        (resource));
                            }
                            Utilities.printLog("VEHICLE_PIN", "Download Successfully");
                            return true;
                        }
                    }).dontAnimate().override(getResources().getDimensionPixelSize(R
                            .dimen.vehicle_pin_width)
                    , getResources().getDimensionPixelSize(R
                            .dimen.vehicle_pin_height)).preload(getResources()
                            .getDimensionPixelSize(R
                                    .dimen.vehicle_pin_width)
                    , getResources().getDimensionPixelSize(R
                            .dimen.vehicle_pin_height));
        } else {
            if (providerMarker != null) {
                providerMarker.setIcon(BitmapDescriptorFactory.fromBitmap
                        (Utilities.drawableToBitmap
                                (AppCompatResources.getDrawable
                                        (DeliveryDetailActivity
                                                .this, R.drawable.driver_car))));
            }
        }


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
                    rejectOrCancelOrder(status, DeliveryDetailActivity.this,
                            orderDetail.getOrder().getId(), cancelReason);
                    cancelOrderDialog.dismiss();
                } else {
                    Utilities.showToast(DeliveryDetailActivity.this, getResources().getString(R
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
                Utilities.printLog(TAG, t.getMessage());
            }
        });
    }

    private void openOrderDetailDialog() {
        if (cartDetailDialog != null && cartDetailDialog.isShowing()) {
            return;
        }

        cartDetailDialog = new Dialog(this);
        cartDetailDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        cartDetailDialog.setContentView(R.layout.dialog_delivery_order_detail);
        CustomTextView tvOrderNumber = cartDetailDialog.findViewById(R
                .id.tvOrderNumber);
        String orderNumber = getResources().getString(R.string.text_order_number)
                + " " + "#" + tvOrderNo.getText();
        tvOrderNumber.setText(orderNumber);
        RecyclerView rcvOrderProductItem = (RecyclerView) cartDetailDialog.findViewById(R.id
                .rcvOrderProductItem);
        rcvOrderProductItem.setLayoutManager(new LinearLayoutManager(this));
        OrderDetailsSection orderDetailsSection = new OrderDetailsSection(orderDetailsList,
                this,
                false);
        rcvOrderProductItem.setAdapter(orderDetailsSection);
        rcvOrderProductItem.addItemDecoration(new PinnedHeaderItemDecoration());
        cartDetailDialog.findViewById(R.id.btnDone).setOnClickListener(new View
                .OnClickListener() {

            @Override
            public void onClick(View v) {
                cartDetailDialog.dismiss();
            }
        });


        WindowManager.LayoutParams params = cartDetailDialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        cartDetailDialog.setCancelable(false);
        cartDetailDialog.show();

    }

    @Override
    public void onOrderReceive() {
        checkRequestStatus();
    }


    private void openVehicleSelectDialog() {
        List<VehicleDetail> vehicleDetails = deliveryPriceUsedType ==
                Constant.VEHICLE_TYPE ?
                CurrentBooking.getInstance()
                        .getVehicleDetails() : CurrentBooking.getInstance()
                .getAdminVehicleDetails();
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
                        Utilities.showToast(DeliveryDetailActivity.this, getResources()
                                .getString(R
                                        .string.msg_select_vehicle));
                    } else {
                        if (vehicleDialog.isManualAssign()) {
                            vehicleDialog.dismiss();
                            openNearestProviderDialog(orderDetail.getOrder().getId(),
                                    vehicleDialog.getVehicleId());
                        } else {
                            vehicleDialog.dismiss();
                            assignDeliveryMan(vehicleDialog.getVehicleId(), null);
                        }


                    }


                }
            });
            vehicleDialog.show();
        }
    }

    private void openNearestProviderDialog(String orderId,
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
                    Utilities.showToast(DeliveryDetailActivity.this, getResources().getString(R
                            .string.msg_select_provider));
                } else {
                    assignDeliveryMan(vehicleId,
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


    private void getDeliveryDetail() {
        if (getIntent() != null && getIntent().getParcelableExtra(Constant.ORDER_DETAIL) != null) {
            Order order = getIntent().getParcelableExtra(Constant.ORDER_DETAIL);
            Utilities.showCustomProgressDialog(this, false);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(Constant.STORE_ID, preferenceHelper
                        .getStoreId());
                jsonObject.put(Constant.SERVER_TOKEN, preferenceHelper
                        .getServerToken());
                jsonObject.put(Constant.ORDER_ID, order.getOrderId());
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
                            setOrderDetailsList(orderDetail);
                            if (!TextUtils.isEmpty(orderDetail.getRequestDetail().getId())) {
                                startSchedule();
                            }

                        } else {
                            parseContent.showErrorMessage(DeliveryDetailActivity.this,
                                    response.body()
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

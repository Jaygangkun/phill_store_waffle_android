package com.edelivery.store;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.edelivery.store.adapter.PlaceAutocompleteAdapter;
import com.edelivery.store.component.CustomFontAutoCompleteView;
import com.edelivery.store.models.datamodel.AutocompleteAddress1;
import com.edelivery.store.models.responsemodel.AutoCompleteResponse;
import com.edelivery.store.parse.ApiClient;
import com.edelivery.store.parse.ApiInterface;
import com.edelivery.store.utils.Constant;
import com.edelivery.store.utils.LocationHelper;
import com.edelivery.store.utils.Utilities;
import com.edelivery.store.widgets.CustomButton;
import com.edelivery.store.widgets.CustomEventMapView;
import com.elluminati.edelivery.store.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.model.RectangularBounds;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.edelivery.store.utils.Constant.REQUEST_CHECK_SETTINGS;

public class StoreLocationActivity extends BaseActivity implements
        LocationHelper.OnLocationReceived, OnMapReadyCallback {

    private LocationHelper locationHelper;
    private CustomFontAutoCompleteView acDeliveryAddress;
    private GoogleMap googleMap;
    private LatLng deliveryLatLng;
    private CustomEventMapView mapView;
    private LatLng storeLatLng;
    private boolean isMapTouched = true;
    private PlaceAutocompleteAdapter autocompleteAdapter;
    private ProgressBar progressBar;
    long last_text_edit = 0;
    Handler handler = new Handler();
    boolean isFirst = true;

    private Runnable input_finish_checker = new Runnable() {
        public void run() {
            if (System.currentTimeMillis() > (last_text_edit + Constant.API_DELAY - 500)) {
                if(!TextUtils.isEmpty(acDeliveryAddress.getText().toString().trim()) && !isFirst) {
                    getAutoCompleteAddresses(acDeliveryAddress.getText().toString().trim());
                }
                else
                    isFirst = false;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_location);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar(toolbar, R.drawable.ic_back, R.color.color_app_theme);
        ((TextView) findViewById(R.id.tvToolbarTitle)).setText(getString(R.string
                .text_store_location));
        mapView = (CustomEventMapView) findViewById(R.id.mapView);
        acDeliveryAddress = (CustomFontAutoCompleteView) findViewById(R.id.acDeliveryAddress);
        progressBar = findViewById(R.id.progressBar);
        ImageView ivTargetLocation = (ImageView) findViewById(R.id.ivTargetLocation);
        ImageView ivClearDeliveryAddressTextMap = (ImageView) findViewById(R.id
                .ivClearDeliveryAddressTextMap);
        CustomButton btnDone = (CustomButton) findViewById(R.id.btnDone);
        mapView.getMapAsync(this);
        ivTargetLocation.setOnClickListener(this);
        ivClearDeliveryAddressTextMap.setOnClickListener(this);
        btnDone.setOnClickListener(this);


        acDeliveryAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    ivClearDeliveryAddressTextMap.setVisibility(View.VISIBLE);
                } else {
                    ivClearDeliveryAddressTextMap.setVisibility(View.GONE);
                }
                handler.removeCallbacks(input_finish_checker);


            }

            @Override
            public void afterTextChanged(Editable s) {
                Utilities.printLog("afterTextChanged","****"+s);
                if (s.length() > 0 &&  acDeliveryAddress.getTag() == null ) {
                    Utilities.printLog("afterTextChanged","*in***"+s);
                    last_text_edit = System.currentTimeMillis();
                    handler.postDelayed(input_finish_checker, Constant.API_DELAY);
                }
            }
        });

        mapView.onCreate(savedInstanceState);
        locationHelper = new LocationHelper(this);
        locationHelper.setLocationReceivedLister(this);
        initPlaceAutoComplete();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        setToolbarCameraIcon(false);
        setToolbarEditIcon(false, R.drawable.ic_filter);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        locationHelper.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        locationHelper.onStop();
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

    private void initPlaceAutoComplete() {
        acDeliveryAddress = (CustomFontAutoCompleteView) findViewById(R.id.acDeliveryAddress);
        autocompleteAdapter = new PlaceAutocompleteAdapter(this);
        acDeliveryAddress.setAdapter(autocompleteAdapter);
        acDeliveryAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i < autocompleteAdapter.getSearchAddresses().size()){
                    AutocompleteAddress1 searchAddress = autocompleteAdapter.getSearchAddresses().get(i);
                    acDeliveryAddress.setTag(searchAddress.getAddress());
                    acDeliveryAddress.setText(searchAddress.getAddress());
                    acDeliveryAddress.setTag(null);
                    getAddressesFromId(searchAddress.getId());
                }
            }
        });


    }


    private void getAutoCompleteAddresses(CharSequence constraint){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(Constant.google.API_KEY, "GKOW7h1yt0yqwZzS105Jdg22725");
        /*Utils.showCustomProgressDialog(this, false);*/
        String apiUrl = "autocomplete/"+ constraint.toString();


        progressBar.setVisibility(View.VISIBLE);

        ApiInterface apiInterface =
                new ApiClient().changeApiBaseUrl(Constant.GET_ADDRESS_API_URL).create(ApiInterface.class);
        Call<AutoCompleteResponse> call = apiInterface.getAutoCompleteAddresses(apiUrl,hashMap);
        call.enqueue(new Callback<AutoCompleteResponse>() {
            @Override
            public void onResponse(Call<AutoCompleteResponse> call, Response<AutoCompleteResponse> response) {
                if (autocompleteAdapter != null && response.body().getSuggestions() != null) {
                    autocompleteAdapter.setSearchAddresses(response.body().getSuggestions());
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<AutoCompleteResponse> call, Throwable t) {

            }
        });
    }

    private void getAddressesFromId(String id){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(Constant.google.API_KEY, "GKOW7h1yt0yqwZzS105Jdg22725");
        Utilities.showCustomProgressDialog(this, false);
        String apiUrl = "get/"+ id;



        ApiInterface apiInterface =
                new ApiClient().changeApiBaseUrl(Constant.GET_ADDRESS_API_URL).create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.getAddressDataFromId(apiUrl,hashMap);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Utilities.hideCustomProgressDialog();

                HashMap<String, String> map = parseContent.parseAddress(response);
                if (map != null) {
                    storeLatLng = new LatLng(Double.valueOf(map.get(Constant
                            .google.LAT)), Double.valueOf(map.get(Constant
                            .google.LNG)));
                    isMapTouched = false;
                    moveCameraFirstMyLocation(true, storeLatLng);
                }
                isFirst = false;
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }


    @Override
    public void onConnected(Bundle bundle) {
        checkLocationPermission(null);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //do something
    }

    @Override
    public void onConnectionSuspended(int i) {
        //do something
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivTargetLocation:
                if (!locationHelper.isOpenGpsDialog()) {
                    locationHelper.setOpenGpsDialog(true);
                    locationHelper.setLocationSettingRequest(this, REQUEST_CHECK_SETTINGS, new
                            OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    checkLocationPermission(null);
                                }
                            }, new LocationHelper.NoGPSDeviceFoundListener() {
                        @Override
                        public void noFound() {
                            checkLocationPermission(null);
                        }
                    });
                }
                break;
            case R.id.ivClearDeliveryAddressTextMap:
                acDeliveryAddress.getText().clear();
                break;
            case R.id.btnDone:
                if (!TextUtils.isEmpty(acDeliveryAddress.getText().toString()) && storeLatLng !=
                        null) {
                    Intent intent = new Intent();
                    intent.putExtra(Constant.LATITUDE, storeLatLng.latitude);
                    intent.putExtra(Constant.LONGITUDE, storeLatLng.longitude);
                    intent.putExtra(Constant.ADDRESS, acDeliveryAddress.getText().toString());
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    Utilities.showToast(this, getResources().getString(R.string
                            .text_select_address_or_location));
                }

                break;
            default:
                // do with default
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        checkLocationPermission(null);
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        locationHelper.setOpenGpsDialog(false);
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    /**
     * This method is used to setUpMap option which help to load map as per option
     */
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

        this.googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                Utilities.printLog("IS_MAP_TOUCHED", String.valueOf(isMapTouched));
                if (isMapTouched) {
                    deliveryLatLng = googleMap.getCameraPosition().target;
                    getGeocodeDataFromLocation(deliveryLatLng);
                }
                isMapTouched = true;

            }

        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        setUpMap();
    }

    /***
     * this method is used to move camera on map at current position and a isCameraMove is
     * used to decide when is move or not
     */
    public void moveCameraFirstMyLocation(final boolean isAnimate, LatLng latLng) {
        if (latLng == null) {
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

        } else {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng).zoom(17).build();

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

    private void setDeliveryAddress(String deliveryAddress) {
        acDeliveryAddress.setTag(deliveryAddress);
        acDeliveryAddress.setFocusable(false);
        acDeliveryAddress.setFocusableInTouchMode(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            acDeliveryAddress.setText(deliveryAddress, false);
        } else {
            acDeliveryAddress.setText(deliveryAddress);
        }
        acDeliveryAddress.setFocusable(true);
        acDeliveryAddress.setFocusableInTouchMode(true);
        acDeliveryAddress.setTag(null);
    }

    /**
     * this method called webservice for get location from storeAddress which is provided by Google
     *
     * @param address storeAddress on map
     */
    private void getGeocodeDataFromAddress(final String address) {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(Constant.google.ADDRESS, address);
        hashMap.put(Constant.google.KEY, preferenceHelper.getGoogleKey());
        Utilities.showCustomProgressDialog(this, false);
        ApiInterface apiInterface = new ApiClient().changeApiBaseUrl(Constant
                .GOOGLE_API_URL).create
                (ApiInterface.class);
        Call<ResponseBody> bodyCall = apiInterface.getGoogleGeocode(hashMap);
        bodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                Utilities.hideCustomProgressDialog();
                HashMap<String, String> map = parseContent.parsGoogleGeocode(response);
                if (map != null) {
                    storeLatLng = new LatLng(Double.valueOf(map.get(Constant
                            .google.LAT)), Double.valueOf(map.get(Constant
                            .google.LNG)));
                    isMapTouched = false;
                    moveCameraFirstMyLocation(true, storeLatLng);
                }
                isFirst = false;
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });


    }

    /**
     * this method called webservice for get Data from LatLng which is provided by Google
     *
     * @param latLng on map
     */
    private void getGeocodeDataFromLocation(LatLng latLng) {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(Constant.google.LAT_LNG, latLng.latitude + "," + latLng.longitude);
        hashMap.put(Constant.google.KEY, preferenceHelper.getGoogleKey());
        Utilities.showCustomProgressDialog(this, false);
        ApiInterface apiInterface = new ApiClient().changeApiBaseUrl(Constant
                .GOOGLE_API_URL).create
                (ApiInterface
                        .class);
        Call<ResponseBody> bodyCall = apiInterface.getGoogleGeocode(hashMap);
        bodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Utilities.hideCustomProgressDialog();
                HashMap<String, String> map = parseContent.parsGoogleGeocode(response);
                if (map != null) {
                    storeLatLng = new LatLng(Double.valueOf(map.get(Constant
                            .google.LAT)), Double.valueOf(map.get(Constant
                            .google.LNG)));
                    setDeliveryAddress(map.get(Constant.google.FORMATTED_ADDRESS));
                }
                isFirst = false;

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });


    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }

    private void checkLocationPermission(LatLng latLng) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission
                .ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission
                            .ACCESS_FINE_LOCATION, android.Manifest.permission
                            .ACCESS_COARSE_LOCATION},
                    Constant.PERMISSION_FOR_LOCATION);
        } else {
            //Do the stuff that requires permission...
            moveCameraFirstMyLocation(true, latLng);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0) {
            switch (requestCode) {
                case Constant.PERMISSION_FOR_LOCATION:
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        //Do the stuff that requires permission...
                        moveCameraFirstMyLocation(true, null);
                    }
                    break;
                default:
                    //do with default
                    break;
            }
        }
    }


    private void setPlaceFilter(String countryCode) {
        if (autocompleteAdapter != null) {

            autocompleteAdapter.setPlaceFilter(countryCode);
            locationHelper.getLastLocation(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        LatLng latLng = new LatLng(location.getLatitude(),
                                location.getLongitude());
                        RectangularBounds latLngBounds = RectangularBounds.newInstance(
                                latLng,
                                latLng);
                        autocompleteAdapter.setBounds(latLngBounds);
                    }


                }
            });

        }
    }
}

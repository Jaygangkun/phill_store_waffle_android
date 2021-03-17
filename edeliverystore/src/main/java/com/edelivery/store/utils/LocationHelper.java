package com.edelivery.store.utils;

import android.Manifest;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;


/**
 * Created by elluminati on 20-06-2016.
 */
public class LocationHelper implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient
        .ConnectionCallbacks {

    private static final long INTERVAL = 5000;// millisecond
    private static final long FASTEST_INTERVAL = 4000;// millisecond
    private final String Tag = "LOCATION_HELPER";
    public GoogleApiClient googleApiClient;
    public LocationRequest locationRequest;
    private Context context;
    private OnLocationReceived locationReceived;
    private LocationSettingsRequest locationSettingsRequest;
    private SettingsClient client;
    private boolean isOpenGpsDialog = false;

    public LocationHelper(Context context) {
        this.context = context;
        getLocationRequest();
        getGoogleApiClientConnect();
    }

    public void setLocationReceivedLister(OnLocationReceived locationReceived) {
        this.locationReceived = locationReceived;
    }

    private void getGoogleApiClientConnect() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder
                (GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();

        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).addApi(Auth
                        .GOOGLE_SIGN_IN_API, googleSignInOptions)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
    }

    private void getLocationRequest() {

        locationRequest = new LocationRequest();
        locationRequest.setInterval(INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        locationSettingsRequest = builder.build();
        client = LocationServices.getSettingsClient(context);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Utilities.printLog(Tag, "GoogleApiClient is Connected Successfully");
        locationReceived.onConnected(bundle);

    }

    @Override
    public void onConnectionSuspended(int i) {
        Utilities.printLog(Tag, "GoogleApiClient is Connection Suspended ");

        locationReceived.onConnectionSuspended(i);

    }

    public void getLastLocation(Context context, OnSuccessListener<Location> onSuccessListener) {
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices
                .getFusedLocationProviderClient(context);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(onSuccessListener);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Utilities.printLog(Tag, "GoogleApiClient is Failed to Connect ");
        locationReceived.onConnectionFailed(connectionResult);

    }

    public void onStart() {
        if (googleApiClient != null && !googleApiClient.isConnected()) {
            googleApiClient.connect(GoogleApiClient.SIGN_IN_MODE_OPTIONAL);
        }
    }

    public void onStop() {
        googleApiClient.disconnect();
    }

    public void setLocationSettingRequest(final AppCompatActivity activity, final int requestCode,
                                          OnSuccessListener onSuccessListener,
                                          final NoGPSDeviceFoundListener noGPSDeviceFoundListener) {

        Task<LocationSettingsResponse> task = client.checkLocationSettings(locationSettingsRequest);
        task.addOnFailureListener(activity, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                int statusCode = ((ApiException) e).getStatusCode();
                switch (statusCode) {
                    case CommonStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult(activity,
                                    requestCode);

                        } catch (IntentSender.SendIntentException sendEx) {
                            // Ignore the error.

                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.
                        if (noGPSDeviceFoundListener != null) {
                            noGPSDeviceFoundListener.noFound();
                        }
                        break;
                }
            }
        });
        task.addOnSuccessListener(activity, onSuccessListener);


    }

    public boolean isOpenGpsDialog() {
        return isOpenGpsDialog;
    }

    public void setOpenGpsDialog(boolean openGpsDialog) {
        isOpenGpsDialog = openGpsDialog;
    }

    public interface OnLocationReceived {


        public void onConnected(Bundle bundle);

        public void onConnectionFailed(@NonNull ConnectionResult connectionResult);

        public void onConnectionSuspended(int i);


    }

    public interface NoGPSDeviceFoundListener {
        void noFound();
    }
}

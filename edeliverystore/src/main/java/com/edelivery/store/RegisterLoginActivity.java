package com.edelivery.store;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.edelivery.store.adapter.ViewPagerAdapter;
import com.edelivery.store.fragment.LoginFragment;
import com.edelivery.store.fragment.RegisterFragment;
import com.edelivery.store.models.datamodel.StoreData;
import com.edelivery.store.models.singleton.CurrentBooking;
import com.edelivery.store.models.singleton.SubStoreAccess;
import com.edelivery.store.persistentroomdata.NotificationRepository;
import com.edelivery.store.utils.Constant;
import com.edelivery.store.utils.LocationHelper;
import com.edelivery.store.utils.PreferenceHelper;
import com.edelivery.store.utils.Utilities;
import com.elluminati.edelivery.store.BuildConfig;
import com.elluminati.edelivery.store.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.internal.ImageRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.material.tabs.TabLayout;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.viewpager.widget.ViewPager;

public class RegisterLoginActivity extends BaseActivity implements LocationHelper
        .OnLocationReceived {

    public static final String TAG = RegisterLoginActivity.class.getName();
    public PreferenceHelper preferenceHelper;
    public CallbackManager callbackManager;
    public LocationHelper locationHelper;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ViewPagerAdapter viewPagerAdapter;
    private RegisterFragment registerFragment;
    private LoginFragment loginFragment;
    private TwitterLoginButton twitterLoginButton;
    private ProfileTracker profileTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_login);
        initTwitter();
        CurrentBooking.getInstance().clearCurrentBookingModel();
        SubStoreAccess.getInstance().clearStoreAccess();
        NotificationRepository.getInstance(this).clearNotification();
        preferenceHelper = PreferenceHelper.getPreferenceHelper(this);
        preferenceHelper.clearVerification();
        preferenceHelper.logout();
        LoginManager.getInstance().logOut();
        locationHelper = new LocationHelper(this);
        locationHelper.setLocationReceivedLister(this);
        callbackManager = CallbackManager.Factory.create();
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);


    }


    public void gotoHomeActivity(StoreData storeData) {
        Intent intent = new Intent(this, HomeActivity.class);
//        intent.putExtra(Constant.STORE_ID, storeData);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    @Override
    public void onConnected(Bundle bundle) {
        initTabLayout();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constant.GOOGLE_SIGN_IN:
                // result from google
                getGoogleSignInResult(data);
                break;
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
        twitterLoginButton.onActivityResult(requestCode, resultCode, data);


    }

    private void initTabLayout() {

        if (viewPagerAdapter == null) {
            viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
            viewPagerAdapter.addFragment(new LoginFragment(), getString(R.string.text_login));
            viewPagerAdapter.addFragment(new RegisterFragment(), getString(R.string.text_register));
            viewPager.setAdapter(viewPagerAdapter);
            tabLayout.setupWithViewPager(viewPager);
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    int position = tab.getPosition();
                    switch (position) {
                        case 0:
                            LoginFragment loginFragment = (LoginFragment) viewPagerAdapter.getItem
                                    (position);
                            loginFragment.clearError();
                            break;
                        case 1:
                            RegisterFragment registerFragment = (RegisterFragment) viewPagerAdapter
                                    .getItem(position);
                            registerFragment.clearError();
                            break;
                        default:
                            // do with default
                            break;
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
            loginFragment = (LoginFragment) viewPagerAdapter.getItem(0);
            registerFragment = (RegisterFragment) viewPagerAdapter
                    .getItem(1);
        }


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

    private void initTwitter() {
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(getResources().getString(R.string
                        .TWITTER_CONSUMER_KEY),
                        getResources().getString(R.string.TWITTER_CONSUMER_SECRET)))
                .debug(BuildConfig.DEBUG)
                .build();
        Twitter.initialize(config);
    }


    private void getFacebookSignInResult(AccessToken accessToken, final Profile profile) {
        GraphRequest data_request = GraphRequest.newMeRequest(
                accessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject json_object,
                            GraphResponse response) {
                        try {
                            registerFragment.updateUiForSocialLogin(json_object.getString(Constant
                                            .Facebook.EMAIL),
                                    profile.getId(), profile.getFirstName(),
                                    profile.getLastName(), ImageRequest.getProfilePictureUri
                                            (profile.getId(), 250,
                                                    250));
                            // LoginManager.getInstance().logOut();
                        } catch (JSONException e) {
                            Utilities.handleException(TAG, e);
                        }

                    }

                });
        Bundle permission_param = new Bundle();
        permission_param.putString("fields", "id,name,email,picture");
        data_request.setParameters(permission_param);
        data_request.executeAsync();
    }

    private void getGoogleSignInResult(Intent data) {
        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent
                (data);
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            if (isLoginTabSelect()) {
                loginFragment.login(acct.getId());
            } else {
                if (acct != null) {
                    String firstName;
                    String lastName = "";
                    if (acct.getDisplayName().contains(" ")) {
                        String[] strings = acct.getDisplayName().split(" ");
                        firstName = strings[0];
                        lastName = strings[1];
                    } else {
                        firstName = acct.getDisplayName();
                    }

                    registerFragment.updateUiForSocialLogin(acct.getEmail(), acct.getId(),
                            firstName,
                            lastName, acct
                                    .getPhotoUrl());
                }
            }


        } else {
            // Signed out, show unauthenticated UI.
            Utilities.printLog("GOOGLE_RESULT", "failed");
        }
    }


    private boolean isLoginTabSelect() {
        return tabLayout.getSelectedTabPosition() == 0;
    }

    public void initTwitterLogin(View view) {
        twitterLoginButton = view.findViewById(R.id.btnTwitterLogin);
        twitterLoginButton.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimensionPixelSize(R.dimen.size_app_text_regular));
        twitterLoginButton.setCompoundDrawablesRelativeWithIntrinsicBounds(AppCompatResources.getDrawable(this, R.drawable.iconmonstr_twitter_1), null, null, null);
        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(final Result<TwitterSession> result) {
                Utilities.showCustomProgressDialog(RegisterLoginActivity.this, false);
                TwitterAuthClient twitterAuthClient = new TwitterAuthClient();
                twitterAuthClient.requestEmail(result.data, new Callback<String>() {
                    @Override
                    public void success(Result<String> result2) {
                        Utilities.hideCustomProgressDialog();
                        if (isLoginTabSelect()) {
                            loginFragment.login(String.valueOf(result.data.getUserId()));
                        } else {
                            registerFragment.updateUiForSocialLogin(result2.data, String
                                            .valueOf(result.data
                                                    .getUserId
                                                            ()),
                                    result.data.getUserName(), "", null);
                        }
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        Utilities.hideCustomProgressDialog();
                        Utilities.handleException("TWITTER_LOGIN", exception);
                    }
                });
            }

            @Override
            public void failure(TwitterException exception) {

            }
        });
    }


    public void initGoogleLogin(View view) {
        SignInButton btnGoogleSingIn;
        btnGoogleSingIn = view.findViewById(R.id.btnGoogleLogin);
        btnGoogleSingIn.setSize(SignInButton.SIZE_WIDE);
        btnGoogleSingIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Auth.GoogleSignInApi.revokeAccess(locationHelper.googleApiClient)
                        .setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(Status status) {
                                        Utilities.printLog("REVOKE_ACCESS_GOOGLE",
                                                status.toString());
                                        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent
                                                (locationHelper
                                                        .googleApiClient);
                                        startActivityForResult(signInIntent,
                                                Constant.GOOGLE_SIGN_IN);
                                    }
                                });
            }
        });
    }

    public void initFBLogin(View view) {
        callbackManager = CallbackManager.Factory.create();
        LoginButton faceBookLogin = view.findViewById(R.id.btnFbLogin);
        faceBookLogin.setReadPermissions(Arrays.asList(Constant.Facebook.PUBLIC_PROFILE,
                Constant.Facebook.EMAIL));
        faceBookLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if (loginResult != null) {
                    if (isLoginTabSelect()) {
                        loginFragment.login(loginResult.getAccessToken().getUserId());
                        LoginManager.getInstance().logOut();
                    } else {
                        profileTracker.startTracking();
                    }

                } else {
                    profileTracker.stopTracking();
                }

            }

            @Override
            public void onCancel() {
                Utilities.showToast(RegisterLoginActivity.this, "Facebook login cancel");

            }

            @Override
            public void onError(FacebookException error) {
                Utilities.showToast(RegisterLoginActivity.this, "Facebook login error");
            }
        });
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile,
                                                   Profile currentProfile) {
                if (AccessToken.getCurrentAccessToken() != null && !AccessToken.getCurrentAccessToken().isExpired()) {
                    getFacebookSignInResult(AccessToken.getCurrentAccessToken(), currentProfile);
                }
            }
        };

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (profileTracker != null) {
            profileTracker.stopTracking();
        }


    }


}

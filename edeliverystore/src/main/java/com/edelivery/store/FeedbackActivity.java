package com.edelivery.store;

import android.app.Activity;
import android.os.Bundle;
import androidx.core.content.res.ResourcesCompat;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.edelivery.store.models.datamodel.ProviderDetail;
import com.edelivery.store.models.datamodel.UserDetail;
import com.edelivery.store.models.responsemodel.IsSuccessResponse;
import com.edelivery.store.parse.ApiClient;
import com.edelivery.store.parse.ApiInterface;
import com.edelivery.store.parse.ParseContent;
import com.edelivery.store.utils.Constant;
import com.edelivery.store.utils.GlideApp;
import com.edelivery.store.utils.Utilities;
import com.edelivery.store.widgets.CustomButton;
import com.edelivery.store.widgets.CustomEditText;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.elluminati.edelivery.store.BuildConfig.IMAGE_URL;

public class FeedbackActivity extends BaseActivity {
    private ImageView ivProviderImageFeedback;
    private CustomTextView tvProviderNameFeedback;
    private RatingBar ratingBarFeedback;
    private CustomEditText etFeedbackReview;
    private CustomButton btnSubmitFeedback;
    private UserDetail userDetail;
    private ProviderDetail providerDetail;
    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar(toolbar, R.drawable.ic_back, R.color.color_app_theme);
        ((TextView) findViewById(R.id.tvToolbarTitle)).setText(getString(R.string.text_feed_back));
        ivProviderImageFeedback = (ImageView) findViewById(R.id
                .ivProviderImageFeedback);
        tvProviderNameFeedback = (CustomTextView) findViewById(R.id
                .tvProviderNameFeedback);
        ratingBarFeedback = (RatingBar) findViewById(R.id.ratingBarFeedback);
        etFeedbackReview = (CustomEditText) findViewById(R.id
                .etFeedbackReview);
        btnSubmitFeedback = (CustomButton) findViewById(R.id.btnSubmitFeedback);
        btnSubmitFeedback.setOnClickListener(this);
        ratingBarFeedback.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                ratingBarFeedback.setRating(v);
            }
        });
        loadExtraData();
        loadData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        setToolbarCameraIcon(false);
        setToolbarEditIcon(false, R.drawable.ic_filter);
        return true;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btnSubmitFeedback:
                sendFeedback();
                break;

            default:
                // do with default
                break;
        }
    }

    private void sendFeedback() {
        if (ratingBarFeedback.getRating() == 0) {
            Utilities.showToast(this, getResources().getString(R
                    .string.msg_plz_give_rating));
        } else {
            Utilities.showCustomProgressDialog(this, false);
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<IsSuccessResponse> responseCall = null;

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(Constant.ORDER_ID, orderId);
                jsonObject.put(Constant.STORE_ID, preferenceHelper.getStoreId());
                jsonObject.put(Constant.SERVER_TOKEN, preferenceHelper.getServerToken());
                if (providerDetail == null) {
                    jsonObject.put(Constant.STORE_RATING_TO_USER, ratingBarFeedback
                            .getRating());
                    jsonObject.put(Constant.STORE_REVIEW_TO_USER, etFeedbackReview
                            .getText().toString());

                    responseCall = apiInterface.setFeedbackUser(ApiClient
                            .makeJSONRequestBody(jsonObject));
                } else {
                    jsonObject.put(Constant.STORE_RATING_TO_PROVIDER, ratingBarFeedback
                            .getRating());
                    jsonObject.put(Constant.STORE_REVIEW_TO_PROVIDER, etFeedbackReview
                            .getText().toString());

                    responseCall = apiInterface.setFeedbackProvider(ApiClient
                            .makeJSONRequestBody(jsonObject));
                }
                responseCall.enqueue(new Callback<IsSuccessResponse>() {
                    @Override
                    public void onResponse(Call<IsSuccessResponse> call, Response<IsSuccessResponse>
                            response) {
                        Utilities.hideCustomProgressDialog();
                        if (response.isSuccessful()) {
                            if (response.body().isSuccess()) {
                                //add
                                setResult(Activity.RESULT_OK);
                                finish();
                            } else {
                                ParseContent.getParseContentInstance().showErrorMessage
                                        (FeedbackActivity.this,
                                                response.body().getErrorCode(), false);
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<IsSuccessResponse> call, Throwable t) {
                        Utilities.printLog(HistoryDetailActivity.class.getName(), t.toString());
                        Utilities.hideCustomProgressDialog();
                    }
                });
            } catch (JSONException e) {
                Utilities.handleException(HistoryDetailActivity.class.getName(), e);
            }


        }
    }

    private void loadExtraData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            orderId = bundle.getString(Constant.ORDER_ID);
            userDetail = bundle.getParcelable(Constant.USER_DETAIL);
            providerDetail = bundle.getParcelable(Constant.PROVIDER_DETAIL);
        }


    }

    private void loadData() {
        if (providerDetail == null) {
            GlideApp.with(this).load(IMAGE_URL + userDetail.getImageUrl()
            )
                    .dontAnimate()
                    .placeholder
                            (ResourcesCompat.getDrawable(this
                                    .getResources(), R.drawable.placeholder, null)).fallback
                    (ResourcesCompat
                            .getDrawable
                                    (this.getResources(), R.drawable.placeholder, null)).into
                    (ivProviderImageFeedback);
            tvProviderNameFeedback.setText(userDetail.getFirstName() + " " +
                    "" + userDetail.getLastName());

        } else {
            GlideApp.with(this).load(IMAGE_URL + providerDetail.getImageUrl()
            )
                    .dontAnimate()
                    .placeholder
                            (ResourcesCompat.getDrawable(this
                                    .getResources(), R.drawable.placeholder, null)).fallback
                    (ResourcesCompat
                            .getDrawable
                                    (this.getResources(), R.drawable.placeholder, null)).into
                    (ivProviderImageFeedback);
            tvProviderNameFeedback.setText(providerDetail.getFirstName() + " " +
                    "" + providerDetail.getLastName());
        }

    }
}

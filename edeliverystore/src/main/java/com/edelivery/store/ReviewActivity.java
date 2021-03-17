package com.edelivery.store;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.edelivery.store.adapter.PublicReviewAdapter;
import com.edelivery.store.models.datamodel.StoreReview;
import com.edelivery.store.models.responsemodel.ReviewResponse;
import com.edelivery.store.parse.ApiClient;
import com.edelivery.store.parse.ApiInterface;
import com.edelivery.store.utils.Constant;
import com.edelivery.store.utils.Utilities;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewActivity extends BaseActivity {
    public static final String TAG = ReviewActivity.class.getName();
    private RecyclerView rcvPublicReview;
    private List<StoreReview> storePublicReview;
    private PublicReviewAdapter publicReviewAdapter;
    private CustomTextView tvReviewAverage, tv5StarCount, tv4StarCount, tv3StarCount,
            tv2StarCount, tv1StarCount;
    private RatingBar ratingBar;
    private ProgressBar bar5Star, bar4Star, bar3Star, bar2Star, bar1Star;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        storePublicReview = new ArrayList<>();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar(toolbar, R.drawable.ic_back, R.color.color_app_theme);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ((TextView) findViewById(R.id.tvToolbarTitle)).setText(getString(R.string.text_review));
        rcvPublicReview = (RecyclerView) findViewById(R.id.rcvPublicReview);
        tvReviewAverage = (CustomTextView) findViewById(R.id.tvReviewAverage);
        tv5StarCount = (CustomTextView) findViewById(R.id.tv5StarCount);
        tv4StarCount = (CustomTextView) findViewById(R.id.tv4StarCount);
        tv3StarCount = (CustomTextView) findViewById(R.id.tv3StarCount);
        tv2StarCount = (CustomTextView) findViewById(R.id.tv2StarCount);
        tv1StarCount = (CustomTextView) findViewById(R.id.tv1StarCount);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        bar5Star = (ProgressBar) findViewById(R.id.bar5Star);
        bar4Star = (ProgressBar) findViewById(R.id.bar4Star);
        bar3Star = (ProgressBar) findViewById(R.id.bar3Star);
        bar2Star = (ProgressBar) findViewById(R.id.bar2Star);
        bar1Star = (ProgressBar) findViewById(R.id.bar1Star);
        initRcvStorePublicReview();
        getStoreReview(preferenceHelper.getStoreId());
    }

    private void initRcvStorePublicReview() {
        rcvPublicReview.setLayoutManager(new LinearLayoutManager(this));
        publicReviewAdapter = new PublicReviewAdapter(storePublicReview, this) {
            @Override
            public void onLike(int position) {

            }

            @Override
            public void onDislike(int position) {
            }
        };
        rcvPublicReview.setNestedScrollingEnabled(false);
        rcvPublicReview.setAdapter(publicReviewAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        setToolbarCameraIcon(false);
        setToolbarEditIcon(false, 0);
        return true;
    }

    private void getStoreReview(String storeId) {
        Utilities.showCustomProgressDialog(this, false);
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put(Constant.STORE_ID, storeId);
        } catch (JSONException e) {
            Utilities.handleThrowable(TAG, e);
        }
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ReviewResponse> call = apiInterface.getStoreReview(ApiClient
                .makeJSONRequestBody(jsonObject));
        call.enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call,
                                   Response<ReviewResponse
                                           > response) {
                Utilities.hideCustomProgressDialog();
                Utilities.printLog("REVIEW_STORE", ApiClient.JSONResponse(response.body()));
                if (parseContent.isSuccessful(response)) {
                    if (response.body().isSuccess()) {
                        storePublicReview.clear();
                        storePublicReview.addAll(response.body().getStoreReviewList());
                        publicReviewAdapter.notifyDataSetChanged();
                        setReviewData(storePublicReview);


                    } else {
                        parseContent.showErrorMessage(ReviewActivity.this, response.body()
                                        .getErrorCode()
                                , false);
                    }
                }

            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                Utilities.handleThrowable(TAG, t);
            }
        });
    }

    private void setReviewData(List<StoreReview> reviewData) {
        int oneStar = 0, twoStat = 0, threeStar = 0, fourStar = 0, fiveStar = 0;
        float rate = 0;
        for (StoreReview review : reviewData) {
            int rateRound = (int) Math.round(review.getUserRatingToStore());
            rate += review.getUserRatingToStore();
            switch (rateRound) {
                case 1:
                    oneStar++;
                    break;
                case 2:
                    twoStat++;
                    break;
                case 3:
                    threeStar++;
                    break;
                case 4:
                    fourStar++;
                    break;
                case 5:
                    fiveStar++;
                    break;
                default:
                    // do with default
                    break;
            }
        }
        tv1StarCount.setText(String.valueOf(oneStar));
        tv2StarCount.setText(String.valueOf(twoStat));
        tv3StarCount.setText(String.valueOf(threeStar));
        tv4StarCount.setText(String.valueOf(fourStar));
        tv5StarCount.setText(String.valueOf(fiveStar));
        int totalRating = reviewData.size();
        if (totalRating > 0) {
            twoStat = twoStat * 100 / totalRating;
            threeStar = threeStar * 100 / totalRating;
            fourStar = fourStar * 100 / totalRating;
            fiveStar = fiveStar * 100 / totalRating;
            bar5Star.setProgress(fiveStar);
            bar4Star.setProgress(fourStar);
            bar3Star.setProgress(threeStar);
            bar2Star.setProgress(twoStat);
            bar1Star.setProgress(oneStar);
            rate = (rate / totalRating);
        }
        tvReviewAverage.setText(String.valueOf((float) Math.round(rate)));
        ratingBar.setRating((float) Math.round(rate));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}

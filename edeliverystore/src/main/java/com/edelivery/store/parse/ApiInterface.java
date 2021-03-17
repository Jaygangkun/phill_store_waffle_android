package com.edelivery.store.parse;

import com.edelivery.store.models.datamodel.AppSetting;
import com.edelivery.store.models.responsemodel.AddCartResponse;
import com.edelivery.store.models.responsemodel.AddOrDeleteSpecificationResponse;
import com.edelivery.store.models.responsemodel.AddOrUpdateItemResponse;
import com.edelivery.store.models.responsemodel.AllDocumentsResponse;
import com.edelivery.store.models.responsemodel.AutoCompleteResponse;
import com.edelivery.store.models.responsemodel.BankDetailResponse;
import com.edelivery.store.models.responsemodel.CardsResponse;
import com.edelivery.store.models.responsemodel.CategoriesResponse;
import com.edelivery.store.models.responsemodel.CityResponse;
import com.edelivery.store.models.responsemodel.CountriesResponse;
import com.edelivery.store.models.responsemodel.DayEarningResponse;
import com.edelivery.store.models.responsemodel.DocumentResponse;
import com.edelivery.store.models.responsemodel.HistoryDetailsResponse;
import com.edelivery.store.models.responsemodel.HistoryResponse;
import com.edelivery.store.models.responsemodel.ImageSettingsResponse;
import com.edelivery.store.models.responsemodel.InvoiceResponse;
import com.edelivery.store.models.responsemodel.IsSuccessResponse;
import com.edelivery.store.models.responsemodel.ItemsResponse;
import com.edelivery.store.models.responsemodel.NearestProviderResponse;
import com.edelivery.store.models.responsemodel.OTPResponse;
import com.edelivery.store.models.responsemodel.OrderDetailResponse;
import com.edelivery.store.models.responsemodel.OrderResponse;
import com.edelivery.store.models.responsemodel.OrderStatusResponse;
import com.edelivery.store.models.responsemodel.PaymentGatewayResponse;
import com.edelivery.store.models.responsemodel.ProductGroupsResponse;
import com.edelivery.store.models.responsemodel.ProductListResponse;
import com.edelivery.store.models.responsemodel.ProductResponse;
import com.edelivery.store.models.responsemodel.PromoCodeResponse;
import com.edelivery.store.models.responsemodel.ReviewResponse;
import com.edelivery.store.models.responsemodel.SpecificationGroupFroAddItemResponse;
import com.edelivery.store.models.responsemodel.SpecificationGroupResponse;
import com.edelivery.store.models.responsemodel.StoreDataResponse;
import com.edelivery.store.models.responsemodel.SubStoresResponse;
import com.edelivery.store.models.responsemodel.VehiclesResponse;
import com.edelivery.store.models.responsemodel.WalletHistoryResponse;
import com.edelivery.store.models.responsemodel.WalletResponse;
import com.edelivery.store.models.responsemodel.WalletTransactionResponse;
import com.edelivery.store.utils.Constant;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * A class to call different api on 24-01-2017.
 */

public interface ApiInterface {


    @Multipart
    @POST("api/admin/check_app_keys")
    Call<AppSetting> getAppSettingDetail(@PartMap Map<String, RequestBody> map);

    @Multipart
    @POST("api/store/logout")
    Call<IsSuccessResponse> logout(@PartMap Map<String, RequestBody> map);

    @GET("api/admin/get_country_list")
    Call<CountriesResponse> getCountries();

    @FormUrlEncoded
    @POST("api/admin/get_city_list")
    Call<CityResponse> getCities(@Field(Constant.COUNTRY_ID) String countryId);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/admin/get_delivery_list_for_city")
    Call<CategoriesResponse> getCategories(@Body RequestBody requestBody);

    @GET("http://maps.googleapis.com/maps/api/geocode/json?")
    Call<ResponseBody> getLatLngFromAddress(@Query(Constant.ADDRESS) String address);

    @Multipart
    @POST("api/store/register")
    Call<StoreDataResponse> register(@PartMap Map<String, RequestBody> map, @Part MultipartBody
            .Part profile);

    @Multipart
    @POST("api/store/register")
    Call<StoreDataResponse> register(@PartMap Map<String, RequestBody> map);

    @Multipart
    @POST("api/admin/otp_verification")
    Call<OTPResponse> otpVerification(@PartMap Map<String, RequestBody> map);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/store/otp_verification")
    Call<IsSuccessResponse> storeOtpVerification(@Body RequestBody requestBody);


    @Multipart
    @POST("api/admin/forgot_password")
    Call<IsSuccessResponse> forgotPassword(@PartMap Map<String, RequestBody> map);

    @Multipart
    @POST("api/store/login")
    Call<StoreDataResponse> login(@PartMap Map<String, RequestBody> map);


    @Multipart
    @POST("api/store/get_store_data")
    Call<StoreDataResponse> getStoreDate(@PartMap Map<String, RequestBody> map);


    @Multipart
    @POST("api/store/get_product_list")
    Call<ProductResponse> getProductList(@PartMap Map<String, RequestBody> map);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/store/add_product")
    Call<IsSuccessResponse> addProduct(@Body RequestBody requestBody);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/store/update_product")
    Call<IsSuccessResponse> updateProduct(@Body RequestBody requestBody);

    @Multipart
    @POST("api/store/add_product")
    Call<IsSuccessResponse> addProduct(@PartMap Map<String, RequestBody> map, @Part MultipartBody
            .Part productLogo);


    @Multipart
    @POST("api/store/add_product")
    Call<IsSuccessResponse> addProduct(@PartMap Map<String, RequestBody> map);

    @Multipart
    @POST("api/store/update_product")
    Call<IsSuccessResponse> updateProduct(@PartMap Map<String, RequestBody> map, @Part
            MultipartBody.Part productLogo);

    @Multipart
    @POST("api/store/update_product")
    Call<IsSuccessResponse> updateProduct(@PartMap Map<String, RequestBody> map);

    @Multipart
    @POST("api/store/add_product_group_data")
    Call<IsSuccessResponse> addProductGroup(@PartMap Map<String, RequestBody> map,
                                            @Part MultipartBody
                                                    .Part productLogo);


    @Multipart
    @POST("api/store/add_product_group_data")
    Call<IsSuccessResponse> addProductGroup(@PartMap Map<String, RequestBody> map);

    @Multipart
    @POST("api/store/update_product_group")
    Call<IsSuccessResponse> updateProductGroup(@PartMap Map<String, RequestBody> map, @Part
            MultipartBody.Part productLogo);

    @Multipart
    @POST("api/store/update_product_group")
    Call<IsSuccessResponse> updateProductGroup(@PartMap Map<String, RequestBody> map);


    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/store/add_specification")
    Call<AddOrDeleteSpecificationResponse> addSpecification(@Body RequestBody requestBody);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/store/get_detail")
    Call<StoreDataResponse> getDetails(@Body RequestBody requestBody);

    @Multipart
    @POST("api/store/update")
    Call<StoreDataResponse> updateProfile(@PartMap Map<String, RequestBody> map, @Part
            MultipartBody.Part profile);

    @Multipart
    @POST("api/store/update")
    Call<StoreDataResponse> updateProfile(@PartMap Map<String, RequestBody> map);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/store/update")
    Call<StoreDataResponse> updateSettings(@Body RequestBody requestBody);


    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/store/delete_specification")
    Call<AddOrDeleteSpecificationResponse> deleteSpecification(@Body RequestBody requestBody);


    @Multipart
    @POST("api/store/get_store_product_item_list")
    Call<ProductResponse> getItemList(@PartMap Map<String, RequestBody> map);


    @Multipart
    @POST("api/store/upload_item_image")
    Call<AddOrUpdateItemResponse> addItemImage(@PartMap Map<String, RequestBody> map, @Part
            MultipartBody
                    .Part[] itemImage);

    @Multipart
    @POST("api/store/is_item_in_stock")
    Call<IsSuccessResponse> isItemInStock(@PartMap Map<String, RequestBody> map);


    @Multipart
    @POST("api/store/order_list")
    Call<OrderResponse> getOrderList(@PartMap Map<String, RequestBody> map);

    @Multipart
    @POST("api/store/set_order_status")
    Call<OrderStatusResponse> setOrderStatus(@PartMap Map<String, RequestBody> map);

    @Multipart
    @POST("api/store/store_cancel_or_reject_order")
    Call<IsSuccessResponse> CancelOrRejectOrder(@PartMap Map<String, RequestBody> map);

    @Multipart
    @POST("api/store/create_request")
    Call<OrderStatusResponse> assignProvider(@PartMap Map<String, RequestBody> map);

    @Multipart
    @POST("api/store/order_list_for_delivery")
    Call<OrderResponse> getDeliveryList(@PartMap Map<String, RequestBody> map);

    @Multipart
    @POST("api/store/check_order_status")
    Call<OrderStatusResponse> checkOrderStatus(@PartMap Map<String, RequestBody> map);

    @Multipart
    @POST("api/store/check_request_status")
    Call<OrderStatusResponse> checkRequestStatus(@PartMap Map<String, RequestBody> map);

    @Multipart
    @POST("api/store/order_history")
    Call<HistoryResponse> getHistoryList(@PartMap Map<String, RequestBody> map);

    @Multipart
    @POST("api/store/order_history_detail")
    Call<HistoryDetailsResponse> getHistoryDetails(@PartMap Map<String, RequestBody> map);


    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/store/get_specification_group")
    Call<SpecificationGroupResponse> getSpecificationGroup(@Body RequestBody requestBody);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/store/get_specification_group")
    Call<SpecificationGroupFroAddItemResponse> getSpecificationGroupFroAddItem(@Body RequestBody
                                                                                       requestBody);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/store/add_specification_group")
    Call<IsSuccessResponse> addSpecificationGroup(@Body RequestBody requestBody);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/admin/update_sp_name")
    Call<IsSuccessResponse> updateSpecificationGroupName(@Body RequestBody requestBody);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/store/update_specification_name")
    Call<IsSuccessResponse> updateSpecificationName(@Body RequestBody requestBody);


    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/store/delete_specification_group")
    Call<IsSuccessResponse> deleteSpecificationGroup(@Body RequestBody requestBody);


    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/store/delete_item_image")
    Call<IsSuccessResponse> deleteItemImage(@Body RequestBody requestBody);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/admin/get_document_list")
    Call<AllDocumentsResponse> getAllDocument(@Body RequestBody requestBody);

    @Multipart
    @POST("api/admin/upload_document")
    Call<DocumentResponse> uploadDocument(@Part MultipartBody.Part file, @PartMap() Map<String,
            RequestBody> partMap);


    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/store/update_item")
    Call<AddOrUpdateItemResponse> updateItem(@Body RequestBody jsonData);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/store/add_item")
    Call<AddOrUpdateItemResponse> addItem(@Body RequestBody jsonData);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/store/order_payment_status_set_on_cash_on_delivery")
    Call<IsSuccessResponse> setOrderPaymentPaidBy(@Body RequestBody requestBody);


    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/store/rating_to_provider")
    Call<IsSuccessResponse> setFeedbackProvider(@Body RequestBody requestBody);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/store/rating_to_user")
    Call<IsSuccessResponse> setFeedbackUser(@Body RequestBody requestBody);


    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/store/daily_earning")
    Call<DayEarningResponse> getDailyEarning(@Body RequestBody requestBody);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/store/weekly_earning")
    Call<DayEarningResponse> getWeeklyEarning(@Body RequestBody requestBody);


    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/store/cancel_request")
    Call<OrderStatusResponse> cancelDeliveryRequest(@Body RequestBody requestBody);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/admin/check_referral")
    Call<IsSuccessResponse> getCheckReferral(@Body RequestBody requestBody);

    @GET("api/geocode/json")
    Call<ResponseBody> getGoogleGeocode(@QueryMap Map<String, String> stringMap);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/store/update_store_time")
    Call<IsSuccessResponse> updateStoreTime(@Body RequestBody requestBody);

    @Multipart
    @POST("api/admin/add_bank_detail")
    Call<IsSuccessResponse> addBankDetail(@PartMap() Map<String, RequestBody> partMap,
                                          @Part MultipartBody.Part file,
                                          @Part MultipartBody.Part file2,
                                          @Part MultipartBody.Part file3);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/admin/update_bank_detail")
    Call<IsSuccessResponse> updateBankDetail(@Body RequestBody requestBody);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/admin/select_bank_detail")
    Call<IsSuccessResponse> selectBankDetail(@Body RequestBody requestBody);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/admin/delete_bank_detail")
    Call<BankDetailResponse> deleteBankDetail(@Body RequestBody requestBody);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/admin/get_bank_detail")
    Call<BankDetailResponse> getBankDetail(@Body RequestBody requestBody);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/admin/create_wallet_request")
    Call<IsSuccessResponse> createWithdrawalRequest(@Body RequestBody requestBody);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("admin/cancel_wallet_request")
    Call<IsSuccessResponse> cancelWithdrawalRequest(@Body RequestBody requestBody);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/admin/get_wallet_history")
    Call<WalletHistoryResponse> getWalletHistory(@Body RequestBody requestBody);


    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/admin/get_wallet_request_list")
    Call<WalletTransactionResponse> getWalletTransaction(@Body RequestBody requestBody);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/user/add_item_in_cart")
    Call<AddCartResponse> addItemInCart(@Body RequestBody requestBody);

    @GET("api/distancematrix/json?")
    Call<ResponseBody> getGoogleDistanceMatrix(@QueryMap Map<String, String> stringMap);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/user/get_order_cart_invoice")
    Call<InvoiceResponse> getDeliveryInvoice(@Body RequestBody requestBody);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/user/create_order")
    Call<IsSuccessResponse> createOrder(@Body RequestBody requestBody);


    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/user/user_get_store_review_list")
    Call<ReviewResponse> getStoreReview(@Body RequestBody requestBody);


    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/admin/get_image_setting")
    Call<ImageSettingsResponse> getImageSettings();

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/store/complete_order")
    Call<IsSuccessResponse> completeOrder(@Body RequestBody requestBody);


    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/store/check_promo_code")
    Call<IsSuccessResponse> checkPromoReuse(@Body RequestBody requestBody);


    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/store/promo_code_list")
    Call<PromoCodeResponse> getPromoCodes(@Body RequestBody requestBody);


    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/store/add_promo")
    Call<IsSuccessResponse> addPromoCodes(@Body RequestBody requestBody);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/store/update_promo_code")
    Call<IsSuccessResponse> updatePromoCodes(@Body RequestBody requestBody);


    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/store/update_order")
    Call<IsSuccessResponse> updateOrder(@Body RequestBody requestBody);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/user/pay_order_payment")
    Call<IsSuccessResponse> payOrderPayment(@Body RequestBody requestBody);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/get_item_detail")
    Call<ItemsResponse> getItems(@Body RequestBody requestBody);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/store/store_change_delivery_address")
    Call<IsSuccessResponse> changeDeliveryAddressStore(@Body RequestBody requestBody);


    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/store/create_order")
    Call<IsSuccessResponse> createOrderWithEmptyCart(@Body RequestBody requestBody);


    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/user/get_payment_gateway")
    Call<PaymentGatewayResponse> getPaymentGateway(@Body RequestBody requestBody);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/user/add_card")
    Call<CardsResponse> getAddCreditCard(@Body RequestBody requestBody);


    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/user/select_card")
    Call<CardsResponse> selectCreditCard(@Body RequestBody requestBody);


    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/user/delete_card")
    Call<IsSuccessResponse> deleteCreditCard(@Body RequestBody requestBody);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/user/get_card_list")
    Call<CardsResponse> getAllCreditCards(@Body RequestBody requestBody);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/user/add_wallet_amount")
    Call<WalletResponse> getAddWalletAmount(@Body RequestBody requestBody);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/store/get_vehicles_list")
    Call<VehiclesResponse> getVehiclesList(@Body RequestBody requestBody);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("/api/store/get_group_list_of_group")
    Call<ProductListResponse> getProductList(@Body RequestBody requestBody);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("/api/store/get_product_group_list")
    Call<ProductGroupsResponse> getProductGroupList(@Body RequestBody requestBody);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("/api/store/delete_product_group")
    Call<ProductGroupsResponse> deleteProductGroup(@Body RequestBody requestBody);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/user/get_stripe_add_card_intent")
    Call<CardsResponse> getStripSetupIntent(@Body RequestBody requestBody);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/user/get_stripe_payment_intent_wallet")
    Call<CardsResponse> getStripPaymentIntentWallet(@Body RequestBody requestBody);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/store/get_order_detail")
    Call<OrderDetailResponse> getOrderDetail(@Body RequestBody requestBody);


    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/store/find_nearest_provider_list")
    Call<NearestProviderResponse> getNearestProviders(@Body RequestBody requestBody);

    @Multipart
    @POST("api/store/sub_store_login")
    Call<StoreDataResponse> subStoreLogin(@PartMap Map<String, RequestBody> map);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/store/sub_store_list")
    Call<SubStoresResponse> getSubStores(@Body RequestBody requestBody);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/store/update_sub_store")
    Call<IsSuccessResponse> updateSubStore(@Body RequestBody requestBody);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/store/add_sub_store")
    Call<IsSuccessResponse> addSubStore(@Body RequestBody requestBody);

    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("api/v1/card-identifiers")
    Call<ResponseBody> get_sagpay_card_identifiers(@Body RequestBody requestBody);


    @GET
    Call<AutoCompleteResponse> getAutoCompleteAddresses(@Url String url, @QueryMap Map<String, String> stringMap);

    @GET
    Call<ResponseBody> getAddressDataFromId(@Url String url, @QueryMap Map<String, String> stringMap);

}

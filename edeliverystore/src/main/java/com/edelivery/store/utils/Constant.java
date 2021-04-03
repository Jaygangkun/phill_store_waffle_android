package com.edelivery.store.utils;

import com.edelivery.store.models.datamodel.ItemSpecification;

/**
 * A class of constants on 31-01-2017.
 */

public class Constant {
    public static final int API_DELAY = 1000;
    public static final int VEHICLE_TYPE=2;
    public static final int INVALID_TOKEN=999;
    public static final int STORE_DATA_NOT_FOUND=634;
    public static final int SMS_VERIFICATION_ON = 2;
    public static final int EMAIL_VERIFICATION_ON = 1;
    public static final int SMS_AND_EMAIL_VERIFICATION_ON = 3;
    public static final String IS_ORDER_UPDATE = "is_order_update";
    public static final String UPDATE_ITEM_INDEX = "update_item_index";
    public static final String UPDATE_ITEM_INDEX_SECTION = "update_item_index_section";
    public static final String PRODUCT_DETAIL = "product_detail";
    public static final int TYPE_SPECIFICATION_MULTIPLE = 2;
    public static final int TYPE_SPECIFICATION_SINGLE = 1;
    public static final String ERROR_CODE_PREFIX = "error_";
    public static final String MESSAGE_CODE_PREFIX = "message_";
    public static final String FRAGMENT_ADD_ITEM_SPECIFICATION = "FRAGMENT_ADD_ITEM_SPECIFICATION";
    public static final String FRAGMENT_TYPE = "FRAGMENT_TYPE";
    public static final String STATUS_TEXT_PREFIX = "text_status";
    public static final int DEFAULT_STATUS = 0;
    public static final int WAITING_FOR_ACCEPT = 1;
    public static final int STORE_ORDER_ACCEPTED = 3;
    public static final int STORE_ORDER_REJECTED = 103;
    public static final int STORE_ORDER_CANCELLED = 104;
    public static final int STORE_ORDER_PREPARING = 5;
    public static final int STORE_ORDER_READY = 7;
    public static final int WAITING_FOR_DELIVERY_MAN = 9;
    public static final int USER_CANCELED_ORDER = 101;
    public static final int DELIVERY_MAN_NOT_FOUND = 109;
    public static final int DELIVERY_MAN_ACCEPTED = 11;
    public static final int DELIVERY_MAN_REJECTED = 111;
    public static final int DELIVERY_MAN_CANCELLED = 112;
    public static final int DELIVERY_MAN_COMING = 13;
    public static final int DELIVERY_MAN_ARRIVED = 15;
    public static final int DELIVERY_MAN_PICKED_ORDER = 17;
    public static final int DELIVERY_MAN_STARTED_DELIVERY = 19;
    public static final int DELIVERY_MAN_ARRIVED_AT_DESTINATION = 21;
    public static final int DELIVERY_MAN_COMPLETE_DELIVERY = 23;
    public static final int FINAL_ORDER_COMPLETED = 25;
    public static final int STORE_CANCELLED_REQUEST = 105;
    public static final String PRODUCT_SPECIFICATION = "product_specification";
    public static final String TOOLBAR_TITLE = "toolbar_title";
    public static final String DEVICE_TOKEN = "device_token";
    public static final String GOOGLE_KEY = "google_key";
    public static final String SERVER_TOKEN = "server_token";
    public static final String IS_PHONE_NUMBER_VERIFIED = "is_phone_number_verified";
    public static final String IS_EMAIL_VERIFIED = "is_email_verified";
    public static final String DEVICE_TOKEN_RECEIVED = "deviceTokenReceived";
    public static final String ANDROID = "android";
    public static final String STORE_ID = "store_id";
    public static final String ID = "id";
    public static final String _ID = "_id";
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String STORE_SERVICE_ID = "store_delivery_id";
    public static final String PASS_WORD = "password";
    public static final String COUNTRY_CODE = "country_phone_code";
    public static final String PHONE = "phone";
    public static final String ADDRESS = "address";
    public static final String DEVICE_TYPE = "device_type";
    public static final String APP_VERSION = "app_version";
    public static final String IMAGE_URL = "image_url";
    public static final String SLOGAN = "slogan";
    public static final String WEBSITE_URL = "website_url";
    public static final String COUNTRY_ID = "country_id";
    public static final String CITY_ID = "city_id";
    public static final String PRODUCT_ITEM = "product_item";
    public static final String STATUS = "status";
    public static final String RESULTS = "results";
    public static final String GEOMETRY = "geometry";
    public static final String LOCATION = "location";
    public static final String LAT = "lat";
    public static final String LNG = "lng";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String DETAILS = "details";
    public static final String IS_VISIBLE_IN_STORE = "is_visible_in_store";
    public static final String SEQUENCE_NUMBER = "sequence_number";
    public static final String PRODUCT_ID = "product_id";
    public static final String PRODUCT_GROUP_ID = "product_group_id";
    public static final String PRODUCT_IDS = "product_ids";
    public static final String PROVIDER_ID = "provider_id";
    public static final String CURRENT_PASS_WORD = "old_password";
    public static final String NEW_PASS_WORD = "new_password";
    public static final String ITEM = "item";
    public static final String SPECIFICATIONS = "specifications";
    public static final String ITEM_ID = "item_id";
    public static final String IS_ITEM_IN_STOCK = "is_item_in_stock";
    public static final String STORE_TIME = "store_time";
    public static final String ORDER_DETAIL = "order_detail";
    public static final String ORDER_ID = "order_id";
    public static final String ORDER_STATUS = "order_status";
    public static final String CANCEL_REASON = "cancel_reason";
    public static final String START_DATE = "start_date";
    public static final String END_DATE = "end_date";
    public static final String DATE_TIME_FORMAT_WEB = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String TIME_FORMAT_AM = "h:mm a";
//    public static final String DATE_FORMAT = "yyyy-MM-dd";
//    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm";
//    public static final String DATE_TIME_FORMAT_AM = "yyyy-MM-dd h:mm a";
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm";
    public static final String DATE_TIME_FORMAT_AM = "dd/MM/yyyy h:mm a";
    public static final String DAY = "d";
    public static final String DATE_FORMAT_2 = "MM-dd-yyyy";
    public static final String DATE_FORMAT_MONTH = "MMM yyyy";
    public static final String WEEK_DAY = "EEE, dd MMMM";
    public static final String TIME_FORMAT = "HH:mm:ss";
    public static final String TIME_FORMAT_2 = "HH:mm";
    public static final String DATE_FORMAT_3 = "dd MMM yy";
    public static final String TYPE = "type";
    public static final int TYPE_STORE = 2;
    public static final String DOCUMENT_ID = "document_id";
    public static final String EXPIRED_DATE = "expired_date";
    public static final String UNIQUE_CODE = "unique_code";
    public static final String DOCUMENT_ACTIVITY = "DOCUMENT_ACTIVITY";
    public static final String ORDER_PAYMENT_ID = "order_payment_id";
    public static final String IS_ORDER_PRICE_PAID_BY_STORE = "is_order_price_paid_by_store";
    public static final String HTTP_ERROR_CODE_PREFIX = "http_error_";
    public static final String STORE_RATING_TO_USER = "store_rating_to_user";
    public static final String STORE_REVIEW_TO_USER = "store_review_to_user";
    public static final String STORE_RATING_TO_PROVIDER = "store_rating_to_provider";
    public static final String STORE_REVIEW_TO_PROVIDER = "store_review_to_provider";
    public static final String REFERRAL_CODE = "referral_code";
    public static final String USER_DETAIL = "USER_DETAIL";
    public static final String PROVIDER_DETAIL = "PROVIDER_DETAIL";
    public static final String LOGIN_BY = "login_by";
    public static final String MANUAL = "manual";
    public static final String SOCIAL = "social";
    public static final String SOCIAL_ID = "social_id";
    public static final String BUNDLE = "BUNDLE";
    public static final String WALLET_STATUS = "wallet_status";
    public static final String CITY_CODE = "CITY_CODE";
    public static final String TOTAL_ITEM_COUNT = "total_item_count";
    public static final String TOTAL_DISTANCE = "total_distance";
    public static final String TOTAL_TIME = "total_time";
    public static final String TOTAL_CART_PRICE = "total_cart_price";
    public static final String IS_PAYMENT_MODE_CASH = "is_payment_mode_cash";
    public static final String TOTAL_SPECIFICATION_COUNT = "total_specification_count";
    public static final String WALLET = "wallet";
    public static final String TOTAL_ITEM_PRICE = "total_item_price";
    public static final String TOTAL_SPECIFICATION_PRICE = "total_specification_price";
    public static final String CART_UNIQUE_TOKEN = "cart_unique_token";
    public static final String USER_ID = "user_id";
    public static final String CART_ID = "cart_id";

    public static final String IS_USER_PICK_UP_ORDER = "is_user_pick_up_order";
    public static final String PROMO_CODE_NAME = "promo_code_name";
    public static final String PROMO_DETAIL = "promo_detail";


    public static final String ESTIMATED_TIME_FOR_READY_ORDER = "estimated_time_for_ready_order";
    public static final String ORDER_TYPE = "order_type";
    public static final String ITEM_ARRAY = "item_array";
    public static final String INVOICE = "invoice";
    public static final String PAYMENT_TOKEN = "payment_token";
    public static final String CARD_ID = "card_id";
    public static final String PAYMENT_ID = "payment_id";
    public static final String LAST_FOUR = "last_four";
    public static final String CARD_EXPIRY_DATE = "card_expiry_date";
    public static final String CARD_HOLDER_NAME = "card_holder_name";
    public static final String CARD_TYPE = "card_type";
    public static final String REQUEST_ID = "request_id";
    public static final String VEHICLE_ID="vehicle_id";
    public static final String LANG = "lang";
    public static final String PRODUCT_GROUP = "product_group";
    public static final String PAYMENT_METHOD = "payment_method";
    public static final String AMOUNT = "amount";
    public static final String PAYMENT_INTENT_ID = "payment_intent_id";

    public static final String TITLE = "title";
    public static final int STORE_CHAT_ID = 4;
    public static final String ADMIN_RECIVER_ID = "000000000000000000000000";
    public static final String RECEIVER_ID = "receiver_id";
    public static final String SUB_STORE = "sub_store";
    public static final String URLS = "urls";
    public static final String IS_APPROVED = "is_approved";
    public static final String BANK_ACCOUNT_NUMBER = "account_number";
    public static final String ACCOUNT_HOLDER_NAME = "account_holder_name";
    public static final String BANK_PERSONAL_ID_NUMBER = "personal_id_number";
    public static final String DOB = "dob";
    public static final String BANK_ROUTING_NUMBER = "routing_number";
    public static final String BANK_ACCOUNT_HOLDER_TYPE = "account_holder_type";
    public static final String BANK_ACCOUNT_HOLDER_NAME = "bank_account_holder_name";
    public static final String BANK_HOLDER_TYPE = "bank_holder_type";
    public static final String POSTAL_CODE = "postal_code";
    public static final String STATE = "state";
    public static final String GENDER = "gender";
    public static final String BUSINESS_NAME = "business_name";
    public static final String BANK_HOLDER_ID ="bank_holder_id";


    /***
     * Permision Request Code
     */
    public static final int REQUEST_STORE_TIME = 33;
    public static final int REQUEST_CHECK_SETTINGS = 32;
    public static final int PERMISSION_CHOOSE_PHOTO = 1;
    public static final int PERMISSION_TAKE_PHOTO = 2;
    public static final int REQUEST_UPDATE_BANK_DETAIL = 34;
    public static final int REQUEST_PROMO_CODE = 35;
    public static final int DELIVERY_LIST_CODE = 2;
    public static final int FAMOUS_TAG_LIST = 43;
    public static final int REQUEST_USER_RATING = 44;
    public static final int REQUEST_PROVIDER_RATING = 45;
    /***
     * Broadcast Status
     */
    public static final int PERMISSION_FOR_LOCATION = 3;
    public static final int PERMISSION_FOR_CALL = 4;
    public static final int LOGIN_ACTIVITY = 4;
    public static final int HOME_ACTIVITY = 5;
    public static final int GOOGLE_SIGN_IN = 21;
    public static final int STORE_LOCATION_RESULT = 41;
    public static final int ORDER_SCHEDULED = 30;/// in seconds
    public static final int DELIVERY_SCHEDULED = 10;/// in seconds

    /***
     * Google url
     */
    public static final String GOOGLE_API_URL = "https://maps.googleapis.com/maps/";
    public static final String GET_ADDRESS_API_URL = "https://api.getAddress.io/";
    public static ItemSpecification itemSpeci;
    public static int updateSpeciPosition = -1;
    public static String PUSH_NOTIFICATION_PREFIX = "push_";
    public static String STRING = "string";
    public static String PUSH_DATA = "push_data";
    public static String PRODUCT_TIME="product_time";

    /**
     * For Promo
     */


    public class Promo {
        public static final int PROMO_FOR_STORE = 2;
        public static final int PROMO_FOR_PRODUCT = 21;
        public static final int PROMO_FOR_ITEM = 22;
    }

    public class Action {
        public static final String NETWORK_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
        public static final String ACTION_NEW_ORDER_ACTION = "edelivery.store.NEW_ORDER";
        public static final String ACTION_STORE_APPROVED = "edelivery.store" +
                ".ACTION_ADMIN_DECLINE";
        public static final String ACTION_STORE_DECLINED = "edelivery.store" +
                ".ACTION_ADMIN_APPROVED";
        public static final String ACTION_ORDER_STATUS_ACTION = "edelivery.store.ORDER_STATUS";
    }

    public class Tag {

        public static final String HISTORYACTIVITY = "historyActivity";
        public static final String HISTORYDETAILS_ACTIVITY = "history_details_activity";
        public static final String ORDER_LIST_FRAGMENT = "ORDER_LIST_FRAGMENT";
        public static final String ITEM_LIST_FRAGMENT = "ITEM_LIST_FRAGMENT";
        public static final String DELIVERIES_LIST_FRAGMENT = "DELIVERIES_LIST_FRAGMENT";
        public static final String PROFILE_FRAGMENT = "PROFILE_FRAGMENT";

    }

    public class Facebook {
        public static final String EMAIL = "email";
        public static final String PUBLIC_PROFILE = "public_profile";
    }

    /**
     * Google params
     */


    public class google {
        public static final String API_KEY = "api-key";
        public static final String OK = "OK";
        public static final String STATUS = "status";
        public static final String RESULTS = "results";
        public static final String GEOMETRY = "geometry";
        public static final String LOCATION = "location";
        public static final String ADDRESS_COMPONENTS = "address_components";
        public static final String LONG_NAME = "long_name";
        public static final String ADMINISTRATIVE_AREA_LEVEL_2 = "administrative_area_level_2";
        public static final String ADMINISTRATIVE_AREA_LEVEL_1 = "administrative_area_level_1";

        public static final String COUNTRY = "country";
        public static final String COUNTRY_CODE = "country_code";
        public static final String SHORT_NAME = "short_name";
        public static final String TYPES = "types";
        public static final String LOCALITY = "locality";
        public static final String LAT = "lat";
        public static final String LNG = "lng";
        public static final String DESTINATION_ADDRESSES = "destination_addresses";
        public static final String ORIGIN_ADDRESSES = "origin_addresses";
        public static final String ROWS = "rows";
        public static final String ELEMENTS = "elements";
        public static final String DISTANCE = "distance";
        public static final String VALUE = "value";
        public static final String DURATION = "duration";
        public static final String ORIGINS = "origins";
        public static final String DESTINATIONS = "destinations";
        public static final String KEY = "key";
        public static final String LAT_LNG = "latlng";
        public static final String ADDRESS = "address";
        public static final String FORMATTED_ADDRESS = "formatted_address";
        public static final String TOWN_OR_CITY = "town_or_city";
        public static final String COUNTY = "county";
        public static final String DISTRICT = "district";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
    }

    public class Wallet {
        public static final int ADDED_BY_ADMIN = 1;
        public static final int ADDED_BY_CARD = 2;
        public static final int ADDED_BY_REFERRAL = 3;
        public static final int ORDER_CHARGED = 4;
        public static final int ORDER_REFUND = 5;
        public static final int ORDER_PROFIT = 6;
        public static final int ORDER_CANCELLATION_CHARGE = 7;
        public static final int WALLET_REQUEST_CHARGE = 8;
        public static final int SET_WEEKLY_PAYMENT_BY_ADMIN = 9;

        public static final int WALLET_STATUS_CREATED = 1;
        public static final int WALLET_STATUS_ACCEPTED = 2;
        public static final int WALLET_STATUS_TRANSFERRED = 3;
        public static final int WALLET_STATUS_COMPLETED = 4;
        public static final int WALLET_STATUS_CANCELLED = 5;


        public static final int ADD_WALLET_AMOUNT = 1;
        public static final int REMOVE_WALLET_AMOUNT = 2;
    }

    public class Payment {
        public static final String CASH = "0";
        public static final String STRIPE = "586f7db95847c8704f537bd55";
        public static final String SAGPAY = "586f7db95847c8704f537bd5";
        public static final String PAY_PAL = "586f7db95847c8704f537bd6";
        public static final String PAY_U_MONEY = "586f7db95847c8704f537bd";
    }

    public class Type {
        public static final int STORE = 2;
        public static final int USER = 7;
        public static final String DESTINATION = "destination";
        public static final String PICKUP = "pickup";
        public static final int NO_RECURSION = 0;
        public static final int DAILY_RECURSION = 1;
        public static final int WEEKLY_RECURSION = 2;
        public static final int MONTHLY_RECURSION = 3;
        public static final int ANNUALLY_RECURSION = 4;
        public static final int PERCENTAGE = 1;
        public static final int ABSOLUTE = 2;

    }

    public class Bank {
        public static final String BANK_ACCOUNT_HOLDER_TYPE = "individual";

    }
    public class DeliveryType {
        public static final int STORE = 1;
        public static final int COURIER = 2;
    }

    public class Chat_Type
    {
        //admin = 1
        //user = 2
        //provider = 3
        //store = 4
        public static final int ADMIN_AND_USER = 12;
        public static final int ADMIN_AND_PROVIDER = 13;
        public static final int ADMIN_AND_STORE = 14;
        public static final int USER_AND_PROVIDER = 23;
        public static final int USER_AND_STORE = 24;
        public static final int PROVIDER_AND_STORE = 34;
    }

    public static class SagpayParam{

        public static final String CARDIDENTIFIER = "cardIdentifier";
        public static final String IS_SAVE = "is_save";
        public static final String DESCRIPTION = "description";
        public static final String MERCHANTSESSIONKEY = "merchantSessionKey";
        public static final String VENDORNAME = "vendorName";
        public static final String CARDHOLDERNAME = "cardholderName";
        public static final String CARDNUMBER = "cardNumber";
        public static final String EXPIRYDATE = "expiryDate";
        public static final String SECURITYCODE = "securityCode";
        public static final String CARDDETAILS = "cardDetails";
        public static final String EXPIRY = "expiry";
        public static final String ERRORS = "errors";
        public static final String CLIENTMESSAGE = "clientMessage";
    }

}

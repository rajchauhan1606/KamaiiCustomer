package com.kamaii.customer.interfacess;


import android.os.Build;
import android.text.Html;
import android.widget.TextView;

public interface Consts {

    String APP_NAME = "Oky Book";
    //new 677440

    String merchantIdSandbox = "595846726305054";
    String accessTokenSandbox = "2A9F6759634732CBEEA397F3F57C1931";


    String merchantIdLIVE = "230782735516696";
    // String accessTokenLIVE="6A9D7ED77A3F5A3BEBB02685BEB3CF21";
    String accessTokenLIVE = "D48728D97B3D7427F7B4A4F0057DCB7C";
//    String accessTokenLIVE="65EA23D7B8CD12D64F4D3CB11BB347AF";

    String IMAGE_URL = "https://kamaii.in/app/";

 //   String BASE_URL = "https://kamaii.in/app/Newwebservice/";
    String BASE_URL = "https://kamaii.in/app/Webservice/";
  //  String BASE_URL = "https://multiscopesolution.com/app/Webservice/";
    String PAYMENT_FAIL = "https://kamaii.in/app/Stripe/Payment/fail";
    String PAYMENT_SUCCESS = "https://kamaii.in/app/Stripe/Payment/success";
    String PAYMENT_SUCCESS_PAYU = "https://kamaii.in/app/Stripe/Payment/success";
    String MAKE_PAYMENT = "https://kamaii.in/app/Stripe/Payment/make_payment/";

    String PAYMENT_FAIL_Paypal = "https://kamaii.in/app/Webservice/payufailure";
    String PAYMENT_SUCCESS_paypal = "https://kamaii.in/app/Webservice/payusuccess";
    String MAKE_PAYMENT_paypal = "https://kamaii.in/app/Webservice/paypalWallent?";

    String INVOICE_PAYMENT_FAIL_Stripe = "https://kamaii.in/app/Stripe/BookingPayement/fail";
    String INVOICE_PAYMENT_SUCCESS_Stripe = "https://kamaii.in/app/Stripe/BookingPayement/success";
    String INVOICE_PAYMENT_Stripe = "https://kamaii.in/app/Stripe/BookingPayement/make_payment/";

    String INVOICE__PAYMENT_paypal = "https://kamaii.in/app/Webservice/paypal?";
    String PRIVACY_URL = "https://kamaii.in/app/Myadmin/privacy";
    String TERMS_URL = "https://kamaii.in/app/Myadmin/term";
    String About_URL = "https://kamaii.in/app/Myadmin/about";
    String COVID_URL = "https://kamaii.in/app/Myadmin/covid19";


    /*Api Details*/
    String WALLET_REQUEST_API = "customerWalletRequest";
    String CONFIRM_TIME_UPDATION_API = "confirm_updation_order";
    String GET_ALL_ARTISTS_API = "getAllArtists";
    String GET_ARTIST_BY_ID_API_ARTIST = "getArtistByid_apiArtist";
    String GET_ARTIST_BY_ID_API_THIRD = "getArtistByid_api_third";
    String GET_NOTIFICATION_API = "getNotifications";
    String GET_INVOICE_API = "getcustomerInvoice";
    String GET_INVOICE_API2 = "getcustomerInvoice2";
    String GET_REFERRAL_CODE_API = "getMyReferralCode";
    String GET_CHAT_HISTORY_API = "getChatHistoryForUser";
    String GET_CHAT_API = "getChat";
    String SEND_CHAT_API = "sendmsg";
    String LOGIN_API = "signIn2";
    String REGISTER_API = "SignUp";
    String VERIFY_API = "verify_otp";
    String UPDATE_PROFILE_API = "editPersonalInfo";
    String Add_REFER = "add_referral";
    String MY_EARNING1_USER = "my_earning_user";
    String VIEW_REFER = "view_referral";
    String VIEW_REFER_SERVICE = "get_referra_det";
    String GET_WHATS_APP_CHAT_API = "get_chat_api";
    String GET_DELIVERY_CONFIRM_API = "get_delivery_confirm";
    //dummy
    String BOOK_ARTIST_API2 = "book_test_artist2";
  //  String BOOK_ARTIST_API2 = "book_test_artist_ionic";
    String BOOK_ARTIST_API2_TEMP = "mybook_test_artist2";
    String BOOK_ARTIST_API2_new = "book_test_artist2new";
    String ADD_TO_CART_TEST_API = "get_shipping_data4";
    String CHECK_MINIMUM_PRICE = "check_min_price";
    String ADD_TO_CART_TEST_API_LIVE = "productaddtocart";
    String DIALOG_ADD_TO_CART_TEST_API_LIVE = "productaddcartqty";
    String REMOVE_FROM_CART_TEST_API_LIVE = "productremovefromcart";
    String BOOK_APPOINTMENT_API = "book_appointment";
    String DECLINE_BOOKING_API = "decline_booking";
    String UPDATE_LOCATION_API = "updateLocation";
    String MAKE_PAYMENT_API = "makePayment";
    String CHECK_COUPON_API = "checkCoupon";
    String GET_MY_TICKET_API = "getMyTicket";
    String GENERATE_TICKET_API = "generateTicket";
    String GET_TICKET_COMMENTS_API = "getTicketComments";
    String ADD_TICKET_COMMENTS_API = "addTicketComments";
    String FORGET_PASSWORD_API = "forgotPassword";
    String GET_APPOINTMENT_API = "getAppointment";
    String EDIT_APPOINTMENT_API = "edit_appointment";
    String APPOINTMENT_OPERATION_API = "appointment_operation";
    String GET_ALL_CATEGORY_API = "getAllCaegory";
    String GET_WELCOME_SLIDER_API = "get_welcome_slider";
    String GET_VERSION = "get_version2";
    String GET_SLIDER = "getSlider";
    String GET_footerSLIDER = "getfotterSlider";
    String GET_CASHBACK = "cashback_and_offer_page";
    String GET_ALL_JOB_USER_API = "get_all_job_user";
    String POST_JOB_API = "post_job_new";
    String GET_APPLIED_JOB_BY_ID_API = "get_applied_job_by_id";
    String JOB_STATUS_USER_API = "job_status_user";
    String EDIT_POST_JOB_API = "edit_post_job";
    String DELETE_JOB_API = "deletejob";
    String ADD_FAVORITES_API = "add_favorites";
    String REMOVE_FAVORITES_API = "remove_favorites";
    String GET_LOCATION_ARTIST_API = "getLocationArtist";
    String GET_RIDER_LOCATION_API = "get_rider_location";
    String ADD_RATING_API = "addRating";
    String BOOKING_OPERATION_API = "booking_operation";
    String JOB_COMPLETE_API = "jobComplete";
    String DELETE_PROFILE_IMAGE_API = "deleteProfileImage";
    String ADD_MONEY_API = "addMoney";
    String GET_WALLET_HISTORY_API = "getWalletHistory";
    String GET_WALLET_API = "getWallet";
    String DELETE_SERVICE = "delete_service";
    String SUB_CAT_ID = "sub_cat_id";
    String GET_SERVICE_SHIPPING2 = "get_service_shipping2";

    /*app data*/
    String CAMERA_ACCEPTED = "camera_accepted";
    String STORAGE_ACCEPTED = "storage_accepted";
    String MODIFY_AUDIO_ACCEPTED = "modify_audio_accepted";
    String FINE_LOC = "fine_loc";
    String CORAS_LOC = "coras_loc";
    String READ_STORAGE = "readstorage";
    String WACK_LOCK = "wack_lock";
    String CALL_PHONE = "call_phone";
    String PAYMENT_URL = "payment_url";
    String MONEY = "money";
    String SURL = "surl";
    String FURL = "furl";
    String SCREEN_TAG = "screen_tag";
    String SERVICE_ARRAY = "service_array";
    String DTO = "dto";
    String POSTION = "postion";
    /*app data*/

    /*Project Parameter*/
    String ARTIST_ID = "artist_id";
    String USER_DTO = "user_dto";
    String POST_JOB_DTO = "post_job_dto";
    String IS_REGISTERED = "is_registered";
    String IMAGE_URI_CAMERA = "image_uri_camera";
    String DATE_FORMATE_SERVER = "EEE, MMM dd, yyyy hh:mm a"; //Wed, JUL 06, 2018 04:30 pm
    String DATE_FORMATE_TIMEZONE = "z";
    String HISTORY_DTO = "history_dto";
    String BROADCAST = "broadcast";
    String ARTIST_DTO = "artist_dto";
    String FLAG = "flag";


    /*Parameter Get Artist and Search*/
    String USER_ID = "user_id";
    String AMOUNT = "amount";
    String LATITUDE = "latitude";
    String LONGITUDE = "longitude";
    String CATEGORY_ID = "category_id";
    String CATEGORY_NAME = "category_name";
    String CATEGORY_IMAGE = "category_image";
    String SUB_CATEGORY_ID = "sub_category_id";
    String Sub_CAT_NAME = "sub_category_name";
    String DESTI_ADDRESS = "destinationaddress";
    String DESTI_ADDRESS1 = "destinationaddress1";
    String DESTI_TIME = "distance_time";
    String TOTAL_AMOUNT = "total_amount";
    String PAY_STATUS = "pay_status";
    String PTYPE = "pay_type";
    String KM = "km";
    String INVOICEID = "invoice_id";
    String ROUTE_PATH = "route_path";
    /*Get All History*/
    String ROLE = "role";
    String THIRACATEGORY = "thirdcategory";
    String WORK_ADDRESS_LATI = "waddresslati";
    String WORK_ADDRESS_LONGI = "waddresslongi";
    String HOME_ADDRESS_LATI = "haddresslati";
    String HOME_ADDRESS_LONGI = "haddresslongi";
    /*Send Message*/
    String MESSAGE = "message";
    String SEND_BY = "send_by";
    String SENDER_NAME = "sender_name";


    /*Login Parameter*/
    String NAME = "name";
    String EMAIL_ID = "email_id";
    String PASSWORD = "password";
    String DEVICE_TYPE = "device_type";
    String DEVICE_TOKEN = "device_token";
    String DEVICE_ID = "device_id";
    String REFERRAL_CODE = "referral_code";


    /*Update Profile*/
    String NEW_PASSWORD = "new_password";
    String GENDER = "gender";
    String OTP = "OTP";
    String MOBILE = "mobile";
    String OFFICE_ADDRESS = "office_address";
    String ADDRESS = "address";
    String ADDRESS_ID = "address_id";
    String FULL_ADDRESS = "fulladdress";
    String IMAGE = "image";
    String CITY = "city";
    String COUNTRY = "country";
    String LOCATION_STATUS = "location_status";
    String LOCATION_STATUS1 = "location_status1";

    /*Book Artist*/

    String DATE_STRING = "date_string";
    String TIMEZONE = "timezone";
    String TIMEZONE1 = "timezone1";
    String PRICE = "price";
    String CARTDATA = "cartdata";
    String SERVICE_ID = "service_id";
    String CHANGE_PRICE = "change_price";
    String CHANGE_PRICE1 = "change_price1";
    /*Decline*/
    String BOOKING_ID = "booking_id";
    String DECLINE_BY = "decline_by";
    String DECLINE_REASON = "decline_reason";
    String QUANTITYDAYS = "quantitydays";
    String MAXMIUMVALUE = "maxmiumvalue";

    /*Make Payment*/
    String INVOICE_ID = "invoice_id";
    String COUPON_CODE = "coupon_code";
    String FINAL_AMOUNT = "final_amount";
    String PAYMENT_STATUS = "payment_status";

    String SOURCE_LAT = "source_lat";
    String SOURCE_LONG = "source_long";
    String DEST_LAT = "dest_lat";
    String DEST_LONG = "dest_long";

    /* tracking screen*/


    String TRACK_BOOKING_VENDOR_LATITUDE = "vendor_latitude";
    String TRACK_BOOKING_VENDOR_LONGITUDE = "vendor_longitude";

    String TRACK_SOURCE_CUSTOMER_LATITUDE = "source_customer_latitude";
    String TRACK_SOURCE_CUSTOMER_LONGITUDE = "source_customer_longitude";

    String TRACK_DESTINATION_CUSTOMER_LATITUDE = "destination_customer_latitude";
    String TRACK_DESTINATION_CUSTOMER_LONGITUDE = "destination_customer_longitude";

    String TRACK_BOOKING_SOURCE_ADDRESS = "s_address";
    String TRACK_BOOKING_DISCOUNT_TXT = "discount_txt";
    String TRACK_BOOKING_DISCOUNT_DIGIT = "discount_digit_txt";
    String TRACK_BOOKING_DESTINATION_ADDRESS = "d_address";
    String TRACK_BOOKING_VENDOR_NAME = "vendor_name";
    String TRACK_BOOKING_RIDER_LATITUDE = "rider_lat";
    String TRACK_BOOKING_RIDER_ORDER_TRACKER = "rider_order";
    String TRACK_BOOKING_RIDER_LONGITUDE = "rider_long";
    String TRACK_BOOKING_PRODUCT_NAME = "product_name";
    String TRACK_BOOKING_VENDOR_IMAGE = "vendor_image";
    String TRACK_BOOKING_MOBILE_NUMBER = "mobile_no";
    String TRACK_BOOKING_SCREEN_FLAG = "screen_flag";
    String TRACK_BOOKING_BOOKING_FLAG = "booking_flag";
    String TRACK_BOOKING_ID = "booking_id";
    String TRACK_BOOKING_TOTAL_AMOUNT = "total_amount";
    String TRACK_BOOKING_PAYMENT_TYPE = "pay_type";
    String TRACK_BOOKING_DATE = "booking_date";
    String TRACK_BOOKING_TIME = "booking_time";
    String TRACK_VEHICLE_NUMBER = "vehicle_no";
    String TRACK_SUB_ID = "sub_id";
    String TRACK_CAT_ID = "cat_id";
    String TRACK_SUB_LEVEL_ID = "sub_level_id";
    String TRACK_ARTIST_ID = "artist_id";
    String BOOKING = "booking";
    String ADD_ADDRESS = "add_user_address";
    String DELETE_ADDRESS = "delete_user_address";
    String GET_ADDRESS = "get_user_address";
    String GET_BOOKING_SECONDS_API = "getBookingSecond";
    String GET_DELIVERY_TIME_API = "getDeliveryTime";
    String REPEAT_ORDER_API = "repeatOrder";
   // String ORDER_TIMEBREAK_API = "getordersec";
    String ORDER_TIMEBREAK_API = "getordersec2";

    /*Chat intent*/
    String ARTIST_NAME = "artist_name";
    String ARTIST_IMAGE = "artist_image";

    /*Add Ticket*/
    String REASON = "reason";


    /*Get Ticket*/
    String TICKET_ID = "ticket_id";
    String COMMENT = "comment";


    /*Edit Appointment*/
    String APPOINTMENT_ID = "appointment_id";

    /*Decline Appointment*/
    String REQUEST = "request";


    /*Post Job*/
    String AVTAR = "avtar";
    String TITLE = "title";
    String DESCRIPTION = "description";
    String LATI = "lati";
    String LONGI = "longi";
    String JOB_DATE = "job_date";

    /*Get Applied Job*/
    String JOB_ID = "job_id";

    /*Job Status*/
    String AJ_ID = "aj_id";
    String STATUS = "status";

    // Google Console APIs developer key
    // Replace this key with your's
    static final String DEVELOPER_KEY = "AIzaSyBlLIsCaCw8ylCTPR0XhaKp-vkeD4S-5_0";


    double DISCOUNT_RATE = 10;
    /*Payment*/
    String PAYMENT_TYPE = "payment_type";
    String DISCOUNT_AMOUNT = "discount_amount";
    /*Chat*/
    String CHAT_TYPE = "chat_type";

    /*Paypal Client Id*/
    /*Add Review*/
    String RATING = "rating";


    String CURRENCY = "currency";

    /*Notifications Codes*/
    String DECLINE_BOOKING_ARTIST_NOTIFICATION = "10002";//both
    String START_BOOKING_ARTIST_NOTIFICATION = "10003";
    String RIDER_HAS_PICKUP_ORDER = "10050";//ar

    String END_BOOKING_ARTIST_NOTIFICATION = "10004";//user
    String ACCEPT_BOOKING_ARTIST_NOTIFICATION = "10006";//user
    String CHAT_NOTIFICATION = "10007";//both
    String TICKET_COMMENT_NOTIFICATION = "10009";//both
    String WALLET_NOTIFICATION = "10010";//both
    String JOB_APPLY_NOTIFICATION = "10012";//user
    String BRODCAST_NOTIFICATION = "10014";//both
    String TICKET_STATUS_NOTIFICATION = "10015";//both
    String ADMIN_NOTIFICATION = "10016";
    String SWIFT_REPEAT_NOTIFICATION = "10017";
    String ARRIVAL_NOTIFICATION = "10018";
    String TIME_CONFIRM_DIALOG_NOTIFICATION = "10080";
    String TYPE = "type";

}


package com.kamaii.customer.ui.activity;

import android.Manifest;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;
import android.text.Html;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.maps.android.SphericalUtil;
import com.kamaii.customer.DTO.ProductDTO;
import com.kamaii.customer.DTO.TaxiRouteData;
import com.kamaii.customer.DTO.TrackingData;
import com.kamaii.customer.DTO.UserBooking;
import com.kamaii.customer.DTO.UserDTO;
import com.kamaii.customer.R;
import com.kamaii.customer.api.apiClient;
import com.kamaii.customer.api.apiRest;
import com.kamaii.customer.dialogs.TrackingDetailDialog;
import com.kamaii.customer.https.HttpsRequest;
import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.interfacess.Helper;
import com.kamaii.customer.preferences.SharedPrefrence;
import com.kamaii.customer.service.MyService;
import com.kamaii.customer.ui.adapter.AdapterCartBooking;
import com.kamaii.customer.ui.adapter.AdapterProductBooking;
import com.kamaii.customer.ui.models.InfoWindowData;
import com.kamaii.customer.utils.AnimationUtils;
import com.kamaii.customer.utils.CustomEditText;
import com.kamaii.customer.utils.CustomInfoWindowGoogleMap;
import com.kamaii.customer.utils.CustomTextView;
import com.kamaii.customer.utils.CustomTextViewBold;
import com.kamaii.customer.utils.DrawRouteUtils;
import com.kamaii.customer.utils.GlobalUtils;
import com.kamaii.customer.utils.ItemDecorationAlbumColumns;
import com.kamaii.customer.utils.MapUtils;
import com.kamaii.customer.utils.ProjectUtils;
import com.tsuryo.androidcountdown.Counter;
import com.tsuryo.androidcountdown.TimeUnits;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.fabric.sdk.android.services.network.HttpRequest;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.google.android.gms.maps.model.JointType.ROUND;
import static com.kamaii.customer.interfacess.Consts.GET_BOOKING_SECONDS_API;
import static com.kamaii.customer.interfacess.Consts.GET_LOCATION_ARTIST_API;
import static com.kamaii.customer.interfacess.Consts.GET_RIDER_LOCATION_API;
import static com.kamaii.customer.interfacess.Consts.ROUTE_PATH;
import static com.kamaii.customer.interfacess.Consts.TRACK_ARTIST_ID;
import static com.kamaii.customer.interfacess.Consts.TRACK_BOOKING_BOOKING_FLAG;
import static com.kamaii.customer.interfacess.Consts.TRACK_BOOKING_DATE;
import static com.kamaii.customer.interfacess.Consts.TRACK_BOOKING_DESTINATION_ADDRESS;
import static com.kamaii.customer.interfacess.Consts.TRACK_BOOKING_DISCOUNT_DIGIT;
import static com.kamaii.customer.interfacess.Consts.TRACK_BOOKING_DISCOUNT_TXT;
import static com.kamaii.customer.interfacess.Consts.TRACK_BOOKING_ID;
import static com.kamaii.customer.interfacess.Consts.TRACK_BOOKING_MOBILE_NUMBER;
import static com.kamaii.customer.interfacess.Consts.TRACK_BOOKING_PAYMENT_TYPE;
import static com.kamaii.customer.interfacess.Consts.TRACK_BOOKING_PRODUCT_NAME;
import static com.kamaii.customer.interfacess.Consts.TRACK_BOOKING_RIDER_LATITUDE;
import static com.kamaii.customer.interfacess.Consts.TRACK_BOOKING_RIDER_LONGITUDE;
import static com.kamaii.customer.interfacess.Consts.TRACK_BOOKING_RIDER_ORDER_TRACKER;
import static com.kamaii.customer.interfacess.Consts.TRACK_BOOKING_SCREEN_FLAG;
import static com.kamaii.customer.interfacess.Consts.TRACK_BOOKING_SOURCE_ADDRESS;
import static com.kamaii.customer.interfacess.Consts.TRACK_BOOKING_TIME;
import static com.kamaii.customer.interfacess.Consts.TRACK_BOOKING_TOTAL_AMOUNT;
import static com.kamaii.customer.interfacess.Consts.TRACK_BOOKING_VENDOR_IMAGE;
import static com.kamaii.customer.interfacess.Consts.TRACK_BOOKING_VENDOR_LATITUDE;
import static com.kamaii.customer.interfacess.Consts.TRACK_BOOKING_VENDOR_LONGITUDE;
import static com.kamaii.customer.interfacess.Consts.TRACK_BOOKING_VENDOR_NAME;
import static com.kamaii.customer.interfacess.Consts.TRACK_CAT_ID;
import static com.kamaii.customer.interfacess.Consts.TRACK_DESTINATION_CUSTOMER_LATITUDE;
import static com.kamaii.customer.interfacess.Consts.TRACK_DESTINATION_CUSTOMER_LONGITUDE;
import static com.kamaii.customer.interfacess.Consts.TRACK_SOURCE_CUSTOMER_LATITUDE;
import static com.kamaii.customer.interfacess.Consts.TRACK_SOURCE_CUSTOMER_LONGITUDE;
import static com.kamaii.customer.interfacess.Consts.TRACK_SUB_ID;
import static com.kamaii.customer.interfacess.Consts.TRACK_SUB_LEVEL_ID;
import static com.kamaii.customer.interfacess.Consts.TRACK_VEHICLE_NUMBER;

public class TrackingActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap googleMap;
    LatLng defaultLocation;
    private UserDTO userDTO;
    private Marker originMarker;
    private Marker destinationMarker;
    CountDownTimer cT;
    private Polyline grayPolyline;
    private Polyline blackPolyline;
    boolean isMarkerRotating = false;
    LatLng previousLatLng;
    LatLng currentLatLng;
    private Marker movingCabMarker;
    private Handler handler = new Handler();
    private Runnable runnable;
    int index = 0;
    String totalkm = "0";
    String totaltime = "00:00";
    String bookingTime = "";
    private final String TAG = "ShivamCheckingTracking";
    int rider_order_tracker = 0;
    int rider_flag = 0;
    private String artistVehicleImage;
    private final double rider_latitude = 0.0;
    private final double rider_longitude = 0.0;
    private double vendorLatitude = 0.0;
    private double vendorLongitude = 0.0;
    private String subtotal = "";
    private String shippingcharges = "";
    private String discount_txt = "";
    private String discount_digit_txt = "";
    private String totalAmount = "";
    private double customerSourceLatitude = 0.0;
    private double customerSourceLongitude = 0.0;
    private double customerDestinationLatitude = 0.0;
    private double customerDestinationLongitude = 0.0;
    private String booking = "", sourceAddress = "", destinationAddress = "", vendorName = "", productName = "", vendorImage = "", mobileNumber = "", screenFlag = "", bookingDate = "",
            vechileNumber = "", category_id = "",
            sub_category_id = "", sub_level_id = "",
            rider_name = "", rider_mobile = "", booking_msg = "", booking_msg2 = "", rider_image = "",
            rider_rating = "";
    private String preparationNote = "";
    private int artistId, bookingId;
    private String artistRating;
    private int bookingFlag = -1, paymentType = -1;
    boolean isAlreadyStartedCustomerToDestination = false;
    boolean isAlreadyStartedVendorToCustomer = false;
    DecimalFormat decimalFormat = new DecimalFormat("#.##");
    DecimalFormat decimalLocationFormat = new DecimalFormat("#.#######");
    private Dialog dialog;
    HashMap paramsDecline = new HashMap<>();
    String booking_type = "product";
    double latitute;
    double longi;
    double delatitute;
    double delongitiute;
    String s_year = "", s_month = "", s_days = "", s_hour = "", s_min = "", e_year = "", e_month = "", e_days = "", e_hour = "", e_min = "";
    @BindView(R.id.pulsator)
    pl.bclogic.pulsator4droid.library.PulsatorLayout pulsator;

    @BindView(R.id.relanimation)
    RelativeLayout relanimation;

    @BindView(R.id.order_preparation_note)
    CustomTextViewBold order_preparation_note;

    @BindView(R.id.discount_txt)
    CustomTextViewBold discount_text;

    @BindView(R.id.discount_digit_txt)
    CustomTextViewBold discount_digit_text;

    @BindView(R.id.relmap)
    RelativeLayout relmap;

    @BindView(R.id.rel_sos)
    RelativeLayout rel_sos;


    @BindView(R.id.img_sos)
    ImageView img_sos;

    @BindView(R.id.relcancel)
    RelativeLayout relcancel;

    @BindView(R.id.imgback1)
    FloatingActionButton imgback;

    @BindView(R.id.call_button)
    CustomTextViewBold call_button;

    @BindView(R.id.call_button_disabled)
    CustomTextViewBold call_button_disabled;

    @BindView(R.id.ivStatus)
    ImageView ivStatus;

    @BindView(R.id.bottom_sheet)
    LinearLayout bottom_sheet;

    BottomSheetBehavior sheetBehavior;

    ProgressDialog progressDialog;
    private DialogInterface dialog_book;
    TrackingDetailDialog trackingDetailDialog;
    TrackingData trackingData;
    private SharedPrefrence prefrence;


    private Handler mBackgroundHandler;
    private static final int START_COUNTER = 1001;
    private static final int FINISH_COUNTER = 3002;
    private static final int PROGRESS_COUNTER = 3005;

    private double templatitude = 0.0, templongitude = 0.0;
    ArrayList<TaxiRouteData> taxiRouteDataArrayList = new ArrayList<>();
    List<List<HashMap<String, String>>> routeArrayList = new ArrayList<>();
    PolylineOptions options;
    boolean isStartBooking = false;
    @BindView(R.id.llSt)
    LinearLayout llSt;

    @BindView(R.id.laycallcancel)
    LinearLayout laycallcancel;


    @BindView(R.id.laycall)
    LinearLayout laycall;

    @BindView(R.id.laycancel)
    LinearLayout laycancel;

    @BindView(R.id.timer_layout)
    RelativeLayout timer_layout;

    @BindView(R.id.share_layout)
    RelativeLayout share_layout;

    @BindView(R.id.providerdata)
    RelativeLayout providerdata;

    @BindView(R.id.riderData)
    RelativeLayout riderData;


    @BindView(R.id.llTime)
    LinearLayout llTime;

    @BindView(R.id.btn_add_refer_layout)
    RelativeLayout lay_add_refer;

    /*@BindView(R.id.btn_add_refer_layout)
    RelativeLayout btn_add_refer_layout;*/

    @BindView(R.id.imgone)
    ImageView imgone;

    @BindView(R.id.imgtwo)
    ImageView imgtwo;

    @BindView(R.id.imgvendonrimage)
    CircleImageView imgvendonrimage;


    @BindView(R.id.etAddressSelectsource)
    CustomTextView etAddressSelectsource;

    @BindView(R.id.etAddressSelectdesti)
    CustomTextView etAddressSelectdesti;

    @BindView(R.id.totalamount)
    CustomTextViewBold txttotalamount;

    @BindView(R.id.txtptype)
    CustomTextViewBold txtptype;

    @BindView(R.id.txtptypemsg)
    CustomTextViewBold txtptypemsg;

    @BindView(R.id.tvStatus)
    CustomTextViewBold tvStatus;

    @BindView(R.id.servicenamerating)
    CustomTextViewBold servicenamerating;

    @BindView(R.id.txtvendorname)
    CustomTextViewBold txtvendorname;

    @BindView(R.id.shippingCharge)
    CustomTextViewBold shippingCharge;

    @BindView(R.id.subTotal)
    CustomTextViewBold subTotal;

    @BindView(R.id.dlivery_timer)
    Counter dlivery_timer;

    Marker lastOpenned = null;
    private Dialog dialogpriview;
    CustomEditText etmobile, etName;
    String name = "";
    LatLng startSource;
    LatLng endDest;

    @BindView(R.id.riderimage)
    CircleImageView riderimage;

    @BindView(R.id.ridername)
    CustomTextViewBold ridername;

    @BindView(R.id.riderrating)
    CustomTextViewBold riderrating;

    @BindView(R.id.rider_call_button)
    CustomTextViewBold rider_call_button;

    @BindView(R.id.rider_call_button_disabled)
    CustomTextViewBold rider_call_button_disabled;

    @BindView(R.id.rv_cart)
    RecyclerView rv_cart;
    @BindView(R.id.totalingdata)
    LinearLayout totalingdata;


    private GridLayoutManager gridLayoutManager;
    AdapterProductBooking adapterCartBooking;
    private ArrayList<ProductDTO> productDTOArrayList;
    boolean category_type = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        ButterKnife.bind(TrackingActivity.this);
        prefrence = SharedPrefrence.getInstance(TrackingActivity.this);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        name = userDTO.getName();
        Log.e("tracking_tracker", "101");

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        initView();

       /* if (getIntent().hasExtra("flag")) {

            String str = getIntent().getStringExtra("flag");
            Toast.makeText(this, "Hidden Notification called", Toast.LENGTH_SHORT).show();
        }*/
    }

    private void getDeliverTime(int bookingid) {

        HashMap<String, String> delivery_params = new HashMap<>();
        delivery_params.put(Consts.BOOKING_ID, String.valueOf(bookingid));
        Log.e("DATE_DELIVERY", "params" + delivery_params.toString());

        new HttpsRequest(Consts.GET_DELIVERY_TIME_API, delivery_params, TrackingActivity.this).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                        String date123 = response.getString("delivery_time_ordernote");
                        Log.e("DATE_DELIVERY", "" + date123);


                     /*   s_month = response.getString("s_month");

                        s_days = response.getString("s_day");
                        s_hour = response.getString("s_hours");
                        s_min = response.getString("s_min");

                        e_year = response.getString("e_year");
                        e_month = response.getString("e_month");
                        e_days = response.getString("e_day");
                        e_hour = response.getString("e_hours");
                        e_min = response.getString("e_min");
*/
                        SimpleDateFormat format = new SimpleDateFormat(
                                "yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());

                        Date date = null;
                        try {
                            date = format.parse(date123);
                            dlivery_timer.setDate(date);//countdown starts

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        //or use:
                        dlivery_timer.setDate(date123); //countdown starts

                        /*
                         * Additional attributes:
                         * */
                        dlivery_timer.setIsShowingTextDesc(true);
                        dlivery_timer.setMaxTimeUnit(TimeUnits.DAY);
                        dlivery_timer.setTextColor(Color.RED);
                        dlivery_timer.setTextSize(30);
                        //Typeface font3 = Typeface.createFromAsset(context.getAssets(), "ds_digit.ttf");

                        //     dlivery_timer.setTypeFace(font3);

                        dlivery_timer.setListener(new Counter.Listener() {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                Log.d(TAG, "onTick: Counter - " + millisUntilFinished);
                            }

                            @Override
                            public void onTick(long days, long hours, long minutes, long seconds) {
                                Log.d(TAG, "onTick" + days + "D " +
                                        hours + "H " +
                                        minutes + "M " +
                                        seconds + "S ");

                                String newtime = "";

                                if (days == 00 && hours == 00) {
                                    //    Log.e("DATE_FORMAT", "first");
                                    newtime =
                                            minutes + "M " +
                                                    seconds + "S ";
                                } else if (days == 00 && hours != 00) {
                                    //   Log.e("DATE_FORMAT", "second");

                                    newtime =
                                            hours + "H " + minutes + "M " + seconds + "S ";
                                } else {
                                    //    Log.e("DATE_FORMAT", "third");

                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                }
            }
        });

    }

    private void getBookingSeconds() {

        Log.e("tracking_tracker", "102");

        new HttpsRequest(GET_BOOKING_SECONDS_API, getparm(), TrackingActivity.this).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                        String second = response.getJSONObject("data").getString("second");
                        String starttimer = response.getJSONObject("data").getString("starttimer");
                        int seocndserver = Integer.parseInt(second);
                        int timer = Integer.parseInt(starttimer);
                        if (seocndserver < timer) {
                            seocndserver = timer - seocndserver;
                            long ssss = seocndserver * 1000;


                            cT = new CountDownTimer(ssss, 1000) {

                                public void onTick(long millisUntilFinished) {

                                    int va = (int) ((millisUntilFinished % 60000) / 1000);
                                }

                                public void onFinish() {
                                    try {
                                        Intent intent = new Intent(TrackingActivity.this, MyService.class);
                                        TrackingActivity.this.stopService(intent);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    if (bookingFlag == 0) {
                                        autodecline(String.valueOf(artistId), String.valueOf(bookingId), "1");
                                    }
                                }
                            };
                            cT.start();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                }
            }
        });


    }

    private void initView() {

        Log.e("tracking_tracker", "103");


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        handler = new Handler();
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        getTrackingData();
        getDeliverTime(bookingId);
        getBookingSeconds();

        progressDialog = new

                ProgressDialog(TrackingActivity.this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        pulsator.start();
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {

                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {

                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        dialogpriview = new

                Dialog(TrackingActivity.this, R.style.Theme_Dialog);
        dialogpriview.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogpriview.setContentView(R.layout.dailog_refer);
        dialogpriview.setCancelable(true);

        etmobile = dialogpriview.findViewById(R.id.etmobile);
        etName = dialogpriview.findViewById(R.id.etName);
        ImageView tvCancel = dialogpriview.findViewById(R.id.tvCancel);
        CustomTextViewBold tvAdd = dialogpriview.findViewById(R.id.tvAdd);
        lay_add_refer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogpriview.show();

            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogpriview.dismiss();
            }
        });
        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etName.getText().toString().trim().isEmpty()) {
                    Toast.makeText(TrackingActivity.this, "Please Enter Name", Toast.LENGTH_LONG).show();
                } else if (etmobile.getText().toString().trim().isEmpty()) {
                    Toast.makeText(TrackingActivity.this, "Please Enter Mobile Number", Toast.LENGTH_LONG).show();
                } else {
                    addrefer();
                    dialogpriview.dismiss();
                }

            }
        });


        if (s_year != null || !s_year.equalsIgnoreCase("")) {

        }
    }

    public void addrefer() {
        Log.e("tracking_tracker", "104");

        new HttpsRequest(Consts.Add_REFER, getparm(), TrackingActivity.this).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {
                        String phoneNumberWithCountryCode = "+91" + etmobile.getText().toString();
                        String message = "Dear" + " " + etName.getText().toString() + "\n \n" + getResources().getString(R.string.wamsgone) + name + getResources().getString(R.string.wamsgtwo);
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("https://api.whatsapp.com/send?phone=%s&text=%s", phoneNumberWithCountryCode, message))));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                }
            }
        });
    }

    private void getTrackingData() {

        Log.e("tracking_tracker", "105");

        artistId = Integer.parseInt(getIntent().getStringExtra(TRACK_ARTIST_ID));
        vendorName = getIntent().getStringExtra(TRACK_BOOKING_VENDOR_NAME);

        discount_txt = getIntent().getStringExtra(TRACK_BOOKING_DISCOUNT_TXT);
        discount_digit_txt = getIntent().getStringExtra(TRACK_BOOKING_DISCOUNT_DIGIT);
        rider_order_tracker = Integer.parseInt(getIntent().getStringExtra(TRACK_BOOKING_RIDER_ORDER_TRACKER));

        if (rider_order_tracker == 1) {
            String rider_latitude = getIntent().getStringExtra(TRACK_BOOKING_VENDOR_LATITUDE);

            String rider_longitude = getIntent().getStringExtra(TRACK_BOOKING_RIDER_LONGITUDE);
        }
        //partner location
        rider_order_tracker = Integer.parseInt(getIntent().getStringExtra(TRACK_BOOKING_RIDER_ORDER_TRACKER));
        category_type = getIntent().getBooleanExtra("category_type", false);


        if (!category_type) {
            booking_type = "cab";
            findViewById(R.id.location_add).setVisibility(View.VISIBLE);
        } else {
            booking_type = "product";
        }
        vendorLatitude = Double.parseDouble(getIntent().getStringExtra(TRACK_BOOKING_VENDOR_LATITUDE));
        vendorLongitude = Double.parseDouble(getIntent().getStringExtra(TRACK_BOOKING_VENDOR_LONGITUDE));
        customerSourceLatitude = Double.parseDouble(getIntent().getStringExtra(TRACK_SOURCE_CUSTOMER_LATITUDE));

        customerSourceLongitude = Double.parseDouble(getIntent().getStringExtra(TRACK_SOURCE_CUSTOMER_LONGITUDE));

        customerDestinationLatitude = Double.parseDouble(getIntent().getStringExtra(TRACK_DESTINATION_CUSTOMER_LATITUDE));

        customerDestinationLongitude = Double.parseDouble(getIntent().getStringExtra(TRACK_DESTINATION_CUSTOMER_LONGITUDE));

        sourceAddress = getIntent().getStringExtra(TRACK_BOOKING_SOURCE_ADDRESS);

        destinationAddress = getIntent().getStringExtra(TRACK_BOOKING_DESTINATION_ADDRESS);

        totalAmount = getIntent().getStringExtra(TRACK_BOOKING_TOTAL_AMOUNT);

        productName = getIntent().getStringExtra(TRACK_BOOKING_PRODUCT_NAME);

        vendorImage = getIntent().getStringExtra(TRACK_BOOKING_VENDOR_IMAGE);
        mobileNumber = getIntent().getStringExtra(TRACK_BOOKING_MOBILE_NUMBER);
        try {
            category_id = getIntent().getStringExtra(TRACK_CAT_ID);
            sub_category_id = getIntent().getStringExtra(TRACK_SUB_ID);
            sub_level_id = getIntent().getStringExtra(TRACK_SUB_LEVEL_ID);
        } catch (Exception e) {
            category_id = "";
            sub_category_id = "";
            sub_level_id = "";
        }
        screenFlag = getIntent().getStringExtra(TRACK_BOOKING_SCREEN_FLAG);
        bookingFlag = Integer.parseInt(getIntent().getStringExtra(TRACK_BOOKING_BOOKING_FLAG));
        bookingId = Integer.parseInt(getIntent().getStringExtra(TRACK_BOOKING_ID));

        paymentType = Integer.parseInt(getIntent().getStringExtra(TRACK_BOOKING_PAYMENT_TYPE));
        bookingDate = getIntent().getStringExtra(TRACK_BOOKING_DATE);
        bookingTime = getIntent().getStringExtra(TRACK_BOOKING_TIME);
        vechileNumber = getIntent().getStringExtra(TRACK_VEHICLE_NUMBER);
        booking = getIntent().getStringExtra(Consts.BOOKING);

        getDeliverTime(bookingId);
        taxiRouteDataArrayList = (ArrayList<TaxiRouteData>) prefrence.getRouteUser(String.valueOf(bookingId));
        if (booking.equalsIgnoreCase("booking")) {
            Intent intent = new Intent(TrackingActivity.this, MyService.class);
            startService(intent);
        }
        try {
            startSource = new LatLng(vendorLatitude, vendorLongitude);
            endDest = new LatLng(Double.parseDouble(prefrence.getValue(Consts.LATITUDE)), Double.parseDouble(prefrence.getValue(Consts.LONGITUDE)));

            Log.e("tracking_tracker", "start location" + " lati: " + vendorLatitude + " longi: " + vendorLongitude);
            Log.e("tracking_tracker", "dest location" + " lati: " + customerDestinationLatitude + " longi: " + customerDestinationLongitude);
            getBookingRoute();
            //  destinationMarker = addOriginDestinationMarkerAndGet(points.get(points.size() - 1));
            destinationMarker = addOriginDestinationMarkerAndGet(new LatLng(customerDestinationLatitude, customerDestinationLongitude));
            destinationMarker.setAnchor(0.5f, 0.5f);
            //moveCamera(new LatLng(customerDestinationLatitude, customerDestinationLongitude));

       /*     if (taxiRouteDataArrayList.size() == 0) {
                new DrawRouteUtils(TrackingActivity.this, bookingId, new LatLng(vendorLatitude, vendorLongitude), new LatLng(Double.parseDouble(prefrence.getValue(Consts.LATITUDE)), Double.parseDouble(prefrence.getValue(Consts.LONGITUDE))));
               // new DrawRouteUtils(TrackingActivity.this, bookingId, new LatLng(vendorLatitude, vendorLongitude), new LatLng(customerDestinationLatitude,customerDestinationLongitude));
            } else {
                DrawRouteUtils drawRouteUtils = new DrawRouteUtils();
                JSONObject jsonObject = new JSONObject(prefrence.getValue(bookingId + ROUTE_PATH));
                routeArrayList = drawRouteUtils.parse(jsonObject);
            }
       */
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }

        trackingData = new TrackingData();

        trackingData.setArtistId(artistId);
        trackingData.setVendorName(vendorName);
        trackingData.setArtistRating(artistRating);
        trackingData.setVendorImage(vendorImage);

        trackingData.setVendorLatitude(vendorLatitude);
        trackingData.setVendorLongitude(vendorLongitude);
        trackingData.setProductName(productName);
        trackingData.setMobileNumber(mobileNumber);
        trackingData.setVehicleNumber(vechileNumber);
        trackingData.setArtistVehicleImage(artistVehicleImage);
        trackingData.setBookingId(bookingId);
        trackingData.setBookingDate(bookingDate);
        trackingData.setBookingFlag(bookingFlag);
        trackingData.setBookingTime(bookingTime);
        trackingData.setTotalAmount(totalAmount);
        trackingData.setSourceAddress(sourceAddress);
        trackingData.setDestinationAddress(destinationAddress);
        trackingData.setRider_name(rider_name);
        trackingData.setRider_mobileno(rider_mobile);
        trackingData.setPaymentType(paymentType);
        trackingData.setScreenFlag(screenFlag);
        trackingData.setRider_image(rider_image);
        trackingData.setRider_rating(rider_rating);
        trackingData.setShippingcharges(shippingcharges);
        trackingData.setDiscount_txt(discount_txt);
        trackingData.setDiscount_digit_txt(discount_digit_txt);
        trackingData.setSubTotal(subtotal);
        trackingData.setPreparationNote(preparationNote);


        HandlerThread handlerThread = new HandlerThread("BackgroundThread");
        handlerThread.start();

        MyCallback callback = new MyCallback();
        mBackgroundHandler = new Handler(handlerThread.getLooper(), callback);
        mBackgroundHandler.obtainMessage(START_COUNTER, true).sendToTarget();

        share_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String totaltime = ProjectUtils.changeDateFormate1(trackingData.getBookingDate()) + " " + trackingData.getBookingTime();
                String phoneNumberWithCountryCode = "+91" + etmobile.getText().toString();
                String message = "Dear," + "\n \n" + getResources().getString(R.string.shareone) + "\n\n" + getResources().getString(R.string.sharetwo) + "\n\n" +
                        getResources().getString(R.string.sharethree) + " " + trackingData.getVendorName() + "\n" +
                        getResources().getString(R.string.sharefour) + " " + trackingData.getProductName() + " " + trackingData.getVehicleNumber() + "\n" +
                        getResources().getString(R.string.sharefive) + " " + totaltime + "\n" +
                        getResources().getString(R.string.sharesix) + " " + trackingData.getSourceAddress() + "\n" +
                        getResources().getString(R.string.shareseven) + " " + trackingData.getDestinationAddress() + "\n \n" +
                        getResources().getString(R.string.shareseight) + "\n\n " +
                        getResources().getString(R.string.sharenine) + "\n\n ";

                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = message;
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));

            }
        });
        laycall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!trackingData.getMobileNumber().trim().isEmpty()) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + trackingData.getMobileNumber()));
                    startActivity(intent);
                } else {
                    Toast.makeText(TrackingActivity.this, "Contact number is not available", Toast.LENGTH_LONG).show();
                }

            }
        });


        laycancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completeDialog(getResources().getString(R.string.cancel), getResources().getString(R.string.booking_cancel_msg) + " " + trackingData.getVendorName() + "?", userDTO.getUser_id(), String.valueOf(trackingData.getBookingId()));
            }
        });
        img_sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + "100"));
                startActivity(intent);
            }
        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        Log.e("tracking_tracker", "106");

        this.googleMap = googleMap;
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.okay_book_map));
        if (bookingFlag == 0) {
            double longitiute = Double.parseDouble(prefrence.getValue(Consts.LONGITUDE));
            double latitute = Double.parseDouble(prefrence.getValue(Consts.LATITUDE));
            defaultLocation = new LatLng(latitute, longitiute);
            showDefaultLocationOnMap(defaultLocation);
            googleMap.setMaxZoomPreference(14.0f);
            googleMap.setMinZoomPreference(6.0f);
        }
        if (bookingFlag == 1 || bookingFlag == 2 || bookingFlag == 3) {
            isAlreadyStartedVendorToCustomer = true;
        }
        if (isAlreadyStartedVendorToCustomer) {

            Log.e("loadmap", "called");
            this.showCurvedPolyline(new LatLng(vendorLatitude, vendorLongitude), new LatLng(customerDestinationLatitude, customerDestinationLongitude), 0.5);
            loadMapOfRoute();
        }
        if (bookingFlag == 3) {

            isAlreadyStartedCustomerToDestination = true;

            if (checkMapCategory(category_id)) {

                getBookingRoute();
            } else {

                mBackgroundHandler.obtainMessage(START_COUNTER, true).sendToTarget();
            }
        }
        this.googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker) {

                return true;
            }
        });
    }

    private void moveCamera(LatLng latLng) {
        Log.e("tracking_tracker", "107");

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    private void animateRouteCamera(LatLng latLng) {
        Log.e("tracking_tracker", "108");

        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(12.5f).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void animateCamera(LatLng latLng) {
        Log.e("tracking_tracker", "108");

        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(12f).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void showDefaultLocationOnMap(LatLng latLng) {
        Log.e("tracking_tracker", "109");

        moveCamera(latLng);
        moveCamera(new LatLng(customerDestinationLatitude, customerDestinationLongitude));
        //  googleMap.setMaxZoomPreference(11.5f);
        googleMap.setMinZoomPreference(6.0f);
        animateCamera(latLng);
    }

    private Marker addOriginDestinationMarkerAndGet(LatLng latLng) {
        Log.e("tracking_tracker", "110");

        Bitmap bitmap = null;
        if (isAlreadyStartedVendorToCustomer && !isAlreadyStartedCustomerToDestination) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.home_marker);
        } else {
            //    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.drop_point);
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.home_marker);
        }
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
        return googleMap.addMarker(new MarkerOptions().position(latLng).flat(true).icon(bitmapDescriptor));
    }

    // private Marker addOriginMarkerAndGet(LatLng latLng) {
    private Marker addOriginMarkerAndGet(LatLng latLng) {

        Log.e("tracking_tracker", "red marker caller");
        Bitmap bitmap = null;
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.placeholder);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
        return googleMap.addMarker(new MarkerOptions().position(latLng).flat(true).icon(bitmapDescriptor));
    }

    private void showRouteVendorToCustomerPath() {
        Log.e("tracking_tracker", "111");

        DrawRouteUtils drawRouteUtils = new DrawRouteUtils();
        JSONObject jsonObject = null;
        List<LatLng> polyLineList = new ArrayList<>();
        try {
            jsonObject = new JSONObject(prefrence.getValue(bookingId + ROUTE_PATH));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        routeArrayList = drawRouteUtils.parse(jsonObject);
        ArrayList<LatLng> points = null;
        PolylineOptions lineOptions = null;

        // Traversing through all the routes
        for (int i = 0; i < routeArrayList.size(); i++) {
            points = new ArrayList<LatLng>();
            lineOptions = new PolylineOptions();

            // Fetching i-th route
            List<HashMap<String, String>> path = routeArrayList.get(i);

            // Fetching all the points in i-th route
            for (int j = 0; j < path.size(); j++) {
                HashMap<String, String> point = path.get(j);

                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);

                points.add(position);
            }

            // Adding all the points in the route to LineOptions
            lineOptions.addAll(points);
            lineOptions.width(8);
            lineOptions.color(Color.GRAY);
        }

        // Drawing polyline in the Google Map for the i-th route
        if (lineOptions != null) {
            if (grayPolyline != null) {
                grayPolyline.remove();
            }
            grayPolyline = googleMap.addPolyline(lineOptions);

        }
        //  originMarker = addOriginMarkerAndGet(points.get(0));
        originMarker = addOriginMarkerAndGet(new LatLng(vendorLatitude, vendorLongitude));
        originMarker.setAnchor(0.5f, 0.5f);
        //  animateCamera(new LatLng(vendorLatitude, vendorLongitude));

        //  destinationMarker = addOriginDestinationMarkerAndGet(points.get(points.size() - 1));
        destinationMarker = addOriginDestinationMarkerAndGet(new LatLng(customerDestinationLatitude, customerDestinationLongitude));
        destinationMarker.setAnchor(0.5f, 0.5f);
        //   animateCamera(new LatLng(customerDestinationLatitude, customerDestinationLongitude));


    }

    private void showRouteCustomerToDestinationPath() {
        Log.e("tracking_tracker", "112");

        ArrayList<LatLng> points = null;
        PolylineOptions lineOptions = null;

        // Traversing through all the routes
        for (int i = 0; i < routeArrayList.size(); i++) {
            points = new ArrayList<LatLng>();
            lineOptions = new PolylineOptions();

            // Fetching i-th route
            List<HashMap<String, String>> path = routeArrayList.get(i);

            // Fetching all the points in i-th route
            for (int j = 0; j < path.size(); j++) {
                HashMap<String, String> point = path.get(j);

                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);

                points.add(position);
            }

            // Adding all the points in the route to LineOptions
            lineOptions.addAll(points);
            lineOptions.width(8);
            lineOptions.color(Color.BLUE);
        }

        // Drawing polyline in the Google Map for the i-th route
        if (lineOptions != null) {
            if (grayPolyline != null) {
                grayPolyline.remove();
            }
            grayPolyline = googleMap.addPolyline(lineOptions);

        }
        //originMarker = addOriginMarkerAndGet(points.get(0));
        originMarker = addOriginMarkerAndGet(new LatLng(vendorLatitude, vendorLongitude));
        originMarker.setAnchor(0.5f, 0.5f);
        //    animateCamera(new LatLng(vendorLatitude, vendorLongitude));

        destinationMarker = addOriginDestinationMarkerAndGet(new LatLng(customerDestinationLatitude, customerDestinationLongitude));
        destinationMarker.setAnchor(0.5f, 0.5f);
        //  animateCamera(new LatLng(customerDestinationLatitude, customerDestinationLongitude));

        this.googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker) {

                return true;
            }
        });

    }

    private void updateCarLocation(LatLng latLng) {
        Log.e("tracking_tracker", "113");

        if (movingCabMarker == null) {
            if (sub_level_id.equalsIgnoreCase("4") || sub_level_id.equalsIgnoreCase("7") || sub_level_id.equalsIgnoreCase("11")) {
                movingCabMarker = addCarMarkerAndGet(latLng, 1); // mini
            } else if (sub_level_id.equalsIgnoreCase("17") || sub_level_id.equalsIgnoreCase("9")) {
                movingCabMarker = addCarMarkerAndGet(latLng, 3); // suv
            } else if (sub_level_id.equalsIgnoreCase("5") || sub_level_id.equalsIgnoreCase("8") || sub_level_id.equalsIgnoreCase("12")) {
                movingCabMarker = addCarMarkerAndGet(latLng, 2); // sedan
            } else if (category_id.equalsIgnoreCase("74")) {
                movingCabMarker = addCarMarkerAndGet(latLng, 4); // rikhso
            } else if (category_id.equalsIgnoreCase("91") || category_id.equalsIgnoreCase("138")
                    || category_id.equalsIgnoreCase("136") || category_id.equalsIgnoreCase("137")
                    || category_id.equalsIgnoreCase("139") || category_id.equalsIgnoreCase("140")
                    || category_id.equalsIgnoreCase("134")) {
                movingCabMarker = addCarMarkerAndGet(latLng, 5); // food
            } else {
                movingCabMarker = addCarMarkerAndGet(latLng, 5); // default
            }

        }
        if (previousLatLng == null) {
            currentLatLng = latLng;
            previousLatLng = currentLatLng;
            movingCabMarker.setPosition(currentLatLng);
            movingCabMarker.setAnchor(0.5f, 0.5f);
            animateCamera(currentLatLng);
        } else {
            previousLatLng = currentLatLng;
            currentLatLng = latLng;
            ValueAnimator valueAnimator = AnimationUtils.carAnimator();
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    if (currentLatLng != null && previousLatLng != null && previousLatLng != currentLatLng) {
                        float multiplier = valueAnimator.getAnimatedFraction();
                        //LatLng nextLocation = new LatLng(multiplier * currentLatLng.latitude + (1 - multiplier) * previousLatLng.latitude, multiplier * currentLatLng.longitude + (1 - multiplier) * previousLatLng.longitude);
                        LatLng nextLocation = new LatLng(multiplier * currentLatLng.latitude + (1 - multiplier) * previousLatLng.latitude, multiplier * currentLatLng.longitude + (1 - multiplier) * previousLatLng.longitude);
                        movingCabMarker.setPosition(nextLocation);
                        movingCabMarker.setAnchor(0.5f, 0.5f);
                        float rotation = MapUtils.getRotation(previousLatLng, nextLocation);
                        movingCabMarker.setRotation(rotation);
                        animateCamera(nextLocation);
                    }
                }
            });

            valueAnimator.start();

        }

    }

    public Marker addCarMarkerAndGet(LatLng latLng, int imageType) {
        Log.e("tracking_trackershi", "114");
        if (options.isVisible()) {
            Log.e("tracking_trackershiv", latLng.toString());
            options.visible(false);
        }
        this.showCurvedPolyline(latLng, new LatLng(customerDestinationLatitude, customerDestinationLongitude), 0.5);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(MapUtils.getCarBitmap(TrackingActivity.this, imageType));
        return googleMap.addMarker(new MarkerOptions().position(latLng).flat(true).icon(bitmapDescriptor));

    }

    public HashMap<String, String> getparm() {
        Log.e("tracking_tracker", "115");

        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.ARTIST_ID, String.valueOf(artistId));
        parms.put("booking_id", String.valueOf(bookingId));
        return parms;
    }

    public void getArtistData() {
        new HttpsRequest(GET_LOCATION_ARTIST_API, getparm(), TrackingActivity.this).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                        //      Toast.makeText(TrackingActivity.this, "abcd", Toast.LENGTH_SHORT).show();
                        JSONObject jsonObject = response.getJSONObject("data");
                        String lati = decimalLocationFormat.format(Double.parseDouble(jsonObject.getString("lati")));
                        String longit = decimalLocationFormat.format(Double.parseDouble(jsonObject.getString("longi")));

                        if (rider_order_tracker == 1) {

                            String rider_lat = decimalLocationFormat.format(Double.parseDouble(jsonObject.getString("riderlati")));
                            String rider_longi = decimalLocationFormat.format(Double.parseDouble(jsonObject.getString("riderlongi")));

                            latitute = Double.parseDouble(rider_lat);
                            longi = Double.parseDouble(rider_longi);

                        } else {

                            latitute = Double.parseDouble(lati);
                            longi = Double.parseDouble(longit);
                        }

                        delatitute = Double.parseDouble(prefrence.getValue(Consts.LATITUDE));
                        delongitiute = Double.parseDouble(prefrence.getValue(Consts.LONGITUDE));

                        originMarker = addOriginMarkerAndGet(new LatLng(vendorLatitude, vendorLongitude));
                        originMarker.setAnchor(0.5f, 0.5f);
                        //   animateCamera(new LatLng(vendorLatitude, vendorLongitude));

                        if (customerDestinationLatitude == 0.0) {

                            destinationMarker = addOriginDestinationMarkerAndGet(new LatLng(delatitute, delongitiute));
                            destinationMarker.setAnchor(0.5f, 0.5f);
                            //  animateCamera(new LatLng(customerDestinationLatitude, customerDestinationLongitude));

                        } else {
                            destinationMarker = addOriginDestinationMarkerAndGet(new LatLng(customerDestinationLatitude, customerDestinationLongitude));
                            destinationMarker.setAnchor(0.5f, 0.5f);
                            //    animateCamera(new LatLng(customerDestinationLatitude, customerDestinationLongitude));

                        }


                        Log.e("destination_latitude", "" + delatitute);
                        Log.e("destination_latitude", "" + delatitute);
                        LatLng latLng = new LatLng(latitute, longi);
                       /* if (templatitude != latitute || templongitude != longi) {
                            setupMarkerWithTimeDistance(latitute, longi, delatitute, delongitiute);
                            templatitude = latitute;
                            templongitude = longi;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateCarLocation(latLng);
                                }
                            });
                        }*/
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                }
            }
        });

    }

    public HashMap<String, String> getRiderparm() {
        HashMap<String, String> parms = new HashMap<>();
        parms.put("booking_id", String.valueOf(bookingId));
        return parms;
    }

    private void showLiveMovingCab() {
        Log.e("tracking_tracker", "116");

        handler = new Handler();

        runnable = new Runnable() {
            @Override
            public void run() {
                if (bookingFlag == 1 || bookingFlag == 0 || bookingFlag == 3) {
                    getArtistData();
                    handler.postDelayed(runnable, 1000);
                } else if (bookingFlag == 4) {
                    handler.removeCallbacks(runnable);
                }
            }
        };
        handler.postDelayed(runnable, 2000);
    }

    public void dialogshow(TrackingData trackingData) {
        dialog = new Dialog(TrackingActivity.this/*, android.R.style.Theme_Dialog*/);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dailog_share);

        CustomEditText etmobile = dialog.findViewById(R.id.etmobile);
        CustomEditText etName = dialog.findViewById(R.id.etName);
        CustomTextView tvCancel = dialog.findViewById(R.id.tvCancel);
        CustomTextView tvAdd = dialog.findViewById(R.id.tvAdd);

        dialog.show();
        dialog.setCancelable(false);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String totaltime = ProjectUtils.changeDateFormate1(trackingData.getBookingDate()) + " " + trackingData.getBookingTime();
                String phoneNumberWithCountryCode = "+91" + etmobile.getText().toString();
                String message = "Dear," + "\n \n" + getResources().getString(R.string.shareone) + "\n\n" + getResources().getString(R.string.sharetwo) + "\n\n" +
                        getResources().getString(R.string.sharethree) + " " + trackingData.getVendorName() + "\n" +
                        getResources().getString(R.string.sharefour) + " " + trackingData.getProductName() + " " + trackingData.getVehicleNumber() + "\n" +
                        getResources().getString(R.string.sharefive) + " " + totaltime + "\n" +
                        getResources().getString(R.string.sharesix) + " " + trackingData.getSourceAddress() + "\n" +
                        getResources().getString(R.string.shareseven) + " " + trackingData.getDestinationAddress() + "\n \n" +
                        getResources().getString(R.string.shareseight) + "\n\n " +
                        getResources().getString(R.string.sharenine) + "\n\n ";

                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = message;
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));

            }
        });

    }

    public void completeDialog(String title, String msg, String userId, String booking_id) {
        try {
            new AlertDialog.Builder(this)
                    .setMessage(msg)
                    .setCancelable(false)
                    .setPositiveButton(this.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog_book = dialog;
                            decline(String.valueOf(artistId), booking_id, "2");

                        }
                    })
                    .setNegativeButton(this.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    })
                    .show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void autodecline(String userId, String bookingId, String declineby) {

        String decl = declineby;
        HashMap paramsDecline = new HashMap<>();
        paramsDecline.put(Consts.USER_ID, userId);
        paramsDecline.put(Consts.BOOKING_ID, bookingId);
        paramsDecline.put("jjjj", decl);
        paramsDecline.put("decline_by", decl);
        paramsDecline.put("passvalue", "0");
        paramsDecline.put(Consts.DECLINE_REASON, "Busy");
        paramsDecline.put("lat", customerDestinationLatitude);
        paramsDecline.put("lang", customerDestinationLongitude);
        paramsDecline.put("cat_id", category_id);
        paramsDecline.put("sub_id", sub_category_id);
        paramsDecline.put("third_id", sub_level_id);

        new HttpsRequest(Consts.DECLINE_BOOKING_API, paramsDecline, TrackingActivity.this).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    getBookingSeconds();
                } else {
                }


            }
        });
    }

    public void decline(String userId, String bookingId, String declineby) {

        paramsDecline.put(Consts.USER_ID, userId);
        paramsDecline.put("user_id2", userId);
        paramsDecline.put(Consts.BOOKING_ID, bookingId);
        String decl = declineby;
        paramsDecline.put("jjjj", decl);
        paramsDecline.put("decline_by", "2");
        paramsDecline.put("passvalue", "0");
        paramsDecline.put(Consts.DECLINE_REASON, "Busy");
        paramsDecline.put("lat", customerDestinationLatitude);
        paramsDecline.put("lang", customerDestinationLongitude);
        paramsDecline.put("cat_id", category_id);
        paramsDecline.put("sub_id", sub_category_id);
        paramsDecline.put("third_id", sub_level_id);

        new HttpsRequest(Consts.DECLINE_BOOKING_API, paramsDecline, TrackingActivity.this).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    findViewById(R.id.laycancel).setEnabled(false);
                    laycancel.setVisibility(View.GONE);
                    relcancel.setVisibility(View.VISIBLE);
                    llSt.setBackground(getDrawable(R.drawable.text_tracking_background_red));
                    tvStatus.setTextColor(getResources().getColor(R.color.red));
                    tvStatus.setText("Order Cancelled");
                    ivStatus.setVisibility(View.VISIBLE);
                    Glide.with(TrackingActivity.this).
                            load(R.drawable.ic_cancel)
                            .dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(ivStatus);
                    order_preparation_note.setVisibility(View.GONE);
                } else {
                }


            }
        });
    }


    class MyCallback implements Handler.Callback {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case START_COUNTER:
                    getBooking();

                    break;

                case FINISH_COUNTER:
                    Intent intent = new Intent(TrackingActivity.this, ScrachActivity.class);
                    intent.putExtra("category_type", true);
                    intent.putExtra("vendor_name", vendorName);
                    intent.putExtra("artist_id", String.valueOf(artistId));
                    intent.putExtra("vendorimage", vendorImage);
                    intent.putExtra("saddress", sourceAddress);
                    intent.putExtra("daddress", destinationAddress);
                    intent.putExtra("totalamount", String.valueOf(totalAmount));
                    intent.putExtra("productname", productName);
                    intent.putExtra("mobileno", mobileNumber);
                    intent.putExtra("booking", "booking");
                    intent.putExtra("bflag", String.valueOf(bookingFlag));
                    intent.putExtra("bid", String.valueOf(bookingId));
                    intent.putExtra("pay_type", String.valueOf(paymentType));
                    intent.putExtra("booking_type", booking_type);
                    startActivity(intent);
                    finish();
                    break;
                default:


            }
            return true;
        }

    }

    public void getBooking() {
        Log.e("tracking_tracker", "117");

        prefrence = SharedPrefrence.getInstance(TrackingActivity.this);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);

        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.getTestbooking(userDTO.getUser_id(), String.valueOf(bookingId));
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {

                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        JSONObject object = new JSONObject(s);
                        try {
                            ArrayList<UserBooking> userBookingList = new ArrayList<>();
                            Type getpetDTO = new TypeToken<List<UserBooking>>() {
                            }.getType();

                            userBookingList = new Gson().fromJson(object.getJSONArray("data").toString(), getpetDTO);
                            artistId = Integer.parseInt(userBookingList.get(0).getArtist_id());
                            vendorName = userBookingList.get(0).getArtistName();


                            vendorImage = userBookingList.get(0).getArtistImage();
                            vendorLatitude = Double.parseDouble(userBookingList.get(0).getArtistlivelat());
                            vendorLongitude = Double.parseDouble(userBookingList.get(0).getArtistlivelang());
                            artistRating = userBookingList.get(0).getArtistRating();


                            mobileNumber = userBookingList.get(0).getArtistMobile();
                            bookingId = Integer.parseInt(userBookingList.get(0).getId());
                            bookingDate = userBookingList.get(0).getBooking_date();
                            bookingFlag = Integer.parseInt(userBookingList.get(0).getBooking_flag());
                            booking_msg = userBookingList.get(0).getBooking_msg();
                            booking_msg2 = userBookingList.get(0).getBooking_msg2();
                            discount_txt = userBookingList.get(0).getDiscount_txt();
                            discount_digit_txt = userBookingList.get(0).getDiscount_digit_txt();

                            productDTOArrayList = new ArrayList<>();
                            productDTOArrayList = userBookingList.get(0).getProduct();
                            preparationNote = userBookingList.get(0).getPreparationNote();

                            String cating = "0";


                            if (category_type) {
                                cating = "0";
                            } else {
                                cating = "1";
                            }


                            adapterCartBooking = new AdapterProductBooking(TrackingActivity.this, productDTOArrayList, cating);
                            rv_cart.setLayoutManager(new LinearLayoutManager(TrackingActivity.this));
                            rv_cart.setAdapter(adapterCartBooking);

                            rider_order_tracker = Integer.parseInt(userBookingList.get(0).getRider_order());
                            if (userBookingList.get(0).getRider_id().equals("0")) {
                                riderData.setVisibility(View.GONE);
                            }
                            if (rider_order_tracker == 1) {

                                riderData.setVisibility(View.VISIBLE);
                                rider_flag = Integer.parseInt(userBookingList.get(0).getRider_flag());

                                rider_name = userBookingList.get(0).getRider_name();
                                rider_mobile = userBookingList.get(0).getRider_mobileno();
                                rider_image = userBookingList.get(0).getRider_image();//riderimage
                                rider_rating = userBookingList.get(0).getRider_rating();//riderrating
                            }
                            if (userBookingList.get(0).getPartnerisrider().equals("1")) {

                                riderData.setVisibility(View.VISIBLE);
                                rider_flag = Integer.parseInt(userBookingList.get(0).getRider_flag());

                                rider_name = userBookingList.get(0).getRider_name();
                                rider_mobile = userBookingList.get(0).getRider_mobileno();
                                rider_image = userBookingList.get(0).getRider_image();//riderimage
                                rider_rating = userBookingList.get(0).getRider_rating();//riderrating
                            }
                            bookingTime = userBookingList.get(0).getBooking_time();

                            sourceAddress = userBookingList.get(0).getAddress();
                            destinationAddress = userBookingList.get(0).getDestinationaddress();

                            paymentType = Integer.parseInt(userBookingList.get(0).getPay_type());
                            totalAmount = userBookingList.get(0).getPrice();
                            shippingcharges = userBookingList.get(0).getShipping_charges();
                            subtotal = userBookingList.get(0).getSub_total();
                            vechileNumber = userBookingList.get(0).getProduct().get(0).getVehicle_number();


                            trackingData = new TrackingData();
                            trackingData.setArtistId(artistId);
                            trackingData.setVendorName(vendorName);
                            trackingData.setArtistRating(artistRating);
                            trackingData.setVendorImage(vendorImage);
                            trackingData.setVendorLatitude(vendorLatitude);
                            trackingData.setVendorLongitude(vendorLongitude);
                            trackingData.setProductName(productName);
                            trackingData.setMobileNumber(mobileNumber);
                            trackingData.setArtistVehicleImage(artistVehicleImage);
                            trackingData.setVehicleNumber(vechileNumber);
                            trackingData.setBookingId(bookingId);
                            trackingData.setBookingDate(bookingDate);
                            trackingData.setRider_name(rider_name);
                            trackingData.setRider_mobileno(rider_mobile);
                            trackingData.setRider_image(rider_image);
                            trackingData.setRider_rating(rider_rating);
                            trackingData.setBookingFlag(bookingFlag);
                            trackingData.setShippingcharges(shippingcharges);
                            trackingData.setSubTotal(subtotal);
                            trackingData.setBookingTime(bookingTime);
                            trackingData.setTotalAmount(totalAmount);
                            trackingData.setDiscount_txt(discount_txt);
                            trackingData.setDiscount_digit_txt(discount_digit_txt);
                            trackingData.setSourceAddress(sourceAddress);
                            trackingData.setDestinationAddress(destinationAddress);
                            trackingData.setPreparationNote(preparationNote);

                            trackingData.setPaymentType(paymentType);
                            trackingData.setScreenFlag(screenFlag);
                            trackingData.setCat_typemap(userBookingList.get(0).getCat_typemap());

                            setViewData();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });

    }

    public void getLatestBooking() {
        Log.e("tracking_tracker", "118");

        prefrence = SharedPrefrence.getInstance(TrackingActivity.this);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);

        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.getbooking(userDTO.getUser_id());
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        JSONObject object = new JSONObject(s);


                        try {
                            List<UserBooking> userBookingList = new ArrayList<>();
                            Type getpetDTO = new TypeToken<List<UserBooking>>() {
                            }.getType();

                            userBookingList = new Gson().fromJson(object.getJSONArray("data").toString(), getpetDTO);

                            bookingId = Integer.parseInt(userBookingList.get(0).getId());
                            artistId = Integer.parseInt(userBookingList.get(0).getArtist_id());
                            trackingData.setBookingId(bookingId);
                            trackingData.setArtistId(bookingId);
                            // get_new_booking_flag = Integer.parseInt(userBookingList.get(0).getGet_new_booking_flag());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });

    }

    public void getBookingRoute() {
        Log.e("tracking_tracker", "119");

        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.getBookingRoute(String.valueOf(bookingId));
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();
                        String s = responseBody.string();
                        JSONObject object = new JSONObject(s);
                        JSONArray jsonElements = object.getJSONArray("data");
                        for (int i = 0; i < jsonElements.length(); i++) {
                            JSONObject obj = jsonElements.getJSONObject(i);
                            String route = obj.getString("route");

                            customerDestinationLatitude = Double.parseDouble(obj.getString("dest_latitude"));
                            customerDestinationLongitude = Double.parseDouble(obj.getString("dest_longitude"));

                            Log.e("customerlatlong", " 1 " + customerDestinationLatitude + " longi " + customerDestinationLongitude);
                            /*JSONObject jObject = new JSONObject(route);
                            DrawRouteUtils drawRouteUtils = new DrawRouteUtils();
                            routeArrayList = drawRouteUtils.parse(jObject);
                            JSONArray routes = jObject.getJSONArray("routes");*/
                            // StartCustomerSourceToDestination(routes);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });

    }

    public void StartCustomerSourceToDestination(JSONArray routes) {
        Log.e("tracking_tracker", "120");

        ArrayList<TaxiRouteData> customertaxiRouteDataArrayList = new ArrayList<>();
        try {


            JSONObject routess = routes.getJSONObject(0);
            JSONArray legs = routess.getJSONArray("legs");

            JSONObject leggs = legs.getJSONObject(0);

            JSONObject total_distance = leggs.getJSONObject("distance");
            long tot_meter = total_distance.getLong("value");

            JSONObject total_duration = leggs.getJSONObject("duration");
            long tot_time = total_duration.getLong("value");

            JSONObject total_start_location = leggs.getJSONObject("start_location");
            String start_lat = total_start_location.getString("lat");
            String start_lng = total_start_location.getString("lng");

            TaxiRouteData taxiRouteData = new TaxiRouteData();
            taxiRouteData.setLatitude(Double.parseDouble(start_lat));
            taxiRouteData.setLongitude(Double.parseDouble(start_lng));
            taxiRouteData.setTime(String.valueOf(tot_time));
            taxiRouteData.setDistance(tot_meter);
            customertaxiRouteDataArrayList.add(taxiRouteData);

            JSONObject total_end_location = leggs.getJSONObject("end_location");
            String end_lat = total_end_location.getString("lat");
            String end_lng = total_end_location.getString("lng");
            TaxiRouteData taxiRouteData1 = new TaxiRouteData();
            taxiRouteData1.setLatitude(Double.parseDouble(end_lat));
            taxiRouteData1.setLongitude(Double.parseDouble(end_lng));
            taxiRouteData1.setTime(String.valueOf(tot_time));
            taxiRouteData1.setDistance(tot_meter);

            JSONArray steps = leggs.getJSONArray("steps");

            for (int i = 0; i < steps.length(); i++) {
                JSONObject jo_inside = steps.getJSONObject(i);

                JSONObject distance = jo_inside.getJSONObject("distance");
                JSONObject duration = jo_inside.getJSONObject("duration");

                long meter = distance.getLong("value");
                long time = duration.getLong("value");


                JSONObject destinationlocation = jo_inside.getJSONObject("end_location");
                String lat = destinationlocation.getString("lat");
                String lng = destinationlocation.getString("lng");
                TaxiRouteData taxiRouteData3 = new TaxiRouteData();
                taxiRouteData3.setLatitude(Double.parseDouble(lat));
                taxiRouteData3.setLongitude(Double.parseDouble(lng));
                taxiRouteData3.setTime(String.valueOf(time));
                taxiRouteData3.setDistance(meter);

                customertaxiRouteDataArrayList.add(taxiRouteData3);
            }
            customertaxiRouteDataArrayList.add(taxiRouteData1);
            taxiRouteDataArrayList = new ArrayList<>();
            taxiRouteDataArrayList = customertaxiRouteDataArrayList;
            resetMapRoute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<TaxiRouteData> checkDistanceTime(double latitude, double longitude) {

        Log.e("tracking_tracker", "121");

        ArrayList<TaxiRouteData> gettaxiRouteList = new ArrayList<>();
        for (TaxiRouteData taxiRouteData : taxiRouteDataArrayList) {
            if (taxiRouteData.getLatitude() == latitude || taxiRouteData.getLongitude() == longitude) {
                gettaxiRouteList.add(taxiRouteData);
                break;
            }
        }

        return gettaxiRouteList;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            pulsator.stop();
            if (booking.equalsIgnoreCase("booking")) {

                Intent intent = new Intent(TrackingActivity.this, BaseActivity.class);
                startActivity(intent);
                finish();

                Intent intenttt = new Intent(TrackingActivity.this, MyService.class);
                stopService(intenttt);

            } else {
                finish();
            }
          /*  if (screenFlag.equalsIgnoreCase(GlobalUtils.MY_BOOKING_SCREEN)) {

                Intent intent = new Intent(TrackingActivity.this, BaseActivity.class);
                startActivity(intent);
                finish();

                Intent intenttt = new Intent(TrackingActivity.this, MyService.class);
                stopService(intenttt);

            } else {
                finish();
            }*/
        } catch (Exception e) {
            pulsator.stop();
        }


    }

    public void setViewData() {
        /*if (category_type) {
         */
        Log.e("tracking_tracker", "122");

        Log.e("DISCOUNT_TXT", "txt:-- " + trackingData.getDiscount_txt() + " digit " + trackingData.getDiscount_digit_txt());
        txtvendorname.setText(trackingData.getVendorName());
        discount_text.setText(Html.fromHtml(trackingData.getDiscount_txt()));
        discount_digit_text.setText(Html.fromHtml("&#x20B9;" + trackingData.getDiscount_digit_txt()));
        servicenamerating.setText(trackingData.getArtistRating());
        if (trackingData.getCat_typemap().equals("1")) {
            totalingdata.setVisibility(View.GONE);
        } else {
            subTotal.setText("₹" + trackingData.getSubTotal());
            if (String.valueOf(trackingData.getShippingcharges()).equals("0.0") || String.valueOf(trackingData.getShippingcharges()).equals("0")) {
                shippingCharge.setText("Free");
            } else {
                shippingCharge.setText("₹" + trackingData.getShippingcharges());
            }
        }
        order_preparation_note.setText(Html.fromHtml(trackingData.getPreparationNote()));


        // dlivery_timer.setText("Timer");


        ridername.setText(trackingData.getRider_name());
        riderrating.setText(trackingData.getRider_rating());
        rider_call_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL); //use ACTION_CALL class
                callIntent.setData(Uri.parse("tel:" + trackingData.getRider_mobileno()));    //this is the phone number calling
                //check permission
                //If the device is running Android 6.0 (API level 23) and the app's targetSdkVersion is 23 or higher,
                //the system asks the user to grant approval.
                if (ActivityCompat.checkSelfPermission(TrackingActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    //request permission from user if the app hasn't got the required permission
                    ActivityCompat.requestPermissions(TrackingActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE},   //request specific permission from user
                            10);
                    return;
                } else {     //have got permission
                    try {
                        startActivity(callIntent);  //call activity and make phone call
                    } catch (android.content.ActivityNotFoundException ex) {
                    }
                }
            }
        });

        call_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + trackingData.getMobileNumber()));
                startActivity(callIntent);*/
                Intent callIntent = new Intent(Intent.ACTION_CALL); //use ACTION_CALL class
                callIntent.setData(Uri.parse("tel:" + trackingData.getMobileNumber()));    //this is the phone number calling
                //check permission
                //If the device is running Android 6.0 (API level 23) and the app's targetSdkVersion is 23 or higher,
                //the system asks the user to grant approval.
                if (ActivityCompat.checkSelfPermission(TrackingActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    //request permission from user if the app hasn't got the required permission
                    ActivityCompat.requestPermissions(TrackingActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE},   //request specific permission from user
                            10);
                    return;
                } else {     //have got permission
                    try {
                        startActivity(callIntent);  //call activity and make phone call
                    } catch (android.content.ActivityNotFoundException ex) {
                    }
                }
            }
        });


        Glide.with(this).
                load(trackingData.getVendorImage())
                .placeholder(R.drawable.dummyuser_image)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgvendonrimage);

        Glide.with(this).
                load(trackingData.getRider_image())
                .placeholder(R.drawable.dummyuser_image)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(riderimage);

        etAddressSelectsource.setText(trackingData.getSourceAddress());

        txttotalamount.setText("₹" + trackingData.getTotalAmount());

        if (!trackingData.getDestinationAddress().trim().isEmpty()) {
            etAddressSelectdesti.setText(trackingData.getDestinationAddress());

        } else {
            etAddressSelectdesti.setText(trackingData.getSourceAddress());
        }

        if (paymentType == 0) {
            txtptype.setText("Online Payment");
            txtptypemsg.setText("Relax when the ride ends");
        } else if (paymentType == 1) {
            txtptype.setText("Cash");
            txtptypemsg.setText("Pay when the ride ends");
        } else if (paymentType == 2) {
            txtptype.setText("Wallet Payment");
            txtptypemsg.setText("Relax when the ride ends");
        } else {
            txtptype.setText("");
            txtptypemsg.setText("");
        }
        tvStatus.setText(booking_msg2);


        if (bookingFlag == 0 && rider_order_tracker == 0) {

            Log.e("tracking_tracker", "1");
            Log.e("tracking_tracker1", "1");
            Glide.with(this).
                    load(R.drawable.ic_service_provider)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivStatus);
            llSt.setBackground(getDrawable(R.drawable.text_tracking_background));
            tvStatus.setTextColor(getResources().getColor(R.color.yellow_color));
            providerdata.setVisibility(View.VISIBLE);
            riderData.setVisibility(View.GONE);

            relmap.setVisibility(View.VISIBLE);
            relanimation.setVisibility(View.VISIBLE);
            laycallcancel.setVisibility(View.VISIBLE);
            share_layout.setVisibility(View.GONE);
            lay_add_refer.setVisibility(View.GONE);
            laycall.setVisibility(View.GONE);
            rel_sos.setVisibility(View.GONE);
            if (screenFlag.equalsIgnoreCase(GlobalUtils.CONFIRM_BOOKING_SCREEN)) {
                Intent intent = new Intent(this, MyService.class);
                startService(intent);
            }
            mBackgroundHandler.obtainMessage(START_COUNTER, true).sendToTarget();
        } else if (bookingFlag == 1 && rider_order_tracker == 0) {
            Log.e("tracking_tracker", "2");
            Log.e("tracking_tracker1", "2");

            providerdata.setVisibility(View.VISIBLE);
            riderData.setVisibility(View.GONE);
            Glide.with(this).
                    load(R.drawable.ic_service_provider)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivStatus);
            llSt.setBackground(getDrawable(R.drawable.text_tracking_background));
            tvStatus.setTextColor(getResources().getColor(R.color.yellow_color));
            laycallcancel.setVisibility(View.VISIBLE);
            laycall.setVisibility(View.VISIBLE);
            relanimation.setVisibility(View.GONE);
            share_layout.setVisibility(View.GONE);
            lay_add_refer.setVisibility(View.GONE);
            rel_sos.setVisibility(View.VISIBLE);
            /*runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateCarLocation(latLng);
                                }
                            });*/
            mBackgroundHandler.obtainMessage(START_COUNTER, true).sendToTarget();
            if (!isAlreadyStartedVendorToCustomer) {
                isAlreadyStartedVendorToCustomer = true;
                this.showCurvedPolyline(new LatLng(vendorLatitude, vendorLongitude), new LatLng(customerDestinationLatitude, customerDestinationLongitude), 0.5);

                // loadMapOfRoute();
            }
        } else if (bookingFlag == 0 && rider_flag == 5) {
            Log.e("tracking_tracker", "3");
            Log.e("tracking_tracker1", "3");

            call_button.setActivated(false);
            providerdata.setVisibility(View.VISIBLE);
            riderData.setVisibility(View.GONE);
            relmap.setVisibility(View.VISIBLE);
            relanimation.setVisibility(View.VISIBLE);
            laycallcancel.setVisibility(View.VISIBLE);
            share_layout.setVisibility(View.GONE);
            lay_add_refer.setVisibility(View.GONE);
            laycall.setVisibility(View.GONE);
            rel_sos.setVisibility(View.GONE);
            if (screenFlag.equalsIgnoreCase(GlobalUtils.CONFIRM_BOOKING_SCREEN)) {
                Intent intent = new Intent(this, MyService.class);
                startService(intent);
            }
            mBackgroundHandler.obtainMessage(START_COUNTER, true).sendToTarget();
        } else if (bookingFlag == 1 && rider_flag == 5) {
            Log.e("tracking_tracker", "4");
            Log.e("tracking_tracker1", "4");
            relmap.setVisibility(View.VISIBLE);
            relanimation.setVisibility(View.VISIBLE);
            laycallcancel.setVisibility(View.VISIBLE);
            share_layout.setVisibility(View.GONE);
            lay_add_refer.setVisibility(View.GONE);
            laycall.setVisibility(View.GONE);
            rel_sos.setVisibility(View.GONE);
            if (screenFlag.equalsIgnoreCase(GlobalUtils.CONFIRM_BOOKING_SCREEN)) {
                Intent intent = new Intent(this, MyService.class);
                startService(intent);
            }
            mBackgroundHandler.obtainMessage(START_COUNTER, true).sendToTarget();
        } else if (bookingFlag == 1 && rider_flag == 0) {
            Log.e("tracking_tracker", "5");
            Log.e("tracking_tracker1", "5");

            Glide.with(this).
                    load(R.drawable.ic_accepted)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivStatus);
            llSt.setBackground(getDrawable(R.drawable.text_tracking_background_green));
            tvStatus.setTextColor(getResources().getColor(R.color.green_text_color));
            call_button.setActivated(true);
            providerdata.setVisibility(View.VISIBLE);
            riderData.setVisibility(View.GONE);

            relmap.setVisibility(View.VISIBLE);
            relanimation.setVisibility(View.VISIBLE);
            laycallcancel.setVisibility(View.VISIBLE);
            share_layout.setVisibility(View.GONE);
            lay_add_refer.setVisibility(View.GONE);
            laycall.setVisibility(View.GONE);
            rel_sos.setVisibility(View.GONE);
            if (screenFlag.equalsIgnoreCase(GlobalUtils.CONFIRM_BOOKING_SCREEN)) {
                Intent intent = new Intent(this, MyService.class);
                startService(intent);
            }
            mBackgroundHandler.obtainMessage(START_COUNTER, true).sendToTarget();
        } else if (bookingFlag == 1 && rider_flag == 1) {
            Log.e("tracking_tracker", "6");
            Log.e("tracking_tracker1", "6");
            providerdata.setVisibility(View.VISIBLE);
            riderData.setVisibility(View.VISIBLE);
            Glide.with(this).
                    load(R.drawable.ic_accepted)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivStatus);
            llSt.setBackground(getDrawable(R.drawable.text_tracking_background_green));
            tvStatus.setTextColor(getResources().getColor(R.color.green_text_color));

            laycallcancel.setVisibility(View.VISIBLE);
            laycall.setVisibility(View.VISIBLE);
            relanimation.setVisibility(View.GONE);
            share_layout.setVisibility(View.GONE);
            lay_add_refer.setVisibility(View.GONE);
            rel_sos.setVisibility(View.VISIBLE);
            mBackgroundHandler.obtainMessage(START_COUNTER, true).sendToTarget();
            if (!isAlreadyStartedVendorToCustomer) {
                isAlreadyStartedVendorToCustomer = true;
                this.showCurvedPolyline(new LatLng(vendorLatitude, vendorLongitude), new LatLng(customerDestinationLatitude, customerDestinationLongitude), 0.5);

//                loadMapOfRoute();
            }
        } else if (bookingFlag == 2) {
            Log.e("tracking_tracker", "7");
            Log.e("tracking_tracker1", "7");
            rel_sos.setVisibility(View.GONE);
            laycallcancel.setVisibility(View.GONE);
            laycall.setVisibility(View.GONE);
            share_layout.setVisibility(View.GONE);
            lay_add_refer.setVisibility(View.GONE);
        } else if (bookingFlag == 3 && rider_flag == 1) {
            Log.e("tracking_tracker", "8");
            Log.e("tracking_tracker1", "8");
            providerdata.setVisibility(View.VISIBLE);
            riderData.setVisibility(View.VISIBLE);
            Glide.with(this).
                    load(R.drawable.ic_service_provider_blue)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivStatus);
            llSt.setBackground(getDrawable(R.drawable.text_tracking_background_blue));
            tvStatus.setTextColor(getResources().getColor(R.color.blue_text_color));
            isStartBooking = true;
            relanimation.setVisibility(View.GONE);
            laycallcancel.setVisibility(View.GONE);
            laycancel.setVisibility(View.GONE);
            rel_sos.setVisibility(View.VISIBLE);
            laycall.setVisibility(View.GONE);
            share_layout.setVisibility(View.VISIBLE);
            lay_add_refer.setVisibility(View.VISIBLE);

            if (templatitude != latitute || templongitude != longi) {
                setupMarkerWithTimeDistance(latitute, longi, delatitute, delongitiute);
                templatitude = latitute;
                templongitude = longi;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateCarLocation(new LatLng(latitute, longi));
                    }
                });
            }

            if (!isAlreadyStartedCustomerToDestination) {
                if (checkMapCategory(category_id)) {
                    getBookingRoute();
                } else {
                    mBackgroundHandler.obtainMessage(START_COUNTER, true).sendToTarget();
                }
            } else {
                mBackgroundHandler.obtainMessage(START_COUNTER, true).sendToTarget();
            }


        } else if (bookingFlag == 3) {
            Log.e("tracking_tracker", "9");
            Log.e("tracking_tracker1", "9");
            providerdata.setVisibility(View.VISIBLE);

            llSt.setBackground(getDrawable(R.drawable.text_tracking_background_blue));
            tvStatus.setTextColor(getResources().getColor(R.color.blue_text_color));
            isStartBooking = true;
            relanimation.setVisibility(View.GONE);
            laycallcancel.setVisibility(View.GONE);
            rel_sos.setVisibility(View.VISIBLE);
            laycall.setVisibility(View.GONE);
            share_layout.setVisibility(View.VISIBLE);
            lay_add_refer.setVisibility(View.VISIBLE);
            if (!isAlreadyStartedCustomerToDestination) {
                if (checkMapCategory(category_id)) {
                    getBookingRoute();
                } else {
                    mBackgroundHandler.obtainMessage(START_COUNTER, true).sendToTarget();
                }
            } else {
                mBackgroundHandler.obtainMessage(START_COUNTER, true).sendToTarget();
            }
        } else if (bookingFlag == 4) {
            Log.e("tracking_tracker", "10");
            Log.e("tracking_tracker1", "10");
            providerdata.setVisibility(View.GONE);
            riderData.setVisibility(View.VISIBLE);
            Glide.with(this).
                    load(R.drawable.ic_service_provider)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivStatus);
            llSt.setBackground(getDrawable(R.drawable.text_tracking_background_blue));
            tvStatus.setTextColor(getResources().getColor(R.color.blue_text_color));
            mBackgroundHandler.obtainMessage(FINISH_COUNTER, true).sendToTarget();
        }

        if (booking_msg.equals("Waiting for service provider response")) {
            Log.e("tracking_tracker", "11");
            Log.e("tracking_tracker1", "11");

            call_button_disabled.setVisibility(View.VISIBLE);
            call_button.setVisibility(View.GONE);
            Glide.with(this).
                    load(R.drawable.ic_service_provider)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivStatus);
        }
        if (booking_msg.equals("Request Accepted By Service Provider")) {
            Log.e("tracking_tracker", "12");
            Log.e("tracking_tracker1", "12");
            llSt.setBackground(getDrawable(R.drawable.text_tracking_background_green));
            tvStatus.setTextColor(getResources().getColor(R.color.green_text_color));
            Glide.with(this).
                    load(R.drawable.ic_accepted)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivStatus);
            call_button_disabled.setVisibility(View.GONE);
            call_button.setVisibility(View.VISIBLE);
        }
        if (booking_msg.equals("Request Accepted By Rider")) {
            Log.e("tracking_tracker", "13");
            Log.e("tracking_tracker1", "13");
            booking_msg = "Request Accepted By Service Provider";
            Glide.with(this).
                    load(R.drawable.ic_accepted)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivStatus);
            call_button_disabled.setVisibility(View.GONE);
            call_button.setVisibility(View.VISIBLE);
            rider_call_button_disabled.setVisibility(View.VISIBLE);
            rider_call_button.setVisibility(View.GONE);

           /* runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateCarLocation(new LatLng(latitute,longi));
                }
            });*/
        }
//        if (booking_msg.equalsIgnoreCase(""))
        if (booking_msg.equals("Preparing Your Order") || booking_msg.equals("Request Accepted By Rider")) {
            Log.e("tracking_tracker", "14");
            Log.e("tracking_tracker1", "14");
            Glide.with(this).
                    load(R.drawable.ic_accepted_blue)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivStatus);
            call_button_disabled.setVisibility(View.GONE);
            call_button.setVisibility(View.VISIBLE);
            rider_call_button_disabled.setVisibility(View.GONE);
            rider_call_button.setVisibility(View.VISIBLE);
        }
        if (booking_msg.equals("Waiting For Rider Response")) {
            Log.e("tracking_tracker", "15");
            Log.e("tracking_tracker1", "15");
            Glide.with(this).
                    load(R.drawable.ic_service_provider_blue)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivStatus);
            call_button_disabled.setVisibility(View.GONE);
            laycallcancel.setVisibility(View.GONE);
            riderData.setVisibility(View.GONE);
            call_button.setVisibility(View.VISIBLE);
            rider_call_button_disabled.setVisibility(View.VISIBLE);
            rider_call_button.setVisibility(View.GONE);
        }
        if (booking_msg.equals("Order Has Handover To Rider")) {
            Log.e("tracking_tracker", "16");
            Log.e("tracking_tracker1", "16");
            llSt.setBackground(getDrawable(R.drawable.text_tracking_background_pink));
            tvStatus.setTextColor(getResources().getColor(R.color.pink_text_color));
            Glide.with(this).
                    load(R.drawable.ic_hand_over_to_rider)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivStatus);
            providerdata.setVisibility(View.GONE);
            lay_add_refer.setVisibility(View.VISIBLE);
            laycancel.setVisibility(View.GONE);
            rider_call_button_disabled.setVisibility(View.GONE);
            rider_call_button.setVisibility(View.VISIBLE);
        }
        if (booking_msg.equals("Rider is On the way")) {
            Log.e("tracking_tracker", "17");
            Log.e("tracking_tracker1", "17");
            providerdata.setVisibility(View.GONE);
            llSt.setBackground(getDrawable(R.drawable.text_tracking_background_pink));
            tvStatus.setTextColor(getResources().getColor(R.color.pink_text_color));
            Glide.with(this).
                    load(R.drawable.ic_hand_over_to_rider)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivStatus);
            lay_add_refer.setVisibility(View.VISIBLE);
            laycancel.setVisibility(View.GONE);
            rider_call_button_disabled.setVisibility(View.GONE);
            rider_call_button.setVisibility(View.VISIBLE);
            if (templatitude != latitute || templongitude != longi) {
                setupMarkerWithTimeDistance(latitute, longi, delatitute, delongitiute);
                templatitude = latitute;
                templongitude = longi;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateCarLocation(new LatLng(latitute, longi));
                    }
                });
            }
        }
        if (booking_msg.equals("Your Booking In Progress")) {
            Log.e("tracking_tracker", "18");
            Log.e("tracking_tracker1", "18");
            Glide.with(this).
                    load(R.drawable.ic_service_provider_blue)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivStatus);
            lay_add_refer.setVisibility(View.VISIBLE);
            laycancel.setVisibility(View.GONE);
            rider_call_button_disabled.setVisibility(View.GONE);
            rider_call_button.setVisibility(View.VISIBLE);
        }
        if (booking_msg.isEmpty()) {
            Log.e("tracking_tracker", "19");
            Log.e("tracking_tracker1", "19");
            laycancel.setVisibility(View.GONE);
            relcancel.setVisibility(View.VISIBLE);
            llSt.setBackground(getDrawable(R.drawable.text_tracking_background_red));
            tvStatus.setTextColor(getResources().getColor(R.color.red));
            tvStatus.setText("Order Cancelled");
            ivStatus.setVisibility(View.VISIBLE);
            Glide.with(this).
                    load(R.drawable.ic_cancel)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivStatus);
            order_preparation_note.setVisibility(View.GONE);
        }
    }

    public void resetMapRoute() {
        Log.e("tracking_tracker", "123");

        double longitiute = Double.parseDouble(prefrence.getValue(Consts.LONGITUDE));
        double latitute = Double.parseDouble(prefrence.getValue(Consts.LATITUDE));

        defaultLocation = new LatLng(latitute, longitiute);
        showDefaultLocationOnMap(defaultLocation);
        if (taxiRouteDataArrayList.size() > 0) {
            totalkm = decimalFormat.format(taxiRouteDataArrayList.get(0).getDistance() / 1000);
            int hours = getHours(Long.parseLong(taxiRouteDataArrayList.get(0).getTime()));
            int min = getMinitues(Long.parseLong(taxiRouteDataArrayList.get(0).getTime()));
            if (hours > 0) {
                totaltime = String.format("%02d Hours %02d Min", hours, min);
            } else {
                totaltime = String.format("%02d Min", min);
            }

        } else {
            totalkm = "0";
            totaltime = "0 Min";
        }

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(defaultLocation)
                .title(sourceAddress)
                .snippet(destinationAddress)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_bg));

        InfoWindowData info = new InfoWindowData();
        info.setImage("snowqualmie");
        info.setKm(totalkm + " KM");
        info.setTime(totaltime + "");
        info.setTransport("Destination Address :" + destinationAddress);

        CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(this);
        this.googleMap.setInfoWindowAdapter(customInfoWindow);

        Marker m = this.googleMap.addMarker(markerOptions);
        m.setTag(info);
        //     m.showInfoWindow();
        this.googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker) {

                return true;
            }
        });
        this.googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //mMarker is the shown marker
                if (m != null) {

                    //            m.showInfoWindow();
                }

            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isAlreadyStartedCustomerToDestination = true;
                //  showRouteCustomerToDestinationPath();
                showLiveMovingCab();
                mBackgroundHandler.obtainMessage(START_COUNTER, true).sendToTarget();
            }
        }, 1000);
    }

    @SuppressLint("NewApi")
    private void showCurvedPolyline(LatLng p1, LatLng p2, double k) {

        Log.e("tracking_tracker", "1200");

        //Calculate distance and heading between two points
        double d = SphericalUtil.computeDistanceBetween(p1, p2);
        double h = SphericalUtil.computeHeading(p1, p2);

        //Midpoint position
        LatLng p = SphericalUtil.computeOffset(p1, d * 0.5, h);

        //Apply some mathematics to calculate position of the circle center
        double x = (1 - k * k) * d * 0.5 / (2 * k);
        double r = (1 + k * k) * d * 0.5 / (2 * k);

        LatLng c = SphericalUtil.computeOffset(p, x, h + 90.0);

        //Polyline options

            options = new PolylineOptions();

        List<PatternItem> pattern = Arrays.<PatternItem>asList(new Dash(30), new Gap(20));

        //Calculate heading between circle center and two points
        double h1 = SphericalUtil.computeHeading(c, p1);
        double h2 = SphericalUtil.computeHeading(c, p2);

        //Calculate positions of points on circle border and add them to polyline options
        int numpoints = 100;
        double step = (h2 - h1) / numpoints;

        for (int i = 0; i < numpoints; i++) {
            LatLng pi = SphericalUtil.computeOffset(c, r, h1 + i * step);
            options.add(pi);
        }

        //Draw polyline
        googleMap.addPolyline(options.width(5).color(getColor(R.color.dark_blue_color)).geodesic(false).pattern(pattern));


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isAlreadyStartedCustomerToDestination = true;
                //  showRouteCustomerToDestinationPath();
                showLiveMovingCab();
                mBackgroundHandler.obtainMessage(START_COUNTER, true).sendToTarget();
            }
        }, 1000);
    }


    public void loadMapOfRoute() {
        Log.e("tracking_tracker", "124");

        double longitiute = Double.parseDouble(prefrence.getValue(Consts.LONGITUDE));
        double latitute = Double.parseDouble(prefrence.getValue(Consts.LATITUDE));

        defaultLocation = new LatLng(latitute, longitiute);
        showDefaultLocationOnMap(defaultLocation);

        taxiRouteDataArrayList = (ArrayList<TaxiRouteData>) prefrence.getRouteUser(String.valueOf(bookingId));
        if (taxiRouteDataArrayList.size() > 0) {
            totalkm = decimalFormat.format(taxiRouteDataArrayList.get(0).getDistance() / 1000);
            int hours = getHours(Long.parseLong(taxiRouteDataArrayList.get(0).getTime()));
            int min = getMinitues(Long.parseLong(taxiRouteDataArrayList.get(0).getTime()));
            if (hours > 0) {
                totaltime = String.format("%02d Hours %02d Min", hours, min);
            } else {
                totaltime = String.format("%02d Min", min);
            }
        } else {
            totalkm = "0";
            totaltime = "0 Min";
        }
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(defaultLocation)
                .title(sourceAddress)
                .snippet(destinationAddress)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_bg));

        InfoWindowData info = new InfoWindowData();
        info.setImage("snowqualmie");
        info.setKm(totalkm + " KM");
        info.setTime(totaltime + "");
        info.setTransport("Destination Address :" + destinationAddress);

        CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(this);
        this.googleMap.setInfoWindowAdapter(customInfoWindow);

        Marker m = this.googleMap.addMarker(markerOptions);
        m.setTag(info);
        //   m.showInfoWindow();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                taxiRouteDataArrayList = (ArrayList<TaxiRouteData>) prefrence.getRouteUser(String.valueOf(bookingId));
                //showRouteVendorToCustomerPath();
                showLiveMovingCab();
            }
        }, 3000);

        this.googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker) {

                return true;
            }
        });
        this.googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //mMarker is the shown marker
                if (m != null) {

                    //      m.showInfoWindow();
                }
            }
        });


    }

    public boolean checkMapCategory(String catid) {
        boolean value = false;
        for (int i = 0; i < BaseActivity.addressModelArrayList.size(); i++) {
            if (BaseActivity.addressModelArrayList.get(i).getCat_id().equalsIgnoreCase(catid)) {
                value = true;
                break;
            } else {
                value = false;

            }

        }
        return value;
    }

    public Animation getBlinkAnimation() {
        Animation animation = new AlphaAnimation(1, 0);         // Change alpha from fully visible to invisible
        animation.setDuration(2000);                             // duration - half a second
        animation.setInterpolator(new LinearInterpolator());    // do not alter animation rate
        animation.setRepeatCount(Animation.INFINITE);                            // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE);             // Reverse animation at the end so the button will fade back in
        return animation;
    }

    public int getMinitues(long totalTime) {
        return (int) ((totalTime % 3600) / 60);
    }

    public int getHours(long totalTime) {
        return (int) (totalTime / 3600);
    }

    private static double distance_in_meter(final double lat1, final double lon1, final double lat2, final double lon2) {
        double R = 6371000f; // Radius of the earth in m
        double dLat = (lat1 - lat2) * Math.PI / 180f;
        double dLon = (lon1 - lon2) * Math.PI / 180f;
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1 * Math.PI / 180f) * Math.cos(lat2 * Math.PI / 180f) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2f * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c;
        return d;
    }

    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return meter;
    }

    public void setupMarkerWithTimeDistance(double latitute, double longi, double delatitute, double delongitiute) {
        Log.e("tracking_tracker", "125");

        double excatelocationdisctance = 0.0;
        if (isAlreadyStartedCustomerToDestination) {
            Log.e("tracking_tracker", "126");

            Log.e("customerlatlong", " 2 " + customerDestinationLatitude + " longi " + customerDestinationLongitude);

            excatelocationdisctance = distance_in_meter(latitute, longi, customerDestinationLatitude, customerDestinationLongitude);
        } else {
            Log.e("tracking_tracker", "127");

            excatelocationdisctance = distance_in_meter(latitute, longi, delatitute, delongitiute);

        }
        LatLng latLnglo = new LatLng(delatitute, delongitiute);
        MarkerOptions markerOptions = new MarkerOptions();

        markerOptions.position(latLnglo)
                .title(sourceAddress)
                .snippet(destinationAddress)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_bg));


        int hours = 0;
        int minutes = 0;
        long LeftTimeInMiniute = TimeFormula(excatelocationdisctance);
        hours = getHours(LeftTimeInMiniute); //since both are ints, you get an int
        minutes = getMinitues(LeftTimeInMiniute);
        String TimeDuration = "";
        if (hours > 0) {
            TimeDuration = String.format("%02d Hours %02d Min", hours, minutes);
        } else {
            TimeDuration = String.format("%02d Min", minutes);
        }
        InfoWindowData info = new InfoWindowData();
        info.setImage("snowqualmie");
        info.setKm(decimalFormat.format(excatelocationdisctance / 1000) + " KM");
        info.setTime(TimeDuration);
        info.setTransport("Destination Address :" + destinationAddress);

        CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(TrackingActivity.this);
        googleMap.setInfoWindowAdapter(customInfoWindow);

        Marker m = googleMap.addMarker(markerOptions);
        m.setTag(info);
        //  m.showInfoWindow();
        this.googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker) {

                return true;
            }
        });
        this.googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //mMarker is the shown marker
                if (m != null) {

                    //    m.showInfoWindow();
                }

            }
        });
    }

    public long TimeFormula(double leftDistance) {
        Log.e("tracking_tracker", "128");

        double duration = 0, takenTime = 0;
        long howMuchTime = 0;
        if (taxiRouteDataArrayList.size() > 0) {
            duration = taxiRouteDataArrayList.get(0).getDistance();
            takenTime = Long.parseLong(taxiRouteDataArrayList.get(0).getTime());
            howMuchTime = (long) ((leftDistance * takenTime) / duration);
        }
        return howMuchTime;

    }


}

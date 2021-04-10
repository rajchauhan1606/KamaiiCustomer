package com.kamaii.customer.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.kamaii.customer.DTO.ArtistDetailsDTO;
import com.kamaii.customer.DTO.ProductDTO;
import com.kamaii.customer.DTO.ShippingDTO;
import com.kamaii.customer.DTO.UserBooking;
import com.kamaii.customer.DTO.UserDTO;
import com.kamaii.customer.R;
import com.kamaii.customer.api.apiClient;
import com.kamaii.customer.api.apiRest;
import com.kamaii.customer.databinding.ActivityBookingProductBinding;
import com.kamaii.customer.https.HttpsRequest;
import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.interfacess.Helper;
import com.kamaii.customer.interfacess.OnAddressSelected;
import com.kamaii.customer.interfacess.OnSelectedItemListener;
import com.kamaii.customer.interfacess.SetonItemPlusListener;
import com.kamaii.customer.network.NetworkManager;
import com.kamaii.customer.preferences.SharedPrefrence;
import com.kamaii.customer.ui.adapter.AdapterServices;
import com.kamaii.customer.ui.adapter.AdapterServicesNew;
import com.kamaii.customer.ui.adapter.AddressBottomSheetAdapter;
import com.kamaii.customer.ui.adapter.CartSuggestionAdapter;
import com.kamaii.customer.ui.adapter.CustomArrayAdapter2;
import com.kamaii.customer.ui.adapter.PaymentBottomDialogadapter;
import com.kamaii.customer.ui.models.CartModel;
import com.kamaii.customer.ui.models.SubCateModel;
import com.kamaii.customer.ui.models.TypeAddressModel;
import com.kamaii.customer.utils.CustomEditText;
import com.kamaii.customer.utils.CustomTextView;
import com.kamaii.customer.utils.CustomTextViewBold;
import com.kamaii.customer.utils.ExpandableHeightGridView;
import com.kamaii.customer.utils.GlobalUtils;
import com.kamaii.customer.utils.ProjectUtils;
import com.paykun.sdk.PaykunApiCall;
import com.paykun.sdk.eventbus.Events;
import com.paykun.sdk.eventbus.GlobalBus;
import com.paykun.sdk.helper.PaykunHelper;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.security.Permission;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.kamaii.customer.interfacess.Consts.BOOKING;
import static com.kamaii.customer.interfacess.Consts.LATITUDE;
import static com.kamaii.customer.interfacess.Consts.LONGITUDE;
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


public class BookingProduct extends AppCompatActivity implements OnAddressSelected {
    private ArrayList<ProductDTO> productDTOListtemp;
    private JsonArray arrayprice;

    String Tag = BookingProduct.class.getSimpleName();
    private ActivityBookingProductBinding binding;
    AddressBottomSheetAdapter dialogadapter;
    private Context mContext;
    private ArrayList<ProductDTO> productDTOList;

    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private ArtistDetailsDTO artistDetailsDTO;
    private Date date;
    String device_token = "";
    private SimpleDateFormat sdf1, timeZone;
    private HashMap<String, String> paramsBookingOp = new HashMap<>();
    private HashMap<String, String> paramsBookingOp2 = new HashMap<>();
    private HashMap<String, String> paramsBookingOp3 = new HashMap<>();
    private String TAG = BookingProduct.class.getSimpleName();
    private JsonArray array, arrayp, arrayquantitydays, arrayquantityvalue;
    private String artist_id = "", location_status = "1", changes_price = "";
    private int screen_tag = 0;
    private RadioGroup radiogroup;
    List<String> paymentList;
    RadioButton rb;
    RecyclerView rv_cart;
    ExpandableHeightGridView rv_cart2;
    public static ArrayList<ProductDTO> cartarraylist = new ArrayList<>();
    private GridLayoutManager gridLayoutManager;
    double total = 0;
    double sub_total = 0;
    String sub_totaln = "0";
    public double cab_total = 0;
    public double cab_sub_total = 0;
    public double cab_allowence = 0;
    int radiopos = 1;
    RadioButton radioslocation, radioylocation;
    private HashMap<String, String> parmsshipping = new HashMap<>();
    ShippingDTO shippingDTO;
    private ArrayList<ShippingDTO> shippingDTOArrayList = new ArrayList<>();
    LinearLayout llerrormsg;
    CardView rider_error;
    public CustomTextViewBold more_items_txt_title, service_rupee_symbol, txtdestinationaddress, tverror, tverror1, sub_total_digits, sub_total_rupee_symbol;
    int error;
    String maxprice = "0", destaddress = "", km = "", destitime = "", Sourceaddress = "";
    public CustomTextView discounted_total_digits, discounted_rupee_symbol, slot_note;
    LinearLayout laydestination;
    private ArrayList<UserBooking> userBookingList;
    public String ptype = "";
    private String paystatus = "0";
    private SharedPreferences firebase;
    private String amt = "", final_amount = "";
    private String currency = "";
    private HashMap<String, String> parmsGetWallet = new HashMap<>();
    private static int AUTOCOMPLETE_REQUEST_CODE = 1;
    String final_user_address = "";
    String landmark_name = "";
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1000;
    String rider_name = "";
    String track = "";
    String rider1_id = "";
    String service_charge_txt = "";
    String servce_charge_tracker = "";
    String rider1_lat = "";
    String rider1_long = "";
    String total_rider_charges = "";
    HashMap<String, String> cartHashmap;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    public CustomTextViewBold spinner_txt, order_now_text;
    public RelativeLayout service_charge_relative;
    CustomTextView booking_date, address;
    CustomTextViewBold tvHeader, order_preparation_note;
    CustomTextView order_shipping_note;
    RadioGroup addressradioGroup;
    String address_radio_txt = "Home";
    ArrayList<TypeAddressModel> typeAddressList = new ArrayList<>();
    List<Place.Field> fields = Arrays.asList(Place.Field.LAT_LNG, Place.Field.ADDRESS, Place.Field.NAME);
    private static final String API_KEY = "AIzaSyCIpO2XuPQr8HDOg92EU0pRkP0G9rKvFoY";
    BottomSheetDialog addressBottomSheet;
    CustomTextView no_rider_found_txt, change_address_btn;
    CustomTextViewBold address_submit;
    CustomEditText location_etx, house_no_etx, society_name_etx;
    String house_no_str = "", society_name_str = "", street_address_str = "";
    LinearLayout customAddress;
    LinearLayout address_lay_dest_location;
    RelativeLayout address_relative;
    int address_adapter_counter = 0;
    HashMap<String, String> addAddressHashmap = new HashMap<>();
    HashMap<String, String> deleteAddressHashmap = new HashMap<>();
    HashMap<String, String> getAddressHashmap = new HashMap<>();
    int current_stage = 0;
    ProgressDialog progressDialog;
    private String address_id = "";
    private HashMap<String, String> paramsBookingOp4 = new HashMap<>();
    int count = 0;
    //   boolean back_btn_flag = false;
    private ArrayList<SubCateModel> subcategoryDTOList;
    LinearLayout suggestionLying;
    private RecyclerView rvSuggestion;
    CartSuggestionAdapter suggestionAdapter;
    boolean from_repeatorder = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_booking_product);
        mContext = BookingProduct.this;
        firebase = this.getSharedPreferences("MyPrefs", MODE_PRIVATE);
        Places.initialize(getApplicationContext(), API_KEY);
        slot_note = findViewById(R.id.slot_note);
        spinner_txt = findViewById(R.id.spinner_txt);
        suggestionLying = findViewById(R.id.suggestionLying);
        prefrence = SharedPrefrence.getInstance(mContext);
        device_token = prefrence.getValue(Consts.DEVICE_TOKEN);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        sdf1 = new SimpleDateFormat(Consts.DATE_FORMATE_SERVER, Locale.ENGLISH);
        timeZone = new SimpleDateFormat(Consts.DATE_FORMATE_TIMEZONE, Locale.ENGLISH);
        date = new Date();
        service_charge_relative = findViewById(R.id.service_charge_relative);
        parmsGetWallet.put(Consts.USER_ID, userDTO.getUser_id());
        paramsBookingOp.put(Consts.DATE_STRING, sdf1.format(date).toString().toUpperCase());
        paramsBookingOp.put(Consts.TIMEZONE, timeZone.format(date));
        order_shipping_note = findViewById(R.id.order_shipping_note);
        order_preparation_note = findViewById(R.id.order_preparation_note);
        radiogroup = findViewById(R.id.radiogroup);
        rv_cart = findViewById(R.id.rv_cart);
        rv_cart2 = findViewById(R.id.rv_cart2);
        radioslocation = findViewById(R.id.radioslocation);
        radioylocation = findViewById(R.id.radioylocation);
        llerrormsg = findViewById(R.id.llerrormsg);
        rider_error = findViewById(R.id.rider_error);
        sub_total_digits = findViewById(R.id.sub_total_digits);
        sub_total_rupee_symbol = findViewById(R.id.sub_total_rupee_symbol);
        discounted_total_digits = findViewById(R.id.discounted_total_digits);
        discounted_rupee_symbol = findViewById(R.id.discounted_rupee_symbol);
        txtdestinationaddress = findViewById(R.id.txtdestinationaddress);
        more_items_txt_title = findViewById(R.id.more_items_txt_title);
        service_rupee_symbol = findViewById(R.id.service_rupee_symbol);
        laydestination = findViewById(R.id.laydestination);
        order_now_text = findViewById(R.id.order_now_text);
        booking_date = findViewById(R.id.booking_date);
        address = findViewById(R.id.address);
        tvHeader = findViewById(R.id.tvHeader);
        rvSuggestion = findViewById(R.id.rvSuggestion);
        cartHashmap = new HashMap<>();
        paymentList = new ArrayList<>();
        binding.cbwallet.setTextColor(getResources().getColor(R.color.gray_wallet));
        Typeface font2 = Typeface.createFromAsset(BookingProduct.this.getAssets(), "Poppins-Regular.otf");
        binding.houseNoEtx.setTypeface(font2);
        binding.societNameEtx.setTypeface(font2);
        getWallet();
        order_shipping_note.setVisibility(View.VISIBLE);

        binding.paymentLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPaymentDialog(spinner_txt.getText().toString());
            }
        });
        if (SubCategoryActivity.categoryid.equalsIgnoreCase("1")) {//Loan
            order_now_text.setText("Apply Now ");
            booking_date.setText("Order Date");
            address.setText("Delivery Order");
            tvHeader.setText("My Order Summary");
        } else if (SubCategoryActivity.categoryid.equalsIgnoreCase("2")) { //Credit Card
            order_now_text.setText("Apply Now ");
            booking_date.setText("Order Date");
            address.setText("Delivery Order");
            tvHeader.setText("My Order Summary");
        } else if (SubCategoryActivity.categoryid.equalsIgnoreCase("82")) { //Grocery-Kirana
            order_now_text.setText("Order Now ");

            booking_date.setText("Order Date");
            address.setText("Delivery Order");
            tvHeader.setText("My Order Summary");
        } else if (SubCategoryActivity.categoryid.equalsIgnoreCase("65")) { //Insaunence
            order_now_text.setText("Buy Now ");
            booking_date.setText("Order Date");
            address.setText("Delivery Order");
            tvHeader.setText("My Order Summary");
        } else if (SubCategoryActivity.categoryid.equalsIgnoreCase("79")) { //Furniture

            order_now_text.setText("Order Now ");
            booking_date.setText("Order Date");
            address.setText("Delivery Order");
            tvHeader.setText("My Order Summary");
        } else if (SubCategoryActivity.categoryid.equalsIgnoreCase("87")) { // Mobile

            order_now_text.setText("Order Now ");

            booking_date.setText("Order Date");
            address.setText("Delivery Order");
            tvHeader.setText("My Order Summary");
        } else if (SubCategoryActivity.categoryid.equalsIgnoreCase("95")) { //Tailor

            order_now_text.setText("Order Now ");

            booking_date.setText("Order Date");
            address.setText("Delivery Order");
            tvHeader.setText("My Order Summary");
        } else if (SubCategoryActivity.categoryid.equalsIgnoreCase("67")) { //Job
            order_now_text.setText("Apply Now ");
            booking_date.setText("Order Date");
            address.setText("Delivery Order");
            tvHeader.setText("My Order Summary");
        } else if (SubCategoryActivity.categoryid.equalsIgnoreCase("91")) { //Dry-Snack

            order_now_text.setText("Order Now ");

            booking_date.setText("Order Date");
            address.setText("Delivery Order");
            tvHeader.setText("My Order Summary");
        } else if (SubCategoryActivity.categoryid.equalsIgnoreCase("85")) { //Cab
            order_now_text.setText("Book Now ");
            booking_date.setText("Booking Date");
            address.setText("Booking Address");
            tvHeader.setText("My Booking Summary");
        } else {
            order_now_text.setText("Order Now ");
            booking_date.setText("Order Date");
            address.setText("Delivery Order");
            tvHeader.setText("My Order Summary");
        }

        tverror = findViewById(R.id.tverror);
        tverror1 = findViewById(R.id.tverror1);

        getpaymenttype();

        gridLayoutManager = new GridLayoutManager(mContext, 1);
        if (getIntent().hasExtra(Consts.ARTIST_DTO)) {
            artistDetailsDTO = (ArtistDetailsDTO) getIntent().getSerializableExtra(Consts.ARTIST_DTO);
            artist_id = getIntent().getStringExtra(Consts.ARTIST_ID);
            changes_price = getIntent().getStringExtra(Consts.CHANGE_PRICE);
            cartHashmap = (HashMap<String, String>) getIntent().getSerializableExtra("cartHashmap");
            screen_tag = getIntent().getIntExtra(Consts.SCREEN_TAG, 0);
            if (screen_tag == 2) {
                Bundle b = getIntent().getExtras();
                cartarraylist = (ArrayList<ProductDTO>) b.getSerializable(Consts.CARTDATA);
                if (SubCategoryActivity.categoryid.equalsIgnoreCase("85") || SubCategoryActivity.categoryid.equalsIgnoreCase("74")) {//Loan
                    rv_cart2.setVisibility(View.GONE);
                    /*Bundle b = getIntent().getExtras();
                    cartarraylist = (ArrayList<ProductDTO>) b.getSerializable(Consts.CARTDATA);*/
                    setdisplay(true);
                    bookArtist2("1");
                } else {
                    rv_cart.setVisibility(View.GONE);
                    /*Bundle b = getIntent().getExtras();
                    cartarraylist = (ArrayList<ProductDTO>) b.getSerializable(Consts.CARTDATA);*/
                    setdisplay(true);
                    getArtist();
                }
            }
            if (checkarss(ViewAddressActivity.catid)) {
                if (!ViewAddressActivity.mainkm.equalsIgnoreCase("")) {
                    if (ViewAddressActivity.sub_category_idd.equalsIgnoreCase("242")) {
                        binding.tvAddress.setEnabled(false);
                        laydestination.setVisibility(View.GONE);
                        getsourceaddress();
                    } else {
                        binding.tvAddress.setEnabled(false);
                        laydestination.setVisibility(View.VISIBLE);
                        destaddress = ViewAddressActivity.Destiaddress;
                        Sourceaddress = ViewAddressActivity.Sourceaddress;
                        km = ViewAddressActivity.mainkm;
                        destitime = ViewAddressActivity.destitime;
                        txtdestinationaddress.setText(destaddress);
                        binding.tvAddress.setText(Sourceaddress);

                    }


                }
            } else {
                ViewAddressActivity.startLati = Double.parseDouble(prefrence.getValue(LATITUDE));
                ViewAddressActivity.startLongi = Double.parseDouble(prefrence.getValue(LONGITUDE));
                binding.tvAddress.setEnabled(true);
                binding.tvAddress.setTextColor(getResources().getColor(R.color.pink_text_color));
                binding.tvAddress.setText(getResources().getString(R.string.tap_to_select_address));
                getsourceaddress();
            }
            totalservicecharge();
            setUiAction();

        }

        else {
            Log.e("GET_ARTIST", "123");
            binding.tvAddress.setText(Html.fromHtml("<font color='#e0215a'>" + getResources().getString(R.string.tap_to_select_address) + "</font>"));
            from_repeatorder = getIntent().getBooleanExtra("booking_flag",false);

            if (from_repeatorder){
                screen_tag = 2;
            }
            getArtist();
            setUiAction();

        }
        getAddress();

        getshipping();


        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {
                    if (rb.getText().toString().equalsIgnoreCase("Takeway")) {
                        order_shipping_note.setVisibility(View.GONE);
                        error = 0;
                        llerrormsg.setVisibility(View.GONE);
                        binding.llTime.setVisibility(View.VISIBLE);
                        binding.llDate.setVisibility(View.VISIBLE);
                        binding.serviceChargeRelative.setVisibility(View.GONE);
                        binding.subtotalRelative.setVisibility(View.GONE);
                        for (int i = 0; i < shippingDTOArrayList.size(); i++) {
                            if (shippingDTOArrayList.get(i).getSub_cat_id().equalsIgnoreCase(ViewAddressActivity.sub_category_idd)) {

                                if (shippingDTOArrayList.get(i).getMy_location().equalsIgnoreCase("0")) {
                                    error = 0;
                                    llerrormsg.setVisibility(View.VISIBLE);
                                    binding.llTime.setVisibility(View.GONE);
                                    binding.laydestination.setVisibility(View.GONE);
                                    binding.llDate.setVisibility(View.GONE);
                                    tverror.setText("Service Provider Location this time not available so please choose My Location");
                                }
                            }
                        }

                        radioslocation.setBackground(getResources().getDrawable(R.drawable.radio_btn_selected));
                        radioylocation.setBackground(getResources().getDrawable(R.drawable.linear_background_gray));
                        radiopos = 0;

                        if (SubCategoryActivity.categoryid.equalsIgnoreCase("85") || SubCategoryActivity.categoryid.equalsIgnoreCase("74")) {//Loan
                            total();
                        } else {
                            binding.tvPrice.setText(String.valueOf(sub_totaln));
                            binding.totalBtnTxt.setText(Html.fromHtml("&#x20B9;" + sub_totaln));
                            findViewById(R.id.service_charge_relative).setVisibility(View.GONE);
                        }
                        location_status = "0";

                        setdisplay(false);

                        binding.tvAddress.setEnabled(false);

                        binding.tvAddress.setText(Html.fromHtml("<font color='#e0215a'>" + getResources().getString(R.string.tap_to_select_address) + "</font>"));
                        getsourceaddress();

                    } else {
                        error = 0;
                        order_shipping_note.setVisibility(View.VISIBLE);
                        llerrormsg.setVisibility(View.GONE);
                        binding.llTime.setVisibility(View.VISIBLE);
                        binding.llDate.setVisibility(View.VISIBLE);

                        if (SubCategoryActivity.categoryid.equalsIgnoreCase("85") || SubCategoryActivity.categoryid.equalsIgnoreCase("74")) {//Loan
                            bookArtist2("1");
                        } else {
                            getArtist();
                        }

                        binding.serviceChargeRelative.setVisibility(View.VISIBLE);

                        binding.totalDiscountRelative.setVisibility(View.VISIBLE);
                        binding.subtotalRelative.setVisibility(View.VISIBLE);
                        for (int i = 0; i < shippingDTOArrayList.size(); i++) {
                            if (shippingDTOArrayList.get(i).getSub_cat_id().equalsIgnoreCase(ViewAddressActivity.sub_category_idd)) {

                                if (shippingDTOArrayList.get(i).getMy_location().equalsIgnoreCase("0")) {
                                    error = 0;
                                }
                            }
                        }

                        radioslocation.setBackground(getResources().getDrawable(R.drawable.linear_background_gray));
                        radioylocation.setBackground(getResources().getDrawable(R.drawable.radio_btn_selected));
                        location_status = "1";
                        radiopos = 1;

                        totalservicecharge();
                        setdisplay(true);

                        if (ViewAddressActivity.sub_category_idd.equals("242") || ViewAddressActivity.sub_category_idd.equals("434")) {
                            Log.e("rickshaw1", "rickshaw1 called");
                            binding.serviceTxt.setText("Driver Allowance");
                        } else if (ViewAddressActivity.sub_category_idd.equals("44") || ViewAddressActivity.sub_category_idd.equals("45")) {
                            Log.e("rickshaw1", "rickshaw1 called");
                            binding.serviceTxt.setVisibility(View.GONE);
                            binding.serviceDigitTxt.setVisibility(View.GONE);
                        } else if (ViewAddressActivity.sub_category_idd.equals("453")) {
                            Log.e("rickshaw1", "rickshaw1 called");
                            binding.serviceTxt.setVisibility(View.GONE);
                        }

                        if (checkarss(ViewAddressActivity.catid)) {
                            binding.laydestination.setVisibility(View.VISIBLE);
                            if (ViewAddressActivity.sub_category_idd.equalsIgnoreCase("242")) {
                                binding.tvAddress.setEnabled(false);
                                getsourceaddress();
                            } else {
                                binding.tvAddress.setEnabled(false);
                                binding.tvAddress.setText(Sourceaddress);
                            }

                        } else {
                            binding.laydestination.setVisibility(View.GONE);
                            binding.tvAddress.setEnabled(true);
                            getsourceaddress();
                        }


                    }

                }

            }
        });

        for (int i = 0; i < cartarraylist.size(); i++) {

            paramsBookingOp2.put("ARTIST_ID" + i, cartarraylist.get(i).getUser_id());
            paramsBookingOp2.put("PRICE" + i, cartarraylist.get(i).getPrice());
            paramsBookingOp2.put("SERVICE_ID" + i, cartarraylist.get(i).getId());
            paramsBookingOp2.put("UID" + i, userDTO.getUser_id());
            paramsBookingOp2.put("QUANTITYDAYS" + i, cartarraylist.get(i).getQuantitydays());

            if (cartHashmap != null) {

                paramsBookingOp2.put("QUANTITYDAYS" + i, cartHashmap.get(cartarraylist.get(i).getId()));
                Log.e(TAG, "onCreate: " + cartHashmap.toString() + cartHashmap.get(cartarraylist.get(i).getId()));
            }
        }

    }

    public void suggestionsecond(String subcat) {

        updateList();
        for (ProductDTO productDTO : cartarraylist) {
            if (!productDTOListtemp.contains(productDTO))
                productDTOListtemp.add(productDTO);
        }
        Bundle b = new Bundle();
        b.putSerializable(Consts.CARTDATA, (Serializable) productDTOListtemp);
        Intent in = new Intent(mContext, ViewServices.class);
        Log.e("SHIVAMO1", artistDetailsDTO.getName());
        Log.e("SHIVAMO2", artistDetailsDTO.getId());
//        Log.e("SHIVAMO3", cartHashmap.toString());
//        Log.e("SHIVAMO4", arrayprice.toString());
        Log.e("SHIVAMO5", artistDetailsDTO.getName());
        Log.e("SHIVAMO6", subcat);
        Log.e("SHIVAMO7", b.toString());
        in.putExtra(Consts.ARTIST_DTO, artistDetailsDTO);
        in.putExtra(Consts.ARTIST_ID, artist_id);
        in.putExtra(Consts.SCREEN_TAG, 2);
        in.putExtra("cartHashmap", cartHashmap);
        in.putExtra(Consts.CHANGE_PRICE, arrayprice.toString());
        in.putExtra("artist_name", artistDetailsDTO.getName());
        in.putExtra(Consts.SUB_CATEGORY_ID, subcat);
        in.putExtras(b);

        startActivity(in);
    }

    public void updateList() {
        Log.e("Khakhra_tHIRDSHIVAM", "" + productDTOList.size());
        array = new JsonArray();
        arrayprice = new JsonArray();
        productDTOListtemp = new ArrayList<>();
        for (int i = 0; i < productDTOList.size(); i++) {
            if (productDTOList.get(i).isSelected()) {
                productDTOList.get(i).setIsdisplay(false);
                array.add(productDTOList.get(i).getId());
                productDTOListtemp.add(productDTOList.get(i));
                arrayprice.add(productDTOList.get(i).getPrice());
            }
        }
    }


    public void showSuggestionData() {
        Log.e("Khakhra_tHIRD", "showSuggestionData" + "0");
        subcategoryDTOList = new ArrayList<>();
        subcategoryDTOList = artistDetailsDTO.getSubcategory();
        if (subcategoryDTOList.size() > 0) {
            more_items_txt_title.setText("Add below items from " + artistDetailsDTO.getName());
            suggestionLying.setVisibility(View.VISIBLE);
            rvSuggestion.setLayoutManager(new GridLayoutManager(this, 1, RecyclerView.HORIZONTAL, false));
            suggestionAdapter = new CartSuggestionAdapter(BookingProduct.this, subcategoryDTOList, onItemListener, artist_id, artistDetailsDTO.getName());
            rvSuggestion.setAdapter(suggestionAdapter);
        } else {
            suggestionLying.setVisibility(View.GONE);
        }
    }

    public void remove_qty_booking(String qty, String pid, int pos) {
        Log.d(TAG, "remove_qty_booking: kamaiii  " + cartHashmap.toString());
        cartHashmap.remove(pid);
        cartarraylist.remove(pos);
        Log.d(TAG, "remove_qty_booking: kamaiii  " + cartHashmap.toString());
        if (cartarraylist.size() == 0) {
            onBackPressed();
        } else {
        }

    }

    private void getPaymentDialog(String paymentselectedtype) {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.payment_mode_spinner_layout);
        bottomSheetDialog.setCancelable(false);

        CustomTextViewBold customTextViewBold = bottomSheetDialog.findViewById(R.id.pay_title);
        customTextViewBold.setText("Payment Mode");
        LinearLayout lll = bottomSheetDialog.findViewById(R.id.lll);
        RecyclerView recyclerView = bottomSheetDialog.findViewById(R.id.payment_dialog_recyclerview);

        PaymentBottomDialogadapter dialogadapter = new PaymentBottomDialogadapter(this, paymentList, amt, paymentselectedtype, paystatus);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(dialogadapter);

        lll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ptype = dialogadapter.item;

                if (ptype.equalsIgnoreCase("cash")) {
                    paystatus = "0";
                } else if (ptype.equalsIgnoreCase("Kamaii Wallet")) {
                    paystatus = "1";
                } else if (ptype.equalsIgnoreCase("Online Payment")) {
                    paystatus = "2";
                }

                if (ptype.equalsIgnoreCase("Kamaii Wallet")) {
                    if (cab_total > Double.parseDouble(amt)) {

                        double p = Double.parseDouble(amt);
                        double total = cab_total - p;
                        Intent intent = new Intent(BookingProduct.this, AddMoney.class);
                        intent.putExtra("from_booking", true);
                        intent.putExtra("diff_amt", total);
                        startActivity(intent);
                    } else {
                        binding.spinnerTxt.setText(ptype);
                    }
                } else {
                    binding.spinnerTxt.setText(ptype);
                }

                if (current_stage != 0) {
                    order_now_text.setText("Order Now ");
                    current_stage = 2;
                    binding.btnConfirm.setEnabled(true);
                    binding.btnConfirm.setClickable(true);
                }


                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.show();
    }

    public void getArtist() {

        Log.e("GET_ARTIST", "7890");

        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Log.e("GET_ARTIST", "uid:--" + userDTO.getUser_id());

        Log.e("DEVICE_TOKEN", "3333" + firebase.getString(Consts.DEVICE_TOKEN, ""));
        Call<ResponseBody> callone = api.getcartdata(userDTO.getUser_id(), firebase.getString(Consts.DEVICE_TOKEN, ""));
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        Log.e("GET_ARTIST", "23");

                        ResponseBody responseBody = response.body();
                        String s = responseBody.string();
                        Log.e("tHIRD_cart_dat", s);
                        JSONObject object = new JSONObject(s);
                        if (object.getString("status").equals("1")) {
                            try {
                                artistDetailsDTO = new Gson().fromJson(object.getJSONObject("data").toString(), ArtistDetailsDTO.class);
                                AdapterServicesNew adapterServices = new AdapterServicesNew(BookingProduct.this, artistDetailsDTO.getProducts(), artistDetailsDTO, setonItemPlusListener);

                                rv_cart2.setAdapter(adapterServices);
                                rv_cart2.setExpanded(true);

                                sub_totaln = artistDetailsDTO.getSubtotal();
                                maxprice = artistDetailsDTO.getMaxprice();

                                if (artistDetailsDTO.getServce_charge_tracker().equalsIgnoreCase("1")) {
                                    binding.serviceChargeRelative.setVisibility(View.VISIBLE);
                                    binding.serviceTxt.setText(artistDetailsDTO.getService_charge_txt());
                                    changetotalprice(artistDetailsDTO.getTotal_amount(), artistDetailsDTO.getSubtotal(), artistDetailsDTO.getShipping_charges(), artistDetailsDTO.getDis_price_text(), artistDetailsDTO.getDis_price());
                                } else {
                                    binding.serviceChargeRelative.setVisibility(View.GONE);
                                    changetotalprice(artistDetailsDTO.getTotal_amount(), artistDetailsDTO.getSubtotal(), artistDetailsDTO.getShipping_charges(), artistDetailsDTO.getDis_price_text(), artistDetailsDTO.getDis_price());
                                }
                                if (artistDetailsDTO.getThere_is_rider_not().equals("0")) {
                                    String sourceString = "Free " + artistDetailsDTO.getService_charge_txt() + " for order ₹" + "<b>" + artistDetailsDTO.getFree_delivery_price() + "</b> " + " and more";
                                    order_shipping_note.setText(Html.fromHtml(sourceString));
                                } else {
                                    order_shipping_note.setVisibility(View.GONE);
                                }

                                if (artistDetailsDTO.getSlotnote().equals("0")) {
                                    slot_note.setVisibility(View.GONE);
                                } else {
                                    slot_note.setVisibility(View.VISIBLE);
                                    slot_note.setText(Html.fromHtml(artistDetailsDTO.getSlotnote()));
                                }
//                                slot_note.setText("Open ⋅ Closes 10:45PM");

                                if (artistDetailsDTO.getSlotnote().equals("0")) {
                                    slot_note.setVisibility(View.GONE);
                                } else {
                                    slot_note.setVisibility(View.VISIBLE);
                                    slot_note.setText(Html.fromHtml(artistDetailsDTO.getSlotnote()));
                                }

                                order_preparation_note.setText(Html.fromHtml(artistDetailsDTO.getPreparationNote()));
                                if (artistDetailsDTO.getCan_do_order().equalsIgnoreCase("0")) {
                                    binding.btnConfirm.setBackground(getResources().getDrawable(R.drawable.btn_disable_drawable));
                                    binding.serviceChargeRelative.setVisibility(View.GONE);
                                    binding.noRiderFoundTxt.setText(artistDetailsDTO.getRider_not_found_msg());
                                    binding.noRiderFoundTxt.setVisibility(View.VISIBLE);
                                    binding.btnConfirm.setEnabled(false);
                                    binding.btnConfirm.setClickable(false);
                                } else {
                                    binding.noRiderFoundTxt.setVisibility(View.GONE);
                                    binding.btnConfirm.setEnabled(true);
                                    binding.btnConfirm.setClickable(true);
                                }

                                if (paymentList.get(0).equals("Kamaii Wallet")) {
                                    spinner_txt.setText("Kamaii Wallet");
                                } else {
                                    spinner_txt.setText("Cash");
                                }
                                showData();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.e("GET_ARTIST", "6");

                            onBackPressed();
                        }
                    }
                } catch (JSONException | IOException e) {
                    Log.e("GET_ARTIST", "8");

                    onBackPressed();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    public void showData() {
        gridLayoutManager = new GridLayoutManager(mContext, 1);
        productDTOList = new ArrayList<>();
        productDTOList = artistDetailsDTO.getProducts();
        if (productDTOList.size() > 0) {
            rvSuggestion.setVisibility(View.VISIBLE);
        } else {
            rvSuggestion.setVisibility(View.GONE);
        }
        showSuggestionData();
    }

    public void bookArtist2(String quentity) {

        Log.e("GET_ARTIST", "bookartist2 called");

        new HttpsRequest(Consts.ADD_TO_CART_TEST_API, paramsBookingOp2, BookingProduct.this).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                if (flag) {
                    try {

                        String s = response.toString();
                        Log.e("SHIVAMKOSHTI", s.toString());
                        userBookingList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<CartModel>>() {
                        }.getType();
                        JSONArray jsonArray = response.getJSONArray("data");
                        JSONObject jsonObject = jsonArray.getJSONObject(0);

                        String SHIP_charge = jsonObject.getString("shipping_charges");
                        String SUB_TOTAL1 = jsonObject.getString("subtotal");
                        String TOTAL1 = jsonObject.getString("total_amount");
                        String track1 = jsonObject.getString("tracker");
                        rider1_id = jsonObject.getString("rider1_id");
                        total_rider_charges = jsonObject.getString("total_rider_charges");
                        service_charge_txt = jsonObject.getString("service_charge_txt");
                        servce_charge_tracker = jsonObject.getString("servce_charge_tracker");


                        if (servce_charge_tracker.equalsIgnoreCase("1")) {
                            binding.serviceChargeRelative.setVisibility(View.VISIBLE);

                            binding.serviceTxt.setText(service_charge_txt);
                        } else {
                            binding.serviceChargeRelative.setVisibility(View.GONE);
                        }

                        changefreeshippingdata(service_charge_txt);
                        cab_total = Double.parseDouble(TOTAL1);
                        cab_sub_total = Double.parseDouble(SUB_TOTAL1);
                        cab_allowence = Double.parseDouble(SHIP_charge);
                        paramsBookingOp.put("rider1_id", rider1_id);
                        paramsBookingOp.put("total_rider_charges", total_rider_charges);


                        if (track.equals("1")) {
                        }
                        //  changetotalprice(TOTAL1, SUB_TOTAL1, SHIP_charge);


                        Log.e("SUB_TOTAL", "" + SUB_TOTAL1);
                        Log.d("userBookingList_TOTAL", "" + jsonArray.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.d(TAG, "backResponse: " + msg);
                }
            }
        });
    }

    private void changefreeshippingdata(String service_charge_txt) {
        order_shipping_note.setText(service_charge_txt);
    }

    public void changetotalprice(String texting, String SUB_TOTAL, String SHIP_charge, String disc_txt, String disc_price) {
        if (SHIP_charge.equals("0.00") || SHIP_charge.equals("0")) {
            findViewById(R.id.service_charge_relative).setVisibility(View.VISIBLE);
            binding.serviceRupeeSymbol.setVisibility(View.GONE);
            binding.orderShippingNote.setVisibility(View.GONE);
            binding.serviceDigitTxt.setText("Free");
        } else {
            binding.orderShippingNote.setVisibility(View.VISIBLE);
            findViewById(R.id.service_charge_relative).setVisibility(View.VISIBLE);
            binding.serviceRupeeSymbol.setVisibility(View.VISIBLE);
            binding.serviceDigitTxt.setText(SHIP_charge);
        }
        binding.rupeeSymbol.setText(Html.fromHtml("&#x20B9;"));
        binding.tvPrice.setText(texting);
        binding.discountTxt.setText(Html.fromHtml(disc_txt));
        binding.discountDigitTxt.setText(Html.fromHtml("&#x20B9;" + disc_price));
        binding.totalBtnTxt.setText(Html.fromHtml("&#x20B9;" + texting));
        binding.subTotalRupeeSymbol.setText(Html.fromHtml("&#x20B9;"));
        binding.subTotalDigits.setText(SUB_TOTAL);
        binding.serviceRupeeSymbol.setText(Html.fromHtml("&#x20B9;"));
        order_shipping_note.setText(service_charge_txt);
    }

    public void getshipping() {

        parmsshipping.put(Consts.USER_ID, artist_id);
        parmsshipping.put("cust_id", userDTO.getUser_id());
        parmsshipping.put(Consts.SUB_CAT_ID, ViewAddressActivity.sub_category_idd);
        ProjectUtils.showProgressDialog(BookingProduct.this, true, getResources().getString(R.string.please_wait));

        new HttpsRequest(Consts.GET_SERVICE_SHIPPING2, parmsshipping, BookingProduct.this).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();


                if (flag) {
                    try {

                        JSONArray jsonarray = response.getJSONArray("data");
                        for (int i = 0; i < jsonarray.length(); i++) {

                            JSONObject c = jsonarray.getJSONObject(i);


                            String id = c.getString("id");
                            String sub_cat_id = c.getString("sub_cat_id");
                            String shipping = c.getString("shipping");
                            String my_location = c.getString("my_location");
                            String maxprice = c.getString("maxprice");
                            shippingDTO = new ShippingDTO();

                            shippingDTO.setId(id);
                            shippingDTO.setSub_cat_id(sub_cat_id);
                            shippingDTO.setShipping(shipping);
                            shippingDTO.setMy_location(my_location);
                            shippingDTO.setMaxprice(maxprice);


                            shippingDTOArrayList.add(shippingDTO);


                        }

                        JSONArray jsonArray1 = response.getJSONArray("riderlist");
                        JSONObject jsonObject1 = jsonArray1.getJSONObject(0);
                        rider_name = jsonObject1.getString("name");
                        error = 0;

                        for (int i = 0; i < shippingDTOArrayList.size(); i++) {
                            if (shippingDTOArrayList.get(i).getSub_cat_id().equalsIgnoreCase(ViewAddressActivity.sub_category_idd)) {

                                if (shippingDTOArrayList.get(i).getMy_location().equalsIgnoreCase("0")) {
                                    error = 0;
                                    tverror.setText("Your location not available so please select Service Provider Location");
                                }
                            }
                            maxprice = shippingDTOArrayList.get(i).getMaxprice();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                }


            }
        });
    }

    private OnSelectedItemListener onItemListener = new OnSelectedItemListener() {
        @Override
        public void setOnClick(String selectionString, int position) {

            cartarraylist.remove(position);
            paramsBookingOp3.clear();
            for (int i = 0; i < cartarraylist.size(); i++) {

                paramsBookingOp3.put("ARTIST_ID" + i, cartarraylist.get(i).getUser_id());
                paramsBookingOp3.put("PRICE" + i, cartarraylist.get(i).getPrice());
                paramsBookingOp3.put("SERVICE_ID" + i, cartarraylist.get(i).getId());
                paramsBookingOp3.put("QUANTITYDAYS" + i, "1");
            }

            new HttpsRequest(Consts.ADD_TO_CART_TEST_API, paramsBookingOp3, BookingProduct.this).stringPost(TAG, new Helper() {
                @Override
                public void backResponse(boolean flag, String msg, JSONObject response) {

                    if (flag) {
                        try {
                            String s = response.toString();
                            userBookingList = new ArrayList<>();
                            Type getpetDTO = new TypeToken<List<CartModel>>() {
                            }.getType();
                            JSONArray jsonArray = response.getJSONArray("data");
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String SHIP_charge = jsonObject.getString("shipping_charges");
                            String SUB_TOTAL = jsonObject.getString("subtotal");
                            String TOTAL = jsonObject.getString("total_amount");

                            cab_total = Double.parseDouble(TOTAL);
                            cab_sub_total = Double.parseDouble(SUB_TOTAL);
                            cab_allowence = Double.parseDouble(SHIP_charge);

                            String track = jsonObject.getString("tracker");

                            if (track.equals("1")) {
                            }
                            // ((BookingProduct) BookingProduct.this).changetotalprice(TOTAL, SUB_TOTAL, SHIP_charge);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                    }
                }
            });
        }

    };

    public void showSpinner() {

        ArrayAdapter customArrayAdapter = new CustomArrayAdapter2(this, R.layout.support_simple_spinner_dropdown_item, paymentList);

        binding.paymentTypeSpinner.setAdapter(customArrayAdapter);


        binding.paymentTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                switch (item) {
                    case "Cash":
                        ptype = "Cash";
                        paystatus = "0";
                        break;
                    case "Kamaii Wallet":
                        ptype = "Kamaii Wallet";
                        paystatus = "1";
                        break;
                    case "Online Payment":
                        ptype = "Online Payment";
                        paystatus = "2";
                        break;
                }
                binding.spinnerTxt.setText(item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private SetonItemPlusListener setonItemPlusListener = new SetonItemPlusListener() {
        @Override
        public void Click(int position, double total, int servicecharge, int servicepostition, int quantity) {
            if (radiopos == 0) {
                if (position == 0) {
                    double previoustotal = Double.parseDouble(binding.tvPrice.getText().toString());
                    double maintotal = previoustotal - total;
                    binding.tvPrice.setText(String.valueOf(maintotal));
                    cartarraylist.get(servicepostition).setQuantityvalue(String.valueOf(quantity));
                } else {
                    double previoustotal = Double.parseDouble(binding.tvPrice.getText().toString());
                    double maintotal = previoustotal + total;
                    binding.tvPrice.setText(String.valueOf(maintotal));
                    cartarraylist.get(servicepostition).setQuantityvalue(String.valueOf(quantity));
                }
            } else {
                if (position == 0) {
                    double previoustotal = Double.parseDouble(binding.tvPrice.getText().toString());
                    double grandtotal = total + servicecharge;
                    double maintotal = previoustotal - grandtotal;
                    binding.tvPrice.setText(String.valueOf(maintotal));
                    cartarraylist.get(servicepostition).setQuantityvalue(String.valueOf(quantity));
                } else {
                    double previoustotal = Double.parseDouble(binding.tvPrice.getText().toString());
                    double grandtotal = total + servicecharge;
                    double maintotal = previoustotal + grandtotal;
                    binding.tvPrice.setText(String.valueOf(maintotal));
                    cartarraylist.get(servicepostition).setQuantityvalue(String.valueOf(quantity));
                }

            }


        }
    };

    public void setUiAction() {
        Log.e("GET_ARTIST", "456");

        if (binding.tvAddress.getText().equals(getResources().getString(R.string.tap_to_select_address))) {
            binding.tvAddress.setTextColor(getResources().getColor(R.color.pink_text_color));
        }
        binding.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (binding.tvAddress.getText().toString().equals(getResources().getString(R.string.tap_to_select_address))) {
            order_now_text.setText("Select Address");
        }

        binding.tvBookingDate.setText(sdf1.format(date).toString().toUpperCase());

        if (artistDetailsDTO != null) {


            if (!artistDetailsDTO.getLocation().equalsIgnoreCase("")) {

                paramsBookingOp.put(Consts.LATITUDE, artistDetailsDTO.getLatitude());
                paramsBookingOp.put(Consts.LONGITUDE, artistDetailsDTO.getLongitude());
            }
        } else {
            getArtist();
        }
        binding.tvAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showAddressDialog();

            }
        });
        binding.llDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickScheduleDateTime();
            }
        });


        binding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Toast.makeText(mContext, "clicked", Toast.LENGTH_SHORT).show();
                binding.btnConfirm.setEnabled(false);
                binding.btnConfirm.setClickable(false);
                Log.e("error", " abc 1");
                paramsBookingOp4.put(Consts.USER_ID, userDTO.getUser_id());

                check_minimum_price();
            }
        });
    }

    public void finalClick() {
        if (binding.tvAddress.getText().toString().trim().length() > 0) {
            if (current_stage == 2) {

                double totalprice = Double.parseDouble(binding.tvPrice.getText().toString());
                double mprice = Double.parseDouble(maxprice);
                Log.e("error", "abc 2");
                if (binding.spinnerTxt.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(BookingProduct.this, "Please Select Payment Type", Toast.LENGTH_LONG).show();
                    return;
                }
                if (binding.spinnerTxt.getText().toString().equalsIgnoreCase("Kamaii Wallet")) {
                    if (totalprice <= Double.parseDouble(amt)) {
                        final_amount = binding.tvPrice.getText().toString();
                    } else {
                        Toast.makeText(BookingProduct.this, "Insufficient Balance", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                if (radiopos == 0) {
                    try {
                        Log.e("error", "abc 3" + error);
                        if (error != 1) {
                            if (binding.spinnerTxt.getText().toString().equalsIgnoreCase("Cash")) {
                                final_amount = binding.tvPrice.getText().toString();
                                if (screen_tag == 1) {
                                    paramsBookingOp.put(Consts.PRICE, artistDetailsDTO.getPrice());
                                    submit();
                                    Log.e("error", "cash ma gayu");

                                } else if ((screen_tag == 2)) {
                                    bookForService();
                                }
                            } else if (binding.spinnerTxt.getText().toString().equalsIgnoreCase("Kamaii Wallet")) {
                                if (totalprice <= Double.parseDouble(amt)) {

                                    final_amount = binding.tvPrice.getText().toString();
                                    if (screen_tag == 1) {
                                        Log.e("error", "wallet ma gayu");

                                        paramsBookingOp.put(Consts.PRICE, artistDetailsDTO.getPrice());

                                        submit();
                                    } else if ((screen_tag == 2)) {
                                        bookForService();
                                    }
                                } else {
                                    Toast.makeText(BookingProduct.this, "Insufficient Balance", Toast.LENGTH_LONG).show();
                                    return;
                                }
                            } else {
                                double total = totalprice;
                                Log.e("error", "online ma gayu");

                                DecimalFormat newFormat = new DecimalFormat("####");
                                double mainprice = Double.parseDouble(newFormat.format(total));
                                final_amount = String.valueOf(mainprice);
                                JSONObject object = new JSONObject();
                                try {
                                    object.put("merchant_id", Consts.merchantIdLIVE);
                                    object.put("access_token", Consts.accessTokenLIVE);
                                    object.put("customer_name", userDTO.getName());
                                    object.put("customer_email", userDTO.getEmail_id());
                                    object.put("customer_phone", userDTO.getMobile());
                                    object.put("product_name", "booking");
                                    object.put("order_no", System.currentTimeMillis()); // order no. should have 10 to 30 character in numeric format
                                    object.put("amount", String.valueOf(mainprice));  // minimum amount should be 10
                                    object.put("isLive", true); // need to send false if you are in sandbox mode
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                new PaykunApiCall.Builder(BookingProduct.this).sendJsonObject(object);

                            }
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    if (error != 1) {
                        Log.e("error", "4");
                        if (binding.tvAddress.getText().toString().equalsIgnoreCase(getResources().getString(R.string.tap_to_select_address))) {
                            Log.e("error", "404");

                            showAddressDialog();
                        } else {
                            Log.e("error", "405");
                            try {
                                if (totalprice < mprice) {
                                    Log.e("error", "406");

                                    new AlertDialog.Builder(mContext)
                                            .setIcon(R.mipmap.ic_launcher)
                                            .setMessage("Please note this service minimum amount is " + maxprice + " Rs")
                                            .setCancelable(false)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    //       dialog.dismiss();
                                                }
                                            })
                                            .show();
                                } else {
                                    Log.e("error", "407");

                                    String daddress = "";
                                    if (!destaddress.equalsIgnoreCase("")) {
                                        Log.e("error", "408");

                                        daddress = "Destination Location:" + " " + txtdestinationaddress.getText().toString();
                                    }

                                    if (binding.spinnerTxt.getText().toString().equalsIgnoreCase("Cash")) {

                                        final_amount = binding.tvPrice.getText().toString();
                                        Log.e("error", "409 " + final_amount + "screen-tag:- "+screen_tag);

                                        if (screen_tag == 1) {

                                            paramsBookingOp.put(Consts.PRICE, artistDetailsDTO.getPrice());

                                            Log.e("error","505");
                                            submit();
                                        } else if ((screen_tag == 2)) {
                                            Log.e("error","506");

                                            bookForService();
                                        }

                                    } else if (binding.spinnerTxt.getText().toString().equalsIgnoreCase("Kamaii Wallet")) {


                                        if (totalprice <= Double.parseDouble(amt)) {

                                            final_amount = binding.tvPrice.getText().toString();
                                            if (screen_tag == 1) {
                                                paramsBookingOp.put(Consts.PRICE, artistDetailsDTO.getPrice());
                                                Log.e("error", "submit called");

                                                submit();
                                            } else if ((screen_tag == 2)) {
                                                Log.e("error", "book api called");
                                                bookForService();
                                            }
                                        } else {
                                            Toast.makeText(BookingProduct.this, "Insufficient Balance", Toast.LENGTH_LONG).show();
                                            return;
                                        }
                                        //   dialog.dismiss();


                                    } else {

                                        double extraadd = (Double.parseDouble(binding.tvPrice.getText().toString()) * 2) / 100;
                                        double total = totalprice + extraadd;
                                        DecimalFormat newFormat = new DecimalFormat("####");
                                        double mainprice = Double.parseDouble(newFormat.format(total));
                                        final_amount = String.valueOf(mainprice);
                                        JSONObject object = new JSONObject();
                                        try {
                                            object.put("merchant_id", Consts.merchantIdLIVE);
                                            object.put("access_token", Consts.accessTokenLIVE);
                                            object.put("customer_name", userDTO.getName());
                                            object.put("customer_email", userDTO.getEmail_id());
                                            object.put("customer_phone", userDTO.getMobile());
                                            object.put("product_name", "booking");
                                            object.put("order_no", System.currentTimeMillis()); // order no. should have 10 to 30 character in numeric format
                                            object.put("amount", String.valueOf(mainprice));  // minimum amount should be 10
                                            object.put("isLive", true); // need to send false if you are in sandbox mode
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        new PaykunApiCall.Builder(BookingProduct.this).sendJsonObject(object);
                                        //   dialog.dismiss();
                                    }
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }
            } else {
                if (current_stage == 0) {
                    showAddressDialog();
                } else if (current_stage == 1) {
                    getPaymentDialog(spinner_txt.getText().toString());
                }
            }
        } else {
            // showAddressDialog();
            ProjectUtils.showLong(mContext, getResources().getString(R.string.val_address));
        }
    }

    private void showAddressDialog() {

        addressBottomSheet = new BottomSheetDialog(this);
        //addressBottomSheet.setCancelable(false);
        //   addressBottomSheet.setCancelable(false);
        addressBottomSheet.setContentView(R.layout.activity_address_bottom_dialog);
        change_address_btn = addressBottomSheet.findViewById(R.id.change_address_btn);
        RadioGroup add_addressradioGroup;
        ImageView back = addressBottomSheet.findViewById(R.id.address_dialog_close_img);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressBottomSheet.dismiss();
            }
        });

        Typeface font2 = Typeface.createFromAsset(BookingProduct.this.getAssets(), "Poppins-Regular.otf");


        location_etx = addressBottomSheet.findViewById(R.id.location_etx);
        house_no_etx = addressBottomSheet.findViewById(R.id.house_no_etx);
        society_name_etx = addressBottomSheet.findViewById(R.id.society_name_etx);
        customAddress = addressBottomSheet.findViewById(R.id.customAddress);
        add_addressradioGroup = addressBottomSheet.findViewById(R.id.add_addressradioGroup);
        address_submit = addressBottomSheet.findViewById(R.id.address_submit);

        location_etx.setTypeface(font2);
        house_no_etx.setTypeface(font2);
        society_name_etx.setTypeface(font2);

        addAddressHashmap.put(Consts.USER_ID, userDTO.getUser_id());
        address_relative = addressBottomSheet.findViewById(R.id.address_relative);
        address_lay_dest_location = addressBottomSheet.findViewById(R.id.address_lay_dest_location);
        RecyclerView recyclerView = addressBottomSheet.findViewById(R.id.address_recycler);

        if (typeAddressList.size() == 0) {
            address_relative.setVisibility(View.GONE);
        }

        dialogadapter = new AddressBottomSheetAdapter(this, typeAddressList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(dialogadapter);


        change_address_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPlace();
            }
        });


        add_addressradioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {

                    case R.id.add_home_radio:
                        address_radio_txt = "Home";
                        addAddressHashmap.put("addresstype", address_radio_txt);
                        break;

                    case R.id.add_work_radio:
                        address_radio_txt = "Work";
                        addAddressHashmap.put("addresstype", address_radio_txt);
                        break;

                    case R.id.add_other_radio:
                        address_radio_txt = "Others";
                        addAddressHashmap.put("addresstype", address_radio_txt);
                        break;
                }
            }
        });


        address_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current_stage = 1;
                order_now_text.setText("Select Payment");
                binding.btnConfirm.setEnabled(true);
                binding.btnConfirm.setClickable(true);

                house_no_str = house_no_etx.getText().toString();
                society_name_str = society_name_etx.getText().toString();
                street_address_str = location_etx.getText().toString();

                addAddressHashmap.put("house_no", house_no_str);
                addAddressHashmap.put("society_name", society_name_str);
                addAddress();
                binding.tvAddress.setTextColor(getResources().getColor(R.color.dark_blue_color));

                if (house_no_str.isEmpty() && society_name_str.isEmpty()) {
                    Log.e("ADDRESS_ADAPTER", " 101");

                    binding.tvAddress.setText(street_address_str);
                } else if (house_no_str.isEmpty()) {
                    Log.e("ADDRESS_ADAPTER", " 102");

                    binding.tvAddress.setText(society_name_str + ", " + street_address_str);
                } else if (society_name_str.isEmpty()) {
                    Log.e("ADDRESS_ADAPTER", " 103");

                    binding.tvAddress.setText(house_no_str + ", " + street_address_str);
                } else {
                    Log.e("ADDRESS_ADAPTER", " 104");

                    binding.tvAddress.setText(house_no_str + ", " + society_name_str + ", " + street_address_str);
                }

                customAddress.setVisibility(View.GONE);
                address_relative.setVisibility(View.VISIBLE);
                address_lay_dest_location.setVisibility(View.VISIBLE);


                addressBottomSheet.dismiss();
            }
        });
        address_lay_dest_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 109);
                    } else {
                        findPlace();
                    }
                }

            }
        });
        addressBottomSheet.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 109) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                findPlace();
            } else {
                //   Toast.makeText(mContext, "Location Permission is required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void changingtext() {
        order_now_text.setText("Select Payment ");
        current_stage = 1;
        binding.btnConfirm.setEnabled(true);
        binding.btnConfirm.setClickable(true);
    }

    public void deleteaddress(String address_id) {
        deleteAddressHashmap.put(Consts.USER_ID, userDTO.getUser_id());
        deleteAddressHashmap.put(Consts.ADDRESS_ID, address_id);
        new HttpsRequest(Consts.DELETE_ADDRESS, deleteAddressHashmap, this).stringPost("Tag", new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    if (msg.equals("0")) {
                        address_relative.setVisibility(View.GONE);
                    }
                } else {

                }
            }
        });
    }

    public void addAddress() {
        addAddressHashmap.put("addresstype", address_radio_txt);
        Log.e("addAddressHashmap", "" + addAddressHashmap.toString());
        new HttpsRequest(Consts.ADD_ADDRESS, addAddressHashmap, this).stringPost(Tag, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                Log.e("addAddressHashmap", "bahar" + response.toString());

                if (flag) {
                    Log.e("addAddressHashmap", "17");

                    getAddress();
                    Log.e("addAddressHashmap", "" + response.toString());
                } else {
                    Log.e("addAddressHashmap", "1234567");
                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getAddress() {

        getAddressHashmap.put("cust_id", userDTO.getUser_id());
        parmsshipping.put(Consts.ARTIST_ID, artist_id);

        new HttpsRequest(Consts.GET_ADDRESS, getAddressHashmap, this).stringPost(Tag, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                if (flag) {

                    Log.e("GET_ADDRESS_API", "" + response.toString());

                    Type getaddDTO = new TypeToken<List<TypeAddressModel>>() {
                    }.getType();
                    try {
                        typeAddressList = (ArrayList<TypeAddressModel>) new Gson().fromJson(response.getJSONArray("data").toString(), getaddDTO);
                       if(typeAddressList.size()!=0){

                           address_id = typeAddressList.get(typeAddressList.size() - 1).getAddress_id();
                       }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.e("ADBVC","12231");

                }
            }
        });
    }

    public Map<String, String> getParms(String type) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Consts.INVOICE_ID, "");
        params.put(Consts.USER_ID, userDTO.getUser_id());
        params.put(Consts.COUPON_CODE, "");
        params.put(Consts.FINAL_AMOUNT, final_amount);
        params.put(Consts.PAYMENT_STATUS, "1");
        params.put(Consts.PAYMENT_TYPE, type);
        params.put(Consts.DISCOUNT_AMOUNT, "");

        Log.e("sendPaymentConfirm", params.toString());
        return params;
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void getResults(Events.PaymentMessage message) {
        if (message.getResults().equalsIgnoreCase(PaykunHelper.MESSAGE_SUCCESS)) {
            // do your stuff here
            // message.getTransactionId() will return your failed or succeed transaction id
            /* if you want to get your transaction detail call message.getTransactionDetail()
             *  getTransactionDetail return all the field from server and you can use it here as per your need
             *  For Example you want to get Order id from detail use message.getTransactionDetail().order.orderId */
            if (!TextUtils.isEmpty(message.getTransactionId())) {
                Toast.makeText(BookingProduct.this, "Your Transaction is succeed", Toast.LENGTH_SHORT).show();
                if (screen_tag == 1) {
                    paramsBookingOp.put(Consts.PRICE, artistDetailsDTO.getPrice());
                    submit();
                } else if ((screen_tag == 2)) {
                    bookForService();
                }
                Log.v(" order id ", " getting order id value : " + message.getTransactionDetail().order.orderId);
            }
        } else if (message.getResults().equalsIgnoreCase(PaykunHelper.MESSAGE_FAILED)) {
            // do your stuff here
            Toast.makeText(BookingProduct.this, "Your Transaction is failed", Toast.LENGTH_SHORT).show();
        } else if (message.getResults().equalsIgnoreCase(PaykunHelper.MESSAGE_SERVER_ISSUE)) {
            // do your stuff here
            Toast.makeText(BookingProduct.this, PaykunHelper.MESSAGE_SERVER_ISSUE, Toast.LENGTH_SHORT).show();
        } else if (message.getResults().equalsIgnoreCase(PaykunHelper.MESSAGE_ACCESS_TOKEN_MISSING)) {
            // do your stuff here
            Toast.makeText(BookingProduct.this, "Access Token missing", Toast.LENGTH_SHORT).show();
        } else if (message.getResults().equalsIgnoreCase(PaykunHelper.MESSAGE_MERCHANT_ID_MISSING)) {
            // do your stuff here
            Toast.makeText(BookingProduct.this, "Merchant Id is missing", Toast.LENGTH_SHORT).show();
        } else if (message.getResults().equalsIgnoreCase(PaykunHelper.MESSAGE_INVALID_REQUEST)) {
            Toast.makeText(BookingProduct.this, "Invalid Request", Toast.LENGTH_SHORT).show();
        } else if (message.getResults().equalsIgnoreCase(PaykunHelper.MESSAGE_NETWORK_NOT_AVAILABLE)) {
            Toast.makeText(BookingProduct.this, "Network is not available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        GlobalBus.getBus().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        GlobalBus.getBus().unregister(this);
    }

    public void bookForService() {
        array = new JsonArray();
        arrayp = new JsonArray();

        arrayquantitydays = new JsonArray();
        arrayquantityvalue = new JsonArray();

        for (int i = 0; i < artistDetailsDTO.getProducts().size(); i++) {

            array.add(artistDetailsDTO.getProducts().get(i).getId());
            arrayp.add(artistDetailsDTO.getProducts().get(i).getPrice());
            arrayquantitydays.add(artistDetailsDTO.getProducts().get(i).getCart_quantity());
            arrayquantityvalue.add("");
        }
        paramsBookingOp.put(Consts.SERVICE_ID, array.toString());
        paramsBookingOp.put(Consts.CHANGE_PRICE, arrayp.toString());
        paramsBookingOp.put(Consts.CHANGE_PRICE1, arrayp.toString());
        paramsBookingOp.put(Consts.MAXMIUMVALUE, arrayquantityvalue.toString());
        paramsBookingOp.put(Consts.QUANTITYDAYS, arrayquantitydays.toString());
        Log.e("error", "submit method");
        submit();

    }

    public void sickbar(String msg) {
        binding.RRsncbarrr.setVisibility(View.VISIBLE);
        Snackbar snackbar = Snackbar.make(binding.RRsncbarrr, msg, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getResources().getColor(R.color.pink_text_color));
        snackbar.show();
    }

    public void bookArtist() {

        //back_btn_flag = true;
        Log.e("USER_ID", "" + userDTO.getUser_id());
        paramsBookingOp.put(Consts.USER_ID, userDTO.getUser_id());
        paramsBookingOp.put("customer_id", userDTO.getUser_id());
        paramsBookingOp.put("orderdelivereddate", artistDetailsDTO.getOrderdelivereddate());
        paramsBookingOp.put("there_is_rider_not", artistDetailsDTO.getThere_is_rider_not());
        paramsBookingOp.put("there_is_rider_not1", artistDetailsDTO.getThere_is_rider_not());
        paramsBookingOp.put("ridersubcat_id", artistDetailsDTO.getRidersubcat_id());
        paramsBookingOp.put(Consts.ARTIST_ID, artistDetailsDTO.getUser_id());
        paramsBookingOp.put(Consts.PRICE, binding.tvPrice.getText().toString());
        paramsBookingOp.put(Consts.LOCATION_STATUS, location_status);
        paramsBookingOp.put(Consts.LOCATION_STATUS1, location_status);
        paramsBookingOp.put(Consts.DESTI_ADDRESS, destaddress);
        paramsBookingOp.put(Consts.DESTI_ADDRESS1, destaddress);
        paramsBookingOp.put(Consts.KM, km);
        paramsBookingOp.put(Consts.DESTI_TIME, destitime);
        paramsBookingOp.put(Consts.PTYPE, ptype);
        paramsBookingOp.put(Consts.TOTAL_AMOUNT, final_amount);
        paramsBookingOp.put(Consts.PAY_STATUS, paystatus);
        paramsBookingOp.put("addresstype", address_radio_txt);
        paramsBookingOp.put("house_no", house_no_str);
        paramsBookingOp.put("street_address", street_address_str);
        paramsBookingOp.put("society_name", society_name_str);
        paramsBookingOp.put("rider1_id", artistDetailsDTO.getRider1_id());
        paramsBookingOp.put("total_rider_charges", artistDetailsDTO.getTotal_rider_charges());
        paramsBookingOp.put("total_rider_charges1", artistDetailsDTO.getTotal_rider_charges());
        paramsBookingOp.put(Consts.ADDRESS, landmark_name + final_user_address);
        paramsBookingOp.put("address_id", address_id);
        paramsBookingOp.put(Consts.FULL_ADDRESS, binding.tvAddress.getText().toString());

        //  paramsBookingOp.put(Consts.ADDRESS, binding.tvAddress.getText().toString());
        paramsBookingOp.put(Consts.ADDRESS, landmark_name + final_user_address);
        paramsBookingOp.put(Consts.SOURCE_LAT, String.valueOf(ViewAddressActivity.startLati));
        paramsBookingOp.put(Consts.SOURCE_LONG, String.valueOf(ViewAddressActivity.startLongi));

        Log.e("BOOKING_LAT", "" + ViewAddressActivity.goalLati + " longitude " + ViewAddressActivity.goalLongi);
        paramsBookingOp.put(Consts.DEST_LAT, String.valueOf(ViewAddressActivity.goalLati));
        paramsBookingOp.put(Consts.DEST_LONG, String.valueOf(ViewAddressActivity.goalLongi));
        paramsBookingOp.put("preparation_note", artistDetailsDTO.getPreparationNote());

        ProjectUtils.showProgressDialog(mContext, false, "Loading...");
        Log.e("book_paramsBookingOp213", "" + artistDetailsDTO.getPreparationNote());
        Log.e("book_test_response", "" + paramsBookingOp.toString());

        new HttpsRequest(Consts.BOOK_ARTIST_API2, paramsBookingOp, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {

                        Log.e("book_test_response", "" + response.toString());
                        userBookingList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<UserBooking>>() {
                        }.getType();
                        userBookingList = (ArrayList<UserBooking>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    AddressOperation(userDTO.getUser_id(), final_user_address, String.valueOf(ViewAddressActivity.startLati), String.valueOf(ViewAddressActivity.startLongi), "3");
                    AddressOperation(userDTO.getUser_id(), destaddress, String.valueOf(ViewAddressActivity.goalLati), String.valueOf(ViewAddressActivity.goalLongi), "3");

                    Log.e("preference_route", "" + prefrence.getValue(ROUTE_PATH));
                    RouteOperation(userBookingList.get(0).getId(), prefrence.getValue(ROUTE_PATH), String.valueOf(ViewAddressActivity.startLati), String.valueOf(ViewAddressActivity.startLongi), String.valueOf(ViewAddressActivity.goalLati), String.valueOf(ViewAddressActivity.goalLongi));

                } else {
                    ProjectUtils.showToast(mContext, msg);
                    binding.btnConfirm.setEnabled(true);
                    binding.btnConfirm.setClickable(true);
                }
            }
        });
    }

    public void AddressOperation(String user_id, String address, String lat, String lang, String address_type) {

        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.AddressOperation(user_id, address, lat, lang, address_type);
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("RES", response.message());

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void findPlace() {


        Intent intentt = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .setCountry("IN")
                .build(BookingProduct.this);

        startActivityForResult(intentt, AUTOCOMPLETE_REQUEST_CODE);

    }

    public void getsourceaddress() {
        Geocoder geocoder = new Geocoder(BookingProduct.this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(Double.parseDouble(prefrence.getValue(Consts.LATITUDE)), Double.parseDouble(prefrence.getValue(Consts.LONGITUDE)), 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
            add = add + "\n" + obj.getCountryName();
            add = add + "\n" + obj.getCountryCode();
            add = add + "\n" + obj.getAdminArea();
            add = add + "\n" + obj.getPostalCode();
            add = add + "\n" + obj.getSubAdminArea();
            add = add + "\n" + obj.getLocality();
            add = add + "\n" + obj.getSubThoroughfare();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == AUTOCOMPLETE_REQUEST_CODE) {

            Place place = Autocomplete.getPlaceFromIntent(data);

            final_user_address = place.getAddress();
            landmark_name = place.getName();

            ViewAddressActivity.startLati = place.getLatLng().latitude;
            ViewAddressActivity.startLongi = place.getLatLng().longitude;

            paramsBookingOp.put(Consts.LATITUDE, String.valueOf(ViewAddressActivity.startLati));
            paramsBookingOp.put(Consts.LONGITUDE, String.valueOf(ViewAddressActivity.startLongi));

            addAddressHashmap.put("street_address", landmark_name + final_user_address);
            addAddressHashmap.put(Consts.LATITUDE, String.valueOf(ViewAddressActivity.startLati));
            addAddressHashmap.put(Consts.LONGITUDE, String.valueOf(ViewAddressActivity.startLongi));

            location_etx.setText(landmark_name + ", " + place.getAddress());
            customAddress.setVisibility(View.VISIBLE);
            address_lay_dest_location.setVisibility(View.GONE);
            address_relative.setVisibility(View.GONE);

        } else {
            Log.d("TAG", "hello");
        }
    }

    @Override
    public void onBackPressed() {
        //  if (!back_btn_flag){
        super.onBackPressed();
      /*  }
        else {
            binding.llBack.setEnabled(false);
        }*/
    }

    public void clickScheduleDateTime() {
        new SingleDateAndTimePickerDialog.Builder(this)
                .bottomSheet()
                .curved()
                .mustBeOnFuture()
                .defaultDate(new Date())
                .listener(new SingleDateAndTimePickerDialog.Listener() {
                    @Override
                    public void onDateSelected(Date date) {
                        paramsBookingOp.put(Consts.DATE_STRING, String.valueOf(sdf1.format(date).toString().toUpperCase()));
                        paramsBookingOp.put(Consts.TIMEZONE, String.valueOf(timeZone.format(date)));
                        paramsBookingOp.put(Consts.TIMEZONE1, String.valueOf(timeZone.format(date)));
                        binding.tvBookingDate.setText(sdf1.format(date).toString().toUpperCase());
                    }
                })
                .display();
    }

    public void submit() {
        if (!validation(binding.tvBookingDate, getResources().getString(R.string.val_date))) {
            return;
        } else if (!validation(binding.tvAddress, getResources().getString(R.string.val_address))) {
            return;
        } else {
            if (NetworkManager.isConnectToInternet(mContext)) {
                bookArtist();
            } else {
                ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
            }
        }
    }

    public boolean validation(TextView textView, String msg) {
        if (!ProjectUtils.isTextFilled(textView)) {
            ProjectUtils.showLong(mContext, msg);
            return false;
        } else {
            return true;
        }
    }

    private void total() {

        total = 0;
        try {

            for (int i = 0; i < cartarraylist.size(); i++) {
                total = total + Double.parseDouble(cartarraylist.get(i).getPrice());
                sub_total = total - Double.parseDouble(cartarraylist.get(i).getService_charge());
            }
            if (cartarraylist.size() == 0) {
                finish();
            }
            binding.tvPrice.setText(String.valueOf(total));
            binding.subTotalDigits.setText(String.valueOf(sub_total));

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

    }

    private void totalservicecharge() {

        total = 0;
        try {

            for (int i = 0; i < cartarraylist.size(); i++) {
                if (cartarraylist.get(i).getService_charge().equalsIgnoreCase("")) {
                    total = total + Double.parseDouble(cartarraylist.get(i).getPrice());
                } else {
                    total = total + Double.parseDouble(cartarraylist.get(i).getPrice());
                }


            }
            if (cartarraylist.size() == 0) {
                finish();
            }
            binding.rupeeSymbol.setText(Html.fromHtml("&#x20B9;"));
            binding.tvPrice.setText(String.valueOf(total));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

    }

    public void setdisplay(boolean display) {
        for (int i = 0; i < cartarraylist.size(); i++) {
            cartarraylist.get(i).setIsdisplay(display);
        }
    }

   /* private void checkPermissions() {
        int permissionLocation = ContextCompat.checkSelfPermission(BaseActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(BaseActivity.this,
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            }
        }

    }*/

    public void getWallet() {
        new HttpsRequest(Consts.GET_WALLET_API, parmsGetWallet, BookingProduct.this).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {
                        amt = response.getJSONObject("data").getString("amount");

                        if (amt.equalsIgnoreCase("")) {
                            amt = "0";
                        }
                        currency = response.getJSONObject("data").getString("currency_type");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {

                }
            }
        });
    }

    public void getpaymenttype() {

        Log.e("PAY_PARAMETERS", "cat id:--" + SubCategoryActivity.categoryid + " " + userDTO.getUser_id());
        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.getpaymenttype(SubCategoryActivity.categoryid, userDTO.getUser_id());
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                Log.e("RES", response.message());
                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        JSONObject object = new JSONObject(s);
                        Log.e("PAY_PARAMETERS", "" + s);

                        String[] separated = null;

                        int status = object.getInt("status");


                        if (status == 1) {
                            JSONArray jsonarray = object.getJSONArray("data");

                            for (int i = 0; i < jsonarray.length(); i++) {

                                JSONObject c = jsonarray.getJSONObject(i);

                                String payment_options = c.getString("payment_option_id");

                                separated = payment_options.split(",");


                            }


                            for (int j = 0; j < separated.length; j++) {
                                if (separated[j].equalsIgnoreCase("2")) {
                                    binding.cbwallet.setEnabled(true);
                                    paymentList.add("Kamaii Wallet");
                                }
                                if (separated[j].equalsIgnoreCase("3")) {
                                    binding.cbonline.setEnabled(true);
                                    paymentList.add("Online Payment");
                                }
                                if (separated[j].equalsIgnoreCase("1")) {
                                    binding.cbcash.setEnabled(true);
                                    binding.cbcash.setChecked(true);
                                    paymentList.add("Cash");
                                }
                            }

                            showSpinner();

                        } else {

                            binding.cbcash.setEnabled(false);
                            binding.cbwallet.setEnabled(false);
                            binding.cbonline.setEnabled(false);
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

    public void check_minimum_price() {

        new HttpsRequest(Consts.CHECK_MINIMUM_PRICE, paramsBookingOp4, BookingProduct.this).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                Log.e("SHIVLO", flag + "->" + msg);
                //1=show msg, 0=cart
                if (flag) {
                    try {
                        finalClick();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    sickbar(msg);
                    Log.d(TAG, "backResponse: " + msg);
                }
            }
        });
    }

    public boolean checkarss(String catid) {

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

    public void RouteOperation(String booking_id, String route, String source_latitude, String source_longitude, String dest_latitude, String dest_longitude) {
        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.RouteOperation(booking_id, route, source_latitude, source_longitude, dest_latitude, dest_longitude);
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ProjectUtils.pauseProgressDialog();
                try {
                    if (response.isSuccessful()) {

                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        JSONObject object = new JSONObject(s);
                        String status = object.getString("status");
                        String message = object.getString("message");
                        if (status.equalsIgnoreCase("1")) {
                            ViewAddressActivity.sub_category_idd = "";
                            ViewAddressActivity.catid = "";
                            ViewAddressActivity.mainkm = "";
                            ViewAddressActivity.thridcategory = "";
                            ViewAddressActivity.Sourceaddress = "";
                            ViewAddressActivity.Destiaddress = "";
                            ViewAddressActivity.startLati = 0;
                            ViewAddressActivity.startLongi = 0;
                            ViewAddressActivity.goalLongi = 0;
                            ViewAddressActivity.goalLati = 0;

                            if (userBookingList.get(0).getLocation_status().equalsIgnoreCase("1")) {
                                Intent intent = new Intent(BookingProduct.this, TrackingActivity.class);
                                intent.putExtra("category_type", true);
                                intent.putExtra(TRACK_BOOKING_VENDOR_LATITUDE, userBookingList.get(0).getArtistlivelat());
                                intent.putExtra(TRACK_BOOKING_VENDOR_LONGITUDE, userBookingList.get(0).getArtistlivelang());
                                intent.putExtra(TRACK_BOOKING_RIDER_ORDER_TRACKER, userBookingList.get(0).getRider_order());
                                Log.e("rider_tracker", "" + userBookingList.get(0).getRider_order());

                                intent.putExtra(TRACK_BOOKING_RIDER_LATITUDE, userBookingList.get(0).getRider_lat());
                                Log.e("riderLatitude", "" + userBookingList.get(0).getRider_lat());

                                intent.putExtra(TRACK_BOOKING_RIDER_LONGITUDE, userBookingList.get(0).getRider_long());
                                Log.e("riderLongitude", "" + userBookingList.get(0).getRider_long());

                                intent.putExtra(TRACK_BOOKING_VENDOR_NAME, userBookingList.get(0).getArtistName());
                                intent.putExtra(TRACK_ARTIST_ID, userBookingList.get(0).getArtist_id());
                                intent.putExtra(TRACK_BOOKING_DISCOUNT_TXT, artistDetailsDTO.getDis_price_text());
                                intent.putExtra(TRACK_BOOKING_DISCOUNT_DIGIT, artistDetailsDTO.getDis_price());
                                intent.putExtra(TRACK_BOOKING_VENDOR_IMAGE, userBookingList.get(0).getArtistImage());
                                intent.putExtra(TRACK_BOOKING_SOURCE_ADDRESS, userBookingList.get(0).getAddress());
                                intent.putExtra(TRACK_BOOKING_DESTINATION_ADDRESS, userBookingList.get(0).getDestinationaddress());
                                intent.putExtra(TRACK_BOOKING_TOTAL_AMOUNT, userBookingList.get(0).getPrice());
                                intent.putExtra(TRACK_BOOKING_PRODUCT_NAME, userBookingList.get(0).getProduct().get(0).getProduct_name());
                                intent.putExtra(TRACK_BOOKING_MOBILE_NUMBER, userBookingList.get(0).getArtistMobile());
                                intent.putExtra(TRACK_SOURCE_CUSTOMER_LATITUDE, userBookingList.get(0).getSource_lat());
                                intent.putExtra(TRACK_SOURCE_CUSTOMER_LONGITUDE, userBookingList.get(0).getSource_long());
                                intent.putExtra(TRACK_DESTINATION_CUSTOMER_LATITUDE, userBookingList.get(0).getDest_lat());
                                intent.putExtra(TRACK_DESTINATION_CUSTOMER_LONGITUDE, userBookingList.get(0).getDest_long());
                                intent.putExtra(TRACK_BOOKING_SCREEN_FLAG, GlobalUtils.MY_BOOKING_SCREEN);
                                intent.putExtra(TRACK_BOOKING_BOOKING_FLAG, userBookingList.get(0).getBooking_flag());
                                intent.putExtra(TRACK_BOOKING_ID, userBookingList.get(0).getId());
                                intent.putExtra(TRACK_BOOKING_PAYMENT_TYPE, userBookingList.get(0).getPay_type());
                                intent.putExtra(TRACK_BOOKING_DATE, userBookingList.get(0).getBooking_date());
                                intent.putExtra(TRACK_BOOKING_TIME, userBookingList.get(0).getBooking_time());
                                intent.putExtra(TRACK_VEHICLE_NUMBER, userBookingList.get(0).getProduct().get(0).getVehicle_number());
                                intent.putExtra(TRACK_CAT_ID, userBookingList.get(0).getProduct().get(0).getCategory_id());
                                intent.putExtra(TRACK_SUB_ID, userBookingList.get(0).getProduct().get(0).getSub_category_id());
                                intent.putExtra(TRACK_SUB_LEVEL_ID, userBookingList.get(0).getProduct().get(0).getSublevel_category());
                                intent.putExtra(BOOKING, "booking");
                                startActivity(intent);
                                finish();
                            } else {
                                Intent intent = new Intent(BookingProduct.this, BaseActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);

                                Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
                                List<Address> addresses = null;
                                Address obj = null;
                                try {
                                    addresses = geocoder.getFromLocation(Double.parseDouble(userBookingList.get(0).getArtistlivelat()), Double.parseDouble(userBookingList.get(0).getArtistlivelang()), 1);
                                    obj = addresses.get(0);
                                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + obj.getAddressLine(0));
                                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                    mapIntent.setPackage("com.google.android.apps.maps");
                                    mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    mContext.startActivity(mapIntent);
                                    finish();

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                    } else {
                        ProjectUtils.showToast(BookingProduct.this, "Something goes wrong...");

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

    @Override
    public void setAddress(int position, String id, String house_no, String street_add, String soc_name, double lati, double longi) {

        binding.tvAddress.setTextColor(getResources().getColor(R.color.dark_blue_color));

        final_user_address = street_add;
        address_id = id;

        ViewAddressActivity.startLati = lati;
        ViewAddressActivity.startLongi = longi;

        Log.e("ADDRESS_ADAPTER", " adapter " + house_no + " " + soc_name + " " + street_add);
        if (house_no.isEmpty() && soc_name.isEmpty()) {
            Log.e("ADDRESS_ADAPTER", "1");
            binding.tvAddress.setText(street_add);
        } else if (house_no.isEmpty()) {
            Log.e("ADDRESS_ADAPTER", "2");

            binding.tvAddress.setText(soc_name + ", " + street_add);
        } else if (soc_name.isEmpty()) {
            Log.e("ADDRESS_ADAPTER", "3");

            binding.tvAddress.setText(house_no + ", " + street_add);
        } else {
            Log.e("ADDRESS_ADAPTER", " 4 " + "house no:-" + house_no + "society:-" + soc_name);

            binding.tvAddress.setText(house_no + ", " + soc_name + ", " + street_add);
        }
        addressBottomSheet.dismiss();
    }

}

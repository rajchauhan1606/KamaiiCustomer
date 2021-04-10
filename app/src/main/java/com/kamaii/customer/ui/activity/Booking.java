package com.kamaii.customer.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.kamaii.customer.databinding.ActivityBookingBinding;
import com.kamaii.customer.ui.adapter.AdapterServices;
import com.kamaii.customer.ui.adapter.AdapterServicesNew;
import com.kamaii.customer.ui.adapter.CustomArrayAdapter;
import com.kamaii.customer.ui.adapter.PaymentBottomDialogadapter;
import com.kamaii.customer.ui.models.CartModel;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
import com.kamaii.customer.DTO.ArtistDetailsDTO;
import com.kamaii.customer.DTO.ProductDTO;
import com.kamaii.customer.DTO.ShippingDTO;
import com.kamaii.customer.DTO.UserBooking;
import com.kamaii.customer.DTO.UserDTO;
import com.kamaii.customer.R;
import com.kamaii.customer.api.apiClient;
import com.kamaii.customer.api.apiRest;
import com.kamaii.customer.https.HttpsRequest;
import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.interfacess.Helper;
import com.kamaii.customer.interfacess.OnSelectedItemListener;
import com.kamaii.customer.interfacess.SetonItemPlusListener;
import com.kamaii.customer.network.NetworkManager;
import com.kamaii.customer.preferences.SharedPrefrence;
import com.kamaii.customer.ui.adapter.AdapterCart;
import com.kamaii.customer.utils.CustomTextView;
import com.kamaii.customer.utils.CustomTextViewBold;
import com.kamaii.customer.utils.GlobalUtils;
import com.kamaii.customer.utils.ItemDecorationAlbumColumns;
import com.kamaii.customer.utils.ProjectUtils;
import com.paykun.sdk.PaykunApiCall;
import com.paykun.sdk.eventbus.Events;
import com.paykun.sdk.eventbus.GlobalBus;
import com.paykun.sdk.helper.PaykunHelper;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import static com.kamaii.customer.interfacess.Consts.SCREEN_TAG;
import static com.kamaii.customer.interfacess.Consts.TRACK_ARTIST_ID;
import static com.kamaii.customer.interfacess.Consts.TRACK_BOOKING_BOOKING_FLAG;
import static com.kamaii.customer.interfacess.Consts.TRACK_BOOKING_DATE;
import static com.kamaii.customer.interfacess.Consts.TRACK_BOOKING_DESTINATION_ADDRESS;
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


public class Booking extends AppCompatActivity {

    private ActivityBookingBinding binding;
    private Context mContext;
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private ArtistDetailsDTO artistDetailsDTO;
    private Date date;
    private SimpleDateFormat sdf1, timeZone;
    private HashMap<String, String> paramsBookingOp = new HashMap<>();
    private HashMap<String, String> paramsBookingOp2 = new HashMap<>();
    private HashMap<String, String> paramsBookingOp3 = new HashMap<>();
    private String TAG = Booking.class.getSimpleName();
    private JsonArray array, arrayquantitydays, arrayquantityvalue;
    private String artist_id = "", location_status = "1", changes_price = "";
    private int screen_tag = 0;
    private RadioGroup radiogroup;
    List<String> paymentList;
    RadioButton rb;
    RecyclerView rv_cart;
    RecyclerView rv_cart2;
    AdapterCart adapterCart;
    ArrayList<ProductDTO> cartarraylist = new ArrayList<>();
    private GridLayoutManager gridLayoutManager;
    double total = 0;
    double sub_total = 0;
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
    public CustomTextViewBold service_rupee_symbol, txtdestinationaddress, tverror, tverror1, sub_total_digits, sub_total_rupee_symbol;
    int error;
    String maxprice = "0", destaddress = "", km = "", destitime = "", Sourceaddress = "";
    public CustomTextView discounted_total_digits, discounted_rupee_symbol;
    LinearLayout laydestination;
    private ArrayList<UserBooking> userBookingList;
    public String ptype = "";
    private String paystatus = "0";
    private String amt = "", final_amount = "";
    private String currency = "";
    private HashMap<String, String> parmsGetWallet = new HashMap<>();
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
    CustomTextView booking_date, address,order_shipping_note;
    CustomTextViewBold tvHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_booking);
        mContext = Booking.this;
        spinner_txt = findViewById(R.id.spinner_txt);
        prefrence = SharedPrefrence.getInstance(mContext);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        sdf1 = new SimpleDateFormat(Consts.DATE_FORMATE_SERVER, Locale.ENGLISH);
        timeZone = new SimpleDateFormat(Consts.DATE_FORMATE_TIMEZONE, Locale.ENGLISH);
        date = new Date();
        service_charge_relative = findViewById(R.id.service_charge_relative);
        parmsGetWallet.put(Consts.USER_ID, userDTO.getUser_id());
        paramsBookingOp.put(Consts.DATE_STRING, sdf1.format(date).toString().toUpperCase());
        paramsBookingOp.put(Consts.TIMEZONE, timeZone.format(date));
        order_shipping_note = findViewById(R.id.order_shipping_note);
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
        service_rupee_symbol = findViewById(R.id.service_rupee_symbol);
        laydestination = findViewById(R.id.laydestination);
        order_now_text = findViewById(R.id.order_now_text);
        booking_date = findViewById(R.id.booking_date);
        address = findViewById(R.id.address);
        tvHeader = findViewById(R.id.tvHeader);
        cartHashmap = new HashMap<>();
        paymentList = new ArrayList<>();
        binding.cbwallet.setTextColor(getResources().getColor(R.color.gray_wallet));
        getWallet();
        order_shipping_note.setVisibility(View.GONE);

        binding.paymentLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPaymentDialog(spinner_txt.getText().toString());
            }
        });
        if (SubCategoryActivity.categoryid.equalsIgnoreCase("1")) {//Loan
            order_now_text.setText("Apply Now ");
            booking_date.setText("Order Date");
            address.setText("Order Address");
            tvHeader.setText("My Order Info");
        } else if (SubCategoryActivity.categoryid.equalsIgnoreCase("2")) { //Credit Card
            order_now_text.setText("Apply Now ");
            booking_date.setText("Order Date");
            address.setText("Order Address");
            tvHeader.setText("My Order Info");
        } else if (SubCategoryActivity.categoryid.equalsIgnoreCase("82")) { //Grocery-Kirana
            order_now_text.setText("Order Now ");
            booking_date.setText("Order Date");
            address.setText("Order Address");
            tvHeader.setText("My Order Info");
        } else if (SubCategoryActivity.categoryid.equalsIgnoreCase("65")) { //Insaunence
            order_now_text.setText("Buy Now ");
            booking_date.setText("Order Date");
            address.setText("Order Address");
            tvHeader.setText("My Order Info");
        } else if (SubCategoryActivity.categoryid.equalsIgnoreCase("79")) { //Furniture
            order_now_text.setText("Order Now ");
            booking_date.setText("Order Date");
            address.setText("Order Address");
            tvHeader.setText("My Order Info");
        } else if (SubCategoryActivity.categoryid.equalsIgnoreCase("87")) { // Mobile
            order_now_text.setText("Order Now ");
            booking_date.setText("Order Date");
            address.setText("Order Address");
            tvHeader.setText("My Order Info");
        } else if (SubCategoryActivity.categoryid.equalsIgnoreCase("95")) { //Tailor
            order_now_text.setText("Order Now ");
            booking_date.setText("Order Date");
            address.setText("Order Address");
            tvHeader.setText("My Order Info");
        } else if (SubCategoryActivity.categoryid.equalsIgnoreCase("67")) { //Job
            order_now_text.setText("Apply Now ");
            booking_date.setText("Order Date");
            address.setText("Order Address");
            tvHeader.setText("My Order Info");
        } else if (SubCategoryActivity.categoryid.equalsIgnoreCase("91")) { //Dry-Snack
            order_now_text.setText("Order Now ");
            booking_date.setText("Order Date");
            address.setText("Order Address");
            tvHeader.setText("My Order Info");
        } else if (SubCategoryActivity.categoryid.equalsIgnoreCase("85")) { //Cab
            order_now_text.setText("Book Now ");
            booking_date.setText("Booking Date");
            address.setText("Booking Address");
            tvHeader.setText("My Booking Info");
        } else {
            order_now_text.setText("Order Now ");
            booking_date.setText("Order Date");
            address.setText("Order Address");
            tvHeader.setText("My Order Info");
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


            Bundle b = getIntent().getExtras();
            cartarraylist = (ArrayList<ProductDTO>) b.getSerializable(Consts.CARTDATA);

            setdisplay(true);
            adapterCart = new AdapterCart(Booking.this, cartarraylist, onItemListener, setonItemPlusListener, userDTO.getUser_id(), cartHashmap);
            rv_cart.setLayoutManager(gridLayoutManager);
            rv_cart.addItemDecoration(new ItemDecorationAlbumColumns(
                    2,
                    4));
            rv_cart.setAdapter(adapterCart);


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
                binding.tvAddress.setText(getResources().getString(R.string.tap_to_select_address));
                getsourceaddress();
            }
            totalservicecharge();
        }
        getshipping();
        setUiAction();

        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {
                    if (rb.getText().toString().equalsIgnoreCase("Takeway")) {
                        adapterCart = new AdapterCart(Booking.this, cartarraylist, onItemListener, setonItemPlusListener, userDTO.getUser_id(), cartHashmap);
                        rv_cart.setAdapter(adapterCart);

                        error = 0;
                        llerrormsg.setVisibility(View.GONE);
                        binding.llTime.setVisibility(View.VISIBLE);
                        binding.llDate.setVisibility(View.VISIBLE);
                        binding.serviceChargeRelative.setVisibility(View.GONE);
                        binding.subtotalRelative.setVisibility(View.GONE);
                        for (int i = 0; i < shippingDTOArrayList.size(); i++) {
                            if (shippingDTOArrayList.get(i).getSub_cat_id().equalsIgnoreCase(ViewAddressActivity.sub_category_idd)) {

                                if (shippingDTOArrayList.get(i).getMy_location().equalsIgnoreCase("0")) {
                                    error = 1;
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
                        adapterCart.NotifyAll(cartarraylist);

                        total();
                        location_status = "0";

                        setdisplay(false);

                        binding.tvAddress.setEnabled(false);

                        binding.tvAddress.setText(getResources().getString(R.string.tap_to_select_address));
                        getsourceaddress();
                    } else {
                        adapterCart = new AdapterCart(Booking.this, cartarraylist, onItemListener, setonItemPlusListener, userDTO.getUser_id(), cartHashmap);
                        rv_cart.setAdapter(adapterCart);
                        error = 0;
                        llerrormsg.setVisibility(View.GONE);
                        binding.llTime.setVisibility(View.VISIBLE);
                        binding.llDate.setVisibility(View.VISIBLE);

                        bookArtist2("1");

                        binding.serviceChargeRelative.setVisibility(View.VISIBLE);

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

                        adapterCart.NotifyAll(cartarraylist);
                        totalservicecharge();
                        setdisplay(true);

                        if (ViewAddressActivity.sub_category_idd.equals("242") || ViewAddressActivity.sub_category_idd.equals("434")) {
                            Log.e("rickshaw1", "rickshaw1 called");
                            binding.serviceTxt.setText("Driver Allowance123");
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
        radiogroup.setVisibility(View.GONE);
        for (
                int i = 0; i < cartarraylist.size(); i++) {

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
        bookArtist2("1");
    }

    public void update_qty_booking(String qty, String pid) {
        cartHashmap.put(pid, qty);
    }

    public void remove_qty_booking(String qty, String pid, int pos) {
        Log.d(TAG, "remove_qty_booking: kamaiii  " + cartHashmap.toString());
        cartHashmap.remove(pid);
        cartarraylist.remove(pos);
        Log.d(TAG, "remove_qty_booking: kamaiii  " + cartHashmap.toString());
        if (cartarraylist.size() == 0) {
            onBackPressed();
        } else {
            adapterCart.NotifyAll_qty(cartarraylist, cartHashmap);
        }

    }

    private void getPaymentDialog(String paymentselectedtype) {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.payment_mode_spinner_layout);

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
                        Intent intent = new Intent(Booking.this, AddMoney.class);
                        intent.putExtra("from_booking", true);
                        intent.putExtra("diff_amt", total);
                        startActivity(intent);
                        //Toast.makeText(mContext, "am :"+total, Toast.LENGTH_SHORT).show();
                    } else {
                        binding.spinnerTxt.setText(ptype);
                    }
                } else {
                    binding.spinnerTxt.setText(ptype);
                }
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.show();
    }

    public void bookArtist2(String quentity) {
        new HttpsRequest(Consts.ADD_TO_CART_TEST_API, paramsBookingOp2, Booking.this).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                if (flag) {
                    try {

                        String s = response.toString();
                        Log.e("GETDATA_RES_1 nimlo ", "" + s);
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
                        cab_total = Double.parseDouble(TOTAL1);
                        cab_sub_total = Double.parseDouble(SUB_TOTAL1);
                        cab_allowence = Double.parseDouble(SHIP_charge);
                        paramsBookingOp.put("rider1_id", rider1_id);
                        paramsBookingOp.put("total_rider_charges", total_rider_charges);

                        if (track.equals("1")) {

                        }
                        changetotalprice(TOTAL1, SUB_TOTAL1, SHIP_charge);


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


    public void changetotalprice(String texting, String SUB_TOTAL, String SHIP_charge) {
        if (SHIP_charge.equals("0.00")) {
            binding.serviceRupeeSymbol.setVisibility(View.GONE);
            binding.serviceDigitTxt.setText("Free");
        } else {
            binding.serviceRupeeSymbol.setVisibility(View.VISIBLE);
            binding.serviceDigitTxt.setText(SHIP_charge);
        }
        binding.rupeeSymbol.setText(Html.fromHtml("&#x20B9;"));
        binding.tvPrice.setText(texting);
        binding.totalBtnTxt.setText(Html.fromHtml("&#x20B9;" + texting));
        binding.subTotalRupeeSymbol.setText(Html.fromHtml("&#x20B9;"));
        binding.subTotalDigits.setText(SUB_TOTAL);
        binding.serviceRupeeSymbol.setText(Html.fromHtml("&#x20B9;"));
    }

    public void getshipping() {

        parmsshipping.put(Consts.USER_ID, artist_id);

        parmsshipping.put(Consts.SUB_CAT_ID, ViewAddressActivity.sub_category_idd);
        ProjectUtils.showProgressDialog(Booking.this, true, getResources().getString(R.string.please_wait));


        new HttpsRequest(Consts.GET_SERVICE_SHIPPING2, parmsshipping, Booking.this).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {

                        Log.e("GET_SHIP nimlo: ", "" + response.toString());
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
                        Log.e("rider_name", "" + rider_name);
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
            adapterCart.notifyDataSetChanged();
            paramsBookingOp3.clear();
            for (int i = 0; i < cartarraylist.size(); i++) {

                paramsBookingOp3.put("ARTIST_ID" + i, cartarraylist.get(i).getUser_id());
                paramsBookingOp3.put("PRICE" + i, cartarraylist.get(i).getPrice());
                paramsBookingOp3.put("SERVICE_ID" + i, cartarraylist.get(i).getId());
                paramsBookingOp3.put("QUANTITYDAYS" + i, "1");
                Log.e("SERVICE_ID", "" + cartarraylist.get(i).getId());
            }

            new HttpsRequest(Consts.ADD_TO_CART_TEST_API, paramsBookingOp3, Booking.this).stringPost(TAG, new Helper() {
                @Override
                public void backResponse(boolean flag, String msg, JSONObject response) {

                    if (flag) {
                        try {
                            String s = response.toString();
                            Log.e("GETDATA_RES1", "" + s);

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
                            ((Booking) Booking.this).changetotalprice(TOTAL, SUB_TOTAL, SHIP_charge);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.d(TAG, "backResponse: " + msg);
                    }
                }
            });
        }
    };

    public void showSpinner() {

        ArrayAdapter customArrayAdapter = new CustomArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, paymentList);

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
        binding.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        binding.tvBookingDate.setText(sdf1.format(date).toString().toUpperCase());


        if (!artistDetailsDTO.getLocation().equalsIgnoreCase("")) {

            paramsBookingOp.put(Consts.LATITUDE, artistDetailsDTO.getLatitude());
            paramsBookingOp.put(Consts.LONGITUDE, artistDetailsDTO.getLongitude());
        }
        binding.tvAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkManager.isConnectToInternet(mContext)) {
                    findPlace();
                } else {
                    ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
                }
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

                if (binding.tvAddress.getText().toString().trim().length() > 0) {
                    double totalprice = Double.parseDouble(binding.tvPrice.getText().toString());
                    double mprice = Double.parseDouble(maxprice);
                    if (binding.spinnerTxt.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(Booking.this, "Please Select Payment Type", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (binding.spinnerTxt.getText().toString().equalsIgnoreCase("Kamaii Wallet")) {

                        if (totalprice <= Double.parseDouble(amt)) {

                            final_amount = binding.tvPrice.getText().toString();
                        } else {
                            Toast.makeText(Booking.this, "Insufficient Balance", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                    try {
                        if (checkarss(ViewAddressActivity.catid)) {
                            if (!cartarraylist.get(0).getMaxmiumvalue().equalsIgnoreCase("")) {
                                double maxmimumvalue = Double.parseDouble(cartarraylist.get(0).getMaxmiumvalue());
                                double km = Double.parseDouble(ViewAddressActivity.mainkm);

                                if (maxmimumvalue < km) {
                                    Toast.makeText(Booking.this, "Not Available", Toast.LENGTH_LONG).show();
                                    return;
                                } else {

                                }

                            }
                        } else {
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    if (radiopos == 0) {
                        try {
                            if (error != 1) {

                                new AlertDialog.Builder(mContext)
                                        .setIcon(R.mipmap.ic_launcher)
                                        .setTitle("Are you sure booking with")

                                        .setMessage("Booking Date:" + " " + binding.tvBookingDate.getText().toString() + "\n \n" + "Booking Location:" + " " + binding.tvAddress.getText().toString())
                                        .setCancelable(false)
                                        .setPositiveButton(mContext.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                if (binding.spinnerTxt.getText().toString().equalsIgnoreCase("Cash")) {
                                                    final_amount = binding.tvPrice.getText().toString();

                                                    if (screen_tag == 1) {
                                                        paramsBookingOp.put(Consts.PRICE, artistDetailsDTO.getPrice());

                                                        submit();
                                                    } else if ((screen_tag == 2)) {
                                                        bookForService();
                                                    }
                                                    dialog.dismiss();


                                                } else if (binding.spinnerTxt.getText().toString().equalsIgnoreCase("Kamaii Wallet")) {
                                                    if (totalprice <= Double.parseDouble(amt)) {

                                                        final_amount = binding.tvPrice.getText().toString();
                                                        if (screen_tag == 1) {
                                                            paramsBookingOp.put(Consts.PRICE, artistDetailsDTO.getPrice());
                                                            submit();
                                                        } else if ((screen_tag == 2)) {
                                                            bookForService();
                                                        }
                                                    } else {
                                                        Toast.makeText(Booking.this, "Insufficient Balance", Toast.LENGTH_LONG).show();
                                                        return;
                                                    }
                                                    dialog.dismiss();
                                                } else {
                                                    double total = totalprice;

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
                                                    new PaykunApiCall.Builder(Booking.this).sendJsonObject(object);
                                                    dialog.dismiss();
                                                }
                                            }
                                        })
                                        .setNegativeButton(mContext.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();

                                            }
                                        })
                                        .show();
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (error != 1) {
                            if (binding.tvAddress.getText().toString().equalsIgnoreCase(getResources().getString(R.string.tap_to_select_address))) {
                                ProjectUtils.showLong(mContext, getResources().getString(R.string.val_address));
                            } else {
                                try {
                                    if (totalprice < mprice) {

                                        new AlertDialog.Builder(mContext)
                                                .setIcon(R.mipmap.ic_launcher)
                                                .setMessage("Please note this service minimum amount is " + maxprice + " Rs")
                                                .setCancelable(false)
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        dialog.dismiss();

                                                    }
                                                })
                                                .show();
                                    } else {


                                        String daddress = "";
                                        if (!destaddress.equalsIgnoreCase("")) {
                                            daddress = "Destination Location:" + " " + txtdestinationaddress.getText().toString();
                                        }

                                        if (binding.spinnerTxt.getText().toString().equalsIgnoreCase("Cash")) {

                                            final_amount = binding.tvPrice.getText().toString();

                                            if (screen_tag == 1) {
                                                paramsBookingOp.put(Consts.PRICE, artistDetailsDTO.getPrice());

                                                submit();
                                            } else if ((screen_tag == 2)) {
                                                bookForService();
                                            }
                                            dialog.dismiss();


                                        } else if (binding.spinnerTxt.getText().toString().equalsIgnoreCase("Kamaii Wallet")) {


                                            if (totalprice <= Double.parseDouble(amt)) {

                                                final_amount = binding.tvPrice.getText().toString();
                                                if (screen_tag == 1) {
                                                    paramsBookingOp.put(Consts.PRICE, artistDetailsDTO.getPrice());

                                                    submit();
                                                } else if ((screen_tag == 2)) {
                                                    bookForService();
                                                }
                                            } else {
                                                Toast.makeText(Booking.this, "Insufficient Balance", Toast.LENGTH_LONG).show();
                                                return;
                                            }
                                            dialog.dismiss();


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
                                            new PaykunApiCall.Builder(Booking.this).sendJsonObject(object);
                                            dialog.dismiss();
                                        }

                                    }


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    }
                } else {
                    ProjectUtils.showLong(mContext, getResources().getString(R.string.val_address));
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
                Toast.makeText(Booking.this, "Your Transaction is succeed", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(Booking.this, "Your Transaction is failed", Toast.LENGTH_SHORT).show();
        } else if (message.getResults().equalsIgnoreCase(PaykunHelper.MESSAGE_SERVER_ISSUE)) {
            // do your stuff here
            Toast.makeText(Booking.this, PaykunHelper.MESSAGE_SERVER_ISSUE, Toast.LENGTH_SHORT).show();
        } else if (message.getResults().equalsIgnoreCase(PaykunHelper.MESSAGE_ACCESS_TOKEN_MISSING)) {
            // do your stuff here
            Toast.makeText(Booking.this, "Access Token missing", Toast.LENGTH_SHORT).show();
        } else if (message.getResults().equalsIgnoreCase(PaykunHelper.MESSAGE_MERCHANT_ID_MISSING)) {
            // do your stuff here
            Toast.makeText(Booking.this, "Merchant Id is missing", Toast.LENGTH_SHORT).show();
        } else if (message.getResults().equalsIgnoreCase(PaykunHelper.MESSAGE_INVALID_REQUEST)) {
            Toast.makeText(Booking.this, "Invalid Request", Toast.LENGTH_SHORT).show();
        } else if (message.getResults().equalsIgnoreCase(PaykunHelper.MESSAGE_NETWORK_NOT_AVAILABLE)) {
            Toast.makeText(Booking.this, "Network is not available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Register this activity to listen to event.
        GlobalBus.getBus().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unregister from activity
        GlobalBus.getBus().unregister(this);
    }

    public void bookForService() {
        array = new JsonArray();

        arrayquantitydays = new JsonArray();
        arrayquantityvalue = new JsonArray();

        for (int i = 0; i < cartarraylist.size(); i++) {

            array.add(cartarraylist.get(i).getId());
            if (cartarraylist.get(i).getCategory_id().trim().equals("85") || cartarraylist.get(i).getCategory_id().trim().equals("126") || cartarraylist.get(i).getCategory_id().trim().equals("74")) {
                if (cartarraylist.get(i).getQuantitydays() == null) {
                    cartarraylist.get(i).setQuantityvalue("1");
                }
                arrayquantitydays.add(cartarraylist.get(i).getQuantitydays());
                arrayquantityvalue.add(cartarraylist.get(i).getQuantityvalue());
            } else {
                arrayquantitydays.add(cartHashmap.get(cartarraylist.get(i).getId()));
                arrayquantityvalue.add("");
            }
        }
        paramsBookingOp.put(Consts.SERVICE_ID, array.toString());
        paramsBookingOp.put(Consts.CHANGE_PRICE, changes_price);
        paramsBookingOp.put(Consts.MAXMIUMVALUE, arrayquantityvalue.toString());
        paramsBookingOp.put(Consts.QUANTITYDAYS, arrayquantitydays.toString());

        submit();

    }


    public void sickbar(String msg) {
        binding.RRsncbarrr.setVisibility(View.VISIBLE);
        Snackbar snackbar = Snackbar.make(binding.RRsncbarrr, msg, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        snackbar.show();
    }

    public void bookArtist() {
        paramsBookingOp.put(Consts.USER_ID, userDTO.getUser_id());
        paramsBookingOp.put(Consts.ARTIST_ID, artistDetailsDTO.getUser_id());
        paramsBookingOp.put(Consts.PRICE, binding.tvPrice.getText().toString());
        paramsBookingOp.put(Consts.LOCATION_STATUS, location_status);
        paramsBookingOp.put(Consts.DESTI_ADDRESS, destaddress);
        paramsBookingOp.put(Consts.KM, km);
        paramsBookingOp.put(Consts.DESTI_TIME, destitime);
        paramsBookingOp.put(Consts.PTYPE, ptype);
        paramsBookingOp.put(Consts.TOTAL_AMOUNT, final_amount);
        paramsBookingOp.put(Consts.PAY_STATUS, paystatus);
        paramsBookingOp.put(Consts.ADDRESS, binding.tvAddress.getText().toString());
        paramsBookingOp.put(Consts.SOURCE_LAT, String.valueOf(ViewAddressActivity.startLati));
        paramsBookingOp.put(Consts.SOURCE_LONG, String.valueOf(ViewAddressActivity.startLongi));
        paramsBookingOp.put(Consts.DEST_LAT, String.valueOf(ViewAddressActivity.goalLati));
        paramsBookingOp.put(Consts.DEST_LONG, String.valueOf(ViewAddressActivity.goalLongi));


        ProjectUtils.showProgressDialog(mContext, false, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.BOOK_ARTIST_API2, paramsBookingOp, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

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
                    AddressOperation(userDTO.getUser_id(), binding.tvAddress.getText().toString(), String.valueOf(ViewAddressActivity.startLati), String.valueOf(ViewAddressActivity.startLongi), "3");
                    AddressOperation(userDTO.getUser_id(), destaddress, String.valueOf(ViewAddressActivity.goalLati), String.valueOf(ViewAddressActivity.goalLongi), "3");
                    Log.e("preference_route", "" + prefrence.getValue(ROUTE_PATH));
                    RouteOperation(userBookingList.get(0).getId(), prefrence.getValue(ROUTE_PATH), String.valueOf(ViewAddressActivity.startLati), String.valueOf(ViewAddressActivity.startLongi), String.valueOf(ViewAddressActivity.goalLati), String.valueOf(ViewAddressActivity.goalLongi));

                } else {
                    ProjectUtils.showToast(mContext, msg);
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
        Intent intent = new PlaceAutocomplete.IntentBuilder()
                .accessToken(getString(R.string.mapbox_access_token))
                .placeOptions(PlaceOptions.builder()
                        .backgroundColor(Color.parseColor("#EEEEEE"))
                        .limit(10)

                        .country("IN")
                        .build(PlaceOptions.MODE_CARDS))
                .build(Booking.this);
        startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
    }


    public void getsourceaddress() {
        Geocoder geocoder = new Geocoder(Booking.this, Locale.getDefault());
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
            binding.tvAddress.setText(obj.getAddressLine(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {

            CarmenFeature selectedCarmenFeature = PlaceAutocomplete.getPlace(data);

            binding.tvAddress.setText(selectedCarmenFeature.placeName());
            ViewAddressActivity.startLati = ((Point) selectedCarmenFeature.geometry()).latitude();
            ViewAddressActivity.startLongi = ((Point) selectedCarmenFeature.geometry()).longitude();

            paramsBookingOp.put(Consts.LATITUDE, String.valueOf(((Point) selectedCarmenFeature.geometry()).latitude()));
            paramsBookingOp.put(Consts.LONGITUDE, String.valueOf(((Point) selectedCarmenFeature.geometry()).longitude()));

        } else {
            Log.d("TAG", "hello");
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
                    //total = total + Double.parseDouble(cartarraylist.get(i).getPrice()) + Double.parseDouble(cartarraylist.get(i).getService_charge());
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

    public void getWallet() {
        new HttpsRequest(Consts.GET_WALLET_API, parmsGetWallet, Booking.this).stringPost(TAG, new Helper() {
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

        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.getpaymenttype(SubCategoryActivity.categoryid,userDTO.getUser_id());
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                Log.e("RES", response.message());
                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        JSONObject object = new JSONObject(s);
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
                                Intent intent = new Intent(Booking.this, TrackingActivity.class);
                                intent.putExtra("category_type", "cab");
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

                                Intent intent = new Intent(Booking.this, BaseActivity.class);
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
                        ProjectUtils.showToast(Booking.this, "Something goes wrong...");

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
}

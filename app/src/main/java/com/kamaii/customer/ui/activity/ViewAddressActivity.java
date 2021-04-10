package com.kamaii.customer.ui.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.kamaii.customer.DTO.AllAtristListDTO;
import com.kamaii.customer.DTO.ArtistDetailsDTO;
import com.kamaii.customer.DTO.ProductDTO;
import com.kamaii.customer.DTO.UserDTO;
import com.kamaii.customer.R;
import com.kamaii.customer.api.apiClient;
import com.kamaii.customer.api.apiRest;
import com.kamaii.customer.dialogs.SelectBottomAddressDialogFragment;
import com.kamaii.customer.https.HttpsRequest;
import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.interfacess.Helper;
import com.kamaii.customer.interfacess.OnBottomSelectedItemListener;
import com.kamaii.customer.interfacess.OnSelectedItemListener;
import com.kamaii.customer.network.NetworkManager;
import com.kamaii.customer.preferences.SharedPrefrence;
import com.kamaii.customer.ui.adapter.CarSelectionAdapter;
import com.kamaii.customer.ui.adapter.DailyAdapter;
import com.kamaii.customer.ui.adapter.RecentAddressAdapter;
import com.kamaii.customer.ui.adapter.ThirdCateAdapter;
import com.kamaii.customer.ui.models.DailyModel;
import com.kamaii.customer.ui.models.RecentAddress;
import com.kamaii.customer.ui.models.ThirdCateModel;
import com.kamaii.customer.utils.CustomTextView;
import com.kamaii.customer.utils.CustomTextViewBold;
import com.kamaii.customer.utils.ProjectUtils;
import com.mapbox.mapboxsdk.location.modes.CameraMode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import pl.bclogic.pulsator4droid.library.PulsatorLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.widget.Toast.LENGTH_LONG;
import static com.kamaii.customer.interfacess.Consts.ROUTE_PATH;
import static com.kamaii.customer.interfacess.Consts.USER_ID;


public class ViewAddressActivity extends AppCompatActivity implements com.google.android.gms.maps.OnMapReadyCallback {
    GoogleMap mapView;
    private String TAG = ViewAddressActivity.class.getSimpleName();
    CustomTextViewBold tvHeader;
    private String LOG_TAG = "Google Places Autocomplete";
    public static double startLati, startLongi, goalLati, goalLongi;
    CardView cardBooksourcedesti;
    TextView txtkm, txtduration;
    private SharedPrefrence prefrence;
    public static String sub_category_idd = "", subcatname = "", catid = "", mainkm = "", thridcategory = "", destitime = "";
    LinearLayout laykm;
    ImageView llBack;
    HashMap<String, String> parms = new HashMap<>();
    private ArrayList<AllAtristListDTO> allAtristListDTOList = new ArrayList<>();
    private UserDTO userDTO;
    double latilive = 0;
    double longilive = 0;
    String iconthirdacat = "";
    public static String Sourceaddress = "", Destiaddress = "";
    int selectBox = 0;
    CardView cardlocal, card_address;
    DailyAdapter dailyAdapter;
    private ArrayList<DailyModel> dailyModelArrayList = new ArrayList<>();
    RecyclerView rec_category;
    PulsatorLayout pulsator;
    private ArtistDetailsDTO artistDetailsDTO;
    private ArrayList<ProductDTO> productDTOList;
    private ArrayList<ProductDTO> productDTOListtemp;
    private JsonArray array;
    private Handler mBackgroundHandler;
    private static final int START_COUNTER = 1001;
    private static final int FINISH_COUNTER = 3002;
    private static int AUTOCOMPLETE_REQUEST_CODE = 1;
    private static int AUTOCOMPLETE_REQUEST_CODE_TWO = 2;
    private LatLng mOrigin;
    private LatLng mDestination;
    private Polyline mPolyline;
    ArrayList<LatLng> mMarkerPoints;
    private GoogleMap mMap;
    ArrayList<ThirdCateModel> thirdCateModelArrayList = new ArrayList<>();
    @BindView(R.id.etAddressSelectsource)
    CustomTextViewBold etAddressSelectsource;
    @BindView(R.id.etAddressSelectdesti)
    CustomTextViewBold etAddressSelectdesti;
    ArrayList<RecentAddress> recentAddressArrayList = new ArrayList<>();

    @BindView(R.id.rec_recent)
    RecyclerView rec_recent;

    @BindView(R.id.layout_recent)
    LinearLayout layout_recent;

    @BindView(R.id.layout_address_main)
    LinearLayout layout_address_main;

    @BindView(R.id.lay_source_location)
    LinearLayout lay_source_location;

    @BindView(R.id.lay_dest_location)
    LinearLayout lay_dest_location;


    @BindView(R.id.lay_address_work)
    LinearLayout lay_address_work;

    @BindView(R.id.lay_address_home)
    LinearLayout lay_address_home;

    @BindView(R.id.img_drop)
    ImageView img_drop;

    @BindView(R.id.remove_home)
    ImageView remove_home;

    @BindView(R.id.remove_work)
    ImageView remove_work;

    @BindView(R.id.address_home)
    CustomTextView address_home;

    @BindView(R.id.address_work)
    CustomTextView address_work;


    @BindView(R.id.address_home_label)
    CustomTextViewBold address_home_label;

    @BindView(R.id.rv_third)
    RecyclerView rv_third;
    @BindView(R.id.bottomlayoutNew)
    LinearLayout bottomlayoutNew;

    //bottom layout data
    @BindView(R.id.bottomlayout)
    LinearLayout bottomlayout;

    @BindView(R.id.etAddressSelectsourceNew)
    CustomTextViewBold etAddressSelectsourceNew;

    @BindView(R.id.etAddressSelectdestiNew)
    CustomTextViewBold etAddressSelectdestiNew;

    @BindView(R.id.action_bar_menus)
    CardView action_bar_menus;

    @BindView(R.id.action_bar_menusNew)
    FloatingActionButton action_bar_menusNew;

    @BindView(R.id.rv_thirdNew)
    RecyclerView rv_thirdNew;
    @BindView(R.id.item_count_digit)
    CustomTextViewBold item_count_digit;
    @BindView(R.id.address_work_label)

    CustomTextViewBold address_work_label;
    boolean isFirstTime = true;
    RecentAddressAdapter recentAddressAdapter;
    String homeId = "", workId = "", homelatitude = "", homelangitude = "", worklatitude = "", worklangitude = "";
    String homeAddress = "", workAddress = "";
    int HOME = 1, WORK = 2;
    private static final String API_KEY = "AIzaSyCIpO2XuPQr8HDOg92EU0pRkP0G9rKvFoY";
    ProgressDialog progressDialog;
    ThirdCateAdapter thirdCateAdapter;
    CarSelectionAdapter carSelectionAdapter;

    Fragment frame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewaddress);
        ButterKnife.bind(ViewAddressActivity.this);
        Places.initialize(getApplicationContext(), API_KEY);
        PlacesClient placesClient = Places.createClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        progressDialog = new ProgressDialog(ViewAddressActivity.this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        mMarkerPoints = new ArrayList<>();
        List<Place.Field> fields = Arrays.asList(Place.Field.LAT_LNG, Place.Field.ADDRESS,Place.Field.NAME);

        rv_third.setLayoutManager(new LinearLayoutManager(ViewAddressActivity.this, LinearLayoutManager.HORIZONTAL, false));
        rv_thirdNew.setLayoutManager(new LinearLayoutManager(ViewAddressActivity.this, LinearLayoutManager.VERTICAL, false));

        thirdCateAdapter = new ThirdCateAdapter(this, thirdCateModelArrayList, onItemListenerthird);
        rv_third.setAdapter(thirdCateAdapter);

        carSelectionAdapter = new CarSelectionAdapter(this, thirdCateModelArrayList, onItemListenerthird, destitime, mainkm);
        rv_thirdNew.setAdapter(carSelectionAdapter);


        cardlocal = findViewById(R.id.cardlocal);
        card_address = findViewById(R.id.card_address);
        rec_category = findViewById(R.id.rvdaily);
        pulsator = findViewById(R.id.pulsator);
        tvHeader = findViewById(R.id.tvHeader);
        pulsator.start();
        prefrence = SharedPrefrence.getInstance(ViewAddressActivity.this);


        findViewById(R.id.know_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewAddressActivity.this, SafetyActivity.class));
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rec_recent.setLayoutManager(layoutManager);

        Log.e("recentAddressArrayList",""+recentAddressArrayList.size());
        recentAddressAdapter = new RecentAddressAdapter(this, recentAddressArrayList);
        rec_recent.setAdapter(recentAddressAdapter);
        recentAddressAdapter.setOnSelectedItemListener(onSelectedItemListener);
        if (getIntent().getStringExtra(Consts.SUB_CATEGORY_ID) != null) {
            sub_category_idd = getIntent().getStringExtra(Consts.SUB_CATEGORY_ID);
            subcatname = getIntent().getStringExtra(Consts.Sub_CAT_NAME);
            catid = getIntent().getStringExtra(Consts.CATEGORY_ID);

        } else {
            Intent intent = new Intent(this, BaseActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
        cardBooksourcedesti = findViewById(R.id.cardBooksourcedesti);
        txtkm = findViewById(R.id.txtkm);
        laykm = findViewById(R.id.laykm);
        txtduration = findViewById(R.id.txtduration);
        llBack = findViewById(R.id.llBack);


        if (sub_category_idd.equalsIgnoreCase("242")) {
            destitime = "10";
            mainkm = "10";
            cardlocal.setVisibility(View.VISIBLE);
            card_address.setVisibility(View.GONE);
            tvHeader.setText("Local Rental Package");

            laykm.setVisibility(View.VISIBLE);//old opening
            laykm.setVisibility(View.GONE);//new hiding
            txtkm.setText(mainkm);
            txtduration.setText(destitime);
            carSelectionAdapter.notifyDataChanged(thirdCateModelArrayList, destitime, mainkm);
        } else {
            card_address.setVisibility(View.VISIBLE);
            cardlocal.setVisibility(View.GONE);
            tvHeader.setText("Choose Address");
        }
        dailyModelArrayList.add(new DailyModel("1 hr", "10", "true"));
        dailyModelArrayList.add(new DailyModel("2 hr", "20", "false"));
        dailyModelArrayList.add(new DailyModel("3 hr", "30", "false"));
        dailyModelArrayList.add(new DailyModel("4 hr", "40", "false"));
        dailyModelArrayList.add(new DailyModel("5 hr", "50", "false"));
        dailyModelArrayList.add(new DailyModel("6 hr", "60", "false"));
        dailyModelArrayList.add(new DailyModel("7 hr", "70", "false"));
        dailyModelArrayList.add(new DailyModel("8 hr", "80", "false"));
        dailyModelArrayList.add(new DailyModel("9 hr", "90", "false"));
        dailyModelArrayList.add(new DailyModel("10 hr", "100", "false"));
        dailyModelArrayList.add(new DailyModel("11 hr", "110", "false"));
        dailyModelArrayList.add(new DailyModel("12 hr", "120", "false"));

        rec_category.setLayoutManager(new LinearLayoutManager(ViewAddressActivity.this, LinearLayoutManager.HORIZONTAL, false));
        dailyAdapter = new DailyAdapter(ViewAddressActivity.this, dailyModelArrayList, onItemListener);
        rec_category.setAdapter(dailyAdapter);

        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        parms.put(Consts.USER_ID, userDTO.getUser_id());

        parms.put(Consts.SUB_CATEGORY_ID, sub_category_idd);


        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        action_bar_menusNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomlayout.setVisibility(View.VISIBLE);
                pulsator.setVisibility(View.GONE);
                action_bar_menus.setVisibility(View.VISIBLE);
                action_bar_menusNew.setVisibility(View.GONE);
                bottomlayoutNew.setVisibility(View.GONE);
            }
        });
        etAddressSelectsource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectBox = 0;
                Intent intentt = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                        .setCountry("IN")
                        .build(ViewAddressActivity.this);

                startActivityForResult(intentt, AUTOCOMPLETE_REQUEST_CODE);

            }
        });
        etAddressSelectsourceNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectBox = 0;
                Intent intentt = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                        .setCountry("IN")
                        .build(ViewAddressActivity.this);

                startActivityForResult(intentt, AUTOCOMPLETE_REQUEST_CODE);

            }
        });

        lay_dest_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectBox = 1;

                Intent intentt = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                        .setCountry("IN")
                        .build(ViewAddressActivity.this);

                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(ViewAddressActivity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                startActivityForResult(intentt, AUTOCOMPLETE_REQUEST_CODE_TWO);

            }
        });
        etAddressSelectdestiNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectBox = 1;

                Intent intentt = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                        .setCountry("IN")
                        .build(ViewAddressActivity.this);

                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(ViewAddressActivity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                startActivityForResult(intentt, AUTOCOMPLETE_REQUEST_CODE_TWO);

            }
        });


        cardBooksourcedesti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sub_category_idd.equalsIgnoreCase("242")) {
                    pulsator.setVisibility(View.GONE);
                    if (NetworkManager.isConnectToInternet(ViewAddressActivity.this)) {
                        mBackgroundHandler.obtainMessage(FINISH_COUNTER, true).sendToTarget();
                        getService(startLati, startLongi);

                    } else {
                    }

                } else {
                    if (mainkm.trim().isEmpty()) {
                        mainkm = "0";
                    }
                    if (etAddressSelectsource.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(ViewAddressActivity.this, "Please Enter Source Address", Toast.LENGTH_LONG).show();
                    } else if (etAddressSelectdesti.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(ViewAddressActivity.this, "Please Enter Destination Address", Toast.LENGTH_LONG).show();
                    } else if (mainkm.equalsIgnoreCase("0")) {
                        Toast.makeText(ViewAddressActivity.this, "Not Available", Toast.LENGTH_LONG).show();
                    } else if (Double.parseDouble(mainkm) > 3000) {
                        Toast.makeText(ViewAddressActivity.this, "Not Available", Toast.LENGTH_LONG).show();
                    } else {
                        pulsator.setVisibility(View.GONE);
                        if (NetworkManager.isConnectToInternet(ViewAddressActivity.this)) {
                            mBackgroundHandler.obtainMessage(FINISH_COUNTER, true).sendToTarget();
                            getService(startLati, startLongi);
                        } else {
                        }
                    }
                }
            }
        });
        try {
            startLati = Double.parseDouble(prefrence.getValue(Consts.LATITUDE));
            startLongi = Double.parseDouble(prefrence.getValue(Consts.LONGITUDE));

        } catch (Exception e) {

        }

        HandlerThread handlerThread = new HandlerThread("BackgroundThread");
        handlerThread.start();
        MyCallback callback = new MyCallback();
        mBackgroundHandler = new Handler(handlerThread.getLooper(), callback);

        Geocoder geocoder = new Geocoder(ViewAddressActivity.this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(startLati, startLongi, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
            add = add + "\n" + obj.getCountryName();
            add = add + "\n" + obj.getCountryCode();
            add = add + "\n" + obj.getAdminArea();
            add = add + "\n" + obj.getPostalCode();
            add = add + "\n" + obj.getSubAdminArea();
            add = add + "\n" + obj.getLocality();
            add = add + "\n" + obj.getSubThoroughfare();
            etAddressSelectsource.setText(obj.getAddressLine(0));
            etAddressSelectsourceNew.setText(obj.getAddressLine(0));
            Sourceaddress = obj.getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        remove_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove_home.setVisibility(View.GONE);
                address_home.setVisibility(View.GONE);
                address_home_label.setText("Add Home");
                RemoveAddressOperation(homeId, userDTO.getUser_id(), String.valueOf(HOME));

            }
        });
        remove_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove_work.setVisibility(View.GONE);
                address_work.setVisibility(View.GONE);
                address_work_label.setText("Add Work");
                RemoveAddressOperation(workId, userDTO.getUser_id(), String.valueOf(WORK));
            }
        });
        lay_address_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (homeId.trim().isEmpty()) {
                    SelectBottomAddressDialogFragment addNewAddressFragment = SelectBottomAddressDialogFragment.newInstance();
                    addNewAddressFragment.show(getSupportFragmentManager(), "add_photo_dialog_fragment");
                    addNewAddressFragment.setHeader("Select your Home address");
                    addNewAddressFragment.setDialogListener(dialogListener1);
                } else {
                    if (selectBox == 0) {
                        etAddressSelectsource.setText(homeAddress);
                        etAddressSelectsourceNew.setText(homeAddress);
                        Sourceaddress = homeAddress;
                        startLati = Double.parseDouble(homelatitude);
                        startLongi = Double.parseDouble(homelangitude);
                        mOrigin = new LatLng(startLati, startLongi);
                        mDestination = new LatLng(goalLati, goalLongi);
                    } else {
                        etAddressSelectdesti.setText(homeAddress);
                        etAddressSelectdestiNew.setText(homeAddress);
                        Destiaddress = homeAddress;
                        goalLati = Double.parseDouble(homelatitude);
                        goalLongi = Double.parseDouble(homelangitude);
                        etAddressSelectdesti.setText(Destiaddress);
                        etAddressSelectdestiNew.setText(Destiaddress);
                        mOrigin = new LatLng(startLati, startLongi);
                        mDestination = new LatLng(goalLati, goalLongi);
                    }
                    if (startLongi != 0.0 && startLongi != 0.0 && goalLati != 0.0 && goalLongi != 0.0) {
                        drawRoute();
                    }
                }

            }
        });
        lay_address_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (workId.trim().isEmpty()) {
                    SelectBottomAddressDialogFragment addNewAddressFragment = SelectBottomAddressDialogFragment.newInstance();
                    addNewAddressFragment.show(getSupportFragmentManager(), "add_photo_dialog_fragment");
                    addNewAddressFragment.setHeader("Select your Work address");
                    addNewAddressFragment.setDialogListener(dialogListener2);

                } else {
                    if (selectBox == 0) {
                        etAddressSelectsource.setText(workAddress);
                        etAddressSelectsourceNew.setText(workAddress);
                        Sourceaddress = workAddress;
                        startLati = Double.parseDouble(worklatitude);
                        startLongi = Double.parseDouble(worklangitude);
                        mOrigin = new LatLng(startLati, startLongi);
                        mDestination = new LatLng(goalLati, goalLongi);
                    } else {
                        etAddressSelectdesti.setText(workAddress);
                        etAddressSelectdestiNew.setText(workAddress);
                        Destiaddress = workAddress;
                        goalLati = Double.parseDouble(worklatitude);
                        goalLongi = Double.parseDouble(worklangitude);
                        mOrigin = new LatLng(startLati, startLongi);
                        mDestination = new LatLng(goalLati, goalLongi);
                    }
                    if (startLongi != 0.0 && startLongi != 0.0 && goalLati != 0.0 && goalLongi != 0.0) {
                        drawRoute();
                    }
                }

            }
        });
        img_drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layout_address_main.getVisibility() == View.VISIBLE) {
                    layout_address_main.setVisibility(View.GONE);
                    img_drop.animate().rotation(180).start();
                } else {
                    layout_address_main.setVisibility(View.VISIBLE);
                    img_drop.animate().rotation(0).start();
                }
            }
        });


        getAddressHistory();


    }


    public void AddressOperation(String user_id, String address, String lat, String lang, String address_type) {
        if (address.trim().isEmpty()) {
            return;
        }
        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.AddressOperation(user_id, address, lat, lang, address_type);
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        JSONObject object = new JSONObject(s);
                        String status = object.getString("status");
                        if (status.equalsIgnoreCase("1")) {
                            JSONObject jsonObject = object.getJSONObject("data");
                            String id = jsonObject.getString("id");
                            if (address_type.equalsIgnoreCase("1")) {
                                remove_home.setVisibility(View.VISIBLE);
                                homeId = id;
                                address_home_label.setText("Home");
                            } else {
                                remove_work.setVisibility(View.VISIBLE);
                                workId = id;
                                address_work_label.setText("Work");
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.e("RES", response.message());

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(false);


        if (!prefrence.getValue(Consts.LATITUDE).equalsIgnoreCase("")) {
            Log.e("tracker", "1");
            LatLng soruce = new LatLng(Double.parseDouble(prefrence.getValue(Consts.LATITUDE)), Double.parseDouble(prefrence.getValue(Consts.LONGITUDE)));
            if (soruce != null) {
                Log.e("tracker", "2");

                CameraPosition cameraPosition = new CameraPosition.Builder().target(soruce).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        }

        if (NetworkManager.isConnectToInternet(ViewAddressActivity.this)) {
            getthirdcategory();

        } else {
        }


    }

    public void getthirdcategory() {
        progressDialog.show();

        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.getthirdcategory2(catid, sub_category_idd, String.valueOf(startLati), String.valueOf(startLongi), userDTO.getUser_id());
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();

                Log.e("RES", response.message());
                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        Log.e("SUBLEVAL_RESPONSE", "" + s);

                        JSONObject object = new JSONObject(s);


                        try {
                            thirdCateModelArrayList = new ArrayList<>();
                            Type getpetDTO = new TypeToken<List<ThirdCateModel>>() {
                            }.getType();
                            thirdCateModelArrayList = (ArrayList<ThirdCateModel>) new Gson().fromJson(object.getJSONArray("data").toString(), getpetDTO);

                            Collections.sort(thirdCateModelArrayList, new Comparator<ThirdCateModel>() {
                                @Override
                                public int compare(ThirdCateModel lhs, ThirdCateModel rhs) {
                                    return lhs.getCat_name().compareTo(rhs.getCat_name());
                                }
                            });

                            thridcategory = thirdCateModelArrayList.get(0).getId();
                            thirdCateModelArrayList.get(0).setSelected(true);

                            Log.d(TAG, "onResponse: kamai  " + thirdCateModelArrayList.toString());
                            thirdCateAdapter.notifyDataChanged(thirdCateModelArrayList);
                            carSelectionAdapter.notifyDataChanged(thirdCateModelArrayList, destitime, mainkm);
                            getArtist(startLati, startLongi);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(ViewAddressActivity.this, "Try again. Server is not responding.",
                                LENGTH_LONG).show();


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

    class MyCallback implements Handler.Callback {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case START_COUNTER:
                    if (NetworkManager.isConnectToInternet(ViewAddressActivity.this)) {
                        getArtist(startLati, startLongi);

                    } else {
                    }
                    break;

                case FINISH_COUNTER:

                    break;

            }
            return true;
        }
    }

    private OnSelectedItemListener onItemListenerthird = new OnSelectedItemListener() {
        @Override
        public void setOnClick(String selectionString, int position) {
            thridcategory = thirdCateModelArrayList.get(position).getId();
            getArtist(startLati, startLongi);

        }
    };

    private OnSelectedItemListener onItemListener = new OnSelectedItemListener() {
        @Override
        public void setOnClick(String selectionString, int position) {
            String kmInDec = dailyModelArrayList.get(position).getDisttance();
            String time = dailyModelArrayList.get(position).getTime();
            txtkm.setText(String.valueOf(kmInDec));
            txtduration.setText(String.valueOf(time));
            destitime = time;
            mainkm = kmInDec;
            carSelectionAdapter.notifyDataChanged(thirdCateModelArrayList, destitime, mainkm);

        }
    };

    public void voidgetPricefromAdapter(String finalValue) {
        item_count_digit.setText(finalValue);
    }


    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                ViewAddressActivity.Sourceaddress = place.getName()+place.getAddress();
                ViewAddressActivity.startLati = place.getLatLng().latitude;
                ViewAddressActivity.startLongi = place.getLatLng().longitude;
                etAddressSelectsource.setText(Sourceaddress);
                etAddressSelectsourceNew.setText(Sourceaddress);
                if (goalLati == 0.0) {

                    mOrigin = new LatLng(startLati, startLongi);
                    mDestination = new LatLng(goalLati, goalLongi);
                    drawRoute();
                } else {
                    mOrigin = new LatLng(startLati, startLongi);
                    mDestination = new LatLng(goalLati, goalLongi);

                    drawRoute();
                }
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
            return;
        }

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE_TWO) {
            if (resultCode == RESULT_OK) {


                Place place = Autocomplete.getPlaceFromIntent(data);
                if (sub_category_idd.equalsIgnoreCase("434")) {

                } else {

                }
                ViewAddressActivity.Destiaddress = place.getName()+place.getAddress();
                ViewAddressActivity.goalLati = place.getLatLng().latitude;
                ViewAddressActivity.goalLongi = place.getLatLng().longitude;
                etAddressSelectdesti.setText(Destiaddress);
                etAddressSelectdestiNew.setText(Destiaddress);

                mOrigin = new LatLng(startLati, startLongi);
                mDestination = new LatLng(goalLati, goalLongi);


                if (mMarkerPoints.size() > 1) {
                    mMarkerPoints.clear();
                    mMap.clear();
                }

                // Adding new item to the ArrayList
                mMarkerPoints.add(mOrigin);
                mMarkerPoints.add(mDestination);

                // Creating MarkerOptions
                MarkerOptions options = new MarkerOptions();

                // Setting the position of the marker
                options.position(mOrigin);
                options.position(mDestination);

                /**
                 * For the start location, the color of marker is GREEN and
                 * for the end location, the color of marker is RED.
                 */
                if (mMarkerPoints.size() == 1) {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                } else if (mMarkerPoints.size() == 2) {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                }

                // Add new marker to the Google Map Android API V2
                mMap.addMarker(options);

                // Checks, whether start and end locations are captured
                if (mMarkerPoints.size() >= 2) {
                    mOrigin = mMarkerPoints.get(0);
                    mDestination = mMarkerPoints.get(1);
                }
                drawRoute();

                bottomlayout.setVisibility(View.GONE);
                pulsator.setVisibility(View.GONE);
                action_bar_menus.setVisibility(View.GONE);
                action_bar_menusNew.setVisibility(View.VISIBLE);
                bottomlayoutNew.setVisibility(View.VISIBLE);

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
            }
            return;
        }
    }

    public void getArtist(double latitue, double longitute) {
        parms.put(Consts.CATEGORY_ID, catid);
        parms.put(Consts.LATITUDE, String.valueOf(latitue));
        parms.put(Consts.LONGITUDE, String.valueOf(longitute));
        parms.put(Consts.THIRACATEGORY, thridcategory);
        new HttpsRequest(Consts.GET_ALL_ARTISTS_API, parms, ViewAddressActivity.this).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {


                mMap.clear();
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(Double.parseDouble(prefrence.getValue(Consts.LATITUDE)), Double.parseDouble(prefrence.getValue(Consts.LONGITUDE))))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.current_pin)));

                if (flag) {
                    try {

                        allAtristListDTOList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<AllAtristListDTO>>() {
                        }.getType();
                        allAtristListDTOList = new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);

                        for (int i = 0; i < allAtristListDTOList.size(); i++) {


                            latilive = Double.parseDouble(allAtristListDTOList.get(i).getLive_lat());
                            longilive = Double.parseDouble(allAtristListDTOList.get(i).getLive_long());
                            iconthirdacat = allAtristListDTOList.get(i).getIconthirdcat();

                            if (iconthirdacat.equalsIgnoreCase("4") || thridcategory.equalsIgnoreCase("7") || thridcategory.equalsIgnoreCase("11")) {
                                Log.e("id1", "" + "1");


                                mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(latilive, longilive))
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_mini)));
                            } else if (iconthirdacat.equalsIgnoreCase("17") || thridcategory.equalsIgnoreCase("9")) {
                                Log.e("id1", "" + "2");

                                mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(latilive, longilive))
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_suv)));
                            } else if (iconthirdacat.equalsIgnoreCase("5") || thridcategory.equalsIgnoreCase("8") || thridcategory.equalsIgnoreCase("12")) {

                                Log.e("id1", "" + "3");

                                mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(latilive, longilive))
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_sedan)));

                            } else if (iconthirdacat.equalsIgnoreCase("78") || catid.equalsIgnoreCase("74")) {

                                Log.e("id1", "" + "4");

                                mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(latilive, longilive))
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_rikshaw)));

                            } else if (catid.equalsIgnoreCase("91") || catid.equalsIgnoreCase("122")) {
                                Log.e("id1", "" + "5");

                                mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(latilive, longilive))
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_bike)));

                            } else {
                                Log.e("id1", "" + "6 else");

                                mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(latilive, longilive))
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin)));
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();

                    }


                } else {


                }
            }
        });

    }

    @Override
    public void onBackPressed() {

        if (bottomlayout.getVisibility() == View.VISIBLE) {
            startLati = 0;
            startLongi = 0;
            goalLati = 0;
            goalLongi = 0;
            Sourceaddress = "";
            goalLati = 0;
            goalLongi = 0;
            Destiaddress = "";
            catid = "";
            mainkm = "";
            sub_category_idd = "";
            thridcategory = "";
            destitime = "";
            subcatname = "";
            mBackgroundHandler.obtainMessage(FINISH_COUNTER, true).sendToTarget();
            super.onBackPressed();
        } else {
            mMap.clear();
            getArtist(startLati, startLongi);
            bottomlayout.setVisibility(View.VISIBLE);
            pulsator.setVisibility(View.GONE);
            etAddressSelectdesti.setText("");
            action_bar_menus.setVisibility(View.VISIBLE);
            action_bar_menusNew.setVisibility(View.GONE);
            bottomlayoutNew.setVisibility(View.GONE);
        }


    }


    @Override
    public void onResume() {

        pulsator.setVisibility(View.GONE);


        super.onResume();

    }

    @Override
    public void onPause() {

        super.onPause();

    }

    @Override
    public void onLowMemory() {

        super.onLowMemory();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public void getService(double startLati, double startLongi) {
        if (allAtristListDTOList.size() != 0) {

            Retrofit retrofit = apiClient.getClient();
            apiRest api = retrofit.create(apiRest.class);
            String user_id = "no";

            Log.d("getService: fuck ", "getService: fuck  " + allAtristListDTOList.size());
            Log.d("getService: fuck ", "getService: fuck third cat " + thridcategory.trim());

            for (int i = 0; i < allAtristListDTOList.size(); i++) {
                Log.d("getService: fuck ", "getService: fuck  " + allAtristListDTOList.get(i).getIconthirdcat().trim());
                if (allAtristListDTOList.get(i).getIconthirdcat().trim().equals(thridcategory.trim())) {
                    user_id = allAtristListDTOList.get(i).getUser_id();
                    break;
                }
            }
            if (user_id.equals("no")) {
                Toast.makeText(this, "Not Available", Toast.LENGTH_SHORT).show();
            } else {
                Call<ResponseBody> callone = api.getArtistbyid_third(sub_category_idd, user_id, userDTO.getUser_id(), thridcategory);
                callone.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            Log.e("RESSHIVAM ", response.message());
                            if (response.isSuccessful()) {
                                ResponseBody responseBody = response.body();

                                String s = responseBody.string();
                                System.out.println("RESSHIVAMASDF " + s.toString());

                                JSONObject object = new JSONObject(s);
                                try {
                                    artistDetailsDTO = new Gson().fromJson(object.getJSONObject("data").toString(), ArtistDetailsDTO.class);
                                    showData();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                            }
                        } catch (JSONException e) {
                            Log.e("RESSHIVAM ", "aaii error");
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
        } else {
            ProjectUtils.showToast(ViewAddressActivity.this, "No Service Provider Found at your location");
        }
    }


    public void showData() {
        array = new JsonArray();
        productDTOList = new ArrayList<>();
        productDTOList = artistDetailsDTO.getProducts();
        if (productDTOList.size() > 0) {

            double km = Double.parseDouble(mainkm);
            DecimalFormat newFormat = new DecimalFormat("##.##");


            for (int i = 0; i < productDTOList.size(); i++) {
                array.add(productDTOList.get(i).getPrice());
                double mainprice = Double.parseDouble(productDTOList.get(i).getPrice()) * km;
                double total = Double.parseDouble(newFormat.format(mainprice));
                productDTOList.get(i).setPrice(String.valueOf(total));

                productDTOList.get(i).setQuantityvalue(String.valueOf(km));

                if (productDTOList.get(i).getRoundtrip().equalsIgnoreCase("1")) {
                    double mainpricee = Double.parseDouble(productDTOList.get(i).getPrice()) * 2;
                    double totall = Double.parseDouble(newFormat.format(mainpricee));
                    productDTOList.get(i).setPrice(String.valueOf(totall));
                    productDTOList.get(i).setQuantityvalue(String.valueOf(Double.parseDouble(productDTOList.get(i).getQuantityvalue()) * 2));
                }
            }
        }


        productDTOListtemp = new ArrayList<>();
        productDTOListtemp.add(productDTOList.get(0));
        Bundle b = new Bundle();
        b.putSerializable(Consts.CARTDATA, (Serializable) productDTOListtemp);
        Intent in = new Intent(ViewAddressActivity.this, Booking.class);
        in.putExtra(Consts.ARTIST_DTO, artistDetailsDTO);
        in.putExtra(Consts.ARTIST_ID, allAtristListDTOList.get(0).getUser_id());
        in.putExtra(Consts.SCREEN_TAG, 2);
        in.putExtra("bool_flag", true);
        in.putExtra(Consts.PRICE, productDTOList.get(0).getPrice());
        in.putExtra(Consts.CHANGE_PRICE, array.toString());
        in.putExtra(ROUTE_PATH, prefrence.getValue(ROUTE_PATH));
        in.putExtras(b);
        startActivity(in);


    }

    private void drawRoute() {

        Log.e("ViewAddress_draw", "" + "view address drawroute called");
        String url = getDirectionsUrl(mOrigin, mDestination);

        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(url);
    }


    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Key
        String key = "key=" + getString(R.string.directionkey);

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception on download", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    /**
     * A class to download data from Google Directions URL
     */
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("DownloadTask", "DownloadTask : " + data);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                prefrence.setValue(ROUTE_PATH, jsonObject.toString());
                JSONArray routes = jsonObject.getJSONArray("routes");

                JSONObject routes1 = routes.getJSONObject(0);

                JSONArray legs = routes1.getJSONArray("legs");

                JSONObject legs1 = legs.getJSONObject(0);

                JSONObject distance = legs1.getJSONObject("distance");

                JSONObject duration = legs1.getJSONObject("duration");

                long stringdistance = distance.getLong("value");
                String stringduraion = duration.getString("text");


                laykm.setVisibility(View.VISIBLE);//old opening
                laykm.setVisibility(View.GONE);//new hiding
                long kmValue = stringdistance / 1000;
                DecimalFormat newFormat = new DecimalFormat("#.##");
                if (stringdistance < 1000) {
                    txtkm.setText(kmValue + " Meter");
                } else {

                    txtkm.setText(newFormat.format(kmValue) + " Km");
                }
                txtduration.setText(stringduraion);

                mainkm = newFormat.format(kmValue);
                destitime = txtduration.getText().toString();
                carSelectionAdapter.notifyDataChanged(thirdCateModelArrayList, destitime, mainkm);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /**
     * A class to parse the Google Directions in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);

                com.kamaii.customer.DirectionsJSONParser parser = new com.kamaii.customer.DirectionsJSONParser();

                routes = parser.parse(jObject);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

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
                lineOptions.color(getResources().getColor(R.color.pink_text_color));
            }

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                if (mPolyline != null) {
                    mPolyline.remove();
                }
                mPolyline = mMap.addPolyline(lineOptions);

            } else
                Toast.makeText(getApplicationContext(), "No route is found", Toast.LENGTH_LONG).show();
        }
    }

    public void getAddressHistory() {
        prefrence = SharedPrefrence.getInstance(ViewAddressActivity.this);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);

        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.getUserAddressHistory(userDTO.getUser_id());
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                Log.e("RES", response.message());
                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        Log.e("RES", ""+s);

                        JSONObject object = new JSONObject(s);
                        String status = object.getString("status");
                        if (status.equalsIgnoreCase("1")) {
                            layout_recent.setVisibility(View.VISIBLE);
                            try {
                                recentAddressArrayList = new ArrayList<>();
                                Type getpetDTO = new TypeToken<List<RecentAddress>>() {
                                }.getType();

                                recentAddressArrayList = new Gson().fromJson(object.getJSONArray("data").toString(), getpetDTO);
                                ArrayList<RecentAddress> recentAddressArrayList1 = getRecentAddress(recentAddressArrayList);
                                Log.e("recentAddressArrayList","Activity"+recentAddressArrayList1.get(0).getAddress());
                                Log.e("recentAddressArrayList","Activity1"+recentAddressArrayList1.get(1).getAddress());

                                if (recentAddressArrayList1.size() > 0) {
                                    recentAddressAdapter.update(recentAddressArrayList1);
                                } else {
                                    layout_recent.setVisibility(View.GONE);
                                }
                                findPlaces();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            recentAddressArrayList = new ArrayList<>();
                            layout_recent.setVisibility(View.GONE);
                            findPlaces();
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

    private ArrayList<RecentAddress> getRecentAddress(ArrayList<RecentAddress> recentAddressArrayList) {
        ArrayList<RecentAddress> recentAddressArrayList1 = new ArrayList<>();
        for (RecentAddress recentAddress : recentAddressArrayList) {
            if (recentAddress.getAddress_type().equalsIgnoreCase("3")) {
                recentAddressArrayList1.add(recentAddress);
            }

        }
        return recentAddressArrayList1;
    }

    private OnBottomSelectedItemListener onSelectedItemListener = new OnBottomSelectedItemListener() {
        @Override
        public void setOnClick(int flag, ArrayList<RecentAddress> recentAddresses, int position) {

            if (isFirstTime) {
                isFirstTime = false;
                Destiaddress = recentAddresses.get(position).getAddress();
                etAddressSelectdesti.setText(Destiaddress);
                etAddressSelectdestiNew.setText(Destiaddress);

                bottomlayout.setVisibility(View.GONE);
                pulsator.setVisibility(View.GONE);
                action_bar_menus.setVisibility(View.GONE);
                action_bar_menusNew.setVisibility(View.VISIBLE);
                bottomlayoutNew.setVisibility(View.VISIBLE);


                goalLati = Double.parseDouble(recentAddresses.get(position).getLat());
                goalLongi = Double.parseDouble(recentAddresses.get(position).getLang());
                mOrigin = new LatLng(startLati, startLongi);
                mDestination = new LatLng(goalLati, goalLongi);
                if (startLati != 0.0 && startLongi != 0.0 && goalLati != 0.0 && goalLongi != 0.0) {
                    drawRoute();
                }
            } else if (selectBox == 0) {
                Destiaddress = recentAddresses.get(position).getAddress();

                etAddressSelectdesti.setText(Destiaddress);
                etAddressSelectdestiNew.setText(Destiaddress);


                bottomlayout.setVisibility(View.GONE);
                pulsator.setVisibility(View.GONE);
                action_bar_menus.setVisibility(View.GONE);
                action_bar_menusNew.setVisibility(View.VISIBLE);
                bottomlayoutNew.setVisibility(View.VISIBLE);

                mOrigin = new LatLng(startLati, startLongi);
                mDestination = new LatLng(goalLati, goalLongi);
                if (startLati != 0.0 && startLongi != 0.0 && goalLati != 0.0 && goalLongi != 0.0) {
                    drawRoute();
                }
                drawRoute();
            } else if (selectBox == 1) {
                Destiaddress = recentAddresses.get(position).getAddress();
                etAddressSelectdesti.setText(Destiaddress);
                etAddressSelectdestiNew.setText(Destiaddress);

                bottomlayout.setVisibility(View.GONE);
                pulsator.setVisibility(View.GONE);
                action_bar_menus.setVisibility(View.GONE);
                action_bar_menusNew.setVisibility(View.VISIBLE);
                bottomlayoutNew.setVisibility(View.VISIBLE);

                goalLati = Double.parseDouble(recentAddresses.get(position).getLat());
                goalLongi = Double.parseDouble(recentAddresses.get(position).getLang());
                mOrigin = new LatLng(startLati, startLongi);
                mDestination = new LatLng(goalLati, goalLongi);
                if (startLati != 0.0 && startLongi != 0.0 && goalLati != 0.0 && goalLongi != 0.0) {
                    drawRoute();
                }
            }
        }
    };


    public void RemoveAddressOperation(String id, String user_id, String address_type) {

        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.RemoveAddressOperation(id, user_id, address_type);
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (address_type.equalsIgnoreCase("1")) {
                    homeId = "";
                    address_home.setText("");
                } else {
                    workId = "";
                    address_work.setText("");
                }
                Log.e("RES", response.message());

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void findPlaces() {
        if (recentAddressArrayList.size() > 0) {
            for (RecentAddress recentAddress : recentAddressArrayList) {
                if (recentAddress.getAddress_type().equalsIgnoreCase("1")) {
                    homeId = recentAddress.getId();
                    homelatitude = recentAddress.getLat();
                    homelangitude = recentAddress.getLang();
                    homeAddress = recentAddress.getAddress();
                } else if (recentAddress.getAddress_type().equalsIgnoreCase("2")) {
                    workId = recentAddress.getId();
                    worklatitude = recentAddress.getLat();
                    worklangitude = recentAddress.getLang();
                    workAddress = recentAddress.getAddress();
                }
            }
        }
        if (homeId.isEmpty()) {
            address_home_label.setText("Add Home");
            address_home.setVisibility(View.GONE);
            remove_home.setVisibility(View.GONE);
        } else {
            address_home_label.setText("Home");
            address_home.setText(homeAddress);
            address_home.setVisibility(View.VISIBLE);
            remove_home.setVisibility(View.VISIBLE);
        }

        if (workId.isEmpty()) {
            address_work_label.setText("Add Work");
            address_work.setVisibility(View.GONE);
            remove_work.setVisibility(View.GONE);

        } else {
            address_work_label.setText("Work");
            address_work.setText(workAddress);
            address_work.setVisibility(View.VISIBLE);
            remove_work.setVisibility(View.VISIBLE);
        }
    }

    private SelectBottomAddressDialogFragment.DialogListener dialogListener1 = new SelectBottomAddressDialogFragment.DialogListener() {
        @Override
        public void onClickView(int flag) {

            if (flag == 1) {
                if (etAddressSelectsource.getText().toString().trim().isEmpty()) {
                    return;
                }
                homeAddress = etAddressSelectsource.getText().toString();
                address_home.setText(homeAddress);
                address_home.setVisibility(View.VISIBLE);
                AddressOperation(userDTO.getUser_id(), homeAddress, String.valueOf(startLati), String.valueOf(startLongi), String.valueOf(HOME));
            } else {
                if (etAddressSelectdesti.getText().toString().trim().isEmpty()) {
                    return;
                }
                homeAddress = etAddressSelectdesti.getText().toString().trim();
                address_home.setText(homeAddress);
                address_home.setVisibility(View.VISIBLE);
                AddressOperation(userDTO.getUser_id(), homeAddress, String.valueOf(goalLati), String.valueOf(goalLongi), String.valueOf(HOME));
            }
            if (selectBox == 0) {
                etAddressSelectsource.setText(homeAddress);
                etAddressSelectsourceNew.setText(homeAddress);
                Sourceaddress = homeAddress;
                mOrigin = new LatLng(startLati, startLongi);
                mDestination = new LatLng(goalLati, goalLongi);

            } else {
                etAddressSelectdesti.setText(homeAddress);
                etAddressSelectdestiNew.setText(homeAddress);
                Destiaddress = homeAddress;
                mOrigin = new LatLng(startLati, startLongi);
                mDestination = new LatLng(goalLati, goalLongi);
            }
            if (startLongi != 0.0 && startLongi != 0.0 && goalLati != 0.0 && goalLongi != 0.0) {
                drawRoute();
            }
        }
    };
    private SelectBottomAddressDialogFragment.DialogListener dialogListener2 = new SelectBottomAddressDialogFragment.DialogListener() {
        @Override
        public void onClickView(int flag) {

            if (flag == 1) {
                if (etAddressSelectsource.getText().toString().trim().isEmpty()) {
                    return;
                }
                workAddress = etAddressSelectsource.getText().toString();
                address_work.setText(workAddress);
                address_work.setVisibility(View.VISIBLE);
                AddressOperation(userDTO.getUser_id(), workAddress, String.valueOf(startLati), String.valueOf(startLongi), String.valueOf(WORK));
            } else {
                if (etAddressSelectdesti.getText().toString().trim().isEmpty()) {
                    return;
                }
                workAddress = etAddressSelectdesti.getText().toString().trim();
                address_work.setText(workAddress);
                address_work.setVisibility(View.VISIBLE);
                AddressOperation(userDTO.getUser_id(), workAddress, String.valueOf(goalLati), String.valueOf(goalLongi), String.valueOf(WORK));
            }
            if (selectBox == 0) {
                etAddressSelectsource.setText(workAddress);
                etAddressSelectsourceNew.setText(workAddress);
                Sourceaddress = workAddress;
                mOrigin = new LatLng(startLati, startLongi);
                mDestination = new LatLng(goalLati, goalLongi);
            } else {
                etAddressSelectdesti.setText(workAddress);
                etAddressSelectdestiNew.setText(workAddress);
                Destiaddress = workAddress;
                mOrigin = new LatLng(startLati, startLongi);
                mDestination = new LatLng(goalLati, goalLongi);
            }
            if (startLongi != 0.0 && startLongi != 0.0 && goalLati != 0.0 && goalLongi != 0.0) {
                drawRoute();
            }
        }
    };

}

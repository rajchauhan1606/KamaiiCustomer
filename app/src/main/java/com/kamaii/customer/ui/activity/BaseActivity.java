package com.kamaii.customer.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.customer.DTO.HistoryDTO;
import com.kamaii.customer.DTO.UserBooking;
import com.kamaii.customer.DTO.UserDTO;
import com.kamaii.customer.R;
import com.kamaii.customer.api.apiClient;
import com.kamaii.customer.api.apiRest;
import com.kamaii.customer.https.HttpsRequest;
import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.interfacess.Helper;
import com.kamaii.customer.preferences.SharedPrefrence;
import com.kamaii.customer.preferences.TextSizeFix;
import com.kamaii.customer.ui.adapter.CurruntBookingListAdapter;
import com.kamaii.customer.ui.fragment.AddReferFragment;
import com.kamaii.customer.ui.fragment.AppUpdateFragment;
import com.kamaii.customer.ui.fragment.AppointmentFrag;
import com.kamaii.customer.ui.fragment.CategoryFragment;
import com.kamaii.customer.ui.fragment.ChatList;
import com.kamaii.customer.ui.fragment.ContactUs;
import com.kamaii.customer.ui.fragment.GetDiscountActivity;
import com.kamaii.customer.ui.fragment.Jobs;
import com.kamaii.customer.ui.fragment.MyBooking;
import com.kamaii.customer.ui.fragment.MyEarning;
import com.kamaii.customer.ui.fragment.NotificationActivity;
import com.kamaii.customer.ui.fragment.PaidFrag;
import com.kamaii.customer.ui.fragment.ProfileSettingActivity;
import com.kamaii.customer.ui.fragment.ReferCustomerFragment;
import com.kamaii.customer.ui.fragment.Tickets;
import com.kamaii.customer.ui.fragment.Wallet;
import com.kamaii.customer.ui.models.AddressModel;
import com.kamaii.customer.utils.CustomTextView;
import com.kamaii.customer.utils.CustomTextViewBold;
import com.kamaii.customer.utils.CustomTypeFaceSpan;
import com.kamaii.customer.utils.FontCache;
import com.kamaii.customer.utils.GlobalUtils;
import com.kamaii.customer.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.widget.Toast.LENGTH_LONG;
import static com.kamaii.customer.interfacess.Consts.BOOKING;
import static com.kamaii.customer.interfacess.Consts.TRACK_ARTIST_ID;
import static com.kamaii.customer.interfacess.Consts.TRACK_BOOKING_BOOKING_FLAG;
import static com.kamaii.customer.interfacess.Consts.TRACK_BOOKING_DATE;
import static com.kamaii.customer.interfacess.Consts.TRACK_BOOKING_DESTINATION_ADDRESS;
import static com.kamaii.customer.interfacess.Consts.TRACK_BOOKING_ID;
import static com.kamaii.customer.interfacess.Consts.TRACK_BOOKING_MOBILE_NUMBER;
import static com.kamaii.customer.interfacess.Consts.TRACK_BOOKING_PAYMENT_TYPE;
import static com.kamaii.customer.interfacess.Consts.TRACK_BOOKING_PRODUCT_NAME;
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

public class BaseActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private String TAG = BaseActivity.class.getSimpleName();
    HashMap<String, String> parms = new HashMap<>();
    private FrameLayout frame;
    private View contentView;
    public NavigationView navigationView;
    public RelativeLayout header;
    public DrawerLayout drawer;
    public View navHeader;
    public ImageView menuLeftIV, ivFilter, ivnotiication, ivaboutus;
    public RelativeLayout ivmainsearchLayout, ivmaincartLayout;
    Context mContext;
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    public RecyclerView base_recyclerview;
    List<UserBooking> CurruntRunningBookingList;
    List<UserBooking> CurruntRunningBookingListdupli;
    public static final String TAG_MAIN = "main";
    public static final String TAG_CHAT = "chat";
    public static final String TAG_BOOKING = "booking";
    public static final String TAG_NOTIFICATION = "notification";
    public static final String TAG_DISCOUNT = "discount";
    public static final String TAG_HISTORY = "history";
    public static final String TAG_PROFILE_SETINGS = "profile_settings";
    public static final String TAG_TICKETS = "tickets";
    public static final String TAG_APPOINTMENT = "appointment";
    public static final String TAG_JOBS = "jobs";
    public static final String TAG_WALLET = "wallet";
    public static final String TAG_UPDATE = "appupdate";
    public static final String TAG_OPEN = "open";
    public static String CURRENT_TAG = TAG_MAIN;
    public static int navItemIndex = 0;
    private Handler mHandler;
    private static final float END_SCALE = 0.8f;
    InputMethodManager inputManager;

    private boolean shouldLoadHomeFragOnBackPress = true;
    public CustomTextViewBold headerNameTV;
    private Location mylocation;
    private GoogleApiClient googleApiClient;
    private final static int REQUEST_CHECK_SETTINGS_GPS = 0x1;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS = 0x2;
    private CircleImageView img_profile;
    public CustomTextViewBold tvName, cart_count;
    private CustomTextView tvEmail, tvOther, tvEnglish;
    private LinearLayout llProfileClick;
    String type = "";
    public static ArrayList<AddressModel> addressModelArrayList = new ArrayList<>();
    boolean first_time_login = false;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextSizeFix.adjustFontScale(getResources().getConfiguration(), 1.0f, BaseActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        findViewById(R.id.ivmainsearchLayout).setVisibility(View.GONE);
        findViewById(R.id.ivmaincartLayout).setVisibility(View.GONE);
        mContext = BaseActivity.this;
        mHandler = new Handler();
        CurruntRunningBookingList = new ArrayList<>();

        inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        prefrence = SharedPrefrence.getInstance(mContext);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);

        first_time_login = getIntent().getBooleanExtra("first_time_login", false);

        /*if (first_time_login) {
            getMyFirstWalletReward();
        }*/
        getreward();
        setUpGClient();
        frame = (FrameLayout) findViewById(R.id.frame);
        cart_count = findViewById(R.id.cart_count);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        contentView = findViewById(R.id.content);
        headerNameTV = findViewById(R.id.headerNameTV);
        menuLeftIV = (ImageView) findViewById(R.id.menuLeftIV);
        ivFilter = (ImageView) findViewById(R.id.ivFilter);
        ivaboutus = (ImageView) findViewById(R.id.ivaboutus);
        ivmainsearchLayout = (RelativeLayout) findViewById(R.id.ivmainsearchLayout);
        ivmaincartLayout = (RelativeLayout) findViewById(R.id.ivmaincartLayout);
        ivnotiication = (ImageView) findViewById(R.id.ivnotiication);
        base_recyclerview = findViewById(R.id.base_recyclerview);


        navHeader = navigationView.getHeaderView(0);
        img_profile = navHeader.findViewById(R.id.img_profile);
        tvName = navHeader.findViewById(R.id.tvName);
        tvEmail = navHeader.findViewById(R.id.tvEmail);

        tvEnglish = navHeader.findViewById(R.id.tvEnglish);
        tvOther = navHeader.findViewById(R.id.tvOther);
        tvOther = navHeader.findViewById(R.id.tvOther);
        llProfileClick = navHeader.findViewById(R.id.llProfileClick);


        llProfileClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ivFilter.setVisibility(View.GONE);
                navItemIndex = 9;
                CURRENT_TAG = TAG_PROFILE_SETINGS;
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, new ProfileSettingActivity());
                fragmentTransaction.commitAllowingStateLoss();
                drawer.closeDrawers();
            }
        });

        tvEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                language("en");

            }
        });
        tvOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                language("ar");

            }
        });
        Glide.with(mContext).
                load(userDTO.getImage())
                .placeholder(R.drawable.dummyuser_image)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(img_profile);
        tvEmail.setText(userDTO.getEmail_id());
       // tvName.setText(getResources().getString(R.string.app_name));
        tvName.setText(userDTO.getName());

        navItemIndex = 0;
        CURRENT_TAG = TAG_MAIN;
        if (savedInstanceState == null) {
            if (type != null) {
                if (type.equals(Consts.RIDER_HAS_PICKUP_ORDER)) {

                    Intent intent = new Intent(getApplicationContext(), TrackingActivity.class);
                    intent.putExtra("flag123", true);
                    startActivity(intent);
                } else {
                    loadHomeFragment(new CategoryFragment(), CURRENT_TAG);
                }
            }
        }
        menuLeftIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerOpen();
            }
        });

        setUpNavigationView();
        Menu menu = navigationView.getMenu();

        changeColorItem(menu, R.id.nav_home_features);
        changeColorItem(menu, R.id.nav_bookings_and_job);
        changeColorItem(menu, R.id.nav_personal);
        changeColorItem(menu, R.id.other);
        changeColorItem(menu, R.id.nav_refer);
        changeColorItemmade(menu, R.id.nav_madeindia);

        for (int i = 0; i < menu.size(); i++) {
            MenuItem mi = menu.getItem(i);
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyCustomFont(subMenuItem);
                }
            }
            applyCustomFont(mi);
        }


        drawer.setScrimColor(Color.TRANSPARENT);
        drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                                     @Override
                                     public void onDrawerSlide(View drawerView, float slideOffset) {

                                         // Scale the View based on current slide offset
                                         final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                                         final float offsetScale = 1 - diffScaledOffset;
                                         contentView.setScaleX(offsetScale);
                                         contentView.setScaleY(offsetScale);

                                         // Translate the View, accounting for the scaled width
                                         final float xOffset = drawerView.getWidth() * slideOffset;
                                         final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                                         final float xTranslation = xOffset - xOffsetDiff;
                                         contentView.setTranslationX(xTranslation);
                                     }

                                     @Override
                                     public void onDrawerClosed(View drawerView) {
                                     }
                                 }
        );

        ivFilter.setVisibility(View.GONE);
    }

    private void getCurruntBooking() {
        prefrence = SharedPrefrence.getInstance(BaseActivity.this);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        //progressDialog.show();

        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.getbookingHome(userDTO.getUser_id());
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //   progressDialog.dismiss();
                //   swipeRefreshLayout.setRefreshing(false);
                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();

                        Log.e("tracking_btn_response96", "" + s);
                        JSONObject object = new JSONObject(s);

                        //       tvNo.setVisibility(View.GONE);
                        //       rvBooking.setVisibility(View.VISIBLE);
                        String message = object.getString("message");
                        int sstatus = object.getInt("status");
                        Log.e("ssstatus", "" + sstatus);
                        if (sstatus == 1) {

                            try {
                                //  CurruntRunningBookingListdupli  = new ArrayList<>();
                                Type getpetDTO = new TypeToken<List<UserBooking>>() {
                                }.getType();

                                CurruntRunningBookingList = (ArrayList<UserBooking>) new Gson().fromJson(object.getJSONArray("data").toString(), getpetDTO);
                                /*for (int i = 0; i < CurruntRunningBookingList.size(); i++) {
                                    if (CurruntRunningBookingList.get(i).getBooking_flag().equals("1") || CurruntRunningBookingList.get(i).getBooking_flag().equals("3")) {
                                        CurruntRunningBookingListdupli.add(CurruntRunningBookingList.get(i));
                                    }
                                }*/

                                CurruntBookingListAdapter adapter = new CurruntBookingListAdapter(BaseActivity.this, CurruntRunningBookingList);
                                base_recyclerview.setLayoutManager(new LinearLayoutManager(BaseActivity.this));
                                base_recyclerview.setAdapter(adapter);
                                //  showData();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else if (sstatus == 3) {
                            // swipeRefreshLayout.setRefreshing(false);
                            // tvNo.setVisibility(View.VISIBLE);
                            //  rvBooking.setVisibility(View.GONE);
                        } else {
                            if (CurruntRunningBookingList.size() != 0) {

                                CurruntRunningBookingList.clear();
                            }
                            base_recyclerview.setVisibility(View.GONE);
                            //  swipeRefreshLayout.setRefreshing(false);
                            // tvNo.setVisibility(View.VISIBLE);
                            //  rvBooking.setVisibility(View.GONE);
                        }
                    } else {
                        // Toast.makeText(getActivity(), "Try again. Server is not responding.", LENGTH_LONG).show();

                        // progressDialog.dismiss();
                        // swipeRefreshLayout.setRefreshing(false);
                        // tvNo.setVisibility(View.VISIBLE);
                        // rvBooking.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                //  progressDialog.dismiss();
                //   swipeRefreshLayout.setRefreshing(false);
                //   tvNo.setVisibility(View.VISIBLE);
                //   rvBooking.setVisibility(View.GONE);
            }
        });

    }


    private void getMyFirstWalletReward() {

        new HttpsRequest("getFirstWalletReward", this).stringGet(this.getClass().getSimpleName(), new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {

                    Log.e("IMAGE_RES", "" + response.toString());
                    try {
                        String status = response.getString("status");
                        String image = response.getString("image");

                        if (status.equals("1")) {

                            Dialog dialog = new Dialog(BaseActivity.this);
                            dialog.setContentView(R.layout.first_wallet_reward_dialog_layout);

                            ImageView imageView;
                            imageView = dialog.findViewById(R.id.first_reward_imageview);

                            Glide.with(BaseActivity.this).load(image).into(imageView);

                            imageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {

                }
            }
        });
    }

    public HashMap<String, String> getparm() {
        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.USER_ID, userDTO.getUser_id());
        return parms;
    }

    private void getreward() {
        new HttpsRequest("getFirstWalletReward", getparm(), mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {

                    try {
                        String status = response.getString("status");
                        String image = response.getString("image");

                        if (status.equals("1")) {

                            Dialog dialog = new Dialog(BaseActivity.this);
                            dialog.setContentView(R.layout.first_wallet_reward_dialog_layout);

                            ImageView imageView;
                            imageView = dialog.findViewById(R.id.first_reward_imageview);

                            Glide.with(BaseActivity.this).load(image).into(imageView);
                            updatereward();
                            imageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }
        });
    }

    private void updatereward() {
        new HttpsRequest("updatereward", getparm(), mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                }
            }
        });
    }


    public void changeColorItemmade(Menu menu, int id) {
        MenuItem tools = menu.findItem(id);
        SpannableString s = new SpannableString(tools.getTitle());
        s.setSpan(new TextAppearanceSpan(this, R.style.TextAppearance55), 0, s.length(), 0);
        tools.setTitle(s);

    }

    public void changeColorItem(Menu menu, int id) {
        MenuItem tools = menu.findItem(id);
        SpannableString s = new SpannableString(tools.getTitle());
        s.setSpan(new TextAppearanceSpan(this, R.style.TextAppearance44), 0, s.length(), 0);
        tools.setTitle(s);

    }

    public void applyCustomFont(MenuItem mi) {
        Typeface customFont = FontCache.getTypeface("Poppins-Regular.otf", BaseActivity.this);
        SpannableString spannableString = new SpannableString(mi.getTitle());
        spannableString.setSpan(new CustomTypeFaceSpan("", customFont), 0, spannableString.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(spannableString);
    }

    public void showImage() {
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        Glide.with(mContext).
                load(userDTO.getImage())
                .placeholder(R.drawable.dummyuser_image)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(img_profile);
    }

    public void loadHomeFragment(final Fragment fragment, final String TAG) {

        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }


        drawer.closeDrawers();

        invalidateOptionsMenu();
    }


    public void drawerOpen() {

        try {
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {

        }
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
    }


    private void setUpNavigationView() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {


            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                switch (menuItem.getItemId()) {

                    case R.id.nav_home:
                        ivFilter.setVisibility(View.GONE);
                        ivmainsearchLayout.setVisibility(View.VISIBLE);
                        ivmaincartLayout.setVisibility(View.VISIBLE);
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_MAIN;
                        fragmentTransaction.replace(R.id.frame, new CategoryFragment());
                        break;
                    case R.id.nav_chat:
                        ivaboutus.setVisibility(View.GONE);
                        ivnotiication.setVisibility(View.GONE);
                        ivFilter.setVisibility(View.GONE);
                        ivmainsearchLayout.setVisibility(View.GONE);
                        ivmaincartLayout.setVisibility(View.GONE);
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_CHAT;
                        fragmentTransaction.replace(R.id.frame, new ChatList());
                        break;
                    case R.id.nav_booking:
                        ivaboutus.setVisibility(View.GONE);
                        ivnotiication.setVisibility(View.GONE);
                        ivFilter.setVisibility(View.GONE);
                        ivmainsearchLayout.setVisibility(View.GONE);
                        ivmaincartLayout.setVisibility(View.GONE);
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_BOOKING;
                        fragmentTransaction.replace(R.id.frame, new MyBooking());
                        break;
                    case R.id.nav_jobs:
                        ivaboutus.setVisibility(View.GONE);
                        ivnotiication.setVisibility(View.GONE);
                        ivFilter.setVisibility(View.GONE);
                        ivmainsearchLayout.setVisibility(View.GONE);
                        ivmaincartLayout.setVisibility(View.GONE);
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_JOBS;
                        fragmentTransaction.replace(R.id.frame, new Jobs());
                        break;
                    case R.id.nav_appointment:
                        ivaboutus.setVisibility(View.GONE);
                        ivnotiication.setVisibility(View.GONE);
                        ivFilter.setVisibility(View.GONE);
                        ivmainsearchLayout.setVisibility(View.GONE);
                        ivmaincartLayout.setVisibility(View.GONE);
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_APPOINTMENT;
                        fragmentTransaction.replace(R.id.frame, new AppointmentFrag());
                        break;
                    case R.id.nav_notification:
                        ivaboutus.setVisibility(View.GONE);
                        ivnotiication.setVisibility(View.GONE);
                        ivFilter.setVisibility(View.GONE);
                        ivmainsearchLayout.setVisibility(View.GONE);
                        ivmaincartLayout.setVisibility(View.GONE);
                        navItemIndex = 5;
                        CURRENT_TAG = TAG_NOTIFICATION;
                        fragmentTransaction.replace(R.id.frame, new NotificationActivity());
                        break;
                    case R.id.nav_history:
                        ivaboutus.setVisibility(View.GONE);
                        ivnotiication.setVisibility(View.GONE);
                        ivFilter.setVisibility(View.GONE);
                        ivmainsearchLayout.setVisibility(View.GONE);
                        ivmaincartLayout.setVisibility(View.GONE);
                        navItemIndex = 6;
                        CURRENT_TAG = TAG_DISCOUNT;
                        fragmentTransaction.replace(R.id.frame, new PaidFrag());
                        break;
                    case R.id.nav_discount:
                        ivaboutus.setVisibility(View.GONE);
                        ivnotiication.setVisibility(View.GONE);
                        ivFilter.setVisibility(View.GONE);
                        ivmainsearchLayout.setVisibility(View.GONE);
                        ivmaincartLayout.setVisibility(View.GONE);
                        navItemIndex = 7;
                        CURRENT_TAG = TAG_HISTORY;
                        fragmentTransaction.replace(R.id.frame, new GetDiscountActivity());
                        break;

                    case R.id.nav_contact:
                        ivaboutus.setVisibility(View.GONE);
                        ivnotiication.setVisibility(View.GONE);
                        ivFilter.setVisibility(View.GONE);
                        ivmainsearchLayout.setVisibility(View.GONE);
                        ivmaincartLayout.setVisibility(View.GONE);
                        navItemIndex = 7;
                        CURRENT_TAG = TAG_HISTORY;
                        fragmentTransaction.replace(R.id.frame, new ContactUs());
                        break;
                    case R.id.nav_wallet:
                        ivaboutus.setVisibility(View.GONE);
                        ivnotiication.setVisibility(View.GONE);
                        ivFilter.setVisibility(View.GONE);
                        ivmainsearchLayout.setVisibility(View.GONE);
                        ivmaincartLayout.setVisibility(View.GONE);
                        navItemIndex = 8;
                        CURRENT_TAG = TAG_WALLET;
                        fragmentTransaction.replace(R.id.frame, new Wallet());
                        break;
                    case R.id.nav_profilesetting:
                        ivaboutus.setVisibility(View.GONE);
                        ivmainsearchLayout.setVisibility(View.GONE);
                        ivmaincartLayout.setVisibility(View.GONE);
                        ivnotiication.setVisibility(View.GONE);
                        navItemIndex = 9;
                        CURRENT_TAG = TAG_PROFILE_SETINGS;
                        ivFilter.setVisibility(View.GONE);
                        fragmentTransaction.replace(R.id.frame, new ProfileSettingActivity());
                        break;
                    case R.id.nav_tickets:
                        ivaboutus.setVisibility(View.GONE);
                        ivnotiication.setVisibility(View.GONE);
                        ivmainsearchLayout.setVisibility(View.GONE);
                        ivmaincartLayout.setVisibility(View.GONE);
                        navItemIndex = 10;
                        CURRENT_TAG = TAG_TICKETS;
                        ivFilter.setVisibility(View.GONE);
                        fragmentTransaction.replace(R.id.frame, new Tickets());
                        break;

                    case R.id.nav_update:
                        ivaboutus.setVisibility(View.GONE);
                        ivnotiication.setVisibility(View.GONE);
                        ivmainsearchLayout.setVisibility(View.GONE);
                        ivmaincartLayout.setVisibility(View.GONE);
                        navItemIndex = 11;
                        CURRENT_TAG = TAG_UPDATE;
                        ivFilter.setVisibility(View.GONE);
                        fragmentTransaction.replace(R.id.frame, new AppUpdateFragment());
                        break;

                    case R.id.nav_addrefer:
                        ivaboutus.setVisibility(View.GONE);
                        ivnotiication.setVisibility(View.GONE);
                        ivmainsearchLayout.setVisibility(View.GONE);
                        ivmaincartLayout.setVisibility(View.GONE);
                        ivFilter.setVisibility(View.GONE);
                        navItemIndex = 12;
                        CURRENT_TAG = TAG_NOTIFICATION;
                        fragmentTransaction.replace(R.id.frame, new AddReferFragment());
                        break;
                    case R.id.nav_viewerefer:
                        ivaboutus.setVisibility(View.GONE);
                        ivnotiication.setVisibility(View.GONE);
                        ivmainsearchLayout.setVisibility(View.GONE);
                        ivmaincartLayout.setVisibility(View.GONE);
                        ivFilter.setVisibility(View.GONE);
                        navItemIndex = 13;
                        CURRENT_TAG = TAG_NOTIFICATION;
                        fragmentTransaction.replace(R.id.frame, new ReferCustomerFragment());
                        break;

                    case R.id.nav_earning:
                        ivaboutus.setVisibility(View.GONE);
                        ivnotiication.setVisibility(View.GONE);
                        ivmainsearchLayout.setVisibility(View.GONE);
                        ivmaincartLayout.setVisibility(View.GONE);
                        ivFilter.setVisibility(View.GONE);
                        navItemIndex = 14;
                        CURRENT_TAG = TAG_NOTIFICATION;
                        fragmentTransaction.replace(R.id.frame, new MyEarning());
                        break;

                    case R.id.signout:

                        confirmLogout();
                        break;
                    case R.id.nav_madeindia:

                        break;
                    default:
                        ivFilter.setVisibility(View.GONE);
                        ivmainsearchLayout.setVisibility(View.GONE);
                        ivmaincartLayout.setVisibility(View.GONE);
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_MAIN;
                        fragmentTransaction.replace(R.id.frame, new CategoryFragment());
                        break;

                }
                fragmentTransaction.commitAllowingStateLoss();
                drawer.closeDrawers();

                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);
                return true;
            }
        });

    }

    public void confirmLogout() {
        try {
            new AlertDialog.Builder(this)
                    .setIcon(R.mipmap.ic_launcher)
                    .setTitle(getResources().getString(R.string.app_name))
                    .setMessage(getResources().getString(R.string.logout_msg))
                    .setCancelable(false)
                    .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            prefrence.clearAllPreferences();
                            Intent intent = new Intent(BaseActivity.this, CheckSignInActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
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

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }
        if (shouldLoadHomeFragOnBackPress) {

            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_MAIN;
                loadHomeFragment(new CategoryFragment(), CURRENT_TAG);
                return;
            }
        }
        clickDone();
    }


    public void clickDone() {
        new AlertDialog.Builder(this)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle(getResources().getString(R.string.app_name))
                .setMessage(getResources().getString(R.string.closeMsg))
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent i = new Intent();
                        i.setAction(Intent.ACTION_MAIN);
                        i.addCategory(Intent.CATEGORY_HOME);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }


    private void getMyLocation() {
        if (googleApiClient != null) {
            if (googleApiClient.isConnected()) {
                int permissionLocation = ContextCompat.checkSelfPermission(BaseActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                    mylocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                    LocationRequest locationRequest = new LocationRequest();
                    locationRequest.setInterval(3000);
                    locationRequest.setFastestInterval(3000);
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                            .addLocationRequest(locationRequest);
                    builder.setAlwaysShow(true);
                    LocationServices.FusedLocationApi
                            .requestLocationUpdates(googleApiClient, locationRequest, this);
                    PendingResult result =
                            LocationServices.SettingsApi
                                    .checkLocationSettings(googleApiClient, builder.build());
                    result.setResultCallback(new ResultCallback() {

                        @Override
                        public void onResult(@NonNull Result result) {
                            final Status status = result.getStatus();
                            switch (status.getStatusCode()) {
                                case LocationSettingsStatusCodes.SUCCESS:
                                    int permissionLocation = ContextCompat
                                            .checkSelfPermission(BaseActivity.this,
                                                    Manifest.permission.ACCESS_FINE_LOCATION);
                                    if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                                        mylocation = LocationServices.FusedLocationApi
                                                .getLastLocation(googleApiClient);

                                    }
                                    break;
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    try {
                                        status.startResolutionForResult(BaseActivity.this,
                                                REQUEST_CHECK_SETTINGS_GPS);
                                    } catch (IntentSender.SendIntentException e) {
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    break;
                            }
                        }
                    });
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCurruntBooking();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS_GPS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        getMyLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                }
                break;
        }
        if (requestCode == 23) {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame);
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        checkPermissions();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mylocation = location;
        if (mylocation != null) {
            Double latitude = mylocation.getLatitude();
            Double longitude = mylocation.getLongitude();
            prefrence.setValue(Consts.LATITUDE, latitude + "");
            prefrence.setValue(Consts.LONGITUDE, longitude + "");


            parms.put(Consts.USER_ID, userDTO.getUser_id());
            parms.put(Consts.ROLE, "2");
            parms.put(Consts.LATITUDE, latitude + "");
            parms.put(Consts.LONGITUDE, longitude + "");
            Geocoder geocoder = new Geocoder(BaseActivity.this, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                Address obj = addresses.get(0);
                String add = obj.getAddressLine(0);
                add = add + "\n" + obj.getCountryName();
                add = add + "\n" + obj.getCountryCode();
                add = add + "\n" + obj.getAdminArea();
                add = add + "\n" + obj.getPostalCode();
                add = add + "\n" + obj.getSubAdminArea();
                add = add + "\n" + obj.getLocality();
                add = add + "\n" + obj.getSubThoroughfare();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            updateLocation();
        }
    }


    private void checkPermissions() {
        int permissionLocation = ContextCompat.checkSelfPermission(BaseActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(BaseActivity.this,
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            }
        } else {
            getMyLocation();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        int permissionLocation = ContextCompat.checkSelfPermission(BaseActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
            getMyLocation();
        }
    }

    private synchronized void setUpGClient() {
        googleApiClient = new GoogleApiClient.Builder(BaseActivity.this)
                .enableAutoManage(BaseActivity.this, 0, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }


    public void updateLocation() {
        new HttpsRequest(Consts.UPDATE_LOCATION_API, parms, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {

                } else {
                    ProjectUtils.showToast(mContext, msg);
                }
            }
        });
    }

    public void language(String language) {
        String languageToLoad = language; // your language

        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.locale = locale;

        BaseActivity.this.getResources().updateConfiguration(config,
                BaseActivity.this.getResources().getDisplayMetrics());

        Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(i);


    }


}

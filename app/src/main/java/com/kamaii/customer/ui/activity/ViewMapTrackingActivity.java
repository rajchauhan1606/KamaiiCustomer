package com.kamaii.customer.ui.activity;


import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.mapbox.turf.TurfConversion;
import com.mapbox.turf.TurfMeasurement;
import com.kamaii.customer.DTO.UserBooking;
import com.kamaii.customer.DTO.UserDTO;
import com.kamaii.customer.R;
import com.kamaii.customer.api.apiClient;
import com.kamaii.customer.api.apiRest;
import com.kamaii.customer.https.HttpsRequest;
import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.interfacess.Helper;
import com.kamaii.customer.preferences.SharedPrefrence;
import com.kamaii.customer.service.MyService;
import com.kamaii.customer.utils.AnimationUtils;
import com.kamaii.customer.utils.CustomEditText;
import com.kamaii.customer.utils.CustomTextView;
import com.kamaii.customer.utils.CustomTextViewBold;
import com.kamaii.customer.utils.MapUtils;
import com.kamaii.customer.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import pl.bclogic.pulsator4droid.library.PulsatorLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ViewMapTrackingActivity extends AppCompatActivity {
    String saddress = "", vendor_name = "", artist_id = "", daddress = "", totalamount = "", productname = "", vendorimage = "", mobileno = "", booking = "", bflag = "", bid = "", pay_type = "", bdate = "", btime = "", vechileno = "";
    private Context mContext;
    private SharedPrefrence prefrence;
    String vendor_longitude, vendor_latitude;
    private UserDTO userDTO;
    private Handler mBackgroundHandler;
    private static final int START_COUNTER = 1001;
    private static final int FINISH_COUNTER = 3002;
    private static final int PROGRESS_COUNTER = 3005;
    private String TAG = ViewMapTrackingActivity.class.getSimpleName();
    private ArrayList<UserBooking> userBookingList;
    CustomTextView txtvendorname, etAddressSelectsource, etAddressSelectdesti;
    ImageView imgvendonrimage, imgback, ivStatus;
    CustomTextViewBold txttotalamount, txtservicename, tvStatus;
    LinearLayout laycall;
    private MapView mapView;
    private DirectionsRoute currentRoute;
    PulsatorLayout pulsator;
    RelativeLayout relanimation;
    TextView txtptype, txtptypemsg;
    LinearLayout laycancel, laycallcancel, layshare, btnshare, laykmdistance;
    private HashMap<String, String> paramsDecline;
    private DialogInterface dialog_book;
    ProgressDialog progressDialog;
    RelativeLayout relcancel;
    String startbooking = "0";
    private Dialog dialog;
    ImageView imgone, imgtwo;
    TextView txtkm, txtdistancetime, txtvechile;


    private GoogleMap googleMap;
    LatLng defaultLocation;

    private Marker originMarker;
    private Marker destinationMarker;

    private Polyline grayPolyline;
    private Polyline blackPolyline;

    LatLng previousLatLng;
    LatLng currentLatLng;
    private Marker movingCabMarker;
    private Handler handler=new Handler();
    private Runnable runnable;
    int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        mContext = ViewMapTrackingActivity.this;
        setContentView(R.layout.activity_view_map_traccking);

        prefrence = SharedPrefrence.getInstance(ViewMapTrackingActivity.this);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);

        progressDialog = new ProgressDialog(ViewMapTrackingActivity.this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);

        txtservicename = findViewById(R.id.txtservicename);
        imgone = findViewById(R.id.imgone);
        imgtwo = findViewById(R.id.imgtwo);
        txtvendorname = findViewById(R.id.txtvendorname);
        imgvendonrimage = findViewById(R.id.imgvendonrimage);
        etAddressSelectsource = findViewById(R.id.etAddressSelectsource);
        etAddressSelectdesti = findViewById(R.id.etAddressSelectdesti);
        txttotalamount = findViewById(R.id.totalamount);
        imgback = findViewById(R.id.imgback);
        laycall = findViewById(R.id.laycall);
        tvStatus = findViewById(R.id.tvStatus);
        ivStatus = findViewById(R.id.ivStatus);
        pulsator = findViewById(R.id.pulsator);
        relanimation = findViewById(R.id.relanimation);
        txtptype = findViewById(R.id.txtptype);
        txtptypemsg = findViewById(R.id.txtptypemsg);
        laycancel = findViewById(R.id.laycancel);
        laycallcancel = findViewById(R.id.laycallcancel);
        layshare = findViewById(R.id.layshare);
        relcancel = findViewById(R.id.relcancel);
        btnshare = findViewById(R.id.btnshare);
        txtkm = findViewById(R.id.txtkm);
        txtdistancetime = findViewById(R.id.txtdistancetime);
        laykmdistance = findViewById(R.id.laykmdistance);
        txtvechile = findViewById(R.id.txtvechile);
        pulsator.start();
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        vendor_latitude = getIntent().getStringExtra("vendor_latitude");
        vendor_longitude = getIntent().getStringExtra("vendor_longitude");
        saddress = getIntent().getStringExtra("saddress");
        daddress = getIntent().getStringExtra("daddress");
        vendor_name = getIntent().getStringExtra("vendor_name");
        artist_id = getIntent().getStringExtra("artist_id");
        totalamount = getIntent().getStringExtra("totalamount");
        productname = getIntent().getStringExtra("productname");
        vendorimage = getIntent().getStringExtra("vendorimage");
        mobileno = getIntent().getStringExtra("mobileno");
        booking = getIntent().getStringExtra("booking");
        bflag = getIntent().getStringExtra("bflag");
        bid = getIntent().getStringExtra("bid");
        pay_type = getIntent().getStringExtra("pay_type");
        bdate = getIntent().getStringExtra("bdate");
        btime = getIntent().getStringExtra("btime");
        vechileno = getIntent().getStringExtra("vno");

        double dclatititutee = Double.parseDouble(vendor_latitude);
        double dclongitiuteeee = Double.parseDouble(vendor_longitude);

        double longitiute = Double.parseDouble(prefrence.getValue(Consts.LONGITUDE));
        double latitute = Double.parseDouble(prefrence.getValue(Consts.LATITUDE));

        Point destinationPoint = Point.fromLngLat(dclongitiuteeee, dclatititutee);
        Point originPoint = Point.fromLngLat(longitiute, latitute);


        NavigationRoute.builder(ViewMapTrackingActivity.this)
                .accessToken(Mapbox.getAccessToken())
                .origin(originPoint)
                .destination(destinationPoint)
                .build()

                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {

                        Log.d(TAG, "Response code: " + response.code());
                        if (response.body() == null) {
                            Log.e(TAG, "No routes found, make sure you set the right user and access token.");
                            return;
                        } else if (response.body().routes().size() < 1) {
                            Log.e(TAG, "No routes found");
                            return;
                        }

                        currentRoute = response.body().routes().get(0);

                        double distanceBetweenLastAndSecondToLastClickPoint = 0;
                        distanceBetweenLastAndSecondToLastClickPoint = TurfMeasurement.distance(originPoint, destinationPoint);


                        DecimalFormat newFormat = new DecimalFormat("##.##");
                        String kmInDec = newFormat.format(distanceBetweenLastAndSecondToLastClickPoint);

                        String finalConvertedFormattedDistance = new DecimalFormat("####")
                                .format(TurfConversion.convertLength(
                                        currentRoute.distance(),
                                        "meters", "kilometers"));


                        txtkm.setText(finalConvertedFormattedDistance + " " + "KM");
                        txtdistancetime.setText(TimeUnit.SECONDS.toMinutes(currentRoute.duration().longValue()) + " " + "Min");
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                        Log.e(TAG, "Error: " + throwable.getMessage());
                    }
                });
        if (bflag.equalsIgnoreCase("0"))
        {

            relanimation.setVisibility(View.VISIBLE);
            tvStatus.setText(mContext.getResources().getString(R.string.waiting_artist));
            laycallcancel.setVisibility(View.VISIBLE);
            layshare.setVisibility(View.GONE);
            laycall.setVisibility(View.GONE);

            if (booking.equalsIgnoreCase("booking")) {
                Intent intent = new Intent(ViewMapTrackingActivity.this, MyService.class);
                startService(intent);
            }
        } else if (bflag.equalsIgnoreCase("1")) {

            laycallcancel.setVisibility(View.VISIBLE);
            laycall.setVisibility(View.VISIBLE);
            relanimation.setVisibility(View.GONE);
            layshare.setVisibility(View.GONE);
            tvStatus.setText(mContext.getResources().getString(R.string.request_acc));
        } else if (bflag.equalsIgnoreCase("3")) {
            startbooking = "1";
            relanimation.setVisibility(View.GONE);
            laycallcancel.setVisibility(View.GONE);
            laycall.setVisibility(View.GONE);
            tvStatus.setText("Your booking in progress");
            layshare.setVisibility(View.VISIBLE);
        }

        if (pay_type.equalsIgnoreCase("0")) {
            txtptype.setText("Online Payment");
            txtptypemsg.setText("Relax when the ride ends");
        } else if (pay_type.equalsIgnoreCase("1")) {
            txtptype.setText("Cash");
            txtptypemsg.setText("Pay when the ride ends");
        } else if (pay_type.equalsIgnoreCase("2")) {
            txtptype.setText("Wallet Payment");
            txtptypemsg.setText("Relax when the ride ends");
        } else {
            txtptype.setText("");
            txtptypemsg.setText("");
        }

        txtservicename.setText(productname);
        txtvendorname.setText(vendor_name);
        txtvechile.setText(vechileno);

        Glide.with(mContext).
                load(vendorimage)
                .placeholder(R.drawable.dummyuser_image)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgvendonrimage);

        etAddressSelectsource.setText(saddress);

        txttotalamount.setText("Total Fare" + " " + totalamount + " " + "Rs");

        if (!daddress.equalsIgnoreCase("")) {
            etAddressSelectdesti.setText(daddress);

        } else {
            etAddressSelectdesti.setText(saddress);
        }

        btnshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogshow();
            }
        });
        laycall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!mobileno.equalsIgnoreCase("")) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + mobileno));
                    mContext.startActivity(intent);
                } else {
                    Toast.makeText(ViewMapTrackingActivity.this, "Contact Number Is Not Available", Toast.LENGTH_LONG).show();
                }

            }
        });


        laycancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completeDialog(mContext.getResources().getString(R.string.cancel), mContext.getResources().getString(R.string.booking_cancel_msg) + " " + vendor_name + "?");
            }
        });

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        mapView.getMapAsync(new com.google.android.gms.maps.OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location buttont
                // googleMap.setMyLocationEnabled(true);

                // For dropping a marker at a point on the Map
                if (ActivityCompat.checkSelfPermission(ViewMapTrackingActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ViewMapTrackingActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissifons
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                googleMap.setMyLocationEnabled(true);
                defaultLocation = new LatLng(22.291542, 70.801882);
                showDefaultLocationOnMap(defaultLocation);
              //  handler = new Handler();
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        showPath(MapUtils.getListOfLocations());
                        showMovingCab(MapUtils.getListOfLocations());
                    }
                },3000);
            }
        });
        HandlerThread handlerThread = new HandlerThread("BackgroundThread");
        handlerThread.start();
        MyCallback callback = new MyCallback();
        mBackgroundHandler = new Handler(handlerThread.getLooper(), callback);
        mBackgroundHandler.obtainMessage(START_COUNTER, true).sendToTarget();

    }

    public void dialogshow()
    {
        dialog = new Dialog(ViewMapTrackingActivity.this/*, android.R.style.Theme_Dialog*/);
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


                if (etName.getText().toString().trim().isEmpty()) {
                    Toast.makeText(ViewMapTrackingActivity.this, "Please Enter Name", Toast.LENGTH_LONG).show();
                } else if (etmobile.getText().toString().trim().isEmpty()) {
                    Toast.makeText(ViewMapTrackingActivity.this, "Please Enter Mobile Number", Toast.LENGTH_LONG).show();
                } else {
                    String totaltime = ProjectUtils.changeDateFormate1(bdate) + " " + btime;
                    String phoneNumberWithCountryCode = "+91" + etmobile.getText().toString();
                    String message = "Dear" + "  " + etName.getText().toString() + "" + "\n \n" + getResources().getString(R.string.shareone) + "\n\n" + getResources().getString(R.string.sharetwo) + "\n\n" +
                            getResources().getString(R.string.sharethree) + " " + vendor_name + "\n" +
                            getResources().getString(R.string.sharefour) + " " + productname + "\n" +
                            getResources().getString(R.string.sharefive) + " " + totaltime + "\n" +
                            getResources().getString(R.string.sharesix) + " " + saddress + "\n" +
                            getResources().getString(R.string.shareseven) + " " + daddress + "\n \n" +
                            getResources().getString(R.string.shareseight) + "\n\n " +
                            getResources().getString(R.string.sharenine) + "\n\n ";

                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse(
                                    String.format("https://api.whatsapp.com/send?phone=%s&text=%s",
                                            phoneNumberWithCountryCode, message))));

                    etmobile.setText("");
                    dialog.dismiss();
                }

            }
        });

    }

    public void completeDialog(String title, String msg) {
        try
        {
            new AlertDialog.Builder(mContext)
                    .setMessage(msg)
                    .setCancelable(false)
                    .setPositiveButton(mContext.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog_book = dialog;
                            decline();

                        }
                    })
                    .setNegativeButton(mContext.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
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

    public void decline() {

        paramsDecline = new HashMap<>();
        paramsDecline.put(Consts.USER_ID, userDTO.getUser_id());
        paramsDecline.put(Consts.BOOKING_ID, bid);
        paramsDecline.put(Consts.DECLINE_BY, "2");
        paramsDecline.put(Consts.DECLINE_REASON, "Busy");
        progressDialog.show();
        new HttpsRequest(Consts.DECLINE_BOOKING_API, paramsDecline, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                progressDialog.dismiss();
                dialog_book.dismiss();
                if (flag) {

                    Log.e("DECLINE_REQUEST",""+response.toString());
                    relcancel.setVisibility(View.VISIBLE);
                    laycallcancel.setVisibility(View.GONE);
                    laykmdistance.setVisibility(View.GONE);


                } else {
                }


            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

    }


    @Override
    public void onBackPressed() {
        pulsator.stop();


        if (booking.equalsIgnoreCase("booking")) {

            Intent intent = new Intent(ViewMapTrackingActivity.this, BaseActivity.class);
            startActivity(intent);
            finish();

            Intent intenttt = new Intent(ViewMapTrackingActivity.this, MyService.class);
            stopService(intenttt);

        } else {
            finish();
        }

    }

    class MyCallback implements Handler.Callback {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case START_COUNTER:
                    getArtistdata();
                    getBooking();

                    break;

                case FINISH_COUNTER:
                    Intent intent = new Intent(ViewMapTrackingActivity.this, ScrachActivity.class);
                    intent.putExtra("vendor_name", vendor_name);
                    intent.putExtra("artist_id", artist_id);
                    intent.putExtra("vendorimage", vendorimage);
                    intent.putExtra("saddress", saddress);
                    intent.putExtra("daddress", daddress);
                    intent.putExtra("totalamount", totalamount);
                    intent.putExtra("productname", productname);
                    intent.putExtra("mobileno", mobileno);
                    intent.putExtra("booking", "booking");
                    intent.putExtra("bflag", bflag);
                    intent.putExtra("bid", bid);
                    intent.putExtra("pay_type", pay_type);
                    startActivity(intent);
                    finish();

                    break;

                case PROGRESS_COUNTER:


                    Geocoder coder = new Geocoder(ViewMapTrackingActivity.this);
                    List<Address> address;
                    try {

                        address = coder.getFromLocationName(daddress, 5);
                        if (address == null) {
                            address = coder.getFromLocationName(saddress, 5);
                        }
                        Address location = address.get(0);
                        double lati = location.getLatitude();
                        double longi = location.getLongitude();

                        LatLng latLng = new LatLng(lati, longi);
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(latLng);

                        if (!daddress.equalsIgnoreCase("")) {
                            markerOptions.title(daddress);

                        } else {
                            markerOptions.title(saddress);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                googleMap.clear();
                                Marker myMarker = googleMap.addMarker(markerOptions);
                                myMarker.showInfoWindow();
                            }
                        });


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    getBooking();

                    break;

            }
            return true;
        }
    }


    public HashMap<String, String> getparm() {
        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.ARTIST_ID, artist_id);
        return parms;
    }


    public void getArtistdata() {

        new HttpsRequest(Consts.GET_LOCATION_ARTIST_API, getparm(), mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {


                if (flag) {


                    try
                    {
                        JSONObject jsonObject = response.getJSONObject("data");
                        double latitute = Double.parseDouble(jsonObject.getString("lati"));
                        double longi = Double.parseDouble(jsonObject.getString("longi"));


                        LatLng latLng = new LatLng(latitute, longi);

                        MarkerOptions markerOptions = new MarkerOptions();


                        // Setting the position for the marker
                        markerOptions.position(latLng);
                        //  markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.mini));
                        // Setting the title for the marker.
                        // This will be displayed on taping the marker
                        markerOptions.title(vendor_name);


                        // Clears the previously touched position


                        // Animating to the touched position
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                googleMap.clear();
                                Marker myMarker = googleMap.addMarker(markerOptions);
                                myMarker.showInfoWindow();
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

    public static void setAnimation(GoogleMap myMap, final List<LatLng> directionPoint, final Bitmap bitmap) {


        Marker marker = myMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                .position(directionPoint.get(0))
                .flat(true));

        myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(directionPoint.get(0), 10));

        animateMarker(myMap, marker, directionPoint, false);
    }


    private static void animateMarker(GoogleMap myMap, final Marker marker, final List<LatLng> directionPoint,
                                      final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = myMap.getProjection();
        final long duration = 30000;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            int i = 0;

            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                if (i < directionPoint.size())
                    marker.setPosition(directionPoint.get(i));
                i++;


                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }



    private double bearingBetweenLocations(LatLng latLng1, LatLng latLng2) {

        double PI = 3.14159;
        double lat1 = latLng1.latitude * PI / 180;
        double long1 = latLng1.longitude * PI / 180;
        double lat2 = latLng2.latitude * PI / 180;
        double long2 = latLng2.longitude * PI / 180;

        double dLon = (long2 - long1);

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon);

        double brng = Math.atan2(y, x);

        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;

        return brng;
    }

    public void getBooking()
    {
        prefrence = SharedPrefrence.getInstance(ViewMapTrackingActivity.this);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);


        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.getbooking(userDTO.getUser_id());
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                Log.e("RES", response.message());
                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        JSONObject object = new JSONObject(s);


                        try {
                            userBookingList = new ArrayList<>();
                            Type getpetDTO = new TypeToken<List<UserBooking>>() {
                            }.getType();

                            userBookingList = new Gson().fromJson(object.getJSONArray("data").toString(), getpetDTO);
                            showData();

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

    public void declinemethod() {
        getBookingcopy();

    }


    public void showData()
    {

        for (int i = 0; i < userBookingList.size(); i++) {

            if (bid.equalsIgnoreCase(userBookingList.get(i).getId())) {
                saddress = userBookingList.get(i).getAddress();
                daddress = userBookingList.get(i).getDestinationaddress();
                vendor_name = userBookingList.get(i).getArtistName();
                artist_id = userBookingList.get(i).getArtist_id();
                totalamount = userBookingList.get(i).getPrice();
                productname = userBookingList.get(i).getProduct().get(0).getProduct_name();
                vendorimage = userBookingList.get(i).getArtistImage();
                mobileno = userBookingList.get(i).getArtistMobile();
                bflag = userBookingList.get(i).getBooking_flag();
                bid = userBookingList.get(i).getId();
                pay_type = userBookingList.get(i).getPay_type();
                bdate = userBookingList.get(i).getBooking_date();
                btime = userBookingList.get(i).getBooking_time();
                vechileno = userBookingList.get(i).getProduct().get(0).getVehicle_number();
            } else {

            }
        }

        double dclatititutee = Double.parseDouble(vendor_latitude);
        double dclongitiuteeee = Double.parseDouble(vendor_longitude);

        double longitiute = Double.parseDouble(prefrence.getValue(Consts.LONGITUDE));
        double latitute = Double.parseDouble(prefrence.getValue(Consts.LATITUDE));

        Point destinationPoint = Point.fromLngLat(dclongitiuteeee, dclatititutee);

        Point originPoint = Point.fromLngLat(longitiute, latitute);


        NavigationRoute.builder(ViewMapTrackingActivity.this)
                .accessToken(Mapbox.getAccessToken())
                .origin(originPoint)
                .destination(destinationPoint)
                .build()

                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {

                        Log.d(TAG, "Response code: " + response.code());
                        if (response.body() == null) {
                            Log.e(TAG, "No routes found, make sure you set the right user and access token.");
                            return;
                        } else if (response.body().routes().size() < 1) {
                            Log.e(TAG, "No routes found");
                            return;
                        }

                        currentRoute = response.body().routes().get(0);


                        double distanceBetweenLastAndSecondToLastClickPoint = 0;
                        distanceBetweenLastAndSecondToLastClickPoint = TurfMeasurement.distance(originPoint, destinationPoint);


                        DecimalFormat newFormat = new DecimalFormat("##.##");
                        String kmInDec = newFormat.format(distanceBetweenLastAndSecondToLastClickPoint);


                        String finalConvertedFormattedDistance = new DecimalFormat("####")
                                .format(TurfConversion.convertLength(
                                        currentRoute.distance(),
                                        "meters", "kilometers"));
                        txtkm.setText(finalConvertedFormattedDistance + " " + "KM");
                        txtdistancetime.setText(TimeUnit.SECONDS.toMinutes(currentRoute.duration().longValue()) + " " + "Min");


                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                        Log.e(TAG, "Error: " + throwable.getMessage());
                    }
                });


        if (pay_type.equalsIgnoreCase("0")) {
            txtptype.setText("Online Payment");
            txtptypemsg.setText("Relax when the ride ends");
        } else if (pay_type.equalsIgnoreCase("1")) {
            txtptype.setText("Cash");
            txtptypemsg.setText("Pay when the ride ends");
        } else if (pay_type.equalsIgnoreCase("2")) {
            txtptype.setText("Wallet Payment");
            txtptypemsg.setText("Relax when the ride ends");
        } else {
            txtptype.setText("");
            txtptypemsg.setText("");
        }


        if (bflag.equalsIgnoreCase("0")) {
            laykmdistance.setVisibility(View.VISIBLE);
            mBackgroundHandler.obtainMessage(START_COUNTER, true).sendToTarget();
            relanimation.setVisibility(View.VISIBLE);
            tvStatus.setText(mContext.getResources().getString(R.string.waiting_artist));
            laycallcancel.setVisibility(View.VISIBLE);
            laycall.setVisibility(View.GONE);
            layshare.setVisibility(View.GONE);
        } else if (bflag.equalsIgnoreCase("1")) {
            laykmdistance.setVisibility(View.VISIBLE);
            mBackgroundHandler.obtainMessage(START_COUNTER, true).sendToTarget();
            laycallcancel.setVisibility(View.VISIBLE);
            laycall.setVisibility(View.VISIBLE);
            relanimation.setVisibility(View.GONE);
            layshare.setVisibility(View.GONE);
            tvStatus.setText(mContext.getResources().getString(R.string.request_acc));
        } else if (bflag.equalsIgnoreCase("2")) {
            relcancel.setVisibility(View.VISIBLE);
            laycallcancel.setVisibility(View.GONE);
            laycall.setVisibility(View.GONE);
            laykmdistance.setVisibility(View.GONE);
            tvStatus.setText("");
            layshare.setVisibility(View.GONE);

        } else if (bflag.equalsIgnoreCase("3")) {
            mBackgroundHandler.obtainMessage(PROGRESS_COUNTER, true).sendToTarget();
            laykmdistance.setVisibility(View.GONE);
            startbooking = "1";
            relanimation.setVisibility(View.GONE);
            laycallcancel.setVisibility(View.GONE);
            laycall.setVisibility(View.GONE);
            tvStatus.setText("Your booking in progress");
            layshare.setVisibility(View.VISIBLE);
        } else if (bflag.equalsIgnoreCase("4")) {
            mBackgroundHandler.obtainMessage(FINISH_COUNTER, true).sendToTarget();
        }
        txtservicename.setText(productname);
        txtvendorname.setText(vendor_name);
        txtvechile.setText(vechileno);

        Glide.with(mContext).
                load(vendorimage)
                .placeholder(R.drawable.dummyuser_image)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgvendonrimage);
        etAddressSelectsource.setText(saddress);


        if (!daddress.equalsIgnoreCase("")) {
            etAddressSelectdesti.setText(daddress);

        } else {
            etAddressSelectdesti.setText(saddress);
        }
        txttotalamount.setText("Total Fare" + " " + totalamount + " " + "Rs");


    }


    public void getBookingcopy()
    {
        prefrence = SharedPrefrence.getInstance(ViewMapTrackingActivity.this);
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


                        try
                        {
                            userBookingList = new ArrayList<>();
                            Type getpetDTO = new TypeToken<List<UserBooking>>() {
                            }.getType();

                            userBookingList = new Gson().fromJson(object.getJSONArray("data").toString(), getpetDTO);

                            bid = userBookingList.get(0).getId();
                            artist_id = userBookingList.get(0).getArtist_id();

                        }
                        catch (Exception e)
                        {
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
    private void moveCamera(LatLng latLng) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    private void animateCamera(LatLng latLng) {
        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(15.5f).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
    private void showDefaultLocationOnMap(LatLng latLng) {
        moveCamera(latLng);
        animateCamera(latLng);
    }
    private Marker addOriginDestinationMarkerAndGet(LatLng latLng)
    {
        BitmapDescriptor bitmapDescriptor =BitmapDescriptorFactory.fromBitmap(MapUtils.getOriginDestinationMarkerBitmap());
        return googleMap.addMarker(new MarkerOptions().position(latLng).flat(true).icon(bitmapDescriptor));
    }
    private void showPath(ArrayList<LatLng> latLngList)
    {

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng:latLngList)
        {
            builder.include(latLng);
        }
        LatLngBounds bounds = builder.build();
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 2));

        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.GRAY);
        polylineOptions.width(5f);
        polylineOptions.addAll(latLngList);
        grayPolyline = googleMap.addPolyline(polylineOptions);


        PolylineOptions blackPolylineOptions = new PolylineOptions();
        blackPolylineOptions.color(Color.BLACK);
        blackPolylineOptions.width(5f);
        blackPolyline = googleMap.addPolyline(blackPolylineOptions);

        ValueAnimator polylineAnimator = AnimationUtils.polylineAnimator();
        polylineAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int percentValue = (int) animation.getAnimatedValue();
                int index = (int) ((grayPolyline.getPoints().size()) * (percentValue / 100.0f));
                blackPolyline.setPoints(grayPolyline.getPoints().subList(0, index));
            }
        });
        polylineAnimator.start();
        originMarker = addOriginDestinationMarkerAndGet(latLngList.get(0));
        originMarker.setAnchor(0.5f, 0.5f);

        destinationMarker = addOriginDestinationMarkerAndGet(latLngList.get(latLngList.size() - 1));
        destinationMarker.setAnchor(0.5f, 0.5f);

    }
    Bitmap getCarBitmap(Context context)
    {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.pin_driver);
        return Bitmap.createScaledBitmap(bitmap, 50, 100, false);
    }
    private void updateCarLocation(LatLng latLng) {
        if (movingCabMarker == null) {
            movingCabMarker = addOriginDestinationMarkerAndGet(latLng);
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
                    if (currentLatLng != null && previousLatLng != null)
                    {
                        float multiplier = valueAnimator.getAnimatedFraction();
                        LatLng nextLocation = new LatLng(multiplier * currentLatLng.latitude + (1 - multiplier) * previousLatLng.latitude,multiplier * currentLatLng.longitude + (1 - multiplier) * previousLatLng.longitude);
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
    private void showMovingCab(ArrayList<LatLng> cabLatLngList)
    {
        handler = new Handler();
        index = 0;
        runnable=new Runnable() {
            @Override
            public void run() {
                if (index < 10) {
                    updateCarLocation(cabLatLngList.get(index));
                    handler.postDelayed(runnable, 3000);
                    ++index;
                } else {
                    handler.removeCallbacks(runnable);
                    Toast.makeText(ViewMapTrackingActivity.this, "Trip Ends", Toast.LENGTH_LONG).show();
                }
            }
        };

        handler.postDelayed(runnable, 5000);
    }
}


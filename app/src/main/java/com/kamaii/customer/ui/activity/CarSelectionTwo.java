package com.kamaii.customer.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import java.net.URISyntaxException;
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

public class CarSelectionTwo extends AppCompatActivity implements com.google.android.gms.maps.OnMapReadyCallback {
    private String TAG = CarSelectionTwo.class.getSimpleName();
    private String LOG_TAG = "Google Places Autocomplete";
    public static double startLati, startLongi, goalLati, goalLongi;
    TextView txtkm, txtduration;
    private SharedPrefrence prefrence;
    public static String sub_category_idd = "", subcatname = "", catid = "", mainkm = "", thridcategory = "", destitime = "";
    LinearLayout laykm;
    HashMap<String, String> parms = new HashMap<>();
    private ArrayList<AllAtristListDTO> allAtristListDTOList = new ArrayList<>();
    private UserDTO userDTO;
    double latilive = 0;
    double longilive = 0;
    public static String Sourceaddress = "", Destiaddress = "";
    int selectBox = 0;

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

    @BindView(R.id.rv_third)
    RecyclerView rv_third;

    boolean isFirstTime = true;
    String homeId = "", workId = "", homelatitude = "", homelangitude = "", worklatitude = "", worklangitude = "";
    String homeAddress = "", workAddress = "";
    int HOME = 1, WORK = 2;
  //  private static final String API_KEY = "AIzaSyCIpO2XuPQr8HDOg92EU0pRkP0G9rKvFoY";
    private static final String API_KEY = "";
    ProgressDialog progressDialog;
    CarSelectionAdapter carSelectionAdapter;
    Fragment frame;
    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_selection_two);
        ButterKnife.bind(CarSelectionTwo.this);

        Intent intentdata = getIntent();

        data = intentdata.getStringExtra("data");


        Places.initialize(getApplicationContext(), API_KEY);
        PlacesClient placesClient = Places.createClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        progressDialog = new ProgressDialog(CarSelectionTwo.this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        mMarkerPoints = new ArrayList<>();
        txtkm = findViewById(R.id.txtkm);
        laykm = findViewById(R.id.laykm);
        txtduration = findViewById(R.id.txtduration);


        if (sub_category_idd.equalsIgnoreCase("242")) {
            destitime = "10";
            mainkm = "10";


            laykm.setVisibility(View.VISIBLE);
            txtkm.setText(mainkm);
            txtduration.setText(destitime);
        }

        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        parms.put(Consts.USER_ID, userDTO.getUser_id());

        parms.put(Consts.SUB_CATEGORY_ID, sub_category_idd);

        try {
            startLati = Double.parseDouble(prefrence.getValue(Consts.LATITUDE));
            startLongi = Double.parseDouble(prefrence.getValue(Consts.LONGITUDE));

        } catch (Exception e) {

        }

        HandlerThread handlerThread = new HandlerThread("BackgroundThread");
        handlerThread.start();
        MyCallback callback = new MyCallback();
        mBackgroundHandler = new Handler(handlerThread.getLooper(), callback);

        Geocoder geocoder = new Geocoder(CarSelectionTwo.this, Locale.getDefault());
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
            Sourceaddress = obj.getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                                homeId = id;
                            } else {
                                workId = id;
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
        googleMap.setMyLocationEnabled(true);


        if (!prefrence.getValue(Consts.LATITUDE).equalsIgnoreCase("")) {
            LatLng soruce = new LatLng(Double.parseDouble(prefrence.getValue(Consts.LATITUDE)), Double.parseDouble(prefrence.getValue(Consts.LONGITUDE)));

            if (soruce != null) {
                CameraPosition cameraPosition = new CameraPosition.Builder().target(soruce).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        }

        if (NetworkManager.isConnectToInternet(CarSelectionTwo.this)) {
            getthirdcategory();
        } else {
            ProjectUtils.showToast(CarSelectionTwo.this, getResources().getString(R.string.internet_concation));
        }


    }

    public void getthirdcategory() {
        progressDialog.show();

        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.getthirdcategory(catid, sub_category_idd);
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

                            getArtist(startLati, startLongi);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(CarSelectionTwo.this, "Try again. Server is not responding.",
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
                    if (NetworkManager.isConnectToInternet(CarSelectionTwo.this)) {
                        getArtist(startLati, startLongi);

                    } else {
                        ProjectUtils.showToast(CarSelectionTwo.this, getResources().getString(R.string.internet_concation));
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

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                CarSelectionTwo.Sourceaddress = place.getAddress();
                CarSelectionTwo.startLati = place.getLatLng().latitude;
                CarSelectionTwo.startLongi = place.getLatLng().longitude;
                etAddressSelectsource.setText(Sourceaddress);
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
                CarSelectionTwo.Destiaddress = place.getAddress();
                CarSelectionTwo.goalLati = place.getLatLng().latitude;
                CarSelectionTwo.goalLongi = place.getLatLng().longitude;
                etAddressSelectdesti.setText(Destiaddress);

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
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
            return;
        }
    }

    public void getArtist(double latitue, double longitute) {
        parms.put(Consts.CATEGORY_ID, catid);
        parms.put(Consts.LATITUDE, String.valueOf(latitue));
        parms.put(Consts.LONGITUDE, String.valueOf(longitute));
        parms.put(Consts.THIRACATEGORY, thridcategory);
        new HttpsRequest(Consts.GET_ALL_ARTISTS_API, parms, CarSelectionTwo.this).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {


                mMap.clear();
                if (flag) {
                    try {

                        allAtristListDTOList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<AllAtristListDTO>>() {
                        }.getType();
                        allAtristListDTOList = new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);

                        for (int i = 0; i < allAtristListDTOList.size(); i++) {


                            latilive = Double.parseDouble(allAtristListDTOList.get(i).getLive_lat());
                            longilive = Double.parseDouble(allAtristListDTOList.get(i).getLive_long());

                            if (thridcategory.equalsIgnoreCase("4") || thridcategory.equalsIgnoreCase("7") || thridcategory.equalsIgnoreCase("11")) {
                                Log.e("id1", "" + "1");
                                mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(latilive, longilive))
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_mini)));
                            } else if (thridcategory.equalsIgnoreCase("17") || thridcategory.equalsIgnoreCase("9")) {
                                Log.e("id1", "" + "2");

                                mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(latilive, longilive))
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_suv)));
                            } else if (thridcategory.equalsIgnoreCase("5") || thridcategory.equalsIgnoreCase("8") || thridcategory.equalsIgnoreCase("12")) {

                                Log.e("id1", "" + "3");

                                mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(latilive, longilive))
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_sedan)));

                            } else if (thridcategory.equalsIgnoreCase("78") || catid.equalsIgnoreCase("74")) {

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
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.other)));
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

        startLati = 0;
        startLongi = 0;
        goalLati = 0;
        goalLongi = 0;
        Sourceaddress = "";
        startLati = 0;
        goalLati = 0;
        startLongi = 0;
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

    }


    @Override
    public void onResume() {
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
        Intent in = new Intent(CarSelectionTwo.this, Booking.class);
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

                laykm.setVisibility(View.VISIBLE);
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
                lineOptions.color(Color.BLUE);
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
                goalLati = Double.parseDouble(recentAddresses.get(position).getLat());
                goalLongi = Double.parseDouble(recentAddresses.get(position).getLang());
                mOrigin = new LatLng(startLati, startLongi);
                mDestination = new LatLng(goalLati, goalLongi);
                if (startLongi != 0.0 && startLongi != 0.0 && goalLati != 0.0 && goalLongi != 0.0) {
                    drawRoute();
                }
            } else if (selectBox == 0) {
                Sourceaddress = recentAddresses.get(position).getAddress();
                etAddressSelectsource.setText(Sourceaddress);
                startLati = Double.parseDouble(recentAddresses.get(position).getLat());
                startLongi = Double.parseDouble(recentAddresses.get(position).getLang());
                mOrigin = new LatLng(startLati, startLongi);
                mDestination = new LatLng(goalLati, goalLongi);
                if (startLongi != 0.0 && startLongi != 0.0 && goalLati != 0.0 && goalLongi != 0.0) {
                    drawRoute();
                }
            } else if (selectBox == 1) {
                Destiaddress = recentAddresses.get(position).getAddress();
                etAddressSelectdesti.setText(Destiaddress);
                goalLati = Double.parseDouble(recentAddresses.get(position).getLat());
                goalLongi = Double.parseDouble(recentAddresses.get(position).getLang());
                mOrigin = new LatLng(startLati, startLongi);
                mDestination = new LatLng(goalLati, goalLongi);
                if (startLongi != 0.0 && startLongi != 0.0 && goalLati != 0.0 && goalLongi != 0.0) {
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
                } else {
                    workId = "";
                }
                Log.e("RES", response.message());

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private SelectBottomAddressDialogFragment.DialogListener dialogListener1 = new SelectBottomAddressDialogFragment.DialogListener() {
        @Override
        public void onClickView(int flag) {

            if (flag == 1) {
                if (etAddressSelectsource.getText().toString().trim().isEmpty()) {
                    return;
                }
                homeAddress = etAddressSelectsource.getText().toString();
            } else {
                if (etAddressSelectdesti.getText().toString().trim().isEmpty()) {
                    return;
                }
                homeAddress = etAddressSelectdesti.getText().toString().trim();
            }
            if (selectBox == 0) {
                etAddressSelectsource.setText(homeAddress);
                Sourceaddress = homeAddress;
                mOrigin = new LatLng(startLati, startLongi);
                mDestination = new LatLng(goalLati, goalLongi);

            } else {
                etAddressSelectdesti.setText(homeAddress);
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
            } else {
                if (etAddressSelectdesti.getText().toString().trim().isEmpty()) {
                    return;
                }
                workAddress = etAddressSelectdesti.getText().toString().trim();
            }
            if (selectBox == 0) {
                etAddressSelectsource.setText(workAddress);
                Sourceaddress = workAddress;
                mOrigin = new LatLng(startLati, startLongi);
                mDestination = new LatLng(goalLati, goalLongi);
            } else {
                etAddressSelectdesti.setText(workAddress);
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

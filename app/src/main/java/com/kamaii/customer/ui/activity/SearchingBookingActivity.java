package com.kamaii.customer.ui.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.kamaii.customer.DTO.AllAtristListDTO;
import com.kamaii.customer.DTO.ArtistDetailsDTO;
import com.kamaii.customer.DTO.ProductDTO;
import com.kamaii.customer.DTO.UserDTO;
import com.kamaii.customer.R;
import com.kamaii.customer.api.apiClient;
import com.kamaii.customer.api.apiRest;
import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.network.NetworkManager;
import com.kamaii.customer.preferences.SharedPrefrence;
import com.kamaii.customer.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import pl.bclogic.pulsator4droid.library.PulsatorLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;

public class SearchingBookingActivity extends AppCompatActivity implements PermissionsListener {

    private MapView mapView;
    private MapboxMap mapboxMap;
    private String TAG = ViewAddressActivity.class.getSimpleName();
    HashMap<String, String> parms = new HashMap<>();
    public String subcatname="",thridcategory="";
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private ArrayList<AllAtristListDTO> allAtristListDTOList = new ArrayList<>();
    double latilive=0;
    double longilive=0;
    PulsatorLayout pulsator;
    public String sub_category_idd="";
    public String mainkm="";
    public String catid="";
    RelativeLayout layloader;
    public static String thirdcategory="0";
    public static String destitime="";
    private PermissionsManager permissionsManager;
    private LocationComponent locationComponent;
    private ArtistDetailsDTO artistDetailsDTO;
    private ArrayList<ProductDTO> productDTOList;
    private ArrayList<ProductDTO> productDTOListtemp;
    private JsonArray array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searching_booking);
        prefrence = SharedPrefrence.getInstance(SearchingBookingActivity.this);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        parms.put(Consts.USER_ID, userDTO.getUser_id());
        mapView = findViewById(R.id.mapppp);
        mapView.onCreate(savedInstanceState);
        pulsator = findViewById(R.id.pulsator);



        sub_category_idd=getIntent().getStringExtra(Consts.SUB_CATEGORY_ID);
        catid=getIntent().getStringExtra(Consts.CATEGORY_ID);



            mainkm=getIntent().getStringExtra(Consts.KM);
            thirdcategory=getIntent().getStringExtra(Consts.THIRACATEGORY);
            destitime=getIntent().getStringExtra(Consts.DESTI_TIME);


        pulsator.start();

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMapp) {
                mapboxMap = mapboxMapp;
                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        enableLocationComponent(style);
                        addDestinationIconSymbolLayer(style);

                        if (NetworkManager.isConnectToInternet(SearchingBookingActivity.this)) {
                            getArtist();

                        } else {
                            ProjectUtils.showToast(SearchingBookingActivity.this, getResources().getString(R.string.internet_concation));
                        }
                    }
                });
            }
        });


    }
    private void addDestinationIconSymbolLayer(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addImage("destination-icon-id",
                BitmapFactory.decodeResource(this.getResources(), R.drawable.mapbox_marker_icon_default));
        GeoJsonSource geoJsonSource = new GeoJsonSource("destination-source-id");
        loadedMapStyle.addSource(geoJsonSource);
        SymbolLayer destinationSymbolLayer = new SymbolLayer("destination-symbol-layer-id", "destination-source-id");
        destinationSymbolLayer.withProperties(
                iconImage("destination-icon-id"),
                iconAllowOverlap(true),
                iconIgnorePlacement(true)
        );
        loadedMapStyle.addLayer(destinationSymbolLayer);
    }

    public void getArtist() {
        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.getArtist(catid,sub_category_idd,thirdcategory,userDTO.getUser_id(),prefrence.getValue(Consts.LATITUDE),prefrence.getValue(Consts.LONGITUDE));
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

                            allAtristListDTOList = new ArrayList<>();
                            Type getpetDTO = new TypeToken<List<AllAtristListDTO>>() {
                            }.getType();
                            allAtristListDTOList = new Gson().fromJson(object.getJSONArray("data").toString(), getpetDTO);


                            for (int i=0;i<allAtristListDTOList.size();i++){


                                latilive= Double.parseDouble(allAtristListDTOList.get(i).getLive_lat());
                                longilive= Double.parseDouble(allAtristListDTOList.get(i).getLive_long());



                                IconFactory iconFactory = IconFactory.getInstance(SearchingBookingActivity.this);
                                Icon icon = iconFactory.fromResource(R.drawable.pin_driver);

                                mapboxMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(latilive, longilive))
                                        .icon(icon));


                            }
                            getService();

                        } catch (Exception e) {
                            e.printStackTrace();

                        }



                    }
                    else {


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
    public void getService() {

            Retrofit retrofit = apiClient.getClient();
            apiRest api = retrofit.create(apiRest.class);
            Call<ResponseBody> callone = api.getArtistbyid_third(sub_category_idd,allAtristListDTOList.get(0).getUser_id(),userDTO.getUser_id(),thirdcategory);
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
                                artistDetailsDTO = new Gson().fromJson(object.getJSONObject("data").toString(), ArtistDetailsDTO.class);
                                showData();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }



                        }
                        else {


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



    public void showData() {

        array = new JsonArray();
        productDTOList = new ArrayList<>();
        productDTOList = artistDetailsDTO.getProducts();
        if (productDTOList.size() > 0) {


            int km = Integer.parseInt(mainkm);
            DecimalFormat newFormat = new DecimalFormat("####");


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
                    productDTOList.get(i).setQuantityvalue(String.valueOf(Integer.parseInt(productDTOList.get(i).getQuantityvalue()) * 2));
                }
            }


        }

            productDTOListtemp=new ArrayList<>();
            productDTOListtemp.add(productDTOList.get(0));
            Bundle b = new Bundle();
            b.putSerializable(Consts.CARTDATA, (Serializable) productDTOListtemp);
            ProjectUtils.pauseProgressDialog();

            Intent in = new Intent(SearchingBookingActivity.this, Booking.class);
            in.putExtra(Consts.ARTIST_DTO, artistDetailsDTO);
            in.putExtra(Consts.ARTIST_ID, allAtristListDTOList.get(0).getUser_id());
            in.putExtra(Consts.SCREEN_TAG, 2);
            in.putExtra(Consts.PRICE, productDTOList.get(0).getPrice());
            in.putExtra(Consts.CHANGE_PRICE, array.toString());
            in.putExtras(b);
            startActivity(in);



    }




    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {

        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(this, loadedMapStyle);
            locationComponent.setLocationComponentEnabled(true);

            locationComponent.setCameraMode(CameraMode.TRACKING);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            enableLocationComponent(mapboxMap.getStyle());
        } else {
            finish();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

}

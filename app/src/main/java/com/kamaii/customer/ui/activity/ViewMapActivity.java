package com.kamaii.customer.ui.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.kamaii.customer.DTO.UserDTO;
import com.kamaii.customer.R;
import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.preferences.SharedPrefrence;

public class ViewMapActivity extends AppCompatActivity {

    String customer_address="",customer_name="";
    private MapView mMapView;
    private SharedPrefrence prefrence;
    String customer_longitude,customer_latitude;
    private UserDTO userDTO;
    private MapboxMap mapboxMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(ViewMapActivity.this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_view_map);
        prefrence = SharedPrefrence.getInstance(ViewMapActivity.this);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        customer_latitude=getIntent().getStringExtra("customer_latitude");
        customer_longitude=getIntent().getStringExtra("customer_longitude");
        customer_address=getIntent().getStringExtra("customer_address");
        customer_name=getIntent().getStringExtra("customer_name");


        mMapView = (MapView) findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);



        mMapView.onResume();



        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMapp) {
                mapboxMap = mapboxMapp;
                mapboxMap.setStyle(Style.LIGHT, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {


                        LatLng sourcelatlang = new LatLng(Double.parseDouble(customer_latitude), Double.parseDouble(customer_longitude));

                        CameraPosition position = new CameraPosition.Builder()
                                .target(sourcelatlang)
                                .zoom(10)
                                .build();

                        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 2000);

                        IconFactory iconFactory = IconFactory.getInstance(ViewMapActivity.this);
                        Icon icon = iconFactory.fromResource(R.drawable.pin_driver);

                        mapboxMap.addMarker(new MarkerOptions()
                                .position(new LatLng(Double.parseDouble(customer_latitude), Double.parseDouble(customer_longitude)))
                                .icon(icon));


                    }
                });
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}

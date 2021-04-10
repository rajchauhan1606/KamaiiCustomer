package com.kamaii.customer.dialogs;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.kamaii.customer.R;
import com.kamaii.customer.databinding.ActivityAddressBottomDialogBinding;
import com.kamaii.customer.ui.activity.BookingProduct;
import com.kamaii.customer.ui.activity.ViewAddressActivity;

import java.util.Arrays;
import java.util.List;

public class AddressBottomDialog extends AppCompatActivity implements View.OnClickListener {

    ActivityAddressBottomDialogBinding binding;
    private static final String API_KEY = "AIzaSyCIpO2XuPQr8HDOg92EU0pRkP0G9rKvFoY";
    List<Place.Field> fields = Arrays.asList(Place.Field.LAT_LNG, Place.Field.ADDRESS);
    private static int AUTOCOMPLETE_REQUEST_CODE = 1;
    public static String dest_address = "";
    public static Double dest_latitude = 0.0;
    public static Double dest_longitude = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_bottom_dialog);

        init();
    }

    private void init() {

        Places.initialize(getApplicationContext(), API_KEY);

        setListeners();
    }

    private void setListeners() {


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.address_lay_dest_location:
                findLocation();
                break;
            case R.id.address_dialog_close_img:
                onBackPressed();
                break;
        }
    }

    private void findLocation() {

        Intent intentt = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .setCountry("IN")
                .build(AddressBottomDialog.this);

        startActivityForResult(intentt, AUTOCOMPLETE_REQUEST_CODE);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE && resultCode == RESULT_OK) {
            Place place = Autocomplete.getPlaceFromIntent(data);

            dest_address = place.getAddress();
            dest_latitude = place.getLatLng().latitude;
            dest_longitude = place.getLatLng().longitude;
        }
    }
}
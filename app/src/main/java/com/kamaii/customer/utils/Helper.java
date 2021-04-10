package com.kamaii.customer.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;
import java.io.InputStream;

public class Helper {
    private static final String DIRECTION_API = "https://maps.googleapis.com/maps/api/directions/json?origin=";
    public static final String API_KEY = "AIzaSyA-gP5b9DyKvmMK8K3YQx9v8G2AnsjbaBA";
    public static final int MY_SOCKET_TIMEOUT_MS = 5000;
    public static String getUrl(String originLat, String originLon, String destinationLat, String destinationLon){
        return Helper.DIRECTION_API + originLat+","+originLon+"&destination="+destinationLat+","+destinationLon+"&key="+API_KEY;
    }
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public static String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("places.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}

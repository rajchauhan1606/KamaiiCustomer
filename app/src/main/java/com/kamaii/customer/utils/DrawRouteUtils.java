package com.kamaii.customer.utils;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.kamaii.customer.DTO.TaxiRouteData;
import com.kamaii.customer.R;
import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.preferences.SharedPrefrence;
import com.kamaii.customer.ui.activity.ViewAddressActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.kamaii.customer.interfacess.Consts.ROUTE_PATH;

public class DrawRouteUtils {
    private Context context;
    private LatLng mOrigin, mDestination;
    private int bookingId;
    private SharedPrefrence prefrence;
    private String km = "0", routeDuration = "0";
    private LatLng mOrigin1;
    private LatLng mDestination1;
    private LatLng mOrigin2;
    private LatLng mDestination2;

    public DrawRouteUtils(Context context, int bookingId, LatLng mOrigin, LatLng mDestination) {
        this.context = context;
        this.mOrigin = mOrigin;
        this.bookingId = bookingId;
        this.mDestination = mDestination;
        prefrence = SharedPrefrence.getInstance(context);
        drawRoute();
    }

    public DrawRouteUtils() {
    }

    public List<List<HashMap<String, String>>> parse(JSONObject jObject) {

        List<List<HashMap<String, String>>> routes = new ArrayList<>();
        JSONArray jRoutes = null;
        JSONArray jLegs = null;
        JSONArray jSteps = null;

        try {

            jRoutes = jObject.getJSONArray("routes");

            /** Traversing all routes */
            for (int i = 0; i < jRoutes.length(); i++) {
                jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
                List path = new ArrayList<HashMap<String, String>>();

                /** Traversing all legs */
                for (int j = 0; j < jLegs.length(); j++) {
                    jSteps = ((JSONObject) jLegs.get(j)).getJSONArray("steps");

                    /** Traversing all steps */
                    for (int k = 0; k < jSteps.length(); k++) {
                        String polyline = "";
                        polyline = (String) ((JSONObject) ((JSONObject) jSteps.get(k)).get("polyline")).get("points");
                        List<LatLng> list = decodePoly(polyline);

                        /** Traversing all points */
                        for (int l = 0; l < list.size(); l++) {
                            HashMap<String, String> hm = new HashMap<String, String>();
                            hm.put("lat", Double.toString(((LatLng) list.get(l)).latitude));
                            hm.put("lng", Double.toString(((LatLng) list.get(l)).longitude));
                            path.add(hm);
                        }
                    }
                    routes.add(path);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
        }
        return routes;
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Mode of driving

        //String mode="mode=driving";
        // String transit_routing="transit_routing_preference=less_driving";

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Key
      //  String key = "key=" + context.getResources().getString(R.string.directionkey);
        String key = "";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + key;

        // Output format
        String output = "json";

        // Building the url to the web service
      //  String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        String url = "";

        return url;
    }

    private void drawRoute() {


       /* String url = getDirectionsUrl(mOrigin, mDestination);

        DownloadTask downloadTask = new DownloadTask();

        // Start downloading json data from Google Directions API
        downloadTask.execute(url);*/
    }

    private void drawRoute2() {

        mDestination1 = new LatLng(Double.parseDouble(prefrence.getValue(Consts.LATITUDE)), Double.parseDouble(prefrence.getValue(Consts.LONGITUDE)));
        mDestination2 = new LatLng(23.041983, 72.589373);
        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(mDestination1, mDestination2);

        DownloadTask downloadTask = new DownloadTask();

        // Start downloading json data from Google Directions API
        downloadTask.execute(url);
    }

    public List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
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
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask();
            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /**
     * A class to parse the Google Directions in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, JSONArray> {

        // Parsing the data in non-ui thread
        @Override
        protected JSONArray doInBackground(String... jsonData) {
            JSONObject jObject;
            JSONArray routes = null;
            try {
                jObject = new JSONObject(jsonData[0]);
                prefrence.setValue(bookingId + ROUTE_PATH, jObject.toString());
                routes = jObject.getJSONArray("routes");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(JSONArray result) {
            getFindTime(result);
        }
    }

    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public static double getKmFromLatLong(double lat1, double lng1, double lat2, double lng2) {

        Location loc1 = new Location("");
        loc1.setLatitude(lat1);
        loc1.setLongitude(lng1);
        Location loc2 = new Location("");
        loc2.setLatitude(lat2);
        loc2.setLongitude(lng2);
        double distanceInMeters = loc1.distanceTo(loc2);
        return distanceInMeters / 1000;
    }

    public void getFindTime(JSONArray routes) {
        ArrayList<TaxiRouteData> taxiRouteDataArrayList = new ArrayList<>();
        try {


            JSONObject routess = routes.getJSONObject(0);
            JSONArray legs = routess.getJSONArray("legs");

            JSONObject leggs = legs.getJSONObject(0);

            JSONObject total_distance = leggs.getJSONObject("distance");
            long tot_meter = total_distance.getLong("value");

            JSONObject total_duration = leggs.getJSONObject("duration");
            long tot_time = total_duration.getLong("value");

            JSONObject total_start_location = leggs.getJSONObject("start_location");
            String start_lat = total_start_location.getString("lat");
            String start_lng = total_start_location.getString("lng");

            TaxiRouteData taxiRouteData = new TaxiRouteData();
            taxiRouteData.setLatitude(Double.parseDouble(start_lat));
            taxiRouteData.setLongitude(Double.parseDouble(start_lng));
            taxiRouteData.setTime(String.valueOf(tot_time));
            taxiRouteData.setDistance(tot_meter);
            taxiRouteDataArrayList.add(taxiRouteData);

            JSONObject total_end_location = leggs.getJSONObject("end_location");
            String end_lat = total_end_location.getString("lat");
            String end_lng = total_end_location.getString("lng");
            TaxiRouteData taxiRouteData1 = new TaxiRouteData();
            taxiRouteData1.setLatitude(Double.parseDouble(end_lat));
            taxiRouteData1.setLongitude(Double.parseDouble(end_lng));
            taxiRouteData1.setTime(String.valueOf(tot_time));
            taxiRouteData1.setDistance(tot_meter);

            JSONArray steps = leggs.getJSONArray("steps");

            for (int i = 0; i < steps.length(); i++) {
                JSONObject jo_inside = steps.getJSONObject(i);

                JSONObject distance = jo_inside.getJSONObject("distance");
                JSONObject duration = jo_inside.getJSONObject("duration");

                long meter = distance.getLong("value");
                long time = duration.getLong("value");


                JSONObject destinationlocation = jo_inside.getJSONObject("end_location");
                String lat = destinationlocation.getString("lat");
                String lng = destinationlocation.getString("lng");
                TaxiRouteData taxiRouteData3 = new TaxiRouteData();
                taxiRouteData3.setLatitude(Double.parseDouble(lat));
                taxiRouteData3.setLongitude(Double.parseDouble(lng));
                taxiRouteData3.setTime(String.valueOf(time));
                taxiRouteData3.setDistance(meter);

                taxiRouteDataArrayList.add(taxiRouteData3);
            }
            taxiRouteDataArrayList.add(taxiRouteData1);
            prefrence.setRoute(taxiRouteDataArrayList, String.valueOf(bookingId));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

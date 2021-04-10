package com.kamaii.customer.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kamaii.customer.DTO.UserBooking;
import com.kamaii.customer.R;
import com.kamaii.customer.ui.activity.TrackingActivity;
import com.kamaii.customer.utils.CustomTextView;
import com.kamaii.customer.utils.CustomTextViewBold;
import com.kamaii.customer.utils.GlobalUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.kamaii.customer.interfacess.Consts.BOOKING;
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

public class CurruntBookingListAdapter extends RecyclerView.Adapter<CurruntBookingListAdapter.CurrentViewholder> {

    Context context;
    List<UserBooking> arrayList;
    List<UserBooking> arrayListduplicate;

    public CurruntBookingListAdapter(Context context, List<UserBooking> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        arrayListduplicate = new ArrayList<>();
    }

    @NonNull
    @Override
    public CurrentViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.current_booking_layout, parent, false);
        return new CurrentViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrentViewholder holder, int position) {


//           holder.current_booking_relative.setVisibility(View.VISIBLE);
        holder.current_booking_name.setText(arrayList.get(position).getArtistName());
        holder.current_booking_prd.setText(arrayList.get(position).getOrder_product());
        Glide.with(context).load(arrayList.get(position).getArtistImage()).into(holder.current_booking_artist);

        holder.current_booking_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrayList.remove(position);
                arrayListduplicate.addAll(arrayList);
//                arrayList.addAll(arrayListduplicate);
                notifyDataSetChanged();
            }
        });

        holder.current_booking_relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.current_booking_marker.performClick();
            }
        });
        holder.current_booking_marker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String vendor_latitude = arrayList.get(position).getArtistlivelat();
                String vendor_longitude = arrayList.get(position).getArtistlivelang();
                String vendor_name = arrayList.get(position).getArtistName();
                String artist_id = arrayList.get(position).getArtist_id();


                if (arrayList.get(position).getLocation_status().equalsIgnoreCase("0")) {
                    Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                    List<Address> addresses = null;
                    Address obj = null;
                    try {
                        addresses = geocoder.getFromLocation(Double.parseDouble(vendor_latitude), Double.parseDouble(vendor_longitude), 1);
                        obj = addresses.get(0);
                        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + obj.getAddressLine(0));
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        context.startActivity(mapIntent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("LODULALIT", vendor_latitude + "->" + vendor_longitude);
                    Intent intent = new Intent(context, TrackingActivity.class);
                    intent.putExtra(TRACK_BOOKING_VENDOR_LATITUDE, vendor_latitude);
                    Log.e("tracking_btn", "1->" + vendor_latitude);
                    intent.putExtra(TRACK_BOOKING_VENDOR_LONGITUDE, vendor_longitude);
                    Log.e("tracking_btn", "2->" + vendor_longitude);
                    intent.putExtra(TRACK_SOURCE_CUSTOMER_LATITUDE, arrayList.get(position).getSource_lat());


                    Log.e("tracking_btn", "3" + arrayList.get(position).getSource_lat());
                    intent.putExtra(TRACK_BOOKING_RIDER_ORDER_TRACKER, arrayList.get(position).getRider_order());
                    Log.e("tracking_btn", "4" + arrayList.get(position).getRider_order());
                    intent.putExtra(TRACK_BOOKING_RIDER_LATITUDE, arrayList.get(position).getRider_lat());
                    Log.e("tracking_btn", "5" + arrayList.get(position).getRider_lat());
                    intent.putExtra(TRACK_BOOKING_RIDER_LONGITUDE, arrayList.get(position).getRider_long());
                    Log.e("tracking_btn", "6" + arrayList.get(position).getRider_long());
                    intent.putExtra(TRACK_SOURCE_CUSTOMER_LONGITUDE, arrayList.get(position).getSource_long());
                    Log.e("tracking_btn", "7" + arrayList.get(position).getSource_long());
                    intent.putExtra(TRACK_DESTINATION_CUSTOMER_LATITUDE, arrayList.get(position).getDest_lat());
                    Log.e("tracking_btn", "8" + arrayList.get(position).getDest_lat());
                    intent.putExtra(TRACK_DESTINATION_CUSTOMER_LONGITUDE, arrayList.get(position).getDest_long());
                    Log.e("tracking_btn", "9" + arrayList.get(position).getDest_long());


                    intent.putExtra(TRACK_BOOKING_VENDOR_NAME, vendor_name);
                    Log.e("tracking_btn", "10" + vendor_name);
                    intent.putExtra(TRACK_ARTIST_ID, artist_id);
                    Log.e("tracking_btn", "11" + artist_id);
                    intent.putExtra(TRACK_BOOKING_VENDOR_IMAGE, arrayList.get(position).getArtistImage());
                    Log.e("tracking_btn", "12" + arrayList.get(position).getArtistImage());
                    intent.putExtra(TRACK_BOOKING_SOURCE_ADDRESS, arrayList.get(position).getAddress());
                    Log.e("tracking_btn", "13" + arrayList.get(position).getAddress());
                    intent.putExtra(TRACK_BOOKING_DESTINATION_ADDRESS, arrayList.get(position).getDestinationaddress());
                    Log.e("tracking_btn", "14" + arrayList.get(position).getDestinationaddress());
                    intent.putExtra(TRACK_BOOKING_TOTAL_AMOUNT, arrayList.get(position).getPrice());
                    Log.e("tracking_btn", "15" + arrayList.get(position).getPrice());
                    intent.putExtra(TRACK_BOOKING_PRODUCT_NAME, arrayList.get(position).getProduct().get(0).getProduct_name());
                    Log.e("tracking_btn", "16" + arrayList.get(position).getProduct().get(0).getProduct_name());
                    intent.putExtra(TRACK_BOOKING_MOBILE_NUMBER, arrayList.get(position).getArtistMobile());
                    Log.e("tracking_btn", "17" + arrayList.get(position).getArtistMobile());
                    intent.putExtra(TRACK_BOOKING_SCREEN_FLAG, GlobalUtils.MY_BOOKING_SCREEN);
                    Log.e("tracking_btn", "18" + GlobalUtils.MY_BOOKING_SCREEN);
                    intent.putExtra(TRACK_BOOKING_BOOKING_FLAG, arrayList.get(position).getBooking_flag());
                    Log.e("tracking_btn", "19" + arrayList.get(position).getBooking_flag());
                    intent.putExtra(TRACK_BOOKING_ID, arrayList.get(position).getId());
                    Log.e("tracking_btn", "20" + arrayList.get(position).getId());
                    intent.putExtra(TRACK_BOOKING_PAYMENT_TYPE, arrayList.get(position).getPay_type());
                    Log.e("tracking_btn", "21" + arrayList.get(position).getPay_type());
                    intent.putExtra(TRACK_BOOKING_DATE, arrayList.get(position).getBooking_date());
                    Log.e("tracking_btn", "22" + arrayList.get(position).getBooking_date());
                    intent.putExtra(TRACK_BOOKING_TIME, arrayList.get(position).getBooking_time());
                    Log.e("tracking_btn", "23" + arrayList.get(position).getBooking_time());
                    intent.putExtra(TRACK_VEHICLE_NUMBER, arrayList.get(position).getProduct().get(0).getVehicle_number());
                    Log.e("tracking_btn", "24" + arrayList.get(position).getProduct().get(0).getVehicle_number());
                    intent.putExtra(TRACK_CAT_ID, arrayList.get(position).getProduct().get(0).getCategory_id());
                    Log.e("tracking_btn", "25" + arrayList.get(position).getProduct().get(0).getCategory_id());
                    intent.putExtra(TRACK_SUB_ID, arrayList.get(position).getProduct().get(0).getSub_category_id());
                    Log.e("tracking_btn", "26" + arrayList.get(position).getProduct().get(0).getSub_category_id());
                    intent.putExtra(TRACK_SUB_LEVEL_ID, arrayList.get(position).getProduct().get(0).getSublevel_category());
                    Log.e("tracking_btn", "27" + arrayList.get(position).getProduct().get(0).getSublevel_category());
                    intent.putExtra(BOOKING, "customerbooking");
                    if (arrayList.get(position).getProduct().get(0).getCategory_id().equals("85") || arrayList.get(position).getProduct().get(0).getCategory_id().equals("126") || arrayList.get(position).getProduct().get(0).getCategory_id().equals("74")) {
                        intent.putExtra("category_type", false);
                    } else {
                        intent.putExtra("category_type", true);
                    }
                    context.startActivity(intent);
                }

            }
        });
       /*else {
            holder.current_booking_relative.setVisibility(View.GONE);
       }*/
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class CurrentViewholder extends RecyclerView.ViewHolder {

        CustomTextViewBold current_booking_name;
        CustomTextView current_booking_prd;
        ImageView current_booking_marker;
        RelativeLayout current_booking_relative;
        ImageView current_booking_artist,current_booking_close;

        public CurrentViewholder(@NonNull View itemView) {
            super(itemView);

            current_booking_relative = itemView.findViewById(R.id.current_booking_relative);
            current_booking_prd = itemView.findViewById(R.id.current_booking_prd);
            current_booking_close = itemView.findViewById(R.id.current_booking_close);
            current_booking_artist = itemView.findViewById(R.id.current_booking_artist);
            current_booking_name = itemView.findViewById(R.id.current_booking_name);
            current_booking_marker = itemView.findViewById(R.id.current_booking_marker);
        }
    }
}

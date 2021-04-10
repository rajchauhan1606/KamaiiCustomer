package com.kamaii.customer.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.nfc.Tag;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kamaii.customer.DTO.ProductDTO;
import com.kamaii.customer.DTO.UserBooking;
import com.kamaii.customer.DTO.UserDTO;
import com.kamaii.customer.R;
import com.kamaii.customer.https.HttpsRequest;
import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.interfacess.Helper;
import com.kamaii.customer.interfacess.OnSelectedItemListenerCart;
import com.kamaii.customer.network.NetworkManager;
import com.kamaii.customer.ui.activity.BaseActivity;
import com.kamaii.customer.ui.activity.BookingProduct;
import com.kamaii.customer.ui.activity.TrackingActivity;
import com.kamaii.customer.ui.activity.ViewInvoice;
import com.kamaii.customer.ui.activity.ViewMapActivity;
import com.kamaii.customer.ui.fragment.MyBooking;
import com.kamaii.customer.utils.CustomTextView;
import com.kamaii.customer.utils.CustomTextViewBold;
import com.kamaii.customer.utils.GlobalUtils;
import com.kamaii.customer.utils.ItemDecorationAlbumColumns;
import com.kamaii.customer.utils.MultiTouchListener;
import com.kamaii.customer.utils.OnTouch;
import com.kamaii.customer.utils.ProjectUtils;
import com.tsuryo.androidcountdown.Counter;
import com.tsuryo.androidcountdown.TimeUnits;

import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

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


public class AdapterCustomerBooking extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String TAG = AdapterCustomerBooking.class.getSimpleName();
    MyBooking myBooking;
    private Context mContext;
    private ArrayList<UserBooking> objects = null;
    private HashMap<String, String> paramsDecline;
    private UserDTO userDTO;
    private HashMap<String, String> paramsRepeat;
    private HashMap<String, String> paramsBookingOp;
    private DialogInterface dialog_book;
    int min;
    int sec;
    private GridLayoutManager gridLayoutManager;
    AdapterCartBooking adapterCartBooking;
    private ArrayList<ProductDTO> productDTOArrayList;
    String locationstatus = "";
    ImageView img_priview;
    private Dialog dialogpriview;
    String id = "";

    public AdapterCustomerBooking(MyBooking myBooking, ArrayList<UserBooking> objects, UserDTO userDTO) {
        this.myBooking = myBooking;
        mContext = myBooking.getActivity();
        this.objects = objects;
        this.userDTO = userDTO;
        Log.e("userBookingList12345", "" + objects.toString());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_customer_booking, parent, false);
        vh = new MyViewHolder(v);
        return vh;
    }

    private OnSelectedItemListenerCart onItemListener = new OnSelectedItemListenerCart() {
        @Override
        public void setOnClick(ArrayList<ProductDTO> productDTOArrayList, int position) {
        }

    };

    public boolean checkarss(String catid) {

        boolean value = false;
        for (int i = 0; i < BaseActivity.addressModelArrayList.size(); i++) {
            if (BaseActivity.addressModelArrayList.get(i).getCat_id().equalsIgnoreCase(catid)) {
                value = true;
                break;

            } else {
                value = false;

            }

        }
        return value;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holderMain, int pos) {
        final int position = pos;
        if (holderMain instanceof MyViewHolder) {
            MyViewHolder holder = (MyViewHolder) holderMain;
            dialogpriview = new Dialog(mContext, R.style.Theme_Dialog);
            dialogpriview.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogpriview.setContentView(R.layout.dailog_priview);
            dialogpriview.setCancelable(true);
            gridLayoutManager = new GridLayoutManager(mContext, 1);
            productDTOArrayList = new ArrayList<>();
            productDTOArrayList = objects.get(position).getProduct();
            id = objects.get(position).getId();
            locationstatus = objects.get(position).getLocation_status();
            adapterCartBooking = new AdapterCartBooking(myBooking, productDTOArrayList, onItemListener, locationstatus, id);
            holder.rv_cart.setLayoutManager(gridLayoutManager);
            holder.rv_cart.addItemDecoration(new ItemDecorationAlbumColumns(
                    2,
                    4));
            holder.rv_cart.setAdapter(adapterCartBooking);
            holder.tvtotal.setText(objects.get(position).getPrice() + " " + "Rs");
            if (objects.get(position).getPay_type().equals("1")) {
                holder.tvPay.setText("Cash");
            } else if (objects.get(position).getPay_type().equals("2")) {
                holder.tvPay.setText("Kamaii Wallet");
            } else {
                holder.tvPay.setText("Online Payment");
            }
            Glide.with(mContext).
                    load(objects.get(position).getArtistImage())
                    .placeholder(R.drawable.dummyuser_image)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.ivArtist);


            holder.txt_tracking.setVisibility(View.VISIBLE);
            holder.txt_trackingDummy.setVisibility(View.GONE);
            holder.view4.setVisibility(View.VISIBLE);
            holder.discount_txt.setText(Html.fromHtml(objects.get(position).getDiscount_txt()));
            holder.discount_digit_txt.setText(Html.fromHtml("₹ " + objects.get(position).getDiscount_digit_txt()));
            holder.discounted_rupee_symbol2.setText("₹ " + objects.get(position).getItemtotal());
            holder.discounted_rupee_symbol1.setText(Html.fromHtml(objects.get(position).getItemstriketotal()));
            holder.tvPrice_f.setText(objects.get(position).getNetpay());
            holder.order_preparation_note.setText(Html.fromHtml(objects.get(position).getPreparationNote()));

            holder.tvStatus.setText(objects.get(position).getBooking_msg2());

            if (productDTOArrayList.get(0).getSub_category_id().equals("242") || productDTOArrayList.get(0).getSub_category_id().equals("434")) {
                holder.service_txt.setText("Driver allowance");
                if (objects.get(position).getServicecharge().equals("0") || objects.get(position).getServicecharge().equals("0.00")) {
                    holder.service_txt.setVisibility(View.GONE);
                    holder.service_digit_txt1.setVisibility(View.GONE);
                } else {
                    holder.service_digit_txt1.setText("₹ " + objects.get(position).getServicecharge());
                }
            } else if (productDTOArrayList.get(0).getSub_category_id().equals("453")) {
                holder.service_txt.setVisibility(View.GONE);
                holder.service_digit_txt1.setVisibility(View.GONE);
            } else {
                holder.service_txt.setText("Shipping Charge");
                if (objects.get(position).getServicecharge().equals("0") || objects.get(position).getServicecharge().equals("0.00")) {
                    holder.service_digit_txt1.setText("Free");
                } else {
                    holder.service_digit_txt1.setText("₹ " + objects.get(position).getServicecharge());
                }
            }

            if (!objects.get(position).getApproxdatetime().equalsIgnoreCase("")) {
                holder.tvapproxtime.setText("Approx Time" + " : " + objects.get(position).getApproxdatetime());
            }
            if (objects.get(position).getLocation_status().equalsIgnoreCase("1")) {
                holder.ivMap.setVisibility(View.GONE);
            }
            if (!objects.get(position).getArtistMobile().equalsIgnoreCase("")) {
                holder.ivcall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String mobileno = objects.get(position).getArtistMobile();

                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + mobileno));
                        mContext.startActivity(intent);
                    }
                });
            }


            if (!objects.get(position).getImage().equalsIgnoreCase("")) {
                Glide.with(mContext).
                        load(Consts.IMAGE_URL + objects.get(position).getImage())
                        .placeholder(R.drawable.dafault_product)
                        .into(holder.iviamge);
            }


            holder.iviamge.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public void onClick(View v) {
                    img_priview = dialogpriview.findViewById(R.id.img_priview);

                    if (!objects.get(position).getImage().equalsIgnoreCase("")) {
                        Glide.with(mContext).
                                load(Consts.IMAGE_URL + objects.get(position).getImage())
                                .placeholder(R.drawable.dafault_product)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(img_priview);
                    }

                    img_priview.setOnTouchListener(new MultiTouchListener(onTouch));
                    dialogpriview.show();
                }
            });
            if (objects.get(position).getBooking_type().equalsIgnoreCase("0") || objects.get(position).getBooking_type().equalsIgnoreCase("3")) {
                if (objects.get(position).getBooking_flag().equalsIgnoreCase("0")) {
                    Log.e("rider-order1234", "1" + "pos:-- " + position);

                    holder.cardData.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                    holder.llStatus.setBackground(mContext.getResources().getDrawable(R.drawable.rectangle_red));
                    holder.ivStatus.setBackground(mContext.getResources().getDrawable(R.drawable.ic_waiting));
                } else if (objects.get(position).getBooking_flag().equalsIgnoreCase("1")) {
                    Log.e("rider-order1234", "2" + "pos:-- " + position);
                    holder.cardData.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                    holder.llStatus.setBackground(mContext.getResources().getDrawable(R.drawable.rectangle_yellow));
                    holder.ivStatus.setBackground(mContext.getResources().getDrawable(R.drawable.ic_accept));
                } else if (objects.get(position).getBooking_flag().equalsIgnoreCase("3")) {
                    Log.e("rider-order1234", "3" + "pos:-- " + position);
                    holder.cardData.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                    holder.tvStatus.setTextColor(mContext.getResources().getColor(R.color.blue_text_color));
                    holder.llStatus.setBackground(mContext.getResources().getDrawable(R.drawable.rectangle_green));
                    holder.llTime.setVisibility(View.VISIBLE);
                    holder.llCancel.setVisibility(View.GONE);
                    holder.llCancelDummy.setVisibility(View.VISIBLE);
                    holder.txt_tracking.setVisibility(View.VISIBLE);
                    holder.txt_trackingDummy.setVisibility(View.GONE);
                    holder.view4.setVisibility(View.VISIBLE);
                    holder.llFinish.setVisibility(View.GONE);

                    holder.ivStatus.setBackground(mContext.getResources().getDrawable(R.drawable.ic_inprogress));
                    Drawable unwrappedDrawable = AppCompatResources.getDrawable(mContext, R.drawable.ic_inprogress);
                    Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
                    DrawableCompat.setTint(wrappedDrawable, Color.argb(100, 0, 145, 255));

                    SimpleDateFormat format = new SimpleDateFormat(
                            "yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());

                    Date date = null;
                    try {
                        date = format.parse(objects.get(position).getWorking_time());
                        holder.counter123.setDate(date);//countdown starts

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    holder.counter123.setDate(objects.get(position).getWorking_time()); //countdown starts

                    holder.counter123.setIsShowingTextDesc(true);
                    holder.counter123.setMaxTimeUnit(TimeUnits.DAY);
                    holder.counter123.setTextColor(Color.BLACK);
                    holder.counter123.setTextSize(30);

                    holder.counter123.setListener(new Counter.Listener() {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            Log.d(TAG, "onTick: Counter - " + millisUntilFinished);
                        }

                        @Override
                        public void onTick(long days, long hours, long minutes, long seconds) {
                            Log.d(TAG, "onTick" + days + "D " +
                                    hours + "H " +
                                    minutes + "M " +
                                    seconds + "S ");

                        }
                    });


                } else if (objects.get(position).getBooking_flag().equalsIgnoreCase("2")) {

                    Log.e("rider-order1234", "4" + "pos:-- " + position);
                    holder.llTime.setVisibility(View.GONE);
                    holder.llCancel.setVisibility(View.GONE);
                    holder.llCancelDummy.setVisibility(View.GONE);
                    holder.txt_tracking.setVisibility(View.GONE);
                    holder.txt_trackingDummy.setVisibility(View.GONE);
                    holder.view4.setVisibility(View.GONE);
                    holder.order_preparation_note.setVisibility(View.GONE);

                    holder.llFinish.setVisibility(View.GONE);
                    holder.llPay.setVisibility(View.GONE);
                    if (!objects.get(position).getImage().equalsIgnoreCase("")) {
                        holder.iviamge.setVisibility(View.VISIBLE);
                    }

                    holder.ivcall.setVisibility(View.GONE);
                    holder.order_status.setImageResource(R.drawable.ic__cancell_order_02);
                    holder.ivStatus.setBackground(mContext.getResources().getDrawable(R.drawable.ic_close_circle_blue));
                    holder.tvStatus.setTextColor(mContext.getResources().getColor(R.color.red));
                    holder.tvStatus.setText(mContext.getResources().getString(R.string.cancel_booking));
                } else if (objects.get(position).getBooking_flag().equalsIgnoreCase("4")) {
                    Log.e("rider-order1234", "5" + "pos:-- " + position);
                    holder.cardData.setBackgroundColor(mContext.getResources().getColor(R.color.white));

                    Log.e("INVOICE_POSITION", "" + "Position :" + position + objects.get(position).getInvoice());
                    if (objects.get(position).getInvoice().getFlag().equalsIgnoreCase("1")) {

                        Log.e("rider-order1234", "5 - > 1" + "pos:-- " + position);
                        holder.llTime.setVisibility(View.GONE);
                        holder.llCancel.setVisibility(View.GONE);
                        holder.llCancelDummy.setVisibility(View.GONE);
                        holder.order_preparation_note.setVisibility(View.GONE);
                        holder.txt_tracking.setVisibility(View.VISIBLE);
                        holder.txt_trackingDummy.setVisibility(View.GONE);
                        // holder.view4.setVisibility(View.GONE);
                        holder.llFinish.setVisibility(View.GONE);
                        holder.llPay.setVisibility(View.GONE);
                        holder.tracking_relative.setVisibility(View.GONE);
                        holder.cancell_relative.setVisibility(View.GONE);
                        holder.repeat_relative.setVisibility(View.VISIBLE);
                        holder.repeat_order.setVisibility(View.VISIBLE);

                        if (!objects.get(position).getImage().equalsIgnoreCase("")) {
                            holder.iviamge.setVisibility(View.VISIBLE);
                        }

                        holder.ivcall.setVisibility(View.GONE);
                        holder.llapproxtime.setVisibility(View.GONE);
                        holder.order_status.setImageResource(R.drawable.ic_ok_01);
                        holder.tvStatus.setTextColor(mContext.getResources().getColor(R.color.green2));
                        holder.tvStatus.setText(mContext.getResources().getString(R.string.invoice_paid));
                        holder.llStatus.setBackground(mContext.getResources().getDrawable(R.drawable.rectangle_green));
                        holder.ivStatus.setBackground(mContext.getResources().getDrawable(R.drawable.ic_accept));
                    } else {
                        Log.e("rider-order1234", "5 -> 2" + "pos:-- " + position);
                        holder.llTime.setVisibility(View.GONE);
                        holder.llCancel.setVisibility(View.GONE);
                        holder.llCancelDummy.setVisibility(View.VISIBLE);
                        holder.txt_tracking.setVisibility(View.GONE);
                        holder.txt_trackingDummy.setVisibility(View.VISIBLE);
                        holder.view4.setVisibility(View.GONE);
                        holder.llFinish.setVisibility(View.GONE);
                        holder.llPay.setVisibility(View.GONE);
                        if (!objects.get(position).getImage().equalsIgnoreCase("")) {
                            holder.iviamge.setVisibility(View.VISIBLE);
                        }
                        holder.tvStatus.setText(mContext.getResources().getString(R.string.invoice_genrate));

                        holder.txtpaynow.setText("Rating Now");

                        holder.llStatus.setBackground(mContext.getResources().getDrawable(R.drawable.rectangle_green));
                        holder.ivStatus.setBackground(mContext.getResources().getDrawable(R.drawable.ic_accept));
                    }

                }
            } else if (objects.get(position).getBooking_type().equalsIgnoreCase("2")) {
                Log.e("rider-order1234", "6" + "pos:-- " + position);
                if (objects.get(position).getBooking_flag().equalsIgnoreCase("0")) {
                    Log.e("rider-order1234", "6 -> 1" + "pos:-- " + position);
                    holder.cardData.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                    holder.llStatus.setBackground(mContext.getResources().getDrawable(R.drawable.rectangle_red));
                    holder.ivStatus.setBackground(mContext.getResources().getDrawable(R.drawable.ic_waiting));
                } else if (objects.get(position).getBooking_flag().equalsIgnoreCase("1")) {
                    Log.e("rider-order1234", "6 -> 2" + "pos:-- " + position);

                    holder.cardData.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                    holder.llStatus.setBackground(mContext.getResources().getDrawable(R.drawable.rectangle_yellow));
                    holder.ivStatus.setBackground(mContext.getResources().getDrawable(R.drawable.ic_accept));
                } else if (objects.get(position).getBooking_flag().equalsIgnoreCase("3")) {
                    Log.e("rider-order1234", "6 -> 3" + "pos:-- " + position);

                    holder.cardData.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                    holder.llStatus.setBackground(mContext.getResources().getDrawable(R.drawable.rectangle_green));
                    holder.llTime.setVisibility(View.VISIBLE);
                    holder.llCancel.setVisibility(View.GONE);
                    holder.llCancelDummy.setVisibility(View.VISIBLE);
                    holder.txt_tracking.setVisibility(View.VISIBLE);
                    holder.txt_trackingDummy.setVisibility(View.GONE);
                    holder.view4.setVisibility(View.VISIBLE);
                    holder.llFinish.setVisibility(View.GONE);

                    SimpleDateFormat format = new SimpleDateFormat(
                            "yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());

                    Date date = null;
                    try {
                        date = format.parse(objects.get(position).getWorking_time());
                        holder.counter123.setDate(date);//countdown starts

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    holder.counter123.setDate(objects.get(position).getWorking_time()); //countdown starts

                    holder.counter123.setIsShowingTextDesc(true);
                    holder.counter123.setMaxTimeUnit(TimeUnits.DAY);
                    holder.counter123.setTextColor(Color.BLACK);
                    holder.counter123.setTextSize(50);

                    holder.counter123.setListener(new Counter.Listener() {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            Log.d(TAG, "onTick: Counter - " + millisUntilFinished);
                        }

                        @Override
                        public void onTick(long days, long hours, long minutes, long seconds) {
                            Log.d(TAG, "onTick" + days + "D " +
                                    hours + "H " +
                                    minutes + "M " +
                                    seconds + "S ");

                        }
                    });



                } else if (objects.get(position).getBooking_flag().equalsIgnoreCase("2")) {

                    Log.e("rider-order1234", "7" + "pos:-- " + position);

                    holder.llTime.setVisibility(View.GONE);
                    holder.llCancel.setVisibility(View.GONE);
                    holder.llCancelDummy.setVisibility(View.GONE);
                    holder.txt_tracking.setVisibility(View.GONE);
                    holder.txt_trackingDummy.setVisibility(View.GONE);
                    holder.view4.setVisibility(View.GONE);
                    holder.llFinish.setVisibility(View.GONE);
                    holder.llPay.setVisibility(View.GONE);
                    if (!objects.get(position).getImage().equalsIgnoreCase("")) {
                        holder.iviamge.setVisibility(View.VISIBLE);
                    }
                    holder.order_status.setImageResource(R.drawable.ic__cancell_order_02);
                    holder.ivStatus.setBackground(mContext.getResources().getDrawable(R.drawable.ic_close_circle_blue));
                    holder.ivcall.setVisibility(View.GONE);
                    holder.tvStatus.setTextColor(mContext.getResources().getColor(R.color.red));
                    holder.tvStatus.setText("Booking Canceled");
                } else if (objects.get(position).getBooking_flag().equalsIgnoreCase("4")) {
                    Log.e("rider-order1234", "8" + "pos:-- " + position);

                    holder.cardData.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                    if (objects.get(position).getInvoice().getFlag().equalsIgnoreCase("1")) {

                        Log.e("rider-order1234", "8 -> 1" + "pos:-- " + position);

                        holder.llTime.setVisibility(View.GONE);
                        holder.llCancel.setVisibility(View.GONE);
                        holder.llCancelDummy.setVisibility(View.GONE);
                        holder.txt_tracking.setVisibility(View.GONE);
                        holder.txt_trackingDummy.setVisibility(View.GONE);
                        holder.view4.setVisibility(View.GONE);
                        holder.llFinish.setVisibility(View.GONE);
                        holder.llPay.setVisibility(View.GONE);
                        holder.llViewInvoice.setVisibility(View.VISIBLE);
                        holder.order_status.setImageResource(R.drawable.ic_ok_01);
                        holder.llapproxtime.setVisibility(View.GONE);
                        holder.tvStatus.setTextColor(mContext.getResources().getColor(R.color.green2));
                        holder.tvStatus.setText(mContext.getResources().getString(R.string.invoice_paid));
                        if (!objects.get(position).getImage().equalsIgnoreCase("")) {
                            holder.iviamge.setVisibility(View.VISIBLE);
                        }
                        holder.txtpaynow.setText("Rating Now");
                        holder.ivcall.setVisibility(View.GONE);
                        holder.llStatus.setBackground(mContext.getResources().getDrawable(R.drawable.rectangle_green));
                        holder.ivStatus.setBackground(mContext.getResources().getDrawable(R.drawable.ic_accept));
                    } else {
                        Log.e("rider-order1234", "8 -> 2" + "pos:-- " + position);

                        holder.llTime.setVisibility(View.GONE);
                        holder.llCancel.setVisibility(View.GONE);
                        holder.llCancelDummy.setVisibility(View.GONE);
                        holder.txt_tracking.setVisibility(View.GONE);
                        holder.txt_trackingDummy.setVisibility(View.GONE);
                        //    holder.view4.setVisibility(View.GONE);
                        holder.llFinish.setVisibility(View.GONE);
                        holder.llPay.setVisibility(View.GONE);
                        holder.llViewInvoice.setVisibility(View.VISIBLE);
                        if (!objects.get(position).getImage().equalsIgnoreCase("")) {
                            holder.iviamge.setVisibility(View.VISIBLE);
                        }
                        holder.tvStatus.setText(mContext.getResources().getString(R.string.invoice_genrate));


                        holder.llStatus.setBackground(mContext.getResources().getDrawable(R.drawable.rectangle_green));
                        holder.ivStatus.setBackground(mContext.getResources().getDrawable(R.drawable.ic_accept));
                    }
                }
            }

            if (objects.get(position).getBooking_type().equalsIgnoreCase("0")) {
                if (objects.get(position).getArtist_commission_type().equalsIgnoreCase("0")) {
                    if (objects.get(position).getBooking_flag().equalsIgnoreCase("3")) {
                        float price_hr = Float.parseFloat(objects.get(position).getPrice()) / 60;
                        float total_price = price_hr * min;
                        holder.tvPrice.setText(objects.get(position).getCurrency_type() + String.format("%.2f", total_price));
                    } else {
                        holder.tvPrice.setText(objects.get(position).getCurrency_type() + objects.get(position).getPrice() + mContext.getResources().getString(R.string.hr_add_on));

                    }
                } else {
                    holder.tvPrice.setText(objects.get(position).getCurrency_type() + objects.get(position).getPrice());
                }
            } else if (objects.get(position).getBooking_type().equalsIgnoreCase("2")) {
                holder.tvPrice.setText(objects.get(position).getCurrency_type() + objects.get(position).getPrice());

            } else if (objects.get(position).getBooking_type().equalsIgnoreCase("3")) {
                holder.tvPrice.setText(objects.get(position).getCurrency_type() + objects.get(position).getPrice());

            }


            holder.tvDate.setText(objects.get(position).getBooking_date2() + " - " + objects.get(position).getBooking_time());
            if (objects.get(position).getDescription() != "") {
                holder.tvDescription.setText(objects.get(position).getDescription());
            }
            holder.tvWork.setText(objects.get(position).getCategory_name());
            holder.tvLocation.setText(objects.get(position).getAddress());
            holder.tvJobComplete.setText(objects.get(position).getJobDone() + " " + mContext.getResources().getString(R.string.jobs_completed));
            holder.tvProfileComplete.setText(objects.get(position).getCompletePercentages() + mContext.getResources().getString(R.string.completion));

            holder.tvName.setText(objects.get(position).getArtistName());


            holder.llCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    completeDialog(mContext.getResources().getString(R.string.cancel), mContext.getResources().getString(R.string.booking_cancel_msg) + " " + objects.get(position).getArtistName() + "?", position);
                }
            });
            holder.ivMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String customer_latitude = objects.get(position).getLatitude();
                    String customer_longitude = objects.get(position).getLongitude();
                    String customer_address = objects.get(position).getAddress();
                    String customer_name = objects.get(position).getUserName();

                    Intent intent = new Intent(mContext, ViewMapActivity.class);
                    intent.putExtra("customer_latitude", customer_latitude);
                    intent.putExtra("customer_longitude", customer_longitude);
                    intent.putExtra("customer_address", customer_address);
                    intent.putExtra("customer_name", customer_name);

                    mContext.startActivity(intent);


                }
            });


            holder.txt_tracking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String vendor_latitude = objects.get(position).getArtistlivelat();
                    String vendor_longitude = objects.get(position).getArtistlivelang();
                    String vendor_name = objects.get(position).getArtistName();
                    String artist_id = objects.get(position).getArtist_id();


                    if (objects.get(position).getLocation_status().equalsIgnoreCase("0")) {
                        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
                        List<Address> addresses = null;
                        Address obj = null;
                        try {
                            addresses = geocoder.getFromLocation(Double.parseDouble(vendor_latitude), Double.parseDouble(vendor_longitude), 1);
                            obj = addresses.get(0);
                            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + obj.getAddressLine(0));
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            mContext.startActivity(mapIntent);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.e("LODULALIT", vendor_latitude + "->" + vendor_longitude);
                        Intent intent = new Intent(mContext, TrackingActivity.class);
                        intent.putExtra(TRACK_BOOKING_VENDOR_LATITUDE, vendor_latitude);
                        Log.e("tracking_btn", "1->" + vendor_latitude);
                        intent.putExtra(TRACK_BOOKING_VENDOR_LONGITUDE, vendor_longitude);
                        Log.e("tracking_btn", "2->" + vendor_longitude);
                        intent.putExtra(TRACK_SOURCE_CUSTOMER_LATITUDE, objects.get(position).getSource_lat());


                        Log.e("tracking_btn", "3" + objects.get(position).getSource_lat());
                        intent.putExtra(TRACK_BOOKING_RIDER_ORDER_TRACKER, objects.get(position).getRider_order());
                        Log.e("tracking_btn", "4" + objects.get(position).getRider_order());
                        intent.putExtra(TRACK_BOOKING_RIDER_LATITUDE, objects.get(position).getRider_lat());
                        Log.e("tracking_btn", "5" + objects.get(position).getRider_lat());
                        intent.putExtra(TRACK_BOOKING_RIDER_LONGITUDE, objects.get(position).getRider_long());
                        Log.e("tracking_btn", "6" + objects.get(position).getRider_long());
                        intent.putExtra(TRACK_SOURCE_CUSTOMER_LONGITUDE, objects.get(position).getSource_long());
                        Log.e("tracking_btn", "7" + objects.get(position).getSource_long());
                        intent.putExtra(TRACK_DESTINATION_CUSTOMER_LATITUDE, objects.get(position).getDest_lat());
                        Log.e("tracking_btn", "8" + objects.get(position).getDest_lat());
                        intent.putExtra(TRACK_DESTINATION_CUSTOMER_LONGITUDE, objects.get(position).getDest_long());
                        Log.e("tracking_btn", "9" + objects.get(position).getDest_long());


                        intent.putExtra(TRACK_BOOKING_VENDOR_NAME, vendor_name);
                        Log.e("tracking_btn", "10" + vendor_name);
                        intent.putExtra(TRACK_ARTIST_ID, artist_id);
                        Log.e("tracking_btn", "11" + artist_id);
                        intent.putExtra(TRACK_BOOKING_VENDOR_IMAGE, objects.get(position).getArtistImage());
                        Log.e("tracking_btn", "12" + objects.get(position).getArtistImage());
                        intent.putExtra(TRACK_BOOKING_SOURCE_ADDRESS, objects.get(position).getAddress());
                        Log.e("tracking_btn", "13" + objects.get(position).getAddress());
                        intent.putExtra(TRACK_BOOKING_DESTINATION_ADDRESS, objects.get(position).getDestinationaddress());
                        Log.e("tracking_btn", "14" + objects.get(position).getDestinationaddress());
                        intent.putExtra(TRACK_BOOKING_TOTAL_AMOUNT, objects.get(position).getPrice());
                        Log.e("tracking_btn", "15" + objects.get(position).getPrice());
                        intent.putExtra(TRACK_BOOKING_PRODUCT_NAME, objects.get(position).getProduct().get(0).getProduct_name());
                        Log.e("tracking_btn", "16" + objects.get(position).getProduct().get(0).getProduct_name());
                        intent.putExtra(TRACK_BOOKING_MOBILE_NUMBER, objects.get(position).getArtistMobile());
                        Log.e("tracking_btn", "17" + objects.get(position).getArtistMobile());
                        intent.putExtra(TRACK_BOOKING_SCREEN_FLAG, GlobalUtils.MY_BOOKING_SCREEN);
                        Log.e("tracking_btn", "18" + GlobalUtils.MY_BOOKING_SCREEN);
                        intent.putExtra(TRACK_BOOKING_BOOKING_FLAG, objects.get(position).getBooking_flag());
                        Log.e("tracking_btn", "19" + objects.get(position).getBooking_flag());
                        intent.putExtra(TRACK_BOOKING_ID, objects.get(position).getId());
                        Log.e("tracking_btn", "20" + objects.get(position).getId());
                        intent.putExtra(TRACK_BOOKING_PAYMENT_TYPE, objects.get(position).getPay_type());
                        Log.e("tracking_btn", "21" + objects.get(position).getPay_type());
                        intent.putExtra(TRACK_BOOKING_DATE, objects.get(position).getBooking_date());
                        Log.e("tracking_btn", "22" + objects.get(position).getBooking_date());
                        intent.putExtra(TRACK_BOOKING_TIME, objects.get(position).getBooking_time());
                        Log.e("tracking_btn", "23" + objects.get(position).getBooking_time());
                        intent.putExtra(TRACK_VEHICLE_NUMBER, objects.get(position).getProduct().get(0).getVehicle_number());
                        Log.e("tracking_btn", "24" + objects.get(position).getProduct().get(0).getVehicle_number());
                        intent.putExtra(TRACK_CAT_ID, objects.get(position).getProduct().get(0).getCategory_id());
                        Log.e("tracking_btn", "25" + objects.get(position).getProduct().get(0).getCategory_id());
                        intent.putExtra(TRACK_SUB_ID, objects.get(position).getProduct().get(0).getSub_category_id());
                        Log.e("tracking_btn", "26" + objects.get(position).getProduct().get(0).getSub_category_id());
                        intent.putExtra(TRACK_SUB_LEVEL_ID, objects.get(position).getProduct().get(0).getSublevel_category());
                        Log.e("tracking_btn", "27" + objects.get(position).getProduct().get(0).getSublevel_category());
                        intent.putExtra(BOOKING, "customerbooking");
                        if (objects.get(position).getProduct().get(0).getCategory_id().equals("85") || objects.get(position).getProduct().get(0).getCategory_id().equals("126") || objects.get(position).getProduct().get(0).getCategory_id().equals("74")) {
                            intent.putExtra("category_type", false);
                        } else {
                            intent.putExtra("category_type", true);
                        }
                        mContext.startActivity(intent);
                    }

                }
            });

            holder.repeat_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    paramsRepeat = new HashMap<>();
                    paramsRepeat.put(Consts.BOOKING_ID, objects.get(position).getId());
                    paramsRepeat.put(Consts.USER_ID, userDTO.getUser_id());
                    paramsRepeat.put(Consts.ARTIST_ID, objects.get(position).getArtist_id());
                    Log.e("REPEAT_ORDER", "" + paramsRepeat.toString());

                    new HttpsRequest(Consts.REPEAT_ORDER_API, paramsRepeat, mContext).stringPost("Tag", new Helper() {
                        @Override
                        public void backResponse(boolean flag, String msg, JSONObject response) {
                            if (flag) {
                                Log.e("REPEAT_ORDER", "" + response.toString());

                                Intent in = new Intent(mContext, BookingProduct.class);
                                //   in.putExtra(Consts.ARTIST_DTO, artistDetailsDTO);
                                //   Log.e("Khakhra_tHIRD", "" + artistDetailsDTO.toString());
                                in.putExtra(Consts.ARTIST_ID, objects.get(position).getArtist_id());
                                //    in.putExtra(Consts.SERVICE_ARRAY, array.toString());
                                in.putExtra(Consts.SCREEN_TAG, 2);
                                in.putExtra("booking_flag", true);
                                // in.putExtra("cartHashmap", cartHashmap);
                                in.putExtra(Consts.PRICE, objects.get(position).getPrice());
                                //  in.putExtra(Consts.CHANGE_PRICE, arrayprice.toString());
                                //  in.putExtras(b);
                                mContext.startActivity(in);
                            } else {

                            }
                        }
                    });
                }
            });

            holder.llFinish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (NetworkManager.isConnectToInternet(mContext)) {
                        booking("3", position);
                    } else {
                    }
                }
            });
            holder.llPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            holder.llViewInvoice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(mContext, ViewInvoice.class);
                    in.putExtra(Consts.HISTORY_DTO, objects.get(position).getInvoice());
                    mContext.startActivity(in);
                }
            });


            if (!objects.get(position).getDestinationaddress().equalsIgnoreCase("")) {
                holder.lldestiLocation.setVisibility(View.VISIBLE);
                holder.tvdestiLocation.setText(objects.get(position).getDestinationaddress());
            } else {
                holder.lldestiLocation.setVisibility(View.GONE);
            }

            if (objects.get(position).getIsmap().equals("1")) {
                holder.llLocation.setVisibility(View.VISIBLE);
                holder.lldestiLocation.setVisibility(View.VISIBLE);
            } else {
                holder.llLocation.setVisibility(View.GONE);
                holder.lldestiLocation.setVisibility(View.GONE);
            }

        } else {
            MyViewHolderSection view = (MyViewHolderSection) holderMain;
            view.tvSection.setText(objects.get(position).getSection_name());
        }
    }

    @Override
    public int getItemCount() {

        return objects.size();
    }

    OnTouch onTouch = new OnTouch() {
        @Override
        public void removeBorder() {

        }
    };

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivArtist;
        public LinearLayout llStatus, llFinish, llPay, llViewInvoice, llapproxtime, lldestiLocation;
        public CustomTextView tvWork, tvLocation, tvDate, tvDescription, tvapproxtime, tvdestiLocation, service_txt, tvPay;
        public CustomTextViewBold order_preparation_note, repeat_order, discount_txt, discount_digit_txt, tvStatus, llCancel, llCancelDummy, tvJobComplete, tvProfileComplete, tvName, tvPrice, tvtotal, txtpaynow, txt_tracking, txt_trackingDummy, discounted_rupee_symbol2, tvPrice_f, service_digit_txt1;
        public CustomTextView discounted_rupee_symbol1;
        public RelativeLayout rlClick, llTime;
        public Chronometer chronometer;
        Counter counter123;
        public ImageView order_status, ivMap, ivStatus, ivcall;
        public RecyclerView rv_cart;
        RelativeLayout cancell_relative, repeat_relative, tracking_relative;
        public CardView cardData;
        public ImageView iviamge;
        public View view4;
        public LinearLayout llLocation;

        public MyViewHolder(View view) {
            super(view);
            tvPay = view.findViewById(R.id.tvPay);
            order_preparation_note = view.findViewById(R.id.order_preparation_note);
            ivArtist = view.findViewById(R.id.ivArtist);
            tvStatus = view.findViewById(R.id.tvStatus);
            tracking_relative = view.findViewById(R.id.tracking_relative);
            repeat_order = view.findViewById(R.id.repeat_order);
            repeat_relative = view.findViewById(R.id.repeat_relative);
            tvDescription = view.findViewById(R.id.tvDescription);
            counter123 = view.findViewById(R.id.counter123);
            llStatus = view.findViewById(R.id.llStatus);
            cancell_relative = view.findViewById(R.id.cancell_relative);
            discount_txt = view.findViewById(R.id.discount_txt);
            discount_digit_txt = view.findViewById(R.id.discount_digit_txt);
            llLocation = view.findViewById(R.id.llLocation);
            llCancel = view.findViewById(R.id.llCancel);
            llCancelDummy = view.findViewById(R.id.llCancelDummy);
            llTime = view.findViewById(R.id.llTime);
            chronometer = view.findViewById(R.id.chronometer);
            tvWork = view.findViewById(R.id.tvWork);
            tvName = view.findViewById(R.id.tvName);
            tvLocation = view.findViewById(R.id.tvLocation);
            tvJobComplete = view.findViewById(R.id.tvJobComplete);
            tvProfileComplete = view.findViewById(R.id.tvProfileComplete);
            ivMap = view.findViewById(R.id.ivMap);
            order_status = view.findViewById(R.id.order_status);
            llFinish = view.findViewById(R.id.llFinish);
            ivStatus = view.findViewById(R.id.ivStatus);
            tvPrice = view.findViewById(R.id.tvPrice);
            llPay = view.findViewById(R.id.llPay);
            llViewInvoice = view.findViewById(R.id.llViewInvoice);
            tvDate = view.findViewById(R.id.tvDate);
            rv_cart = view.findViewById(R.id.rv_cart);
            tvtotal = view.findViewById(R.id.tvtotal);
            cardData = view.findViewById(R.id.cardData);
            ivcall = view.findViewById(R.id.ivcall);
            llapproxtime = view.findViewById(R.id.llapproxtime);
            tvapproxtime = view.findViewById(R.id.tvapproxtime);
            iviamge = view.findViewById(R.id.iviamge);
            lldestiLocation = view.findViewById(R.id.lldestiLocation);
            tvdestiLocation = view.findViewById(R.id.tvdestiLocation);
            txtpaynow = view.findViewById(R.id.txtpaynow);
            txt_tracking = view.findViewById(R.id.txt_tracking);
            txt_trackingDummy = view.findViewById(R.id.txt_trackingDummy);
            view4 = view.findViewById(R.id.view4);
            discounted_rupee_symbol1 = view.findViewById(R.id.discounted_rupee_symbol1);
            discounted_rupee_symbol2 = view.findViewById(R.id.discounted_rupee_symbol2);
            service_digit_txt1 = view.findViewById(R.id.service_digit_txt1);
            service_txt = view.findViewById(R.id.service_txt);

            tvPrice_f = view.findViewById(R.id.tvPrice_f);


        }
    }

    public static class MyViewHolderSection extends RecyclerView.ViewHolder {
        public CustomTextView tvSection;

        public MyViewHolderSection(View view) {
            super(view);
            tvSection = view.findViewById(R.id.tvSection);
        }
    }


    public void decline(int pos) {
        paramsDecline = new HashMap<>();
        paramsDecline.put(Consts.USER_ID, userDTO.getUser_id());
        paramsDecline.put(Consts.BOOKING_ID, objects.get(pos).getId());
        paramsDecline.put(Consts.DECLINE_BY, "2");
        paramsDecline.put(Consts.DECLINE_REASON, "Busy");
        ProjectUtils.showProgressDialog(mContext, true, mContext.getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.DECLINE_BOOKING_API, paramsDecline, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                dialog_book.dismiss();
                if (flag) {
                    myBooking.getBooking();


                } else {
                }


            }
        });
    }

    public void booking(String req, int pos) {
        paramsBookingOp = new HashMap<>();
        paramsBookingOp.put(Consts.BOOKING_ID, objects.get(pos).getId());
        paramsBookingOp.put(Consts.REQUEST, req);
        paramsBookingOp.put(Consts.USER_ID, objects.get(pos).getUser_id());
        ProjectUtils.showProgressDialog(mContext, true, mContext.getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.BOOKING_OPERATION_API, paramsBookingOp, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    myBooking.getBooking();


                } else {
                }


            }
        });
    }

    public void completeDialog(String title, String msg, final int pos) {
        try {
            new AlertDialog.Builder(mContext)
                    .setMessage(msg)
                    .setCancelable(false)
                    .setPositiveButton(mContext.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog_book = dialog;

                            decline(pos);

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


}
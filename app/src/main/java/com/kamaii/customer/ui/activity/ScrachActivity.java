package com.kamaii.customer.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cooltechworks.views.ScratchImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.customer.DTO.ProductDTO;
import com.kamaii.customer.DTO.UserBooking;
import com.kamaii.customer.R;
import com.kamaii.customer.api.apiClient;
import com.kamaii.customer.api.apiRest;
import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.ui.adapter.AdapterProductBooking;
import com.kamaii.customer.utils.CustomTextView;
import com.kamaii.customer.utils.CustomTextViewBold;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class ScrachActivity extends AppCompatActivity {

    LinearLayout layprice;
    LinearLayout totalingdata;
    Button btnapplycode;
    TextView txtprice;
    CircleImageView artist_img;

    double pricedouble;
    ScratchImageView scratchImageView;
    String value = "";
    CustomTextView txtvendorname, etAddressSelectsource, etAddressSelectdesti, tvStatus;
    ImageView imgvendonrimage, imgback, ivStatus, imgone, imgtwo;
    CustomTextViewBold discount_txt, discount_digit_txt, txttotalamount, txtservicename, subTotal, shippingCharge, netPrice;
    TextView txtptype, txtptypemsg;
    RecyclerView rv_cart;
    String booking_type = "", saddress = "", vendor_name = "", artist_id = "", daddress = "", totalamount = "", productname = "", vendorimage = "", mobileno = "", booking = "", bflag = "", bid = "", pay_type = "";
    CustomTextViewBold artist_name;
    private GridLayoutManager gridLayoutManager;
    AdapterProductBooking adapterCartBooking;
    private ArrayList<ProductDTO> productDTOArrayList;
    boolean category_type = false;
    CardView scratch_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrach);

        subTotal = findViewById(R.id.subTotal);
        shippingCharge = findViewById(R.id.shippingCharge);
        netPrice = findViewById(R.id.tvPrice);
        totalingdata = findViewById(R.id.totalingdata);
        btnapplycode = findViewById(R.id.btnapplycode);
        layprice = findViewById(R.id.layprice);
        discount_txt = findViewById(R.id.discount_txt);
        discount_digit_txt = findViewById(R.id.discount_digit_txt);
        txtprice = findViewById(R.id.txtprice);
        txtservicename = findViewById(R.id.txtservicename);
        txtvendorname = findViewById(R.id.txtvendorname);
        imgvendonrimage = findViewById(R.id.imgvendonrimage);
        etAddressSelectsource = findViewById(R.id.etAddressSelectsource);
        etAddressSelectdesti = findViewById(R.id.etAddressSelectdesti);
        txttotalamount = findViewById(R.id.totalamount);
        imgback = findViewById(R.id.imgback);
        tvStatus = findViewById(R.id.tvStatus);
        ivStatus = findViewById(R.id.ivStatus);
        txtptype = findViewById(R.id.txtptype);
        txtptypemsg = findViewById(R.id.txtptypemsg);
        imgone = findViewById(R.id.imgone);
        imgtwo = findViewById(R.id.imgtwo);
        rv_cart = findViewById(R.id.rv_cart);
        artist_img = findViewById(R.id.artist_img);
        artist_name = findViewById(R.id.artist_name);
        scratch_add = findViewById(R.id.scratch_add);


      /*  artistid= getIntent().getStringExtra(Consts.ARTIST_ID);
        bid=  getIntent().getStringExtra(Consts.BOOKING_ID);
        price=  getIntent().getStringExtra(Consts.PRICE);*/
        category_type = getIntent().getBooleanExtra("category_type", false);
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
        booking_type = getIntent().getStringExtra("booking_type");
        pay_type = getIntent().getStringExtra("pay_type");
        DecimalFormat newFormat = new DecimalFormat("##.##");
        try {
            if (!totalamount.equalsIgnoreCase("")) {
                pricedouble = (Double.parseDouble(totalamount) * 1) / 100;

                double total = Double.parseDouble(newFormat.format(pricedouble));
                //txtprice.setText(" ₹ " + String.valueOf(total));
            }
        } catch (Exception e) {

        }

        /*if (!booking_type.equalsIgnoreCase("") || booking_type.equalsIgnoreCase("null") || booking_type.equalsIgnoreCase(null)){

            if (booking_type.equalsIgnoreCase("cab")){

                scratch_add.setVisibility(View.VISIBLE);
            }
            else {
                scratch_add.setVisibility(View.GONE);

            }

        }*/

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

        Glide.with(ScrachActivity.this).
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

        scratchImageView = findViewById(R.id.scratchImageView);


        scratchImageView.setRevealListener(new ScratchImageView.IRevealListener() {
            @Override
            public void onRevealed(ScratchImageView scratchImageView) {
                Log.e("Main", "onRevealed");
            }

            @Override
            public void onRevealPercentChangedListener(ScratchImageView siv, float percent) {
                if (percent >= 0.40) {

                    layprice.setVisibility(View.VISIBLE);
                }
            }
        });


        btnapplycode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (layprice.getVisibility() == View.VISIBLE) {

                    Intent in = new Intent(ScrachActivity.this, WriteReview.class);
                    in.putExtra(Consts.ARTIST_ID, artist_id);
                    in.putExtra(Consts.BOOKING_ID, bid);
                    startActivity(in);
                    finish();
                } else {
                    Toast.makeText(ScrachActivity.this, "Please Scratch First", Toast.LENGTH_LONG).show();
                }

            }
        });

        getBooking();
    }

    @Override
    public void onBackPressed() {

        if (value.equalsIgnoreCase("")) {
            Toast.makeText(ScrachActivity.this, "Please Give Review", Toast.LENGTH_LONG).show();
            return;
        } else {

        }

    }

    public boolean checkMapCategory(String catid) {
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


    public void getBooking() {
        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.getMyBookingDetails(String.valueOf(bid));
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                Log.e("RESSHIVAM", response.message());
                try {

                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        Log.e("dhaval123 ", "" + s);
                        JSONObject object = new JSONObject(s);
                        try {
                            ArrayList<UserBooking> userBookingList = new ArrayList<>();
                            Type getpetDTO = new TypeToken<List<UserBooking>>() {
                            }.getType();

                            userBookingList = new Gson().fromJson(object.getJSONArray("data").toString(), getpetDTO);

                            Glide.with(ScrachActivity.this).
                                    load(userBookingList.get(0).getArtistImage())
                                    .placeholder(R.drawable.dummyuser_image)
                                    .dontAnimate()
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(artist_img);

                            artist_name.setText(userBookingList.get(0).getArtistName());

                            productDTOArrayList = new ArrayList<>();
                            productDTOArrayList = userBookingList.get(0).getProduct();
                            String cating;
                            //Log.e("SHIAVAKASHI", String.valueOf(category_type));
                            if (userBookingList.get(0).getCat_typemap().equals("1")) {
                                cating = "1";
                            } else {
                                cating = "0";
                            }
                            adapterCartBooking = new AdapterProductBooking(ScrachActivity.this, productDTOArrayList, cating);
                            rv_cart.setLayoutManager(new LinearLayoutManager(ScrachActivity.this));
                            rv_cart.setAdapter(adapterCartBooking);

                            if (userBookingList.get(0).getCat_typemap().equals("1")) {
                                totalingdata.setVisibility(View.GONE);
                            } else {
                                subTotal.setText("₹" + String.valueOf(userBookingList.get(0).getSub_total()));
                                netPrice.setText("₹" + String.valueOf(userBookingList.get(0).getNetpay()));

                                txtprice.setText(" ₹ " + String.valueOf(userBookingList.get(0).getScratch_amount()));

                                discount_txt.setText(Html.fromHtml(userBookingList.get(0).getDiscount_txt()));
                                discount_digit_txt.setText(Html.fromHtml("₹" + userBookingList.get(0).getDiscount_digit_txt()));

                                if (String.valueOf(userBookingList.get(0).getShipping_charges()).equals("0.0") || String.valueOf(userBookingList.get(0).getShipping_charges()).equals("0")) {
                                    shippingCharge.setText("Free");
                                } else {
                                    shippingCharge.setText("₹" + String.valueOf(userBookingList.get(0).getShipping_charges()));
                                }
                            }
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
}




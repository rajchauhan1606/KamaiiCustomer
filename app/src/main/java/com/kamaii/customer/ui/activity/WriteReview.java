package com.kamaii.customer.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.customer.DTO.ProductDTO;
import com.kamaii.customer.DTO.UserBooking;
import com.kamaii.customer.DTO.UserDTO;
import com.kamaii.customer.R;
import com.kamaii.customer.api.apiClient;
import com.kamaii.customer.api.apiRest;
import com.kamaii.customer.https.HttpsRequest;
import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.interfacess.Helper;
import com.kamaii.customer.network.NetworkManager;
import com.kamaii.customer.preferences.SharedPrefrence;
import com.kamaii.customer.ui.adapter.AdapterProductBooking;
import com.kamaii.customer.ui.adapter.AdapterProductRatingBooking;
import com.kamaii.customer.utils.CustomButton;
import com.kamaii.customer.utils.CustomEditText;
import com.kamaii.customer.utils.CustomTextView;
import com.kamaii.customer.utils.CustomTextViewBold;
import com.kamaii.customer.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class WriteReview extends AppCompatActivity implements View.OnClickListener {
    private String TAG = WriteReview.class.getSimpleName();
    private RatingBar rbReview;
    private CustomTextView tvCharReview;
    private EditText yourReviewET;
    private LinearLayout btnSubmit;
    private Context mContext;
    private float myrating;
    private String id = "";
    private HashMap<String, String> parms = new HashMap<>();
    private ImageView ivBack;
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    String artistid = "", bid = "";
    String reviewstatus = "", review;
    AdapterProductRatingBooking adapterProductRatingBooking;
    private ArrayList<ProductDTO> productDTOArrayList;
    CircleImageView artist_img;
    RecyclerView rv_cart;
    CustomTextViewBold service_provider_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);
        mContext = WriteReview.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        artist_img = findViewById(R.id.add_profile_pic);
        service_provider_name = findViewById(R.id.service_provider_name);
        rv_cart = findViewById(R.id.rv_cart2);
        artistid = getIntent().getStringExtra(Consts.ARTIST_ID);
        bid = getIntent().getStringExtra(Consts.BOOKING_ID);
        parms.put(Consts.USER_ID, userDTO.getUser_id());
        parms.put(Consts.ARTIST_ID, artistid);
        parms.put(Consts.BOOKING_ID, bid);
        init();

        getBooking();
    }

    public void init() {
        ivBack = (ImageView) findViewById(R.id.ivBack);
        rbReview = (RatingBar) findViewById(R.id.rbReview);
        tvCharReview = (CustomTextView) findViewById(R.id.tvCharReview);
        yourReviewET = (EditText) findViewById(R.id.yourReviewET);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(this);
        ivBack.setOnClickListener(this);

        rbReview.setRating(5);
        rbReview.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                myrating = ratingBar.getRating();
            }
        });

        yourReviewET.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                tvCharReview.setText(String.valueOf(s.length()) + "/200");

            }
        });

    }

    public void submit() {

        sendReview();

    }


    /*
     *
     * method  onclick()  is handling  the button event
     *
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit:
                if (NetworkManager.isConnectToInternet(mContext)) {
                    submit();
                } else {
                    ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
                }

                break;
            case R.id.ivBack:
                onBackPressed();
                break;
        }
    }
    /*
     *
     * method  sendReview()  send reviews of users  to server
     *
     */

    public void sendReview() {
        review = yourReviewET.getText().toString();
        myrating = rbReview.getRating();
        parms.put(Consts.RATING, String.valueOf(myrating));
        parms.put(Consts.COMMENT, review);
        new HttpsRequest(Consts.ADD_RATING_API, parms, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    Intent in = new Intent(WriteReview.this, BaseActivity.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(in);
                    finish();
                } else {
                    ProjectUtils.showLong(mContext, msg);
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (reviewstatus.equalsIgnoreCase("")) {
            Toast.makeText(WriteReview.this, "Please Give Review", Toast.LENGTH_LONG).show();
            return;
        } else {
            Intent in = new Intent(WriteReview.this, BaseActivity.class);
            in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(in);
            finish();
        }
    }

    public void getBooking() {
        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.getMyBookingDetails(String.valueOf(bid));
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("RES dhaval 8238 ", response.message());
                try {

                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        Log.e("dhaval review ", "" + s);
                        JSONObject object = new JSONObject(s);
                        try {
                            ArrayList<UserBooking> userBookingList = new ArrayList<>();
                            Type getpetDTO = new TypeToken<List<UserBooking>>() {
                            }.getType();

                            userBookingList = new Gson().fromJson(object.getJSONArray("data").toString(), getpetDTO);

                            Glide.with(WriteReview.this).
                                    load(userBookingList.get(0).getArtistImage())
                                    .placeholder(R.drawable.dummyuser_image)
                                    .dontAnimate()
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(artist_img);
                            service_provider_name.setText(userBookingList.get(0).getArtistName());
                            productDTOArrayList = new ArrayList<>();
                            productDTOArrayList = userBookingList.get(0).getProduct();

                            Log.d(TAG, "onResponse: webknight " + productDTOArrayList);
                            adapterProductRatingBooking = new AdapterProductRatingBooking(WriteReview.this, productDTOArrayList);
                            rv_cart.setLayoutManager(new LinearLayoutManager(WriteReview.this));
                            rv_cart.setAdapter(adapterProductRatingBooking);


                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(mContext, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, e.toString(), Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }
}

package com.kamaii.customer.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.kamaii.customer.DTO.AllAtristListDTO;
import com.kamaii.customer.DTO.ArtistDetailsDTO;
import com.kamaii.customer.DTO.ProductDTO;
import com.kamaii.customer.DTO.UserBooking;
import com.kamaii.customer.DTO.UserDTO;
import com.kamaii.customer.R;
import com.kamaii.customer.api.apiClient;
import com.kamaii.customer.api.apiRest;
import com.kamaii.customer.https.HttpsRequest;
import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.interfacess.Helper;
import com.kamaii.customer.interfacess.OnSelectedItemListener;
import com.kamaii.customer.interfacess.ServiceClickListener;
import com.kamaii.customer.interfacess.SetonItemPlusListener;
import com.kamaii.customer.network.NetworkManager;
import com.kamaii.customer.preferences.SharedPrefrence;
import com.kamaii.customer.ui.adapter.AdapterServices;
import com.kamaii.customer.ui.adapter.SubCateProviderAdapter;
import com.kamaii.customer.ui.adapter.SubCateSuggestionAdapter;
import com.kamaii.customer.ui.models.CartModel;
import com.kamaii.customer.ui.models.SubCateModel;
import com.kamaii.customer.utils.CustomEditText;
import com.kamaii.customer.utils.CustomTextView;
import com.kamaii.customer.utils.CustomTextViewBold;
import com.kamaii.customer.utils.ExpandableHeightGridView;
import com.kamaii.customer.utils.ItemDecorationAlbumColumns;
import com.kamaii.customer.utils.ProjectUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ViewServices extends AppCompatActivity implements View.OnClickListener {
    Handler handler = new Handler();
    Runnable runnable;
    private Context mContext;
    private ArrayList<SubCateModel> subcategoryDTOList;
    ArtistDetailsDTO artistDetailsDTO;
//    private ExpandableHeightGridView rvGallery;
    private RecyclerView rvGallery;
    private RecyclerView rvSuggestion;
    private AdapterServices adapterServices;
    private ArrayList<ProductDTO> productDTOList;
    private ArrayList<ProductDTO> productDTOListtemp;
    private GridLayoutManager gridLayoutManager;
    public CustomTextViewBold more_items_txt_title, tvNotFound, item_count_txt, total_product, tvHeader, tvPrice, tvPriceType, tvBookNow;
    private JsonArray array, arrayprice;
    private CardView cardBook;
    private DialogInterface dialog_book;
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private String artist_id = "";
    private HashMap<String, String> parms = new HashMap<>();
    private HashMap<String, String> parmsthird = new HashMap<>();
    private String TAG = ViewServices.class.getSimpleName();
    LinearLayout laychat;
    public List<ProductDTO> cartList;
    HashMap<String, String> cartHashmap;
    CustomEditText svSearch;
    private Dialog dialogpriview;
    CustomTextViewBold item_count_digit, layaprofile, tvNoPass, tvYesPass, llltxtamount;
    RelativeLayout layloader;
    String artist_name = "";
    String subcategoryid = "";
    int total_amount = 0;
    private ArrayList<UserBooking> userBookingList;
    SubCateSuggestionAdapter suggestionAdapter;
    String searchtext = "";
    private HashMap<String, String> paramsBookingOp3 = new HashMap<>();
    private HashMap<String, String> paramsBookingOp4 = new HashMap<>();
    private Timer timer;
    String changes_price = "";
    private int screen_tag = 0;
    public static ArrayList<ProductDTO> cartarraylist = new ArrayList<>();
    LinearLayout suggestionLying;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_services);
        mContext = ViewServices.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        item_count_digit = findViewById(R.id.item_count_digit);
        suggestionLying = findViewById(R.id.suggestionLying);

        total_product = findViewById(R.id.total_product);
        item_count_txt = findViewById(R.id.item_count_txt);
        more_items_txt_title = findViewById(R.id.more_items_txt_title);

        cartList = new ArrayList<>();
        cartHashmap = new HashMap<>();
        if (getIntent().hasExtra(Consts.ARTIST_ID)) {

            artist_id = getIntent().getStringExtra(Consts.ARTIST_ID);
            Log.e("view_service_tracker"," 1:-- "+artist_id);
            artist_name = getIntent().getStringExtra("artist_name");
            subcategoryid = getIntent().getStringExtra(Consts.SUB_CATEGORY_ID);
            searchtext = getIntent().getStringExtra("searchtext");
        } else {
            Log.e("view_service_tracker"," 2 ");

            Intent intent = new Intent(this, BaseActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

        if (getIntent().hasExtra(Consts.ARTIST_DTO)) {
            Log.e("view_service_tracker"," 3 ");

            artistDetailsDTO = (ArtistDetailsDTO) getIntent().getSerializableExtra(Consts.ARTIST_DTO);
            artist_id = getIntent().getStringExtra(Consts.ARTIST_ID);
            changes_price = getIntent().getStringExtra(Consts.CHANGE_PRICE);
            cartHashmap = (HashMap<String, String>) getIntent().getSerializableExtra("cartHashmap");
            screen_tag = getIntent().getIntExtra(Consts.SCREEN_TAG, 0);
            if (screen_tag == 2) {
                Log.e("view_service_tracker"," 4 ");

                Bundle b = getIntent().getExtras();
                cartarraylist = (ArrayList<ProductDTO>) b.getSerializable(Consts.CARTDATA);
            }
        }

        parms.put(Consts.SUB_CATEGORY_ID, DiscoverActivity.sub_category_idd);
        parms.put(Consts.ARTIST_ID, artist_id);
        parms.put(Consts.USER_ID, userDTO.getUser_id());
        parmsthird.put(Consts.SUB_CATEGORY_ID, DiscoverActivity.sub_category_idd);
        parmsthird.put(Consts.ARTIST_ID, artist_id);
        parmsthird.put(Consts.USER_ID, userDTO.getUser_id());
        parmsthird.put(Consts.THIRACATEGORY, DiscoverActivity.thirdcategory);
        svSearch = findViewById(R.id.svSearch);
        showUiAction();
    }

    public void showSuggestionData() {
        Log.e("view_service_tracker"," 5 ");


        subcategoryDTOList = new ArrayList<>();
        subcategoryDTOList = artistDetailsDTO.getSubcategory();
        if (subcategoryDTOList.size() > 0) {

//            more_items_txt_title.setText(R.string.more_items_txt1);
            more_items_txt_title.setText("Add below items from " + artistDetailsDTO.getName());

            suggestionLying.setVisibility(View.VISIBLE);
            rvSuggestion.setLayoutManager(new GridLayoutManager(this, 1, RecyclerView.HORIZONTAL, false));

            suggestionAdapter = new SubCateSuggestionAdapter(ViewServices.this, subcategoryDTOList, onItemListener, artist_id, artist_name);
            rvSuggestion.setAdapter(suggestionAdapter);
        } else {
            suggestionLying.setVisibility(View.GONE);
        }
    }

    public void showUiAction() {
        tvPrice = findViewById(R.id.tvservicePrice);
        tvHeader = (CustomTextViewBold) findViewById(R.id.tvHeader);
        tvPriceType = (CustomTextViewBold) findViewById(R.id.tvPriceType);
        tvNotFound = (CustomTextViewBold) findViewById(R.id.tvNotFound);
        rvGallery = findViewById(R.id.rvGallery);
        rvSuggestion = findViewById(R.id.rvSuggestion);

        cardBook = (CardView) findViewById(R.id.cardBook);
        laychat = findViewById(R.id.laychat);
        layaprofile = findViewById(R.id.layaprofile);
        tvBookNow = findViewById(R.id.tvBookNow);
        layloader = findViewById(R.id.layloader);

        findViewById(R.id.llBack).setOnClickListener(this);
        cardBook.setOnClickListener(this);
        tvHeader.setText(artist_name);
        svSearch.setText(searchtext);


        svSearch.addTextChangedListener(new addListenerOnTextChange(this, svSearch));

        laychat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkManager.isConnectToInternet(mContext)) {
                    /*Intent in = new Intent(ViewServices.this, OneTwoOneChat.class);
                    in.putExtra(Consts.ARTIST_ID, artistDetailsDTO.getUser_id());
                    in.putExtra(Consts.ARTIST_NAME, artistDetailsDTO.getName());
                    in.putExtra(Consts.ARTIST_IMAGE, artistDetailsDTO.getImage());
                    mContext.startActivity(in);*/

                    Log.e("MOBILE_NO", "" + artistDetailsDTO.getDefualt_whatsappno());
                    String phoneNumberWithCountryCode = "+91" + artistDetailsDTO.getDefualt_whatsappno();

                    Log.e("JOINING_DATE", "" + userDTO.getCreated_at());
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse(
                                    String.format("https://api.whatsapp.com/send?phone=%s&text=%s",
                                            phoneNumberWithCountryCode, ""))));
                } else {
                }
            }
        });

        layaprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkManager.isConnectToInternet(mContext)) {
                    updateList();
                    //   if (array.size() > 0) {
                    Log.e("Khakhra_tHIRD", "intent");
                    Bundle b = new Bundle();
                    b.putSerializable(Consts.CARTDATA, (Serializable) productDTOListtemp);
                    Intent in = new Intent(mContext, ArtistProfile.class);
                    in.putExtra(Consts.ARTIST_DTO, artistDetailsDTO);
                    in.putExtra(Consts.ARTIST_ID, artist_id);
                    in.putExtra(Consts.SCREEN_TAG, 2);
                    in.putExtra(Consts.SUB_CATEGORY_ID, subcategoryid);
                    in.putExtra("cartHashmap", cartHashmap);
                    in.putExtra(Consts.CHANGE_PRICE, arrayprice.toString());
                    in.putExtras(b);
                    startActivity(in);

                } else {
                }
            }
        });

        dialogpriview = new Dialog(ViewServices.this, R.style.Theme_Dialog);
        dialogpriview.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogpriview.setContentView(R.layout.dailog_minium);
        dialogpriview.setCancelable(false);
        tvNoPass = dialogpriview.findViewById(R.id.tvNoPass);
        tvYesPass = dialogpriview.findViewById(R.id.tvYesPass);
        llltxtamount = dialogpriview.findViewById(R.id.llltxtamount);
        tvNoPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogpriview.dismiss();
            }
        });
    }

    public void suggestionsecond(String subcat) {

        Log.e("view_service_tracker"," 6 ");
        Log.e("view_service_tracker"," suggestionsecond_method "+artist_id);

        updateList();
        for (ProductDTO productDTO : cartarraylist) {
            if (!productDTOListtemp.contains(productDTO))
                productDTOListtemp.add(productDTO);
        }
        Bundle b = new Bundle();
        b.putSerializable(Consts.CARTDATA, (Serializable) productDTOListtemp);
        Intent in = new Intent(mContext, ViewServices.class);
        in.putExtra(Consts.ARTIST_DTO, artistDetailsDTO);
        in.putExtra(Consts.ARTIST_ID, artist_id);
        in.putExtra(Consts.SCREEN_TAG, 2);
        in.putExtra("cartHashmap", cartHashmap);
        in.putExtra(Consts.CHANGE_PRICE, arrayprice.toString());
        in.putExtras(b);
        in.putExtra("artist_name", artist_name);
        in.putExtra(Consts.SUB_CATEGORY_ID, subcat);

        startActivity(in);
    }

    public class addListenerOnTextChange implements TextWatcher {
        private Context mContext;
        CustomEditText mEdittextview;

        public addListenerOnTextChange(Context context, CustomEditText edittextview) {
            super();
            this.mContext = context;
            this.mEdittextview = edittextview;
        }

        @Override
        public void afterTextChanged(Editable s) {
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count,
                                      int after) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            if (charSequence.length() > 0) {
                adapterServices.filter(charSequence.toString());

            } else {


            }
        }
    }

    private OnSelectedItemListener onItemListener = new OnSelectedItemListener() {
        @Override
        public void setOnClick(String selectionString, int position) {

            productDTOList.remove(position);
            adapterServices.notifyDataSetChanged();
            paramsBookingOp3.clear();
            for (
                    int i = 0; i < productDTOList.size(); i++) {

                paramsBookingOp3.put("ARTIST_ID" + i, productDTOList.get(i).getUser_id());
                paramsBookingOp3.put("PRICE" + i, productDTOList.get(i).getPrice());
                paramsBookingOp3.put("SERVICE_ID" + i, productDTOList.get(i).getId());
                paramsBookingOp3.put("QUANTITYDAYS" + i, "1");
                Log.e("SERVICE_ID", "" + productDTOList.get(i).getId());
            }

            new HttpsRequest(Consts.ADD_TO_CART_TEST_API, paramsBookingOp3, ViewServices.this).stringPost(TAG, new Helper() {
                @Override
                public void backResponse(boolean flag, String msg, JSONObject response) {

                    if (flag) {
                        try {


                            userBookingList = new ArrayList<>();
                            Type getpetDTO = new TypeToken<List<CartModel>>() {
                            }.getType();
                            JSONArray jsonArray = response.getJSONArray("data");
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String SHIP_charge = jsonObject.getString("shipping_charges");
                            String SUB_TOTAL = jsonObject.getString("subtotal");
                            String TOTAL = jsonObject.getString("total_amount");
                            String item_count = jsonObject.getString("item_count");

                            String track = jsonObject.getString("tracker");


                            if (track.equals("1")) {

                            }
                            ((ViewServices) ViewServices.this).changetotalprice(TOTAL, SUB_TOTAL, SHIP_charge, item_count);

                            Log.e("SERVICE_SHIP_RES", "" + response.toString());

                            Log.e("SUB_TOTAL", "" + SUB_TOTAL);
                            Log.d("userBookingList_TOTAL", "" + jsonArray.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        Log.d(TAG, "backResponse: " + msg);
                    }
                }
            });
        }

    };


    private SetonItemPlusListener setonItemPlusListener = new SetonItemPlusListener() {
        @Override
        public void Click(int position, double total, int servicecharge, int servicepostition, int quantity) {

            if (position == 0) {
                double previoustotal = Double.parseDouble(tvPrice.getText().toString());
                double maintotal = previoustotal - total;
                tvPrice.setText(Html.fromHtml("&#8377;" + String.valueOf(maintotal)));
            } else {

                double previoustotal = Double.parseDouble(tvPrice.getText().toString());
                double maintotal = previoustotal + total;
                tvPrice.setText(Html.fromHtml("&#8377;" + String.valueOf(maintotal)));
            }
        }
    };

    @Override
    protected void onResume() {

        if (NetworkManager.isConnectToInternet(mContext)) {
            getArtist();

        } else {
        }
        super.onResume();
    }

    public void getArtist() {

        if (DiscoverActivity.thirdcategory.equalsIgnoreCase(null) || DiscoverActivity.thirdcategory.equalsIgnoreCase("0")) {

            Log.e("SUB_CAT123456", "" + subcategoryid);
            Log.e("SUB_CAT123456", "" + artist_id);
            Log.e("SUB_CAT123456", "" + userDTO.getUser_id());

            Retrofit retrofit = apiClient.getClient();
            apiRest api = retrofit.create(apiRest.class);
            Call<ResponseBody> callone = api.getArtistbyid(subcategoryid, artist_id, userDTO.getUser_id());
            callone.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        if (response.isSuccessful()) {
                            ResponseBody responseBody = response.body();

                            String s = responseBody.string();
                            Log.e("Khakhra_tHIRD", s);
                            JSONObject object = new JSONObject(s);


                            try {
                                artistDetailsDTO = new Gson().fromJson(object.getJSONObject("data").toString(), ArtistDetailsDTO.class);
                                showData();

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
        } else {

            Retrofit retrofit = apiClient.getClient();
            apiRest api = retrofit.create(apiRest.class);
            Call<ResponseBody> callone = api.getArtistbyid_third(DiscoverActivity.sub_category_idd, artist_id, userDTO.getUser_id(), DiscoverActivity.thirdcategory);
            callone.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.e("RES", response.message());
                    try {
                        if (response.isSuccessful()) {
                            ResponseBody responseBody = response.body();

                            String s = responseBody.string();
                            Log.e("tHIRD", "" + s);
                            JSONObject object = new JSONObject(s);
                            try {
                                artistDetailsDTO = new Gson().fromJson(object.getJSONObject("data").toString(), ArtistDetailsDTO.class);
                                showData();

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

    public void showData() {
        gridLayoutManager = new GridLayoutManager(mContext, 1);
        productDTOList = new ArrayList<>();
        productDTOList = artistDetailsDTO.getProducts();
        if (productDTOList.size() > 0) {

            if (productDTOList.size() == 1) {
                total_product.setText(productDTOList.size() + " item");

            } else {
                total_product.setText(productDTOList.size() + " items");
            }
            tvNotFound.setVisibility(View.GONE);
            rvGallery.setVisibility(View.VISIBLE);
            rvSuggestion.setVisibility(View.VISIBLE);

            adapterServices = new AdapterServices(ViewServices.this, productDTOList, artistDetailsDTO, setonItemPlusListener);
            rvGallery.setLayoutManager(new LinearLayoutManager(this));
            //rvGallery.getRecycledViewPool().setMaxRecycledViews(R.layout.adapter_services,0);
            rvGallery.setAdapter(adapterServices);
           // rvGallery.setExpanded(true);
            changetotalprice(artistDetailsDTO.getTotal_amount(), artistDetailsDTO.getSubtotal(), artistDetailsDTO.getShipping_charges(), artistDetailsDTO.getItem_count());

            if (SubCategoryActivity.categoryid.equalsIgnoreCase("1")) {//Loan
                tvBookNow.setText("Apply Now ");
            } else if (SubCategoryActivity.categoryid.equalsIgnoreCase("2")) { //Credit Card
                tvBookNow.setText("Apply Now ");
            } else if (SubCategoryActivity.categoryid.equalsIgnoreCase("82")) { //Grocery-Kirana
                tvBookNow.setText("Continue ");
            } else if (SubCategoryActivity.categoryid.equalsIgnoreCase("65")) { //Insaunence
                tvBookNow.setText("Buy Now ");
            } else if (SubCategoryActivity.categoryid.equalsIgnoreCase("79")) { //Furniture
                tvBookNow.setText("Continue ");
            } else if (SubCategoryActivity.categoryid.equalsIgnoreCase("87")) { // Mobile
                tvBookNow.setText("Continue ");
            } else if (SubCategoryActivity.categoryid.equalsIgnoreCase("95")) { //Tailor
                tvBookNow.setText("Continue ");
            } else if (SubCategoryActivity.categoryid.equalsIgnoreCase("67")) { //Job
                tvBookNow.setText("Apply Now ");
            } else if (SubCategoryActivity.categoryid.equalsIgnoreCase("91")) { //Dry-Snack
                tvBookNow.setText("Continue ");
            } else if (SubCategoryActivity.categoryid.equalsIgnoreCase("85")) { //Cab
                tvBookNow.setText("Book Now ");
            } else {
                tvBookNow.setText("Continue ");
            }
        } else {
            tvNotFound.setVisibility(View.VISIBLE);
            rvGallery.setVisibility(View.GONE);
            rvSuggestion.setVisibility(View.GONE);
        }
        showSuggestionData();

    }

    public void updateList() {
        Log.e("Khakhra_tHIRD", "updateList");
        array = new JsonArray();
        arrayprice = new JsonArray();
        productDTOListtemp = new ArrayList<>();
        for (int i = 0; i < productDTOList.size(); i++) {
            if (productDTOList.get(i).isSelected()) {
                productDTOList.get(i).setIsdisplay(false);
                array.add(productDTOList.get(i).getId());
                productDTOListtemp.add(productDTOList.get(i));
                arrayprice.add(productDTOList.get(i).getPrice());
            }
        }
    }

    public void sickbar(String msg) {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.RRsncbarr), msg, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(mContext.getResources().getColor(R.color.pink_text_color));
        snackbar.show();
    }

    public void changetotalprice(String texting, String prd_id, String quentity, String item_count) {
        if (item_count.equals("0")) {
            findViewById(R.id.recycle_relative).setPadding(0, 0, 0, 0);
            findViewById(R.id.lll).setVisibility(View.GONE);
        } else {
//            cartHashmap.put(prd_id, quentity);
            /*if (cartHashmap.size() == 0) {
                findViewById(R.id.lll).setVisibility(View.VISIBLE);
            } else {
                findViewById(R.id.lll).setVisibility(View.VISIBLE);
            }*/
            findViewById(R.id.recycle_relative).setPadding(0, 0, 0, 0);
            findViewById(R.id.lll).setVisibility(View.VISIBLE);

            item_count_digit.setText(item_count);
            if (Integer.parseInt(item_count) == 1) {
                item_count_txt.setText(" Item | ");
            } else {
                item_count_txt.setText(" Items | ");
            }
            Log.e(TAG, "changetotalprice: " + cartHashmap);
            tvPrice.setText(Html.fromHtml("&#8377;" + prd_id));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llBack:
                onBackPressed();
                break;
            case R.id.cardBook:
                paramsBookingOp4.put(Consts.USER_ID, userDTO.getUser_id());
//                bookingredirection();
                check_minimum_price();

                break;
        }
    }

    public void check_minimum_price() {
        new HttpsRequest(Consts.CHECK_MINIMUM_PRICE, paramsBookingOp4, ViewServices.this).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                Log.e("SHIVLO", flag + "->" + msg);
                //1=show msg, 0=cart
                if (flag) {
                    try {
                        bookingredirection();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    sickbar(msg);
                    Log.d(TAG, "backResponse: " + msg);
                }
            }
        });
    }

    public void bookingredirection() {
        updateList();

        ViewAddressActivity.sub_category_idd = subcategoryid;

        Bundle b = new Bundle();
        for (ProductDTO productDTO : artistDetailsDTO.getProducts()) {
            Log.e("LODULALIT5", "d " + productDTO);
            if (!productDTOListtemp.contains(productDTO))
                productDTOListtemp.add(productDTO);
        }

        b.putSerializable(Consts.CARTDATA, (Serializable) productDTOListtemp);

//        Toast.makeText(mContext, ""+artistDetailsDTO, Toast.LENGTH_SHORT).show();
        Intent in = new Intent(mContext, BookingProduct.class);
        in.putExtra(Consts.ARTIST_DTO, artistDetailsDTO);
        Log.e("Khakhra_tHIRD", "" + artistDetailsDTO.toString());
        in.putExtra(Consts.ARTIST_ID, artist_id);
        in.putExtra(Consts.SERVICE_ARRAY, array.toString());
        in.putExtra(Consts.SCREEN_TAG, 2);
        in.putExtra("cartHashmap", cartHashmap);
        in.putExtra(Consts.PRICE, tvPrice.getText().toString().trim());
        in.putExtra(Consts.CHANGE_PRICE, arrayprice.toString());
        in.putExtras(b);
        startActivity(in);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}

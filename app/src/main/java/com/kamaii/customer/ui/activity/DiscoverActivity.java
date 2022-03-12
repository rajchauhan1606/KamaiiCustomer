package com.kamaii.customer.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.customer.DTO.AllAtristListDTO;
import com.kamaii.customer.DTO.UserDTO;
import com.kamaii.customer.R;
import com.kamaii.customer.api.apiClient;
import com.kamaii.customer.api.apiRest;
import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.network.NetworkManager;
import com.kamaii.customer.preferences.SharedPrefrence;
import com.kamaii.customer.ui.adapter.DiscoverAdapter;
import com.kamaii.customer.utils.CustomEditText;
import com.kamaii.customer.utils.CustomTextView;
import com.kamaii.customer.utils.CustomTextViewBold;
import com.kamaii.customer.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class DiscoverActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private String TAG = DiscoverActivity.class.getSimpleName();
    private RecyclerView rvDiscover;
    private DiscoverAdapter discoverAdapter;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<AllAtristListDTO> allAtristListDTOList = new ArrayList<>();
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private LayoutInflater myInflater;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CustomTextViewBold tvNotFound;
    private EditText etSearchFilter;
    public ImageView ivFilter, img_back;
    public CustomTextViewBold headerNameTV;
    public static String sub_category_idd = "";
    public static String catid = "";
    RelativeLayout layloader;
    public static String thirdcategory = "0";
    boolean flag = false;
    public CustomTextViewBold total_product;
    CustomEditText svSearch;
    boolean load = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_discover);

        flag = getIntent().getBooleanExtra("flag", false);

        if (flag) {
            thirdcategory = getIntent().getStringExtra("third_leval_tracker");
        }
    }

    private void initView() {
        prefrence = SharedPrefrence.getInstance(this);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);

        myInflater = LayoutInflater.from(this);
        sub_category_idd = getIntent().getStringExtra(Consts.SUB_CATEGORY_ID);
        catid = getIntent().getStringExtra(Consts.CATEGORY_ID);

        if (getIntent().getStringExtra(Consts.THIRACATEGORY) != null) {
            thirdcategory = getIntent().getStringExtra(Consts.THIRACATEGORY);
        }

        setUiAction();
        getArtist();
    }

    public void setUiAction() {
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        tvNotFound = findViewById(R.id.tvNotFound);
        etSearchFilter = findViewById(R.id.etSearchFilter);
        img_back = findViewById(R.id.img_back);
        total_product = findViewById(R.id.total_product);
        svSearch = findViewById(R.id.svSearch);
        headerNameTV = findViewById(R.id.NameTV);
        layloader = findViewById(R.id.layloader);
        ivFilter = findViewById(R.id.ivfilter);
        rvDiscover = findViewById(R.id.rvDiscover);
        mLayoutManager = new LinearLayoutManager(DiscoverActivity.this);
        swipeRefreshLayout.setOnRefreshListener(this);
        rvDiscover.setLayoutManager(mLayoutManager);
        tvNotFound.setVisibility(View.GONE);
        headerNameTV.setText(getIntent().getStringExtra(Consts.Sub_CAT_NAME));

        discoverAdapter = new DiscoverAdapter(this, allAtristListDTOList, myInflater, total_product);
        rvDiscover.setAdapter(discoverAdapter);
        etSearchFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                discoverAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ivFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        sub_category_idd = "";
        catid = "";
        thirdcategory = "0";
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!load) {

            initView();

            swipeRefreshLayout.setRefreshing(false);
            swipeRefreshLayout.post(new Runnable() {
                                        @Override
                                        public void run() {


                                            if (NetworkManager.isConnectToInternet(DiscoverActivity.this)) {
                                                swipeRefreshLayout.setRefreshing(true);

                                                getArtist();

                                            } else {
                                                ProjectUtils.showToast(DiscoverActivity.this, getResources().getString(R.string.internet_concation));
                                            }
                                        }
                                    }
            );
            load = true;
        }
    }


    public void getArtist() {
        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);

        Log.e("API_PARAMS", " catid " + catid);
        Log.e("API_PARAMS", " sub_category_idd " + catid);
        Log.e("API_PARAMS", " thirdcategory " + catid);
        Log.e("API_PARAMS", "" + userDTO.getUser_id());
        Log.e("API_PARAMS", " lat " + prefrence.getValue(Consts.LATITUDE));
        Log.e("API_PARAMS", " long " + prefrence.getValue(Consts.LONGITUDE));


        Call<ResponseBody> callone = api.getArtist(catid, sub_category_idd, thirdcategory, userDTO.getUser_id(), prefrence.getValue(Consts.LATITUDE), prefrence.getValue(Consts.LONGITUDE));
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                swipeRefreshLayout.setRefreshing(false);

                // Log.e("RES_DISCOVER", response.message());
                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        Log.e("API_PARAMS_RESPONSE", s);

                        JSONObject object = new JSONObject(s);

                        try {

                            allAtristListDTOList = new ArrayList<>();
                            Type getpetDTO = new TypeToken<List<AllAtristListDTO>>() {
                            }.getType();
                            allAtristListDTOList = new Gson().fromJson(object.getJSONArray("data").toString(), getpetDTO);
                            showData();

                        } catch (Exception e) {

                            tvNotFound.setVisibility(View.VISIBLE);
                            rvDiscover.setVisibility(View.GONE);
                            layloader.setVisibility(View.GONE);

                            e.printStackTrace();
                        }

                    } else {
                        tvNotFound.setVisibility(View.VISIBLE);
                        rvDiscover.setVisibility(View.GONE);
                        layloader.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                swipeRefreshLayout.setRefreshing(false);
                tvNotFound.setVisibility(View.VISIBLE);
                rvDiscover.setVisibility(View.GONE);
                layloader.setVisibility(View.GONE);

            }
        });

    }


    public void showData() {
        tvNotFound.setVisibility(View.GONE);
        rvDiscover.setVisibility(View.VISIBLE);
        if (allAtristListDTOList.size() == 1) {

            total_product.setText(allAtristListDTOList.size() + " Partner Near by");
        } else {
            total_product.setText(allAtristListDTOList.size() + " Partners Near by");

        }
        svSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    discoverAdapter.filter(charSequence.toString());

                } else {


                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        discoverAdapter = new DiscoverAdapter(DiscoverActivity.this, allAtristListDTOList, myInflater, total_product);
        rvDiscover.setAdapter(discoverAdapter);
    }

    @Override
    public void onRefresh() {
        Log.e("ONREFREST_Firls", "FIRS");

        getArtist();
    }


}

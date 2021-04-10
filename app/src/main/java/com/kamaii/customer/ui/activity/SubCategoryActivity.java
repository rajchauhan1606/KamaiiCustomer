package com.kamaii.customer.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.afollestad.viewpagerdots.DotsIndicator;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.kamaii.customer.DTO.UserDTO;
import com.kamaii.customer.R;
import com.kamaii.customer.api.apiClient;
import com.kamaii.customer.api.apiRest;
import com.kamaii.customer.https.HttpsRequest;
import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.interfacess.Helper;
import com.kamaii.customer.interfacess.OnDataErrorListener;
import com.kamaii.customer.interfacess.OnSelectedItemListener;
import com.kamaii.customer.network.NetworkManager;
import com.kamaii.customer.preferences.SharedPrefrence;
import com.kamaii.customer.ui.adapter.SliderAdapter;
import com.kamaii.customer.ui.adapter.SubCateAdapter;
import com.kamaii.customer.ui.adapter.SubcatSliderAdapter;
import com.kamaii.customer.ui.fragment.CheckInternetFragment;
import com.kamaii.customer.ui.models.SiderModel;
import com.kamaii.customer.ui.models.SubCateModel;
import com.kamaii.customer.ui.models.SubcatSlidermodel;
import com.kamaii.customer.utils.ItemDecorationAlbumColumns;
import com.kamaii.customer.utils.ProjectUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.widget.Toast.LENGTH_LONG;
import static com.kamaii.customer.interfacess.Consts.CATEGORY_NAME;
import static com.kamaii.customer.utils.ProjectUtils.TAG;

public class SubCategoryActivity extends AppCompatActivity {
    private View view;
    private SharedPrefrence prefrence;
    private String TAG = SubCategoryActivity.class.getSimpleName();
    private BaseActivity baseActivity;
    RecyclerView rec_category;
    SubCateAdapter subCateAdapter;
    ArrayList<SubCateModel> subCateModelArrayList = new ArrayList<>();
    private HashMap<String, String> category_id = new HashMap<>();
    private UserDTO userDTO;
    private TextView tvNo, txt_name;
    private ImageView img_back;
    private String category_idd = "";
    private EditText etSearchFilter;
    public static String categoryid = "";
    public static String categoryname = "";
    ProgressDialog progressDialog;
    ViewPager pagerhometwo;
    private SharedPreferences firebase;
    private HashMap<String, String> parmsCategory = new HashMap<>();
    ArrayList<SubcatSlidermodel> silderarraylist = new ArrayList<>();
    int currentPage = 0;
    boolean show_banner = false;
    public static String subleval = "";
    DotsIndicator dots;
    ViewPager mPager;
    List<String> sub_cat_slider = new ArrayList<>();
    RelativeLayout ivmainsearchLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);
        progressDialog = new ProgressDialog(SubCategoryActivity.this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        mPager = findViewById(R.id.pagersubcat);
        dots = findViewById(R.id.dotssubcat);
        ivmainsearchLayout = findViewById(R.id.ivmainsearchLayout);
        firebase = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        prefrence = SharedPrefrence.getInstance(this);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        parmsCategory.put(Consts.USER_ID, userDTO.getUser_id());
        parmsCategory.put(Consts.DEVICE_TOKEN, firebase.getString(Consts.DEVICE_TOKEN, ""));


        if (getIntent().getStringExtra(Consts.CATEGORY_ID) != null) {

            category_idd = getIntent().getStringExtra(Consts.CATEGORY_ID);
            categoryid = getIntent().getStringExtra(Consts.CATEGORY_ID);
            categoryname = getIntent().getStringExtra(Consts.CATEGORY_NAME);
        } else {
            Intent intent = new Intent(this, BaseActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

        category_id.put("category_id", category_idd);
        pagerhometwo = findViewById(R.id.pagerhometwo);
        setUiAction();
    }

    private class SliderTimer extends TimerTask {

        @Override
        public void run() {

            try {
                SubCategoryActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("silderarraylist", "" + silderarraylist.size());
                        if (mPager.getCurrentItem() < silderarraylist.size() - 1) {
                            mPager.setCurrentItem(mPager.getCurrentItem() + 1);
                        } else {
                            mPager.setCurrentItem(0);
                        }
                    }
                });
            } catch (Exception e) {

            }

        }
    }


    private void setUiAction() {


        tvNo = findViewById(R.id.tvNotFound);
        txt_name = findViewById(R.id.txt_name);
        img_back = findViewById(R.id.img_back);

        if (getIntent().getStringExtra(Consts.CATEGORY_NAME) != null) {

            txt_name.setText(getIntent().getStringExtra(Consts.CATEGORY_NAME));
        }

        rec_category = findViewById(R.id.rvCategory);
        etSearchFilter = findViewById(R.id.etSearchFilter);

        rec_category.setLayoutManager(new GridLayoutManager(SubCategoryActivity.this, 2));
        rec_category.addItemDecoration(new ItemDecorationAlbumColumns(
                4,
                2));
        subCateAdapter = new SubCateAdapter(this, subCateModelArrayList, onItemListener, subleval,getSupportFragmentManager());
        rec_category.setAdapter(subCateAdapter);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        etSearchFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                subCateAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        subCateAdapter.setDataErrorListener(new OnDataErrorListener() {
            @Override
            public void DataNotFound(boolean b) {
                if (b) {
                    tvNo.setVisibility(View.GONE);
                } else {
                    tvNo.setVisibility(View.GONE);
                }
            }
        });

        if (NetworkManager.isConnectToInternet(SubCategoryActivity.this)) {

            getsubCategory();

        } else {

//            Fragment fragment = new CheckInternetFragment();
//            FragmentTransaction fragmentTransaction = getApplicationContext().getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
//                    android.R.anim.fade_out);
//            fragmentTransaction.replace(R.id.frame, fragment, TAG);
//            fragmentTransaction.commitAllowingStateLoss();

        }

        ivmainsearchLayout.setVisibility(View.VISIBLE);
        ivmainsearchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SubCategoryActivity.this, SearchActivity.class));
            }
        });
    }

    private OnSelectedItemListener onItemListener = new OnSelectedItemListener() {
        @Override
        public void setOnClick(String selectionString, int position) {
        }
    };

    @Override
    public void onResume() {
        etSearchFilter.setText("");
        subCateAdapter.getFilter().filter("");

        super.onResume();

        if (!NetworkManager.isConnectToInternet(SubCategoryActivity.this)) {

            Fragment fragment = new CheckInternetFragment();
            FragmentTransaction fragmentTransaction = SubCategoryActivity.this.getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                    android.R.anim.fade_out);
            fragmentTransaction.replace(R.id.frame, fragment, TAG);
            fragmentTransaction.commitAllowingStateLoss();


        }

    }

    public void getsubCategory() {
        progressDialog.show();
        Log.v("RES " + " fuck ", " fuck ");


        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.getsubcategory(category_idd, userDTO.getUser_id());
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();

                Log.v("RES " + " fuck ", response.message());
                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        Log.e("SUBCATRESPONSE123", "" + s);
                        JSONObject object = new JSONObject(s);
                        String banner_status = object.getString("image_show");

                        if (banner_status.equalsIgnoreCase("1")) {
                            silderarraylist.clear();

                            JSONArray jsonArray = object.getJSONArray("image_path");
                            Log.e("jsonarraylistlength", "" + jsonArray.length());
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                SubcatSlidermodel siderModel = new SubcatSlidermodel();
                                siderModel.setSlider_image(jsonObject.getString("slider_image"));
                                silderarraylist.add(siderModel);
                            }
                            findViewById(R.id.subcat_banner_layout).setVisibility(View.VISIBLE);
                            mPager.setAdapter(new SubcatSliderAdapter(SubCategoryActivity.this, silderarraylist));

                            Timer timer = new Timer();
                            timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);

                        } else {
                            findViewById(R.id.subcat_banner_layout).setVisibility(View.GONE);
                        }

                        subleval = object.getString("sublevel");
                        tvNo.setVisibility(View.GONE);

                        try {
                            subCateModelArrayList = new ArrayList<>();
                            Type getpetDTO = new TypeToken<List<SubCateModel>>() {
                            }.getType();
                            subCateModelArrayList = (ArrayList<SubCateModel>) new Gson().fromJson(object.getJSONArray("data").toString(), getpetDTO);
                            Log.e("SUBCATRESPONSE", "" + subCateModelArrayList.toString());
                            Collections.sort(subCateModelArrayList, new Comparator<SubCateModel>() {
                                @Override
                                public int compare(SubCateModel lhs, SubCateModel rhs) {
                                    return lhs.getCat_name().compareTo(rhs.getCat_name());
                                }
                            });
                            subCateAdapter.notifyDataChanged(subCateModelArrayList);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(SubCategoryActivity.this, "Try again. Server is not responding.",
                                LENGTH_LONG).show();

                        tvNo.setVisibility(View.VISIBLE);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.v("RES " + " fuck ", call.toString());
                Log.v("RES " + " fuck ", t.toString());

                tvNo.setVisibility(View.VISIBLE);
            }
        });
    }

}

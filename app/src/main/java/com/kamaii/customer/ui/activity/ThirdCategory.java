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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
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
import com.kamaii.customer.ui.adapter.ThirdCateAdapter;
import com.kamaii.customer.ui.models.SiderModel;
import com.kamaii.customer.ui.models.ThirdCateModel;
import com.kamaii.customer.utils.ItemDecorationAlbumColumns;
import com.kamaii.customer.utils.ProjectUtils;

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

public class ThirdCategory extends AppCompatActivity {

    public String sub_category_idd="",subcatname="",catid="";
    RecyclerView rec_category;
    private EditText etSearchFilter;
    private TextView tvNo,txt_name;
    private ImageView img_back;
    ArrayList<ThirdCateModel> subCateModelArrayList=new ArrayList<>();
    private String TAG = ThirdCategory.class.getSimpleName();
    ThirdCateAdapter subCateAdapter;
    ProgressDialog progressDialog;
    ArrayList<SiderModel> silderarraylist = new ArrayList<>();
    ViewPager pagerhometwo;
    private UserDTO userDTO;
    private SharedPrefrence prefrence;
    private SharedPreferences firebase;
    private HashMap<String, String> parmsCategory = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_category);
        progressDialog=new ProgressDialog(ThirdCategory.this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        firebase = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        prefrence = SharedPrefrence.getInstance(this);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        parmsCategory.put(Consts.USER_ID, userDTO.getUser_id());
        parmsCategory.put(Consts.DEVICE_TOKEN, firebase.getString(Consts.DEVICE_TOKEN, ""));


        if(getIntent().getStringExtra(Consts.SUB_CATEGORY_ID)!=null){
            sub_category_idd=getIntent().getStringExtra(Consts.SUB_CATEGORY_ID);
            subcatname=getIntent().getStringExtra(Consts.Sub_CAT_NAME);
            catid=getIntent().getStringExtra(Consts.CATEGORY_ID);

        }
        else {
            Intent intent = new Intent(this, BaseActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

        pagerhometwo = findViewById(R.id.pagerhometwo);
        getslider();

        tvNo = findViewById(R.id.tvNotFound);
        txt_name = findViewById(R.id.txt_name);
        img_back = findViewById(R.id.img_back);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        rec_category=findViewById(R.id.rvCategory);
        etSearchFilter = findViewById(R.id.etSearchFilter);

        rec_category.setLayoutManager(new GridLayoutManager(ThirdCategory.this, 4));
        rec_category.addItemDecoration(new ItemDecorationAlbumColumns(
                2,
                4));

        subCateAdapter=new ThirdCateAdapter(this,subCateModelArrayList,onItemListener);
        rec_category.setAdapter(subCateAdapter);
        txt_name.setText(subcatname);

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
                if(b)
                {
                    tvNo.setVisibility(View.VISIBLE);
                }
                else {
                    tvNo.setVisibility(View.GONE);
                }
            }
        });

        if (NetworkManager.isConnectToInternet(ThirdCategory.this)) {


            getsubCategory();

        } else {
            ProjectUtils.showToast(ThirdCategory.this, getResources().getString(R.string.internet_concation));
        }

    }

    public void getslider() {

        new HttpsRequest(Consts.GET_footerSLIDER, parmsCategory,ThirdCategory.this).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {


                if (flag) {

                    try {
                        silderarraylist = new ArrayList<>();

                        Type getpetDTO = new TypeToken<List<SiderModel>>() {
                        }.getType();
                        silderarraylist = (ArrayList<SiderModel>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);

                        pagerhometwo.setAdapter(new SliderAdapter(ThirdCategory.this, silderarraylist,getSupportFragmentManager()));

                        Timer timer = new Timer();
                        timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {

                }
            }
        });
    }
    private class SliderTimer extends TimerTask {

        @Override
        public void run() {

            try {
                ThirdCategory.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {



                        if (pagerhometwo.getCurrentItem() < silderarraylist.size() - 1) {
                            pagerhometwo.setCurrentItem(pagerhometwo.getCurrentItem() + 1);
                        } else  {
                            pagerhometwo.setCurrentItem(0);
                        }
                    }
                });
            }catch (Exception e){

            }

        }
    }
    private OnSelectedItemListener onItemListener=new OnSelectedItemListener() {
        @Override
        public void setOnClick(String selectionString, int position) {


        }

    };
    @Override
    public void onResume() {
        super.onResume();


        etSearchFilter.setText("");
        subCateAdapter.getFilter().filter("");
    }

    public HashMap<String, String> getparm() {
        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.CATEGORY_ID, catid);
        parms.put(Consts.SUB_CATEGORY_ID,sub_category_idd);
        return parms;
    }
    public void getsubCategory() {
        progressDialog.show();

        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.getthirdcategory(catid,sub_category_idd);
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();

                Log.e("RES", response.message());
                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        JSONObject object = new JSONObject(s);

                        tvNo.setVisibility(View.GONE);

                        try {
                            subCateModelArrayList = new ArrayList<>();
                            Type getpetDTO = new TypeToken<List<ThirdCateModel>>() {
                            }.getType();
                            subCateModelArrayList = (ArrayList<ThirdCateModel>) new Gson().fromJson(object.getJSONArray("data").toString(), getpetDTO);

                            Collections.sort(subCateModelArrayList, new Comparator<ThirdCateModel>() {
                                @Override
                                public int compare(ThirdCateModel lhs, ThirdCateModel rhs) {
                                    return lhs.getCat_name().compareTo(rhs.getCat_name());
                                }
                            });
                            subCateAdapter.notifyDataChanged(subCateModelArrayList);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        Toast.makeText(ThirdCategory.this, "Try again. Server is not responding.",
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



                tvNo.setVisibility(View.VISIBLE);

            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

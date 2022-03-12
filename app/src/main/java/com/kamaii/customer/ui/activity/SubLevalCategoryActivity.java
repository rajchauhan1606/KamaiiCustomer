package com.kamaii.customer.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.customer.DTO.AllAtristListDTO;
import com.kamaii.customer.DTO.ArtistDetailsDTO;
import com.kamaii.customer.R;
import com.kamaii.customer.api.apiClient;
import com.kamaii.customer.api.apiRest;
import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.ui.adapter.SubLevalCatAdapter;
import com.kamaii.customer.ui.models.ThirdCateModel;
import com.kamaii.customer.utils.CustomTextViewBold;
import com.kamaii.customer.utils.ItemDecorationAlbumColumns;
import com.kamaii.customer.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.widget.Toast.LENGTH_LONG;
import static com.kamaii.customer.interfacess.Consts.CATEGORY_NAME;

public class SubLevalCategoryActivity extends AppCompatActivity {

    private ArrayList<AllAtristListDTO> allAtristListDTOList = new ArrayList<>();
    public static String sub_category_idd = "", subcatname = "", catid = "", mainkm = "", thridcategory = "", destitime = "";
    private ArtistDetailsDTO artistDetailsDTO;
    ProgressDialog progressDialog;
    ArrayList<ThirdCateModel> thirdCateModelArrayList = new ArrayList<>();
    SubLevalCatAdapter subLevalCatAdapter;
    RecyclerView recyclerView;
    CustomTextViewBold txt_name;
    ImageView back_img;
    TextView tvNo;
    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_leval_category);

        progressDialog = new ProgressDialog(SubLevalCategoryActivity.this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);

        init();
        if (getIntent().getStringExtra(Consts.SUB_CATEGORY_ID) != null) {
            sub_category_idd = getIntent().getStringExtra(Consts.SUB_CATEGORY_ID);
            subcatname = getIntent().getStringExtra(Consts.Sub_CAT_NAME);
            catid = getIntent().getStringExtra(Consts.CATEGORY_ID);
            txt_name.setText(subcatname);

        } else {
            Intent intent = new Intent(this, BaseActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getthirdcategory();
    }

    public void init() {

        tvNo = findViewById(R.id.tvNotFound);
        txt_name = findViewById(R.id.sublevalcat_txt_name);
        back_img = findViewById(R.id.sublevalcat_img_back);
        recyclerView = findViewById(R.id.subleval_rvCategory);
    }

    public void getthirdcategory() {
        progressDialog.show();

        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Log.e("SUBLEVAL_RESPONSE"," Params "+ " catid "+catid+" subcatid "+sub_category_idd);
        Call<ResponseBody> callone = api.getthirdcategory(catid, sub_category_idd);
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();

                Log.e("RES", response.message());
                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();

                        Log.e("SUBLEVAL_RESPONSE", "" + s);
                        JSONObject object = new JSONObject(s);


                        try {
                            thirdCateModelArrayList = new ArrayList<>();
                            Type getpetDTO = new TypeToken<List<ThirdCateModel>>() {
                            }.getType();
                            thirdCateModelArrayList = (ArrayList<ThirdCateModel>) new Gson().fromJson(object.getJSONArray("data").toString(), getpetDTO);

                            Collections.sort(thirdCateModelArrayList, new Comparator<ThirdCateModel>() {
                                @Override
                                public int compare(ThirdCateModel lhs, ThirdCateModel rhs) {
                                    return lhs.getCat_name().compareTo(rhs.getCat_name());
                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(SubLevalCategoryActivity.this, "Try again. Server is not responding.",
                                LENGTH_LONG).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                loadAdapter();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


            }
        });
    }

    public void loadAdapter() {

        if (thirdCateModelArrayList.size() == 0) {
            tvNo.setVisibility(View.VISIBLE);
        } else {
            tvNo.setVisibility(View.GONE);
        }
        subLevalCatAdapter = new SubLevalCatAdapter(SubLevalCategoryActivity.this, thirdCateModelArrayList, sub_category_idd, subcatname, catid);
        recyclerView.setLayoutManager(new GridLayoutManager(SubLevalCategoryActivity.this, 3));
        recyclerView.addItemDecoration(new ItemDecorationAlbumColumns(
                4,
                2));

        recyclerView.setAdapter(subLevalCatAdapter);
    }
}
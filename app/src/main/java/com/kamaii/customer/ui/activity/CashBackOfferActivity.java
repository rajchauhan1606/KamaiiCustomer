package com.kamaii.customer.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.customer.DTO.UserDTO;
import com.kamaii.customer.R;
import com.kamaii.customer.https.HttpsRequest;
import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.interfacess.Helper;
import com.kamaii.customer.preferences.SharedPrefrence;
import com.kamaii.customer.ui.adapter.CashBackAdapter;
import com.kamaii.customer.ui.adapter.FooterSliderAdapter;
import com.kamaii.customer.ui.fragment.CategoryFragment;
import com.kamaii.customer.ui.models.FooterSliderModel;
import com.kamaii.customer.utils.CustomTextView;
import com.kamaii.customer.utils.CustomTextViewBold;
import com.kamaii.customer.utils.ExpandableHeightGridView;
import com.kamaii.customer.utils.ProjectUtils;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;

public class CashBackOfferActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = CashBackOfferActivity.class.getSimpleName();
    ImageView back_img;
    CustomTextViewBold toolbar_title;
    RecyclerView CashbackRecycleView;
    CashBackAdapter cashBackAdapter;
    ArrayList<FooterSliderModel> footersilderarraylist = new ArrayList<>();
    private HashMap<String, String> parmsCategory = new HashMap<>();
    private UserDTO userDTO;
    private SharedPrefrence prefrence;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_back_offer);

        init();
    }

    private void init() {

        toolbar_title = findViewById(R.id.txt_name_cash);
        CashbackRecycleView = findViewById(R.id.rv_cash);

        toolbar_title.setText("Cashback & Offers");

        prefrence = SharedPrefrence.getInstance(CashBackOfferActivity.this);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);


        parmsCategory.put(Consts.USER_ID, userDTO.getUser_id());

        findViewById(R.id.img_back_cash).setOnClickListener(this);

        getfooterslider();
    }

    public void loadAdapter(){

        cashBackAdapter = new CashBackAdapter(this, footersilderarraylist);
        CashbackRecycleView.setLayoutManager(new LinearLayoutManager(this));
        CashbackRecycleView.setAdapter(cashBackAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.img_back_cash:
                onBackPressed();
                break;
        }
    }

    public void getfooterslider() {

        new HttpsRequest(Consts.GET_CASHBACK, parmsCategory, CashBackOfferActivity.this).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {


                if (flag) {

                    Log.e("cashback_res",""+response.toString());
                    try {
                        footersilderarraylist = new ArrayList<>();

                        Type getpetDTO = new TypeToken<List<FooterSliderModel>>() {
                        }.getType();
                        footersilderarraylist = (ArrayList<FooterSliderModel>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);

                        Log.e("imge",""+footersilderarraylist.get(0).getSlider_image());
                        Log.e("RES_SIZE",""+footersilderarraylist.size());
                        loadAdapter();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    ProjectUtils.showToast(CashBackOfferActivity.this, msg);
                }
            }
        });
    }
}
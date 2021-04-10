package com.kamaii.customer.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kamaii.customer.DTO.AllAtristListDTO;
import com.kamaii.customer.DTO.UserDTO;
import com.kamaii.customer.R;
import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.interfacess.OnSelectedItemListener;
import com.kamaii.customer.preferences.SharedPrefrence;
import com.kamaii.customer.ui.adapter.DailyAdapter;
import com.kamaii.customer.ui.models.DailyModel;

import java.util.ArrayList;
import java.util.HashMap;

public class DailyActivity extends AppCompatActivity {

    public String sub_category_idd="",subcatname="",catid="",thridcategory="";
    RecyclerView rec_category;
    CardView cardBooksourcedesti;
    TextView txtkm;
    DailyAdapter dailyAdapter;
    private ArrayList<DailyModel> dailyModelArrayList=new ArrayList<>();
    LinearLayout laykm,llBack;
    private SharedPrefrence prefrence;


    private UserDTO userDTO;
    HashMap<String, String> parms = new HashMap<>();
    private String TAG = DailyActivity.class.getSimpleName();
    private ArrayList<AllAtristListDTO> allAtristListDTOList = new ArrayList<>();
    public  double startLati,startLongi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_daily);

        rec_category=findViewById(R.id.rvdaily);
        sub_category_idd=getIntent().getStringExtra(Consts.SUB_CATEGORY_ID);
        subcatname=getIntent().getStringExtra(Consts.Sub_CAT_NAME);
        catid=getIntent().getStringExtra(Consts.CATEGORY_ID);
        thridcategory=getIntent().getStringExtra(Consts.THIRACATEGORY);
        prefrence = SharedPrefrence.getInstance(DailyActivity.this);
        txtkm=findViewById(R.id.txtkm);
        laykm=findViewById(R.id.laykm);
        llBack=findViewById(R.id.llBack);
        cardBooksourcedesti=findViewById(R.id.cardBooksourcedesti);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        parms.put(Consts.USER_ID, userDTO.getUser_id());
        parms.put(Consts.SUB_CATEGORY_ID, sub_category_idd);
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        dailyModelArrayList.add(new DailyModel("1 hr","10","true"));
        dailyModelArrayList.add(new DailyModel("2 hr","20","false"));
        dailyModelArrayList.add(new DailyModel("3 hr","30","false"));
        dailyModelArrayList.add(new DailyModel("4 hr","40","false"));
        dailyModelArrayList.add(new DailyModel("5 hr","50","false"));
        dailyModelArrayList.add(new DailyModel("6 hr","60","false"));
        dailyModelArrayList.add(new DailyModel("7 hr","70","false"));
        dailyModelArrayList.add(new DailyModel("8 hr","80","false"));
        dailyModelArrayList.add(new DailyModel("9 hr","90","false"));
        dailyModelArrayList.add(new DailyModel("10 hr","100","false"));
        dailyModelArrayList.add(new DailyModel("11 hr","110","false"));
        dailyModelArrayList.add(new DailyModel("12 hr","120","false"));
        rec_category.setLayoutManager(new LinearLayoutManager(DailyActivity.this, LinearLayoutManager.HORIZONTAL, false));
        dailyAdapter=new DailyAdapter(DailyActivity.this,dailyModelArrayList,onItemListener);
        rec_category.setAdapter(dailyAdapter);


        cardBooksourcedesti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 if(txtkm.getText().toString().equalsIgnoreCase("0"))
                {
                    Toast.makeText(DailyActivity.this,"Not Available", Toast.LENGTH_LONG).show();
                }
                else {
                }


            }
        });

        startLati= Double.parseDouble(prefrence.getValue(Consts.LATITUDE));
        startLongi= Double.parseDouble(prefrence.getValue(Consts.LONGITUDE));



    }

    private OnSelectedItemListener onItemListener=new OnSelectedItemListener() {
        @Override
        public void setOnClick(String selectionString, int position) {

            String kmInDec=  dailyModelArrayList.get(position).getDisttance();
            laykm.setVisibility(View.VISIBLE);
            txtkm.setText(String.valueOf(kmInDec));
        }
    };


}

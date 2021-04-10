package com.kamaii.customer.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.snackbar.Snackbar;
import com.kamaii.customer.DTO.UserDTO;
import com.kamaii.customer.R;
import com.kamaii.customer.databinding.ActivityOtpBinding;
import com.kamaii.customer.https.HttpsRequest;
import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.interfacess.Helper;
import com.kamaii.customer.preferences.SharedPrefrence;
import com.kamaii.customer.utils.CustomButton;
import com.kamaii.customer.utils.CustomEditText;
import com.kamaii.customer.utils.ProjectUtils;

import org.json.JSONObject;

import java.util.HashMap;

public class OtpTwoActivity extends AppCompatActivity {

    private Context mContext;
    private String TAG = OtpActivity.class.getSimpleName();
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private SharedPreferences firebase;
    private boolean isHide = false;
    private ActivityOtpBinding binding;
    ImageView img_back;
    CustomButton btn_signIn;
    EditText edt_otp;
    String OTP="",mobile="",user_id="";


    CustomEditText edtotp1,edtotp2,edtotp3,edtotp4,edtotp5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.Fullscreen(OtpTwoActivity.this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_otp_two);
        mContext = OtpTwoActivity.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        firebase = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        Log.e("tokensss", firebase.getString(Consts.DEVICE_TOKEN, ""));


        mobile=getIntent().getStringExtra("mobile");
        user_id=getIntent().getStringExtra("user_id");
        btn_signIn=findViewById(R.id.CBsignIn);
        edtotp1=findViewById(R.id.edtotp1);
        edtotp2=findViewById(R.id.edtotp2);
        edtotp3=findViewById(R.id.edtotp3);
        edtotp4=findViewById(R.id.edtotp4);
        edtotp5=findViewById(R.id.edtotp5);
        edtotp1.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                // TODO Auto-generated method stub
                if(edtotp1.getText().toString().length()==1)     //size as per your requirement
                {
                    edtotp2.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });
        edtotp2.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                // TODO Auto-generated method stub
                if(edtotp2.getText().toString().length()==1)     //size as per your requirement
                {
                    edtotp3.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });

        edtotp3.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                // TODO Auto-generated method stub
                if(edtotp3.getText().toString().length()==1)     //size as per your requirement
                {
                    edtotp4.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });
        edtotp4.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                // TODO Auto-generated method stub
                if(edtotp4.getText().toString().length()==1)     //size as per your requirement
                {
                    edtotp5.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });
        edtotp5.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                // TODO Auto-generated method stub
                if(edtotp5.getText().toString().length()==1)     //size as per your requirement
                {
                    edtotp5.clearFocus();
                    btn_signIn.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });
        edtotp5.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ( (actionId == EditorInfo.IME_ACTION_DONE) || ((event.getKeyCode() == KeyEvent.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_DOWN ))) {

                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);



                }

                return false;
            }
        });
        btn_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtotp1.getText().toString().trim().isEmpty() || edtotp2.getText().toString().trim().isEmpty() || edtotp3.getText().toString().trim().isEmpty() || edtotp4.getText().toString().trim().isEmpty() || edtotp5.getText().toString().trim().isEmpty())
                {

                    showSickbar("Please enter valid otp");
                    setfocus();
                    return;
                }
                String otp=edtotp1.getText().toString().trim()+edtotp2.getText().toString().trim()+edtotp3.getText().toString().trim()+edtotp4.getText().toString().trim()+edtotp5.getText().toString().trim();
                if(!otp.trim().isEmpty())
                {
                    OTP=otp;
                    verify();
                }
                else
                {
                    showSickbar("Please enter  otp");

                    setfocus();
                }
            }
        });
    }
    public HashMap<String, String> getparm() {

        HashMap<String, String> parms = new HashMap<>();
        parms.put("user_id", user_id);
        parms.put("mobile", mobile);
        parms.put("otp", OTP);

        return parms;
    }


    public void showSickbar(String msg) {
        Snackbar snackbar = Snackbar.make(binding.RRsncbarr, msg, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        snackbar.show();
    }
    public void verify() {
        ProjectUtils.showProgressDialog(mContext, true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.VERIFY_API, getparm(), mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag)
                {
                    try {
                        showSickbar(msg);
                        Intent intent=new Intent(mContext,SignInActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    showSickbar(msg);
                }


            }
        });
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(mContext, SignInActivity.class));
        finish();
    }
    public void setfocus()
    {
        if(edtotp1.getText().toString().trim().isEmpty())
        {
            edtotp1.requestFocus();
            return;
        }
        if(edtotp2.getText().toString().trim().isEmpty())
        {

            edtotp2.requestFocus();
            return;
        }
        if(edtotp3.getText().toString().trim().isEmpty())
        {

            edtotp3.requestFocus();
            return;
        }
        if(edtotp4.getText().toString().trim().isEmpty())
        {

            edtotp4.requestFocus();
            return;
        }
        if(edtotp5.getText().toString().trim().isEmpty())
        {

            edtotp5.requestFocus();
            return;
        }
    }
}

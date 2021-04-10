package com.kamaii.customer.ui.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.kamaii.customer.DTO.UserDTO;
import com.kamaii.customer.R;
import com.kamaii.customer.api.apiClient;
import com.kamaii.customer.api.apiRest;
import com.kamaii.customer.databinding.ActivityOtpBinding;
import com.kamaii.customer.https.HttpsRequest;
import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.interfacess.Helper;
import com.kamaii.customer.preferences.SharedPrefrence;
import com.kamaii.customer.utils.CustomEditText;
import com.kamaii.customer.utils.CustomTextView;
import com.kamaii.customer.utils.CustomTextViewBold;
import com.kamaii.customer.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.widget.Toast.LENGTH_LONG;

public class OtpActivity extends AppCompatActivity {

    private Context mContext;
    private String TAG = OtpActivity.class.getSimpleName();
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private SharedPreferences firebase;
    private boolean isHide = false;
    private ActivityOtpBinding binding;
    CustomTextViewBold otp_txt;
    ImageView img_back;
    ImageView btn_signIn;
    String mobile = "", mainotp = "", verificationCode = "", name = "", email, pasword;
    ProgressDialog progressDialog;
    TextView txt_timer, txt_resend_timer;
    CustomTextView txt_resend;
    CustomEditText otp_receieved;
    TextInputEditText edtotp1, edtotp2, edtotp3, edtotp4, edtotp5, edtotp0;
    boolean flag = false;
    public int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ProjectUtils.Fullscreen(OtpActivity.this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_otp);
        mContext = OtpActivity.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        firebase = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        Log.e("tokensss", firebase.getString(Consts.DEVICE_TOKEN, ""));
        progressDialog = new ProgressDialog(OtpActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");
        otp_txt = findViewById(R.id.otp_txt);
        txt_resend_timer = findViewById(R.id.txt_resend_timer);

        flag = getIntent().getBooleanExtra("signin_flag", false);
        if (flag) {
            mobile = getIntent().getStringExtra(Consts.MOBILE);
            mainotp = getIntent().getStringExtra(Consts.OTP);
        } else {
            name = getIntent().getStringExtra(Consts.NAME);
            email = getIntent().getStringExtra(Consts.EMAIL_ID);
            mobile = getIntent().getStringExtra(Consts.MOBILE);
            mainotp = getIntent().getStringExtra(Consts.OTP);
        }

        btn_signIn = findViewById(R.id.CBsignIn);
        edtotp1 = findViewById(R.id.edtotp1);
        edtotp2 = findViewById(R.id.edtotp2);
        edtotp3 = findViewById(R.id.edtotp3);
        edtotp4 = findViewById(R.id.edtotp4);
        edtotp5 = findViewById(R.id.edtotp5);
        edtotp0 = findViewById(R.id.edtotp0);
        txt_timer = findViewById(R.id.txt_timer);
        txt_resend = findViewById(R.id.txt_resend);
        otp_receieved = findViewById(R.id.otp_receieved);

        otp_txt.setText(mobile);


        edtotp0.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (edtotp0.getText().toString().length() == 1)     //size as per your requirement
                {
                    edtotp1.requestFocus();
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
        edtotp1.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (edtotp1.getText().toString().length() == 1)     //size as per your requirement
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

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (edtotp2.getText().toString().length() == 1)     //size as per your requirement
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

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (edtotp3.getText().toString().length() == 1)     //size as per your requirement
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

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (edtotp4.getText().toString().length() == 1)     //size as per your requirement
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

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (edtotp5.getText().toString().length() == 1)     //size as per your requirement
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

        otp_receieved.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (otp_receieved.getText().toString().length() == 6)     //size as per your requirement
                {
                    otp_receieved.requestFocus();
                    btn_signIn.performClick();
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

        btn_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = otp_receieved.getText().toString().trim();
                if (otp.isEmpty()) {


                    showSickbar("Please enter valid otp");

                    setfocus();
                    return;
                }
                if (!otp.trim().isEmpty()) {

                    if (mainotp.equalsIgnoreCase(otp)) {

                        if (flag) {
                            login();
                        } else {
                            register();
                        }
                    } else {
                        Toast.makeText(OtpActivity.this, "Please enter valid otp", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    showSickbar("Please enter valid otp");
                    setfocus();
                }

            }
        });

        txt_resend.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                sendotp();
                txt_resend_timer.setVisibility(View.VISIBLE);
                txt_resend.setClickable(false);
                txt_resend.setTextColor(getResources().getColor(R.color.pink_text_color));
                new CountDownTimer(30000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        txt_resend_timer.setText(String.valueOf(counter) + " seconds");
                        counter++;
                    }

                    @Override
                    public void onFinish() {
                        counter = 0;
                        txt_resend_timer.setVisibility(View.GONE);
                        txt_resend.setTextColor(getResources().getColor(R.color.colorAccent));
                        txt_resend.setClickable(true);
                    }
                }.start();
            }
        });
    }

    public void sendotp() {

        progressDialog.show();
        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.send_otp(mobile,"2");
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();

                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        JSONObject object = new JSONObject(s);

                        String message = object.getString("message");
                        int sstatus = object.getInt("status");

                        if (sstatus == 1) {

                            try {

                                String otp = object.getString("otp");
                                mainotp = otp;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {
                            Toast.makeText(OtpActivity.this, message,
                                    LENGTH_LONG).show();
                        }


                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(OtpActivity.this, "Please try again later",
                                LENGTH_LONG).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(OtpActivity.this, "Try again. Server is not responding.",
                        LENGTH_LONG).show();
            }
        });
    }

    public HashMap<String, String> getparm() {

        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.NAME, name);
        parms.put(Consts.EMAIL_ID, email);
        parms.put(Consts.MOBILE, mobile);
        parms.put(Consts.ROLE, "2");
        parms.put(Consts.DEVICE_TYPE, "ANDROID");
        parms.put(Consts.DEVICE_TOKEN, firebase.getString(Consts.DEVICE_TOKEN, ""));
        parms.put(Consts.DEVICE_ID, "12345");
        Log.e(TAG+"register123_params", parms.toString());


        return parms;
    }

    public void register() {
        progressDialog.show();
        new HttpsRequest(Consts.REGISTER_API, getparm(), mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                progressDialog.dismiss();
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {

                        Log.e(TAG+"register123_response", response.toString());

                        userDTO = new Gson().fromJson(response.getJSONObject("data").toString(), UserDTO.class);
                        prefrence.setParentUser(userDTO, Consts.USER_DTO);

                        prefrence.setBooleanValue(Consts.IS_REGISTERED, true);
                        Intent in = new Intent(mContext, BaseActivity.class);
                        in.putExtra("first_time_login", true);
                        startActivity(in);
                        finish();
                        overridePendingTransition(R.anim.anim_slide_in_left,
                                R.anim.anim_slide_out_left);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    ProjectUtils.showToast(mContext, msg);
                    Intent in = new Intent(mContext, CheckSignInActivity.class);
                    startActivity(in);
                    finish();
                    overridePendingTransition(R.anim.anim_slide_in_left,
                            R.anim.anim_slide_out_left);

                }
            }
        });
    }

    public HashMap<String, String> getparmotp(String user_id) {

        HashMap<String, String> parms = new HashMap<>();
        parms.put("user_id", user_id);
        parms.put("mobile", mobile);


        return parms;
    }

    public void showSickbar(String msg) {

        Toast.makeText(OtpActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void setfocus() {

        if (edtotp0.getText().toString().trim().isEmpty()) {
            edtotp0.requestFocus();
            return;
        }
        if (edtotp1.getText().toString().trim().isEmpty()) {
            edtotp1.requestFocus();
            return;
        }
        if (edtotp2.getText().toString().trim().isEmpty()) {

            edtotp2.requestFocus();
            return;
        }
        if (edtotp3.getText().toString().trim().isEmpty()) {

            edtotp3.requestFocus();
            return;
        }
        if (edtotp4.getText().toString().trim().isEmpty()) {

            edtotp4.requestFocus();
            return;
        }
        if (edtotp5.getText().toString().trim().isEmpty()) {

            edtotp5.requestFocus();
            return;
        }
    }

    public HashMap<String, String> getSignparm() {

        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.MOBILE, mobile);
        parms.put("otp", mainotp);
        parms.put(Consts.DEVICE_TYPE, "ANDROID");
        parms.put(Consts.DEVICE_TOKEN, firebase.getString(Consts.DEVICE_TOKEN, ""));
        parms.put(Consts.DEVICE_ID, "12345");
        parms.put(Consts.ROLE, "2");
        Log.e(TAG + " Login", parms.toString());
        return parms;
    }

    public void login() {
        progressDialog.show();
        new HttpsRequest(Consts.LOGIN_API, getSignparm(), mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                progressDialog.dismiss();
                Log.e("LOGIN_RES_flag", "" + response.toString());

                if (flag) {
                    try {
                        Log.e("LOGIN_RES", "" + response.toString());

                        userDTO = new Gson().fromJson(response.getJSONObject("data").toString(), UserDTO.class);
                        prefrence.setParentUser(userDTO, Consts.USER_DTO);

                        prefrence.setBooleanValue(Consts.IS_REGISTERED, true);

                        Intent in = new Intent(OtpActivity.this, BaseActivity.class);
                        startActivity(in);
                        finish();
                        overridePendingTransition(R.anim.anim_slide_in_left,
                                R.anim.anim_slide_out_left);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(OtpActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

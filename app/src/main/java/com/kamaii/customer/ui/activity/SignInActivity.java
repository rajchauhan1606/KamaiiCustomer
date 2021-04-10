package com.kamaii.customer.ui.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.kamaii.customer.DTO.UserDTO;
import com.kamaii.customer.R;
import com.kamaii.customer.databinding.ActivitySignInBinding;
import com.kamaii.customer.https.HttpsRequest;
import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.interfacess.Helper;
import com.kamaii.customer.network.NetworkManager;
import com.kamaii.customer.preferences.SharedPrefrence;
import com.kamaii.customer.utils.ProjectUtils;

import org.json.JSONObject;

import java.util.HashMap;


public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;
    private String TAG = SignInActivity.class.getSimpleName();
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private SharedPreferences firebase;
    private boolean isHide = false;
    private ActivitySignInBinding binding;
    String mobileno = "";
    private HashMap<String, String> parms = new HashMap<>();
    EditText edt_mono;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.Fullscreen(SignInActivity.this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in);
        mContext = SignInActivity.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        firebase = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        Log.e("tokensss", firebase.getString(Consts.DEVICE_TOKEN, ""));
        progressDialog = new ProgressDialog(SignInActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");
        if (getIntent() != null) {
            mobileno = getIntent().getStringExtra(Consts.MOBILE);
            binding.CETemailadd.setText(mobileno);

        }
        setUiAction();
    }

    public void setUiAction() {
        binding.CBsignIn.setOnClickListener(this);
        binding.CTVBforgot.setOnClickListener(this);
        binding.CTVsignup.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.CTVBforgot:

                final Dialog dialog = new Dialog(SignInActivity.this);
                dialog.setContentView(R.layout.custom_dialog_forgot);

                 edt_mono =  dialog.findViewById(R.id.edt_mono);



                TextView txt_cancel =  dialog.findViewById(R.id.txt_cancel);

                txt_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                TextView txt_continue =  dialog.findViewById(R.id.txt_continue);

                txt_continue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!Validatemobile()) {
                            return;
                        } else {
                            if (NetworkManager.isConnectToInternet(mContext)) {
                                parms.put(Consts.MOBILE, ProjectUtils.getEditTextValue(edt_mono));
                                ProjectUtils.showProgressDialog(mContext, false, getResources().getString(R.string.please_wait));
                                new HttpsRequest(Consts.FORGET_PASSWORD_API, parms, mContext).stringPost(TAG, new Helper() {
                                    @Override
                                    public void backResponse(boolean flag, String msg, JSONObject response) {
                                        ProjectUtils.pauseProgressDialog();
                                        if (flag) {
                                            Toast.makeText(SignInActivity.this,msg, Toast.LENGTH_SHORT).show();

                                            binding.CETemailadd.setText("");
                                            dialog.dismiss();

                                        } else {
                                            Toast.makeText(SignInActivity.this,msg, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            } else {
                                Toast.makeText(SignInActivity.this,getResources().getString(R.string.internet_concation), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

                dialog.show();
                break;
            case R.id.CBsignIn:
                clickForSubmit();
                break;
            case R.id.CTVsignup:
                startActivity(new Intent(mContext, SignUpActivity.class));
                break;
        }
    }


    public boolean Validatemobile() {
        if (!ProjectUtils.isPhoneNumberValid(edt_mono.getText().toString().trim())) {
            Toast.makeText(SignInActivity.this, getResources().getString(R.string.val_mobile), Toast.LENGTH_SHORT).show();

            return false;
        }
        return true;
    }

    public void login() {
        progressDialog.show();
        new HttpsRequest(Consts.LOGIN_API, getparm(), mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                progressDialog.dismiss();
                if (flag) {
                    try {
                        userDTO = new Gson().fromJson(response.getJSONObject("data").toString(), UserDTO.class);
                        prefrence.setParentUser(userDTO, Consts.USER_DTO);

                        prefrence.setBooleanValue(Consts.IS_REGISTERED, true);
                        Intent in = new Intent(SignInActivity.this, BaseActivity.class);
                        startActivity(in);
                        finish();
                        overridePendingTransition(R.anim.anim_slide_in_left,
                                R.anim.anim_slide_out_left);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(SignInActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void clickForSubmit() {

        if (!ProjectUtils.isPhoneNumberValid(binding.CETemailadd.getText().toString().trim())) {
            Toast.makeText(SignInActivity.this, getResources().getString(R.string.val_mobile), Toast.LENGTH_SHORT).show();
        } else {
            if (NetworkManager.isConnectToInternet(mContext)) {
                login();
            } else {
                Toast.makeText(SignInActivity.this, getResources().getString(R.string.internet_concation), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public HashMap<String, String> getparm() {

        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.MOBILE, ProjectUtils.getEditTextValue(binding.CETemailadd));
        parms.put(Consts.PASSWORD, binding.CETenterpassword.getText().toString().trim());
        parms.put(Consts.DEVICE_TYPE, "ANDROID");
        parms.put(Consts.DEVICE_TOKEN, firebase.getString(Consts.DEVICE_TOKEN, ""));
        parms.put(Consts.DEVICE_ID, "12345");
        parms.put(Consts.ROLE, "2");
        Log.e(TAG + " Login", parms.toString());
        return parms;
    }


    @Override
    public void onBackPressed() {
        finish();
    }
}

package com.kamaii.customer.ui.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.kamaii.customer.DTO.UserDTO;
import com.kamaii.customer.R;
import com.kamaii.customer.api.apiClient;
import com.kamaii.customer.api.apiRest;
import com.kamaii.customer.databinding.ActivitySignUpBinding;
import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.network.NetworkManager;
import com.kamaii.customer.preferences.SharedPrefrence;
import com.kamaii.customer.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.widget.Toast.LENGTH_LONG;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;
    private String TAG = SignUpActivity.class.getSimpleName();
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private SharedPreferences firebase;
    private boolean isHide = false;
    private ActivitySignUpBinding binding;
    String mobileno = "";
    ProgressDialog progressDialog;
    private FusedLocationProviderClient mFusedLocationClient;
    private double latitude = 0.0;
    private double longitude = 0.0;
    boolean single_time_location = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.Fullscreen(SignUpActivity.this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        mContext = SignUpActivity.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        firebase = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        Log.e("tokensss", firebase.getString(Consts.DEVICE_TOKEN, ""));
        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if (location != null) {

                    if (!single_time_location){
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();

                        Log.e("GPS_L"," latitude "+latitude);
                        Log.e("GPS_L"," longitude "+longitude);
                    }
                    //txtLocation.setText(String.format(Locale.US, "%s -- %s", wayLatitude, wayLongitude));

                    Geocoder geocoder = new Geocoder(SignUpActivity.this, Locale.getDefault());
                    try {
                        // Toast.makeText(mContext, "8", Toast.LENGTH_LONG).show();

                        List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                        Address obj = addresses.get(0);
                        String add = obj.getAddressLine(0);
                        add = add + "\n" + obj.getCountryName();
                        add = add + "\n" + obj.getCountryCode();
                        add = add + "\n" + obj.getAdminArea();
                        add = add + "\n" + obj.getPostalCode();
                        add = add + "\n" + obj.getSubAdminArea();
                        add = add + "\n" + obj.getLocality();
                        add = add + "\n" + obj.getSubThoroughfare();

                       // rider_location = obj.getAddressLine(0);
                        Log.e("GPS_L"," address "+obj.getAddressLine(0));

                        single_time_location = true;

                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

            }
        });
        setUiAction();
    }

    public void setUiAction() {
        binding.CBsignUp.setOnClickListener(this);
        binding.CTVsignin.setOnClickListener(this);
        binding.tvTerms.setOnClickListener(this);
        binding.tvPrivacy.setOnClickListener(this);
        if (getIntent() != null) {
            mobileno = getIntent().getStringExtra(Consts.MOBILE);
            binding.edtmono.setText(mobileno);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.CBsignUp:
                clickForSubmit();
                break;
            case R.id.CTVsignin:
                startActivity(new Intent(mContext, SignInActivity.class));
                finish();
                break;
            case R.id.tvTerms:
                startActivity(new Intent(mContext, Terms.class));
                break;
            case R.id.tvPrivacy:
                startActivity(new Intent(mContext, Privacy.class));
                break;

        }

    }

    public void clickForSubmit() {
        if (!ProjectUtils.isPhoneNumberValid(binding.edtmono.getText().toString().trim())) {
            Toast.makeText(SignUpActivity.this, getResources().getString(R.string.val_mobile), Toast.LENGTH_SHORT).show();
        } else if (!validation(binding.CETfirstname, getResources().getString(R.string.val_name))) {
            return;
        } else if (!ProjectUtils.isEmailValid(binding.CETemailadd.getText().toString().trim())) {
            Toast.makeText(SignUpActivity.this, getResources().getString(R.string.val_email), Toast.LENGTH_SHORT).show();

        } else if (!validateTerms()) {
            return;
        } else {
            if (NetworkManager.isConnectToInternet(mContext)) {

                sendotp();
            } else {
                Toast.makeText(SignUpActivity.this, getResources().getString(R.string.internet_concation), Toast.LENGTH_SHORT).show();
            }
        }


    }

    public void sendotp() {


        progressDialog.show();
        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.send_otp(ProjectUtils.getEditTextValue(binding.edtmono), "2");
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();

                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        Log.e("register123otp_response", "" + s);
                        JSONObject object = new JSONObject(s);

                        String message = object.getString("message");
                        int sstatus = object.getInt("status");

                        if (sstatus == 1) {

                            try {

                                String otp = object.getString("otp");

                                Intent intent = new Intent(SignUpActivity.this, OtpActivity.class);
                                intent.putExtra(Consts.NAME, ProjectUtils.getEditTextValue(binding.CETfirstname));
                                intent.putExtra(Consts.EMAIL_ID, ProjectUtils.getEditTextValue(binding.CETemailadd));
                                //intent.putExtra(Consts.PASSWORD, ProjectUtils.getEditTextValue(binding.CETenterpassword));
                                intent.putExtra(Consts.REFERRAL_CODE, "");
                                intent.putExtra(Consts.MOBILE, ProjectUtils.getEditTextValue(binding.edtmono));
                                intent.putExtra(Consts.OTP, otp);
                                intent.putExtra(Consts.LATITUDE, String.valueOf(latitude));
                                intent.putExtra(Consts.LONGITUDE, String.valueOf(longitude));
                                startActivity(intent);
                                finish();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {
                            Toast.makeText(SignUpActivity.this, message,
                                    LENGTH_LONG).show();
                        }


                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(SignUpActivity.this, "Please try again later",
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
                Toast.makeText(SignUpActivity.this, "Try again. Server is not responding.",
                        LENGTH_LONG).show();


            }
        });
    }

    private boolean validateTerms() {
        if (binding.termsCB.isChecked()) {
            return true;
        } else {
            Toast.makeText(SignUpActivity.this, getResources().getString(R.string.trms_acc), Toast.LENGTH_SHORT).show();

            return false;
        }
    }


    public HashMap<String, String> getparm() {

        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.NAME, ProjectUtils.getEditTextValue(binding.CETfirstname));
        parms.put(Consts.EMAIL_ID, ProjectUtils.getEditTextValue(binding.CETemailadd));
        parms.put(Consts.MOBILE, ProjectUtils.getEditTextValue(binding.edtmono));
        parms.put(Consts.ROLE, "2");
        parms.put(Consts.DEVICE_TYPE, "ANDROID");
        parms.put(Consts.DEVICE_TOKEN, firebase.getString(Consts.DEVICE_TOKEN, ""));
        parms.put(Consts.DEVICE_ID, "12345");
        Log.e(TAG, parms.toString());
        return parms;
    }


    public boolean validation(EditText editText, String msg) {
        if (!ProjectUtils.isEditTextFilled(editText)) {

            Toast.makeText(SignUpActivity.this, msg, Toast.LENGTH_SHORT).show();

            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}

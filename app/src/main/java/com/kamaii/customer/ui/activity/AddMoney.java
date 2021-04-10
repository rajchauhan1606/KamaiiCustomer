package com.kamaii.customer.ui.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.kamaii.customer.DTO.UserDTO;
import com.kamaii.customer.R;
import com.kamaii.customer.databinding.ActivityAddMoneyBinding;
import com.kamaii.customer.databinding.DailogPaymentOptionBinding;
import com.kamaii.customer.https.HttpsRequest;
import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.interfacess.Helper;
import com.kamaii.customer.network.NetworkManager;
import com.kamaii.customer.preferences.SharedPrefrence;
import com.kamaii.customer.ui.fragment.Wallet;
import com.kamaii.customer.utils.DecimalDigitsInputFilter;
import com.kamaii.customer.utils.ProjectUtils;
import com.paykun.sdk.PaykunApiCall;
import com.paykun.sdk.eventbus.Events;
import com.paykun.sdk.eventbus.GlobalBus;
import com.paykun.sdk.helper.PaykunHelper;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;

public class AddMoney extends AppCompatActivity implements View.OnClickListener {

    private String TAG = AddMoney.class.getSimpleName();
    private Context mContext;
    float rs = 0;
    float rs1 = 0;
    float final_rs = 0;
    private HashMap<String, String> parmas = new HashMap<>();
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private String amt = "";
    private String currency = "";
    private HashMap<String, String> parmsGetWallet = new HashMap<>();

    private Dialog dialog;
    private ActivityAddMoneyBinding binding;
    ProgressDialog progressDialog;
    boolean from_booking = false;
    double diff_amt = 0;

    private String productName = "Wallet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_money);

        mContext = AddMoney.this;
        progressDialog = new ProgressDialog(AddMoney.this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);

        prefrence = SharedPrefrence.getInstance(mContext);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        parmas.put(Consts.USER_ID, userDTO.getUser_id());
        parmsGetWallet.put(Consts.USER_ID, userDTO.getUser_id());
        from_booking = getIntent().getBooleanExtra("from_booking", false);
        if (from_booking) {
            diff_amt = getIntent().getDoubleExtra("diff_amt", 0);
            int amt = (int) Math.round(diff_amt) + 1;
            binding.etAddMoney.setText(String.valueOf(amt));
        }
        setUiAction();
    }

    public void setUiAction() {

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (getIntent().hasExtra(Consts.AMOUNT)) {
            amt = getIntent().getStringExtra(Consts.AMOUNT);
            currency = getIntent().getStringExtra(Consts.CURRENCY);

            binding.tvWallet.setText(currency + " " + amt);
        }

        binding.cbAdd.setOnClickListener(this);

        binding.etAddMoney.setSelection(binding.etAddMoney.getText().length());

        binding.tv1000.setOnClickListener(this);

        binding.tv1500.setOnClickListener(this);

        binding.tv2000.setOnClickListener(this);

        binding.tv1000.setText("+ " + currency + " 1000");
        binding.tv1500.setText("+ " + currency + " 1500");
        binding.tv2000.setText("+ " + currency + " 2000");


        binding.etAddMoney.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(12, 2)});
        binding.etAddMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 1 && s.toString().startsWith("0")) {
                    s.clear();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (binding.etAddMoney.getText().toString().trim().equalsIgnoreCase("")) {
            rs1 = 0;

        } else {
            rs1 = Float.parseFloat(binding.etAddMoney.getText().toString().trim());

        }

        switch (v.getId()) {
            case R.id.tv1000:
                rs = 1000;
                final_rs = rs;
                binding.etAddMoney.setText(final_rs + "");
                binding.etAddMoney.setSelection(binding.etAddMoney.getText().length());
                break;
            case R.id.tv1500:
                rs = 1500;
                final_rs = rs;
                binding.etAddMoney.setText(final_rs + "");
                binding.etAddMoney.setSelection(binding.etAddMoney.getText().length());
                break;
            case R.id.tv2000:
                rs = 2000;
                final_rs = rs;
                binding.etAddMoney.setText(final_rs + "");
                binding.etAddMoney.setSelection(binding.etAddMoney.getText().length());
                break;
            case R.id.cbAdd:

                if (binding.etAddMoney.getText().toString().length() > 0 && Float.parseFloat(binding.etAddMoney.getText().toString().trim()) > 0) {
                    if (NetworkManager.isConnectToInternet(mContext)) {
                        parmas.put(Consts.AMOUNT, ProjectUtils.getEditTextValue(binding.etAddMoney));

                        double extraadd = (Double.parseDouble(ProjectUtils.getEditTextValue(binding.etAddMoney)) * 2) / 100;
                        double total = Double.parseDouble(ProjectUtils.getEditTextValue(binding.etAddMoney)) + extraadd;


                        DecimalFormat newFormat = new DecimalFormat("####");
                        int mainprice = Integer.valueOf(newFormat.format(total));


                        JSONObject object = new JSONObject();
                        try {
                            object.put("merchant_id", Consts.merchantIdLIVE);
                            object.put("access_token", Consts.accessTokenLIVE);
                            object.put("customer_name", userDTO.getName());
                            object.put("customer_email", userDTO.getEmail_id());
                            object.put("customer_phone", userDTO.getMobile());
                            object.put("product_name", productName);
                            object.put("order_no", System.currentTimeMillis()); // order no. should have 10 to 30 character in numeric format
                            object.put("amount", mainprice);  // minimum amount should be 10
                            object.put("isLive", true); // need to send false if you are in sandbox mode
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        new PaykunApiCall.Builder(AddMoney.this).sendJsonObject(object);
                        getWallet();

                    } else {
                        ProjectUtils.showLong(mContext, getResources().getString(R.string.internet_concation));
                    }
                } else {
                    ProjectUtils.showLong(mContext, getResources().getString(R.string.val_money));
                }
                break;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (prefrence.getValue(Consts.SURL).equalsIgnoreCase(Consts.PAYMENT_SUCCESS)) {
            prefrence.clearPreferences(Consts.SURL);
        } else if (prefrence.getValue(Consts.FURL).equalsIgnoreCase(Consts.PAYMENT_FAIL)) {
            prefrence.clearPreferences(Consts.FURL);
            finish();
        } else if (prefrence.getValue(Consts.SURL).equalsIgnoreCase(Consts.PAYMENT_SUCCESS_paypal)) {
            prefrence.clearPreferences(Consts.SURL);
        } else if (prefrence.getValue(Consts.FURL).equalsIgnoreCase(Consts.PAYMENT_FAIL_Paypal)) {
            prefrence.clearPreferences(Consts.FURL);
            finish();
        }
    }


    public void addMoney() {
        progressDialog.show();
        new HttpsRequest(Consts.ADD_MONEY_API, parmas, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                progressDialog.dismiss();
                if (flag) {
                    ProjectUtils.showLong(mContext, msg);
                    getWallet();
                    finish();
                } else {
                    ProjectUtils.showLong(mContext, msg);
                }
            }
        });
    }

    public void getWallet() {
        new HttpsRequest(Consts.GET_WALLET_API, parmsGetWallet, AddMoney.this).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {
                        amt = response.getJSONObject("data").getString("amount");
                        currency = response.getJSONObject("data").getString("currency_type");
                        binding.tvWallet.setText(currency + " " + amt);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {

                }
            }
        });
    }

    public void dialogPayment() {
        dialog = new Dialog(mContext/*, android.R.style.Theme_Dialog*/);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        final DailogPaymentOptionBinding binding1 = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.dailog_payment_option, null, false);
        dialog.setContentView(binding1.getRoot());
        dialog.show();
        dialog.setCancelable(false);
        binding1.llCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        binding1.llPaypall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Consts.MAKE_PAYMENT_paypal + "amount=" + ProjectUtils.getEditTextValue(binding.etAddMoney) + "&userId=" + userDTO.getUser_id();
                Intent in2 = new Intent(mContext, PaymetWeb.class);
                in2.putExtra(Consts.PAYMENT_URL, url);
                startActivity(in2);
                dialog.dismiss();

            }
        });
        binding1.llStripe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Consts.MAKE_PAYMENT + userDTO.getUser_id() + "/" + ProjectUtils.getEditTextValue(binding.etAddMoney);
                Intent in2 = new Intent(mContext, PaymetWeb.class);
                in2.putExtra(Consts.PAYMENT_URL, url);
                startActivity(in2);
                dialog.dismiss();

            }
        });
        binding1.llPayuMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in2 = new Intent(mContext, PaymetWeb.class);
                in2.putExtra(Consts.MONEY, ProjectUtils.getEditTextValue(binding.etAddMoney));
                startActivity(in2);
                dialog.dismiss();

            }
        });

    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void getResults(Events.PaymentMessage message) {
        if (message.getResults().equalsIgnoreCase(PaykunHelper.MESSAGE_SUCCESS)) {
            /* if you want to get your transaction detail call message.getTransactionDetail()
             *  getTransactionDetail return all the field from server and you can use it here as per your need
             *  For Example you want to get Order id from detail use message.getTransactionDetail().order.orderId */
            if (!TextUtils.isEmpty(message.getTransactionId())) {
                addMoney();
                Log.v(" order id ", " getting order id value : " + message.getTransactionDetail().order.orderId);
            }
        } else if (message.getResults().equalsIgnoreCase(PaykunHelper.MESSAGE_FAILED)) {
            Toast.makeText(AddMoney.this, "Your Transaction is failed", Toast.LENGTH_SHORT).show();
        } else if (message.getResults().equalsIgnoreCase(PaykunHelper.MESSAGE_SERVER_ISSUE)) {
            Toast.makeText(AddMoney.this, PaykunHelper.MESSAGE_SERVER_ISSUE, Toast.LENGTH_SHORT).show();
        } else if (message.getResults().equalsIgnoreCase(PaykunHelper.MESSAGE_ACCESS_TOKEN_MISSING)) {
            Toast.makeText(AddMoney.this, "Access Token missing", Toast.LENGTH_SHORT).show();
        } else if (message.getResults().equalsIgnoreCase(PaykunHelper.MESSAGE_MERCHANT_ID_MISSING)) {
            Toast.makeText(AddMoney.this, "Merchant Id is missing", Toast.LENGTH_SHORT).show();
        } else if (message.getResults().equalsIgnoreCase(PaykunHelper.MESSAGE_INVALID_REQUEST)) {
            Toast.makeText(AddMoney.this, "Invalid Request", Toast.LENGTH_SHORT).show();
        } else if (message.getResults().equalsIgnoreCase(PaykunHelper.MESSAGE_NETWORK_NOT_AVAILABLE)) {
            Toast.makeText(AddMoney.this, "Network is not available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        GlobalBus.getBus().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        GlobalBus.getBus().unregister(this);
    }


}

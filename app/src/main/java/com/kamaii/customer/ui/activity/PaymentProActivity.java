package com.kamaii.customer.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kamaii.customer.DTO.HistoryDTO;
import com.kamaii.customer.DTO.UserDTO;
import com.kamaii.customer.R;
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

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PaymentProActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = PaymentProActivity.class.getSimpleName();
    private Context mContext;
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private HistoryDTO historyDTO;
    private CircleImageView ivArtist;
    private CustomTextView tvCategory, tvLocation, llltxt;
    private CustomTextViewBold tvName, tvApplyCode, tvAmount, tvCancelCode, llltxtamount, txtcash, tvNameHedar;
    private LinearLayout llPayment, llCash, llWallet, llrozorpay, layfddf;
    private CustomEditText etCode;
    private String merchantKey, salt, userCredentials, invoice_id, user_id, coupon_code = "", final_amount, email;
    private HashMap<String, String> params = new HashMap<>();
    private ImageView IVback;
    private Dialog dialog;
    public static final int PAYPAL_REQUEST_CODE = 123;
    private LinearLayout llPaypall, llStripe, llCancel, llPayuMoney;
    private String amt1 = "";
    private String discount_amount = "0";
    private String currency = "", cat_id = "";
    private HashMap<String, String> parmsGetWallet = new HashMap<>();
    float amount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        mContext = PaymentProActivity.this;


        prefrence = SharedPrefrence.getInstance(mContext);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);

        parmsGetWallet.put(Consts.USER_ID, userDTO.getUser_id());
        params.put(Consts.USER_ID, userDTO.getUser_id());


        if (getIntent().hasExtra(Consts.HISTORY_DTO)) {
            historyDTO = (HistoryDTO) getIntent().getSerializableExtra(Consts.HISTORY_DTO);
        }

        if (getIntent().hasExtra(Consts.CATEGORY_ID)) {
            cat_id = getIntent().getStringExtra(Consts.CATEGORY_ID);
        }
        setUiAction();
    }

    public void setUiAction() {
        IVback = (ImageView) findViewById(R.id.IVback);
        tvNameHedar = findViewById(R.id.tvNameHedar);
        IVback.setOnClickListener(this);
        ivArtist = findViewById(R.id.ivArtist);
        tvCategory = findViewById(R.id.tvCategory);
        tvLocation = findViewById(R.id.tvLocation);
        tvName = findViewById(R.id.tvName);
        tvApplyCode = findViewById(R.id.tvApplyCode);
        tvCancelCode = findViewById(R.id.tvCancelCode);
        tvAmount = findViewById(R.id.tvAmount);
        llPayment = findViewById(R.id.llPayment);
        llCash = findViewById(R.id.llCash);
        llWallet = findViewById(R.id.llWallet);
        etCode = findViewById(R.id.etCode);
        llrozorpay = findViewById(R.id.llrozorpay);
        llltxt = findViewById(R.id.llltxt);
        llltxtamount = findViewById(R.id.llltxtamount);
        layfddf = findViewById(R.id.layfddf);
        txtcash = findViewById(R.id.txtcash);


        llPayment.setOnClickListener(this);
        llCash.setOnClickListener(this);
        tvApplyCode.setOnClickListener(this);
        tvCancelCode.setOnClickListener(this);
        llWallet.setOnClickListener(this);

        Glide.with(mContext).
                load(historyDTO.getArtistImage())
                .placeholder(R.drawable.dummyuser_image)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivArtist);

        tvCategory.setText(historyDTO.getCategoryName());
        tvLocation.setText(historyDTO.getAddress());
        tvName.setText(ProjectUtils.getFirstLetterCapital(historyDTO.getArtistName()));
        tvAmount.setText(historyDTO.getCurrency_type() + historyDTO.getTotal_amount());


        getWallet();
        final_amount = historyDTO.getTotal_amount();

        amount = Float.parseFloat(final_amount);

        float newamount = (amount * 2 / 100);
        llltxtamount.setText(historyDTO.getCurrency_type() + String.valueOf(newamount));
        llltxt.setText(" " + "convernience fees will be applied");


        llrozorpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });


        if (cat_id.equalsIgnoreCase("85")) {
            txtcash.setText("Rating Now");
            tvNameHedar.setText("Rate Now");
        } else {
            txtcash.setText(getResources().getString(R.string.cash_payment1));
            tvNameHedar.setText(getResources().getString(R.string.payment));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llPayment:
                Intent in2 = new Intent(mContext, PaymetWeb.class);
                in2.putExtra(Consts.MONEY, final_amount);
                startActivity(in2);

                break;
            case R.id.llWallet:
                if (Float.parseFloat(amt1) >= Float.parseFloat(final_amount)) {

                    cashDialog(getResources().getString(R.string.wallet_payment), getResources().getString(R.string.wallet_msg), "2");

                } else {
                    ProjectUtils.showToast(mContext, "Insufficient balance, please add money to wallet first.");
                }

                break;
            case R.id.llCash:
                if (cat_id.equalsIgnoreCase("85")) {
                    sendPayment("1");
                } else {
                    cashDialog(getResources().getString(R.string.cash_payment), getResources().getString(R.string.cash_msg), "1");
                }

                break;
            case R.id.tvApplyCode:
                params.put(Consts.INVOICE_ID, historyDTO.getInvoice_id());
                params.put(Consts.COUPON_CODE, ProjectUtils.getEditTextValue(etCode));

                checkCoupon();
                break;
            case R.id.tvCancelCode:
                etCode.setText("");
                tvAmount.setText(historyDTO.getCurrency_type() + historyDTO.getTotal_amount());
                final_amount = historyDTO.getTotal_amount();
                tvApplyCode.setVisibility(View.VISIBLE);
                tvCancelCode.setVisibility(View.GONE);
                coupon_code = "";
                etCode.setEnabled(true);
                break;
            case R.id.IVback:
                finish();
                break;

        }
    }


    public void checkCoupon() {
        ProjectUtils.showProgressDialog(mContext, true, mContext.getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.CHECK_COUPON_API, params, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {
                        ProjectUtils.showToast(mContext, msg);
                        String amt = response.getString("final_amount").toString();
                        discount_amount = response.getString("discount_amount").toString();
                        final_amount = amt;
                        tvAmount.setText(historyDTO.getCurrency_type() + amt);
                        tvApplyCode.setVisibility(View.GONE);
                        tvCancelCode.setVisibility(View.VISIBLE);
                        coupon_code = etCode.getText().toString().trim();
                        etCode.setEnabled(false);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    ProjectUtils.showToast(mContext, msg);
                    etCode.setEnabled(true);
                    coupon_code = "";

                }


            }
        });
    }

    public void sendPayment(String type) {
        new HttpsRequest(Consts.MAKE_PAYMENT_API, getParms(type), mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                Log.d(TAG, response.toString());
                if (flag) {
                    Intent in = new Intent(mContext, WriteReview.class);
                    in.putExtra(Consts.HISTORY_DTO, historyDTO);
                    startActivity(in);
                    finish();
                } else {
                    ProjectUtils.showToast(mContext, msg);

                }
            }
        });
    }


    public Map<String, String> getParms(String type) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Consts.INVOICE_ID, historyDTO.getInvoice_id());
        params.put(Consts.USER_ID, userDTO.getUser_id());
        params.put(Consts.COUPON_CODE, coupon_code);
        params.put(Consts.FINAL_AMOUNT, final_amount);
        params.put(Consts.PAYMENT_STATUS, "1");
        params.put(Consts.PAYMENT_TYPE, type);
        params.put(Consts.DISCOUNT_AMOUNT, discount_amount);

        Log.e("sendPaymentConfirm", params.toString());
        return params;
    }


    public void cashDialog(String title, String msg, final String type) {
        try {
            new AlertDialog.Builder(mContext)
                    .setIcon(R.mipmap.ic_launcher)
                    .setTitle(title)
                    .setMessage(msg)
                    .setCancelable(false)
                    .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sendPayment(type);
                            dialog.dismiss();


                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    })
                    .show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    public void dialogPayment() {
        dialog = new Dialog(mContext);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dailog_payment_option);


        llPaypall = (LinearLayout) dialog.findViewById(R.id.llPaypall);
        llStripe = (LinearLayout) dialog.findViewById(R.id.llStripe);
        llCancel = (LinearLayout) dialog.findViewById(R.id.llCancel);
        llPayuMoney = (LinearLayout) dialog.findViewById(R.id.llPayuMoney);

        dialog.show();
        dialog.setCancelable(false);
        llCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        llPaypall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Consts.INVOICE__PAYMENT_paypal + "amount=" + final_amount + "&userId=" + userDTO.getUser_id() + "&invoice_id=" + historyDTO.getInvoice_id();
                Intent in2 = new Intent(mContext, PaymetWeb.class);
                in2.putExtra(Consts.PAYMENT_URL, url);
                startActivity(in2);
                dialog.dismiss();
            }
        });
        llStripe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Consts.INVOICE_PAYMENT_Stripe + userDTO.getUser_id() + "/" + final_amount;
                Intent in2 = new Intent(mContext, PaymetWeb.class);
                in2.putExtra(Consts.PAYMENT_URL, url);
                startActivity(in2);
                dialog.dismiss();
            }
        });
        llPayuMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in2 = new Intent(mContext, PaymetWeb.class);
                in2.putExtra(Consts.MONEY, final_amount);
                startActivity(in2);
                dialog.dismiss();

            }
        });

    }

    public void getWallet() {
        new HttpsRequest(Consts.GET_WALLET_API, parmsGetWallet, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {
                        amt1 = response.getJSONObject("data").getString("amount");
                        currency = response.getJSONObject("data").getString("currency_type");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {

                }
            }
        });
    }

}

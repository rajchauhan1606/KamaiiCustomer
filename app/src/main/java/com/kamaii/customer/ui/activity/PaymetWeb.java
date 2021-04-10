package com.kamaii.customer.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.kamaii.customer.DTO.UserDTO;
import com.kamaii.customer.R;
import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.preferences.SharedPrefrence;
import com.kamaii.customer.utils.ProjectUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PaymetWeb extends AppCompatActivity {

    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private Context mContext;
    private WebView wvPayment;
    private ImageView IVback;
    private static String url = "";
    private static String surl = Consts.PAYMENT_SUCCESS;
    private static String surlpayu = Consts.PAYMENT_SUCCESS_PAYU;
    private static String furl = Consts.PAYMENT_FAIL;
    private static String surl_paypal = Consts.PAYMENT_SUCCESS_paypal;
    private static String furl_paypal = Consts.PAYMENT_FAIL_Paypal;
    private static String surl_stripe_book = Consts.INVOICE_PAYMENT_SUCCESS_Stripe;
    private static String furl_stripe_book = Consts.INVOICE_PAYMENT_FAIL_Stripe;
    private String price;
    private String mHash; // This will create below randomly
    private String mSalt = "u06Fxp2Qnl";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymet_web);
        mContext = PaymetWeb.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        if (getIntent().hasExtra(Consts.MONEY)) {
            price=getIntent().getStringExtra(Consts.MONEY);
        }
        init();
    }

    private void init() {
        IVback = (ImageView) findViewById(R.id.IVback);
        IVback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.click_event));
                finish();
            }
        });
    }

    private class SSLTolerentWebViewClient extends WebViewClient {

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            handler.proceed();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            ProjectUtils.pauseProgressDialog();
            if (url.equals(surl)) {
                ProjectUtils.showToast(mContext, "Payment was successful.");
                super.onPageFinished(view, surl);
                prefrence.setValue(Consts.SURL, surl);
                finish();

                wvPayment.clearCache(true);

                wvPayment.clearHistory();

                wvPayment.destroy();
            } else if (url.equals(furl)) {
                ProjectUtils.showToast(mContext, "Payment fail.");
                super.onPageFinished(view, furl);
                prefrence.setValue(Consts.FURL, furl);
                finish();
                wvPayment.clearCache(true);

                wvPayment.clearHistory();

                wvPayment.destroy();
            }else if (url.equals(surl_stripe_book)) {
                ProjectUtils.showToast(mContext, "Payment was successful.");
                super.onPageFinished(view, surl_stripe_book);
                prefrence.setValue(Consts.SURL, surl_stripe_book);
                finish();

                wvPayment.clearCache(true);

                wvPayment.clearHistory();

                wvPayment.destroy();
            } else if (url.equals(furl_stripe_book)) {
                ProjectUtils.showToast(mContext, "Payment fail.");
                super.onPageFinished(view, furl_stripe_book);
                prefrence.setValue(Consts.FURL, furl_stripe_book);
                finish();
                wvPayment.clearCache(true);

                wvPayment.clearHistory();

                wvPayment.destroy();
            } else if (url.equals(surl_paypal)) {
                ProjectUtils.showToast(mContext, "Payment was successful.");
                super.onPageFinished(view, surl_paypal);
                prefrence.setValue(Consts.SURL, surl_paypal);
                finish();
                wvPayment.clearCache(true);

                wvPayment.clearHistory();

                wvPayment.destroy();
            } else if (url.equals(furl_paypal)) {
                ProjectUtils.showToast(mContext, "Payment fail.");
                super.onPageFinished(view, furl_paypal);
                prefrence.setValue(Consts.FURL, furl_paypal);
                finish();
                wvPayment.clearCache(true);

                wvPayment.clearHistory();

                wvPayment.destroy();
            } else {
                super.onPageFinished(view, url);
            }

        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            // TODO Auto-generated method stub
            super.onReceivedError(view, errorCode, description, failingUrl);
        }
    }
    public String hashCal(String type, String str) {

        byte[] hashSequence = str.getBytes();
        StringBuffer hexString = new StringBuffer();
        try {
            MessageDigest algorithm = MessageDigest.getInstance(type);
            algorithm.reset();
            algorithm.update(hashSequence);
            byte messageDigest[] = algorithm.digest();

            for (int i = 0; i < messageDigest.length; i++) {
                String hex = Integer.toHexString(0xFF & messageDigest[i]);
                if (hex.length() == 1)
                    hexString.append("0");
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException NSAE) {
        }
        return hexString.toString();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        Log.e("StartPaymentActivity", "request code " + requestCode + " resultcode " + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
    }

}

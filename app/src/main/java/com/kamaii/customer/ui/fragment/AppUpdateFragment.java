package com.kamaii.customer.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.fragment.app.Fragment;

import com.kamaii.customer.R;
import com.kamaii.customer.https.HttpsRequest;
import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.interfacess.Helper;
import com.kamaii.customer.ui.activity.BaseActivity;
import com.kamaii.customer.utils.CustomTextView;

import org.json.JSONObject;

public class AppUpdateFragment extends Fragment {

        private View view;
        private BaseActivity baseActivity;
        private WebView mWebView;
        String data = "";
        CustomTextView webviewdata;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            view = inflater.inflate(R.layout.fragment_appupdate, container, false);

            //  getActivity().findViewById(R.id.ivLogo).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.customer_location_relative_header).setVisibility(View.GONE);

          //  webviewdata = view.findViewById(r.id.webviewdata);
            baseActivity.headerNameTV.setText(getResources().getString(R.string.app_update));
            getActivity().findViewById(R.id.headerNameTV).setVisibility(View.VISIBLE);
            mWebView = (WebView) view.findViewById(R.id.mWebView);
            mWebView.setWebViewClient(new MyBrowser());
            mWebView.getSettings().setLoadsImagesAutomatically(true);
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.loadUrl(Consts.About_URL);
         //   mWebView.loadData(Html.fromHtml(htmlData),"text/html","utf-8");
        //    webviewdata.setText(Html.fromHtml(Consts.About_URL));

         //   getUrlData();
            return view;
        }

        public void getUrlData(){

            new HttpsRequest(Consts.About_URL,getActivity()).stringGet("APP", new Helper() {
                @Override
                public void backResponse(boolean flag, String msg, JSONObject response) {
                    if (flag){

                        data = response.toString();
                        Log.e("WEBVIEWDATA",""+data);
                    }
                }
            });
        }
        private class MyBrowser extends WebViewClient {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            baseActivity = (BaseActivity) activity;
        }
}

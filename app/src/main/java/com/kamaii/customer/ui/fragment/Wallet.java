package com.kamaii.customer.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.customer.DTO.UserDTO;
import com.kamaii.customer.DTO.WalletHistory;
import com.kamaii.customer.R;
import com.kamaii.customer.https.HttpsRequest;
import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.interfacess.Helper;
import com.kamaii.customer.network.NetworkManager;
import com.kamaii.customer.preferences.SharedPrefrence;
import com.kamaii.customer.ui.activity.AddMoney;
import com.kamaii.customer.ui.activity.BaseActivity;
import com.kamaii.customer.ui.adapter.AdapterWalletHistory;
import com.kamaii.customer.utils.CustomEditText;
import com.kamaii.customer.utils.CustomTextView;
import com.kamaii.customer.utils.CustomTextViewBold;
import com.kamaii.customer.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Wallet extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private View view;
    private LinearLayout llAddMoney;
    private CustomTextView tvAll, tvDebit, tvCredit;
    private CustomTextView tvAllSelect, tvDebitSelect, tvCreditSelect;
    private AdapterWalletHistory adapterWalletHistory;
    private ArrayList<WalletHistory> walletHistoryList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String TAG = Wallet.class.getSimpleName();
    private RecyclerView RVhistorylist;
    private LinearLayoutManager mLayoutManager;
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private CustomTextViewBold tvNo;
    private String status = "";
    private HashMap<String, String> parms;
    private HashMap<String, String> parmsGetWallet = new HashMap<>();
    private CustomTextViewBold tvWallet;
    private String amt = "";
    private String currency = "";
    private BaseActivity baseActivity;
    Button btncashout;
    private Dialog dialog;
    CustomEditText etamount;
    CustomTextView tvCancel,tvAdd;
    private HashMap<String, String> paramsRequest = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_wallet, container, false);
        prefrence = SharedPrefrence.getInstance(getActivity());
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        baseActivity.headerNameTV.setText(getResources().getString(R.string.ic_wallet));
        baseActivity.base_recyclerview.setVisibility(View.GONE);

        parmsGetWallet.put(Consts.USER_ID, userDTO.getUser_id());
        paramsRequest.put(Consts.USER_ID, userDTO.getUser_id());
        parms = new HashMap<>();

        getActivity().findViewById(R.id.ivLogo).setVisibility(View.GONE);
        getActivity().findViewById(R.id.headerNameTV).setVisibility(View.VISIBLE);
        CustomTextViewBold screen_name = getActivity().findViewById(R.id.headerNameTV);

        parms.put(Consts.USER_ID, userDTO.getUser_id());
        setUiAction(view);
        return view;
    }

    public void setUiAction(View v) {
        tvWallet = v.findViewById(R.id.tvWallet);

        tvAll = v.findViewById(R.id.tvAll);
        tvAll.setOnClickListener(this);

        tvDebit = v.findViewById(R.id.tvDebit);
        tvDebit.setOnClickListener(this);

        tvCredit = v.findViewById(R.id.tvCredit);
        tvCredit.setOnClickListener(this);

        tvAllSelect = v.findViewById(R.id.tvAllSelect);
        tvDebitSelect = v.findViewById(R.id.tvDebitSelect);
        tvCreditSelect = v.findViewById(R.id.tvCreditSelect);
        btncashout = v.findViewById(R.id.btncashout);
        dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dailog_cashout);
        etamount=dialog.findViewById(R.id.etamount);
        tvCancel=dialog.findViewById(R.id.tvCancel);
        tvAdd=dialog.findViewById(R.id.tvAdd);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btncashout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        llAddMoney = v.findViewById(R.id.llAddMoney);
        llAddMoney.setOnClickListener(this);

        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
        tvNo = v.findViewById(R.id.tvNo);
        RVhistorylist = v.findViewById(R.id.RVhistorylist);

        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        RVhistorylist.setLayoutManager(mLayoutManager);

        swipeRefreshLayout.setOnRefreshListener(this);


    }


    public void requestPayment() {

        paramsRequest.put(Consts.AMOUNT, etamount.getText().toString());
        ProjectUtils.showProgressDialog(getActivity(), true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.WALLET_REQUEST_API, paramsRequest, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    Toast.makeText(getActivity(),msg, Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    getWallet();
                } else {
                    Toast.makeText(getActivity(),msg, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llAddMoney:
                if (NetworkManager.isConnectToInternet(getActivity())) {
                    Intent in = new Intent(getActivity(), AddMoney.class);
                    in.putExtra(Consts.AMOUNT, amt);
                    in.putExtra(Consts.CURRENCY, currency);
                    startActivity(in);
                } else {
                    ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
                }

                break;
            case R.id.tvAll:
                setSelected(true, false, false);
                parms = new HashMap<>();
                parms.put(Consts.USER_ID, userDTO.getUser_id());
                getHistroy();
                break;
            case R.id.tvCredit:
                setSelected(false, false, true);
                status = "0";
                parms = new HashMap<>();
                parms.put(Consts.USER_ID, userDTO.getUser_id());
                parms.put(Consts.STATUS, status);
                getHistroy();
                break;
            case R.id.tvDebit:
                setSelected(false, true, false);
                status = "1";
                parms = new HashMap<>();
                parms.put(Consts.USER_ID, userDTO.getUser_id());
                parms.put(Consts.STATUS, status);
                getHistroy();
                break;
        }
    }

    public void getHistroy() {
        ProjectUtils.showProgressDialog(getActivity(), true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.GET_WALLET_HISTORY_API, parms, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                swipeRefreshLayout.setRefreshing(false);
                if (flag) {
                    tvNo.setVisibility(View.GONE);
                    RVhistorylist.setVisibility(View.VISIBLE);
                    try {
                        walletHistoryList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<WalletHistory>>() {
                        }.getType();
                        walletHistoryList = (ArrayList<WalletHistory>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);
                        showData();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    tvNo.setVisibility(View.VISIBLE);
                    RVhistorylist.setVisibility(View.GONE);

                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getWallet();
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        Log.e("Runnable", "FIRST");
                                        if (NetworkManager.isConnectToInternet(getActivity())) {
                                            swipeRefreshLayout.setRefreshing(true);
                                            getHistroy();

                                        } else {
                                            ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
                                        }
                                    }
                                }
        );

    }

    public void getWallet() {
        new HttpsRequest(Consts.GET_WALLET_API, parmsGetWallet, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {
                        amt = response.getJSONObject("data").getString("amount");
                        currency = response.getJSONObject("data").getString("currency_type");
                        tvWallet.setText(currency + " " + amt);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {

                }
            }
        });
    }

    public void showData() {

        etamount.setText(amt);
        if (walletHistoryList.size() > 0) {
            tvNo.setVisibility(View.GONE);
            RVhistorylist.setVisibility(View.VISIBLE);

            adapterWalletHistory = new AdapterWalletHistory(Wallet.this, walletHistoryList);
            RVhistorylist.setAdapter(adapterWalletHistory);
        } else {
            tvNo.setVisibility(View.VISIBLE);
            RVhistorylist.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRefresh() {
        Log.e("ONREFREST_Firls", "FIRS");
        getHistroy();
    }

    public void setSelected(boolean firstBTN, boolean secondBTN, boolean thirdBTN) {
        if (firstBTN) {
            tvAllSelect.setVisibility(View.VISIBLE);
            tvDebitSelect.setVisibility(View.GONE);
            tvCreditSelect.setVisibility(View.GONE);

        }
        if (secondBTN) {
            tvDebitSelect.setVisibility(View.VISIBLE);
            tvAllSelect.setVisibility(View.GONE);
            tvCreditSelect.setVisibility(View.GONE);

        }
        if (thirdBTN) {
            tvCreditSelect.setVisibility(View.VISIBLE);
            tvAllSelect.setVisibility(View.GONE);
            tvDebitSelect.setVisibility(View.GONE);

        }
        tvAllSelect.setSelected(firstBTN);
        tvDebitSelect.setSelected(secondBTN);
        tvCreditSelect.setSelected(secondBTN);

    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        baseActivity = (BaseActivity) activity;
    }
}

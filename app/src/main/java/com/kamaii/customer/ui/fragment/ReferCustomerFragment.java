package com.kamaii.customer.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.customer.DTO.EarningDTO;
import com.kamaii.customer.DTO.UserDTO;
import com.kamaii.customer.R;
import com.kamaii.customer.https.HttpsRequest;
import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.interfacess.Helper;
import com.kamaii.customer.network.NetworkManager;
import com.kamaii.customer.preferences.SharedPrefrence;
import com.kamaii.customer.ui.activity.BaseActivity;
import com.kamaii.customer.ui.adapter.ReferCustomerAdapter;
import com.kamaii.customer.ui.models.ReferCustomer;
import com.kamaii.customer.utils.CustomEditText;
import com.kamaii.customer.utils.CustomTextViewBold;
import com.kamaii.customer.utils.ProjectUtils;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReferCustomerFragment extends Fragment {

    private View view;
    ReferCustomerAdapter referCustomerAdapter;
    private ArrayList<ReferCustomer> referCustomerArrayList = new ArrayList<>();
    RecyclerView rvrefercustomer;
    private LinearLayoutManager mLayoutManager;
    private UserDTO userDTO;
    private SharedPrefrence prefrence;
    private String TAG = ReferCustomerFragment.class.getSimpleName();
    CustomTextViewBold tvNo, txttotalreferamount;
    private BaseActivity baseActivity;
    CustomEditText svSearch_etx;
    private HashMap<String, String> paramsRequest = new HashMap<>();
    EarningDTO earningDTO;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_refercustomerfragment, container, false);
        //    getActivity().findViewById(R.id.ivLogo).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.customer_location_relative_header).setVisibility(View.GONE);

        baseActivity.headerNameTV.setText(getResources().getString(R.string.viewrefer));
        getActivity().findViewById(R.id.headerNameTV).setVisibility(View.VISIBLE);
        baseActivity.base_recyclerview.setVisibility(View.GONE);

        rvrefercustomer = view.findViewById(R.id.rvrefercustomer);
        tvNo = view.findViewById(R.id.tvNo);
        txttotalreferamount = view.findViewById(R.id.txttotalreferamount);

        prefrence = SharedPrefrence.getInstance(getActivity());
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        paramsRequest.put(Consts.USER_ID, userDTO.getUser_id());
        getActivity().findViewById(R.id.ivLogo).setVisibility(View.GONE);
        getActivity().findViewById(R.id.headerNameTV).setVisibility(View.VISIBLE);
        CustomTextViewBold screen_name = getActivity().findViewById(R.id.headerNameTV);
        screen_name.setText("Refer");
        if (NetworkManager.isConnectToInternet(getActivity())) {
            viewrefer();

        } else {
            ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
        }
        svSearch_etx = view.findViewById(R.id.svSearch_refer);


        view.findViewById(R.id.add_refer_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AddReferFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        if (NetworkManager.isConnectToInternet(getActivity())) {
            getEarning();
        } else {
            ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
        }


        svSearch_etx.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 0) {
                    referCustomerAdapter.filter(s.toString());

                } else {
                    viewrefer();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return view;
    }

    public void showData() {
        txttotalreferamount.setText(earningDTO.getReferral_earning());

    }

    public void getEarning() {
        ProjectUtils.showProgressDialog(getActivity(), true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.MY_EARNING1_USER, paramsRequest, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {
                        earningDTO = new Gson().fromJson(response.getJSONObject("data").toString(), EarningDTO.class);
                        showData();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                }
            }
        });

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        baseActivity = (BaseActivity) activity;
    }

    public HashMap<String, String> getparm() {
        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.USER_ID, userDTO.getUser_id());
        return parms;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (NetworkManager.isConnectToInternet(getActivity())) {
            viewrefer();

        } else {
            ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
        }
    }

    public void viewrefer() {
        new HttpsRequest(Consts.VIEW_REFER, getparm(), getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                        referCustomerArrayList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<ReferCustomer>>() {
                        }.getType();
                        referCustomerArrayList = new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);
                        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                        rvrefercustomer.setLayoutManager(mLayoutManager);
                        referCustomerAdapter = new ReferCustomerAdapter(getActivity(), referCustomerArrayList);
                        rvrefercustomer.setAdapter(referCustomerAdapter);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {

                    tvNo.setVisibility(View.VISIBLE);
                    rvrefercustomer.setVisibility(View.GONE);
                    svSearch_etx.setVisibility(View.GONE);
                }


            }
        });
    }


}

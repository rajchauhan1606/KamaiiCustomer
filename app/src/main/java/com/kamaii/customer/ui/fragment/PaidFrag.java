package com.kamaii.customer.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.customer.DTO.HistoryDTO;
import com.kamaii.customer.DTO.UserDTO;
import com.kamaii.customer.R;
import com.kamaii.customer.https.HttpsRequest;
import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.interfacess.Helper;
import com.kamaii.customer.network.NetworkManager;
import com.kamaii.customer.preferences.SharedPrefrence;
import com.kamaii.customer.ui.activity.BaseActivity;
import com.kamaii.customer.ui.adapter.PaidAdapter;
import com.kamaii.customer.utils.CustomEditText;
import com.kamaii.customer.utils.CustomTextViewBold;
import com.kamaii.customer.utils.ProjectUtils;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class PaidFrag extends Fragment {

    private View view;
    private String TAG = PaidFrag.class.getSimpleName();
    private RecyclerView RVhistorylist;
    private PaidAdapter paidAdapter;
    private ArrayList<HistoryDTO> historyDTOList;
    private ArrayList<HistoryDTO> historyDTOListPaid;
    private LinearLayoutManager mLayoutManager;
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private CustomTextViewBold tvNo;
    private LayoutInflater myInflater;
    //private SearchView svSearch;
    CustomEditText svSearch;
    private RelativeLayout rlSearch;
    private SwipeRefreshLayout swipeRefreshLayout;
    private BaseActivity baseActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_paid, container, false);
        baseActivity.headerNameTV.setText(getResources().getString(R.string.invoice));
        getActivity().findViewById(R.id.headerNameTV).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.ivLogo).setVisibility(View.GONE);
        getActivity().findViewById(R.id.customer_location_relative_header).setVisibility(View.GONE);
        baseActivity.base_recyclerview.setVisibility(View.GONE);

        prefrence = SharedPrefrence.getInstance(getActivity());
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        myInflater = LayoutInflater.from(getActivity());
        setUiAction(view);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        baseActivity = (BaseActivity) activity;
    }

    public void setUiAction(View v) {
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
        rlSearch = v.findViewById(R.id.rlSearch);
        svSearch = v.findViewById(R.id.svSearch);
        tvNo = v.findViewById(R.id.tvNo);
        RVhistorylist = v.findViewById(R.id.RVhistorylist);

        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        RVhistorylist.setLayoutManager(mLayoutManager);

        svSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                paidAdapter.filter(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

/*
        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText.length() > 0) {
                    paidAdapter.filter(newText.toString());

                } else {

                    if (NetworkManager.isConnectToInternet(getActivity())) {
                        swipeRefreshLayout.setRefreshing(true);

                        getHistroy();

                    } else {
                        ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
                    }
                }
                return false;
            }
        });
*/

        getHistroy();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void getHistroy() {
        ProjectUtils.showProgressDialog(getActivity(), true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.GET_INVOICE_API, getparm(), getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();

                if (flag) {
                    tvNo.setVisibility(View.GONE);
                    RVhistorylist.setVisibility(View.VISIBLE);
                    rlSearch.setVisibility(View.VISIBLE);
                    try {
                        historyDTOList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<HistoryDTO>>() {
                        }.getType();
                        historyDTOList = (ArrayList<HistoryDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);
                        showData();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    tvNo.setVisibility(View.VISIBLE);
                    RVhistorylist.setVisibility(View.GONE);
                    rlSearch.setVisibility(View.GONE);
                }
            }
        });
    }

    public HashMap<String, String> getparm() {
        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.USER_ID, userDTO.getUser_id());
        parms.put(Consts.ROLE, "2");
        return parms;
    }

    public void showData() {
        historyDTOListPaid = new ArrayList<>();
        for (int i = 0; i < historyDTOList.size(); i++) {
            if (historyDTOList.get(i).getFlag().trim().equals("1")) {
                historyDTOListPaid.add(historyDTOList.get(i));
            } else {
            }
        }

        if (historyDTOListPaid.size() > 0) {
            tvNo.setVisibility(View.GONE);
            RVhistorylist.setVisibility(View.VISIBLE);
            rlSearch.setVisibility(View.VISIBLE);

            paidAdapter = new PaidAdapter(getActivity().getApplicationContext(), historyDTOListPaid, myInflater);
            RVhistorylist.setAdapter(paidAdapter);
        } else {
            tvNo.setVisibility(View.VISIBLE);
            RVhistorylist.setVisibility(View.GONE);
            rlSearch.setVisibility(View.GONE);
        }
    }


}

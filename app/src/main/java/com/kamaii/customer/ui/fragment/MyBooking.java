package com.kamaii.customer.ui.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.customer.DTO.UserBooking;
import com.kamaii.customer.DTO.UserDTO;
import com.kamaii.customer.R;
import com.kamaii.customer.api.apiClient;
import com.kamaii.customer.api.apiRest;
import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.network.NetworkManager;
import com.kamaii.customer.preferences.SharedPrefrence;
import com.kamaii.customer.ui.activity.BaseActivity;
import com.kamaii.customer.ui.adapter.AdapterCustomerBooking;
import com.kamaii.customer.utils.CustomTextViewBold;
import com.kamaii.customer.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.widget.Toast.LENGTH_LONG;

public class MyBooking extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private String TAG = NotificationActivity.class.getSimpleName();
    private RecyclerView rvBooking;
    private AdapterCustomerBooking adapterCustomerBooking;
    private ArrayList<UserBooking> userBookingList;
    private ArrayList<UserBooking> userBookingListSection;
    private ArrayList<UserBooking> userBookingListSection1;
    private LinearLayoutManager mLayoutManager;
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private CustomTextViewBold tvNo;
    private View view;
    private BaseActivity baseActivity;
    private SearchView svSearch;
    private RelativeLayout rlSearch;
    private SwipeRefreshLayout swipeRefreshLayout;
    ProgressDialog progressDialog;
    LocalBroadcastManager bm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_my_booking, container, false);
        prefrence = SharedPrefrence.getInstance(getActivity());

        getActivity().findViewById(R.id.ivLogo).setVisibility(View.GONE);
        getActivity().findViewById(R.id.customer_location_relative_header).setVisibility(View.GONE);

        baseActivity.headerNameTV.setText(getResources().getString(R.string.my_bookings));
        getActivity().findViewById(R.id.headerNameTV).setVisibility(View.VISIBLE);
        baseActivity.base_recyclerview.setVisibility(View.GONE);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        setUiAction(view);
        if (NetworkManager.isConnectToInternet(getActivity())) {
            getBooking();

        } else {
            ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
        }
        return view;
    }


    public void setUiAction(View v) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        swipeRefreshLayout = v.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        rlSearch = v.findViewById(R.id.rlSearch);
        svSearch = v.findViewById(R.id.svSearch);
        tvNo = v.findViewById(R.id.tvNo);
        rvBooking = v.findViewById(R.id.rvBooking);
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        rvBooking.setLayoutManager(mLayoutManager);

        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 0) {
                } else {
                }
                return false;
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();

        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        if (NetworkManager.isConnectToInternet(getActivity())) {
                                            getBooking();

                                        } else {
                                            ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
                                        }
                                    }
                                }
        );

    }

    @Override
    public void onRefresh() {
        getBooking();
    }

    public void getBooking() {

        prefrence = SharedPrefrence.getInstance(getActivity());
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        progressDialog.show();

        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.getbooking(userDTO.getUser_id());
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                swipeRefreshLayout.setRefreshing(false);
                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();

                        Log.e("tracking_btn_response12", "" + s);
                        JSONObject object = new JSONObject(s);

                        tvNo.setVisibility(View.GONE);
                        rvBooking.setVisibility(View.VISIBLE);
                        String message = object.getString("message");
                        int sstatus = object.getInt("status");
                        Log.e("ssstatus", "" + sstatus);
                        if (sstatus == 1) {

                            try {
                                userBookingList = new ArrayList<>();
                                Type getpetDTO = new TypeToken<List<UserBooking>>() {
                                }.getType();

                                userBookingList = (ArrayList<UserBooking>) new Gson().fromJson(object.getJSONArray("data").toString(), getpetDTO);
                                showData();

                            } catch (Exception e) {
                                Log.e("ssstatus_error", "" + sstatus);
                                e.printStackTrace();
                            }


                        } else if (sstatus == 3) {
                            swipeRefreshLayout.setRefreshing(false);
                            tvNo.setVisibility(View.VISIBLE);
                            rvBooking.setVisibility(View.GONE);
                        } else {
                            swipeRefreshLayout.setRefreshing(false);
                            tvNo.setVisibility(View.VISIBLE);
                            rvBooking.setVisibility(View.GONE);
                        }
                    } else {
                        Toast.makeText(getActivity(), "Try again. Server is not responding.", LENGTH_LONG).show();

                        progressDialog.dismiss();
                        swipeRefreshLayout.setRefreshing(false);
                        tvNo.setVisibility(View.VISIBLE);
                        rvBooking.setVisibility(View.GONE);
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
                swipeRefreshLayout.setRefreshing(false);
                tvNo.setVisibility(View.VISIBLE);
                rvBooking.setVisibility(View.GONE);
            }
        });

    }


    public HashMap<String, String> getparm() {
        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.USER_ID, userDTO.getUser_id());
        return parms;
    }

    public void showData() {
        Log.e("userBookingListOK", "" + userBookingList.get(0).getProduct());
        adapterCustomerBooking = new AdapterCustomerBooking(MyBooking.this, userBookingList, userDTO);
        rvBooking.setAdapter(adapterCustomerBooking);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        baseActivity = (BaseActivity) activity;
    }

    private BroadcastReceiver onJsonReceived = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                getBooking();
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        bm = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter actionReceiver = new IntentFilter();
        actionReceiver.addAction("nameOfTheActionShouldBeUniqueName");
        bm.registerReceiver(onJsonReceived, actionReceiver);
    }

    public void setSection() {
        HashMap<String, ArrayList<UserBooking>> has = new HashMap<>();
        userBookingListSection = new ArrayList<>();
        for (int i = 0; i < userBookingList.size(); i++) {
            if (has.containsKey(ProjectUtils.changeDateFormate1(userBookingList.get(i).getBooking_date()))) {
                userBookingListSection1 = new ArrayList<>();
                userBookingListSection1 = has.get(ProjectUtils.changeDateFormate1(userBookingList.get(i).getBooking_date()));
                userBookingListSection1.add(userBookingList.get(i));
                has.put(ProjectUtils.changeDateFormate1(userBookingList.get(i).getBooking_date()), userBookingListSection1);
            } else {
                userBookingListSection1 = new ArrayList<>();
                userBookingListSection1.add(userBookingList.get(i));
                has.put(ProjectUtils.changeDateFormate1(userBookingList.get(i).getBooking_date()), userBookingListSection1);
            }
        }

        for (String key : has.keySet()) {
            UserBooking userBooking = new UserBooking();
            userBooking.setSection(true);
            userBooking.setSection_name(key);
            userBookingListSection.add(userBooking);
            userBookingListSection.addAll(has.get(key));
        }
        showData();

    }
}

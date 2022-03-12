package com.kamaii.customer.ui.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.customer.DTO.NotificationDTO;
import com.kamaii.customer.DTO.UserDTO;
import com.kamaii.customer.R;
import com.kamaii.customer.api.apiClient;
import com.kamaii.customer.api.apiRest;
import com.kamaii.customer.https.HttpsRequest;
import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.interfacess.Helper;
import com.kamaii.customer.network.NetworkManager;
import com.kamaii.customer.preferences.SharedPrefrence;
import com.kamaii.customer.ui.activity.BaseActivity;
import com.kamaii.customer.ui.adapter.NotificationAdapter;
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

public class NotificationActivity extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private String TAG = NotificationActivity.class.getSimpleName();
    private RecyclerView RVnotification;
    private NotificationAdapter notificationAdapter;
    private ArrayList<NotificationDTO> notificationDTOlist;
    private LinearLayoutManager mLayoutManager;
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private CustomTextViewBold tvNo;
    private View view;
    private BaseActivity baseActivity;
    private SwipeRefreshLayout swipeRefreshLayout;
    ProgressDialog progressDialog;
    Button btnclear;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_notification, container, false);
        prefrence = SharedPrefrence.getInstance(getActivity());
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        getActivity().findViewById(R.id.ivLogo).setVisibility(View.GONE);
        getActivity().findViewById(R.id.customer_location_relative_header).setVisibility(View.GONE);

        baseActivity.headerNameTV.setText(getResources().getString(R.string.notification));
        getActivity().findViewById(R.id.headerNameTV).setVisibility(View.VISIBLE);
        baseActivity.base_recyclerview.setVisibility(View.GONE);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        setUiAction(view);
        return view;
    }

    public void setUiAction(View v) {
        swipeRefreshLayout = v.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        tvNo = v.findViewById(R.id.tvNo);
        RVnotification = v.findViewById(R.id.RVnotification);
        btnclear = v.findViewById(R.id.btnclear);

        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        RVnotification.setLayoutManager(mLayoutManager);
        if (NetworkManager.isConnectToInternet(getActivity())) {
            getNotification();

        } else {
            ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
        }

        btnclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearnotification();
            }
        });
    }

    public void clearnotification() {


        progressDialog.show();
        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.deleteNotification(userDTO.getUser_id());
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                Log.e("RES", response.message());
                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        JSONObject object = new JSONObject(s);


                        String message = object.getString("message");
                        int sstatus = object.getInt("status");

                        if (sstatus == 1) {

                            try {
                                getNotification();
                                Toast.makeText(getActivity(), message,
                                        LENGTH_LONG).show();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {
                            Toast.makeText(getActivity(), message,
                                    LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(getActivity(), "Try again. Server is not responding.",
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

                                        Log.e("Runnable", "FIRST");
                                        if (NetworkManager.isConnectToInternet(getActivity())) {
                                            swipeRefreshLayout.setRefreshing(true);

                                            getNotification();

                                        } else {
                                            ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
                                        }
                                    }
                                }
        );
    }

    @Override
    public void onRefresh() {
        Log.e("ONREFREST_Firls", "FIRS");
        getNotification();
    }

    public void getNotification() {
        progressDialog.show();
        new HttpsRequest(Consts.GET_NOTIFICATION_API, getparm(), getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                progressDialog.dismiss();
                if (flag) {
                    tvNo.setVisibility(View.GONE);
                    RVnotification.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setRefreshing(false);
                    try {
                        notificationDTOlist = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<NotificationDTO>>() {
                        }.getType();
                        notificationDTOlist = (ArrayList<NotificationDTO>) new Gson().fromJson(response.getJSONArray("my_notifications").toString(), getpetDTO);
                        showData();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    tvNo.setVisibility(View.VISIBLE);
                    RVnotification.setVisibility(View.GONE);
                }
            }
        });
    }

    public HashMap<String, String> getparm() {
        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.USER_ID, userDTO.getUser_id());
        return parms;
    }

    public void showData() {
        notificationAdapter = new NotificationAdapter(getActivity(), notificationDTOlist);
        RVnotification.setAdapter(notificationAdapter);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        baseActivity = (BaseActivity) activity;
    }
}

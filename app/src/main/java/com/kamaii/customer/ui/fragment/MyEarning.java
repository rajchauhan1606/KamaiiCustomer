package com.kamaii.customer.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.kamaii.customer.DTO.EarningDTO;
import com.kamaii.customer.DTO.UserDTO;
import com.kamaii.customer.R;
import com.kamaii.customer.https.HttpsRequest;
import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.interfacess.Helper;
import com.kamaii.customer.network.NetworkManager;
import com.kamaii.customer.preferences.SharedPrefrence;
import com.kamaii.customer.ui.activity.BaseActivity;
import com.kamaii.customer.utils.CustomTextViewBold;
import com.kamaii.customer.utils.ProjectUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyEarning extends Fragment {

    private View view;
    private String TAG = MyEarning.class.getSimpleName();
    private HashMap<String, String> paramsRequest = new HashMap<>();
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private BaseActivity baseActivity;
    List<String> list = new ArrayList<String>();
    private CustomTextViewBold tvreferfriend, tvearningcashback;
    EarningDTO earningDTO;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_earning, container, false);

        baseActivity.headerNameTV.setText(getResources().getString(R.string.my_earnings));
        baseActivity.base_recyclerview.setVisibility(View.GONE);

        prefrence = SharedPrefrence.getInstance(getActivity());
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        paramsRequest.put(Consts.USER_ID, userDTO.getUser_id());

        setUiAction(view);

        if (NetworkManager.isConnectToInternet(getActivity())) {
            getEarning();
        } else {
            ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
        }
        return view;
    }

    public void setUiAction(View v) {
        tvreferfriend = v.findViewById(R.id.tvreferfriend);
        tvearningcashback = v.findViewById(R.id.tvearningcashback);
    }


    @Override
    public void onResume() {
        super.onResume();
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

    public void showData() {

        tvreferfriend.setText(earningDTO.getTot_ref());
        tvearningcashback.setText(earningDTO.getReferral_earning());

    }

}

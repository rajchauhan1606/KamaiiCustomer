package com.kamaii.customer.ui.fragment;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.kamaii.customer.DTO.DiscountDTO;
import com.kamaii.customer.DTO.UserDTO;
import com.kamaii.customer.R;
import com.kamaii.customer.https.HttpsRequest;
import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.interfacess.Helper;
import com.kamaii.customer.network.NetworkManager;
import com.kamaii.customer.preferences.SharedPrefrence;
import com.kamaii.customer.ui.activity.BaseActivity;
import com.kamaii.customer.utils.CustomButton;
import com.kamaii.customer.utils.CustomTextView;
import com.kamaii.customer.utils.CustomTextViewBold;
import com.kamaii.customer.utils.ProjectUtils;

import org.json.JSONObject;

import java.util.HashMap;

public class GetDiscountActivity extends Fragment implements View.OnClickListener {

    private String TAG = GetDiscountActivity.class.getSimpleName();
    private CustomTextView CTVdescription;
    private CustomTextViewBold CTVBcode, CTVBcopy,tvcode1,Btncopy1,tvcode2,Btncopy2;
    private CustomButton CBinvitefriend;
    private HashMap<String, String> parms = new HashMap<>();
    private DiscountDTO discountDTO;
    private String code = "";
    private ClipboardManager myClipboard;
    private ClipData myClip;
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private View view;
    private BaseActivity baseActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_get_discount, container, false);

        prefrence = SharedPrefrence.getInstance(getActivity());

        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        myClipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        parms.put(Consts.USER_ID, userDTO.getUser_id());

        //    getActivity().findViewById(R.id.ivLogo).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.customer_location_relative_header).setVisibility(View.GONE);

        baseActivity.headerNameTV.setText(getResources().getString(R.string.get_discount));
        getActivity().findViewById(R.id.headerNameTV).setVisibility(View.VISIBLE);
       baseActivity.base_recyclerview.setVisibility(View.GONE);

        setUiAction(view);
        return view;
    }


    public void setUiAction(View v) {
        CTVdescription = v.findViewById(R.id.CTVdescription);
        CTVBcode = v.findViewById(R.id.CTVBcode);
        CTVBcopy = v.findViewById(R.id.CTVBcopy);
        CBinvitefriend = v.findViewById(R.id.CBinvitefriend);
        tvcode1 = v.findViewById(R.id.tvcode1);
        Btncopy1 = v.findViewById(R.id.Btncopy1);
        tvcode2 = v.findViewById(R.id.tvcode2);
        Btncopy2 = v.findViewById(R.id.Btncopy2);
        CTVBcopy.setOnClickListener(this);
        CBinvitefriend.setOnClickListener(this);

        Btncopy1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkManager.isConnectToInternet(getActivity())) {
                    String text;
                    text = tvcode1.getText().toString().trim();
                    myClip = ClipData.newPlainText("text", text);
                    myClipboard.setPrimaryClip(myClip);
                    ProjectUtils.showLong(getActivity(), getResources().getString(R.string.code_copy));
                } else {
                    ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
                }

            }
        });
        Btncopy2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkManager.isConnectToInternet(getActivity())) {
                    String text;
                    text = tvcode2.getText().toString().trim();
                    myClip = ClipData.newPlainText("text", text);
                    myClipboard.setPrimaryClip(myClip);
                    ProjectUtils.showLong(getActivity(), getResources().getString(R.string.code_copy));
                } else {
                    ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
                }

            }
        });
        if (NetworkManager.isConnectToInternet(getActivity())) {
            getCode();
        } else {
            ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.CTVBcopy:
                if (NetworkManager.isConnectToInternet(getActivity())) {
                    String text;
                    text = CTVBcode.getText().toString().trim();
                    myClip = ClipData.newPlainText("text", text);
                    myClipboard.setPrimaryClip(myClip);
                    ProjectUtils.showLong(getActivity(), getResources().getString(R.string.code_copy));
                } else {
                    ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
                }

                break;
            case R.id.CBinvitefriend:
                if (NetworkManager.isConnectToInternet(getActivity())) {
                    share();
                } else {
                    ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
                }
                break;

        }
    }

    public void getCode() {
        ProjectUtils.showProgressDialog(getActivity(), true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.GET_REFERRAL_CODE_API, parms, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {

                        discountDTO = new Gson().fromJson(response.getJSONObject("my_referral_code").toString(), DiscountDTO.class);
                        showData();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                }
            }
        });
    }

    public void showData() {
        CTVdescription.setText(discountDTO.getDescription());
        CTVBcode.setText(discountDTO.getCode());

        code = discountDTO.getCode();
    }

    public void share() {
        try {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.my_code) + " " + code);
            startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.app_name)));
        } catch (Exception e) {
            e.printStackTrace();
            ProjectUtils.showLong(getActivity(), getResources().getString(R.string.opps_share));
        }


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        baseActivity = (BaseActivity) activity;
    }


}

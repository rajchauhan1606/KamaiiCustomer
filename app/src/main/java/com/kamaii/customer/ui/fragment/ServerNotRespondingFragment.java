package com.kamaii.customer.ui.fragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kamaii.customer.R;
import com.kamaii.customer.ui.activity.BaseActivity;

public class ServerNotRespondingFragment extends Fragment {

     private BaseActivity baseActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
     baseActivity.base_recyclerview.setVisibility(View.GONE);
        baseActivity.ivmainsearchLayout.setVisibility(View.GONE);

        return inflater.inflate(R.layout.fragment_server_not_responding, container, false);
    }

     @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        baseActivity = (BaseActivity) activity;
    }
}
package com.kamaii.customer.ui.fragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kamaii.customer.R;
import com.kamaii.customer.network.NetworkManager;
import com.kamaii.customer.ui.activity.BaseActivity;


public class CheckInternetFragment extends Fragment {


    private static final String TAG = CheckInternetFragment.class.getSimpleName();
    private BaseActivity baseActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_check_internet, container, false);
        baseActivity.base_recyclerview.setVisibility(View.GONE);
        baseActivity.ivmainsearchLayout.setVisibility(View.GONE);
        getActivity().findViewById(R.id.ivLogo).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.headerNameTV).setVisibility(View.GONE);
        getActivity().findViewById(R.id.customer_location_relative_header).setVisibility(View.GONE);
        view.findViewById(R.id.retry_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!NetworkManager.isConnectToInternet(getActivity())) {
                    Fragment fragment = new CheckInternetFragment();
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                            android.R.anim.fade_out);
                    fragmentTransaction.replace(R.id.frame, fragment, TAG);
                    fragmentTransaction.commitAllowingStateLoss();

                } else {
                    Fragment fragment = new CategoryFragment();
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                            android.R.anim.fade_out);
                    fragmentTransaction.replace(R.id.frame, fragment, TAG);
                    fragmentTransaction.commitAllowingStateLoss();
                }
            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        baseActivity = (BaseActivity) activity;
    }
}
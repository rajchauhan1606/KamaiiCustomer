package com.kamaii.customer.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.kamaii.customer.R;
import com.kamaii.customer.ui.activity.BaseActivity;
import com.kamaii.customer.utils.CustomTextView;


public class Home extends Fragment implements View.OnClickListener{


    private View view;
    private BaseActivity baseActivity;
    private FragmentManager fragmentManager;
    private CategoryFragment categoryFragment = new CategoryFragment();
    private DiscoverFragment discoverFragment = new DiscoverFragment();
    private NotificationActivity notificationActivity = new NotificationActivity();
    private AppUpdateFragment appUpdateFragment = new AppUpdateFragment();
    private CustomTextView CTVdiscover, CTVnearby;
    FragmentTransaction fragmentTransaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        baseActivity.headerNameTV.setText(getResources().getString(R.string.fabartist));
        fragmentManager = getChildFragmentManager();
        CTVnearby = (CustomTextView) view.findViewById(R.id.CTVnearby);
        CTVdiscover = (CustomTextView) view.findViewById(R.id.CTVdiscover);

        CTVdiscover.setOnClickListener(this);
        CTVnearby.setOnClickListener(this);

        fragmentManager.beginTransaction().add(R.id.frame, categoryFragment).commit();

        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

        baseActivity.ivnotiication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.frame, notificationActivity).commit();
            }
        });

        baseActivity.ivaboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.frame, appUpdateFragment).commit();
            }
        });
        return view;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        baseActivity = (BaseActivity) activity;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.CTVdiscover:
                setSelected(true, false);
                fragmentManager.beginTransaction().replace(R.id.frame, categoryFragment).commit();
                break;
            case R.id.CTVnearby:
                setSelected(false, true);
                break;
        }

    }

    public void setSelected(boolean firstBTN, boolean secondBTN) {
        if (firstBTN) {
            CTVdiscover.setBackground(getResources().getDrawable(R.drawable.button_near_bg));
            CTVdiscover.setTextColor(getResources().getColor(R.color.gray));
            CTVnearby.setBackground(getResources().getDrawable(R.drawable.button_disc_bg));
            CTVnearby.setTextColor(getResources().getColor(R.color.white));
        }
        if (secondBTN) {
            CTVdiscover.setBackground(getResources().getDrawable(R.drawable.button_disc_bg));
            CTVdiscover.setTextColor(getResources().getColor(R.color.white));
            CTVnearby.setBackground(getResources().getDrawable(R.drawable.button_near_bg));
            CTVnearby.setTextColor(getResources().getColor(R.color.gray));


        }
        CTVdiscover.setSelected(firstBTN);
        CTVnearby.setSelected(secondBTN);
    }
}

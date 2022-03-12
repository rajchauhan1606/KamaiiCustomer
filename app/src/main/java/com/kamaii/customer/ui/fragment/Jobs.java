package com.kamaii.customer.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.customer.DTO.PostedJobDTO;
import com.kamaii.customer.DTO.UserDTO;
import com.kamaii.customer.R;
import com.kamaii.customer.https.HttpsRequest;
import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.interfacess.Helper;
import com.kamaii.customer.network.NetworkManager;
import com.kamaii.customer.preferences.SharedPrefrence;
import com.kamaii.customer.ui.activity.BaseActivity;
import com.kamaii.customer.ui.activity.PostJob;
import com.kamaii.customer.ui.adapter.JobsAdapter;
import com.kamaii.customer.utils.CustomTextViewBold;
import com.kamaii.customer.utils.ProjectUtils;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Jobs extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private String TAG = Jobs.class.getSimpleName();
    private RecyclerView RVhistorylist;
    private JobsAdapter jobsAdapter;
    private ArrayList<PostedJobDTO> postedJobDTOSList;
    private ArrayList<PostedJobDTO> postedJobDTOSList1;
    private ArrayList<PostedJobDTO> postedJobDTOSList2;
    private LinearLayoutManager mLayoutManager;
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private CustomTextViewBold tvNo;
    private View view;
    private BaseActivity baseActivity;
    private ImageView ivPost;
    private SearchView svSearch;
    private RelativeLayout rlSearch;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_jobs, container, false);

        getActivity().findViewById(R.id.ivLogo).setVisibility(View.GONE);
        getActivity().findViewById(R.id.customer_location_relative_header).setVisibility(View.GONE);

        baseActivity.headerNameTV.setText(getResources().getString(R.string.jobs));
        getActivity().findViewById(R.id.headerNameTV).setVisibility(View.VISIBLE);

        baseActivity.base_recyclerview.setVisibility(View.GONE);

        prefrence = SharedPrefrence.getInstance(getActivity());
        userDTO = prefrence.getParentUser(Consts.USER_DTO);

        setUiAction(view);
        return view;
    }

    public void setUiAction(View v) {
        rlSearch = v.findViewById(R.id.rlSearch);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
        svSearch = v.findViewById(R.id.svSearch);
        tvNo = v.findViewById(R.id.tvNo);
        RVhistorylist = v.findViewById(R.id.RVhistorylist);
        ivPost = v.findViewById(R.id.ivPost);

        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        RVhistorylist.setLayoutManager(mLayoutManager);

        ivPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkManager.isConnectToInternet(getActivity())) {
                    startActivity(new Intent(getActivity(), PostJob.class));
                } else {
                    ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
                }
            }
        });
        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText.length() > 0) {
                    jobsAdapter.filter(newText.toString());

                } else {


                }
                return false;
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        Log.e("Runnable", "FIRST");
                                        if (NetworkManager.isConnectToInternet(getActivity())) {
                                            swipeRefreshLayout.setRefreshing(true);

                                            getjobs();

                                        } else {
                                            ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
                                        }
                                    }
                                }
        );
    }

    public void getjobs() {
        ProjectUtils.showProgressDialog(getActivity(), true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.GET_ALL_JOB_USER_API, getparm(), getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                swipeRefreshLayout.setRefreshing(false);
                if (flag) {
                    tvNo.setVisibility(View.GONE);
                    RVhistorylist.setVisibility(View.VISIBLE);
                    rlSearch.setVisibility(View.VISIBLE);
                    try {

                        postedJobDTOSList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<PostedJobDTO>>() {
                        }.getType();
                        postedJobDTOSList = (ArrayList<PostedJobDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);
                        showData();

                    } catch (Exception e) {
                        e.printStackTrace();
                        rlSearch.setVisibility(View.GONE);
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
        return parms;
    }

    public void showData() {

        jobsAdapter = new JobsAdapter(Jobs.this, postedJobDTOSList, userDTO);
        RVhistorylist.setAdapter(jobsAdapter);


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        baseActivity = (BaseActivity) activity;
    }

    @Override
    public void onRefresh() {
        Log.e("ONREFREST_Firls", "FIRS");
        getjobs();
    }

    public void setSection() {
        HashMap<String, ArrayList<PostedJobDTO>> has = new HashMap<>();
        postedJobDTOSList1 = new ArrayList<>();
        for (int i = 0; i < postedJobDTOSList.size(); i++) {


            if (has.containsKey(ProjectUtils.changeDateFormate1(postedJobDTOSList.get(i).getJob_date()))) {
                postedJobDTOSList2 = new ArrayList<>();
                postedJobDTOSList2 = has.get(ProjectUtils.changeDateFormate1(postedJobDTOSList.get(i).getJob_date()));
                postedJobDTOSList2.add(postedJobDTOSList.get(i));
                has.put(ProjectUtils.changeDateFormate1(postedJobDTOSList.get(i).getJob_date()), postedJobDTOSList2);


            } else {
                postedJobDTOSList2 = new ArrayList<>();
                postedJobDTOSList2.add(postedJobDTOSList.get(i));
                has.put(ProjectUtils.changeDateFormate1(postedJobDTOSList.get(i).getJob_date()), postedJobDTOSList2);
            }
        }

        for (String key : has.keySet()) {
            PostedJobDTO postedJobDTO = new PostedJobDTO();
            postedJobDTO.setSection(true);
            postedJobDTO.setSection_name(key);
            postedJobDTOSList1.add(postedJobDTO);
            postedJobDTOSList1.addAll(has.get(key));

        }


        showData();

    }

}

package com.kamaii.customer.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.customer.DTO.HistoryDTO;
import com.kamaii.customer.DTO.TicketDTO;
import com.kamaii.customer.DTO.UserDTO;
import com.kamaii.customer.R;
import com.kamaii.customer.https.HttpsRequest;
import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.interfacess.Helper;
import com.kamaii.customer.interfacess.OnSpinerItemClick;
import com.kamaii.customer.network.NetworkManager;
import com.kamaii.customer.preferences.SharedPrefrence;
import com.kamaii.customer.ui.activity.BaseActivity;
import com.kamaii.customer.ui.adapter.TicketAdapter;
import com.kamaii.customer.utils.CustomEditText;
import com.kamaii.customer.utils.CustomTextView;
import com.kamaii.customer.utils.CustomTextViewBold;
import com.kamaii.customer.utils.ProjectUtils;
import com.kamaii.customer.utils.SpinnerDialogInvoice;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Tickets extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private String TAG = Tickets.class.getSimpleName();
    private RecyclerView RVhistorylist;
    private TicketAdapter ticketAdapter;
    private ArrayList<TicketDTO> ticketDTOSList;
    private LinearLayoutManager mLayoutManager;
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private CustomTextViewBold tvNo;
    private View view;
    private BaseActivity baseActivity;
    private ImageView ivPost;
    private Dialog dialog;
    private CustomEditText etReason, etDescription;
    private CustomTextView tvCancel, tvAdd;
    private HashMap<String, String> parmsadd = new HashMap<>();
    private ArrayList<HistoryDTO> historyDTOList = new ArrayList<>();
    SpinnerDialogInvoice spinnerDialogInvoice;
    CustomTextViewBold tvcat;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_ticket, container, false);
        getActivity().findViewById(R.id.ivLogo).setVisibility(View.GONE);
        getActivity().findViewById(R.id.customer_location_relative_header).setVisibility(View.GONE);

        baseActivity.headerNameTV.setText(getResources().getString(R.string.support));
        getActivity().findViewById(R.id.headerNameTV).setVisibility(View.VISIBLE);
        baseActivity.base_recyclerview.setVisibility(View.GONE);

        prefrence = SharedPrefrence.getInstance(getActivity());
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        if (NetworkManager.isConnectToInternet(getActivity())) {

            getHistroy();

        } else {
            ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
        }
        setUiAction(view);
        return view;
    }

    public void getHistroy() {
        new HttpsRequest(Consts.GET_INVOICE_API, getparminvoice(), getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                historyDTOList = new ArrayList<>();
                swipeRefreshLayout.setRefreshing(false);
                if (flag) {


                    try {

                        Type getpetDTO = new TypeToken<List<HistoryDTO>>() {
                        }.getType();
                        historyDTOList = (ArrayList<HistoryDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);
                        spinnerDialogInvoice = new SpinnerDialogInvoice(getActivity(), historyDTOList, getResources().getString(R.string.select_invoice_id));// With 	Animation
                        spinnerDialogInvoice.bindOnSpinerListener(new OnSpinerItemClick() {
                            @Override
                            public void onClick(String item, String id, int position) {
                                tvcat.setText(item);

                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    swipeRefreshLayout.setRefreshing(false);

                }
            }
        });
    }

    public HashMap<String, String> getparminvoice() {
        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.USER_ID, userDTO.getUser_id());
        parms.put(Consts.ROLE, "2");
        return parms;
    }

    public void setUiAction(View v) {
        tvNo = v.findViewById(R.id.tvNo);
        RVhistorylist = v.findViewById(R.id.RVhistorylist);
        ivPost = v.findViewById(R.id.ivPost);

        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        RVhistorylist.setLayoutManager(mLayoutManager);

        ivPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkManager.isConnectToInternet(getActivity())) {
                    dialogshow();
                } else {
                    ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
                }
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        if (NetworkManager.isConnectToInternet(getActivity())) {
            getTicket();

        } else {
            ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
        }


        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        Log.e("Runnable", "FIRST");
                                        if (NetworkManager.isConnectToInternet(getActivity())) {
                                            swipeRefreshLayout.setRefreshing(true);

                                            getHistroy();

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
        getHistroy();
    }

    public void getTicket() {
        new HttpsRequest(Consts.GET_MY_TICKET_API, getparm(), getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                swipeRefreshLayout.setRefreshing(false);
                if (flag) {
                    tvNo.setVisibility(View.GONE);
                    RVhistorylist.setVisibility(View.VISIBLE);
                    try {
                        ticketDTOSList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<TicketDTO>>() {
                        }.getType();
                        ticketDTOSList = (ArrayList<TicketDTO>) new Gson().fromJson(response.getJSONArray("my_ticket").toString(), getpetDTO);
                        showData();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    tvNo.setVisibility(View.VISIBLE);
                    RVhistorylist.setVisibility(View.GONE);
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
        ticketAdapter = new TicketAdapter(Tickets.this, ticketDTOSList, userDTO);
        RVhistorylist.setAdapter(ticketAdapter);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        baseActivity = (BaseActivity) activity;
    }


    public void dialogshow() {
        dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dailog_add_ticket);

        etReason = (CustomEditText) dialog.findViewById(R.id.etReason);
        tvCancel = (CustomTextView) dialog.findViewById(R.id.tvCancel);
        tvAdd = (CustomTextView) dialog.findViewById(R.id.tvAdd);
        etDescription = (CustomEditText) dialog.findViewById(R.id.etDescription);
        tvcat = dialog.findViewById(R.id.tvcat);

        tvcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (historyDTOList.size() != 0) {
                    spinnerDialogInvoice.showSpinerDialog();
                } else {
                    ProjectUtils.showLong(getActivity(), getResources().getString(R.string.no_invoice_found));
                }
            }
        });
        dialog.show();
        dialog.setCancelable(false);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });

    }


    public void submitForm() {

        String cat = tvcat.getText().toString().trim();

        if (cat.equalsIgnoreCase(getResources().getString(R.string.select_invoice_id))) {
            Toast.makeText(getActivity(), getResources().getString(R.string.val_iid), Toast.LENGTH_SHORT).show();
        } else if (!validateReason()) {
            return;
        } else if (!validateDescription()) {
            return;
        } else {
            addTicket();

        }
    }

    public boolean validateReason() {
        if (etReason.getText().toString().trim().equalsIgnoreCase("")) {
            etReason.setError(getResources().getString(R.string.val_title));
            etReason.requestFocus();
            return false;
        } else {
            etReason.setError(null);
            etReason.clearFocus();
            return true;
        }
    }

    public boolean validateDescription() {
        if (etDescription.getText().toString().trim().equalsIgnoreCase("")) {
            etDescription.setError(getResources().getString(R.string.val_description));
            etDescription.requestFocus();
            return false;
        } else {
            etDescription.setError(null);
            etDescription.clearFocus();
            return true;
        }
    }

    public void addTicket() {
        parmsadd.put(Consts.DESCRIPTION, ProjectUtils.getEditTextValue(etDescription));
        parmsadd.put(Consts.REASON, ProjectUtils.getEditTextValue(etReason));
        parmsadd.put(Consts.USER_ID, userDTO.getUser_id());
        parmsadd.put(Consts.INVOICEID, tvcat.getText().toString());
        ProjectUtils.showProgressDialog(getActivity(), false, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.GENERATE_TICKET_API, parmsadd, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    dialog.dismiss();
                    ProjectUtils.showToast(getActivity(), msg);
                    getTicket();
                } else {
                    ProjectUtils.showToast(getActivity(), msg);
                }
            }
        });
    }
}

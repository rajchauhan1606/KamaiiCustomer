package com.kamaii.customer.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.kamaii.customer.DTO.TrackingData;
import com.kamaii.customer.DTO.UserDTO;
import com.kamaii.customer.R;
import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.preferences.SharedPrefrence;
import com.kamaii.customer.utils.CustomTextView;
import com.kamaii.customer.utils.CustomTextViewBold;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.bclogic.pulsator4droid.library.PulsatorLayout;

public class TrackingDetailDialog extends BottomSheetDialogFragment {
    private int BookingFlag = 0;
    private int PaymentType = 0;

    private int CancelFlag = 1;
    private int CallFlag = 2;

    boolean isStartBooking = false;

    @BindView(R.id.llSt)
    LinearLayout llSt;

    @BindView(R.id.laycallcancel)
    LinearLayout laycallcancel;

    @BindView(R.id.laycall)
    LinearLayout laycall;

    @BindView(R.id.laycancel)
    LinearLayout laycancel;

    @BindView(R.id.layshare)
    LinearLayout layshare;

    @BindView(R.id.btnshare)
    LinearLayout btnshare;

    @BindView(R.id.llTime)
    LinearLayout llTime;

    @BindView(R.id.imgone)
    ImageView imgone;

    @BindView(R.id.imgtwo)
    ImageView imgtwo;

    @BindView(R.id.imgvendonrimage)
    ImageView imgvendonrimage;

    @BindView(R.id.etAddressSelectsource)
    CustomTextView etAddressSelectsource;

    @BindView(R.id.etAddressSelectdesti)
    CustomTextView etAddressSelectdesti;

    @BindView(R.id.txtservicename)
    CustomTextViewBold txtservicename;

    @BindView(R.id.totalamount)
    CustomTextViewBold txttotalamount;

    @BindView(R.id.txtptype)
    CustomTextViewBold txtptype;

    @BindView(R.id.txtptypemsg)
    CustomTextViewBold txtptypemsg;

    @BindView(R.id.tvStatus)
    CustomTextViewBold tvStatus;

    @BindView(R.id.txtvechile)
    CustomTextView txtvechile;

    @BindView(R.id.ridername)
    CustomTextView rider_name;

    @BindView(R.id.riderrating)
    CustomTextView riderrating;

    @BindView(R.id.txtvendorname)
    CustomTextView txtvendorname;


    DialogListener dialogListener;

    TrackingData liveTrackingData;

    PulsatorLayout pulsator;
    RelativeLayout relanimation;
    private UserDTO userDTO;
    SharedPrefrence prefrence;
    private BottomSheetBehavior mBottomSheetBehavior;

    public static TrackingDetailDialog newInstance() {
        return new TrackingDetailDialog();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_tracking_detail_dialog, container, false);
        ButterKnife.bind(this, view);
        prefrence = SharedPrefrence.getInstance(getActivity());
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        setCancelable(false);

        setViewData();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
                setupFullHeight(bottomSheetDialog);
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }

    private void setupFullHeight(BottomSheetDialog bottomSheetDialog) {
        FrameLayout bottomSheet = (FrameLayout) bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();

        int windowHeight = getWindowHeight();
        if (layoutParams != null) {
            layoutParams.height = windowHeight;
        }
        behavior.setHideable(false);
        behavior.setPeekHeight(300);
        bottomSheet.setLayoutParams(layoutParams);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private int getWindowHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public void setData(TrackingData trackingData, RelativeLayout relativeLayout, PulsatorLayout pulsatorLayout) {
        relanimation = relativeLayout;
        pulsator = pulsatorLayout;
        liveTrackingData = trackingData;


    }

    public void setViewData() {
        txtservicename.setText(liveTrackingData.getProductName());
        txtvendorname.setText(liveTrackingData.getVendorName());
        txtvechile.setText(liveTrackingData.getVehicleNumber());
        rider_name.setText(liveTrackingData.getRider_name());

        Glide.with(getActivity()).
                load(liveTrackingData.getVendorImage())
                .placeholder(R.drawable.dummyuser_image)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgvendonrimage);

        etAddressSelectsource.setText(liveTrackingData.getSourceAddress());

        txttotalamount.setText("Total Fare" + " " + liveTrackingData.getTotalAmount() + " " + "Rs");

        if (!liveTrackingData.getDestinationAddress().trim().isEmpty()) {
            etAddressSelectdesti.setText(liveTrackingData.getDestinationAddress());

        } else {
            etAddressSelectdesti.setText(liveTrackingData.getSourceAddress());
        }

        if (liveTrackingData.getPaymentType() == 0) {
            txtptype.setText("Online Payment");
            txtptypemsg.setText("Relax when the ride ends");
        } else if (liveTrackingData.getPaymentType() == 1) {
            txtptype.setText("Cash");
            txtptypemsg.setText("Pay when the ride ends");
        } else if (liveTrackingData.getPaymentType() == 2) {
            txtptype.setText("Wallet Payment");
            txtptypemsg.setText("Relax when the ride ends");
        } else {
            txtptype.setText("");
            txtptypemsg.setText("");
        }
        if (liveTrackingData.getBookingFlag() == 0) {

            relanimation.setVisibility(View.VISIBLE);
            tvStatus.setText(getActivity().getResources().getString(R.string.waiting_artist));
            laycallcancel.setVisibility(View.VISIBLE);
            layshare.setVisibility(View.GONE);
            laycall.setVisibility(View.GONE);

        } else if (liveTrackingData.getBookingFlag() == 1) {

            laycallcancel.setVisibility(View.VISIBLE);
            laycall.setVisibility(View.VISIBLE);
            relanimation.setVisibility(View.GONE);
            layshare.setVisibility(View.GONE);
            tvStatus.setText(getActivity().getResources().getString(R.string.request_acc));
        } else if (liveTrackingData.getBookingFlag() == 2) {

            laycallcancel.setVisibility(View.GONE);
            laycall.setVisibility(View.GONE);
            tvStatus.setText("");
            layshare.setVisibility(View.GONE);

        } else if (liveTrackingData.getBookingFlag() == 3) {
            isStartBooking = true;
            relanimation.setVisibility(View.GONE);
            laycallcancel.setVisibility(View.GONE);
            laycall.setVisibility(View.GONE);
            tvStatus.setText("Your booking in progress");
            layshare.setVisibility(View.VISIBLE);
        }

        btnshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogListener.onClickView(CallFlag, liveTrackingData);
            }
        });
        laycall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!liveTrackingData.getMobileNumber().trim().isEmpty()) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + liveTrackingData.getMobileNumber()));
                    getActivity().startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "Contact number is not available", Toast.LENGTH_LONG).show();
                }

            }
        });


        laycancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogListener.onClickView(CancelFlag, liveTrackingData);
            }
        });
    }

    public void setvisiblityView() {
        laycallcancel.setVisibility(View.GONE);
    }

    public void setDialogListener(DialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    public interface DialogListener {

        void onClickView(int flag, TrackingData track);
    }
}

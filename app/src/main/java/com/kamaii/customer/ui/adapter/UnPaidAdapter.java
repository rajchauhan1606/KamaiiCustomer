package com.kamaii.customer.ui.adapter;

/**
 * Created by WebKnight Infosystem on 01/01/19.
 */

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kamaii.customer.DTO.HistoryDTO;
import com.kamaii.customer.DTO.UserDTO;
import com.kamaii.customer.R;
import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.preferences.SharedPrefrence;
import com.kamaii.customer.ui.activity.BaseActivity;
import com.kamaii.customer.ui.activity.PaymentProActivity;
import com.kamaii.customer.ui.activity.ViewInvoice;
import com.kamaii.customer.ui.fragment.UnPaidFrag;
import com.kamaii.customer.utils.CustomTextView;
import com.kamaii.customer.utils.CustomTextViewBold;
import com.kamaii.customer.utils.ProjectUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class UnPaidAdapter extends RecyclerView.Adapter<UnPaidAdapter.MyViewHolder> {

    private Context mContext;
    private UnPaidFrag unPaidFrag;
    private ArrayList<HistoryDTO> objects = null;
    ArrayList<HistoryDTO> historyDTOList;
    private UserDTO userDTO;
    private SharedPrefrence prefrence;
    private LayoutInflater inflater;
    public UnPaidAdapter(UnPaidFrag unPaidFrag, ArrayList<HistoryDTO> objects, UserDTO userDTO, LayoutInflater inflater) {
        this.unPaidFrag = unPaidFrag;
        this.mContext = unPaidFrag.getActivity();
        this.objects = objects;
        this.userDTO = userDTO;
        this.historyDTOList = new ArrayList<HistoryDTO>();
        this.historyDTOList.addAll(objects);
        this.inflater = inflater;
        prefrence = SharedPrefrence.getInstance(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater
                .inflate(R.layout.adapter_unpaid, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        holder.CTVBservice.setText(mContext.getResources().getString(R.string.service)+" " + objects.get(position).getInvoice_id());
        try{
            holder.CTVdate.setText(ProjectUtils.convertTimestampDateToTime(ProjectUtils.correctTimestamp(Long.parseLong(objects.get(position).getCreated_at()))));

        }catch (Exception e){
            e.printStackTrace();
        }


        holder.CTVprice.setText(objects.get(position).getCurrency_type() + objects.get(position).getFinal_amount());
        holder.CTVServicetype.setText(objects.get(position).getCategoryName());
        holder.CTVname.setText(ProjectUtils.getFirstLetterCapital(objects.get(position).getArtistName()));

        Glide.with(mContext).
                load(objects.get(position).getArtistImage())
                .placeholder(R.drawable.dummyuser_image)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.IVprofile);

        if (objects.get(position).getFlag().equalsIgnoreCase("0")) {
            holder.llPay.setVisibility(View.VISIBLE);
            holder.tvStatus.setText(mContext.getResources().getString(R.string.unpaid));
            holder.llStatus.setBackground(mContext.getResources().getDrawable(R.drawable.rectangle_red));
        } else if (objects.get(position).getFlag().equalsIgnoreCase("1")) {
            holder.llPay.setVisibility(View.GONE);
            holder.tvStatus.setText(mContext.getResources().getString(R.string.paid));
            holder.llStatus.setBackground(mContext.getResources().getDrawable(R.drawable.rectangle_green));
        }
        holder.llPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(mContext, PaymentProActivity.class);
                in.putExtra(Consts.HISTORY_DTO, objects.get(position));
                in.putExtra(Consts.CATEGORY_ID, historyDTOList.get(position).getProduct().get(0).getCategory_id());
                mContext.startActivity(in);

            }
        });

        holder.tvView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(mContext, ViewInvoice.class);
                in.putExtra(Consts.HISTORY_DTO, objects.get(position));
                in.putExtra(Consts.CATEGORY_ID, historyDTOList.get(position).getProduct().get(0).getCategory_id());
                mContext.startActivity(in);

            }
        });

        SimpleDateFormat sdf = new SimpleDateFormat("mm.ss");

        try {
            Date dt = sdf.parse(objects.get(position).getWorking_min());
            sdf = new SimpleDateFormat("HH:mm:ss");

            holder.CTVTime.setText(mContext.getResources().getString(R.string.duration)+" " + sdf.format(dt));

        } catch (Exception e) {
            e.printStackTrace();
        }

        if(checkarss(historyDTOList.get(position).getProduct().get(0).getCategory_id())){
            holder.txtpay.setText("Rating Now");
        }else {
            holder.txtpay.setText(mContext.getResources().getString(R.string.cash_payment1));
        }
    }
    public   boolean checkarss(String catid){

        boolean value=false;
        for(int i = 0; i< BaseActivity.addressModelArrayList.size(); i++) {
            if (BaseActivity.addressModelArrayList.get(i).getCat_id().equalsIgnoreCase(catid)) {
                value= true;
                break;
            } else {
                value= false;

            }

        }
        return value;
    }
    @Override
    public int getItemCount() {

        return objects.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public CustomTextViewBold CTVBservice,CTVname,txtpay;
        public CustomTextView CTVprice, CTVdate, CTVServicetype, CTVwork,  tvStatus,CTVTime,tvView;
        public CircleImageView IVprofile;
        public LinearLayout llStatus, llPay;

        public MyViewHolder(View view) {
            super(view);
            llStatus = view.findViewById(R.id.llStatus);
            llPay = view.findViewById(R.id.llPay);
            tvStatus = view.findViewById(R.id.tvStatus);
            CTVBservice = view.findViewById(R.id.CTVBservice);
            CTVdate = view.findViewById(R.id.CTVdate);
            CTVprice = view.findViewById(R.id.CTVprice);
            CTVTime = view.findViewById(R.id.CTVTime);
            CTVServicetype = view.findViewById(R.id.CTVServicetype);
            CTVwork = view.findViewById(R.id.CTVwork);
            tvView = view.findViewById(R.id.tvView);
            CTVname = view.findViewById(R.id.CTVname);
            IVprofile = view.findViewById(R.id.IVprofile);
            txtpay = view.findViewById(R.id.txtpay);


        }
    }
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        objects.clear();
        if (charText.length() == 0) {
            objects.addAll(historyDTOList);
        } else {
            for (HistoryDTO historyDTO : historyDTOList) {
                if (historyDTO.getInvoice_id().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    objects.add(historyDTO);
                }
            }
        }
        notifyDataSetChanged();
    }
}
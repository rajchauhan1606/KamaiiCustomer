package com.kamaii.customer.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kamaii.customer.R;
import com.kamaii.customer.interfacess.OnBottomSelectedItemListener;
import com.kamaii.customer.ui.models.RecentAddress;
import com.kamaii.customer.utils.CustomTextView;
import com.kamaii.customer.utils.CustomTextViewBold;

import java.util.ArrayList;

/**
 * Created by WebKnight Infosystem on 01/01/19.
 */

public class RecentAddressAdapter extends RecyclerView.Adapter<RecentAddressAdapter.MyViewHolder> {

    LayoutInflater mLayoutInflater;
    private ArrayList<RecentAddress> recentAddressArrayList;
    private Context context;
    private OnBottomSelectedItemListener onSelectedItemListener;

    public RecentAddressAdapter(Context context, ArrayList<RecentAddress> recentAddressArrayList) {
        this.context = context;
        this.recentAddressArrayList = recentAddressArrayList;
        Log.e("recentAddressArrayList","adapter"+recentAddressArrayList.size());
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void update(ArrayList<RecentAddress> recentAddressArrayList) {
        findLimit(recentAddressArrayList);
        Log.e("recentAddressArrayList","Activity update"+recentAddressArrayList.get(0).getAddress());
        notifyDataSetChanged();
    }

    public void setOnSelectedItemListener(OnBottomSelectedItemListener onSelectedItemListener) {
        this.onSelectedItemListener = onSelectedItemListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_address_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txt_recent_address.setText(recentAddressArrayList.get(position).getAddress());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectedItemListener.setOnClick(position, recentAddressArrayList, position);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return recentAddressArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public CustomTextViewBold txt_recent_address;

        public MyViewHolder(View view) {
            super(view);

            txt_recent_address = (CustomTextViewBold) itemView.findViewById(R.id.txt_recent_address);
        }
    }

    void findLimit(ArrayList<RecentAddress> recentAddressArrayList) {
        int i = 0;
        for (RecentAddress recentAddress : recentAddressArrayList) {
            if (i < 3) {
                this.recentAddressArrayList.add(recentAddress);
                Log.e("recentAddressArrayList","adapter1"+recentAddressArrayList.size());

            }
            i++;
        }
    }
}
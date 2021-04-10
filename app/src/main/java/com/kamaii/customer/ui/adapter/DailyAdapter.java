package com.kamaii.customer.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kamaii.customer.R;
import com.kamaii.customer.interfacess.OnDataErrorListener;
import com.kamaii.customer.interfacess.OnSelectedItemListener;
import com.kamaii.customer.ui.models.DailyModel;

import java.util.ArrayList;

public class DailyAdapter extends RecyclerView.Adapter<DailyAdapter.ItemViewHolder> {
    Context context;
    private ArrayList<DailyModel> list;
    private OnSelectedItemListener onItemListener;

    private OnDataErrorListener dataErrorListener;

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView text_view_item_time, text_view_item_distance;
        LinearLayout layall;

        public ItemViewHolder(View view) {
            super(view);
            this.text_view_item_distance = view.findViewById(R.id.text_view_item_distance);
            this.text_view_item_time = (TextView) view.findViewById(R.id.text_view_item_time);
            this.layall = view.findViewById(R.id.layall);
        }
    }

    public DailyAdapter(Activity activity, ArrayList<DailyModel> list, OnSelectedItemListener onItemListener) {
        this.context = activity;
        this.list = list;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_daily, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder itemViewHolder, int i) {
        final int pos = i;
        itemViewHolder.text_view_item_time.setText(list.get(pos).getTime());
        itemViewHolder.text_view_item_distance.setText(list.get(pos).getDisttance());


        if (list.get(pos).getIsselected().equalsIgnoreCase("true")) {
            itemViewHolder.layall.setBackground(context.getDrawable(R.drawable.button_pressed_55999_blue));
        } else {
            itemViewHolder.layall.setBackground(context.getDrawable(R.drawable.button_pressed_55999));
        }


        itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setIsselected("false");
                }
                list.get(pos).setIsselected("true");

                notifyDataSetChanged();
                onItemListener.setOnClick(list.get(pos).getIsselected(), pos);
            }
        });

    }

    public void setDataErrorListener(OnDataErrorListener dataErrorListener) {
        this.dataErrorListener = dataErrorListener;
    }

    public long getItemId(int i) {
        return (long) i;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}


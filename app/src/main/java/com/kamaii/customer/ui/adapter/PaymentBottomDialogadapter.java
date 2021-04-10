package com.kamaii.customer.ui.adapter;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kamaii.customer.R;
import com.kamaii.customer.utils.CustomTextView;
import com.kamaii.customer.utils.CustomTextViewBold;

import java.util.List;

public class PaymentBottomDialogadapter extends RecyclerView.Adapter<PaymentBottomDialogadapter.BottomDialogViewHolder> {

    Context context;
    List<String> arraylist;
    public String item = "";
    String amt = "";
    String ptype = "";
    int pos = 0;

    public PaymentBottomDialogadapter(Context context, List<String> arraylist, String amt, String ptype,String pos) {
        this.context = context;
        this.arraylist = arraylist;
        this.amt = amt;
        this.ptype = ptype;
        this.pos=Integer.parseInt(pos);

    }

    @NonNull
    @Override
    public BottomDialogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_payment_dialog_recycler_layout, parent, false);
        return new BottomDialogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BottomDialogViewHolder holder, int position) {


        holder.payment_dialog_radio.setClickable(false);
        holder.payment_type.setText(arraylist.get(position));

        if (arraylist.get(position).equals("Online Payment")) {
            holder.payment_type_description.setText("2% extra charges");
        } else {
            holder.payment_type_description.setText("0% extra charge");
        }

        if (position == 0) {
            holder.tmp_dialog_txtview.setVisibility(View.GONE);
            holder.payment_dialog_imageview.setBackground(context.getResources().getDrawable(R.drawable.ic_cash));
        }

        if (arraylist.get(position).equalsIgnoreCase("Online Payment")) {

            holder.payment_dialog_imageview.setBackground(context.getResources().getDrawable(R.drawable.ic_online_payment));

        }
        if (arraylist.get(position).equalsIgnoreCase("Kamaii Wallet")) {
            holder.wallet_amount.setVisibility(View.VISIBLE);
            holder.wallet_amount.setText("(" + Html.fromHtml("&#x20B9;" + amt + ")"));
            holder.payment_dialog_imageview.setBackground(context.getResources().getDrawable(R.drawable.ic_kamaii_wallet));
        }
        holder.child_dialog_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos = position;
                notifyDataSetChanged();
             }
        });

        if (pos == position) {
            holder.payment_dialog_radio.setChecked(true);
            item = arraylist.get(pos);
        } else {
            holder.payment_dialog_radio.setChecked(false);
        }
        Log.d("TAG pos dhaval ", "onBindViewHolder: " + pos);
    }

    @Override
    public int getItemCount() {
        return arraylist.size();
    }

    class BottomDialogViewHolder extends RecyclerView.ViewHolder {

        CustomTextViewBold payment_type, wallet_amount;
        RelativeLayout child_dialog_rel;
        CustomTextView payment_type_description;
        ImageView payment_dialog_imageview;
        RadioButton payment_dialog_radio;
        TextView tmp_dialog_txtview;

        public BottomDialogViewHolder(@NonNull View itemView) {
            super(itemView);

            tmp_dialog_txtview = itemView.findViewById(R.id.tmp_dialog_txtview);
            payment_type = itemView.findViewById(R.id.payment_type);
            child_dialog_rel = itemView.findViewById(R.id.child_dialog_rel);
            wallet_amount = itemView.findViewById(R.id.wallet_amount);
            payment_type_description = itemView.findViewById(R.id.payment_type_description);
            payment_dialog_imageview = itemView.findViewById(R.id.payment_dialog_imageview);
            payment_dialog_radio = itemView.findViewById(R.id.payment_dialog_radio);
        }
    }
}

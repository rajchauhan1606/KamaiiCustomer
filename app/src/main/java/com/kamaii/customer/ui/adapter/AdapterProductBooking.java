package com.kamaii.customer.ui.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kamaii.customer.DTO.ProductDTO;
import com.kamaii.customer.R;
import com.kamaii.customer.ui.fragment.MyBooking;
import com.kamaii.customer.utils.CustomTextView;
import com.kamaii.customer.utils.CustomTextViewBold;

import java.util.ArrayList;
import java.util.HashMap;


public class AdapterProductBooking extends RecyclerView.Adapter<AdapterProductBooking.MyViewHolder> {

    private LayoutInflater mLayoutInflater;
    private MyBooking myBooking;
    private Context context;
    private ArrayList<ProductDTO> productDTOList;
    private int po;
    private String TAG = MyBooking.class.getSimpleName();
    private HashMap<String, String> params = new HashMap<>();
    Boolean isCheck = true;
    String cating = "";

    public AdapterProductBooking(Context myBooking, ArrayList<ProductDTO> productDTOList, String cating) {

        context = myBooking;
        this.productDTOList = productDTOList;
        this.cating = cating;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_product_list, parent, false);

        return new MyViewHolder(itemView);
    }

    public void NotifyAll(ArrayList<ProductDTO> productDTOList) {
        this.productDTOList = productDTOList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final int pos = position;
        po = pos;
        holder.txtservicename.setText(productDTOList.get(pos).getProduct_name());
        if (pos == 0) {
            holder.toping.setVisibility(View.GONE);
        } else {
            holder.toping.setVisibility(View.VISIBLE);
        }
        if (productDTOList.get(pos).getVehicle_number().isEmpty()) {
            String next = "Qty: " + "<font color='#e0215a'>" + productDTOList.get(pos).getQty() + "</font>" + "&nbsp;&nbsp;&nbsp;" + productDTOList.get(pos).getDescriptionType();
            holder.txtvechile.setText(Html.fromHtml(next));
            holder.pricing.setText(" ₹" + productDTOList.get(pos).getPrice());
            holder.finalproducttext.setText(Html.fromHtml("&#x20B9;" +productDTOList.get(pos).getStrike_price()));
            //tvDiscountPrice.setText(Html.fromHtml("&#x20B9;" + productDTOList.get(position).getDiscounted_price()));
            holder.finalproducttext.setPaintFlags(holder.finalproducttext.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        } else {
            holder.txtvechile.setText(productDTOList.get(pos).getVehicle_number());
        }

        Log.d(TAG, "onResponse: dhaval " + productDTOList.get(0).getProduct_image());

        if (cating.equals("0")) {
            holder.hiding.setVisibility(View.GONE);
        } else {
            Glide.with(context).
                    load(productDTOList.get(pos).getProduct_image())
                    .placeholder(R.drawable.single_logo)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.tvProductImage);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return productDTOList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {


        public CustomTextViewBold txtservicename;
        public CustomTextViewBold txtvechile;
        public CustomTextViewBold pricing;
        public CustomTextView finalproducttext;
        public ImageView tvProductImage;
        public TextView toping;
        public RelativeLayout hiding;

        public MyViewHolder(View view) {
            super(view);
            txtservicename = (CustomTextViewBold) view.findViewById(R.id.txtservicename);
            toping = (TextView) view.findViewById(R.id.toping);
            txtvechile = (CustomTextViewBold) view.findViewById(R.id.txtvechile);
            pricing = (CustomTextViewBold) view.findViewById(R.id.pricing);
            finalproducttext = (CustomTextView) view.findViewById(R.id.finalproducttext);
            tvProductImage = (ImageView) view.findViewById(R.id.artistVehicleImageView);
            hiding = (RelativeLayout) view.findViewById(R.id.hiding);
        }
    }
}

package com.kamaii.customer.ui.adapter;


import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kamaii.customer.DTO.ProductDTO;
import com.kamaii.customer.R;
import com.kamaii.customer.ui.activity.BaseActivity;
import com.kamaii.customer.utils.CustomTextView;
import com.kamaii.customer.utils.CustomTextViewBold;

import java.util.ArrayList;


public class AdapterInvoiceService extends RecyclerView.Adapter<AdapterInvoiceService.MyViewHolder> {

    private LayoutInflater mLayoutInflater;
    private Context context;
    private ArrayList<ProductDTO> productDTOArrayList, productDTOArrayListtemp;
    String locationstatus = "";

    Boolean isCheck = true;

    public AdapterInvoiceService(Context context, ArrayList<ProductDTO> productDTOArrayList, String locationstatus) {
        this.context = context;
        this.productDTOArrayList = productDTOArrayList;
        this.locationstatus = locationstatus;
        productDTOArrayListtemp = isused();
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_cart_booking, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        if (position == 0) {
            holder.toping.setVisibility(View.GONE);
        }
        if (productDTOArrayListtemp.get(position).getVehicle_number().equalsIgnoreCase("")) {
            holder.tvvehiclenumber.setVisibility(View.GONE);
        } else {
            holder.tvvehiclenumber.setVisibility(View.VISIBLE);
            holder.tvvehiclenumber.setText(productDTOArrayListtemp.get(position).getVehicle_number());
        }

        if (!productDTOArrayListtemp.get(position).getQuantitydays().equalsIgnoreCase("")) {

            if (productDTOArrayListtemp.get(position).getQuantitydays().equalsIgnoreCase("0")) {
                holder.tvquantitydays.setText("Qty");
            } else {
                holder.tvquantitydays.setText("KM");
            }
        }

        if (productDTOArrayListtemp.get(position).getSub_category_id().equals("242") || productDTOArrayListtemp.get(position).getSub_category_id().equals("434")) {
            holder.qty_tit.setVisibility(View.GONE);
            holder.qty_final.setVisibility(View.GONE);
            holder.descriptionType.setVisibility(View.GONE);

        } else if (productDTOArrayListtemp.get(position).getSub_category_id().equals("453")) {
            holder.qty_tit.setVisibility(View.GONE);
            holder.qty_final.setVisibility(View.GONE);
            holder.descriptionType.setVisibility(View.GONE);
        } else {
            holder.qty_tit.setVisibility(View.VISIBLE);
            holder.qty_final.setVisibility(View.VISIBLE);
            holder.qty_final.setText(productDTOArrayListtemp.get(position).getQuantitydays());
            holder.descriptionType.setVisibility(View.VISIBLE);
            holder.descriptionType.setText(Html.fromHtml(productDTOArrayListtemp.get(position).getDescriptionType()));
        }

        Glide.with(context).load(productDTOArrayListtemp.get(position).getProduct_image()).placeholder(R.drawable.dafault_product).into(holder.ProductImg);

        if (!productDTOArrayListtemp.get(position).getMaxmiumvalue().equalsIgnoreCase("")) {
            holder.tvquantity.setText(productDTOArrayListtemp.get(position).getMaxmiumvalue());
        }
        if (!productDTOArrayListtemp.get(position).getPrice().equalsIgnoreCase("")) {
            holder.tvPrice.setText("₹" + productDTOArrayListtemp.get(position).getChange_price());
            holder.tvPrices.setText(Html.fromHtml("₹" +productDTOArrayListtemp.get(position).getStrike_price()));
        }

        if (!productDTOArrayListtemp.get(position).getProduct_name().equalsIgnoreCase("")) {
            holder.tvProductName.setText(productDTOArrayListtemp.get(position).getProduct_name());
        }


        if (checkarss(productDTOArrayListtemp.get(position).getCategory_id())) {
            if (!productDTOArrayListtemp.get(position).getService_charge().equalsIgnoreCase("")) {
                holder.tvService_charge.setVisibility(View.VISIBLE);
                holder.tvService_charge.setText("Driver Allowance:" + " " + productDTOArrayListtemp.get(position).getService_charge() + " " + productDTOArrayListtemp.get(position).getRate());
            }
        } else {
            if (!productDTOArrayListtemp.get(position).getService_charge().equalsIgnoreCase("")) {
                holder.tvService_charge.setVisibility(View.VISIBLE);
                holder.tvService_charge.setText("Service Charge:" + " " + productDTOArrayListtemp.get(position).getService_charge() + " " + productDTOArrayListtemp.get(position).getRate());
            }
        }
        holder.tvService_charge.setVisibility(View.GONE);

        holder.tvdesc.setText(Html.fromHtml(productDTOArrayListtemp.get(position).getDescription()));

        holder.tvmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCheck) {
                    holder.tvdesc.setVisibility(View.VISIBLE);
                    isCheck = false;
                } else {
                    holder.tvdesc.setVisibility(View.GONE);
                    isCheck = true;
                }
            }
        });


    }

    public boolean checkarss(String catid) {

        boolean value = false;
        for (int i = 0; i < BaseActivity.addressModelArrayList.size(); i++) {
            if (BaseActivity.addressModelArrayList.get(i).getCat_id().equalsIgnoreCase(catid)) {
                value = true;
                break;
            } else {
                value = false;

            }

        }
        return value;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return productDTOArrayListtemp.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {


        public LinearLayout rel_view, lay_service_charge;
        public CustomTextViewBold qty_final, descriptionType, tvProductName, tvPrice, tvService_charge, tvquantity, tvquantitydays, tvroundtrip, tvmore, tvvehiclenumber, qty_tit;
        public CustomTextView tvPrices;
        public ImageView img_delete_cart_data, img_plus, img_minus, ProductImg;
        CustomTextView tvdesc;
        TextView toping;

        public MyViewHolder(View view) {
            super(view);

            descriptionType = view.findViewById(R.id.descriptionType);
            toping = view.findViewById(R.id.toping);
            img_plus = view.findViewById(R.id.img_plus);
            img_minus = view.findViewById(R.id.img_minus);
            tvProductName = (CustomTextViewBold) view.findViewById(R.id.tvProductName);
            ProductImg = (ImageView) view.findViewById(R.id.ProductImg);
            tvPrice = (CustomTextViewBold) view.findViewById(R.id.tvPrice);
            tvPrices = (CustomTextView) view.findViewById(R.id.tvPrices);
            rel_view = view.findViewById(R.id.rel_view);
            tvService_charge = view.findViewById(R.id.tvService_charge);
            img_delete_cart_data = view.findViewById(R.id.img_delete_cart_data);
            lay_service_charge = view.findViewById(R.id.lay_service_charge);
            tvquantity = view.findViewById(R.id.tvquantity);
            tvquantitydays = view.findViewById(R.id.tvquantitydays);
            tvroundtrip = view.findViewById(R.id.tvroundtrip);
            tvdesc = view.findViewById(R.id.tvdesc);
            tvmore = view.findViewById(R.id.tvmore);
            tvvehiclenumber = view.findViewById(R.id.tvvehiclenumber);
            qty_tit = view.findViewById(R.id.qty_tit);
            qty_final = view.findViewById(R.id.qty_final);
        }
    }

    private ArrayList<ProductDTO> isused() {
        ArrayList<ProductDTO> arrayList = new ArrayList<>();
        for (int i = 0; i < productDTOArrayList.size(); i++) {
            if (productDTOArrayList.get(i).getIs_used().equalsIgnoreCase("1")) {
                arrayList.add(productDTOArrayList.get(i));
            }
        }
        return arrayList;
    }


}

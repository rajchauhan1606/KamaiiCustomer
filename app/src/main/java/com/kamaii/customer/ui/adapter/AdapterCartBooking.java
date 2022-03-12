package com.kamaii.customer.ui.adapter;

import android.content.Context;
import android.provider.ContactsContract;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kamaii.customer.DTO.ProductDTO;
import com.kamaii.customer.R;
import com.kamaii.customer.https.HttpsRequest;
import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.interfacess.Helper;
import com.kamaii.customer.interfacess.OnSelectedItemListenerCart;
import com.kamaii.customer.ui.activity.BaseActivity;
import com.kamaii.customer.ui.activity.ViewAddressActivity;
import com.kamaii.customer.ui.fragment.MyBooking;
import com.kamaii.customer.utils.CustomTextView;
import com.kamaii.customer.utils.CustomTextViewBold;
import com.kamaii.customer.utils.ProjectUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class AdapterCartBooking extends RecyclerView.Adapter<AdapterCartBooking.MyViewHolder> {

    private LayoutInflater mLayoutInflater;
    private MyBooking myBooking;
    private Context context;
    private ArrayList<ProductDTO> productDTOList;
    private OnSelectedItemListenerCart onSelectedItemListener;
    private int po;
    private String TAG = MyBooking.class.getSimpleName();
    private HashMap<String, String> params = new HashMap<>();
    String locationstatus = "";
    String id;
    Boolean isCheck = true;
    String finalDesc = "";

    public AdapterCartBooking(MyBooking myBooking, ArrayList<ProductDTO> productDTOList, OnSelectedItemListenerCart onSelectedItemListener, String locationstatus, String id) {
        this.myBooking = myBooking;
        context = myBooking.getActivity();
        this.productDTOList = productDTOList;
        this.onSelectedItemListener = onSelectedItemListener;
        this.locationstatus = locationstatus;
        this.id = id;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_cart_booking, parent, false);

        return new MyViewHolder(itemView);
    }

    public void NotifyAll(ArrayList<ProductDTO> productDTOList) {
        this.productDTOList = productDTOList;
        notifyDataSetChanged();
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
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {


        final int pos = position;
        po = pos;
        holder.tvPrices.setText(Html.fromHtml(productDTOList.get(position).getStrike_product_price()));
        holder.tvPrice.setText(productDTOList.get(position).getRate() + " " + productDTOList.get(pos).getChange_price());
        holder.tvProductName.setText(productDTOList.get(pos).getProduct_name());
        if (position == 0) {
            holder.toping.setVisibility(View.GONE);
        }

        Glide.with(context).
                load(productDTOList.get(position).getProduct_image())
                .placeholder(R.drawable.dafault_product)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ProductImg);

        if (productDTOList.get(pos).getVehicle_number().equalsIgnoreCase("")) {
            holder.tvvehiclenumber.setVisibility(View.GONE);
        } else {
            holder.tvvehiclenumber.setVisibility(View.VISIBLE);
            holder.tvvehiclenumber.setText(productDTOList.get(pos).getVehicle_number());
        }
        holder.tvvehiclenumber.setVisibility(View.GONE);

        if (!productDTOList.get(pos).getQuantitydays().equalsIgnoreCase("")) {

            if (productDTOList.get(pos).getQuantitydays().equalsIgnoreCase("0")) {
                holder.tvquantitydays.setText("Qty");
            } else {
                holder.tvquantitydays.setText("KM ");
            }
            holder.tvquantitydays.setVisibility(View.GONE);

        }

        if (!productDTOList.get(pos).getMaxmiumvalue().equalsIgnoreCase("")) {
            holder.tvquantity.setText(productDTOList.get(pos).getMaxmiumvalue());
        }


        if (locationstatus.equalsIgnoreCase("0")) {

        } else if (locationstatus.equalsIgnoreCase("1")) {

            if (checkarss(ViewAddressActivity.catid)) {
                if (!productDTOList.get(pos).getService_charge().equalsIgnoreCase("")) {
                    holder.tvService_charge.setVisibility(View.GONE);
                    holder.tvService_charge.setText("Driver Allowance:" + " " + productDTOList.get(position).getService_charge() + " " + productDTOList.get(position).getRate());
                }
            } else {
                if (!productDTOList.get(pos).getService_charge().equalsIgnoreCase("")) {
                    holder.tvService_charge.setVisibility(View.GONE);
                    holder.tvService_charge.setText("Service Charge:" + " " + productDTOList.get(pos).getService_charge() + " " + productDTOList.get(position).getRate());
                }
            }

        }

        if (productDTOList.get(pos).getSub_category_id().equals("242") || productDTOList.get(pos).getSub_category_id().equals("434")) {
            holder.qty_tit.setVisibility(View.GONE);
            holder.qty_final.setVisibility(View.GONE);
            holder.descriptionType.setVisibility(View.GONE);
        } else if (productDTOList.get(pos).getSub_category_id().equals("453")) {
            holder.qty_tit.setVisibility(View.GONE);
            holder.qty_final.setVisibility(View.GONE);
            holder.descriptionType.setVisibility(View.GONE);
        } else {
            holder.qty_tit.setVisibility(View.VISIBLE);
            holder.qty_final.setVisibility(View.VISIBLE);
            holder.qty_final.setText(productDTOList.get(pos).getQuantitydays());
            holder.descriptionType.setVisibility(View.VISIBLE);
            holder.descriptionType.setText(Html.fromHtml(productDTOList.get(position).getDescriptionType()));
        }
        if (productDTOList.get(position).getDescription() == null || productDTOList.get(position).getDescription().equals("")) {
            finalDesc = " ";
        } else {
            finalDesc = productDTOList.get(position).getDescription();
        }
        if (productDTOList.get(position).getDescription() != null) {
            holder.tvdesc.setText(Html.fromHtml(productDTOList.get(position).getDescription()));
        }
        holder.tvmore.setVisibility(View.GONE);
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

    public void deleteservice() {

        ProjectUtils.showProgressDialog(context, true, context.getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.DELETE_SERVICE, params, context).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    productDTOList.remove(po);
                    notifyDataSetChanged();
                } else {
                }


            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void setvisiblity(TextView textView, boolean isvisible) {
        textView.setVisibility(isvisible ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return productDTOList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {


        public LinearLayout rel_view, lay_service_charge;
        public CustomTextViewBold descriptionType, qty_final, tvProductName, tvPrice, tvService_charge, tvquantity, tvquantitydays, tvroundtrip, tvmore, tvvehiclenumber, qty_tit;
        public ImageView img_delete_cart_data, img_plus, img_minus, ProductImg;
        CustomTextView tvdesc,tvPrices;
        String finalDesc = "";
        TextView toping;

        public MyViewHolder(View view) {
            super(view);

            toping = view.findViewById(R.id.toping);
            img_plus = view.findViewById(R.id.img_plus);
            img_minus = view.findViewById(R.id.img_minus);
            descriptionType = (CustomTextViewBold) view.findViewById(R.id.descriptionType);
            tvProductName = (CustomTextViewBold) view.findViewById(R.id.tvProductName);
            ProductImg = (ImageView) view.findViewById(R.id.ProductImg);
            tvPrice = (CustomTextViewBold) view.findViewById(R.id.tvPrice);
            tvPrices =  view.findViewById(R.id.tvPrices);
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


}

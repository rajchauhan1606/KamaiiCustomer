package com.kamaii.customer.ui.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kamaii.customer.DTO.AllAtristListDTO;
import com.kamaii.customer.DTO.ProductDTO;
import com.kamaii.customer.R;
import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.preferences.SharedPrefrence;
import com.kamaii.customer.ui.activity.BaseActivity;
import com.kamaii.customer.ui.activity.ViewServices;
import com.kamaii.customer.utils.CustomTextView;
import com.kamaii.customer.utils.CustomTextViewBold;
import com.kamaii.customer.utils.ProjectUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


public class DiscoverAdapter extends RecyclerView.Adapter<DiscoverAdapter.MyViewHolder> implements Filterable {

    Context mContext;
    private ArrayList<AllAtristListDTO> allAtristListDTOList;
    private ArrayList<AllAtristListDTO> allAtristListDTOListfiltered;
    private LayoutInflater inflater;
    private SharedPrefrence prefrence;
    CustomTextViewBold total_product;

    public DiscoverAdapter(Context mContext, ArrayList<AllAtristListDTO> allAtristListDTOList, LayoutInflater inflater, CustomTextViewBold total_product) {
        this.mContext = mContext;
        this.allAtristListDTOList = allAtristListDTOList;
        allAtristListDTOListfiltered = new ArrayList<>();
        this.allAtristListDTOListfiltered.addAll(allAtristListDTOList);
        this.total_product = total_product;
        this.inflater = inflater;
        prefrence = SharedPrefrence.getInstance(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.adapterdiscover, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(allAtristListDTOList.get(position).getLatitude()), Double.parseDouble(allAtristListDTOList.get(position).getLongitude()), 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
            add = add + "\n" + obj.getCountryName();
            Log.e("locality1", "" + obj.getCountryName());

            add = add + "\n" + obj.getCountryCode();
            Log.e("locality2", "" + obj.getCountryCode());

            String area = obj.getSubAdminArea();
            Log.e("locality3", "" + area);
            add = add + "\n" + obj.getPostalCode();
            Log.e("locality4", "" + obj.getPostalCode());

            add = add + "\n" + obj.getSubAdminArea();
            Log.e("locality5", "" + obj.getSubAdminArea());

            add = add + "\n" + obj.getLocality();
            add = add + "\n" + obj.getLocale();

            Log.e("locality6", "" + obj.getLocality());
            Log.e("locality6", "" + obj.getLocale());

            holder.area_name.setText(area);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.cat_name.setText(allAtristListDTOListfiltered.get(position).getCat_name());
        holder.CTVartistworknote.setText(Html.fromHtml(allAtristListDTOListfiltered.get(position).getOrdernote()));
        holder.CTVartistname.setText(allAtristListDTOListfiltered.get(position).getName());


        holder.order_time_text.setText(allAtristListDTOListfiltered.get(position).getOrder_time_text());

        /*Glide.with(mContext).
                load(allAtristListDTOListfiltered.get(position).getOrder_type())
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.order_type);*/


        if (allAtristListDTOListfiltered.get(position).getProduct_count().equalsIgnoreCase("1")) {
            holder.total_items.setText(allAtristListDTOListfiltered.get(position).getProduct_count() + " Item");
        } else {
            holder.total_items.setText(allAtristListDTOListfiltered.get(position).getProduct_count() + " Items");
        }
        if (allAtristListDTOListfiltered.get(position).getArtist_commission_type().equalsIgnoreCase("0")) {
            if (allAtristListDTOListfiltered.get(position).getCommission_type().equalsIgnoreCase("0")) {
                holder.CTVartistchargeprh.setText(allAtristListDTOListfiltered.get(position).getCurrency_type() + allAtristListDTOListfiltered.get(position).getPrice() + mContext.getResources().getString(R.string.hr_add_on));
            } else if (allAtristListDTOListfiltered.get(position).getCommission_type().equalsIgnoreCase("1") && allAtristListDTOListfiltered.get(position).getFlat_type().equalsIgnoreCase("2")) {
                holder.CTVartistchargeprh.setText(allAtristListDTOListfiltered.get(position).getCurrency_type() + allAtristListDTOListfiltered.get(position).getPrice() + mContext.getResources().getString(R.string.hr_add_on));
            } else if (allAtristListDTOListfiltered.get(position).getCommission_type().equalsIgnoreCase("1") && allAtristListDTOListfiltered.get(position).getFlat_type().equalsIgnoreCase("1")) {
                holder.CTVartistchargeprh.setText(allAtristListDTOListfiltered.get(position).getCurrency_type() + allAtristListDTOListfiltered.get(position).getPrice() + mContext.getResources().getString(R.string.hr_add_on));
            } else {
                holder.CTVartistchargeprh.setText(allAtristListDTOList.get(position).getCurrency_type() + allAtristListDTOListfiltered.get(position).getPrice() + mContext.getResources().getString(R.string.hr_add_on));
            }
        } else {
            if (allAtristListDTOListfiltered.get(position).getCommission_type().equalsIgnoreCase("0")) {
                holder.CTVartistchargeprh.setText(allAtristListDTOListfiltered.get(position).getCurrency_type() + allAtristListDTOListfiltered.get(position).getPrice() + " " + mContext.getResources().getString(R.string.fixed_rate_add_on));
            } else if (allAtristListDTOListfiltered.get(position).getCommission_type().equalsIgnoreCase("1") && allAtristListDTOListfiltered.get(position).getFlat_type().equalsIgnoreCase("2")) {
                holder.CTVartistchargeprh.setText(allAtristListDTOListfiltered.get(position).getCurrency_type() + allAtristListDTOListfiltered.get(position).getPrice() + " " + mContext.getResources().getString(R.string.fixed_rate_add_on));
            } else if (allAtristListDTOListfiltered.get(position).getCommission_type().equalsIgnoreCase("1") && allAtristListDTOListfiltered.get(position).getFlat_type().equalsIgnoreCase("1")) {
                holder.CTVartistchargeprh.setText(allAtristListDTOListfiltered.get(position).getCurrency_type() + allAtristListDTOListfiltered.get(position).getPrice() + " " + mContext.getResources().getString(R.string.fixed_rate_add_on));
            } else {
                holder.CTVartistchargeprh.setText(allAtristListDTOListfiltered.get(position).getCurrency_type() + allAtristListDTOListfiltered.get(position).getPrice() + " " + mContext.getResources().getString(R.string.fixed_rate_add_on));
            }
        }


        holder.CTVlocation.setText(allAtristListDTOListfiltered.get(position).getLocation());
        if (allAtristListDTOListfiltered.get(position).getSlotnote().equals("0")) {
            holder.CTVSlot.setVisibility(View.GONE);
            holder.tmp_view_two.setVisibility(View.GONE);

            holder.order_type_relative.setVisibility(View.GONE);
        } else {
            holder.tmp_view_two.setVisibility(View.VISIBLE);
            holder.CTVSlot.setVisibility(View.VISIBLE);
            holder.order_type_relative.setVisibility(View.VISIBLE);
            holder.CTVSlot.setText(Html.fromHtml(allAtristListDTOListfiltered.get(position).getSlotnote()));

            if (allAtristListDTOListfiltered.get(position).getOrder_time().equalsIgnoreCase("0")) {

                holder.order_type_image.setImageTintList(mContext.getResources().getColorStateList(R.color.image_color_pink));
                holder.order_time_text.setTextColor(mContext.getResources().getColor(R.color.image_color_pink));
            } else {
                holder.order_type_image.setImageTintList(mContext.getResources().getColorStateList(R.color.image_color_blue));
                holder.order_time_text.setTextColor(mContext.getResources().getColor(R.color.image_color_blue));

            }
        }
        if (!allAtristListDTOListfiltered.get(position).getDistance().equalsIgnoreCase("")) {
            double sdistance = Double.parseDouble(allAtristListDTOListfiltered.get(position).getDistance());

            holder.CTVdistance.setText(String.valueOf(new DecimalFormat("##.##").format(sdistance * 1.4) + " " + mContext.getString(R.string.km)));
        }


        try {
            if (!allAtristListDTOListfiltered.get(position).getCreated_at().equalsIgnoreCase("")) {

                holder.CTVtime.setText(ProjectUtils.getDisplayableTime(ProjectUtils.correctTimestamp(Long.parseLong(allAtristListDTOListfiltered.get(position).getCreated_at()))));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        holder.CTVjobdone.setText(allAtristListDTOListfiltered.get(position).getJobDone());
        Log.e("rating", "" + allAtristListDTOListfiltered.get(position).getJobDone());

        holder.tvRating.setText(allAtristListDTOListfiltered.get(position).getAva_rating() + "/5");
        Log.e("rating123", allAtristListDTOListfiltered.get(position).getName() + "" + allAtristListDTOListfiltered.get(position).getAva_rating());

        int a = Integer.parseInt(allAtristListDTOListfiltered.get(position).getPercentages());
        if (a < 40) {

            holder.CTVpersuccess.setText(allAtristListDTOListfiltered.get(position).getPercentages() + "%");
            holder.CTVpersuccess.setBackground(mContext.getResources().getDrawable(R.drawable.work_percentage));
        } else if (a >= 40 && a < 70) {
            holder.CTVpersuccess.setText(allAtristListDTOListfiltered.get(position).getPercentages() + "%");
            holder.CTVpersuccess.setBackground(mContext.getResources().getDrawable(R.drawable.yellow_percentage));
        } else {
            holder.CTVpersuccess.setText(allAtristListDTOListfiltered.get(position).getPercentages() + "%");
            holder.CTVpersuccess.setBackground(mContext.getResources().getDrawable(R.drawable.green_percentage));
        }
        Log.e("IMAGE", "" + allAtristListDTOListfiltered.get(position).getImage());
        Glide.with(mContext).
                load(allAtristListDTOListfiltered.get(position).getImage())
                .placeholder(R.drawable.ic__01_shop)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.IVartist);

        if (allAtristListDTOListfiltered.get(position).getFav_status().equalsIgnoreCase("1")) {
            holder.ivFav.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_fav_full));
        } else {
            holder.ivFav.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_fav_blank));
        }
        if (allAtristListDTOListfiltered.get(position).getFeatured().equalsIgnoreCase("1")) {
            holder.ivfeatured.setVisibility(View.VISIBLE);
            holder.relative_card.setBackground(mContext.getResources().getDrawable(R.drawable.vendor_card_green_border));
        } else {
            holder.ivfeatured.setVisibility(View.INVISIBLE);
        }

        holder.ratingbar.setRating(Float.parseFloat(allAtristListDTOListfiltered.get(position).getAva_rating()));
        holder.rlClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(mContext, ViewServices.class);
                in.putExtra("artist_name", allAtristListDTOListfiltered.get(position).getName());
                in.putExtra(Consts.ARTIST_ID, allAtristListDTOListfiltered.get(position).getUser_id());
                in.putExtra(Consts.SUB_CATEGORY_ID, allAtristListDTOListfiltered.get(position).getSub_category_id2());
                mContext.startActivity(in);
            }
        });
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        allAtristListDTOListfiltered.clear();
        if (charText.length() == 0) {
            allAtristListDTOListfiltered.addAll(allAtristListDTOList);
        } else {
            for (AllAtristListDTO historyDTO : allAtristListDTOList) {
                if (historyDTO.getName().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    allAtristListDTOListfiltered.add(historyDTO);
                }
            }
        }
        total_product.setText(allAtristListDTOListfiltered.size() + " Vendors Near by");
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {

        return allAtristListDTOListfiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    allAtristListDTOListfiltered = allAtristListDTOList;
                } else {
                    ArrayList<AllAtristListDTO> filteredList = new ArrayList<>();
                    for (AllAtristListDTO row : allAtristListDTOList) {
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    allAtristListDTOListfiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = allAtristListDTOListfiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                allAtristListDTOListfiltered = (ArrayList<AllAtristListDTO>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public CustomTextViewBold order_time_text, total_items, CTVartistname, CTVSlot, CTVartistworknote, CTVdistance, CTVjobdone, CTVtime, CTVartistchargeprh, CTVpersuccess, tvRating;
        public CustomTextView cat_name, CTVlocation, area_name;

        public ImageView IVartist;
        public RatingBar ratingbar;
        public RelativeLayout order_type_relative, rlClick, mainrel, relative_card;
        public ImageView order_type_image, ivFav, ivfeatured, order_type;
        CardView cardv;
        RelativeLayout relativeLayout;
        TextView tmp_view_two;

        public MyViewHolder(View view) {
            super(view);

            order_type_image = view.findViewById(R.id.order_type_image);
            order_time_text = view.findViewById(R.id.order_time_text);
            rlClick = view.findViewById(R.id.rlClick);
            tmp_view_two = view.findViewById(R.id.tmp_view_two);
            cat_name = view.findViewById(R.id.cat_name);
            total_items = view.findViewById(R.id.total_items);
            order_type_relative = view.findViewById(R.id.order_type_relative);
            CTVartistname = view.findViewById(R.id.CTVartistname);
            CTVartistworknote = view.findViewById(R.id.CTVartistworknote);
            CTVSlot = view.findViewById(R.id.CTVSlot);
            CTVjobdone = view.findViewById(R.id.CTVjobdone);
            CTVpersuccess = view.findViewById(R.id.CTVpersuccess);
            relative_card = view.findViewById(R.id.relative_card);
            CTVartistchargeprh = view.findViewById(R.id.CTVartistchargeprh);
            CTVlocation = view.findViewById(R.id.CTVlocation);
            CTVdistance = view.findViewById(R.id.CTVdistance);
            CTVtime = view.findViewById(R.id.CTVtime);
            IVartist = view.findViewById(R.id.IVartist);
            tvRating = view.findViewById(R.id.tvRating);
            area_name = view.findViewById(R.id.area_name);
            ratingbar = view.findViewById(R.id.ratingbar);
            ivFav = view.findViewById(R.id.ivFav);
            order_type = view.findViewById(R.id.order_type);
            ivfeatured = view.findViewById(R.id.ivfeatured);
            mainrel = view.findViewById(R.id.mainrel);
            cardv = view.findViewById(R.id.cardv);
            relativeLayout = view.findViewById(R.id.rlClick);

        }
    }

}
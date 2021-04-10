package com.kamaii.customer.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kamaii.customer.R;
import com.kamaii.customer.interfacess.OnDataErrorListener;
import com.kamaii.customer.interfacess.OnSelectedItemListener;
import com.kamaii.customer.ui.activity.BaseActivity;
import com.kamaii.customer.ui.activity.ViewAddressActivity;
import com.kamaii.customer.ui.activity.ViewServices;
import com.kamaii.customer.ui.models.ThirdCateModel;
import com.kamaii.customer.utils.CustomTextView;
import com.kamaii.customer.utils.CustomTextViewBold;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CarSelectionAdapter extends RecyclerView.Adapter<CarSelectionAdapter.ViewHolder> implements Filterable {

    private List<ThirdCateModel> newsPaperModelList, newsFilterdList;
    private Context context;
    private ArrayList<Integer> mSectionPositions;
    private OnSelectedItemListener onSelectedItemListener;
    private OnDataErrorListener dataErrorListener;
    private String destitime;
    private String mainkm;
    String finalValue = "";

    public CarSelectionAdapter(Context context, List<ThirdCateModel> newsPaperModelList, OnSelectedItemListener onSelectedItemListener, String destitime, String mainkm) {
        this.context = context;
        this.newsPaperModelList = newsPaperModelList;
        newsFilterdList = newsPaperModelList;
        this.onSelectedItemListener = onSelectedItemListener;
        this.destitime = destitime;
        this.mainkm = mainkm;
    }

    @Override
    public int getItemCount() {
        return newsFilterdList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_car_selection, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.txt_contain.setText(newsFilterdList.get(position).getCat_name());
        holder.txtvechile.setText(destitime + " | " + mainkm + " km");
        Glide.with(this.context).load(newsFilterdList.get(position).getImage()).placeholder(R.drawable.default_image).into(holder.img_sub_cat);

        if (!mainkm.isEmpty() && !newsFilterdList.get(position).getPrice().isEmpty()) {
            DecimalFormat newFormat = new DecimalFormat("##.##");

            double mainkmstr = Double.parseDouble(newFormat.format(Double.parseDouble(mainkm)));
            double kmstr = Double.parseDouble(newFormat.format(Double.parseDouble(newsFilterdList.get(position).getPrice())));
            double driverstr = Double.parseDouble(newFormat.format(Double.parseDouble(newsFilterdList.get(position).getDriverallowance())));
            finalValue = "₹" + String.valueOf(Double.parseDouble(newFormat.format((mainkmstr * kmstr) + driverstr)));
            holder.finalproducttext.setText(finalValue);
        }

        if (newsFilterdList.get(position).isSelected()) {
            holder.car_selection_background.setBackground(context.getDrawable(R.drawable.cab_flow_circuler_border_active));
            ((ViewAddressActivity) context).voidgetPricefromAdapter(String.valueOf(finalValue));

        } else {
            holder.car_selection_background.setBackground(context.getDrawable(R.drawable.cab_flow_circuler_border));
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < newsFilterdList.size(); i++) {
                    newsFilterdList.get(i).setSelected(false);
                }
                newsFilterdList.get(position).setSelected(true);

                notifyDataSetChanged();
                onSelectedItemListener.setOnClick(newsFilterdList.get(position).getCat_name(), position);
            }
        });


    }

    public boolean checkarss(int position) {

        boolean value = false;
        for (int i = 0; i < BaseActivity.addressModelArrayList.size(); i++) {
            if (BaseActivity.addressModelArrayList.get(i).getCat_id().equalsIgnoreCase(newsFilterdList.get(position).getCat_id())) {
                value = true;
                break;

            } else {
                value = false;
            }
        }
        return value;
    }


    public void setDataErrorListener(OnDataErrorListener dataErrorListener) {
        this.dataErrorListener = dataErrorListener;
    }

    public void update(List<ThirdCateModel> newsPaperModelList, String desitime, String mainkm) {
        this.destitime = desitime;
        this.mainkm = mainkm;
        this.newsPaperModelList.addAll(newsPaperModelList);
        notifyDataSetChanged();
    }

    public void notifyDataChanged(ArrayList<ThirdCateModel> list, String desitime, String mainkm) {
        this.newsPaperModelList = list;
        this.destitime = desitime;
        this.mainkm = mainkm;
        newsFilterdList = this.newsPaperModelList;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    newsFilterdList = newsPaperModelList;
                } else {
                    List<ThirdCateModel> filteredList = new ArrayList<>();
                    for (ThirdCateModel subcategory : newsPaperModelList) {
                        if (subcategory.getCat_name().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(subcategory);
                        }
                    }

                    newsFilterdList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = newsFilterdList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                newsFilterdList = (ArrayList<ThirdCateModel>) filterResults.values;
                if (newsFilterdList.size() <= 0)
                    dataErrorListener.DataNotFound(true);
                else {
                    dataErrorListener.DataNotFound(false);
                }
                notifyDataSetChanged();
            }
        };
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextViewBold txt_contain;
        CustomTextView txtvechile;
        ImageView img_sub_cat;
        RelativeLayout car_selection_background;
        View vehicleactive;
        CustomTextViewBold finalproducttext;

        ViewHolder(View itemView) {
            super(itemView);
            car_selection_background = itemView.findViewById(R.id.car_selection_background);
            txt_contain = itemView.findViewById(R.id.txtservicename);
            img_sub_cat = itemView.findViewById(R.id.artistVehicleImageView);
            txtvechile = itemView.findViewById(R.id.txtvechile);
            vehicleactive = itemView.findViewById(R.id.vehicleactive);
            finalproducttext = itemView.findViewById(R.id.finalproducttext);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


}


package com.kamaii.customer.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.List;

public class ThirdCateAdapter extends RecyclerView.Adapter<ThirdCateAdapter.ViewHolder> implements Filterable {

    private List<ThirdCateModel> newsPaperModelList, newsFilterdList;
    private Context context;
    private ArrayList<Integer> mSectionPositions;
    private OnSelectedItemListener onSelectedItemListener;
    private OnDataErrorListener dataErrorListener;
    public ThirdCateAdapter(Context context, List<ThirdCateModel> newsPaperModelList, OnSelectedItemListener onSelectedItemListener) {
        this.context = context;
        this.newsPaperModelList = newsPaperModelList;
        newsFilterdList = newsPaperModelList;
        this.onSelectedItemListener = onSelectedItemListener;
    }

    @Override
    public int getItemCount() {
        return newsFilterdList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_third, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.txt_contain.setText(newsFilterdList.get(position).getCat_name());
        Glide.with(this.context).load(newsFilterdList.get(position).getImage()).placeholder(R.drawable.default_image).into(holder.img_sub_cat);
        holder.arrivein.setVisibility(View.VISIBLE);
        holder.arrivein.setText(newsFilterdList.get(position).getArrivein());

        if (newsFilterdList.get(position).isSelected()) {
            holder.vehicleactive.setVisibility(View.VISIBLE);

        } else {
            holder.vehicleactive.setVisibility(View.INVISIBLE);
            holder.layall.setBackground(context.getDrawable(R.drawable.button_pressed_55999));
        }
        holder.num_artist.setVisibility(View.VISIBLE);
        holder.num_artist.setText(newsFilterdList.get(position).getNoofartistavailable());
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

    public void update(List<ThirdCateModel> newsPaperModelList) {
        this.newsPaperModelList.addAll(newsPaperModelList);
        notifyDataSetChanged();
    }

    public void notifyDataChanged(ArrayList<ThirdCateModel> list) {
        this.newsPaperModelList = list;
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
        ImageView img_sub_cat;
        LinearLayout item_layout;
        LinearLayout layall;
        View vehicleactive;
        CustomTextView arrivein;
        CustomTextViewBold num_artist;

        ViewHolder(View itemView) {
            super(itemView);
            txt_contain = itemView.findViewById(R.id.txt_contain);
            img_sub_cat = itemView.findViewById(R.id.img_sub_cat);
            layall = itemView.findViewById(R.id.layall);
            vehicleactive = itemView.findViewById(R.id.vehicleactive);
            vehicleactive = itemView.findViewById(R.id.vehicleactive);
            arrivein = itemView.findViewById(R.id.arrivein);
            num_artist = itemView.findViewById(R.id.num_artist);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


}


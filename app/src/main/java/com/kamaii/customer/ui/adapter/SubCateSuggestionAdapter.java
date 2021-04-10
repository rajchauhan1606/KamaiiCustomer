package com.kamaii.customer.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kamaii.customer.DTO.ArtistDetailsDTO;
import com.kamaii.customer.R;
import com.kamaii.customer.api.apiClient;
import com.kamaii.customer.api.apiRest;
import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.interfacess.OnDataErrorListener;
import com.kamaii.customer.interfacess.OnSelectedItemListener;
import com.kamaii.customer.ui.activity.ArtistProfile;
import com.kamaii.customer.ui.activity.SubCategoryActivity;
import com.kamaii.customer.ui.activity.SubLevalCategoryActivity;
import com.kamaii.customer.ui.activity.ViewAddressActivity;
import com.kamaii.customer.ui.activity.ViewServices;
import com.kamaii.customer.ui.models.SubCateModel;
import com.kamaii.customer.utils.CustomTextView;
import com.kamaii.customer.utils.CustomTextViewBold;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.widget.Toast.LENGTH_LONG;

public class SubCateSuggestionAdapter extends RecyclerView.Adapter<SubCateSuggestionAdapter.ViewHolder> implements Filterable {

    private List<SubCateModel> newsPaperModelList, newsFilterdList;
    private Context context;
    private ArrayList<Integer> mSectionPositions;
    private OnSelectedItemListener onSelectedItemListener;
    private OnDataErrorListener dataErrorListener;
    private ArtistDetailsDTO artistDetailsDTO;
    private Bundle bundle;
    String artist_id;
    String artist_name;

    public SubCateSuggestionAdapter(Context context, List<SubCateModel> newsPaperModelList, OnSelectedItemListener onSelectedItemListener, String artist_id, String artist_name) {
        this.context = context;
        this.newsPaperModelList = newsPaperModelList;
        newsFilterdList = newsPaperModelList;
        this.artist_id = artist_id;
        this.artist_name = artist_name;
        this.onSelectedItemListener = onSelectedItemListener;
    }

    @Override
    public int getItemCount() {
        return newsFilterdList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sub_cate_suggestion, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.txt_contain.setText(newsFilterdList.get(position).getCat_name());
        holder.total_items.setText(newsFilterdList.get(position).getProduct_count());
        Glide.with(this.context).load(newsFilterdList.get(position).getImage()).placeholder(R.drawable.default_image).into(holder.img_sub_cat);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ViewServices) context).suggestionsecond(newsFilterdList.get(position).getId());
            }
        });


    }
    public void setDataErrorListener(OnDataErrorListener dataErrorListener) {
        this.dataErrorListener = dataErrorListener;
    }

    public void update(List<SubCateModel> newsPaperModelList) {
        this.newsPaperModelList.addAll(newsPaperModelList);
        notifyDataSetChanged();
    }

    public void notifyDataChanged(ArrayList<SubCateModel> list) {
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
                    List<SubCateModel> filteredList = new ArrayList<>();
                    for (SubCateModel subcategory : newsPaperModelList) {

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
                newsFilterdList = (ArrayList<SubCateModel>) filterResults.values;
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
        CustomTextView total_items;
        ImageView img_sub_cat;
        LinearLayout item_layout;
        ImageView forward_arrow;

        ViewHolder(View itemView) {
            super(itemView);
            txt_contain = itemView.findViewById(R.id.txt_contain);
            img_sub_cat = itemView.findViewById(R.id.img_sub_cat);
            total_items = itemView.findViewById(R.id.total_items);
            forward_arrow = itemView.findViewById(R.id.forward_arrow);
        }


    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}


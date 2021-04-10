package com.kamaii.customer.ui.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kamaii.customer.R;
import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.ui.activity.SubCategoryActivity;
import com.kamaii.customer.ui.models.FooterSliderModel;

import java.util.ArrayList;

public class CashBackAdapter extends RecyclerView.Adapter<CashBackAdapter.RecycleViewHolder> {

    private Context context;
    private ArrayList<FooterSliderModel> images;

    public CashBackAdapter(Context context, ArrayList<FooterSliderModel> images) {
        this.context = context;
        this.images = images;
        Log.e("RES_SIZE", "" + images.size());
    }

    @NonNull
    @Override
    public RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_cashback_recycle_layout, parent, false);
        return new RecycleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewHolder holder, int position) {

        Glide.with(context).load(images.get(position).getSlider_image()).into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (images.get(position).getCategory_id().equalsIgnoreCase("0")) {
                    Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(images.get(position).getLink()));
                    try {
                        context.startActivity(webIntent);
                    } catch (ActivityNotFoundException ex) {
                    }
                } else {
                    Intent intent = new Intent(context, SubCategoryActivity.class);
                    intent.putExtra(Consts.CATEGORY_ID, images.get(position).getCategory_id());
                    intent.putExtra(Consts.CATEGORY_NAME, images.get(position).getCategory_name());
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }


    class RecycleViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public RecycleViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.cashback_image);


        }
    }

}

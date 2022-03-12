package com.kamaii.customer.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kamaii.customer.R;
import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.ui.activity.ArtistProfile;
import com.kamaii.customer.ui.activity.DiscoverActivity;
import com.kamaii.customer.ui.models.PopulerPartnerModel;

import java.util.List;

public class PopulerPartnerAdapter extends RecyclerView.Adapter<PopulerPartnerAdapter.PopulerViewHolder> {

    Context context;
    List<PopulerPartnerModel> arrayList;

    public PopulerPartnerAdapter(Context context, List<PopulerPartnerModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        Log.e("POPULER_PARTNER_"," adapter "+arrayList.toString());

    }

    @NonNull
    @Override
    public PopulerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.populer_partner_recycle_layout,parent,false);
        return new PopulerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PopulerViewHolder holder, int position) {

        Glide.with(context).load(Uri.parse(arrayList.get(position).getBanner_image())).into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ArtistProfile.class).putExtra(Consts.ARTIST_ID,arrayList.get(position).getArtist_id()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class PopulerViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        public PopulerViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.artistlogoimage);
        }
    }
}

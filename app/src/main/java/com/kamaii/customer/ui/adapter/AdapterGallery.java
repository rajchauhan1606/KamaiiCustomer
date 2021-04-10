package com.kamaii.customer.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kamaii.customer.DTO.GalleryDTO;
import com.kamaii.customer.R;
import com.kamaii.customer.ui.fragment.ImageGallery;
import com.kamaii.customer.utils.ImagePopup;
import com.kamaii.customer.utils.ImagePreviewerUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by WebKnight Infosystem on 01/01/19.
 */

public class AdapterGallery extends RecyclerView.Adapter<AdapterGallery.MyViewHolder> {
    private ImageGallery imageGallery;
    LayoutInflater mLayoutInflater;
    private ArrayList<GalleryDTO> gallery;
    private Context context;

    public AdapterGallery(ImageGallery imageGallery, ArrayList<GalleryDTO> gallery) {
        this.imageGallery = imageGallery;
        context = imageGallery.getActivity();
        this.gallery = gallery;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_gallery, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        Glide
                .with(context)
                .load(gallery.get(position).getImage())
                .placeholder(R.drawable.dummyuser_image)
                .into(holder.iv_bottom_foster);

        holder.iv_bottom_foster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show(context, holder.iv_bottom_foster, gallery.get(position).getImage());
            }
        });

    }

    @SuppressLint("ClickableViewAccessibility")
    public void show(Context context, ImageView source, String s) {
        BitmapDrawable background = ImagePreviewerUtils.getBlurredScreenDrawable(context, source.getRootView());

        View dialogView = LayoutInflater.from(context).inflate(R.layout.view_image_previewer, null);
        ImageView imageView = (ImageView) dialogView.findViewById(R.id.previewer_image);
        ImageView ic_close_imaging = (ImageView) dialogView.findViewById(R.id.ic_close_imaging);

        Glide
                .with(context)
                .load(s)
                .placeholder(R.drawable.dummyuser_image)
                .into(imageView);

        /*Drawable copy = source.getDrawable().getConstantState().newDrawable();
        imageView.setImageDrawable(copy);*/

        final Dialog dialog = new Dialog(context, R.style.ImagePreviewerTheme);
        dialog.getWindow().setBackgroundDrawable(background);
        dialog.setContentView(dialogView);
        dialog.setCancelable(true);
        dialog.show();
        ic_close_imaging.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getParent().requestDisallowInterceptTouchEvent(false);
                dialog.dismiss();
            }
        });
        source.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (dialog.isShowing()) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    int action = event.getActionMasked();
                    if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        dialog.dismiss();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return gallery.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_bottom_foster;

        public MyViewHolder(View view) {
            super(view);

            iv_bottom_foster = (ImageView) itemView.findViewById(R.id.iv_bottom_foster);


        }
    }
}
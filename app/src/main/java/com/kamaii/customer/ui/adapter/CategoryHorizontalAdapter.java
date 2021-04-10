package com.kamaii.customer.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kamaii.customer.DTO.CategoryDTO;
import com.kamaii.customer.R;
import com.kamaii.customer.utils.CustomTextView;
import com.kamaii.customer.utils.CustomTextViewBold;

import java.util.List;

public class CategoryHorizontalAdapter extends RecyclerView.Adapter<CategoryHorizontalAdapter.HorizontalViewHolder> {

    Context context;
    List<CategoryDTO> arrayList;

    public CategoryHorizontalAdapter(Context context, List<CategoryDTO> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        Log.e("ARRAYLIST",""+arrayList.toString());
    }

    @NonNull
    @Override
    public HorizontalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_category,parent,false);
        return new HorizontalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalViewHolder holder, int position) {
        holder.textView.setText(arrayList.get(position).getCat_name());
        Glide.with(this.context).load((arrayList.get(position).getImgurl())).placeholder(R.drawable.default_image).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class HorizontalViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        CustomTextViewBold textView;
        public HorizontalViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_view_item_category);
            textView = itemView.findViewById(R.id.text_view_item_category);
        }
    }
}

package com.kamaii.customer.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kamaii.customer.R;
import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.interfacess.OnDataErrorListener;
import com.kamaii.customer.ui.activity.DiscoverActivity;
import com.kamaii.customer.ui.models.ThirdCateModel;
import com.kamaii.customer.utils.CustomTextViewBold;

import java.util.List;

public class SubLevalCatAdapter extends RecyclerView.Adapter<SubLevalCatAdapter.SubLevalViewHolder> {

    Context context;
    List<ThirdCateModel> arraylist;
    String sub_category_idd,subcatname,catid;
    private OnDataErrorListener dataErrorListener;

    public SubLevalCatAdapter(Context context, List<ThirdCateModel> arraylist,String sub_category_idd, String subcatname , String catid) {
        this.context = context;
        this.arraylist = arraylist;
        this.sub_category_idd = sub_category_idd;
        this.subcatname = subcatname;
        this.catid = catid;
    }

    public void setDataErrorListener(OnDataErrorListener dataErrorListener) {
        this.dataErrorListener = dataErrorListener;
    }


    @NonNull
    @Override
    public SubLevalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sub_cate, parent, false);
        return new SubLevalViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SubLevalViewHolder holder, int position) {

        holder.txt_contain.setText(arraylist.get(position).getCat_name());
        Glide.with(this.context).load(arraylist.get(position).getImage()).placeholder(R.drawable.default_image).into(holder.img_sub_cat);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, DiscoverActivity.class);
                intent.putExtra(Consts.SUB_CATEGORY_ID, arraylist.get(position).getSubcat_id());
                intent.putExtra(Consts.Sub_CAT_NAME, arraylist.get(position).getCat_name());
                intent.putExtra(Consts.CATEGORY_ID, arraylist.get(position).getCat_id());
                intent.putExtra("flag",true);
                intent.putExtra("third_leval_tracker", arraylist.get(position).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arraylist.size();
    }

    class SubLevalViewHolder extends RecyclerView.ViewHolder {
        CustomTextViewBold txt_contain;
        ImageView img_sub_cat;
        LinearLayout item_layout;

        public SubLevalViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_contain = itemView.findViewById(R.id.txt_contain);
            img_sub_cat = itemView.findViewById(R.id.img_sub_cat);

        }
    }
}

package com.kamaii.customer.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kamaii.customer.R;
import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.network.NetworkManager;
import com.kamaii.customer.ui.activity.DiscoverActivity;
import com.kamaii.customer.ui.fragment.CheckInternetFragment;
import com.kamaii.customer.ui.models.PopulerServiceModel;
import com.kamaii.customer.utils.CustomTextView;
import com.kamaii.customer.utils.CustomTextViewBold;

import java.util.List;

import static com.kamaii.customer.utils.ProjectUtils.TAG;

/*public class PopulerServiceAdapter extends BaseAdapter {

    Context context;
    List<PopulerServiceModel> arrayList;
    FragmentManager fragmentManager;
    public PopulerServiceAdapter(Context context, List<PopulerServiceModel> arrayList, FragmentManager fragmentManager) {
        this.context = context;
        this.arrayList = arrayList;
        this.fragmentManager = fragmentManager;
    }


    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.populer_service_recycle_layout, parent, false);

        CustomTextViewBold txt_contain;
        CustomTextView total_items;
        ImageView img_sub_cat;
        LinearLayout item_layout;

        txt_contain = v.findViewById(R.id.populer_service_txt_contain);
        img_sub_cat = v.findViewById(R.id.populer_service_img_sub_cat);
        total_items = v.findViewById(R.id.populer_service_total_items);

        txt_contain.setText(arrayList.get(position).getSubcat_name());
        total_items.setText(arrayList.get(position).getTotal_product());
        Glide.with(this.context).load(arrayList.get(position).getSubcat_image()).placeholder(R.drawable.default_image).into(img_sub_cat);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!NetworkManager.isConnectToInternet(context)) {

                    Fragment fragment = new CheckInternetFragment();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                            android.R.anim.fade_out);
                    fragmentTransaction.replace(R.id.frame, fragment, TAG);
                    fragmentTransaction.commitAllowingStateLoss();


                } else {
                    Intent intent = new Intent(context, DiscoverActivity.class);
                    intent.putExtra(Consts.SUB_CATEGORY_ID, arrayList.get(position).getSubcat_id());
                    intent.putExtra(Consts.Sub_CAT_NAME, arrayList.get(position).getSubcat_name());
                    intent.putExtra(Consts.CATEGORY_ID, arrayList.get(position).getCat_id());
                    context.startActivity(intent);
                }
            }
        });



        return v;
    }
}*/

public class PopulerServiceAdapter extends RecyclerView.Adapter<PopulerServiceAdapter.PopulerServiceViewholder> {

    Context context;
    List<PopulerServiceModel> arrayList;
    FragmentManager fragmentManager;

    public PopulerServiceAdapter(Context context, List<PopulerServiceModel> arrayList, FragmentManager fragmentManager) {
        this.context = context;
        this.arrayList = arrayList;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public PopulerServiceViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.populer_service_recycle_layout, parent, false);
        return new PopulerServiceViewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PopulerServiceViewholder holder, int position) {

        holder.txt_contain.setText(arrayList.get(position).getSubcat_name());
        holder.total_items.setText(arrayList.get(position).getTotal_product());
        Glide.with(this.context).load(arrayList.get(position).getSubcat_image()).placeholder(R.drawable.default_image).into(holder.img_sub_cat);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!NetworkManager.isConnectToInternet(context)) {

                    Fragment fragment = new CheckInternetFragment();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                            android.R.anim.fade_out);
                    fragmentTransaction.replace(R.id.frame, fragment, TAG);
                    fragmentTransaction.commitAllowingStateLoss();


                } else {
                    Intent intent = new Intent(context, DiscoverActivity.class);
                    intent.putExtra(Consts.SUB_CATEGORY_ID, arrayList.get(position).getSubcat_id());
                    intent.putExtra(Consts.Sub_CAT_NAME, arrayList.get(position).getSubcat_name());
                    intent.putExtra(Consts.CATEGORY_ID, arrayList.get(position).getCat_id());
                    context.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class PopulerServiceViewholder extends RecyclerView.ViewHolder {

        CustomTextViewBold txt_contain;
        CustomTextView total_items;
        ImageView img_sub_cat;
        LinearLayout item_layout;

        public PopulerServiceViewholder(@NonNull View itemView) {
            super(itemView);
            txt_contain = itemView.findViewById(R.id.populer_service_txt_contain);
            img_sub_cat = itemView.findViewById(R.id.populer_service_img_sub_cat);
            total_items = itemView.findViewById(R.id.populer_service_total_items);


        }
    }
}

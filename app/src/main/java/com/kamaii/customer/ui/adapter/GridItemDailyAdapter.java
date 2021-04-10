package com.kamaii.customer.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kamaii.customer.DTO.CategoryDTO;
import com.kamaii.customer.R;
import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.interfacess.OnDataErrorListener;
import com.kamaii.customer.network.NetworkManager;
import com.kamaii.customer.ui.activity.CashBackOfferActivity;
import com.kamaii.customer.ui.activity.SubCategoryActivity;
import com.kamaii.customer.ui.fragment.CheckInternetFragment;
import com.kamaii.customer.utils.CustomTextView;
import com.kamaii.customer.utils.CustomTextViewBold;

import java.util.ArrayList;
import java.util.List;

import static com.kamaii.customer.utils.ProjectUtils.TAG;

public class GridItemDailyAdapter extends RecyclerView.Adapter<GridItemDailyAdapter.ItemViewHolder> {
    Context context;
    private List<CategoryDTO> list, FilterdList;


    private OnDataErrorListener dataErrorListener;
    private FragmentManager fragmentManager;

    public GridItemDailyAdapter(FragmentManager fragmentManager) {
        list = new ArrayList<>();
        this.fragmentManager = fragmentManager;
        FilterdList = new ArrayList<>();
    }

    public void addInner(List<CategoryDTO> inners) {
        list.clear();
        FilterdList.clear();
        FilterdList.addAll(inners);
        list.addAll(inners);
        notifyDataSetChanged();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView grid_item_image;
        CardView llmain;
        CustomTextViewBold text_view_item_category;

        public ItemViewHolder(View view) {
            super(view);
            this.grid_item_image = (ImageView) view.findViewById(R.id.image_view_item_category);
            this.text_view_item_category = (CustomTextViewBold) view.findViewById(R.id.text_view_item_category);
        }
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        return new ItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_category_daily, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int i) {
        int pos = i;

        itemViewHolder.text_view_item_category.setText(FilterdList.get(pos).getCat_name());
        Glide.with(this.context).load((FilterdList.get(pos).getImgurl())).placeholder(R.drawable.default_image).into(itemViewHolder.grid_item_image);

        itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!NetworkManager.isConnectToInternet(context)) {

                    Fragment fragment = new CheckInternetFragment();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                            android.R.anim.fade_out);
                    fragmentTransaction.replace(R.id.frame, fragment, TAG);
                    fragmentTransaction.commitAllowingStateLoss();

                } else {

                    Intent intent = new Intent(context, SubCategoryActivity.class);
                    intent.putExtra(Consts.CATEGORY_ID, FilterdList.get(pos).getId());
                    intent.putExtra(Consts.CATEGORY_NAME, FilterdList.get(pos).getCat_name());
                    intent.putExtra(Consts.CATEGORY_IMAGE, FilterdList.get(pos).getImgurl());
                    context.startActivity(intent);

                }
            }
        });

    }

    public void setDataErrorListener(OnDataErrorListener dataErrorListener) {
        this.dataErrorListener = dataErrorListener;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return FilterdList.size();
    }


}


package com.kamaii.customer.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kamaii.customer.DTO.CategoryDTO;
import com.kamaii.customer.R;
import com.kamaii.customer.ui.fragment.CategoryFragment;
import com.kamaii.customer.ui.models.ParentModel;
import com.kamaii.customer.utils.ItemDecorationAlbumColumns;

import java.util.ArrayList;
import java.util.List;


public class OuterAdapter extends RecyclerView.Adapter<OuterAdapter.ViewHolder>  {

    private final RecyclerView.RecycledViewPool recycledViewPool;
    private Context context;
    private FragmentManager fragmentManager;
    private ArrayList<ParentModel> outerList;
    CategoryFragment categoryFragment;

    CategoryHorizontalAdapter adapter;
    public OuterAdapter(CategoryFragment categoryFragment, FragmentManager fragmentManager) {
        categoryFragment = this.categoryFragment;
        this.fragmentManager=fragmentManager;
        outerList = new ArrayList<>();
        recycledViewPool = new RecyclerView.RecycledViewPool();
        Log.e("OUTER_ADAPTER_LIST",""+outerList.toString());
    }

    public void addOuter(ParentModel outer) {
        outerList.add(outer);
        Log.e("OUTER_ADAPTER_LIST"," ParentModel "+outer.toString());
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_parent_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.rvInner.setRecycledViewPool(recycledViewPool);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return outerList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView text_product_type;
        RecyclerView rvInner;
        private GridItemAdapter innerAdapter;

        public ViewHolder(View itemView) {
            super(itemView);
            text_product_type=itemView.findViewById(R.id.text_product_type);
            rvInner=itemView.findViewById(R.id.rec_child_product);
            setupRv();
        }

        private void setupRv() {
            rvInner.setLayoutManager(new GridLayoutManager(context, 4));
            rvInner.addItemDecoration(new ItemDecorationAlbumColumns(
                    5,
                    5));

           innerAdapter = new GridItemAdapter(fragmentManager);

            rvInner.setAdapter(innerAdapter);
        }

        public void bind(int position) {
            ParentModel outer = outerList.get(position);
            if (outer != null) {
                text_product_type.setText(outer.getName());
                    innerAdapter.addInner(outer.getChildModelList());
            }


        }


    }



}

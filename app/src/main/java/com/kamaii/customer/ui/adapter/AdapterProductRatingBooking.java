package com.kamaii.customer.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kamaii.customer.DTO.ProductDTO;
import com.kamaii.customer.R;
import com.kamaii.customer.ui.fragment.MyBooking;
import com.kamaii.customer.utils.CustomTextView;
import com.kamaii.customer.utils.CustomTextViewBold;

import java.util.ArrayList;
import java.util.HashMap;


public class AdapterProductRatingBooking extends RecyclerView.Adapter<AdapterProductRatingBooking.MyViewHolder> {

    private LayoutInflater mLayoutInflater;
    private MyBooking myBooking;
    private Context context;
    private ArrayList<ProductDTO> productDTOList;
    private int po;
    private String TAG = MyBooking.class.getSimpleName();
    private HashMap<String, String> params = new HashMap<>();
    Boolean isCheck= true;

    public AdapterProductRatingBooking(Context myBooking, ArrayList<ProductDTO> productDTOList) {

        context = myBooking;
        this.productDTOList = productDTOList;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.e("RECYCLRDATA",""+productDTOList.toString());
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_rating_product, parent, false);

        return new MyViewHolder(itemView);
    }

    public void NotifyAll(ArrayList<ProductDTO> productDTOList)
    {
        this.productDTOList=productDTOList;
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder,  int position) {
        holder.product_name.setText(productDTOList.get(position).getProduct_name());
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return productDTOList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {


        public CustomTextViewBold product_name;
        public MyViewHolder(View view) {
            super(view);
            product_name = (CustomTextViewBold) view.findViewById(R.id.product_name);
        }
    }
}

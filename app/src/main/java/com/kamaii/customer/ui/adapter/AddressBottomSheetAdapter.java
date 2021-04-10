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

import com.kamaii.customer.R;
import com.kamaii.customer.https.HttpsRequest;
import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.interfacess.Helper;
import com.kamaii.customer.interfacess.OnAddressSelected;
import com.kamaii.customer.ui.activity.BookingProduct;
import com.kamaii.customer.ui.models.TypeAddressModel;
import com.kamaii.customer.utils.CustomTextView;
import com.kamaii.customer.utils.CustomTextViewBold;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class AddressBottomSheetAdapter extends RecyclerView.Adapter<AddressBottomSheetAdapter.AddressViewholder> {

    Context context;
    List<TypeAddressModel> homeList;
    OnAddressSelected onAddressSelected;
    BookingProduct bookingProduct;
    HashMap<String, String> deleteAddressHashmap = new HashMap<>();

    public AddressBottomSheetAdapter(Context context, List<TypeAddressModel> homeList, OnAddressSelected onAddressSelected) {
        this.context = context;
        this.homeList = homeList;
        this.onAddressSelected = onAddressSelected;
    }

    @NonNull
    @Override
    public AddressViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.address_recycle_layout, parent, false);
        return new AddressViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewholder holder, int position) {
        String comma = "";
        holder.add_type.setText(homeList.get(position).getAdd_type());
        /*if (homeList.get(position).getSociety_name().equals("") || homeList.get(position).getSociety_name().isEmpty()) {
            comma = " ";
        } else {
            comma = homeList.get(position).getSociety_name() + ", ";
        }*/

        if(homeList.get(position).getHouse_no().isEmpty() && homeList.get(position).getSociety_name().isEmpty()){
            Log.e("ADDRESS_ADAPTER","1");
            holder.address_txt.setText(homeList.get(position).getStreet_address());
        }
        else if(homeList.get(position).getHouse_no().isEmpty()){
            Log.e("ADDRESS_ADAPTER","2");
            holder.address_txt.setText(homeList.get(position).getSociety_name() + ", " + comma + homeList.get(position).getStreet_address());
            //binding.tvAddress.setText(soc_name + ", " + street_add);
        }
        else if (homeList.get(position).getSociety_name().isEmpty()){
            Log.e("ADDRESS_ADAPTER","3");
            holder.address_txt.setText(homeList.get(position).getHouse_no() + ", " + comma + homeList.get(position).getStreet_address());

            //binding.tvAddress.setText(house_no + ", "+ street_add);
        }
        else {
            //Log.e("ADDRESS_ADAPTER"," 4 "+"house no:-"+house_no+"society:-"+soc_name);

            //binding.tvAddress.setText(house_no + ", " + soc_name + ", " + street_add);
            holder.address_txt.setText(homeList.get(position).getHouse_no() + ", " +homeList.get(position).getSociety_name() + ", " + homeList.get(position).getStreet_address());
        }

        if (position == homeList.size() - 1) {
            holder.line.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (homeList.get(position).getSociety_name().isEmpty()){
                    Log.e("ADDRESS_ADAPTER","empty 1");

                    Log.e("ADDRESS_ADAPTER","houseno_"+homeList.get(position).getHouse_no()+"societyname_"+homeList.get(position).getSociety_name());
                }
                else if(homeList.get(position).getHouse_no().isEmpty()){
                    Log.e("ADDRESS_ADAPTER","empty 2");

                    //"societyname_"+homeList.get(position).getSociety_name();
                }
//                Log.e("ADDRESS_ADAPTER",);

                onAddressSelected.setAddress(position, homeList.get(position).getAddress_id(), homeList.get(position).getHouse_no(), homeList.get(position).getStreet_address(), homeList.get(position).getSociety_name(),Double.parseDouble(homeList.get(position).getLatitude()),Double.parseDouble(homeList.get(position).getLongitude()));
                ((BookingProduct) context).changingtext();
            }
        });
        holder.delete_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BookingProduct) context).deleteaddress(homeList.get(position).getAddress_id());
                int newPosition = holder.getAdapterPosition();
                homeList.remove(newPosition);
                notifyItemRemoved(newPosition);
                notifyItemRangeChanged(newPosition, homeList.size());

            }
        });
    }

    @Override
    public int getItemCount() {
        return homeList.size();
    }

    class AddressViewholder extends RecyclerView.ViewHolder {

        CustomTextViewBold add_type;
        CustomTextView address_txt;
        TextView line;
        ImageView delete_address;

        public AddressViewholder(@NonNull View itemView) {
            super(itemView);

            delete_address = itemView.findViewById(R.id.delete_address);
            add_type = itemView.findViewById(R.id.add_type);
            address_txt = itemView.findViewById(R.id.address_txt);
            line = itemView.findViewById(R.id.add_recycle_view);
        }
    }
}

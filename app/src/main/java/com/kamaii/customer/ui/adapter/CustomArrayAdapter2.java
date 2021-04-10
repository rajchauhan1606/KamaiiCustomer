package com.kamaii.customer.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.kamaii.customer.R;
import com.kamaii.customer.ui.activity.Booking;
import com.kamaii.customer.ui.activity.BookingProduct;
import com.kamaii.customer.utils.CustomTextView;
import com.kamaii.customer.utils.CustomTextViewBold;

import java.util.List;

public class CustomArrayAdapter2 extends ArrayAdapter<String> {

    private BookingProduct booking;
    private List<String> objects;
    private Context context;
    int pos = -1;

    public CustomArrayAdapter2(Context context, int resourceId,
                               List<String> objects) {
        super(context, resourceId, objects);
        booking = (BookingProduct) context;
        this.objects = objects;
        this.context = context;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.payment_mode_spinner_layout, parent, false);
        RadioButton radioButton = row.findViewById(R.id.payment_dialog_radio);
        CustomTextViewBold label = row.findViewById(R.id.payment_type);
        RelativeLayout cliking = row.findViewById(R.id.cliking);
        CustomTextView desc = row.findViewById(R.id.payment_type_description);

        radioButton.setClickable(false);
        label.setText(objects.get(position));
        desc.setText(objects.get(position));
        if (desc.getText().toString().equals("Online")) {
            desc.setText("2% extra charges");
        } else {
            desc.setText("No extra charges");
        }

        cliking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos = position;
                notifyDataSetChanged();
                booking.spinner_txt.setText(label.getText().toString());
            }
        });

        if (pos == position) {
            radioButton.setChecked(true);
            row.setBackgroundColor(context.getResources().getColor(R.color.gray_light));
        }

        return row;
    }


}

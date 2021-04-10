package com.kamaii.customer.ui.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.kamaii.customer.R;
import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.ui.activity.SubCategoryActivity;
import com.kamaii.customer.ui.models.FooterSliderModel;

import java.util.ArrayList;

public class FooterSliderAdapter extends PagerAdapter {

    private ArrayList<FooterSliderModel> images;
    private LayoutInflater inflater;
    private Context context;

    public FooterSliderAdapter(Context context, ArrayList<FooterSliderModel> images) {
        this.context = context;
        this.images = images;
        Log.e("FOOTER", "" + images.get(0).getSlider_image());
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {

        View myImageLayout = inflater.inflate(R.layout.slide, view, false);
        ImageView myImage = myImageLayout.findViewById(R.id.image);

        Glide.with(context)
                .load(images.get(position).getSlider_image())
                .into(myImage);
        view.addView(myImageLayout, 0);

        myImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (images.get(position).getCategory_id().equalsIgnoreCase("0")) {
                    Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(images.get(position).getLink()));
                    try {
                        context.startActivity(webIntent);
                    } catch (ActivityNotFoundException ex) {
                    }
                } else {
                    Intent intent = new Intent(context, SubCategoryActivity.class);
                    intent.putExtra(Consts.CATEGORY_ID, images.get(position).getCategory_id());
                    intent.putExtra(Consts.CATEGORY_NAME, images.get(position).getCategory_name());
                    context.startActivity(intent);
                }

            }
        });
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}

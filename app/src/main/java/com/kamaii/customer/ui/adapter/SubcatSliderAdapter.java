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

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.kamaii.customer.R;
import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.ui.activity.SubCategoryActivity;
import com.kamaii.customer.ui.models.SubcatSlidermodel;
import com.kamaii.customer.ui.models.SubcatSlidermodel;

import java.util.ArrayList;

public class SubcatSliderAdapter extends PagerAdapter {

    private ArrayList<SubcatSlidermodel> images;
    private LayoutInflater inflater;
    private Context context;

    public SubcatSliderAdapter(Context context, ArrayList<SubcatSlidermodel> images) {
        this.context = context;
        this.images = images;
        inflater = LayoutInflater.from(context);

        Log.e("Sliderimagesizenew",""+images.size());
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

        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}

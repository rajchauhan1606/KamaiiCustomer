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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.kamaii.customer.R;
import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.network.NetworkManager;
import com.kamaii.customer.ui.activity.CashBackOfferActivity;
import com.kamaii.customer.ui.activity.SubCategoryActivity;
import com.kamaii.customer.ui.fragment.CategoryFragment;
import com.kamaii.customer.ui.fragment.CheckInternetFragment;
import com.kamaii.customer.ui.models.SiderModel;

import java.util.ArrayList;

import static com.kamaii.customer.utils.ProjectUtils.TAG;
import static java.security.AccessController.getContext;

public class SliderAdapter extends PagerAdapter {

    private ArrayList<SiderModel> images;
    private LayoutInflater inflater;
    private Context context;
    private FragmentManager fragmentManager;

    public SliderAdapter(Context context, ArrayList<SiderModel> images, FragmentManager fragmentManager) {
        this.context = context;
        this.images = images;
        this.fragmentManager = fragmentManager;
        inflater = LayoutInflater.from(context);

        Log.e("Sliderimagesize", "" + images.size());
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

                    if (!NetworkManager.isConnectToInternet(context)) {

                        Fragment fragment = new CheckInternetFragment();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                                android.R.anim.fade_out);
                        fragmentTransaction.replace(R.id.frame, fragment, TAG);
                        fragmentTransaction.commitAllowingStateLoss();

                    } else {

                        Intent intent = new Intent(context, SubCategoryActivity.class);
                        intent.putExtra("fragmentManager", String.valueOf(fragmentManager));
                        intent.putExtra(Consts.CATEGORY_ID, images.get(position).getCategory_id());
                        intent.putExtra(Consts.CATEGORY_NAME, images.get(position).getCategory_name());
                        context.startActivity(intent);

                    }

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
/* cyclic adapter
public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewholder> {

    private ArrayList<SiderModel> images;
    private LayoutInflater inflater;
    private Context context;
    private FragmentManager fragmentManager;

    public SliderAdapter(Context context, ArrayList<SiderModel> images, FragmentManager fragmentManager) {
        this.context = context;
        this.images = images;
        this.fragmentManager = fragmentManager;
        inflater = LayoutInflater.from(context);

        Log.e("Sliderimagesize", "" + images.size());
    }

    @NonNull
    @Override
    public SliderViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.slide,parent,false);
        return new SliderViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewholder holder, int position) {

        Glide.with(context).load(images.get(position).getSlider_image()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    class SliderViewholder extends RecyclerView.ViewHolder {

        ImageView image;
        public SliderViewholder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
        }
    }

}
*/
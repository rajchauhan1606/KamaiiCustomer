package com.kamaii.customer.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import com.ToxicBakery.viewpager.transforms.StackTransformer;
import com.kamaii.customer.R;
import com.kamaii.customer.databinding.ActivityAppIntro2Binding;
import com.kamaii.customer.preferences.SharedPrefrence;
import com.kamaii.customer.ui.adapter.AppIntroPagerAdapter;
import com.kamaii.customer.utils.CustomTextView;
import com.kamaii.customer.utils.CustomTextViewBold;
import com.kamaii.customer.utils.ProjectUtils;


public class AppIntro extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private AppIntroPagerAdapter mAdapter;
    private int dotsCount;
    private ImageView[] dots;
    public SharedPrefrence preference;
    private Context mContext;
    int[] mResources = {R.drawable.first_intro, R.drawable.second_intro, R.drawable.third_intro};
    private ActivityAppIntro2Binding binding;
    CustomTextViewBold skip_txt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.Fullscreen(AppIntro.this);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_app_intro2);
        mContext = AppIntro.this;
        preference = SharedPrefrence.getInstance(mContext);



        binding.skipTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AppIntro.this, CheckSignInActivity.class));
            }
        });

        mAdapter = new AppIntroPagerAdapter(AppIntro.this, mContext, mResources);
        binding.viewpager.setAdapter(mAdapter);
        binding.viewpager.setPageTransformer(true, new StackTransformer());
        binding.viewpager.setCurrentItem(0);
        binding.viewpager.setOnPageChangeListener(this);
        binding.btnnext.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                if(binding.viewpager.getCurrentItem()==0)
                {
                    binding.viewpager.setCurrentItem(binding.viewpager.getCurrentItem()+1);
                }
                else if (binding.viewpager.getCurrentItem()==1){
                    binding.viewpager.setCurrentItem(binding.viewpager.getCurrentItem()+1);

                }
                else if(binding.viewpager.getCurrentItem()==2) {
                    Intent intent=new Intent(AppIntro.this,CheckSignInActivity.class);
                    startActivity(intent);
                }

            }
        });


    }




    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void scrollPage(int position) {
        binding.viewpager.setCurrentItem(position);
    }

    @Override
    public void onBackPressed() {
        clickDone();
    }

    public void clickDone() {
        new AlertDialog.Builder(this)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle(getString(R.string.app_name))
                .setMessage(getResources().getString(R.string.closeMsg))
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent i = new Intent();
                        i.setAction(Intent.ACTION_MAIN);
                        i.addCategory(Intent.CATEGORY_HOME);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);

                        finish();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }


}

package com.kamaii.customer.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.kamaii.customer.DTO.ArtistDetailsDTO;
import com.kamaii.customer.DTO.ProductDTO;
import com.kamaii.customer.DTO.QualificationsDTO;
import com.kamaii.customer.DTO.UserDTO;
import com.kamaii.customer.R;
import com.kamaii.customer.databinding.ActivityArtistProfileBinding;
import com.kamaii.customer.https.HttpsRequest;
import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.interfacess.Helper;
import com.kamaii.customer.network.NetworkManager;
import com.kamaii.customer.preferences.SharedPrefrence;
import com.kamaii.customer.ui.adapter.AdapterCart;
import com.kamaii.customer.ui.adapter.QualificationAdapter;
import com.kamaii.customer.ui.fragment.ImageGallery;
import com.kamaii.customer.ui.fragment.PersnoalInfo;
import com.kamaii.customer.ui.fragment.PreviousWork;
import com.kamaii.customer.ui.fragment.Reviews;
import com.kamaii.customer.ui.fragment.ServiceProfile;
import com.kamaii.customer.utils.ItemDecorationAlbumColumns;
import com.kamaii.customer.utils.ProjectUtils;

import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.kamaii.customer.interfacess.Consts.LATITUDE;
import static com.kamaii.customer.interfacess.Consts.LONGITUDE;

public class ArtistProfile extends AppCompatActivity implements View.OnClickListener, AppBarLayout.OnOffsetChangedListener {

    private String TAG = ArtistProfile.class.getSimpleName();
    private Context mContext;
    private String artist_id = "";
    private ArtistDetailsDTO artistDetailsDTO;
    private HashMap<String, String> parms = new HashMap<>();
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private HashMap<String, String> paramsFav = new HashMap<>();
    SimpleDateFormat sdf1, timeZone;
    public static String name = "", email = "";
    private ArrayList<String> list;
    public static String changes_price = "";
    private PersnoalInfo persnoalInfo = new PersnoalInfo();
    private ServiceProfile serviceList = new ServiceProfile();
    private ImageGallery imageGallery = new ImageGallery();
    private PreviousWork previousWork = new PreviousWork();
    private Reviews reviews = new Reviews();
    private int screen_tag = 0;
    private Bundle bundle;

    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 20;
    private boolean mIsAvatarShown = true;
    private int mMaxScrollSize;
    private DialogInterface dialog_book;
    private HashMap<String, String> paramsBookingOp = new HashMap<>();
    public static HashMap<String, String> cartHashmap;
    private Date date;
    private HashMap<String, String> paramBookAppointment = new HashMap<>();
    private QualificationAdapter qualificationAdapter;
    private ArrayList<QualificationsDTO> qualificationsDTOList;
    private LinearLayoutManager mLayoutManagerQuali;
    private ActivityArtistProfileBinding binding;
    int flag = 0;
    String subcategoryid = "";
    public static ArrayList<ProductDTO> cartarraylist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_artist_profile);
        mContext = ArtistProfile.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        sdf1 = new SimpleDateFormat(Consts.DATE_FORMATE_SERVER, Locale.ENGLISH);
        timeZone = new SimpleDateFormat(Consts.DATE_FORMATE_TIMEZONE, Locale.ENGLISH);
        date = new Date();
        userDTO = prefrence.getParentUser(Consts.USER_DTO);

        if (getIntent().hasExtra(Consts.ARTIST_ID)) {
            if (getIntent().hasExtra(Consts.FLAG)) {
                flag = getIntent().getIntExtra(Consts.FLAG, 0);
            }
            artist_id = getIntent().getStringExtra(Consts.ARTIST_ID);
        }
        if (getIntent().hasExtra(Consts.SUB_CATEGORY_ID)) {
            parms.put(Consts.SUB_CATEGORY_ID, getIntent().getStringExtra(Consts.SUB_CATEGORY_ID));
        } else {
            parms.put(Consts.SUB_CATEGORY_ID, "");
        }
        parms.put(Consts.ARTIST_ID, artist_id);
        parms.put(Consts.USER_ID, userDTO.getUser_id());

        paramsFav.put(Consts.ARTIST_ID, artist_id);
        paramsFav.put(Consts.USER_ID, userDTO.getUser_id());
        setUiAction();


        if (getIntent().hasExtra(Consts.ARTIST_DTO)) {
            artistDetailsDTO = (ArtistDetailsDTO) getIntent().getSerializableExtra(Consts.ARTIST_DTO);
            artist_id = getIntent().getStringExtra(Consts.ARTIST_ID);
            changes_price = getIntent().getStringExtra(Consts.CHANGE_PRICE);
            cartHashmap = (HashMap<String, String>) getIntent().getSerializableExtra("cartHashmap");
            screen_tag = getIntent().getIntExtra(Consts.SCREEN_TAG, 0);

            if (screen_tag == 2) {
                Bundle b = getIntent().getExtras();
                cartarraylist = (ArrayList<ProductDTO>) b.getSerializable(Consts.CARTDATA);
            }
        }
    }

    public void setUiAction() {
        binding.llBack.setOnClickListener(this);
        binding.tvChat.setOnClickListener(this);

        binding.tvAppointment.setOnClickListener(this);
        binding.tvBookNow.setOnClickListener(this);
        binding.ivFav.setOnClickListener(this);
        binding.tvViewServices.setOnClickListener(this);


        if (NetworkManager.isConnectToInternet(mContext)) {
            getArtist();

        } else {
            ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
        }
        if (flag == 1) {
            binding.llBottom.setVisibility(View.GONE);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent in = getIntent();
        Uri data = in.getData();
        if (data!=null){
            List<String> uriData = data.getPathSegments();
            artist_id = uriData.get(uriData.size()-1);
            //artist_id = "4";
            parms.put(Consts.ARTIST_ID, artist_id);
            parms.put(Consts.USER_ID, userDTO.getUser_id());

            setUiAction();

        }

//        System.out.println("deeplinkingcallback   :- "+data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvBookNow:

                if (NetworkManager.isConnectToInternet(mContext)) {
                    if (artistDetailsDTO != null) {
                        if (artistDetailsDTO.getProducts() != null) {
                            Intent viewService = new Intent(mContext, Booking.class);
                            viewService.putExtra(Consts.ARTIST_DTO, artistDetailsDTO);
                            viewService.putExtra(Consts.ARTIST_ID, artist_id);
                            viewService.putExtra(Consts.SCREEN_TAG, 1);
                            mContext.startActivity(viewService);
                        } else {
                            ProjectUtils.showLong(mContext, getResources().getString(R.string.no_services_found));
                        }

                    } else {
                        ProjectUtils.showLong(mContext, getResources().getString(R.string.no_data_found));
                    }
                } else {
                    ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
                }


                break;
            case R.id.tvAppointment:
                if (NetworkManager.isConnectToInternet(mContext)) {
                    paramBookAppointment.put(Consts.USER_ID, userDTO.getUser_id());
                    paramBookAppointment.put(Consts.ARTIST_ID, artistDetailsDTO.getUser_id());
                    clickScheduleDateTime();
                } else {
                    ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
                }


                break;
            case R.id.llBack:
                finish();
                break;
            case R.id.ivFav:
                if (NetworkManager.isConnectToInternet(mContext)) {
                    if (artistDetailsDTO.getFav_status().equalsIgnoreCase("1")) {
                        removeFav();
                    } else {
                        addFav();
                    }

                } else {
                    ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
                }


                break;
            case R.id.tvChat:
                if (NetworkManager.isConnectToInternet(mContext)) {
                    Intent in = new Intent(mContext, OneTwoOneChat.class);
                    in.putExtra(Consts.ARTIST_ID, artistDetailsDTO.getUser_id());
                    in.putExtra(Consts.ARTIST_NAME, artistDetailsDTO.getName());
                    mContext.startActivity(in);
                } else {
                    ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
                }

                break;
            case R.id.tvViewServices:
                if (NetworkManager.isConnectToInternet(mContext)) {
                    if (artistDetailsDTO != null) {
                        if (artistDetailsDTO.getProducts() != null) {
                            Intent viewService = new Intent(mContext, ViewServices.class);
                            viewService.putExtra(Consts.ARTIST_DTO, artistDetailsDTO);
                            viewService.putExtra(Consts.ARTIST_ID, artist_id);
                            mContext.startActivity(viewService);
                        } else {
                            ProjectUtils.showLong(mContext, getResources().getString(R.string.no_services_found));
                        }
                    } else {
                        ProjectUtils.showLong(mContext, getResources().getString(R.string.no_services_found));
                    }
                } else {
                    ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
                }

                break;

        }
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    public void getArtist() {
       // ProjectUtils.showProgressDialog(mContext, false, getResources().getString(R.string.please_wait));

        new HttpsRequest(Consts.GET_ARTIST_BY_ID_API_ARTIST, parms, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

              //  Log.e("ALLDATAAAYO", parms.toString());
             //   ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {
                        Log.e("aaaaa", "" + response.toString());
                        artistDetailsDTO = new Gson().fromJson(response.getJSONObject("data").toString(), ArtistDetailsDTO.class);
                        showData();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                }
            }
        });
    }

    public void showData() {
        bundle = new Bundle();
        bundle.putSerializable(Consts.ARTIST_DTO, artistDetailsDTO);

        binding.tvName.setText(artistDetailsDTO.getName());
        binding.tvWork.setText(artistDetailsDTO.getCategory_name());

        binding.tvLocation.setText(artistDetailsDTO.getLocation());
        binding.ratingbar.setRating(Float.parseFloat(artistDetailsDTO.getAva_rating()));
        binding.tvRating.setText("(" + artistDetailsDTO.getAva_rating() + "/5)");

        binding.tvAbout.setText(artistDetailsDTO.getAbout_us());
        if (artistDetailsDTO.getSlotnote().equals("0")) {
            binding.slotnote.setVisibility(View.GONE);
        } else {
            binding.slotnote.setVisibility(View.VISIBLE);
            binding.slotnote.setText(Html.fromHtml(artistDetailsDTO.getSlotnote()));
        }
        if (Integer.parseInt(artistDetailsDTO.getJobDone()) > 1) {
            binding.tvJobComplete.setText(artistDetailsDTO.getJobDone() + " " + getResources().getString(R.string.jobs_completed));
        } else {
            binding.tvJobComplete.setText(artistDetailsDTO.getJobDone() + " " + getResources().getString(R.string.jobs_completed_single));
        }
        binding.tvProfileComplete.setText(artistDetailsDTO.getName());
        binding.tvProfileComplete.setText(artistDetailsDTO.getCompletePercentages() + "" + getResources().getString(R.string.completion));


        mLayoutManagerQuali = new LinearLayoutManager(ArtistProfile.this);
        binding.rvQualification.setLayoutManager(mLayoutManagerQuali);
        qualificationsDTOList = new ArrayList<>();
        qualificationsDTOList = artistDetailsDTO.getQualifications();
        qualificationAdapter = new QualificationAdapter(ArtistProfile.this, qualificationsDTOList);
        binding.rvQualification.setAdapter(qualificationAdapter);

        if (artistDetailsDTO.getFav_status().equalsIgnoreCase("1")) {
            binding.ivFav.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_fav_full));
        } else {
            binding.ivFav.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_fav_blank));
        }

        Glide.with(mContext).
                load(artistDetailsDTO.getImage())
                .placeholder(R.drawable.dummyuser_image)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.ivArtist);

        Glide.with(mContext).
                load(artistDetailsDTO.getArtist_banner())
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.artistBanner);

        setupViewPager(binding.viewPager);
        binding.tabLayout.setupWithViewPager(binding.viewPager);

    }

    public void doredirect(String subid) {
        Bundle b = new Bundle();
        b.putSerializable(Consts.CARTDATA, (Serializable) cartarraylist);

        Intent in = new Intent(this, ViewServices.class);
        in.putExtra(Consts.ARTIST_DTO, artistDetailsDTO);
        in.putExtra(Consts.ARTIST_ID, artistDetailsDTO.getUser_id());
        in.putExtra(Consts.SCREEN_TAG, 2);
        in.putExtra("cartHashmap", cartHashmap);
        in.putExtra(Consts.CHANGE_PRICE, changes_price.toString());
        in.putExtra("artist_name", artistDetailsDTO.getName());
        in.putExtra(Consts.SUB_CATEGORY_ID, subid);

        in.putExtras(b);
        startActivity(in);
    }

    public void addFav() {
        ProjectUtils.showProgressDialog(mContext, false, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.ADD_FAVORITES_API, paramsFav, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    ProjectUtils.showToast(mContext, msg);
                    getArtist();
                } else {
                    ProjectUtils.showToast(mContext, msg);
                }
            }
        });
    }

    public void removeFav() {
        ProjectUtils.showProgressDialog(mContext, false, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.REMOVE_FAVORITES_API, paramsFav, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    ProjectUtils.showToast(mContext, msg);
                    getArtist();
                } else {
                    ProjectUtils.showToast(mContext, msg);
                }
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {

        serviceList.setArguments(bundle);
        imageGallery.setArguments(bundle);
        previousWork.setArguments(bundle);
        reviews.setArguments(bundle);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(serviceList, "Service");
        adapter.addFragment(imageGallery, "Gallery");
        adapter.addFragment(reviews, "Reviews");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.getTotalScrollRange();

        int percentage = (Math.abs(i)) * 100 / mMaxScrollSize;

        if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && mIsAvatarShown) {
            mIsAvatarShown = false;

            binding.ivArtist.animate()
                    .scaleY(0).scaleX(0)
                    .setDuration(200)
                    .start();
        }

        if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !mIsAvatarShown) {
            mIsAvatarShown = true;

            binding.ivArtist.animate()
                    .scaleY(1).scaleX(1)
                    .start();
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    public void clickScheduleDateTime() {
        new SingleDateAndTimePickerDialog.Builder(this)
                .bottomSheet()
                .curved()
                .mustBeOnFuture()
                .defaultDate(new Date())
                .listener(new SingleDateAndTimePickerDialog.Listener() {
                    @Override
                    public void onDateSelected(Date date) {
                        paramBookAppointment.put(Consts.DATE_STRING, String.valueOf(sdf1.format(date).toString().toUpperCase()));
                        paramBookAppointment.put(Consts.TIMEZONE, String.valueOf(timeZone.format(date)));
                        bookAppointment();
                    }
                })
                .display();
    }

    public void bookAppointment() {

        ProjectUtils.showProgressDialog(mContext, true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.BOOK_APPOINTMENT_API, paramBookAppointment, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    ProjectUtils.pauseProgressDialog();
                    ProjectUtils.showToast(mContext, msg);

                } else {
                    ProjectUtils.showToast(mContext, msg);
                }
            }
        });
    }
}

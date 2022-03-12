package com.kamaii.customer.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.afollestad.viewpagerdots.DotsIndicator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.customer.DTO.CategoryDTO;
import com.kamaii.customer.DTO.UserBooking;
import com.kamaii.customer.DTO.UserDTO;
import com.kamaii.customer.R;
import com.kamaii.customer.api.apiClient;
import com.kamaii.customer.api.apiRest;
import com.kamaii.customer.https.HttpsRequest;
import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.interfacess.Helper;
import com.kamaii.customer.network.NetworkManager;
import com.kamaii.customer.preferences.SharedPrefrence;
import com.kamaii.customer.ui.activity.BaseActivity;
import com.kamaii.customer.ui.activity.BookingProduct;
import com.kamaii.customer.ui.activity.CashBackOfferActivity;
import com.kamaii.customer.ui.activity.ScrachActivity;
import com.kamaii.customer.ui.activity.SearchActivity;
import com.kamaii.customer.ui.activity.SubCategoryActivity;
import com.kamaii.customer.ui.activity.TrackingActivity;
import com.kamaii.customer.ui.adapter.FirstOuterAdapter;
import com.kamaii.customer.ui.adapter.FooterSliderAdapter;
import com.kamaii.customer.ui.adapter.OuterAdapter;
import com.kamaii.customer.ui.adapter.PopulerServiceAdapter;
import com.kamaii.customer.ui.adapter.SliderAdapter;
import com.kamaii.customer.ui.models.AddressModel;
import com.kamaii.customer.ui.models.FirstModel;
import com.kamaii.customer.ui.models.FooterSliderModel;
import com.kamaii.customer.ui.models.ParentModel;
import com.kamaii.customer.ui.adapter.PopulerPartnerAdapter;
import com.kamaii.customer.ui.models.PopulerPartnerModel;
import com.kamaii.customer.ui.models.PopulerServiceModel;
import com.kamaii.customer.ui.models.SiderModel;
import com.kamaii.customer.utils.AppController;
import com.kamaii.customer.utils.CustomTextView;
import com.kamaii.customer.utils.CustomTextViewBold;
import com.kamaii.customer.utils.ExpandableHeightGridView;
import com.kamaii.customer.utils.ItemDecorationAlbumColumns;
import com.kamaii.customer.utils.ProjectUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;
import static android.widget.Toast.LENGTH_LONG;
import static com.kamaii.customer.ui.activity.BaseActivity.CURRENT_TAG;
import static com.kamaii.customer.ui.activity.BaseActivity.TAG_OPEN;
import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

public class CategoryFragment extends Fragment {

    private View view;
    private SharedPrefrence prefrence;
    private String TAG = CategoryFragment.class.getSimpleName();
    private BaseActivity baseActivity;
    RecyclerView rec_category;
    RecyclerView rec_categoryDaily;
    RecyclerView partner_recyclerview;
    RecyclerView services_recyclerview;
    public RecyclerView horizontal_recyclerview;
    private HashMap<String, String> parmsCategory = new HashMap<>();
    private UserDTO userDTO;
    private CustomTextViewBold tvNo;
    private EditText etSearchFilter;
    ArrayList<SiderModel> silderarraylist = new ArrayList<>();
    ArrayList<PopulerPartnerModel> populerPartnerlist;
    ArrayList<PopulerServiceModel> populerServicelist;
    LinearLayout populer_partner_linear;
    LinearLayout populer_service_linear;
    ArrayList<FooterSliderModel> footersilderarraylist = new ArrayList<>();
    ArrayList<FooterSliderModel> footersilderarraylistFooter = new ArrayList<>();
    ViewPager mPager, pagerhometwo;
    private Dialog dialogapproxtime;
    ProgressDialog progressDialog;
    CustomTextViewBold tvskip, tvupdate, ctvbTitle;
    CustomTextView txtdesc;
    FragmentTransaction fragmentTransaction;
    private ArrayList<UserBooking> userBookingList;
    private SharedPreferences firebase;
    DotsIndicator dotsIndicator, dotsIndicator2;
    OuterAdapter outerAdapter;
    PopulerPartnerAdapter populerPartnerAdapter;
    PopulerServiceAdapter populerServiceAdapter;
    FirstOuterAdapter firstouterAdapter;
    private HashMap<String, String> parmsversion = new HashMap<>();
    private Dialog dailograting;
    CustomTextViewBold tvupdateapp, populer_partner_title_txt, populer_service_title_txt;
    ImageView img_sub_cat;
    ImageView bg_imageview;
    RelativeLayout first_relative_layout, second_relative_layout;
    boolean isCheck = false;
    String carticon_flag = "";
    String carticon_count = "";
    List<String> areaSpinnerList = new ArrayList<>();
    boolean load = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_category, container, false);
        prefrence = SharedPrefrence.getInstance(getActivity());

        getActivity().findViewById(R.id.ivLogo).setVisibility(View.GONE);
        getActivity().findViewById(R.id.customer_location_relative_header).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.headerNameTV).setVisibility(View.GONE);

        firebase = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        parmsCategory.put(Consts.USER_ID, userDTO.getUser_id());
        parmsCategory.put(Consts.DEVICE_TOKEN, firebase.getString(Consts.DEVICE_TOKEN, ""));

        dialogapproxtime = new Dialog(getActivity(), R.style.Theme_Dialog);
        dialogapproxtime.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogapproxtime.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogapproxtime.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        dialogapproxtime.setContentView(R.layout.dailog_approxtime);
        dialogapproxtime.setCancelable(false);
        tvskip = dialogapproxtime.findViewById(R.id.tvskip);
        tvupdate = dialogapproxtime.findViewById(R.id.tvupdate);
        ctvbTitle = dialogapproxtime.findViewById(R.id.ctvbTitle);
        txtdesc = dialogapproxtime.findViewById(R.id.txtdesc);
        bg_imageview = view.findViewById(R.id.category_frag_bg_img);
        mPager = view.findViewById(R.id.pagerhome);
        pagerhometwo = view.findViewById(R.id.category_pagerhometwo);
        populer_partner_title_txt = view.findViewById(R.id.populer_partner_title_txt);
        populer_service_title_txt = view.findViewById(R.id.populer_service_title_txt);
        dotsIndicator = view.findViewById(R.id.dots);
        dotsIndicator2 = view.findViewById(R.id.dots2);
        first_relative_layout = view.findViewById(R.id.first_card_relative);
        second_relative_layout = view.findViewById(R.id.second_card_relative);
        populer_partner_linear = view.findViewById(R.id.populer_partner_linear);
        populer_service_linear = view.findViewById(R.id.populer_service_linear);
        //getWelcomeBanner();


        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        tvskip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogapproxtime.dismiss();
            }
        });
        tvupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogapproxtime.dismiss();

            }
        });


        view.findViewById(R.id.cashback_linear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), CashBackOfferActivity.class));
            }
        });

        view.findViewById(R.id.became_a_partnet_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent viewIntent =
                            new Intent("android.intent.action.VIEW",
                                    Uri.parse("https://play.google.com/store/apps/details?id=com.kamaii.partner"));
                    startActivity(viewIntent);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Unable to Connect Try Again...",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

        view.findViewById(R.id.know_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseActivity.navItemIndex = 103;
                baseActivity.navItemIndex = 103;
                baseActivity.loadHomeFragment(new SafetyFragment(), "3");
            }
        });

        view.findViewById(R.id.invite_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseActivity.navItemIndex = 100;
                baseActivity.loadHomeFragment(new AddReferFragment(), "1");
            }
        });
        view.findViewById(R.id.help_linear_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseActivity.navItemIndex = 101;
                baseActivity.loadHomeFragment(new ContactUs(), "2");
            }
        });
        dailograting = new Dialog(getActivity(), R.style.Theme_Dialog);
        dailograting.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dailograting.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dailograting.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        dailograting.setContentView(R.layout.dailog_update);
        dailograting.setCancelable(false);
        tvupdateapp = dailograting.findViewById(R.id.tvupdateapp);
        img_sub_cat = dailograting.findViewById(R.id.img_sub_cat);
        return view;

    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onPause() {
        Log.e("DEBUG", "OnPause of HomeFragment");
        super.onPause();

    }

    public void getVersion() {

        parmsversion.put(Consts.USER_ID, userDTO.getUser_id());

        new HttpsRequest(Consts.GET_VERSION, parmsversion, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {

                    Log.e("VERSION_12345", "" + response.toString());
                    try {
                        String status = response.getString("status");

                        if (status.equalsIgnoreCase("1")) {

                            int version = -1;
                            carticon_flag = response.getString("cart_tracker");
                            carticon_count = response.getString("cart_count");

                            if (carticon_flag.equalsIgnoreCase("1")) {

                                baseActivity.ivmaincartLayout.setVisibility(View.VISIBLE);
                                baseActivity.cart_count.setText(carticon_count);
                            } else {

                                baseActivity.ivmaincartLayout.setVisibility(View.GONE);
                            }
                            JSONObject jsonObjectmain = new JSONObject(String.valueOf(response));
                            JSONArray jsonArray = jsonObjectmain.getJSONArray("data");

                            String pname = getActivity().getPackageName();
                            String update_image = "";
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);

                                String sversion = obj.getString("version_code");
                                String serverpname = obj.getString("package_name");
                                if (serverpname.equalsIgnoreCase(pname)) {
                                    try {
                                        PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
                                        version = pInfo.versionCode;
                                        update_image = obj.getString("update_image");
                                    } catch (PackageManager.NameNotFoundException e) {
                                        e.printStackTrace();
                                    }


                                    if (sversion.equalsIgnoreCase(String.valueOf(version))) {

                                    } else {

                                        dailograting.show();
                                        Glide.with(getActivity()).load(update_image).into(img_sub_cat);

                                        img_sub_cat.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/iUJ4Omb8lro"));
                                                try {
                                                    getActivity().startActivity(webIntent);
                                                } catch (ActivityNotFoundException ex) {
                                                }


                                            }
                                        });
                                        tvupdateapp.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                final String appPackageName = getActivity().getApplication().getPackageName();
                                                try {
                                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                                } catch (ActivityNotFoundException anfe) {
                                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                                                }


                                            }
                                        });

                                    }
                                }
                            }
                        } else if (status.equalsIgnoreCase("5")) {


                            carticon_flag = response.getString("cart_tracker");
                            carticon_count = response.getString("cart_count");

                            if (carticon_flag.equalsIgnoreCase("1")) {

                                baseActivity.ivmaincartLayout.setVisibility(View.VISIBLE);
                                baseActivity.cart_count.setText(carticon_count);
                            } else {

                                baseActivity.ivmaincartLayout.setVisibility(View.GONE);
                            }
                            Fragment fragment = new MaintenenceFragment();
                            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                                    android.R.anim.fade_out);
                            fragmentTransaction.replace(R.id.frame, fragment, TAG);
                            fragmentTransaction.commitAllowingStateLoss();

                        }

                        checkaddress();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    ProjectUtils.showToast(getActivity(), msg);
                }
            }
        });
    }

    public HashMap<String, String> getparm() {
        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.USER_ID, userDTO.getUser_id());
        parms.put(Consts.ROLE, "2");
        return parms;
    }


    private void setUiAction(View view) {

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);


        /*new Thread(new Runnable() {
            @Override
            public void run() {
                getProduct();
            }
        }).start();*/

        getslider();

        /*if (NetworkManager.isConnectToInternet(getActivity())) {*/

        tvNo = view.findViewById(R.id.tvNotFound);
        partner_recyclerview = view.findViewById(R.id.partner_recyclerview);
        services_recyclerview = view.findViewById(R.id.services_recyclerview);
        rec_category = view.findViewById(R.id.rvCategory);
        rec_categoryDaily = view.findViewById(R.id.rvCategoryDaily);
        etSearchFilter = view.findViewById(R.id.etSearchFilter);
        rec_categoryDaily.setHasFixedSize(true);
        rec_categoryDaily.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        firstouterAdapter = new FirstOuterAdapter(this, getFragmentManager());
        rec_categoryDaily.setAdapter(firstouterAdapter);
        rec_category.setHasFixedSize(true);
        rec_category.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        outerAdapter = new OuterAdapter(this, getFragmentManager());
        rec_category.setAdapter(outerAdapter);


        baseActivity.ivmainsearchLayout.setVisibility(View.VISIBLE);
        baseActivity.base_recyclerview.setVisibility(View.VISIBLE);

        baseActivity.ivmainsearchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SearchActivity.class));
            }
        });
        //  baseActivity.ivmaincartLayout.setVisibility(View.VISIBLE);
        baseActivity.ivmaincartLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), BookingProduct.class).putExtra("fom_cart", true));
            }
        });
    }

    public void bookingredirection() {
        Intent in = new Intent(getActivity(), BookingProduct.class);
        startActivity(in);
    }

    public void getBooking() {
        prefrence = SharedPrefrence.getInstance(getActivity());
        userDTO = prefrence.getParentUser(Consts.USER_DTO);


        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.getbooking(userDTO.getUser_id());
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                //         progressDialog.dismiss();
                Log.e("RES", response.message());
                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        JSONObject object = new JSONObject(s);

                        tvNo.setVisibility(View.GONE);

                        try {
                            userBookingList = new ArrayList<>();
                            Type getpetDTO = new TypeToken<List<UserBooking>>() {
                            }.getType();

                            userBookingList = (ArrayList<UserBooking>) new Gson().fromJson(object.getJSONArray("data").toString(), getpetDTO);
                            showData();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        /*Toast.makeText(getActivity(), "Try again. Server is not responding.",
                                LENGTH_LONG).show();*/
                        //            progressDialog.dismiss();

                        Fragment fragment = new ServerNotRespondingFragment();
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                                android.R.anim.fade_out);
                        fragmentTransaction.replace(R.id.frame, fragment, TAG);
                        fragmentTransaction.commitAllowingStateLoss();


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


            }
        });

    }

    private void showData() {

        for (int i = 0; i < userBookingList.size(); i++) {

            if (userBookingList.get(i).getBooking_flag().equalsIgnoreCase("4") && userBookingList.get(i).getReview_status().equalsIgnoreCase("0")) {
                Intent intent = new Intent(getActivity(), ScrachActivity.class);
                intent.putExtra("vendor_name", userBookingList.get(i).getArtistName());
                intent.putExtra("artist_id", userBookingList.get(i).getArtist_id());
                intent.putExtra("vendorimage", userBookingList.get(i).getArtistImage());
                intent.putExtra("saddress", userBookingList.get(i).getAddress());
                intent.putExtra("daddress", userBookingList.get(i).getDestinationaddress());
                intent.putExtra("totalamount", userBookingList.get(i).getPrice());
                intent.putExtra("productname", userBookingList.get(i).getProduct().get(i).getProduct_name());
                intent.putExtra("mobileno", userBookingList.get(i).getArtistMobile());
                intent.putExtra("booking", "booking");
                intent.putExtra("bflag", userBookingList.get(i).getBooking_flag());
                intent.putExtra("bid", userBookingList.get(i).getId());
                intent.putExtra("pay_type", userBookingList.get(i).getPay_type());
                intent.putExtra(CURRENT_TAG, TAG_OPEN);
                startActivity(intent);
                getActivity().finish();
                break;
            }
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        baseActivity = (BaseActivity) activity;
    }

    public void getPopulerService() {

        if (AppController.getInstance().isNetworkAvailable()) {

            //progressDialog.show();
            Retrofit retrofit = apiClient.getClient();
            apiRest api = retrofit.create(apiRest.class);
            Call<ResponseBody> callone = api.getPopulerService(userDTO.getUser_id(), firebase.getString(Consts.DEVICE_TOKEN, ""));
            callone.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                    progressDialog.dismiss();
                    try {
                        if (response.isSuccessful()) {
                            ResponseBody responseBody = response.body();

                            String s = responseBody.string();
                            Log.e("getPopulerServices", "" + s);
                            JSONObject object = new JSONObject(s);

                            String status = object.getString("status");
                            String name = object.getString("name");

                            if (status.equalsIgnoreCase("1")) {

                                populer_service_title_txt.setText(name);

                                populerServicelist = new ArrayList<>();

                                Type getpetDTO = new TypeToken<List<PopulerServiceModel>>() {
                                }.getType();

                                populerServicelist = new Gson().fromJson(object.getJSONArray("data").toString(), getpetDTO);

                                Log.e("POPULER_SERVICE_", " list_size " + populerServicelist.toString());


                                if (populerServicelist.size() != 0) {

                                    populer_service_linear.setVisibility(View.VISIBLE);

/*                                    //services_recyclerview.setLayoutManager(new GridLayoutManager(getActivity(),1,GridLayoutManager.HORIZONTAL,false));
                                    services_recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false));
                                    services_recyclerview.addItemDecoration(new ItemDecorationAlbumColumns(
                                            0,
                                            2));*/
                                    services_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                                    populerServiceAdapter = new PopulerServiceAdapter(getActivity(), populerServicelist, getFragmentManager());
                                    services_recyclerview.setAdapter(populerServiceAdapter);
                                } else {

                                    populer_service_linear.setVisibility(View.GONE);
                                }

                            } else {

                                populer_service_linear.setVisibility(View.GONE);
                            }


                        } else {
                            /*Toast.makeText(getActivity(), "Try again. Server is not responding.",
                                    LENGTH_LONG).show();
*/
                            //               progressDialog.dismiss();

                            Fragment fragment = new ServerNotRespondingFragment();
                            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                                    android.R.anim.fade_out);
                            fragmentTransaction.replace(R.id.frame, fragment, TAG);
                            fragmentTransaction.commitAllowingStateLoss();

                        }
                        getfooterslider();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    //        progressDialog.dismiss();
                    Log.d(TAG, t.getMessage());

                }
            });
        } else {
/*            Toast.makeText(getActivity(), "Check Your Internet connection and Try again ",
                    LENGTH_LONG).show();*/

            //         progressDialog.dismiss();

            Fragment fragment = new ServerNotRespondingFragment();
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                    android.R.anim.fade_out);
            fragmentTransaction.replace(R.id.frame, fragment, TAG);
            fragmentTransaction.commitAllowingStateLoss();

        }


    }

    public void getPopulerPartners() {

        if (AppController.getInstance().isNetworkAvailable()) {

            //           progressDialog.show();
            Retrofit retrofit = apiClient.getClient();
            apiRest api = retrofit.create(apiRest.class);
            Call<ResponseBody> callone = api.getPopulerPartners(userDTO.getUser_id(), firebase.getString(Consts.DEVICE_TOKEN, ""));
            callone.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                    progressDialog.dismiss();
                    try {
                        if (response.isSuccessful()) {
                            ResponseBody responseBody = response.body();

                            String s = responseBody.string();
                            Log.e("POPULER_PARTNER_", "" + s);
                            JSONObject object = new JSONObject(s);
                            populerPartnerlist = new ArrayList<>();

                            String status = object.getString("status");
                            String name = object.getString("name");

                            if (status.equalsIgnoreCase("1")) {

                                populer_partner_title_txt.setText(name);
                                Type getpetDTO = new TypeToken<List<PopulerPartnerModel>>() {
                                }.getType();

                                populerPartnerlist = new Gson().fromJson(object.getJSONArray("data").toString(), getpetDTO);

                                Log.e("POPULER_PARTNER_", " list_size " + populerPartnerlist.toString());


                                if (populerPartnerlist.size() > 2) {

                                /*partner_recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false));
                                partner_recyclerview.addItemDecoration(new ItemDecorationAlbumColumns(
                                        0,
                                        2));*/
                                    partner_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                                    populerPartnerAdapter = new PopulerPartnerAdapter(getActivity(), populerPartnerlist);
                                    partner_recyclerview.setAdapter(populerPartnerAdapter);

                                } else {

                                    populer_partner_linear.setVisibility(View.GONE);
                                }

                            } else {

                                populer_partner_linear.setVisibility(View.GONE);
                            }
                        } else {

                            //                  progressDialog.dismiss();

                            Fragment fragment = new ServerNotRespondingFragment();
                            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                                    android.R.anim.fade_out);
                            fragmentTransaction.replace(R.id.frame, fragment, TAG);
                            fragmentTransaction.commitAllowingStateLoss();

                        }

                        getBooking();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    progressDialog.dismiss();
                    Log.d(TAG, t.getMessage());

                }
            });
        } else {
/*            Toast.makeText(getActivity(), "Check Your Internet connection and Try again ",
                    LENGTH_LONG).show();*/
            //        progressDialog.dismiss();


            Fragment fragment = new ServerNotRespondingFragment();
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                    android.R.anim.fade_out);
            fragmentTransaction.replace(R.id.frame, fragment, TAG);
            fragmentTransaction.commitAllowingStateLoss();

        }

    }

    public void getProduct() {
        if (AppController.getInstance().isNetworkAvailable()) {

            //   progressDialog.show();
            Retrofit retrofit = apiClient.getClient();
            apiRest api = retrofit.create(apiRest.class);
            Call<ResponseBody> callone = api.getAllCatalogCategory(userDTO.getUser_id(), firebase.getString(Consts.DEVICE_TOKEN, ""));
            callone.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    //            progressDialog.dismiss();
                    try {
                        if (response.isSuccessful()) {
                            ResponseBody responseBody = response.body();

                            String s = responseBody.string();
                            Log.e("CATALOG_CATEGORY", "" + s);
                            JSONObject object = new JSONObject(s);

                            List<CategoryDTO> inners = new ArrayList<>();
                            List<CategoryDTO> innersFirst = new ArrayList<>();

                            String message = object.getString("message");
                            int sstatus = object.getInt("status");
                            String slider_data = object.getJSONObject("slider_data").getString("img");


                            if (isAdded()) {
                                Glide.with(getContext()).load(slider_data).into(bg_imageview);
                            }
                            if (sstatus == 1) {
                                JSONArray jsonElements = object.getJSONArray("data");

                                for (int i = 0; i < jsonElements.length(); i++) {
                                    JSONObject obj = jsonElements.getJSONObject(i);
                                    if (i == 0) {
                                        String catelog_idFirst = obj.getString("id");
                                        String nameFirst = obj.getString("name");
                                        String cpriceFirst = obj.getString("status");

                                        innersFirst = new Gson().fromJson(obj.getJSONArray("category").toString(), new TypeToken<ArrayList<CategoryDTO>>() {
                                        }.getType());

                                        Log.e("OUTER_ADAPTER_LIST", " innerlist first" + innersFirst.toString());
                                        if (innersFirst.size() == 0) {
                                            Log.e("inner", "innerfirst size is 0");
                                            first_relative_layout.setVisibility(View.GONE);
                                        } else {
                                            Log.e("inner", "innerfirst size more than 0");
                                            first_relative_layout.setVisibility(View.VISIBLE);
                                            firstouterAdapter.addFirstOuter(new FirstModel(catelog_idFirst, nameFirst, innersFirst));
                                        }
                                    } else {
                                        String catelog_id = obj.getString("id");
                                        String name = obj.getString("name");
                                        String cprice = obj.getString("status");

                                        inners = new Gson().fromJson(obj.getJSONArray("category").toString(), new TypeToken<ArrayList<CategoryDTO>>() {
                                        }.getType());

                                        Log.e("OUTER_ADAPTER_LIST", " inners " + inners.toString() + " size " + inners.size());

                                        if (inners.size() == 0) {
                                            Log.e("inner", "innerlist size is 0");

                                            if (second_relative_layout.getVisibility() == View.VISIBLE) {
                                                Log.e("OUTER_ADAPTER_LIST", " tracker 1 size :-- " + inners.size());

                                                second_relative_layout.setVisibility(View.GONE);
                                            }

                                            second_relative_layout.setVisibility(View.GONE);

                                        } else {
                                            Log.e("inner", "innerlist size is more than 0");

                                            if (second_relative_layout.getVisibility() == View.GONE) {
                                                Log.e("OUTER_ADAPTER_LIST", " tracker 2 size :-- " + inners.size());

                                                second_relative_layout.setVisibility(View.VISIBLE);
                                            }
                                            outerAdapter.addOuter(new ParentModel(catelog_id, name, inners));
                                        }
                                    }
                                }

                            } else if (sstatus == 3) {
                                Toast.makeText(getActivity(), message, LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getActivity(), message, LENGTH_LONG).show();
                            }


                        } else {
                            /*Toast.makeText(getActivity(), "Try again. Server is not responding.",
                                    LENGTH_LONG).show();
*/
                            //        progressDialog.dismiss();

                            Fragment fragment = new ServerNotRespondingFragment();
                            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                                    android.R.anim.fade_out);
                            fragmentTransaction.replace(R.id.frame, fragment, TAG);
                            fragmentTransaction.commitAllowingStateLoss();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    //       progressDialog.dismiss();
                    Log.d(TAG, t.getMessage());

                }
            });
        } else {
/*            Toast.makeText(getActivity(), "Check Your Internet connection and Try again ",
                    LENGTH_LONG).show();*/
            //        progressDialog.dismiss();

            Fragment fragment = new ServerNotRespondingFragment();
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                    android.R.anim.fade_out);
            fragmentTransaction.replace(R.id.frame, fragment, TAG);
            fragmentTransaction.commitAllowingStateLoss();

        }
    }


    public void getslider() {

        new HttpsRequest(Consts.GET_SLIDER, parmsCategory, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {


                if (flag) {

                    try {
                        silderarraylist = new ArrayList<>();

                        Type getpetDTO = new TypeToken<List<SiderModel>>() {
                        }.getType();
                        silderarraylist = (ArrayList<SiderModel>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);

                        mPager.setAdapter(new SliderAdapter(getActivity(), silderarraylist, getFragmentManager()));
                        dotsIndicator.attachViewPager(mPager);

                        getPopulerService();


                        // Timer timer = new Timer();
                        // timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    ProjectUtils.showToast(getActivity(), msg);
                }
            }
        });
    }


    public void getfooterslider() {

        new HttpsRequest(Consts.GET_footerSLIDER, parmsCategory, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {


                if (flag) {

                    try {
                        footersilderarraylist = new ArrayList<>();

                        Type getpetDTO = new TypeToken<List<FooterSliderModel>>() {
                        }.getType();
                        footersilderarraylist = (ArrayList<FooterSliderModel>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);

                        Log.e("FOOTER", "" + footersilderarraylist.toString());
                        pagerhometwo.setAdapter(new FooterSliderAdapter(getActivity(), footersilderarraylist));
                        dotsIndicator2.attachViewPager(pagerhometwo);


                        Timer timer = new Timer();
                        timer.scheduleAtFixedRate(new SliderTimer2(), 4000, 6000);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    getPopulerPartners();

                } else {
                    ProjectUtils.showToast(getActivity(), msg);
                }
            }
        });
    }

    private class SliderTimer extends TimerTask {

        @Override
        public void run() {
            try {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mPager.getCurrentItem() < silderarraylist.size() - 1) {
                            mPager.setCurrentItem(mPager.getCurrentItem() + 1);
                        } else {
                            mPager.setCurrentItem(0);
                        }
                    }
                });
            } catch (Exception e) {

            }

        }
    }

    private class SliderTimer2 extends TimerTask {

        @Override
        public void run() {
            try {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (pagerhometwo.getCurrentItem() < footersilderarraylist.size() - 1) {
                            pagerhometwo.setCurrentItem(pagerhometwo.getCurrentItem() + 1);
                        } else {
                            pagerhometwo.setCurrentItem(0);
                        }
                    }
                });
            } catch (Exception e) {

            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!load) {


            if (!NetworkManager.isConnectToInternet(getActivity())) {
                Fragment fragment = new CheckInternetFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, TAG);
                fragmentTransaction.commitAllowingStateLoss();

            } else {
                getVersion();
                new Thread() {
                    public void run() {
                        try {
                            getActivity().runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    getProduct();

                                }
                            });
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }.start();

                setUiAction(view);


            }
        }


    }

    public void checkaddress() {

        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.getMapCategory(userDTO.getUser_id());
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                Log.e("RES", response.message());
                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        JSONObject object = new JSONObject(s);

                        String message = object.getString("message");
                        String status = object.getString("status");
                        BaseActivity.addressModelArrayList = new ArrayList<>();

                        if (status.equalsIgnoreCase("1")) {
                            JSONArray jsonElements = object.getJSONArray("data");
                            for (int i = 0; i < jsonElements.length(); i++) {
                                JSONObject obj = jsonElements.getJSONObject(i);


                                String id = obj.getString("id");
                                String cat_id = obj.getString("cat_id");
                                String statuss = obj.getString("status");

                                BaseActivity.addressModelArrayList.add(new AddressModel(id, cat_id, statuss));

                            }
                        } else {

                        }
                    } else {
/*                        Toast.makeText(getActivity(), "Try again. Server is not responding.",
                                LENGTH_LONG).show();*/

                        //                 progressDialog.dismiss();


                        Fragment fragment = new ServerNotRespondingFragment();
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                                android.R.anim.fade_out);
                        fragmentTransaction.replace(R.id.frame, fragment, TAG);
                        fragmentTransaction.commitAllowingStateLoss();


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


            }
        });
    }
}

package com.kamaii.customer.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cocosw.bottomsheet.BottomSheet;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.customer.interfacess.OnAddressSelected;
import com.kamaii.customer.ui.activity.BookingProduct;
import com.kamaii.customer.ui.activity.CheckSignInActivity;
import com.kamaii.customer.ui.activity.ViewAddressActivity;
import com.kamaii.customer.ui.adapter.AddressBottomSheetAdapter;
import com.kamaii.customer.ui.models.TypeAddressModel;
import com.kamaii.customer.utils.CustomTextView;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
import com.kamaii.customer.DTO.UserDTO;
import com.kamaii.customer.R;
import com.kamaii.customer.https.HttpsRequest;
import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.interfacess.Helper;
import com.kamaii.customer.network.NetworkManager;
import com.kamaii.customer.preferences.SharedPrefrence;
import com.kamaii.customer.ui.activity.BaseActivity;
import com.kamaii.customer.ui.activity.SignInActivity;
import com.kamaii.customer.utils.CustomButton;
import com.kamaii.customer.utils.CustomEditText;
import com.kamaii.customer.utils.CustomTextViewBold;
import com.kamaii.customer.utils.ImageCompression;
import com.kamaii.customer.utils.MainFragment;
import com.kamaii.customer.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class ProfileSettingActivity extends Fragment implements View.OnClickListener, OnAddressSelected {

    private ImageView ivPersonalInfoChange, edit_address, ivAddressChange;
    private CircleImageView ivProfile;
    private CustomButton btnDelete, btnChange;
    private CustomEditText etName, etEmail, etMobile, etGender, etAddress, etAddressSelect, etCity, etCountry;
    public RadioGroup rg_gender_options;
    String address_radio_txt = "Home";
    private static int AUTOCOMPLETE_REQUEST_CODE = 1;
    ProfileAddressAdapter dialogadapter;

    public RadioButton rb_gender_female, rb_gender_male;
    private Dialog dialog_profile, dialog_pass, dialog_address;
    private CustomTextViewBold tvYes, tvNo, tvYesPass, tvNoPass, tvYesAddress, tvNoAddress;
    private CustomEditText etNameD, etEmailD, etMobileD, etOldPassD, etNewPassD, etConfrimPassD, etAddressD, etCityD, etCountryD;
    private HashMap<String, String> params;
    private RelativeLayout RRsncbar;
    HashMap<String, String> getAddressHashmap = new HashMap<>();
    ArrayList<TypeAddressModel> typeAddressList;
    ProfileAddressAdapter addressAdapter;
    BottomSheetDialog addressBottomSheet;
    CustomTextView change_address_btn;
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private String TAG = ProfileSettingActivity.class.getSimpleName();
    private HashMap<String, File> paramsFile = new HashMap<>();
    BottomSheet.Builder builder;
    Uri picUri;
    int PICK_FROM_CAMERA = 1, PICK_FROM_GALLERY = 2;
    int CROP_CAMERA_IMAGE = 3, CROP_GALLERY_IMAGE = 4;
    String imageName;
    String pathOfImage;
    String final_user_address = "";
    String landmark_name = "";
    private HashMap<String, String> paramsBookingOp = new HashMap<>();
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS = 0x2;

    Bitmap bm;
    ImageCompression imageCompression;
    File file;
    Bitmap bitmap = null;
    private View view;
    private BaseActivity baseActivity;
    private HashMap<String, String> paramsDeleteImg = new HashMap<>();
    private double lats = 0.0;
    private double longs = 0.0;
    private LinearLayout llChangePass, llLogout;
    int selectBox = 0;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1000;
    RecyclerView customer_address_list;
    CustomTextViewBold address_submit, cooking_instruction_txt;
    CustomEditText location_etx, house_no_etx, society_name_etx, remark;
    String house_no_str = "", society_name_str = "", street_address_str = "";
    LinearLayout customAddress, laychat123;
    LinearLayout address_lay_dest_location;
    RelativeLayout address_relative;
    int address_adapter_counter = 0;
    HashMap<String, String> addAddressHashmap = new HashMap<>();
    private String address_id = "";
    private static final String API_KEY = "AIzaSyCIpO2XuPQr8HDOg92EU0pRkP0G9rKvFoY";
    private GoogleApiClient googleApiClient;
    List<Place.Field> fields = Arrays.asList(Place.Field.LAT_LNG, Place.Field.ADDRESS, Place.Field.NAME);
    private double latitude = 0.0, longitude = 0.0;
    private double live_latitude = 0.0, live_longitude = 0.0;
    boolean map_flag = false;
    private Location mylocation;
    boolean live_address_str = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_profile_setting, container, false);
        prefrence = SharedPrefrence.getInstance(getActivity());
        getActivity().findViewById(R.id.ivLogo).setVisibility(View.GONE);
        getActivity().findViewById(R.id.customer_location_relative_header).setVisibility(View.GONE);
        prefrence = SharedPrefrence.getInstance(getActivity());

        userDTO = prefrence.getParentUser(Consts.USER_DTO);

        googleApiClient = baseActivity.googleApiClient;
        baseActivity.headerNameTV.setText(getResources().getString(R.string.profile_settings));
        getActivity().findViewById(R.id.headerNameTV).setVisibility(View.VISIBLE);
        baseActivity.base_recyclerview.setVisibility(View.GONE);

        customer_address_list = view.findViewById(R.id.customer_address_list);
        customer_address_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        Typeface font2 = Typeface.createFromAsset(getActivity().getAssets(), "Poppins-Regular.otf");
        addressBottomSheet = new BottomSheetDialog(getActivity());
        //addressBottomSheet.setCancelable(false);
        //   addressBottomSheet.setCancelable(false);
        addressBottomSheet.setContentView(R.layout.activity_address_bottom_dialog);
        change_address_btn = addressBottomSheet.findViewById(R.id.change_address_btn);
        customAddress = addressBottomSheet.findViewById(R.id.customAddress);
        address_lay_dest_location = addressBottomSheet.findViewById(R.id.address_lay_dest_location);
        location_etx = addressBottomSheet.findViewById(R.id.location_etx);
        house_no_etx = addressBottomSheet.findViewById(R.id.house_no_etx);
        society_name_etx = addressBottomSheet.findViewById(R.id.society_name_etx);
        address_submit = addressBottomSheet.findViewById(R.id.address_submit);
        getAddress();
        setUiAction(view);
        return view;
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    public void setUiAction(View v) {
        llLogout = v.findViewById(R.id.llLogout);
        RRsncbar = v.findViewById(R.id.RRsncbar);
        ivPersonalInfoChange = v.findViewById(R.id.ivPersonalInfoChange);
        llChangePass = v.findViewById(R.id.llChangePass);
        edit_address = v.findViewById(R.id.edit_address);
        ivAddressChange = v.findViewById(R.id.ivAddressChange);
        ivProfile = v.findViewById(R.id.ivProfile);
        btnDelete = v.findViewById(R.id.btnDelete);
        btnChange = v.findViewById(R.id.btnChange);
        etName = v.findViewById(R.id.etName);
        etEmail = v.findViewById(R.id.etEmail);
        etMobile = v.findViewById(R.id.etMobile);
        etGender = v.findViewById(R.id.etGender);
        etAddress = v.findViewById(R.id.etAddress);
        etCity = v.findViewById(R.id.etCity);
        etCountry = v.findViewById(R.id.etCountry);
        etAddressSelect = v.findViewById(R.id.etAddressSelect);


        ivPersonalInfoChange.setOnClickListener(this);
        llChangePass.setOnClickListener(this);
        ivAddressChange.setOnClickListener(this);
        edit_address.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnChange.setOnClickListener(this);
        llLogout.setOnClickListener(this);

        showData();

        builder = new BottomSheet.Builder(getActivity()).sheet(R.menu.menu_cards);
        builder.title(getResources().getString(R.string.take_image));
        builder.listener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case R.id.camera_cards:
                        if (ProjectUtils.hasPermissionInManifest(getActivity(), PICK_FROM_CAMERA, Manifest.permission.CAMERA)) {
                            if (ProjectUtils.hasPermissionInManifest(getActivity(), PICK_FROM_GALLERY, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                try {
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    File file = getOutputMediaFile(1);
                                    if (!file.exists()) {
                                        try {
                                            ProjectUtils.pauseProgressDialog();
                                            file.createNewFile();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        picUri = FileProvider.getUriForFile(getActivity().getApplicationContext(), getActivity().getApplicationContext().getPackageName() + ".fileprovider", file);
                                    } else {
                                        picUri = Uri.fromFile(file);
                                    }

                                    prefrence.setValue(Consts.IMAGE_URI_CAMERA, picUri.toString());
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
                                    startActivityForResult(intent, PICK_FROM_CAMERA);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        break;
                    case R.id.gallery_cards:
                        if (ProjectUtils.hasPermissionInManifest(getActivity(), PICK_FROM_CAMERA, Manifest.permission.CAMERA)) {
                            if (ProjectUtils.hasPermissionInManifest(getActivity(), PICK_FROM_GALLERY, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                                File file = getOutputMediaFile(1);
                                if (!file.exists()) {
                                    try {
                                        file.createNewFile();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                picUri = Uri.fromFile(file);

                                Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.select_picture)), PICK_FROM_GALLERY);

                            }
                        }
                        break;
                    case R.id.cancel_cards:
                        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                dialog.dismiss();
                            }
                        });
                        break;
                }
            }
        });
    }

    public void getAddress() {

        getAddressHashmap.put("cust_id", userDTO.getUser_id());
        //parmsshipping.put(Consts.ARTIST_ID, artist_id);

        new HttpsRequest(Consts.GET_ADDRESS, getAddressHashmap, getActivity()).stringPost("Tag", new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                if (flag) {

                    Log.e("GET_ADDRESS_API", "" + response.toString());
                    typeAddressList = new ArrayList<>();
                    Type getaddDTO = new TypeToken<List<TypeAddressModel>>() {
                    }.getType();
                    try {
                        typeAddressList = (ArrayList<TypeAddressModel>) new Gson().fromJson(response.getJSONArray("data").toString(), getaddDTO);

                        if (typeAddressList.size() != 0) {

                            address_id = typeAddressList.get(typeAddressList.size() - 1).getAddress_id();
                        } else {
                            address_lay_dest_location.setVisibility(View.GONE);
                            customAddress.setVisibility(View.VISIBLE);
                        }
                        addressAdapter = new ProfileAddressAdapter(getActivity(), typeAddressList);
                        customer_address_list.setAdapter(addressAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.e("ADBVC", "12231");
                }
            }
        });
    }

    private File getOutputMediaFile(int type) {
        String root = Environment.getExternalStorageDirectory().toString();
        File mediaStorageDir = new File(root, Consts.APP_NAME);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == 1) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    Consts.APP_NAME + timeStamp + ".png");

            imageName = Consts.APP_NAME + timeStamp + ".png";
        } else {
            return null;
        }
        return mediaFile;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       /* if (requestCode == CROP_CAMERA_IMAGE) {
            if (data != null) {
                picUri = Uri.parse(data.getExtras().getString("resultUri"));
                try {
                    pathOfImage = picUri.getPath();
                    imageCompression = new ImageCompression(getActivity());
                    imageCompression.execute(pathOfImage);
                    imageCompression.setOnTaskFinishedEvent(new ImageCompression.AsyncResponse() {
                        @Override
                        public void processFinish(String imagePath) {
                            Glide.with(getActivity()).load("file://" + imagePath)
                                    .thumbnail(0.5f)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(ivProfile);
                            try {
                                file = new File(imagePath);
                                paramsFile.put(Consts.IMAGE, file);
                                Log.e("image", imagePath);
                                params = new HashMap<>();
                                params.put(Consts.USER_ID, userDTO.getUser_id());
                                if (NetworkManager.isConnectToInternet(getActivity())) {
                                    updateProfile();
                                } else {
                                    ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (requestCode == CROP_GALLERY_IMAGE) {
            if (data != null) {
                picUri = Uri.parse(data.getExtras().getString("resultUri"));
                try {
                    bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), picUri);
                    pathOfImage = picUri.getPath();
                    imageCompression = new ImageCompression(getActivity());
                    imageCompression.execute(pathOfImage);
                    imageCompression.setOnTaskFinishedEvent(new ImageCompression.AsyncResponse() {
                        @Override
                        public void processFinish(String imagePath) {
                            Glide.with(getActivity()).load("file://" + imagePath)
                                    .thumbnail(0.5f)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(ivProfile);
                            Log.e("image", imagePath);
                            try {
                                file = new File(imagePath);
                                paramsFile.put(Consts.IMAGE, file);

                                params = new HashMap<>();
                                params.put(Consts.USER_ID, userDTO.getUser_id());
                                if (NetworkManager.isConnectToInternet(getActivity())) {
                                    updateProfile();
                                } else {
                                    ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
                                }
                                Log.e("image", imagePath);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (requestCode == PICK_FROM_CAMERA && resultCode == RESULT_OK) {
            if (picUri != null) {
                picUri = Uri.parse(prefrence.getValue(Consts.IMAGE_URI_CAMERA));
                startCropping(picUri, CROP_CAMERA_IMAGE);
            } else {
                picUri = Uri.parse(prefrence
                        .getValue(Consts.IMAGE_URI_CAMERA));
                startCropping(picUri, CROP_CAMERA_IMAGE);
            }
        }
        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK) {
            try {
                Uri tempUri = data.getData();
                Log.e("front tempUri", "" + tempUri);
                if (tempUri != null) {
                    startCropping(tempUri, CROP_GALLERY_IMAGE);
                } else {

                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {

            CarmenFeature selectedCarmenFeature = PlaceAutocomplete.getPlace(data);

            lats = ((Point) selectedCarmenFeature.geometry()).latitude();
            longs = ((Point) selectedCarmenFeature.geometry()).longitude();

            prefrence.setValue(Consts.HOME_ADDRESS_LATI, String.valueOf(lats));
            prefrence.setValue(Consts.HOME_ADDRESS_LONGI, String.valueOf(longs));
            etAddressD.setText(selectedCarmenFeature.placeName());

        }
*/
        if (resultCode == Activity.RESULT_OK && requestCode == AUTOCOMPLETE_REQUEST_CODE) {

            Place place = Autocomplete.getPlaceFromIntent(data);

            final_user_address = place.getAddress();
            landmark_name = place.getName();

            latitude = place.getLatLng().latitude;
            longitude = place.getLatLng().longitude;

            paramsBookingOp.put(Consts.LATITUDE, String.valueOf(latitude));
            paramsBookingOp.put(Consts.LONGITUDE, String.valueOf(longitude));

            addAddressHashmap.put("street_address", landmark_name + final_user_address);
            addAddressHashmap.put(Consts.LATITUDE, String.valueOf(latitude));
            addAddressHashmap.put(Consts.LONGITUDE, String.valueOf(longitude));

            location_etx.setText(landmark_name + ", " + place.getAddress());
            customAddress.setVisibility(View.VISIBLE);
            address_lay_dest_location.setVisibility(View.GONE);
            address_relative.setVisibility(View.GONE);

        }
        if (resultCode == RESULT_OK && requestCode == 23) {
            if (requestCode == 23) {
                Uri picUri = data.getData();

                pathOfImage = picUri.getPath();
                imageCompression = new ImageCompression(getActivity());
                imageCompression.execute(pathOfImage);
                imageCompression.setOnTaskFinishedEvent(new ImageCompression.AsyncResponse() {
                    @Override
                    public void processFinish(String imagePath) {
                        Glide.with(getActivity()).load("file://" + imagePath)
                                .thumbnail(0.5f)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(ivProfile);
                        try {
                            file = new File(imagePath);
                            paramsFile.put(Consts.IMAGE, file);
                            Log.e("image", imagePath);
                            params = new HashMap<>();
                            params.put(Consts.USER_ID, userDTO.getUser_id());
                            if (NetworkManager.isConnectToInternet(getActivity())) {
                                updateProfile();
                            } else {
                                ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } else {
            Log.d("TAG", "hello");
        }

    }


    public void startCropping(Uri uri, int requestCode) {
        Intent intent = new Intent(getActivity(), MainFragment.class);
        intent.putExtra("imageUri", uri.toString());
        intent.putExtra("requestCode", requestCode);
        startActivityForResult(intent, requestCode);
    }

    public void showData() {
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        Glide.with(getActivity()).
                load(userDTO.getImage())
                .placeholder(R.drawable.dummyuser_image)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivProfile);

        etName.setText(userDTO.getName());
        etEmail.setText(userDTO.getEmail_id());
        etMobile.setText(userDTO.getMobile());
        if (userDTO.getGender().equalsIgnoreCase("0")) {
            etGender.setText(getResources().getString(R.string.female));
        } else if (userDTO.getGender().equalsIgnoreCase("1")) {
            etGender.setText(getResources().getString(R.string.male));
        } else {
            etGender.setText("");
        }

        etAddress.setText(userDTO.getAddress());
        etCity.setText(userDTO.getCity());
        etCountry.setText(userDTO.getCountry());
        etAddressSelect.setText(userDTO.getOffice_address());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDelete:
                if (NetworkManager.isConnectToInternet(getActivity())) {
                    if (!userDTO.getImage().equalsIgnoreCase("")) {
                        deleteImage();
                    } else {
                        ProjectUtils.showToast(getActivity(), getResources().getString(R.string.upload_image_first));
                    }
                } else {
                    ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
                }
                break;
            case R.id.btnChange:
                ImagePicker.Companion.with(getActivity())
                        .crop()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start(23);
                break;
            case R.id.ivPersonalInfoChange:
                dialogPersonalProfile();
                break;
            case R.id.llChangePass:
                if (NetworkManager.isConnectToInternet(getActivity())) {
                    dialogPassword();
                } else {
                    ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
                }
                break;
            case R.id.ivAddressChange:
                if (NetworkManager.isConnectToInternet(getActivity())) {
                    dialogAddress();
                } else {
                    ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
                }
                break;
            case R.id.edit_address:
                if (NetworkManager.isConnectToInternet(getActivity())) {
                    showAddressDialog();
                } else {
                    ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
                }
                break;
            case R.id.llLogout:
                break;

        }
    }

    private void showAddressDialog() {

        RadioGroup add_addressradioGroup;
        add_addressradioGroup = addressBottomSheet.findViewById(R.id.add_addressradioGroup);

        house_no_etx.setText("");
        society_name_etx.setText("");
        ImageView back = addressBottomSheet.findViewById(R.id.address_dialog_close_img);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressBottomSheet.dismiss();
            }
        });

        Typeface font2 = Typeface.createFromAsset(getActivity().getAssets(), "Poppins-Regular.otf");


        location_etx.setTypeface(font2);
        house_no_etx.setTypeface(font2);
        society_name_etx.setTypeface(font2);

        addAddressHashmap.put(Consts.USER_ID, userDTO.getUser_id());
        address_relative = addressBottomSheet.findViewById(R.id.address_relative);
        address_relative.setVisibility(View.GONE);
        location_etx.setText(baseActivity.customer_live_address);

        addAddressHashmap.put("street_address", baseActivity.customer_live_address);
        addAddressHashmap.put(Consts.LATITUDE, String.valueOf(baseActivity.customer_live_lat));
        addAddressHashmap.put(Consts.LONGITUDE, String.valueOf(baseActivity.customer_live_long));

        customAddress.setVisibility(View.VISIBLE);
        RecyclerView recyclerView = addressBottomSheet.findViewById(R.id.address_recycler);
        recyclerView.setVisibility(View.GONE);
        if (typeAddressList.size() == 0) {
            address_relative.setVisibility(View.GONE);
        }

        dialogadapter = new ProfileAddressAdapter(getActivity(), typeAddressList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(dialogadapter);


        change_address_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPlace();
            }
        });

        add_addressradioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {

                    case R.id.add_home_radio:
                        address_radio_txt = "Home";
                        addAddressHashmap.put("addresstype", address_radio_txt);
                        break;

                    case R.id.add_work_radio:
                        address_radio_txt = "Work";
                        addAddressHashmap.put("addresstype", address_radio_txt);
                        break;

                    case R.id.add_other_radio:
                        address_radio_txt = "Others";
                        addAddressHashmap.put("addresstype", address_radio_txt);
                        break;
                }
            }
        });


        address_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                house_no_str = house_no_etx.getText().toString().trim();
                Log.e("HOUSENO12345",""+house_no_str);

                society_name_str = society_name_etx.getText().toString().trim();
                Log.e("HOUSENO12345",""+society_name_str);

                street_address_str = location_etx.getText().toString();

                if (house_no_str.isEmpty() || house_no_str.equals("&nbsp;")) {
                    Toast.makeText(getActivity(), "Please enter House no. / Flat no. / Floor / Building", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (society_name_str.equalsIgnoreCase("&nbsp;")) {
                    Toast.makeText(getActivity(), "Please enter valid Landmark", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (street_address_str.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter valid Location", Toast.LENGTH_SHORT).show();
                    return;
                } else {


                    addAddressHashmap.put("house_no", house_no_str);
                    addAddressHashmap.put("society_name", society_name_str);
                    addAddress();
                    //binding.tvAddress.setTextColor(getResources().getColor(R.color.dark_blue_color));

                    if (house_no_str.isEmpty() && society_name_str.isEmpty()) {
                        Log.e("ADDRESS_ADAPTER", " 101");

                        //binding.tvAddress.setText(street_address_str);
                    } else if (house_no_str.isEmpty()) {
                        Log.e("ADDRESS_ADAPTER", " 102");

                        //binding.tvAddress.setText(society_name_str + ", " + street_address_str);
                    } else if (society_name_str.isEmpty()) {
                        Log.e("ADDRESS_ADAPTER", " 103");

                        //binding.tvAddress.setText(house_no_str + ", " + street_address_str);
                    } else {
                        Log.e("ADDRESS_ADAPTER", " 104");

                        //  binding.tvAddress.setText(house_no_str + ", " + society_name_str + ", " + street_address_str);
                    }

                    customAddress.setVisibility(View.GONE);
                    address_relative.setVisibility(View.VISIBLE);
                    address_lay_dest_location.setVisibility(View.VISIBLE);


                    addressBottomSheet.dismiss();
                }
            }
        });
        address_lay_dest_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 109);
                    } else {
                        findPlace();
                    }
                }

            }
        });

        addressBottomSheet.show();
    }

    public void addAddress() {
        addAddressHashmap.put("addresstype", address_radio_txt);
        Log.e("addAddressHashmap", "" + addAddressHashmap.toString());
        new HttpsRequest(Consts.ADD_ADDRESS, addAddressHashmap, getActivity()).stringPost("Tag", new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                Log.e("addAddressHashmap", "bahar" + response.toString());

                if (flag) {
                    Log.e("addAddressHashmap", "17");

                    getAddress();
                    Log.e("addAddressHashmap", "" + response.toString());
                } else {
                    Log.e("addAddressHashmap", "1234567");
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void findPlace() {


        Intent intentt = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .setCountry("IN")
                .build(getActivity());

        startActivityForResult(intentt, AUTOCOMPLETE_REQUEST_CODE);

    }

    public void dialogPersonalProfile() {
        dialog_profile = new Dialog(getActivity());
        dialog_profile.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_profile.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_profile.setContentView(R.layout.dailog_personal_info);

        rg_gender_options = dialog_profile.findViewById(R.id.rg_gender_options);
        rb_gender_male = dialog_profile.findViewById(R.id.rb_gender_male);
        rb_gender_female = dialog_profile.findViewById(R.id.rb_gender_female);

        etNameD = (CustomEditText) dialog_profile.findViewById(R.id.etNameD);
        etEmailD = (CustomEditText) dialog_profile.findViewById(R.id.etEmailD);
        etMobileD = (CustomEditText) dialog_profile.findViewById(R.id.etMobileD);

        etMobileD.setClickable(false);
        etMobileD.setEnabled(false);
        etNameD.setText(userDTO.getName());
        etEmailD.setText(userDTO.getEmail_id());
        etMobileD.setText(userDTO.getMobile());

        if (userDTO.getGender().equalsIgnoreCase("0")) {
            rb_gender_female.setChecked(true);
            rb_gender_male.setChecked(false);
        } else if (userDTO.getGender().equalsIgnoreCase("1")) {
            rb_gender_female.setChecked(false);
            rb_gender_male.setChecked(true);
        } else {
            rb_gender_female.setChecked(false);
            rb_gender_male.setChecked(false);
        }

        tvYes = (CustomTextViewBold) dialog_profile.findViewById(R.id.tvYes);
        tvNo = (CustomTextViewBold) dialog_profile.findViewById(R.id.tvNo);
        dialog_profile.show();
        dialog_profile.setCancelable(false);

        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_profile.dismiss();

            }
        });
        tvYes.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        params = new HashMap<>();
                        params.put(Consts.USER_ID, userDTO.getUser_id());
                        params.put(Consts.NAME, ProjectUtils.getEditTextValue(etNameD));
                        params.put(Consts.EMAIL_ID, ProjectUtils.getEditTextValue(etEmailD));
                        params.put(Consts.MOBILE, ProjectUtils.getEditTextValue(etMobileD));
                        if (rb_gender_female.isChecked()) {
                            params.put(Consts.GENDER, "0");
                        } else {
                            params.put(Consts.GENDER, "1");
                        }

                        if (NetworkManager.isConnectToInternet(getActivity())) {
                            updateProfile();
                            dialog_profile.dismiss();
                        } else {
                            ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
                        }
                    }
                });

    }

    public void dialogPassword() {
        dialog_pass = new Dialog(getActivity());
        dialog_pass.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_pass.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_pass.setContentView(R.layout.dailog_password);


        etOldPassD = (CustomEditText) dialog_pass.findViewById(R.id.etOldPassD);
        etNewPassD = (CustomEditText) dialog_pass.findViewById(R.id.etNewPassD);
        etConfrimPassD = (CustomEditText) dialog_pass.findViewById(R.id.etConfrimPassD);


        etOldPassD.setTransformationMethod(new PasswordTransformationMethod());
        etNewPassD.setTransformationMethod(new PasswordTransformationMethod());
        etConfrimPassD.setTransformationMethod(new PasswordTransformationMethod());


        tvYesPass = (CustomTextViewBold) dialog_pass.findViewById(R.id.tvYesPass);
        tvNoPass = (CustomTextViewBold) dialog_pass.findViewById(R.id.tvNoPass);
        dialog_pass.show();
        dialog_pass.setCancelable(false);

        tvNoPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_pass.dismiss();

            }
        });
        tvYesPass.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        params = new HashMap<>();
                        params.put(Consts.USER_ID, userDTO.getUser_id());
                        params.put(Consts.PASSWORD, ProjectUtils.getEditTextValue(etOldPassD));
                        params.put(Consts.NEW_PASSWORD, ProjectUtils.getEditTextValue(etNewPassD));

                        if (NetworkManager.isConnectToInternet(getActivity())) {
                            Submit();

                        } else {
                            ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
                        }
                    }
                });

    }


    public void dialogAddress() {
        dialog_address = new Dialog(getActivity());
        dialog_address.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_address.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog_address.setContentView(R.layout.dailog_address);


        etAddressD = (CustomEditText) dialog_address.findViewById(R.id.etAddressD);
        etCityD = (CustomEditText) dialog_address.findViewById(R.id.etCityD);
        etCountryD = (CustomEditText) dialog_address.findViewById(R.id.etCountryD);


        etAddressD.setText(userDTO.getAddress());
        etCityD.setText(userDTO.getCity());
        etCountryD.setText(userDTO.getCountry());


        tvYesAddress = (CustomTextViewBold) dialog_address.findViewById(R.id.tvYesAddress);
        tvNoAddress = (CustomTextViewBold) dialog_address.findViewById(R.id.tvNoAddress);
        dialog_address.show();
        dialog_address.setCancelable(false);


        etAddressD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectBox = 0;
                findPlacehome();
            }
        });

        tvNoAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_address.dismiss();

            }
        });
        tvYesAddress.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        params = new HashMap<>();
                        params.put(Consts.USER_ID, userDTO.getUser_id());
                        params.put(Consts.ADDRESS, ProjectUtils.getEditTextValue(etAddressD));
                        params.put(Consts.OFFICE_ADDRESS, ProjectUtils.getEditTextValue(etAddressD));
                        params.put(Consts.CITY, ProjectUtils.getEditTextValue(etCityD));
                        params.put(Consts.COUNTRY, ProjectUtils.getEditTextValue(etCountryD));
                        params.put(Consts.LATITUDE, String.valueOf(lats));
                        params.put(Consts.LONGITUDE, String.valueOf(longs));
                        if (NetworkManager.isConnectToInternet(getActivity())) {


                            if (etAddressD.getText().toString().equalsIgnoreCase("")) {
                                Toast.makeText(getActivity(), getResources().getString(R.string.val_address), Toast.LENGTH_SHORT).show();
                            } else if (etCityD.getText().toString().equalsIgnoreCase("")) {
                                Toast.makeText(getActivity(), "Please enter city", Toast.LENGTH_SHORT).show();
                            } else if (etCountryD.getText().toString().equalsIgnoreCase("")) {
                                Toast.makeText(getActivity(), "Please enter country", Toast.LENGTH_SHORT).show();
                            } else {
                                updateProfile();
                                dialog_address.dismiss();
                            }

                        } else {
                            ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
                        }
                    }
                });

    }

    public void updateProfile() {
        ProjectUtils.showProgressDialog(getActivity(), true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.UPDATE_PROFILE_API, params, paramsFile, getActivity()).imagePost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {
                        ProjectUtils.showToast(getActivity(), msg);

                        userDTO = new Gson().fromJson(response.getJSONObject("data").toString(), UserDTO.class);
                        prefrence.setParentUser(userDTO, Consts.USER_DTO);
                        baseActivity.showImage();
                        showData();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    ProjectUtils.showToast(getActivity(), msg);
                }


            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        baseActivity = (BaseActivity) activity;
    }

    public void deleteImage() {
        ProjectUtils.showProgressDialog(getActivity(), true, getResources().getString(R.string.please_wait));
        paramsDeleteImg.put(Consts.USER_ID, userDTO.getUser_id());
        new HttpsRequest(Consts.DELETE_PROFILE_IMAGE_API, paramsDeleteImg, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    userDTO.setImage("");
                    prefrence.setParentUser(userDTO, Consts.USER_DTO);
                    showData();
                } else {
                    ProjectUtils.showToast(getActivity(), msg);
                }


            }
        });
    }

    private void findPlacehome() {
        Intent intent = new PlaceAutocomplete.IntentBuilder()
                .accessToken(getString(R.string.mapbox_access_token))
                .placeOptions(PlaceOptions.builder()
                        .backgroundColor(Color.parseColor("#EEEEEE"))
                        .limit(10)
                        .country("IN")
                        .build(PlaceOptions.MODE_CARDS))
                .build(getActivity());
        startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
    }


    private void Submit() {
        if (!passwordValidation()) {
            return;
        } else if (!checkpass()) {
            return;
        } else {
            if (NetworkManager.isConnectToInternet(getActivity())) {
                updateProfile();
                dialog_pass.dismiss();
            } else {
                ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
            }

        }
    }

    public boolean passwordValidation() {
        if (!ProjectUtils.isPasswordValid(etOldPassD.getText().toString().trim())) {
            etOldPassD.setError(getResources().getString(R.string.val_pass_c));
            etOldPassD.requestFocus();
            return false;
        } else if (!ProjectUtils.isPasswordValid(etNewPassD.getText().toString().trim())) {
            etNewPassD.setError(getResources().getString(R.string.val_pass_c));
            etNewPassD.requestFocus();
            return false;
        } else
            return true;

    }

    private boolean checkpass() {
        if (etNewPassD.getText().toString().trim().equals("")) {
            etNewPassD.setError(getResources().getString(R.string.val_new_pas));
            return false;
        } else if (etConfrimPassD.getText().toString().trim().equals("")) {
            etConfrimPassD.setError(getResources().getString(R.string.val_c_pas));
            return false;
        } else if (!etNewPassD.getText().toString().trim().equals(etConfrimPassD.getText().toString().trim())) {
            etConfrimPassD.setError(getResources().getString(R.string.val_n_c_pas));
            return false;
        }
        return true;
    }

    @Override
    public void setAddress(int position, String id, String house_no, String street_add, String soc_name, double lati, double longi) {

    }


    public class ProfileAddressAdapter extends RecyclerView.Adapter<ProfileAddressAdapter.AddressProfileViewholder> {

        Context context;
        List<TypeAddressModel> homeList;
        BookingProduct bookingProduct;
        HashMap<String, String> deleteAddressHashmap = new HashMap<>();

        public ProfileAddressAdapter(Context context, List<TypeAddressModel> homeList) {
            this.context = context;
            this.homeList = homeList;
        }

        @NonNull
        @Override
        public ProfileAddressAdapter.AddressProfileViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.address_profile_recycle_layout, parent, false);
            return new AddressProfileViewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ProfileAddressAdapter.AddressProfileViewholder holder, int position) {
            String comma = "";
            holder.add_type.setText(homeList.get(position).getAdd_type());
            holder.done_img_address.setVisibility(View.GONE);
            holder.add_recycle_txt.setText("Address " + (position + 1));
        /*if (homeList.get(position).getSociety_name().equals("") || homeList.get(position).getSociety_name().isEmpty()) {
            comma = " ";
        } else {
            comma = homeList.get(position).getSociety_name() + ", ";
        }*/

            if (homeList.get(position).getHouse_no().isEmpty() && homeList.get(position).getSociety_name().isEmpty()) {
                Log.e("ADDRESS_ADAPTER", "1");
                holder.address_txt.setText(homeList.get(position).getStreet_address());
            } else if (homeList.get(position).getHouse_no().isEmpty()) {
                Log.e("ADDRESS_ADAPTER", "2");
                holder.address_txt.setText(homeList.get(position).getSociety_name() + ", " + comma + homeList.get(position).getStreet_address());
                //binding.tvAddress.setText(soc_name + ", " + street_add);
            } else if (homeList.get(position).getSociety_name().isEmpty()) {
                Log.e("ADDRESS_ADAPTER", "3");
                holder.address_txt.setText(homeList.get(position).getHouse_no() + ", " + comma + homeList.get(position).getStreet_address());

                //binding.tvAddress.setText(house_no + ", "+ street_add);
            } else {
                //Log.e("ADDRESS_ADAPTER"," 4 "+"house no:-"+house_no+"society:-"+soc_name);

                //binding.tvAddress.setText(house_no + ", " + soc_name + ", " + street_add);
                holder.address_txt.setText(homeList.get(position).getHouse_no() + ", " + homeList.get(position).getSociety_name() + ", " + homeList.get(position).getStreet_address());
            }

            /*if (position == homeList.size() - 1) {
                holder.line.setVisibility(View.GONE);
            }*/
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (homeList.get(position).getSociety_name().isEmpty()) {
                        Log.e("ADDRESS_ADAPTER", "empty 1");

                        Log.e("ADDRESS_ADAPTER", "houseno_" + homeList.get(position).getHouse_no() + "societyname_" + homeList.get(position).getSociety_name());
                    } else if (homeList.get(position).getHouse_no().isEmpty()) {
                        Log.e("ADDRESS_ADAPTER", "empty 2");

                        //"societyname_"+homeList.get(position).getSociety_name();
                    }
//                Log.e("ADDRESS_ADAPTER",);

                    //  ((BookingProduct) context).changingtext();
                }
            });
            holder.delete_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteaddress(homeList.get(position).getAddress_id());
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

        class AddressProfileViewholder extends RecyclerView.ViewHolder {

            CustomTextViewBold add_type, add_recycle_txt;
            CustomTextView address_txt;
            TextView line;
            ImageView delete_address, done_img_address;

            public AddressProfileViewholder(@NonNull View itemView) {
                super(itemView);

                done_img_address = itemView.findViewById(R.id.done_img_address);
                add_recycle_txt = itemView.findViewById(R.id.add_recycle_txt);
                delete_address = itemView.findViewById(R.id.delete_address);
                add_type = itemView.findViewById(R.id.add_type);
                address_txt = itemView.findViewById(R.id.address_txt);
                line = itemView.findViewById(R.id.add_recycle_view);
            }
        }

        public void deleteaddress(String address_id) {
            deleteAddressHashmap.put(Consts.USER_ID, userDTO.getUser_id());
            deleteAddressHashmap.put(Consts.ADDRESS_ID, address_id);
            new HttpsRequest(Consts.DELETE_ADDRESS, deleteAddressHashmap, getActivity()).stringPost("Tag", new Helper() {
                @Override
                public void backResponse(boolean flag, String msg, JSONObject response) {
                    if (flag) {
                        if (msg.equals("0")) {
                            //address_relative.setVisibility(View.GONE);

                            getAddress();

                        }
                    } else {

                    }
                }
            });
        }
    }

}

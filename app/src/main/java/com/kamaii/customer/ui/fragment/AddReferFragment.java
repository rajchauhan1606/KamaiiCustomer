package com.kamaii.customer.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.Snackbar;
import com.kamaii.customer.DTO.UserDTO;
import com.kamaii.customer.R;
import com.kamaii.customer.https.HttpsRequest;
import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.interfacess.Helper;
import com.kamaii.customer.network.NetworkManager;
import com.kamaii.customer.preferences.SharedPrefrence;
import com.kamaii.customer.ui.activity.BaseActivity;
import com.kamaii.customer.utils.CustomEditText;
import com.kamaii.customer.utils.CustomTextView;
import com.kamaii.customer.utils.ProjectUtils;

import org.json.JSONObject;

import java.util.HashMap;


public class AddReferFragment extends Fragment {

    private final String TAG = AddReferFragment.class.getSimpleName();
    CustomTextView btnenter;
    CustomEditText edtmobileno, edtfname, edtemail;
    RelativeLayout RRsncbar;
    String name = "";
    SQLiteDatabase db;
    private View view;
    private BaseActivity baseActivity;
    private UserDTO userDTO;
    private SharedPrefrence prefrence;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_addrefer, container, false);

        //  getActivity().findViewById(R.id.ivLogo).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.customer_location_relative_header).setVisibility(View.GONE);

        baseActivity.headerNameTV.setText(getResources().getString(R.string.addrefer));
        getActivity().findViewById(R.id.headerNameTV).setVisibility(View.VISIBLE);
      prefrence = SharedPrefrence.getInstance(getActivity());
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        btnenter = view.findViewById(R.id.btnenter);
        edtmobileno = view.findViewById(R.id.edtmobileno);
        edtfname = view.findViewById(R.id.edtfname);
        edtemail = view.findViewById(R.id.edtemail);
        RRsncbar = view.findViewById(R.id.RRsncbar);

        db = getActivity().openOrCreateDatabase("kamaii.db", android.content.Context.MODE_PRIVATE, null);
        try {
            final String CREATE_TABLE_CONTAIN = "CREATE TABLE IF NOT EXISTS con_data ("
                    + "ID INTEGER primary key AUTOINCREMENT,"
                    + "Name TEXT,"
                    + "Phone TEXT,"
                    + "Fav INTEGER,"
                    + "AlreadySent INTEGER);";

            db.execSQL(CREATE_TABLE_CONTAIN);
        } catch (Exception ignored) {
        }
        name = userDTO.getName();

        btnenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!ValidateMobile()) {
                    return;
                } else if (!validation(edtfname, getResources().getString(R.string.val_name))) {
                    return;
                } else {
                    if (NetworkManager.isConnectToInternet(getActivity())) {

                        addrefer();
                    } else {
                        ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
                    }
                }
            }
        });

        view.findViewById(R.id.upload_contacts_linear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(baseActivity, "Coming soon....", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        baseActivity = (BaseActivity) activity;
    }

    public void addrefer() {
        new HttpsRequest(Consts.Add_REFER, getparm(), getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {

                        ProjectUtils.showToast(getActivity(), msg);

                        String phoneNumberWithCountryCode = "+91" + edtmobileno.getText().toString();
                        String message = "Dear" + " " + edtfname.getText().toString() + "\n \n" + getResources().getString(R.string.wamsgone) + name + getResources().getString(R.string.wamsgtwo);

                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse(
                                        String.format("https://api.whatsapp.com/send?phone=%s&text=%s",
                                                phoneNumberWithCountryCode, message))));

                        edtemail.setText("");
                        edtfname.setText("");
                        edtmobileno.setText("");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    ProjectUtils.showToast(getActivity(), msg);
                }
            }
        });
    }

    public void callSqlite() {

        //progressDialog = new ProgressDialog(getActivity());
        //progressDialog.setMessage("Please Wait...");
        //progressDialog.show();
//                db = getActivity().openOrCreateDatabase("DATABASE", android.content.Context.MODE_PRIVATE, null);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 111);
        } else {


           // new UploadContacts().execute();
//                    callSqlite();
        }
    }


    public HashMap<String, String> getparm() {

        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.EMAIL_ID, "");
        parms.put(Consts.NAME, ProjectUtils.getEditTextValue(edtfname));
        parms.put(Consts.MOBILE, ProjectUtils.getEditTextValue(edtmobileno));
        parms.put(Consts.USER_ID, userDTO.getUser_id());
        return parms;
    }

    public boolean ValidateMobile() {

        if (!ProjectUtils.isPhoneNumberValid(edtmobileno.getText().toString().trim())) {
            String msg = getResources().getString(R.string.val_mobile);
            Snackbar snackbar = Snackbar.make(RRsncbar, msg, Snackbar.LENGTH_LONG);
            View snackbarView = snackbar.getView();
            snackbarView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            snackbar.show();
            return false;
        } else {
            return true;
        }

    }

    public boolean validation(EditText editText, String msg) {
        if (!ProjectUtils.isEditTextFilled(editText)) {
            Snackbar snackbar = Snackbar.make(RRsncbar, msg, Snackbar.LENGTH_LONG);
            View snackbarView = snackbar.getView();
            snackbarView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            snackbar.show();
            return false;
        } else {
            return true;
        }
    }

/*
    class UploadContacts extends AsyncTask<String,String,String>{

        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                Cursor cc = db.rawQuery("SELECT * FROM con_data", null);
                if (cc != null) {
                    cc.moveToFirst();
                    getActivity().startManagingCursor(cc);

                    if (cc.getCount() < 1) {
                        Intent intent4 = new Intent(getActivity(), NormalFilePickActivity.class);
                        intent4.putExtra(Constant.MAX_NUMBER, 1);
                        intent4.putExtra(com.webknight.filemanager.activity.BaseActivity.IS_NEED_FOLDER_LIST, false);
                        intent4.putExtra(NormalFilePickActivity.SUFFIX,
                                new String[]{"vcf"});
                        startActivityForResult(intent4, Constant.REQUEST_CODE_PICK_FILE);
                        //Toast.makeText(getActivity(), "khali", Toast.LENGTH_SHORT).show();
                        //  Log.e("khali",""+"khali");
                    } else {
                        getActivity().startActivity(new Intent(getActivity(), ContactListActivity.class));
                        // Toast.makeText(getActivity(), "bharelu", Toast.LENGTH_SHORT).show();
                        //Log.e("khali",""+"bharelu");

                    }
                }
            }catch (Exception e){
                //    Log.e("ERROR",""+e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
        }
    }
*/
}

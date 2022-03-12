package com.kamaii.customer.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.kamaii.customer.DTO.UserDTO;
import com.kamaii.customer.R;
import com.kamaii.customer.https.HttpsRequest;
import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.interfacess.Helper;
import com.kamaii.customer.preferences.SharedPrefrence;
import com.kamaii.customer.ui.activity.BaseActivity;
import com.kamaii.customer.utils.CustomTextViewBold;

import org.json.JSONObject;

import java.util.HashMap;

public class ContactUs extends Fragment {
    private View view;

    private BaseActivity baseActivity;
    LinearLayout cardcall, cardchat, cardemail;
    private HashMap<String, String> chat = new HashMap<>();
    private SharedPrefrence prefrence;

    private UserDTO userDTO;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_contact, container, false);

        //    getActivity().findViewById(R.id.ivLogo).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.customer_location_relative_header).setVisibility(View.GONE);

        baseActivity.headerNameTV.setText(getResources().getString(R.string.contact));
        getActivity().findViewById(R.id.headerNameTV).setVisibility(View.VISIBLE);
       baseActivity.base_recyclerview.setVisibility(View.GONE);

        cardcall = view.findViewById(R.id.cardcall);
        cardchat = view.findViewById(R.id.cardchat);
        cardemail = view.findViewById(R.id.cardemail);

        prefrence = SharedPrefrence.getInstance(getActivity());

        userDTO = prefrence.getParentUser(Consts.USER_DTO);

        chat.put(Consts.USER_ID, userDTO.getUser_id());

        getActivity().findViewById(R.id.ivLogo).setVisibility(View.GONE);
        getActivity().findViewById(R.id.headerNameTV).setVisibility(View.VISIBLE);
        CustomTextViewBold screen_name = getActivity().findViewById(R.id.headerNameTV);
        cardemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
            }
        });
        cardchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    whatsappChatmsg();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        cardcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:9909998402"));
                startActivity(intent);

            }
        });
        return view;
    }

    public void whatsappChatmsg()   {
        new HttpsRequest(Consts.GET_WHATS_APP_CHAT_API, chat, getContext()).stringPost("Kamaii", new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                        JSONObject jsonObject = response;
                        String status = jsonObject.getString("status");
                        String success_msg = jsonObject.getString("message");
                        String data_msg = jsonObject.getString("data");

                        String phoneNumberWithCountryCode = "+919909998402";
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse(
                                        String.format("https://api.whatsapp.com/send?phone=%s&text=%s",
                                                phoneNumberWithCountryCode, data_msg))));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    protected void sendEmail() {
        String[] TO = {"support@kamaii.in"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));


        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        baseActivity = (BaseActivity) activity;
    }
}

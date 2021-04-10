package com.kamaii.customer.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kamaii.customer.DTO.ArtistDetailsDTO;
import com.kamaii.customer.DTO.ProductDTO;
import com.kamaii.customer.R;
import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.interfacess.OnSelectedItemListener;
import com.kamaii.customer.interfacess.SetonItemPlusListener;
import com.kamaii.customer.ui.activity.ArtistProfile;
import com.kamaii.customer.ui.activity.ViewServices;
import com.kamaii.customer.ui.adapter.SubCateProviderAdapter;
import com.kamaii.customer.ui.models.SubCateModel;
import com.kamaii.customer.utils.CustomTextViewBold;

import java.io.Serializable;
import java.util.ArrayList;

public class ServiceProfile extends Fragment {
    private View view;
    public RecyclerView rvSubCats;
    public RecyclerView rvProductList;
    private ArtistDetailsDTO artistDetailsDTO;
    private SubCateProviderAdapter subcatAdapter;
    private ArrayList<SubCateModel> subcatDTOList;
    private ArrayList<ProductDTO> productDTOList;
    private CustomTextViewBold tvReviewsText;
    private Bundle bundle;
    private CustomTextViewBold tvNotFound;
    private LinearLayout llList;
    public static String subleval = "";
    private SetonItemPlusListener setonItemPlusListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_service_profile, container, false);
        bundle = this.getArguments();
        artistDetailsDTO = (ArtistDetailsDTO) bundle.getSerializable(Consts.ARTIST_DTO);
        showUiAction(view);
        return view;
    }

    public void showUiAction(View v) {
        tvNotFound = (CustomTextViewBold) v.findViewById(R.id.tvNotFound);
        llList = v.findViewById(R.id.llList);

        tvReviewsText = v.findViewById(R.id.tvReviewsText);
        rvSubCats = v.findViewById(R.id.rvSubCats);
        rvProductList = v.findViewById(R.id.rvProductList);
        rvSubCats.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        showSubCatData();
    }


    public void showSubCatData() {
        subcatDTOList = new ArrayList<>();
        subcatDTOList = artistDetailsDTO.getSubcategory();
        if (subcatDTOList.size() > 0) {
            llList.setVisibility(View.VISIBLE);
            tvNotFound.setVisibility(View.GONE);
            subcatAdapter = new SubCateProviderAdapter(getActivity(), subcatDTOList, onItemListener, artistDetailsDTO.getUser_id(), artistDetailsDTO.getName());
            rvSubCats.setAdapter(subcatAdapter);
            tvReviewsText.setText(getString(R.string.reviews) + subcatDTOList.size() + ")");

        } else {
            llList.setVisibility(View.GONE);
            tvNotFound.setVisibility(View.VISIBLE);
        }
    }


    private OnSelectedItemListener onItemListener = new OnSelectedItemListener() {
        @Override
        public void setOnClick(String selectionString, int position) {
        }
    };
}

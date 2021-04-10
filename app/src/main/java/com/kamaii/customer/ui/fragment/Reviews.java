package com.kamaii.customer.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kamaii.customer.DTO.ArtistDetailsDTO;
import com.kamaii.customer.DTO.ReviewsDTO;
import com.kamaii.customer.R;
import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.ui.adapter.ReviewAdapter;
import com.kamaii.customer.utils.CustomTextViewBold;

import java.util.ArrayList;

public class Reviews extends Fragment {
    private View view;
    private RecyclerView rvReviews;
    private ArtistDetailsDTO artistDetailsDTO;
    private ReviewAdapter reviewAdapter;
    private LinearLayoutManager mLayoutManagerReview;
    private ArrayList<ReviewsDTO> reviewsDTOList;
    private CustomTextViewBold tvReviewsText;
    private Bundle bundle;
    private CustomTextViewBold tvNotFound;
    private LinearLayout llList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_reviews, container, false);
        bundle = this.getArguments();
        artistDetailsDTO = (ArtistDetailsDTO) bundle.getSerializable(Consts.ARTIST_DTO);
        showUiAction(view);
        return view;
    }

    public void showUiAction(View v) {
        tvNotFound = (CustomTextViewBold) v.findViewById(R.id.tvNotFound);
        llList = v.findViewById(R.id.llList);

        tvReviewsText = v.findViewById(R.id.tvReviewsText);
        rvReviews = v.findViewById(R.id.rvReviews);
        mLayoutManagerReview = new LinearLayoutManager(getActivity().getApplicationContext());

        rvReviews.setLayoutManager(mLayoutManagerReview);
        showData();
    }


    public void showData() {
        reviewsDTOList = new ArrayList<>();
        reviewsDTOList = artistDetailsDTO.getReviews();
        if (reviewsDTOList.size() > 0) {
            llList.setVisibility(View.VISIBLE);
            tvNotFound.setVisibility(View.GONE);
            reviewAdapter = new ReviewAdapter(getActivity(), reviewsDTOList);
            rvReviews.setAdapter(reviewAdapter);
            tvReviewsText.setText(getString(R.string.reviews) + reviewsDTOList.size() + ")");
        } else {
            llList.setVisibility(View.GONE);
            tvNotFound.setVisibility(View.VISIBLE);
        }

    }
}

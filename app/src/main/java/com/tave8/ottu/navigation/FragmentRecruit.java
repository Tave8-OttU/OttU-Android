package com.tave8.ottu.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;

import com.tave8.ottu.R;
import com.tave8.ottu.RecruitActivity;

public class FragmentRecruit extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_recruit, container, false);

        Display display = requireActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = getResources().getDisplayMetrics().density;
        float dpHeight = outMetrics.heightPixels / density;

        if (dpHeight < 700) {   //화면 크기가 700보다 작을 때
            LinearLayout llCommunity = rootView.findViewById(R.id.ll_frag_recruit);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            llCommunity.setLayoutParams(layoutParams);
            llCommunity.setOrientation(LinearLayout.HORIZONTAL);
        }

        AppCompatImageButton ibtNetflix = rootView.findViewById(R.id.ibt_frag_recruit_netflix);
        AppCompatImageButton ibtTving = rootView.findViewById(R.id.ibt_frag_recruit_tving);
        AppCompatImageButton ibtWavve = rootView.findViewById(R.id.ibt_frag_recruit_wavve);
        AppCompatImageButton ibtWatcha = rootView.findViewById(R.id.ibt_frag_recruit_watcha);
        AppCompatImageButton ibtDisneyPlus = rootView.findViewById(R.id.ibt_frag_recruit_disneyplus);
        AppCompatImageButton ibtCoupangPlay = rootView.findViewById(R.id.ibt_frag_recruit_coupangplay);

        ibtNetflix.setOnClickListener(v -> {
            Intent netflixRecruit = new Intent(rootView.getContext(), RecruitActivity.class);
            Bundle bundle = new Bundle();
                bundle.putInt("platformIdx", 1);
            netflixRecruit.putExtras(bundle);
            startActivity(netflixRecruit);
        });

        ibtTving.setOnClickListener(v -> {
            Intent netflixRecruit = new Intent(rootView.getContext(), RecruitActivity.class);
            Bundle bundle = new Bundle();
                bundle.putInt("platformIdx", 2);
            netflixRecruit.putExtras(bundle);
            startActivity(netflixRecruit);
        });

        ibtWavve.setOnClickListener(v -> {
            Intent netflixRecruit = new Intent(rootView.getContext(), RecruitActivity.class);
            Bundle bundle = new Bundle();
                bundle.putInt("platformIdx", 3);
            netflixRecruit.putExtras(bundle);
            startActivity(netflixRecruit);
        });

        ibtWatcha.setOnClickListener(v -> {
            Intent netflixRecruit = new Intent(rootView.getContext(), RecruitActivity.class);
            Bundle bundle = new Bundle();
                bundle.putInt("platformIdx", 4);
            netflixRecruit.putExtras(bundle);
            startActivity(netflixRecruit);
        });

        ibtDisneyPlus.setOnClickListener(v -> {
            Intent netflixRecruit = new Intent(rootView.getContext(), RecruitActivity.class);
            Bundle bundle = new Bundle();
                bundle.putInt("platformIdx", 5);
            netflixRecruit.putExtras(bundle);
            startActivity(netflixRecruit);
        });

        ibtCoupangPlay.setOnClickListener(v -> {
            Intent netflixRecruit = new Intent(rootView.getContext(), RecruitActivity.class);
            Bundle bundle = new Bundle();
                bundle.putInt("platformIdx", 6);
            netflixRecruit.putExtras(bundle);
            startActivity(netflixRecruit);
        });

        return rootView;
    }
}

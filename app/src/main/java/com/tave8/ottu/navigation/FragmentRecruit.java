package com.tave8.ottu.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

        AppCompatImageButton ibtNetflix = rootView.findViewById(R.id.ibt_frag_recruit_netflix);
        AppCompatImageButton ibtTving = rootView.findViewById(R.id.ibt_frag_recruit_tving);
        AppCompatImageButton ibtWavve = rootView.findViewById(R.id.ibt_frag_recruit_wavve);
        AppCompatImageButton ibtWatcha = rootView.findViewById(R.id.ibt_frag_recruit_watcha);
        AppCompatImageButton ibtDisneyPlus = rootView.findViewById(R.id.ibt_frag_recruit_disneyplus);
        AppCompatImageButton ibtCoupangPlay = rootView.findViewById(R.id.ibt_frag_recruit_coupangplay);

        ibtNetflix.setOnClickListener(v -> {
            Intent netflixRecruit = new Intent(rootView.getContext(), RecruitActivity.class);
            Bundle bundle = new Bundle();
                bundle.putInt("platformId", 1);
            netflixRecruit.putExtras(bundle);
            startActivity(netflixRecruit);
        });

        ibtTving.setOnClickListener(v -> {
            Intent netflixRecruit = new Intent(rootView.getContext(), RecruitActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("platformId", 2);
            netflixRecruit.putExtras(bundle);
            startActivity(netflixRecruit);
        });

        ibtWavve.setOnClickListener(v -> {
            Intent netflixRecruit = new Intent(rootView.getContext(), RecruitActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("platformId", 3);
            netflixRecruit.putExtras(bundle);
            startActivity(netflixRecruit);
        });

        ibtWatcha.setOnClickListener(v -> {
            Intent netflixRecruit = new Intent(rootView.getContext(), RecruitActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("platformId", 4);
            netflixRecruit.putExtras(bundle);
            startActivity(netflixRecruit);
        });

        ibtDisneyPlus.setOnClickListener(v -> {
            Intent netflixRecruit = new Intent(rootView.getContext(), RecruitActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("platformId", 5);
            netflixRecruit.putExtras(bundle);
            startActivity(netflixRecruit);
        });

        ibtCoupangPlay.setOnClickListener(v -> {
            Intent netflixRecruit = new Intent(rootView.getContext(), RecruitActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("platformId", 6);
            netflixRecruit.putExtras(bundle);
            startActivity(netflixRecruit);
        });

        return rootView;
    }
}

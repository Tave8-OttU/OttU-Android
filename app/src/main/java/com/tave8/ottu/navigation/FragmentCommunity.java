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
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tave8.ottu.CommunityActivity;
import com.tave8.ottu.R;

public class FragmentCommunity extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_community, container, false);

        Display display = requireActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = getResources().getDisplayMetrics().density;
        float dpHeight = outMetrics.heightPixels / density;

        if (dpHeight < 800) {   //화면 크기가 800보다 작을 때
            LinearLayout llCommunity = rootView.findViewById(R.id.ll_frag_community);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            llCommunity.setLayoutParams(layoutParams);
            llCommunity.setOrientation(LinearLayout.VERTICAL);
        }

        RelativeLayout rlNetflix = rootView.findViewById(R.id.rl_frag_community_netflix);
        RelativeLayout rlTving = rootView.findViewById(R.id.rl_frag_community_tving);
        RelativeLayout rlWavve = rootView.findViewById(R.id.rl_frag_community_wavve);
        RelativeLayout rlWatcha = rootView.findViewById(R.id.rl_frag_community_watcha);
        RelativeLayout rlDisneyPlus = rootView.findViewById(R.id.rl_frag_community_disney);
        RelativeLayout rlCoupangPlay = rootView.findViewById(R.id.rl_frag_community_coupangplay);

        rlNetflix.setOnClickListener(v -> {
            Intent netflixCommunity = new Intent(rootView.getContext(), CommunityActivity.class);
            Bundle bundle = new Bundle();
                bundle.putInt("platformIdx", 1);
            netflixCommunity.putExtras(bundle);
            startActivity(netflixCommunity);
        });

        rlTving.setOnClickListener(v -> {
            Intent tvingCommunity = new Intent(rootView.getContext(), CommunityActivity.class);
            Bundle bundle = new Bundle();
                bundle.putInt("platformIdx", 2);
            tvingCommunity.putExtras(bundle);
            startActivity(tvingCommunity);
        });

        rlWavve.setOnClickListener(v -> {
            Intent wavveCommunity = new Intent(rootView.getContext(), CommunityActivity.class);
            Bundle bundle = new Bundle();
                bundle.putInt("platformIdx", 3);
            wavveCommunity.putExtras(bundle);
            startActivity(wavveCommunity);
        });

        rlWatcha.setOnClickListener(v -> {
            Intent watchaCommunity = new Intent(rootView.getContext(), CommunityActivity.class);
            Bundle bundle = new Bundle();
                bundle.putInt("platformIdx", 4);
            watchaCommunity.putExtras(bundle);
            startActivity(watchaCommunity);
        });

        rlDisneyPlus.setOnClickListener(v -> {
            Intent disneyPlusCommunity = new Intent(rootView.getContext(), CommunityActivity.class);
            Bundle bundle = new Bundle();
                bundle.putInt("platformIdx", 5);
            disneyPlusCommunity.putExtras(bundle);
            startActivity(disneyPlusCommunity);
        });

        rlCoupangPlay.setOnClickListener(v -> {
            Intent coupangPlayCommunity = new Intent(rootView.getContext(), CommunityActivity.class);
            Bundle bundle = new Bundle();
                bundle.putInt("platformIdx", 6);
            coupangPlayCommunity.putExtras(bundle);
            startActivity(coupangPlayCommunity);
        });

        return rootView;
    }
}

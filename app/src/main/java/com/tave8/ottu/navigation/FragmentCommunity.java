package com.tave8.ottu.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        RelativeLayout rlNetflix = rootView.findViewById(R.id.rl_frag_community_netflix);
        RelativeLayout rlTving = rootView.findViewById(R.id.rl_frag_community_tving);
        RelativeLayout rlWavve = rootView.findViewById(R.id.rl_frag_community_wavve);
        RelativeLayout rlWatcha = rootView.findViewById(R.id.rl_frag_community_watcha);
        RelativeLayout rlDisneyPlus = rootView.findViewById(R.id.rl_frag_community_disney);
        RelativeLayout rlCoupangPlay = rootView.findViewById(R.id.rl_frag_community_coupangplay);

        rlNetflix.setOnClickListener(v -> {
            Intent netflixCommunity = new Intent(rootView.getContext(), CommunityActivity.class);
            Bundle bundle = new Bundle();
                bundle.putInt("platformId", 1);
            netflixCommunity.putExtras(bundle);
            startActivity(netflixCommunity);
        });

        rlTving.setOnClickListener(v -> {
            Intent tvingCommunity = new Intent(rootView.getContext(), CommunityActivity.class);
            Bundle bundle = new Bundle();
                bundle.putInt("platformId", 2);
            tvingCommunity.putExtras(bundle);
            startActivity(tvingCommunity);
        });

        rlWavve.setOnClickListener(v -> {
            Intent wavveCommunity = new Intent(rootView.getContext(), CommunityActivity.class);
            Bundle bundle = new Bundle();
                bundle.putInt("platformId", 3);
            wavveCommunity.putExtras(bundle);
            startActivity(wavveCommunity);
        });

        rlWatcha.setOnClickListener(v -> {
            Intent watchaCommunity = new Intent(rootView.getContext(), CommunityActivity.class);
            Bundle bundle = new Bundle();
                bundle.putInt("platformId", 4);
            watchaCommunity.putExtras(bundle);
            startActivity(watchaCommunity);
        });

        rlDisneyPlus.setOnClickListener(v -> {
            Intent disneyPlusCommunity = new Intent(rootView.getContext(), CommunityActivity.class);
            Bundle bundle = new Bundle();
                bundle.putInt("platformId", 5);
            disneyPlusCommunity.putExtras(bundle);
            startActivity(disneyPlusCommunity);
        });

        rlCoupangPlay.setOnClickListener(v -> {
            Intent coupangPlayCommunity = new Intent(rootView.getContext(), CommunityActivity.class);
            Bundle bundle = new Bundle();
                bundle.putInt("platformId", 6);
            coupangPlayCommunity.putExtras(bundle);
            startActivity(coupangPlayCommunity);
        });

        return rootView;
    }
}

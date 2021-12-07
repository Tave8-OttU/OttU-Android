package com.tave8.ottu.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.tave8.ottu.MyOTTActivity;
import com.tave8.ottu.MyRecruitActivity;
import com.tave8.ottu.R;

import static com.tave8.ottu.MainActivity.myInfo;

public class FragmentMypage extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_mypage, container, false);

        TextView tvNick = rootView.findViewById(R.id.tv_frag_mypage_nick);
        tvNick.setText(myInfo.getNick());

        ProgressBar pbOttULevel = rootView.findViewById(R.id.pb_frag_mypage_level);
        TextView tvOttULevel = rootView.findViewById(R.id.tv_frag_mypage_level);
        pbOttULevel.setProgress(myInfo.getLevel());
        tvOttULevel.setText(String.valueOf(myInfo.getLevel()));
        if (myInfo.isFirst()) {
            pbOttULevel.setProgressDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.bg_progress_first));
            tvOttULevel.setTextColor(requireContext().getColor(R.color.sub_text_color));
        }

        AppCompatButton btGenre1 = rootView.findViewById(R.id.bt_frag_mypage_genre1);
        AppCompatButton btGenre2 = rootView.findViewById(R.id.bt_frag_mypage_genre2);
        AppCompatButton btGenre3 = rootView.findViewById(R.id.bt_frag_mypage_genre3);
        if (myInfo.getInterestGenre().size() == 1) {
            btGenre1.setText(myInfo.getInterestGenre().get(0).getGenreName());
            btGenre2.setVisibility(View.INVISIBLE);
            btGenre3.setVisibility(View.INVISIBLE);
        } else if (myInfo.getInterestGenre().size() == 2) {
            btGenre1.setText(myInfo.getInterestGenre().get(0).getGenreName());
            btGenre2.setText(myInfo.getInterestGenre().get(1).getGenreName());
            btGenre3.setVisibility(View.INVISIBLE);
        } else {
            btGenre1.setText(myInfo.getInterestGenre().get(0).getGenreName());
            btGenre2.setText(myInfo.getInterestGenre().get(1).getGenreName());
            btGenre3.setText(myInfo.getInterestGenre().get(2).getGenreName());
        }

        TextView tvMyOTT = rootView.findViewById(R.id.tv_frag_mypage_my_ott);
        tvMyOTT.setOnClickListener(v -> startActivity(new Intent(requireContext(), MyOTTActivity.class)));

        TextView tvMyRecruit = rootView.findViewById(R.id.tv_frag_mypage_my_recruit);
        tvMyRecruit.setOnClickListener(v -> startActivity(new Intent(requireContext(), MyRecruitActivity.class)));

        return rootView;
    }
}

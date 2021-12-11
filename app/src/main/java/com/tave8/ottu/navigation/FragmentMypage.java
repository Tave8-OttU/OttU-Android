package com.tave8.ottu.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.tave8.ottu.ChangeGenreActivity;
import com.tave8.ottu.ChangeKakaoTalkIdActivity;
import com.tave8.ottu.ChangeNickActivity;
import com.tave8.ottu.LoginActivity;
import com.tave8.ottu.MyOTTActivity;
import com.tave8.ottu.MyRecruitActivity;
import com.tave8.ottu.R;

import static android.app.Activity.RESULT_OK;
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

        LinearLayout llMyOTT = rootView.findViewById(R.id.ll_frag_mypage_my_ott);
        llMyOTT.setOnClickListener(v -> startActivity(new Intent(requireContext(), MyOTTActivity.class)));

        LinearLayout llMyRecruit = rootView.findViewById(R.id.ll_frag_mypage_my_recruit);
        llMyRecruit.setOnClickListener(v -> startActivity(new Intent(requireContext(), MyRecruitActivity.class)));

        ActivityResultLauncher<Intent> startChangeActivityResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK)
                        updateMyInfo();
                });

        LinearLayout llChangeNick = rootView.findViewById(R.id.ll_frag_mypage_change_nick);
        llChangeNick.setOnClickListener(v -> startChangeActivityResult.launch(new Intent(getContext(), ChangeNickActivity.class)));

        LinearLayout llChangeGenre = rootView.findViewById(R.id.ll_frag_mypage_change_genre);
        llChangeGenre.setOnClickListener(v -> startChangeActivityResult.launch(new Intent(getContext(), ChangeGenreActivity.class)));

        LinearLayout llChangeKakaoId = rootView.findViewById(R.id.ll_frag_mypage_change_kakaoid);
        llChangeKakaoId.setOnClickListener(v -> startChangeActivityResult.launch(new Intent(getContext(), ChangeKakaoTalkIdActivity.class)));

        LinearLayout llLogout = rootView.findViewById(R.id.ll_frag_mypage_logout);
        llLogout.setOnClickListener(v -> {
            //TODO: 서버에 전달 혹은 jwt 키 삭제
            //PreferenceManager.removeKey(this, "jwt");
            startActivity(new Intent(getContext(), LoginActivity.class));
            requireActivity().finish();
        });

        LinearLayout llWithdrawal = rootView.findViewById(R.id.ll_frag_mypage_withdrawal);
        llWithdrawal.setOnClickListener(v -> {
            //TODO: 서버에 전달
            startActivity(new Intent(getContext(), LoginActivity.class));
            requireActivity().finish();
        });

        return rootView;
    }

    private void updateMyInfo() {
        //TODO: 서버로부터 내 정보를 받음(userId, 닉네임, 이메일, 카카오아이디, 오뜨레벨, isFirst, 관심 장르) -> myInfo 내용을 변경해야 함!
    }
}

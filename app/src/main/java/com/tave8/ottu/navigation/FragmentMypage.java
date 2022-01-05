package com.tave8.ottu.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.tave8.ottu.MyCommunityActivity;
import com.tave8.ottu.MyOTTActivity;
import com.tave8.ottu.MyRecruitActivity;
import com.tave8.ottu.OttURetrofitClient;
import com.tave8.ottu.PreferenceManager;
import com.tave8.ottu.R;
import com.tave8.ottu.data.Genre;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.tave8.ottu.MainActivity.myInfo;

public class FragmentMypage extends Fragment {
    private AppCompatButton btGenre1, btGenre2, btGenre3;
    private ProgressBar pbOttULevel;
    private TextView tvNick, tvOttULevel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_mypage, container, false);

        tvNick = rootView.findViewById(R.id.tv_frag_mypage_nick);
        tvNick.setText(myInfo.getNick());

        pbOttULevel = rootView.findViewById(R.id.pb_frag_mypage_level);
        tvOttULevel = rootView.findViewById(R.id.tv_frag_mypage_level);
        pbOttULevel.setProgress(myInfo.getReliability());
        tvOttULevel.setText(String.valueOf(myInfo.getReliability()));
        if (myInfo.isFirst()) {
            pbOttULevel.setProgressDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.bg_progress_first));
            tvOttULevel.setTextColor(requireContext().getColor(R.color.sub_text_color));
        }

        btGenre1 = rootView.findViewById(R.id.bt_frag_mypage_genre1);
        btGenre2 = rootView.findViewById(R.id.bt_frag_mypage_genre2);
        btGenre3 = rootView.findViewById(R.id.bt_frag_mypage_genre3);
        if (myInfo.getInterestGenre().size() == 0) {
            btGenre1.setText("없음");
            btGenre2.setVisibility(View.INVISIBLE);
            btGenre3.setVisibility(View.INVISIBLE);
        } else if (myInfo.getInterestGenre().size() == 1) {
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

        getMyInfo();

        LinearLayout llMyRecruit = rootView.findViewById(R.id.ll_frag_mypage_my_recruit);
        llMyRecruit.setOnClickListener(v -> startActivity(new Intent(requireContext(), MyRecruitActivity.class)));

        LinearLayout llMyCommunityPost = rootView.findViewById(R.id.ll_frag_mypage_my_community_post);
        llMyCommunityPost.setOnClickListener(v -> startActivity(new Intent(requireContext(), MyCommunityActivity.class)));

        LinearLayout llMyOTT = rootView.findViewById(R.id.ll_frag_mypage_my_ott);
        llMyOTT.setOnClickListener(v -> startActivity(new Intent(requireContext(), MyOTTActivity.class)));

        ActivityResultLauncher<Intent> startChangeActivityResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        if (Objects.requireNonNull(result.getData()).hasExtra("isGenre"))
                            getMyInfo();
                        else
                            updateMyInfo();
                    }
                });

        LinearLayout llChangeNick = rootView.findViewById(R.id.ll_frag_mypage_change_nick);
        llChangeNick.setOnClickListener(v -> startChangeActivityResult.launch(new Intent(getContext(), ChangeNickActivity.class)));

        LinearLayout llChangeGenre = rootView.findViewById(R.id.ll_frag_mypage_change_genre);
        llChangeGenre.setOnClickListener(v -> startChangeActivityResult.launch(new Intent(getContext(), ChangeGenreActivity.class)));

        LinearLayout llChangeKakaoId = rootView.findViewById(R.id.ll_frag_mypage_change_kakaoid);
        llChangeKakaoId.setOnClickListener(v -> startChangeActivityResult.launch(new Intent(getContext(), ChangeKakaoTalkIdActivity.class)));

        LinearLayout llLogout = rootView.findViewById(R.id.ll_frag_mypage_logout);
        llLogout.setOnClickListener(v -> {
            PreferenceManager.removeKey(getContext(), "jwt");
            startActivity(new Intent(getContext(), LoginActivity.class));
            requireActivity().finish();
        });

        LinearLayout llWithdrawal = rootView.findViewById(R.id.ll_frag_mypage_withdrawal);
        llWithdrawal.setOnClickListener(v -> {
            OttURetrofitClient.getApiService().deleteUser(PreferenceManager.getString(requireContext(), "jwt"), myInfo.getUserIdx()).enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if (response.code() == 200) {
                        PreferenceManager.removeKey(getContext(), "jwt");
                        startActivity(new Intent(getContext(), LoginActivity.class));
                        requireActivity().finish();
                    }
                    else if (response.code() == 401) {
                        Toast.makeText(requireContext(), "로그인 기한이 만료되어\n 로그인 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
                        PreferenceManager.removeKey(requireContext(), "jwt");
                        Intent reLogin = new Intent(requireContext(), LoginActivity.class);
                        reLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        requireContext().startActivity(reLogin);
                        requireActivity().finish();
                    }
                    else
                        Toast.makeText(requireContext(), "회원 삭제에 문제가 생겼습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    Toast.makeText(requireContext(), "서버와 연결되지 않았습니다. 확인해 주세요.", Toast.LENGTH_SHORT).show();
                }
            });
        });

        return rootView;
    }

    private void getMyInfo() {
        OttURetrofitClient.getApiService().getUser(PreferenceManager.getString(getContext(), "jwt"), myInfo.getUserIdx()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.code() == 200) {
                    try {
                        JSONObject userInfo = new JSONObject(Objects.requireNonNull(response.body()));
                        JSONObject user = userInfo.getJSONObject("user");
                        myInfo.getUserEssentialInfo().setNick(user.getString("nickname"));
                        myInfo.setKakaotalkId(user.getString("kakaotalkId"));
                        myInfo.setReliability(user.getInt("reliability"));
                        myInfo.setIsFirst(user.getBoolean("isFirst"));

                        JSONArray genres = user.getJSONArray("genres");
                        myInfo.getInterestGenre().clear();
                        for (int i=0; i<genres.length(); i++) {
                            JSONObject genre = genres.getJSONObject(i);
                            myInfo.getInterestGenre().add(new Genre(genre.getInt("genreIdx"), genre.getString("genreName")));
                        }

                        updateMyInfo();
                    } catch (JSONException e) { e.printStackTrace(); }
                }
                else if (response.code() == 401) {
                    Toast.makeText(getContext(), "로그인 기한이 만료되어\n 로그인 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
                    PreferenceManager.removeKey(getContext(), "jwt");
                    Intent reLogin = new Intent(getContext(), LoginActivity.class);
                    reLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    requireContext().startActivity(reLogin);
                    requireActivity().finish();
                }
                else
                    Toast.makeText(getContext(), "회원 정보 불러오기에 문제가 생겼습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "서버와 연결되지 않았습니다. 확인해 주세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateMyInfo() {
        tvNick.setText(myInfo.getNick());

        pbOttULevel.setProgress(myInfo.getReliability());
        tvOttULevel.setText(String.valueOf(myInfo.getReliability()));
        if (myInfo.isFirst()) {
            pbOttULevel.setProgressDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.bg_progress_first));
            tvOttULevel.setTextColor(requireContext().getColor(R.color.sub_text_color));
        }

        if (myInfo.getInterestGenre().size() == 0) {
            btGenre1.setText("없음");
            btGenre2.setVisibility(View.INVISIBLE);
            btGenre3.setVisibility(View.INVISIBLE);
        } else if (myInfo.getInterestGenre().size() == 1) {
            btGenre1.setText(myInfo.getInterestGenre().get(0).getGenreName());
            btGenre2.setVisibility(View.INVISIBLE);
            btGenre3.setVisibility(View.INVISIBLE);
        } else if (myInfo.getInterestGenre().size() == 2) {
            btGenre2.setVisibility(View.VISIBLE);
            btGenre1.setText(myInfo.getInterestGenre().get(0).getGenreName());
            btGenre2.setText(myInfo.getInterestGenre().get(1).getGenreName());
            btGenre3.setVisibility(View.INVISIBLE);
        } else {
            btGenre2.setVisibility(View.VISIBLE);
            btGenre3.setVisibility(View.VISIBLE);
            btGenre1.setText(myInfo.getInterestGenre().get(0).getGenreName());
            btGenre2.setText(myInfo.getInterestGenre().get(1).getGenreName());
            btGenre3.setText(myInfo.getInterestGenre().get(2).getGenreName());
        }
    }
}

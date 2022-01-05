package com.tave8.ottu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tave8.ottu.data.Genre;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tave8.ottu.MainActivity.myInfo;

public class ChangeGenreActivity extends AppCompatActivity {
    private boolean isChanged = false;
    private HashSet<View> interestingGenre = null;
    private HashSet<View> changedGenre = null;
    private ArrayList<Integer> genreIdList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_genre);

        Toolbar toolbar = findViewById(R.id.tb_change_genre_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
            Objects.requireNonNull(actionBar).setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
        View customView = View.inflate(this, R.layout.actionbar_mypage, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(customView, params);
        toolbarListener(toolbar);

        interestingGenre = new HashSet<>();
        changedGenre = new HashSet<>();
        genreIdList = new ArrayList<>();
        genreIdList.add(R.id.bt_change_genre_genre1);
        genreIdList.add(R.id.bt_change_genre_genre2);
        genreIdList.add(R.id.bt_change_genre_genre3);
        genreIdList.add(R.id.bt_change_genre_genre4);
        genreIdList.add(R.id.bt_change_genre_genre5);
        genreIdList.add(R.id.bt_change_genre_genre6);
        genreIdList.add(R.id.bt_change_genre_genre7);
        genreIdList.add(R.id.bt_change_genre_genre8);
        genreIdList.add(R.id.bt_change_genre_genre9);
        genreIdList.add(R.id.bt_change_genre_genre10);
        genreIdList.add(R.id.bt_change_genre_genre11);
        genreIdList.add(R.id.bt_change_genre_genre12);
        for (Genre genre : myInfo.getInterestGenre()) {
            int genreBtIndex = genre.getGenreIdx()-1;
            AppCompatButton btSelectedGenre = findViewById(genreIdList.get(genreBtIndex));
            btSelectedGenre.setBackgroundResource(R.drawable.bg_button_select);
            interestingGenre.add(btSelectedGenre);
            changedGenre.add(btSelectedGenre);
        }

        changeNickClickListener();
    }

    @Override
    public void finish() {
        if (isChanged) {
            Intent submittedIntent = new Intent();
            submittedIntent.putExtra("isGenre", true);
            setResult(RESULT_OK, submittedIntent);
        }
        else {
            Intent returnIntent = new Intent();
            setResult(RESULT_CANCELED, returnIntent);
        }

        super.finish();
    }

    private void toolbarListener(Toolbar toolbar) {
        AppCompatImageButton ibtBack = toolbar.findViewById(R.id.ibt_ab_mypage_back);
        ibtBack.setOnClickListener(v -> finish());

        TextView tvTitle = toolbar.findViewById(R.id.tv_ab_mypage_title);
        tvTitle.setText("관심 장르 변경");
    }

    private void changeNickClickListener() {
        View.OnClickListener genreClickListener = view -> {
            if (changedGenre.contains(view)) {
                changedGenre.remove(view);
                view.setBackgroundResource(R.drawable.bg_button_non_select);
            } else if (changedGenre.size() == 3) {
                Toast.makeText(this, "선택할 수 있는 관심 장르의 개수는 최대 3개입니다.", Toast.LENGTH_SHORT).show();
            } else {
                changedGenre.add(view);
                view.setBackgroundResource(R.drawable.bg_button_select);
            }
        };
        AppCompatButton btGenre1 = findViewById(R.id.bt_change_genre_genre1);
        btGenre1.setOnClickListener(genreClickListener);
        AppCompatButton btGenre2 = findViewById(R.id.bt_change_genre_genre2);
        btGenre2.setOnClickListener(genreClickListener);
        AppCompatButton btGenre3 = findViewById(R.id.bt_change_genre_genre3);
        btGenre3.setOnClickListener(genreClickListener);
        AppCompatButton btGenre4 = findViewById(R.id.bt_change_genre_genre4);
        btGenre4.setOnClickListener(genreClickListener);
        AppCompatButton btGenre5 = findViewById(R.id.bt_change_genre_genre5);
        btGenre5.setOnClickListener(genreClickListener);
        AppCompatButton btGenre6 = findViewById(R.id.bt_change_genre_genre6);
        btGenre6.setOnClickListener(genreClickListener);
        AppCompatButton btGenre7 = findViewById(R.id.bt_change_genre_genre7);
        btGenre7.setOnClickListener(genreClickListener);
        AppCompatButton btGenre8 = findViewById(R.id.bt_change_genre_genre8);
        btGenre8.setOnClickListener(genreClickListener);
        AppCompatButton btGenre9 = findViewById(R.id.bt_change_genre_genre9);
        btGenre9.setOnClickListener(genreClickListener);
        AppCompatButton btGenre10 = findViewById(R.id.bt_change_genre_genre10);
        btGenre10.setOnClickListener(genreClickListener);
        AppCompatButton btGenre11 = findViewById(R.id.bt_change_genre_genre11);
        btGenre11.setOnClickListener(genreClickListener);
        AppCompatButton btGenre12 = findViewById(R.id.bt_change_genre_genre12);
        btGenre12.setOnClickListener(genreClickListener);

        Button btSubmit = findViewById(R.id.bt_change_genre_submit);
        btSubmit.setOnClickListener(v -> {
            if (changedGenre.isEmpty()) {
                Toast.makeText(this, "관심 장르를 하나 이상 선택해 주세요.", Toast.LENGTH_SHORT).show();
            } else if (changedGenre.equals(interestingGenre)) {
                Toast.makeText(this, "기존의 관심 장르와 동일합니다.", Toast.LENGTH_SHORT).show();
            } else {
                ArrayList<Integer> genres = new ArrayList<>();
                for (View genre : changedGenre) {
                    genres.add(genreIdList.indexOf(genre.getId())+1);
                }

                JsonObject requestData = new JsonObject();
                requestData.add("genres", new Gson().toJsonTree(genres));
                OttURetrofitClient.getApiService().patchUser(PreferenceManager.getString(this, "jwt"), myInfo.getUserIdx(), requestData).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        if (response.code() == 200) {
                            isChanged = true;
                            Toast.makeText(ChangeGenreActivity.this, "장르 변경에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else if (response.code() == 401) {
                            Toast.makeText(ChangeGenreActivity.this, "로그인 기한이 만료되어\n 로그인 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
                            PreferenceManager.removeKey(ChangeGenreActivity.this, "jwt");
                            Intent reLogin = new Intent(ChangeGenreActivity.this, LoginActivity.class);
                            reLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(reLogin);
                            finish();
                        }
                        else
                            Toast.makeText(ChangeGenreActivity.this, "장르 변경에 문제가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        Toast.makeText(ChangeGenreActivity.this, "서버와 연결되지 않았습니다. 확인해 주세요:)", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}

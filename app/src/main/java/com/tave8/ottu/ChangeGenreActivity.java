package com.tave8.ottu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;

import com.tave8.ottu.data.Genre;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

import static com.tave8.ottu.MainActivity.myInfo;

public class ChangeGenreActivity extends AppCompatActivity {
    private boolean isChanged = false;
    private HashSet<View> selectedGenre = null;
    private ArrayList<Integer> genreIdList = null;

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
        View customView = View.inflate(this, R.layout.actionbar_change, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(customView, params);
        toolbarListener(toolbar);

        selectedGenre = new HashSet<>();
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
            selectedGenre.add(btSelectedGenre);
        }

        changeNickClickListener();
    }

    @Override
    public void finish() {
        if (isChanged) {
            Intent submittedIntent = new Intent();
            setResult(RESULT_OK, submittedIntent);
        }
        else {
            Intent returnIntent = new Intent();
            setResult(RESULT_CANCELED, returnIntent);
        }

        super.finish();
    }

    private void toolbarListener(Toolbar toolbar) {
        AppCompatImageButton ibtBack = toolbar.findViewById(R.id.ibt_ab_change_back);
        ibtBack.setOnClickListener(v -> finish());

        TextView tvTitle = toolbar.findViewById(R.id.tv_ab_change_title);
        tvTitle.setText("관심 장르 변경");
    }

    private void changeNickClickListener() {
        View.OnClickListener genreClickListener = view -> {
            if (selectedGenre.contains(view)) {
                selectedGenre.remove(view);
                view.setBackgroundResource(R.drawable.bg_button_non_select);
            } else if (selectedGenre.size() == 3) {
                Toast.makeText(this, "선택할 수 있는 관심 장르의 개수는 최대 3개입니다.", Toast.LENGTH_SHORT).show();
            } else {
                selectedGenre.add(view);
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
            if (selectedGenre.isEmpty()) {
                Toast.makeText(this, "관심 장르를 하나 이상 선택해 주세요.", Toast.LENGTH_SHORT).show();
            }
            else {
                //TODO: 서버에 전달함 (if 예전과 똑같다면 그냥 보냄??)
                isChanged = true;
                finish();
            }
        });
    }
}

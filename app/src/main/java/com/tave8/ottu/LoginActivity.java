package com.tave8.ottu;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.tave8.ottu.data.Genre;
import com.tave8.ottu.data.UserInfo;

import java.util.ArrayList;

import static com.tave8.ottu.MainActivity.myInfo;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AppCompatButton btLogin = findViewById(R.id.bt_login);
        btLogin.setOnClickListener(v -> {
            //처음 사용 회원인 경우
            //startActivity(new Intent(LoginActivity.this, InitialSettingActivity.class));
            //finish();

            //회원 기록이 있는 사람인 경우
            //TODO: 예시
            ArrayList<Genre> interestGenreList = new ArrayList<>();
            interestGenreList.add(Genre.CRIME);
            interestGenreList.add(Genre.FANTASY);
            myInfo = new UserInfo(1L, "young@naver.com", "youngeun", "영은", 10, true, interestGenreList);

            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        });
    }
}

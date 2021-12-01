package com.tave8.ottu;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AppCompatButton btLogin = findViewById(R.id.bt_login);
        btLogin.setOnClickListener(v -> {
            //처음 사용 회원인 경우
            startActivity(new Intent(LoginActivity.this, InitialSettingActivity.class));
            finish();

            //회원 기록이 있는 사람인 경우
            //startActivity(new Intent(LoginActivity.this, MainActivity.class));
            //finish();
        });
    }
}

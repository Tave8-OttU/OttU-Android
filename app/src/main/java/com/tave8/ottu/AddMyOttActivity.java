package com.tave8.ottu;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class AddMyOttActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_my_ott);
        //TODO: 선택된 ott 서비스의 background는 @drawable/bg_r5white_s4blue
        //TODO: 요금제 선택 시의 background는 @drawable/bg_r5black_sblue이면 됨!
    }
}

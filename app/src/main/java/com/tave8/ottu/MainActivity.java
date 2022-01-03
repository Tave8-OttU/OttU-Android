package com.tave8.ottu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonObject;
import com.tave8.ottu.data.UserInfo;
import com.tave8.ottu.navigation.FragmentCommunity;
import com.tave8.ottu.navigation.FragmentHome;
import com.tave8.ottu.navigation.FragmentMypage;
import com.tave8.ottu.navigation.FragmentNotice;
import com.tave8.ottu.navigation.FragmentRecruit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    public static UserInfo myInfo = null;

    private FragmentManager fragmentManager;
    private FragmentCommunity fragmentCommunity;
    private FragmentRecruit fragmentRecruit;
    private FragmentHome fragmentHome;
    private FragmentNotice fragmentNotice;
    private FragmentMypage fragmentMypage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //알림 토큰 전달
        if (!PreferenceManager.getString(this, "notice_token").equals("")) {
            JsonObject requestData = new JsonObject();
            requestData.addProperty("notice_token", PreferenceManager.getString(this, "notice_token"));
            OttURetrofitClient.getApiService().patchNoticeToken(PreferenceManager.getString(this, "jwt"), myInfo.getUserIdx(), requestData).enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if (response.code() == 200);
                    else if (response.code() == 401) {
                        Toast.makeText(MainActivity.this, "로그인 기한이 만료되어\n 로그인 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
                        PreferenceManager.removeKey(MainActivity.this, "jwt");
                        Intent reLogin = new Intent(MainActivity.this, LoginActivity.class);
                        reLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(reLogin);
                        finish();
                    }
                    else
                        Toast.makeText(MainActivity.this, "알림 토큰 저장에 문제가 생겼습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    Toast.makeText(MainActivity.this, "서버와 연결되지 않았습니다. 확인해 주세요.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        fragmentManager = getSupportFragmentManager();
        fragmentCommunity = new FragmentCommunity();
        fragmentRecruit = new FragmentRecruit();
        fragmentHome = new FragmentHome();
        fragmentNotice = new FragmentNotice();
        fragmentMypage = new FragmentMypage();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fl_main, fragmentHome).commitAllowingStateLoss();

        bottomNavigationTabListener();
    }

    @SuppressLint("NonConstantResourceId")
    private void bottomNavigationTabListener() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_main);
        bottomNavigationView.setSelectedItemId(R.id.menu_nav_home);

        bottomNavigationView.setOnItemSelectedListener(menuItem -> {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch (menuItem.getItemId()) {
                case R.id.menu_nav_community: {
                    transaction.replace(R.id.fl_main, fragmentCommunity).commitAllowingStateLoss();
                    break;
                }
                case R.id.menu_nav_recruit: {
                    transaction.replace(R.id.fl_main, fragmentRecruit).commitAllowingStateLoss();
                    break;
                }
                case R.id.menu_nav_home: {
                    transaction.replace(R.id.fl_main, fragmentHome).commitAllowingStateLoss();
                    break;
                }
                case R.id.menu_nav_notice: {
                    transaction.replace(R.id.fl_main, fragmentNotice).commitAllowingStateLoss();
                    break;
                }
                case R.id.menu_nav_mypage: {
                    transaction.replace(R.id.fl_main, fragmentMypage).commitAllowingStateLoss();
                    break;
                }
            }
            return true;
        });
    }
}

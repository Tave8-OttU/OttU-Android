package com.tave8.ottu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tave8.ottu.navigation.FragmentCommunity;
import com.tave8.ottu.navigation.FragmentHome;
import com.tave8.ottu.navigation.FragmentMypage;
import com.tave8.ottu.navigation.FragmentNotice;
import com.tave8.ottu.navigation.FragmentRecruit;

public class MainActivity extends AppCompatActivity {
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

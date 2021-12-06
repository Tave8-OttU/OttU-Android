package com.tave8.ottu.navigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.tave8.ottu.R;
import com.tave8.ottu.adapter.HomeCommunityRecyclerAdapter;
import com.tave8.ottu.adapter.HomePaymentPagerAdapter;
import com.tave8.ottu.data.SimpleCommunityInfo;
import com.tave8.ottu.data.SimpleOttPayment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import me.relex.circleindicator.CircleIndicator3;

public class FragmentHome extends Fragment {
    private ArrayList<SimpleOttPayment> ottPaymentList = null;
    private ArrayList<SimpleCommunityInfo> ottCommunityList = null;

    private HomePaymentPagerAdapter homePaymentPagerAdapter;
    private HomeCommunityRecyclerAdapter homeCommunityRecyclerAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        ottPaymentList = new ArrayList<>();
        ottCommunityList = new ArrayList<>();

        //TODO: 임시
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        try {
            ottPaymentList.add(new SimpleOttPayment(2, new Date(String.valueOf(sdf.parse("2021-12-15")))));
            ottPaymentList.add(new SimpleOttPayment(3, new Date(String.valueOf(sdf.parse("2021-12-20")))));
            ottPaymentList.add(new SimpleOttPayment(6, new Date(String.valueOf(sdf.parse("2021-12-30")))));
        } catch (ParseException e) { e.printStackTrace(); }

        ViewPager2 vpOttPayment = rootView.findViewById(R.id.vp_home_payment);
        homePaymentPagerAdapter = new HomePaymentPagerAdapter(ottPaymentList);
        vpOttPayment.setAdapter(homePaymentPagerAdapter);
        vpOttPayment.setOffscreenPageLimit(3);
        vpOttPayment.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);
        vpOttPayment.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        CircleIndicator3 ciOttPayment = rootView.findViewById(R.id.ci_home_payment);
        ciOttPayment.setViewPager(vpOttPayment);

        //TODO: 임시
        ottCommunityList.add(new SimpleCommunityInfo(1, "Netflix", "커뮤티니 글 content"));
        ottCommunityList.add(new SimpleCommunityInfo(2, "Tving", "커뮤티니 글 content"));
        ottCommunityList.add(new SimpleCommunityInfo(3, "Wavve", "커뮤티니 글 content"));
        ottCommunityList.add(new SimpleCommunityInfo(4, "Watcha", "커뮤티니 글 content"));
        ottCommunityList.add(new SimpleCommunityInfo(5, "Disney+", "커뮤티니 글 content"));
        ottCommunityList.add(new SimpleCommunityInfo(6, "Coupang Play", "커뮤티니 글 content"));

        RecyclerView rvOttCommunity= rootView.findViewById(R.id.rv_home_community);
        LinearLayoutManager commuManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        rvOttCommunity.setLayoutManager(commuManager);
        homeCommunityRecyclerAdapter = new HomeCommunityRecyclerAdapter(ottCommunityList);
        rvOttCommunity.setAdapter(homeCommunityRecyclerAdapter);

        return rootView;
    }
}

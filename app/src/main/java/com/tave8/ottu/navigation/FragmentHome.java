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

import com.tave8.ottu.R;
import com.tave8.ottu.adapter.HomeCommunityRecyclerAdapter;
import com.tave8.ottu.data.SimpleCommunityInfo;

import java.util.ArrayList;

public class FragmentHome extends Fragment {
    private ArrayList<SimpleCommunityInfo> ottCommunityList = null;

    private HomeCommunityRecyclerAdapter homeCommunityRecyclerAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        ottCommunityList = new ArrayList<>();
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

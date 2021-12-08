package com.tave8.ottu;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tave8.ottu.adapter.RecruitRecyclerAdapter;
import com.tave8.ottu.data.RecruitInfo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

import static com.tave8.ottu.MainActivity.myInfo;

public class MyRecruitActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recruit);

        ArrayList<RecruitInfo> myRecruitList = new ArrayList<>();

        Toolbar toolbar = findViewById(R.id.tb_my_recruit_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
            Objects.requireNonNull(actionBar).setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
        View customView = View.inflate(this, R.layout.actionbar_mypage, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(customView, params);
        toolbarListener(toolbar);

        //TODO: 서버로부터 내가 쓴 모집글 받아오기!
        //TODO: 임시 recruitList
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        myRecruitList.add(new RecruitInfo(3L, 6, myInfo.getUserEssentialInfo(), false, 3, 1, LocalDateTime.parse("2021-12-03 07:20:23",formatter)));
        myRecruitList.add(new RecruitInfo(2L, 2, myInfo.getUserEssentialInfo(), true, 4, 4, LocalDateTime.parse("2021-11-23 14:20:23", formatter)));
        myRecruitList.add(new RecruitInfo(1L, 3, myInfo.getUserEssentialInfo(), false, 4, 2, LocalDateTime.parse("2021-11-08 12:03:10", formatter)));

        RecyclerView rvMyRecruitPost = findViewById(R.id.rv_my_recruit_recruitList);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        rvMyRecruitPost.setLayoutManager(manager);
        RecruitRecyclerAdapter recruitRecyclerAdapter = new RecruitRecyclerAdapter(myRecruitList);
        rvMyRecruitPost.setAdapter(recruitRecyclerAdapter);
        DividerItemDecoration devider = new DividerItemDecoration(this, 1);
        devider.setDrawable(Objects.requireNonNull(ResourcesCompat.getDrawable(getResources(), R.drawable.item_divide_bar, null)));
        rvMyRecruitPost.addItemDecoration(devider);
    }

    private void toolbarListener(Toolbar toolbar) {
        AppCompatImageButton ibtBack = toolbar.findViewById(R.id.ibt_ab_mypage_back);
        ibtBack.setOnClickListener(v -> finish());

        TextView tvTitle = toolbar.findViewById(R.id.tv_ab_mypage_title);
        tvTitle.setText("내가 작성한 모집글");
    }
}

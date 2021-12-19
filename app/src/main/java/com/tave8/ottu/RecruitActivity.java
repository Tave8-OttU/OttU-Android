package com.tave8.ottu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tave8.ottu.adapter.RecruitRecyclerAdapter;
import com.tave8.ottu.data.RecruitInfo;
import com.tave8.ottu.data.SingletonPlatform;
import com.tave8.ottu.data.UserEssentialInfo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

public class RecruitActivity extends AppCompatActivity {
    private int platformId = 0;
    private ArrayList<RecruitInfo> recruitList = null;
    
    private RecruitRecyclerAdapter recruitRecyclerAdapter;
    private SwipeRefreshLayout srlRecruitPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruit);

        recruitList = new ArrayList<>();
        platformId = getIntent().getExtras().getInt("platformId");

        Toolbar toolbar = findViewById(R.id.tb_recruit_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
            Objects.requireNonNull(actionBar).setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
        View customView = View.inflate(this, R.layout.actionbar_recruit, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(customView, params);
        toolbarListener(toolbar);

        //TODO: 임시 recruitList
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        recruitList.add(new RecruitInfo(3L, platformId, new UserEssentialInfo(4L, "nick4"), false, 2, 1, LocalDateTime.parse("2021-12-03 07:20:23",formatter)));
        recruitList.add(new RecruitInfo(2L, platformId, new UserEssentialInfo(3L, "nick3"), true, 4, 4, LocalDateTime.parse("2021-11-23 14:20:23", formatter)));
        recruitList.add(new RecruitInfo(1L, platformId, new UserEssentialInfo(1L, "nick1"), false, 4, 2, LocalDateTime.parse("2021-11-08 12:03:10", formatter)));
        //

        srlRecruitPosts = findViewById(R.id.srl_recruit_postlist);
        srlRecruitPosts.setDistanceToTriggerSync(400);
        srlRecruitPosts.setOnRefreshListener(() -> updateRecruitList(true));
        RecyclerView rvRecruitPost = findViewById(R.id.rv_recruit_postlist);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        rvRecruitPost.setLayoutManager(manager);
        recruitRecyclerAdapter = new RecruitRecyclerAdapter(recruitList);
        rvRecruitPost.setAdapter(recruitRecyclerAdapter);
        DividerItemDecoration devider = new DividerItemDecoration(this, 1);
        devider.setDrawable(Objects.requireNonNull(ResourcesCompat.getDrawable(getResources(), R.drawable.item_divide_bar, null)));
        rvRecruitPost.addItemDecoration(devider);
        updateRecruitList(false);

        recruitClickListener();
    }

    private void toolbarListener(Toolbar toolbar) {
        AppCompatImageButton ibtBack = toolbar.findViewById(R.id.ibt_ab_recruit_back);
        ibtBack.setOnClickListener(v -> finish());

        ImageView ivPlatform = toolbar.findViewById(R.id.iv_ab_recruit_platform);
        ivPlatform.setImageResource(SingletonPlatform.getPlatform().getPlatformLogoList().get(platformId));
    }

    private void recruitClickListener() {
        ActivityResultLauncher<Intent> startActivityResultRecruiting = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK)
                        updateRecruitList(false);
                });

        FloatingActionButton fabAddRecruitment = findViewById(R.id.fab_recruit_add);
        fabAddRecruitment.setOnClickListener(v -> {
            Intent recruitingIntent = new Intent(this, RecruitingActivity.class);
            Bundle bundle = new Bundle();
                bundle.putInt("platformId", platformId);
            recruitingIntent.putExtras(bundle);
            startActivityResultRecruiting.launch(recruitingIntent);
        });
    }
    
    private void updateRecruitList(boolean isSwipe) {
        //TODO: 서버로부터 모집글들 받아오기!
    }
}

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
import com.tave8.ottu.adapter.CommunityPostRecyclerAdapter;
import com.tave8.ottu.data.CommunityPostInfo;
import com.tave8.ottu.data.SingletonPlatform;
import com.tave8.ottu.data.UserEssentialInfo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

public class CommunityActivity extends AppCompatActivity {
    private int platformIdx = 0;
    private ArrayList<CommunityPostInfo> communityPostList = null;

    private CommunityPostRecyclerAdapter postRecyclerAdapter;
    private SwipeRefreshLayout srlCommunityPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        communityPostList = new ArrayList<>();
        platformIdx = getIntent().getExtras().getInt("platformIdx");

        Toolbar toolbar = findViewById(R.id.tb_community_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
            Objects.requireNonNull(actionBar).setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
        View customView = View.inflate(this, R.layout.actionbar_community, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(customView, params);
        toolbarListener(toolbar);

        //TODO: 임시 communityPostList
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        communityPostList.add(new CommunityPostInfo(3L, platformIdx, new UserEssentialInfo(4L, "닉네임4"), "넷플릭스에 드라마 많이 있나요?", LocalDateTime.parse("2021-12-03 07:20:23",formatter), 1));
        communityPostList.add(new CommunityPostInfo(2L, platformIdx, new UserEssentialInfo(3L, "닉네임3"), "넷플릭스가 티빙보다 좋나요?", LocalDateTime.parse("2021-11-23 14:20:23", formatter), 5));
        communityPostList.add(new CommunityPostInfo(1L, platformIdx, new UserEssentialInfo(1L, "닉네임1"), "넷플릭스에 해리포터 있나요?", LocalDateTime.parse("2021-11-08 12:03:10", formatter), 3));
        //

        ActivityResultLauncher<Intent> startActivityResultPost = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK)
                        updateCommunityPostList(false);
                });

        srlCommunityPosts = findViewById(R.id.srl_community_postlist);
        srlCommunityPosts.setDistanceToTriggerSync(400);
        srlCommunityPosts.setOnRefreshListener(() -> updateCommunityPostList(true));
        RecyclerView rvCommunityPost = findViewById(R.id.rv_community_postlist);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        rvCommunityPost.setLayoutManager(manager);
        postRecyclerAdapter = new CommunityPostRecyclerAdapter(communityPostList, startActivityResultPost);
        rvCommunityPost.setAdapter(postRecyclerAdapter);
        DividerItemDecoration devider = new DividerItemDecoration(this, 1);
        devider.setDrawable(Objects.requireNonNull(ResourcesCompat.getDrawable(getResources(), R.drawable.item_divide_bar, null)));
        rvCommunityPost.addItemDecoration(devider);
        updateCommunityPostList(false);

        communityClickListener();
    }

    private void toolbarListener(Toolbar toolbar) {
        AppCompatImageButton ibtBack = toolbar.findViewById(R.id.ibt_ab_community_back);
        ibtBack.setOnClickListener(v -> finish());

        ImageView ivPlatform = toolbar.findViewById(R.id.iv_ab_community_platform);
        ivPlatform.setImageResource(SingletonPlatform.getPlatform().getPlatformLogoList().get(platformIdx));
    }
    
    private void communityClickListener() {
        ActivityResultLauncher<Intent> startActivityResultPosting = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK)
                        updateCommunityPostList(false);
                });

        FloatingActionButton fabAddCommunityPost = findViewById(R.id.fab_community_add);
        fabAddCommunityPost.setOnClickListener(v -> {
            Intent postingIntent = new Intent(this, PostingActivity.class);
            Bundle bundle = new Bundle();
                bundle.putInt("platformIdx", platformIdx);
            postingIntent.putExtras(bundle);
            startActivityResultPosting.launch(postingIntent);
        });
    }

    private void updateCommunityPostList(boolean isSwipe) {
        //TODO: 서버로부터 커뮤니티글들 받아오기!
    }
}

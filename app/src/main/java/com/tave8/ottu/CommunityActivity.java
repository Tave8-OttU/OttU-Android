package com.tave8.ottu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommunityActivity extends AppCompatActivity {
    private int platformIdx = 0;
    private ArrayList<CommunityPostInfo> communityPostList = null;

    private CommunityPostRecyclerAdapter postRecyclerAdapter;
    private SwipeRefreshLayout srlCommunityPosts;
    private TextView tvPostNo;

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

        tvPostNo = findViewById(R.id.tv_community_no);

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
        tvPostNo.setVisibility(View.GONE);
        OttURetrofitClient.getApiService().getCommunityPostList(PreferenceManager.getString(this, "jwt"), platformIdx).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.code() == 200) {
                    communityPostList.clear();
                    try {
                        JSONObject result = new JSONObject(Objects.requireNonNull(response.body()));
                        JSONArray jsonPostList = result.getJSONArray("postlist");
                        for (int i=0; i<jsonPostList.length(); i++) {
                            JSONObject post = jsonPostList.getJSONObject(i);
                            Long postIdx = post.getLong("postIdx");
                            int platformIdx = post.getJSONObject("platform").getInt("platformIdx");

                            JSONObject writer = post.getJSONObject("writer");
                            UserEssentialInfo writerInfo = new UserEssentialInfo(writer.getLong("userIdx"), writer.getString("nickname"));

                            String content = post.getString("content");

                            String createdDate = post.getString("createdDate");
                            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

                            int commmentNum = (int) post.getLong("commentNum");

                            CommunityPostInfo postInfo = new CommunityPostInfo(postIdx, platformIdx, writerInfo, content, LocalDateTime.parse(createdDate, dateTimeFormatter), commmentNum);
                            communityPostList.add(postInfo);
                        }
                        postRecyclerAdapter.notifyDataSetChanged();
                    } catch (JSONException e) { e.printStackTrace(); }
                }
                else if (response.code() == 401) {
                    Toast.makeText(CommunityActivity.this, "로그인 기한이 만료되어\n 로그인 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
                    PreferenceManager.removeKey(CommunityActivity.this, "jwt");
                    Intent reLogin = new Intent(CommunityActivity.this, LoginActivity.class);
                    reLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(reLogin);
                    finish();
                }
                else
                    Toast.makeText(CommunityActivity.this, "커뮤니티 글 로드에 문제가 생겼습니다. 새로 고침을 해주세요.", Toast.LENGTH_SHORT).show();

                if (communityPostList.size() == 0)
                    tvPostNo.setVisibility(View.VISIBLE);

                if (isSwipe)
                    srlCommunityPosts.setRefreshing(false);
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                if (isSwipe)
                    srlCommunityPosts.setRefreshing(false);

                Toast.makeText(CommunityActivity.this, "서버와 연결되지 않았습니다. 확인해 주세요:)", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

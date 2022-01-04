package com.tave8.ottu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
import com.tave8.ottu.adapter.RecruitRecyclerAdapter;
import com.tave8.ottu.data.RecruitInfo;
import com.tave8.ottu.data.SingletonPlatform;
import com.tave8.ottu.data.UserEssentialInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecruitActivity extends AppCompatActivity {
    private int platformIdx = 0;
    private ArrayList<RecruitInfo> recruitList = null;
    
    private RecruitRecyclerAdapter recruitRecyclerAdapter;
    private SwipeRefreshLayout srlRecruitPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruit);

        recruitList = new ArrayList<>();
        platformIdx = getIntent().getExtras().getInt("platformIdx");

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
        ivPlatform.setImageResource(SingletonPlatform.getPlatform().getPlatformLogoList().get(platformIdx));
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
                bundle.putInt("platformIdx", platformIdx);
            recruitingIntent.putExtras(bundle);
            startActivityResultRecruiting.launch(recruitingIntent);
        });
    }
    
    public void updateRecruitList(boolean isSwipe) {
        OttURetrofitClient.getApiService().getRecruitList(PreferenceManager.getString(this, "jwt"), platformIdx).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.code() == 200) {
                    recruitList.clear();
                    try {
                        JSONObject result = new JSONObject(Objects.requireNonNull(response.body()));
                        JSONArray jsonRecruitList = result.getJSONArray("recruitlist");
                        for (int i=0; i<jsonRecruitList.length(); i++) {
                            JSONObject recruit = jsonRecruitList.getJSONObject(i);
                            Long recruitIdx = recruit.getLong("recruitIdx");
                            int platformIdx = recruit.getJSONObject("platform").getInt("platformIdx");

                            JSONObject writer = recruit.getJSONObject("writer");
                            UserEssentialInfo writerInfo = new UserEssentialInfo(writer.getLong("userIdx"), writer.getString("nickname"));

                            int headcount = recruit.getInt("headcount");
                            int choiceNum = (int) recruit.getLong("choiceNum");
                            boolean isCompleted = recruit.getBoolean("isCompleted");
                            String createdDate = recruit.getString("createdDate");

                            RecruitInfo recruitInfo = new RecruitInfo(recruitIdx, platformIdx, writerInfo, isCompleted, headcount, choiceNum, createdDate);
                            recruitList.add(recruitInfo);
                        }
                        recruitRecyclerAdapter.notifyDataSetChanged();
                    } catch (JSONException e) { e.printStackTrace(); }
                }
                else if (response.code() == 401) {
                    Toast.makeText(RecruitActivity.this, "로그인 기한이 만료되어\n 로그인 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
                    PreferenceManager.removeKey(RecruitActivity.this, "jwt");
                    Intent reLogin = new Intent(RecruitActivity.this, LoginActivity.class);
                    reLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(reLogin);
                    finish();
                }
                else
                    Toast.makeText(RecruitActivity.this, "모집글 로드에 문제가 생겼습니다. 새로 고침을 해주세요.", Toast.LENGTH_SHORT).show();

                if (isSwipe)
                    srlRecruitPosts.setRefreshing(false);
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                if (isSwipe)
                    srlRecruitPosts.setRefreshing(false);

                Toast.makeText(RecruitActivity.this, "서버와 연결되지 않았습니다. 확인해 주세요:)", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

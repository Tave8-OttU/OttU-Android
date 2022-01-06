package com.tave8.ottu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

import com.tave8.ottu.adapter.MyCommunityPostRecyclerAdapter;
import com.tave8.ottu.data.CommunityPostInfo;
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

import static com.tave8.ottu.MainActivity.myInfo;

public class MyCommunityActivity extends AppCompatActivity {
    private ArrayList<CommunityPostInfo> myCommunityPostList = null;
    private MyCommunityPostRecyclerAdapter myCommunityRecyclerAdapter;
    private TextView tvMyPostNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_community);

        myCommunityPostList = new ArrayList<>();

        Toolbar toolbar = findViewById(R.id.tb_my_community_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        View customView = View.inflate(this, R.layout.actionbar_mypage, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(customView, params);
        toolbarListener(toolbar);

        tvMyPostNo = findViewById(R.id.tv_my_community_no);

        ActivityResultLauncher<Intent> startActivityResultPost = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK)
                        updateMyCommunityPostList();
                });

        RecyclerView rvMyCommunityPost = findViewById(R.id.rv_my_community_postlist);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        rvMyCommunityPost.setLayoutManager(manager);
        myCommunityRecyclerAdapter = new MyCommunityPostRecyclerAdapter(myCommunityPostList, startActivityResultPost);
        rvMyCommunityPost.setAdapter(myCommunityRecyclerAdapter);
        DividerItemDecoration devider = new DividerItemDecoration(this, 1);
        devider.setDrawable(Objects.requireNonNull(ResourcesCompat.getDrawable(getResources(), R.drawable.item_divide_bar, null)));
        rvMyCommunityPost.addItemDecoration(devider);

        updateMyCommunityPostList();
    }

    private void toolbarListener(Toolbar toolbar) {
        AppCompatImageButton ibtBack = toolbar.findViewById(R.id.ibt_ab_mypage_back);
        ibtBack.setOnClickListener(v -> finish());

        TextView tvTitle = toolbar.findViewById(R.id.tv_ab_mypage_title);
        tvTitle.setText("내가 작성한 커뮤니티 글");
    }

    private void updateMyCommunityPostList() {
        tvMyPostNo.setVisibility(View.GONE);
        OttURetrofitClient.getApiService().getMyPostList(PreferenceManager.getString(this, "jwt"), myInfo.getUserIdx()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.code() == 200) {
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
                            myCommunityPostList.add(postInfo);
                        }
                        myCommunityRecyclerAdapter.notifyDataSetChanged();
                    } catch (JSONException e) { e.printStackTrace(); }
                }
                else if (response.code() == 401) {
                    Toast.makeText(MyCommunityActivity.this, "로그인 기한이 만료되어\n 로그인 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
                    PreferenceManager.removeKey(MyCommunityActivity.this, "jwt");
                    Intent reLogin = new Intent(MyCommunityActivity.this, LoginActivity.class);
                    reLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(reLogin);
                    finish();
                }
                else
                    Toast.makeText(MyCommunityActivity.this, "내가 쓴 커뮤니티 글 로드에 문제가 생겼습니다. 새로 고침을 해주세요.", Toast.LENGTH_SHORT).show();

                if (myCommunityPostList.size() == 0)
                    tvMyPostNo.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Toast.makeText(MyCommunityActivity.this, "서버와 연결되지 않았습니다. 확인해 주세요:)", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

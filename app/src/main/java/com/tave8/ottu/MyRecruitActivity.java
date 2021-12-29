package com.tave8.ottu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tave8.ottu.adapter.MyRecruitRecyclerAdapter;
import com.tave8.ottu.data.RecruitInfo;
import com.tave8.ottu.data.UserEssentialInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tave8.ottu.MainActivity.myInfo;

public class MyRecruitActivity extends AppCompatActivity {
    private ArrayList<RecruitInfo> myRecruitList;
    private MyRecruitRecyclerAdapter recruitRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recruit);

        myRecruitList = new ArrayList<>();

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

        RecyclerView rvMyRecruitPost = findViewById(R.id.rv_my_recruit_recruitList);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        rvMyRecruitPost.setLayoutManager(manager);
        recruitRecyclerAdapter = new MyRecruitRecyclerAdapter(myRecruitList);
        rvMyRecruitPost.setAdapter(recruitRecyclerAdapter);
        DividerItemDecoration devider = new DividerItemDecoration(this, 1);
        devider.setDrawable(Objects.requireNonNull(ResourcesCompat.getDrawable(getResources(), R.drawable.item_divide_bar, null)));
        rvMyRecruitPost.addItemDecoration(devider);

        updateMyRecruitList();
    }

    private void toolbarListener(Toolbar toolbar) {
        AppCompatImageButton ibtBack = toolbar.findViewById(R.id.ibt_ab_mypage_back);
        ibtBack.setOnClickListener(v -> finish());

        TextView tvTitle = toolbar.findViewById(R.id.tv_ab_mypage_title);
        tvTitle.setText("내가 작성한 모집글");
    }

    public void updateMyRecruitList() {
        OttURetrofitClient.getApiService().getMyRecruitLists(PreferenceManager.getString(this, "jwt"), myInfo.getUserIdx()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.code() == 200) {
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
                            myRecruitList.add(recruitInfo);
                        }
                        recruitRecyclerAdapter.notifyDataSetChanged();
                    } catch (JSONException e) { e.printStackTrace(); }
                }
                else if (response.code() == 401) {
                    Toast.makeText(MyRecruitActivity.this, "로그인 기한이 만료되어\n 로그인 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
                    PreferenceManager.removeKey(MyRecruitActivity.this, "jwt");
                    Intent reLogin = new Intent(MyRecruitActivity.this, LoginActivity.class);
                    reLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(reLogin);
                    finish();
                }
                else
                    Toast.makeText(MyRecruitActivity.this, "내가 쓴 모집글 로드에 문제가 생겼습니다. 새로 고침을 해주세요.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Toast.makeText(MyRecruitActivity.this, "서버와 연결되지 않았습니다. 확인해 주세요:)", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

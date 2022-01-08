package com.tave8.ottu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.PopupMenu;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tave8.ottu.MainActivity.myInfo;

public class RecruitActivity extends AppCompatActivity {
    private int platformIdx = 0;
    private int headcount = 0;  //0일 때는 전체
    private ArrayList<RecruitInfo> recruitList = null;
    
    private RecruitRecyclerAdapter recruitRecyclerAdapter;
    private SwipeRefreshLayout srlRecruitPosts;
    private TextView tvRecruitNo;

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

        tvRecruitNo = findViewById(R.id.tv_recruit_no);

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

        AppCompatImageButton ivFilter = toolbar.findViewById(R.id.iv_ab_recruit_filter);
        ivFilter.setOnClickListener(v -> {
            View filterDialogView = View.inflate(this, R.layout.dialog_recruit_filter, null);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(filterDialogView);
            AlertDialog alertDialog = builder.create();
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.show();

            WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
            Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            Point size = new Point();
            display.getRealSize(size);
            int width = size.x;
            params.width = (int) (width*0.89);
            alertDialog.getWindow().setAttributes(params);

            TextView tvHeadcount = filterDialogView.findViewById(R.id.tv_dialog_filter_headcount);
            if (headcount == 0)
                tvHeadcount.setText("전체");
            else
                tvHeadcount.setText(String.valueOf(headcount));

            LinearLayout llHeadcount = filterDialogView.findViewById(R.id.ll_dialog_filter_headcount);
            llHeadcount.setOnClickListener(view -> {
                Context wrapper = new ContextThemeWrapper(this, R.style.PopUpMenuTheme);
                PopupMenu filterMenu = new PopupMenu(wrapper, view);
                getMenuInflater().inflate(R.menu.menu_filter_headcount, filterMenu.getMenu());

                //플랫폼에 따른 선택 인원 수가 다름(기본 전체, 1, 2, 4)
                if (platformIdx == 4)                                   //왓챠는 2인이 없음
                    filterMenu.getMenu().getItem(2).setVisible(false);
                else if (platformIdx == 5 || platformIdx == 6) {        //디즈니 플러스와 쿠팡 플레이는 1, 2인이 없음(전체=4인)
                    filterMenu.getMenu().getItem(1).setVisible(false);
                    filterMenu.getMenu().getItem(2).setVisible(false);
                    filterMenu.getMenu().getItem(3).setVisible(false);
                }

                filterMenu.setOnMenuItemClickListener(menuItem -> {
                    if (menuItem.getItemId() == R.id.menu_headcount_all) {
                        headcount = 0;
                        tvHeadcount.setText("전체");
                    }
                    else { //인원 수 1, 2, 4
                        headcount = Integer.valueOf(menuItem.getTitle().toString());
                        tvHeadcount.setText(menuItem.getTitle());
                    }
                    return false;
                });
                filterMenu.show();
            });

            AppCompatButton btCancel = filterDialogView.findViewById(R.id.bt_dialog_filter_cancel);
            btCancel.setOnClickListener(view -> alertDialog.dismiss());

            AppCompatButton btApplyFilter = filterDialogView.findViewById(R.id.bt_dialog_filter_apply);
            btApplyFilter.setOnClickListener(view -> {
                alertDialog.dismiss();
                updateRecruitList(false);
            });
        });
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
        tvRecruitNo.setVisibility(View.GONE);

        Callback<String> recruitListCallback = new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.code() == 200) {
                    recruitList.clear();
                    try {
                        JSONObject result = new JSONObject(Objects.requireNonNull(response.body()));
                        JSONArray jsonRecruitList = result.getJSONArray("recruitlist");
                        for (int i = 0; i < jsonRecruitList.length(); i++) {
                            JSONObject recruit = jsonRecruitList.getJSONObject(i);
                            Long recruitIdx = recruit.getLong("recruitIdx");
                            int platformIdx = recruit.getJSONObject("platform").getInt("platformIdx");

                            JSONObject writer = recruit.getJSONObject("writer");
                            UserEssentialInfo writerInfo = new UserEssentialInfo(writer.getLong("userIdx"), writer.getString("nickname"));

                            int headcount = recruit.getInt("headcount");
                            int choiceNum = (int) recruit.getLong("choiceNum");
                            boolean isCompleted = recruit.getBoolean("isCompleted");
                            boolean isApplying = recruit.getBoolean("isApplying");
                            String createdDate = recruit.getString("createdDate");

                            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                            RecruitInfo recruitInfo = new RecruitInfo(recruitIdx, platformIdx, writerInfo, isCompleted, isApplying, headcount, choiceNum, LocalDateTime.parse(createdDate, dateTimeFormatter));
                            recruitList.add(recruitInfo);
                        }
                        recruitRecyclerAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == 401) {
                    Toast.makeText(RecruitActivity.this, "로그인 기한이 만료되어\n 로그인 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
                    PreferenceManager.removeKey(RecruitActivity.this, "jwt");
                    Intent reLogin = new Intent(RecruitActivity.this, LoginActivity.class);
                    reLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(reLogin);
                    finish();
                } else
                    Toast.makeText(RecruitActivity.this, "모집글 로드에 문제가 생겼습니다. 새로 고침을 해주세요.", Toast.LENGTH_SHORT).show();

                if (recruitList.size() == 0)
                    tvRecruitNo.setVisibility(View.VISIBLE);

                if (isSwipe)
                    srlRecruitPosts.setRefreshing(false);
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                if (isSwipe)
                    srlRecruitPosts.setRefreshing(false);

                Toast.makeText(RecruitActivity.this, "서버와 연결되지 않았습니다. 확인해 주세요:)", Toast.LENGTH_SHORT).show();
            }
        };
        
        if (headcount == 0)
            OttURetrofitClient.getApiService().getRecruitList(PreferenceManager.getString(this, "jwt"), platformIdx, myInfo.getUserIdx()).enqueue(recruitListCallback);
        else       //filter 시에 사용
            OttURetrofitClient.getApiService().getHeadcountRecruitList(PreferenceManager.getString(this, "jwt"), platformIdx, myInfo.getUserIdx(), headcount).enqueue(recruitListCallback);
    }
}

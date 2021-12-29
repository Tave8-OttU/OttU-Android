package com.tave8.ottu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.tave8.ottu.adapter.RatePlanRecyclerAdapter;
import com.tave8.ottu.data.RatePlanInfo;
import com.tave8.ottu.data.SingletonPlatform;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tave8.ottu.MainActivity.myInfo;

public class RecruitingActivity extends AppCompatActivity {
    private boolean isSubmitted = false;
    private int platformIdx = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruiting);

        platformIdx = getIntent().getExtras().getInt("platformIdx");
        ArrayList<RatePlanInfo> ratePlanInfoList = SingletonPlatform.getPlatform().getPlatformInfoList().get(platformIdx);

        Toolbar toolbar = findViewById(R.id.tb_recruiting_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
            Objects.requireNonNull(actionBar).setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
        View customView = View.inflate(this, R.layout.actionbar_recruiting, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(customView, params);
        toolbarListener(toolbar);

        RecyclerView rvRatePlan = findViewById(R.id.rv_recruiting_rateplan);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        rvRatePlan.setLayoutManager(manager);
        RatePlanRecyclerAdapter ratePlanAdapter = new RatePlanRecyclerAdapter(ratePlanInfoList);
        rvRatePlan.setAdapter(ratePlanAdapter);

        TextView tvNotification1 = findViewById(R.id.tv_recruiting_notification1);
        TextView tvNotification2 = findViewById(R.id.tv_recruiting_notification2);
        tvNotification1.setText(getString(R.string.recruiting_notice1).replace(" ", "\u00A0"));
        tvNotification2.setText(getString(R.string.recruiting_notice2).replace(" ", "\u00A0"));

        recruitingClickListener(ratePlanAdapter);
    }

    @Override
    public void finish() {
        if (isSubmitted) {
            Intent submittedIntent = new Intent();
            setResult(RESULT_OK, submittedIntent);
        }
        else {
            Intent returnIntent = new Intent();
            setResult(RESULT_CANCELED, returnIntent);
        }

        super.finish();
    }

    private void toolbarListener(Toolbar toolbar) {
        AppCompatImageButton ibtBack = toolbar.findViewById(R.id.ibt_ab_recruiting_back);
        ibtBack.setOnClickListener(v -> finish());

        ImageView ivPlatform = toolbar.findViewById(R.id.iv_ab_recruiting_platform);
        ivPlatform.setImageResource(SingletonPlatform.getPlatform().getPlatformLogoList().get(platformIdx));
    }

    private void recruitingClickListener(RatePlanRecyclerAdapter ratePlanAdapter) {
        NestedScrollView nsvRecruiting = findViewById(R.id.nsv_recruiting);

        CheckBox cbAgree = findViewById(R.id.cb_recruiting_agree);
        TextView tvAgree = findViewById(R.id.tv_recruiting_agree);
        tvAgree.setOnClickListener(v -> cbAgree.toggle());

        Button btSubmit = findViewById(R.id.bt_recruiting_submit);
        btSubmit.setOnClickListener(v -> {
            if (ratePlanAdapter.getSelectedRatePlanPosition() == -1) {
                nsvRecruiting.post(() -> nsvRecruiting.fullScroll(View.FOCUS_UP));
                Toast.makeText(this, "요금제를 선택해 주세요.", Toast.LENGTH_SHORT).show();
            } else if (!cbAgree.isChecked()) {
                nsvRecruiting.post(() -> nsvRecruiting.fullScroll(View.FOCUS_DOWN));
                Toast.makeText(this, "항목 확인을 체크해 주세요.", Toast.LENGTH_SHORT).show();
            } else {
                JsonObject requestData = new JsonObject();
                requestData.addProperty("userIdx", myInfo.getUserIdx());
                requestData.addProperty("userIdx", 7);
                requestData.addProperty("headcount", ratePlanAdapter.getSelectedRatePlanHeadCount());
                OttURetrofitClient.getApiService().postRecruitUpload(PreferenceManager.getString(this, "jwt"), requestData).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        if (response.code() == 201) {
                            isSubmitted = true;
                            finish();
                        }
                        else if (response.code() == 401) {
                            Toast.makeText(RecruitingActivity.this, "로그인 기한이 만료되어\n 로그인 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
                            PreferenceManager.removeKey(RecruitingActivity.this, "jwt");
                            Intent reLogin = new Intent(RecruitingActivity.this, LoginActivity.class);
                            reLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(reLogin);
                            finish();
                        }
                        else
                            Toast.makeText(RecruitingActivity.this, "모집글 제출에 문제가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        Toast.makeText(RecruitingActivity.this, "서버와 연결되지 않았습니다. 확인해 주세요:)", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}

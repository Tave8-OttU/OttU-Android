package com.tave8.ottu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tave8.ottu.adapter.RatePlanRecyclerAdapter;
import com.tave8.ottu.data.RatePlanInfo;

import java.util.ArrayList;
import java.util.Objects;

public class RecruitingActivity extends AppCompatActivity {
    private boolean isSubmitted = false;
    private int platformId = 0;
    private ArrayList<RatePlanInfo> ratePlanInfos = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruiting);

        platformId = getIntent().getExtras().getInt("platformId");

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

        ratePlanInfos = new ArrayList<>();
        setPlatformRatePlanList();

        RecyclerView rvRatePlan = findViewById(R.id.rv_recruiting_charge);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        rvRatePlan.setLayoutManager(manager);
        RatePlanRecyclerAdapter ratePlanAdapter = new RatePlanRecyclerAdapter(ratePlanInfos);
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
        switch (platformId) {
            case 1: {
                ivPlatform.setImageResource(R.drawable.icon_ott_netflix);
                break;
            } case 2: {
                ivPlatform.setImageResource(R.drawable.icon_ott_tving);
                break;
            } case 3: {
                ivPlatform.setImageResource(R.drawable.icon_ott_wavve);
                break;
            } case 4: {
                ivPlatform.setImageResource(R.drawable.icon_ott_watcha);
                break;
            } case 5: {
                ivPlatform.setImageResource(R.drawable.icon_ott_disney);
                break;
            } case 6: {
                ivPlatform.setImageResource(R.drawable.icon_ott_coupang_play);
                break;
            }
        }
    }

    private void setPlatformRatePlanList() {
        switch (platformId) {
            case 1: {   //넷플릭스
                ratePlanInfos.add(new RatePlanInfo("베이식", 1, 9500));
                ratePlanInfos.add(new RatePlanInfo("스탠다드", 2, 13500));
                ratePlanInfos.add(new RatePlanInfo("프리미엄", 4, 17000));
                break;
            } case 2: {   //티빙
                ratePlanInfos.add(new RatePlanInfo("베이직", 1, 7900));
                ratePlanInfos.add(new RatePlanInfo("스탠다드", 2, 10900));
                ratePlanInfos.add(new RatePlanInfo("프리미엄", 4, 13900));
                break;
            } case 3: {   //웨이브
                ratePlanInfos.add(new RatePlanInfo("Basic", 1, 7900));
                ratePlanInfos.add(new RatePlanInfo("Standard", 2, 10900));
                ratePlanInfos.add(new RatePlanInfo("Premium", 4, 13900));
                break;
            } case 4: {   //왓챠
                ratePlanInfos.add(new RatePlanInfo("베이직", 1, 7900));
                ratePlanInfos.add(new RatePlanInfo("프리미엄", 4, 12900));
                break;
            } case 5: {   //디즈니 플러스
                ratePlanInfos.add(new RatePlanInfo("Disney+", 4, 9900));
                break;
            } case 6: {   //쿠팡 플레이
                ratePlanInfos.add(new RatePlanInfo("쿠팡 와우 멤버십", 2, 2900));
                break;
            }
        }
    }

    private void recruitingClickListener(RatePlanRecyclerAdapter ratePlanAdapter) {
        NestedScrollView nsvRecruiting = findViewById(R.id.nsv_recruiting);
        CheckBox cbAgree = findViewById(R.id.cb_recruiting_agree);
        Button btSubmit = findViewById(R.id.bt_recruiting_submit);
        btSubmit.setOnClickListener(v -> {
            if (ratePlanAdapter.getSelectedRatePlanPosition() == -1) {
                nsvRecruiting.post(() -> nsvRecruiting.fullScroll(View.FOCUS_UP));
                Toast.makeText(this, "요금제를 선택해 주세요.", Toast.LENGTH_SHORT).show();
            } else if (!cbAgree.isChecked()) {
                nsvRecruiting.post(() -> nsvRecruiting.fullScroll(View.FOCUS_DOWN));
                Toast.makeText(this, "항목 확인을 체크해 주세요.", Toast.LENGTH_SHORT).show();
            } else {
                //TODO: 서버로 입력사항 제출함
                isSubmitted = true;
                finish();
            }
        });
    }
}

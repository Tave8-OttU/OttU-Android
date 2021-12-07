package com.tave8.ottu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tave8.ottu.adapter.RatePlanRecyclerAdapter;
import com.tave8.ottu.data.RatePlanInfo;
import com.tave8.ottu.data.SingletonPlatform;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class AddMyOTTActivity extends AppCompatActivity {
    private boolean isAdded = false;
    private int platformId = 0;
    private View ibtSelectedPlatform;
    private ArrayList<RatePlanInfo> ratePlanInfoList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_my_ott);
        ibtSelectedPlatform = new View(this);

        Toolbar toolbar = findViewById(R.id.tb_addmyott_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        View customView = View.inflate(this, R.layout.actionbar_mypage, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(customView, params);
        toolbarListener(toolbar);

        ratePlanInfoList = new ArrayList<>();
        RecyclerView rvRatePlan = findViewById(R.id.rv_addmyott_rateplan);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        rvRatePlan.setLayoutManager(manager);
        RatePlanRecyclerAdapter ratePlanAdapter = new RatePlanRecyclerAdapter(ratePlanInfoList);
        rvRatePlan.setAdapter(ratePlanAdapter);

        addOttClickListener(ratePlanAdapter);
    }

    @Override
    public void finish() {
        if (isAdded) {
            Intent addedOttIntent = new Intent();
            setResult(RESULT_OK, addedOttIntent);
        }
        else {
            Intent returnIntent = new Intent();
            setResult(RESULT_CANCELED, returnIntent);
        }

        super.finish();
    }

    private void toolbarListener(Toolbar toolbar) {
        AppCompatImageButton ibtBack = toolbar.findViewById(R.id.ibt_ab_mypage_back);
        ibtBack.setOnClickListener(v -> finish());

        TextView tvTitle = toolbar.findViewById(R.id.tv_ab_mypage_title);
        tvTitle.setText("OTT 서비스 추가");
    }

    private void addOttClickListener(RatePlanRecyclerAdapter ratePlanAdapter) {
        NestedScrollView nsvAddMyOtt = findViewById(R.id.nsv_add_my_ott);

        View.OnClickListener platformClickListener = ibtPlatformView -> {
            if (ibtSelectedPlatform != ibtPlatformView) {
                switch (ibtPlatformView.getId()) {
                    case R.id.ibt_addmyott_netflix: {
                        platformId = 1;
                        break;
                    } case R.id.ibt_addmyott_tving: {
                        platformId = 2;
                        break;
                    } case R.id.ibt_addmyott_wavve: {
                        platformId = 3;
                        break;
                    } case R.id.ibt_addmyott_watcha: {
                        platformId = 4;
                        break;
                    } case R.id.ibt_addmyott_disney: {
                        platformId = 5;
                        break;
                    } case R.id.ibt_addmyott_coupangplay: {
                        platformId = 6;
                        break;
                    }
                }

                ibtSelectedPlatform.setBackgroundResource(R.drawable.bg_r5white);
                ibtPlatformView.setBackgroundResource(R.drawable.bg_r5white_s4blue);
                ibtSelectedPlatform = ibtPlatformView;

                ratePlanInfoList.clear();
                ratePlanInfoList.addAll(SingletonPlatform.getPlatform().getPlatformInfoList().get(platformId));
                ratePlanAdapter.notifyDataSetChanged();
            }
        };

        AppCompatImageButton ibtNetflix = findViewById(R.id.ibt_addmyott_netflix);
        ibtNetflix.setOnClickListener(platformClickListener);
        AppCompatImageButton ibtTving = findViewById(R.id.ibt_addmyott_tving);
        ibtTving.setOnClickListener(platformClickListener);
        AppCompatImageButton ibtWavve = findViewById(R.id.ibt_addmyott_wavve);
        ibtWavve.setOnClickListener(platformClickListener);
        AppCompatImageButton ibtWatcha = findViewById(R.id.ibt_addmyott_watcha);
        ibtWatcha.setOnClickListener(platformClickListener);
        AppCompatImageButton ibtDisneyPlus = findViewById(R.id.ibt_addmyott_disney);
        ibtDisneyPlus.setOnClickListener(platformClickListener);
        AppCompatImageButton ibtCoupangPlay = findViewById(R.id.ibt_addmyott_coupangplay);
        ibtCoupangPlay.setOnClickListener(platformClickListener);

        EditText etPaymentDay = findViewById(R.id.et_addmyott_payment_day);
        etPaymentDay.setOnClickListener(v -> {
            final NumberPicker dayPick = new NumberPicker(getApplicationContext());
            dayPick.setMinValue(1);
            dayPick.setMaxValue(31);
            if (etPaymentDay.getText().toString().equals(""))
                dayPick.setValue(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
            else
                dayPick.setValue(Integer.parseInt(etPaymentDay.getText().toString()));

            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("출생년도 입력");
            dialog.setView(dayPick);
            dialog.setPositiveButton("확인", (dialogInterface, which) -> etPaymentDay.setText(String.valueOf(dayPick.getValue())));
            dialog.setNegativeButton("취소", (dialogInterface, which) -> dialogInterface.dismiss());
            dialog.show();
        });

        Button btSubmit = findViewById(R.id.bt_addmyott_submit);
        btSubmit.setOnClickListener(v -> {
            if (platformId == 0) {
                nsvAddMyOtt.post(() -> nsvAddMyOtt.fullScroll(View.FOCUS_UP));
                Toast.makeText(this, "OTT 서비스 플랫폼을 선택해 주세요.", Toast.LENGTH_SHORT).show();
            }
            else if (ratePlanAdapter.getSelectedRatePlanPosition() == -1)
                Toast.makeText(this, "요금제를 선택해 주세요.", Toast.LENGTH_SHORT).show();
            else if (etPaymentDay.getText().length() == 0)
                Toast.makeText(this, "결제 일자를 선택해 주세요.", Toast.LENGTH_SHORT).show();
            else {
                //TODO: 서버로 입력사항 제출함
                isAdded = true;
                finish();
            }
        });
    }
}

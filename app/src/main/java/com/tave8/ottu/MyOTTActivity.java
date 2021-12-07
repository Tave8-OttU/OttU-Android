package com.tave8.ottu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tave8.ottu.adapter.OttPaymentRecyclerAdapter;
import com.tave8.ottu.data.PaymentInfo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

public class MyOTTActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ott);

        //TODO: 서버로부터 내가 쓴 글 받아오기!
        ArrayList<PaymentInfo> myOttPaymentList = new ArrayList<>();

        Toolbar toolbar = findViewById(R.id.tb_my_ott_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
            Objects.requireNonNull(actionBar).setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
        View customView = View.inflate(this, R.layout.actionbar_mypage, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(customView, params);
        toolbarListener(toolbar);

        //TODO: 임시 myOttPaymentList
        myOttPaymentList.add(new PaymentInfo(1L, 3, 4, LocalDate.parse("2021-12-15", DateTimeFormatter.ISO_DATE)));
        myOttPaymentList.add(new PaymentInfo(2L, 2, 2, LocalDate.parse("2021-12-20", DateTimeFormatter.ISO_DATE)));
        myOttPaymentList.add(new PaymentInfo(3L, 6, 2, LocalDate.parse("2021-12-30", DateTimeFormatter.ISO_DATE)));

        RecyclerView rvMyOttPost = findViewById(R.id.rv_my_ott_platformList);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        rvMyOttPost.setLayoutManager(manager);
        OttPaymentRecyclerAdapter ottPaymentRecyclerAdapter = new OttPaymentRecyclerAdapter(myOttPaymentList);
        rvMyOttPost.setAdapter(ottPaymentRecyclerAdapter);
        DividerItemDecoration devider = new DividerItemDecoration(this, 1);
        devider.setDrawable(Objects.requireNonNull(ResourcesCompat.getDrawable(getResources(), R.drawable.item_divide_bar, null)));
        rvMyOttPost.addItemDecoration(devider);

        myOTTClickListener();
        updateMyOTTList();
    }

    private void toolbarListener(Toolbar toolbar) {
        AppCompatImageButton ibtBack = toolbar.findViewById(R.id.ibt_ab_mypage_back);
        ibtBack.setOnClickListener(v -> finish());
    }

    private void myOTTClickListener() {
        ActivityResultLauncher<Intent> startActivityResultAddingOtt = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK)
                        updateMyOTTList();
                });

        FloatingActionButton fabAddOttService = findViewById(R.id.fab_my_ott_add);
        fabAddOttService.setOnClickListener(v -> {
            Intent addingOttIntent = new Intent(this, AddMyOTTActivity.class);
            startActivityResultAddingOtt.launch(addingOttIntent);
        });
    }

    private void updateMyOTTList() {
        //TODO: 서버로부터 내가 이용하는 ott 서비스 정보 받아오기!
    }
}

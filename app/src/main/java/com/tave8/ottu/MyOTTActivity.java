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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tave8.ottu.adapter.MyOttPaymentRecyclerAdapter;
import com.tave8.ottu.data.PaymentInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tave8.ottu.MainActivity.myInfo;

public class MyOTTActivity extends AppCompatActivity {
    private boolean isAdded = false;
    private ArrayList<PaymentInfo> myOttPaymentList;

    private MyOttPaymentRecyclerAdapter myOttPaymentRecyclerAdapter;
    private TextView tvMyOttNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ott);

        myOttPaymentList = new ArrayList<>();

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

        tvMyOttNo = findViewById(R.id.tv_my_ott_no);

        RecyclerView rvMyOttPost = findViewById(R.id.rv_my_ott_platformList);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        rvMyOttPost.setLayoutManager(manager);
        myOttPaymentRecyclerAdapter = new MyOttPaymentRecyclerAdapter(myOttPaymentList);
        rvMyOttPost.setAdapter(myOttPaymentRecyclerAdapter);

        updateMyOTTList();
        myOTTClickListener();
    }

    @Override
    public void finish() {
        if (isAdded) {
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
        AppCompatImageButton ibtBack = toolbar.findViewById(R.id.ibt_ab_mypage_back);
        ibtBack.setOnClickListener(v -> finish());
    }

    private void myOTTClickListener() {
        ActivityResultLauncher<Intent> startActivityResultAddingOtt = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        isAdded = true;
                        updateMyOTTList();
                    }
                });

        FloatingActionButton fabAddOttService = findViewById(R.id.fab_my_ott_add);
        fabAddOttService.setOnClickListener(v -> {
            Intent addingOttIntent = new Intent(this, AddMyOTTActivity.class);
            startActivityResultAddingOtt.launch(addingOttIntent);
        });
    }

    public void updateMyOTTList() {
        tvMyOttNo.setVisibility(View.GONE);
        OttURetrofitClient.getApiService().getMyOttList(PreferenceManager.getString(this, "jwt"), myInfo.getUserIdx()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.code() == 200) {
                    myOttPaymentList.clear();
                    try {
                        JSONObject result = new JSONObject(Objects.requireNonNull(response.body()));
                        JSONArray jsonOttList = result.getJSONArray("ottlist");
                        for (int i=0; i<jsonOttList.length(); i++) {
                            JSONObject paymentOtt = jsonOttList.getJSONObject(i);
                            Long paymentIdx = paymentOtt.getLong("teamIdx");
                            int platformIdx = paymentOtt.getJSONObject("platform").getInt("platformIdx");
                            String teamName = paymentOtt.getString("teamName");

                            int headcount = paymentOtt.getInt("headcount");
                            int paymentDay = paymentOtt.getInt("paymentDay");
                            String paymentDate = paymentOtt.getString("paymentDate");
                            myOttPaymentList.add(new PaymentInfo(paymentIdx, platformIdx, teamName, headcount, paymentDay, LocalDate.parse(paymentDate, DateTimeFormatter.ISO_DATE)));
                        }
                        myOttPaymentRecyclerAdapter.notifyDataSetChanged();
                    } catch (JSONException e) { e.printStackTrace(); }
                }
                else if (response.code() == 401) {
                    Toast.makeText(MyOTTActivity.this, "로그인 기한이 만료되어\n 로그인 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
                    PreferenceManager.removeKey(MyOTTActivity.this, "jwt");
                    Intent reLogin = new Intent(MyOTTActivity.this, LoginActivity.class);
                    reLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(reLogin);
                    finish();
                }
                else
                    Toast.makeText(MyOTTActivity.this, "나의 OTT 서비스 로드에 문제가 생겼습니다. 새로 고침을 해주세요.", Toast.LENGTH_SHORT).show();

                if (myOttPaymentList.size() == 0)
                    tvMyOttNo.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Toast.makeText(MyOTTActivity.this, "서버와 연결되지 않았습니다. 확인해 주세요:)", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

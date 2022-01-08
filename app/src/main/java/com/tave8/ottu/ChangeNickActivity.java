package com.tave8.ottu;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tave8.ottu.MainActivity.myInfo;

public class ChangeNickActivity extends AppCompatActivity {
    private boolean isChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_nick);

        Toolbar toolbar = findViewById(R.id.tb_change_nick_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
            Objects.requireNonNull(actionBar).setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
        View customView = View.inflate(this, R.layout.actionbar_mypage, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(customView, params);
        toolbarListener(toolbar);

        EditText etCurrentNick = findViewById(R.id.et_change_nick_current);
        etCurrentNick.setText(myInfo.getNick());

        changeNickClickListener();
    }

    @Override
    public void finish() {
        if (isChanged) {
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

        TextView tvTitle = toolbar.findViewById(R.id.tv_ab_mypage_title);
        tvTitle.setText("닉네임 변경");
    }

    private void changeNickClickListener() {
        TextView tvNickError = findViewById(R.id.tv_change_nick_error);

        EditText etNewNick = findViewById(R.id.et_change_nick_new);
        etNewNick.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvNickError.setVisibility(View.GONE);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        Button btSubmit = findViewById(R.id.bt_change_nick_submit);
        btSubmit.setOnClickListener(v -> {
            etNewNick.setEnabled(false);
            String newNick = etNewNick.getText().toString().trim();

            if (newNick.equals(myInfo.getNick())) {
                tvNickError.setVisibility(View.VISIBLE);
                tvNickError.setText("기존 닉네임과 일치합니다.");
                etNewNick.setEnabled(true);
            }
            else if (newNick.length() != etNewNick.getText().toString().length()) {
                tvNickError.setVisibility(View.VISIBLE);
                tvNickError.setText(R.string.nickname_rule1);
                etNewNick.setEnabled(true);
            }
            else if (newNick.length() == 0) {
                tvNickError.setVisibility(View.VISIBLE);
                tvNickError.setText(R.string.nickname_rule2);
                etNewNick.setEnabled(true);
                etNewNick.requestFocus();
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(etNewNick, 0);
            }
            else if (newNick.length()<2 || newNick.length()>12) {
                tvNickError.setVisibility(View.VISIBLE);
                tvNickError.setText(R.string.nickname_rule3);
                etNewNick.setEnabled(true);
            }
            else if (!newNick.matches(".*[a-zㄱ-ㅎㅏ-ㅣ가-힣]+.*")) {
                tvNickError.setVisibility(View.VISIBLE);
                tvNickError.setText(R.string.nickname_rule4);
                etNewNick.setEnabled(true);
            }
            else if (newNick.matches(".*[^0-9a-zㄱ-ㅎㅏ-ㅣ가-힣_[*]].*")) {
                tvNickError.setVisibility(View.VISIBLE);
                tvNickError.setText(R.string.nickname_rule5);
                etNewNick.setEnabled(true);
            }
            else {
                //1. 닉네임 중복 확인
                //2. 닉네임 중복 확인 성공 후 닉네임 변경
                OttURetrofitClient.getApiService().getCheckNick(PreferenceManager.getString(this, "jwt"), newNick).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        if (response.code() == 200) {
                            try {
                                JSONObject result = new JSONObject(Objects.requireNonNull(response.body()));
                                if (result.getBoolean("isExisted")) {
                                    tvNickError.setVisibility(View.VISIBLE);
                                    tvNickError.setText("이미 존재하는 닉네임입니다.");
                                    etNewNick.setEnabled(true);
                                }
                                else {
                                    JsonObject requestData = new JsonObject();
                                    requestData.addProperty("nickname", newNick);
                                    OttURetrofitClient.getApiService().patchUser(PreferenceManager.getString(ChangeNickActivity.this, "jwt"), myInfo.getUserIdx(), requestData).enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                                            if (response.code() == 200) {
                                                isChanged = true;
                                                myInfo.getUserEssentialInfo().setNick(newNick);
                                                Toast.makeText(ChangeNickActivity.this, "닉네임 변경에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                            else if (response.code() == 401) {
                                                Toast.makeText(ChangeNickActivity.this, "로그인 기한이 만료되어\n 로그인 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
                                                PreferenceManager.removeKey(ChangeNickActivity.this, "jwt");
                                                Intent reLogin = new Intent(ChangeNickActivity.this, LoginActivity.class);
                                                reLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(reLogin);
                                                finish();
                                            }
                                            else {
                                                etNewNick.setEnabled(true);
                                                Toast.makeText(ChangeNickActivity.this, "닉네임 변경에 문제가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                                            etNewNick.setEnabled(true);
                                            Toast.makeText(ChangeNickActivity.this, "서버와 연결되지 않았습니다. 확인해 주세요:)", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            } catch (JSONException e) { e.printStackTrace(); }
                        }
                        else if (response.code() == 401) {
                            Toast.makeText(ChangeNickActivity.this, "로그인 토큰 기한이 만료되어\n 로그인 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
                            PreferenceManager.removeKey(ChangeNickActivity.this, "jwt");
                            Intent reLogin = new Intent(ChangeNickActivity.this, LoginActivity.class);
                            reLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(reLogin);
                            finish();
                        }
                        else {
                            etNewNick.setEnabled(true);
                            Toast.makeText(ChangeNickActivity.this, "다시 한번 닉네임 변경해 주세요.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        etNewNick.setEnabled(true);
                        Toast.makeText(ChangeNickActivity.this, "서버와 연결되지 않았습니다. 확인해 주세요:)", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}

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

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tave8.ottu.MainActivity.myInfo;

public class ChangeKakaoTalkIdActivity extends AppCompatActivity {
    private boolean isChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_kakaotalk_id);

        Toolbar toolbar = findViewById(R.id.tb_change_kakaotalk_id_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
            Objects.requireNonNull(actionBar).setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
        View customView = View.inflate(this, R.layout.actionbar_mypage, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(customView, params);
        toolbarListener(toolbar);

        EditText etCurrentKakaoTalkId = findViewById(R.id.et_change_kakaotalk_id_current);
        etCurrentKakaoTalkId.setText(myInfo.getKakaotalkId());

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
        tvTitle.setText("카카오톡 아이디 변경");
    }

    private void changeNickClickListener() {
        TextView tvNickError = findViewById(R.id.tv_change_kakaotalk_id_error);

        EditText etNewKakaoTalkId = findViewById(R.id.et_change_kakaotalk_id_new);
        etNewKakaoTalkId.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvNickError.setVisibility(View.GONE);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        Button btSubmit = findViewById(R.id.bt_change_kakaotalk_id_submit);
        btSubmit.setOnClickListener(v -> {
            etNewKakaoTalkId.setEnabled(false);
            String newKakaoTalkId = etNewKakaoTalkId.getText().toString().trim();

            if (newKakaoTalkId.length() == 0) {
                etNewKakaoTalkId.setEnabled(true);
                etNewKakaoTalkId.requestFocus();
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(etNewKakaoTalkId, 0);
            } else if (newKakaoTalkId.equals(myInfo.getKakaotalkId())) {
                tvNickError.setVisibility(View.VISIBLE);
                etNewKakaoTalkId.setEnabled(true);
            } else {
                JsonObject requestData = new JsonObject();
                requestData.addProperty("kakaotalkId", newKakaoTalkId);
                OttURetrofitClient.getApiService().patchUser(PreferenceManager.getString(ChangeKakaoTalkIdActivity.this, "jwt"), myInfo.getUserIdx(), requestData).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        if (response.code() == 200) {
                            isChanged = true;
                            myInfo.setKakaotalkId(newKakaoTalkId);
                            Toast.makeText(ChangeKakaoTalkIdActivity.this, "카카오톡 아이디 변경에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else if (response.code() == 401) {
                            Toast.makeText(ChangeKakaoTalkIdActivity.this, "로그인 기한이 만료되어\n 로그인 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
                            PreferenceManager.removeKey(ChangeKakaoTalkIdActivity.this, "jwt");
                            Intent reLogin = new Intent(ChangeKakaoTalkIdActivity.this, LoginActivity.class);
                            reLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(reLogin);
                            finish();
                        }
                        else {
                            etNewKakaoTalkId.setEnabled(true);
                            Toast.makeText(ChangeKakaoTalkIdActivity.this, "카카오톡 아이디 변경에 문제가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        etNewKakaoTalkId.setEnabled(true);
                        Toast.makeText(ChangeKakaoTalkIdActivity.this, "서버와 연결되지 않았습니다. 확인해 주세요:)", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}

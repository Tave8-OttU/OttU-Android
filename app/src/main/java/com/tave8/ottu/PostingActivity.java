package com.tave8.ottu;

import  android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.JsonObject;
import com.tave8.ottu.data.CommunityPostInfo;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tave8.ottu.MainActivity.myInfo;

public class PostingActivity extends AppCompatActivity {
    private boolean isEditing = false, isSubmitted = false;
    private int platformIdx = 0;                                 //글 등록 시 사용
    private CommunityPostInfo postInfo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting);

        EditText etPostContent = findViewById(R.id.et_posting_content);

        if (getIntent().hasExtra("postInfo")) {
            isEditing = true;
            postInfo = getIntent().getExtras().getParcelable("postInfo");
            etPostContent.setText(postInfo.getContent());
        } else {
            platformIdx = getIntent().getExtras().getInt("platformIdx");
        }

        Toolbar toolbar = findViewById(R.id.tb_posting_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
            Objects.requireNonNull(actionBar).setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
        View customView = View.inflate(this, R.layout.actionbar_posting, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(customView, params);
        toolbarListener(toolbar, etPostContent);
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

    private void toolbarListener(Toolbar toolbar, EditText etPostContent) {
        AppCompatImageButton ibtBack = toolbar.findViewById(R.id.ibt_ab_posting_back);
        ibtBack.setOnClickListener(v -> finish());

        if (isEditing) {
            TextView tvTitle = toolbar.findViewById(R.id.tv_ab_posting_title);
            tvTitle.setText("글 수정");
        }

        AppCompatButton btSubmit = toolbar.findViewById(R.id.bt_ab_posting_submit);
        btSubmit.setOnClickListener(v -> {
            etPostContent.setEnabled(false);
            String content = etPostContent.getText().toString().trim();

            if (content.length() == 0) {
                etPostContent.setEnabled(true);
                etPostContent.requestFocus();
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(etPostContent, 0);
            }
            else if (isEditing) {
                if (content.equals(postInfo.getContent())) {
                    etPostContent.setEnabled(true);
                    Toast.makeText(this, "기존의 글 내용과 일치합니다.", Toast.LENGTH_SHORT).show();
                }
                else {
                    JsonObject requestData = new JsonObject();
                    requestData.addProperty("platformIdx", platformIdx);
                    requestData.addProperty("userIdx", myInfo.getUserIdx());
                    requestData.addProperty("content", content);
                    OttURetrofitClient.getApiService().patchPost(PreferenceManager.getString(this, "jwt"), postInfo.getPostIdx(), requestData).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                            if (response.code() == 200) {
                                isSubmitted = true;
                                finish();
                            }
                            else if (response.code() == 401) {
                                Toast.makeText(PostingActivity.this, "로그인 기한이 만료되어\n 로그인 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
                                PreferenceManager.removeKey(PostingActivity.this, "jwt");
                                Intent reLogin = new Intent(PostingActivity.this, LoginActivity.class);
                                reLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(reLogin);
                                finish();
                            }
                            else {
                                etPostContent.setEnabled(true);
                                Toast.makeText(PostingActivity.this, "글 제출에 문제가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                            etPostContent.setEnabled(true);
                            Toast.makeText(PostingActivity.this, "서버와 연결되지 않았습니다. 확인해 주세요:)", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                JsonObject requestData = new JsonObject();
                requestData.addProperty("platformIdx", platformIdx);
                requestData.addProperty("userIdx", myInfo.getUserIdx());
                requestData.addProperty("content", content);
                OttURetrofitClient.getApiService().postPostUpload(PreferenceManager.getString(this, "jwt"), requestData).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        if (response.code() == 201) {
                            isSubmitted = true;
                            finish();
                        }
                        else if (response.code() == 401) {
                            Toast.makeText(PostingActivity.this, "로그인 기한이 만료되어\n 로그인 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
                            PreferenceManager.removeKey(PostingActivity.this, "jwt");
                            Intent reLogin = new Intent(PostingActivity.this, LoginActivity.class);
                            reLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(reLogin);
                            finish();
                        }
                        else {
                            etPostContent.setEnabled(true);
                            Toast.makeText(PostingActivity.this, "글 제출에 문제가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        etPostContent.setEnabled(true);
                        Toast.makeText(PostingActivity.this, "서버와 연결되지 않았습니다. 확인해 주세요:)", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}

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

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

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

            if (newNick.length() == 0) {
                etNewNick.setEnabled(true);
                etNewNick.requestFocus();
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(etNewNick, 0);
            } else if (newNick.equals(myInfo.getNick())) {
                tvNickError.setVisibility(View.VISIBLE);
                tvNickError.setText("기존 닉네임과 일치합니다.");
                etNewNick.setEnabled(true);
            } else {
                //TODO: 서버에 전달함
                //1. 이미 존재하는 닉네임인 경우 -> tvNickError.setVisibility(VISIBLE), etNewNick.setEnabled(true), tvNickError.setText("이미 존재하는 닉네임입니다.");
                //2. 닉네임 변경 -> isChanged = true, finish()
                finish();
            }
        });
    }
}

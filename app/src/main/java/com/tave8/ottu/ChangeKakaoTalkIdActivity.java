package com.tave8.ottu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

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
        View customView = View.inflate(this, R.layout.actionbar_change, null);
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
        AppCompatImageButton ibtBack = toolbar.findViewById(R.id.ibt_ab_change_back);
        ibtBack.setOnClickListener(v -> finish());

        TextView tvTitle = toolbar.findViewById(R.id.tv_ab_change_title);
        tvTitle.setText("카카오톡 아이디 변경");
    }

    private void changeNickClickListener() {
        EditText etNewKakaoTalkId = findViewById(R.id.et_change_kakaotalk_id_new);

        Button btSubmit = findViewById(R.id.bt_change_kakaotalk_id_submit);
        btSubmit.setOnClickListener(v -> {
            etNewKakaoTalkId.setEnabled(false);
            String newKakaoTalkId = etNewKakaoTalkId.getText().toString().trim();

            if (newKakaoTalkId.length() == 0) {
                etNewKakaoTalkId.setEnabled(true);
                etNewKakaoTalkId.requestFocus();
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(etNewKakaoTalkId, 0);
            } else if (newKakaoTalkId.equals(myInfo.getKakaotalkId())) {
                Toast.makeText(this, "기존 카카오톡 아이디와 일치합니다.", Toast.LENGTH_SHORT).show();
                etNewKakaoTalkId.setEnabled(true);
            } else {
                //TODO: 서버에 전달함
                isChanged = true;
                finish();
            }
        });
    }
}

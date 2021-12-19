package com.tave8.ottu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tave8.ottu.adapter.CommentRecyclerAdapter;
import com.tave8.ottu.data.CommentInfo;
import com.tave8.ottu.data.CommunityPostInfo;
import com.tave8.ottu.data.SingletonPlatform;
import com.tave8.ottu.data.UserEssentialInfo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

import static com.tave8.ottu.MainActivity.myInfo;

public class PostActivity extends AppCompatActivity {
    private Long postId = 0L;
    private CommunityPostInfo postInfo = null;
    private ArrayList<CommentInfo> commentInfoList;
    private boolean isWriter = false, isDeleted = false, isChanged = false;

    private TextView tvWriterNick, tvPostDateTime, tvContent, tvCommentNum;
    private CommentRecyclerAdapter commentRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        postInfo = getIntent().getExtras().getParcelable("postInfo");
        postId = postInfo.getPostIdx();
        if (myInfo.getUserIdx().equals(postInfo.getWriterInfo().getUserIdx()))
            isWriter = true;
        commentInfoList = new ArrayList<>();

        Toolbar toolbar = findViewById(R.id.tb_post_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
            Objects.requireNonNull(actionBar).setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
        View customView = View.inflate(this, R.layout.actionbar_post, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(customView, params);
        toolbarListener(toolbar);

        tvWriterNick = findViewById(R.id.tv_post_nick);
        tvPostDateTime = findViewById(R.id.tv_post_datetime);
        tvContent = findViewById(R.id.tv_post_content);
        tvCommentNum = findViewById(R.id.tv_post_comment_num);

        tvWriterNick.setText(postInfo.getWriterInfo().getNick());
        tvPostDateTime.setText(postInfo.getPostDateTime().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss")));
        tvContent.setText(postInfo.getContent().replace(" ", "\u00A0"));
        tvCommentNum.setText(String.valueOf(postInfo.getCommentNum()));

        //TODO: 임시 commentInfoList
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        commentInfoList.add(new CommentInfo(1L, postId, new UserEssentialInfo(1L, "닉네임1"), "첫 번째 댓글입니다.", LocalDateTime.parse("2021-11-08 12:03:10", formatter)));
        commentInfoList.add(new CommentInfo(2L, postId, new UserEssentialInfo(5L, "닉네임5"), "두 번째 댓글입니다.", LocalDateTime.parse("2021-11-23 14:20:23", formatter)));
        commentInfoList.add(new CommentInfo(3L, postId, new UserEssentialInfo(8L, "닉네임8"), "세 번째 댓글입니다.", LocalDateTime.parse("2021-12-03 07:20:23",formatter)));
        //

        RecyclerView rvComment = findViewById(R.id.rv_post_comment);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        rvComment.setLayoutManager(manager);
        commentRecyclerAdapter = new CommentRecyclerAdapter(commentInfoList);
        rvComment.setAdapter(commentRecyclerAdapter);
        DividerItemDecoration devider = new DividerItemDecoration(this, 1);
        devider.setDrawable(Objects.requireNonNull(ResourcesCompat.getDrawable(getResources(), R.drawable.item_divide_bar, null)));
        rvComment.addItemDecoration(devider);
        updatePostInfo(false);

        postClickListener();
    }

    @Override
    public void finish() {
        if (isDeleted || isChanged) {
            Intent editedPostIntent = new Intent();
            setResult(RESULT_OK, editedPostIntent);
        }
        else {
            Intent returnIntent = new Intent();
            setResult(RESULT_CANCELED, returnIntent);
        }

        super.finish();
    }

    private void toolbarListener(Toolbar toolbar) {
        AppCompatImageButton ibtBack = toolbar.findViewById(R.id.ibt_ab_post_back);
        ibtBack.setOnClickListener(v -> finish());

        TextView tvPlatform = toolbar.findViewById(R.id.tv_ab_post_platform);
        tvPlatform.setText(SingletonPlatform.getPlatform().getPlatformNameList().get(postInfo.getPlatformIdx()));

        AppCompatImageButton ibtMenu = toolbar.findViewById(R.id.ibt_ab_post_menu);
        if (isWriter) {
            ibtMenu.setVisibility(View.VISIBLE);
            ActivityResultLauncher<Intent> startActivityResultPosting = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK) {
                            isChanged = true;
                            updatePostInfo(false);
                        }
                    });

            ibtMenu.setOnClickListener(view -> {
                Context wrapper = new ContextThemeWrapper(this, R.style.PopUpMenuTheme);
                PopupMenu postMenu = new PopupMenu(wrapper, view);
                getMenuInflater().inflate(R.menu.menu_post, postMenu.getMenu());

                postMenu.setOnMenuItemClickListener(menuItem -> {
                    if (menuItem.getItemId() == R.id.menu_post_edit) {  //글 수정
                        Intent postingIntent = new Intent(this, PostingActivity.class);
                        Bundle bundle = new Bundle();
                            bundle.putParcelable("postInfo", postInfo);
                        postingIntent.putExtras(bundle);
                        startActivityResultPosting.launch(postingIntent);
                    }
                    else {  //글 삭제
                        View deletePostDialogView = View.inflate(this, R.layout.dialog_delete_post, null);

                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setView(deletePostDialogView);
                        AlertDialog alertDialog = builder.create();
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        alertDialog.show();

                        WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
                        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
                        Point size = new Point();
                        display.getRealSize(size);
                        int width = size.x;
                        params.width = (int) (width*0.75);
                        alertDialog.getWindow().setAttributes(params);

                        AppCompatButton btDeletePostYes = deletePostDialogView.findViewById(R.id.bt_dialog_delete_post_yes);
                        btDeletePostYes.setOnClickListener(v -> {
                            //TODO: 서버에 삭제함을 전달함
                            isDeleted = true;
                            finish();
                        });

                        AppCompatButton btDeletePostNo = deletePostDialogView.findViewById(R.id.bt_dialog_delete_post_no);
                        btDeletePostNo.setOnClickListener(v -> alertDialog.dismiss());
                    }
                    return false;
                });
                postMenu.show();
            });
        }
    }

    private void postClickListener() {
        EditText etAddComment = findViewById(R.id.et_post_comment_content);
        AppCompatButton btAddComment = findViewById(R.id.bt_post_comment_submit);

        etAddComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etAddComment.getText().length() == 0)
                    btAddComment.setVisibility(View.GONE);
                else
                    btAddComment.setVisibility(View.VISIBLE);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        btAddComment.setOnClickListener(v -> {
            etAddComment.setEnabled(false);

            String comment = etAddComment.getText().toString().trim();
            if (comment.length() <= 0)
                etAddComment.setEnabled(true);
            else {
                //TODO: 서버에 내용 추가
                isChanged = true;
                updatePostInfo(true);
                etAddComment.setText("");
                etAddComment.clearFocus();
                ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(etAddComment.getWindowToken(), 0);

                etAddComment.setEnabled(true);
            }
        });
    }

    private void updatePostInfo(boolean isCommentAdded) {
        //TODO: 서버로부터 post내용+댓글 받음
        //tvWriterNick, tvPostDateTime, tvContent, tvCommentNum바꿔줘야 함
        //isCommentAdded가 true이면   nsvPost.post(() -> nsvPost.fullScroll(View.FOCUS_DOWN));
    }
}

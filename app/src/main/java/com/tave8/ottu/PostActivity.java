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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.tave8.ottu.adapter.CommentRecyclerAdapter;
import com.tave8.ottu.data.CommentInfo;
import com.tave8.ottu.data.CommunityPostInfo;
import com.tave8.ottu.data.Genre;
import com.tave8.ottu.data.SingletonPlatform;
import com.tave8.ottu.data.UserEssentialInfo;
import com.tave8.ottu.data.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tave8.ottu.MainActivity.myInfo;

public class PostActivity extends AppCompatActivity {
    private Long postIdx = 0L;
    private CommunityPostInfo postInfo = null;
    private ArrayList<CommentInfo> commentList;
    private boolean isWriter = false, isDeleted = false, isChanged = false;

    private CommentRecyclerAdapter commentRecyclerAdapter;
    private NestedScrollView nsvPost;
    private TextView tvWriterNick, tvContent, tvCommentNum, tvComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        postInfo = getIntent().getExtras().getParcelable("postInfo");
        postIdx = postInfo.getPostIdx();
        if (myInfo.getUserIdx().equals(postInfo.getWriterInfo().getUserIdx()))
            isWriter = true;
        commentList = new ArrayList<>();

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
        TextView tvPostDateTime = findViewById(R.id.tv_post_datetime);
        tvContent = findViewById(R.id.tv_post_content);
        tvCommentNum = findViewById(R.id.tv_post_comment_num);

        tvWriterNick.setText(postInfo.getWriterInfo().getNick());
        tvPostDateTime.setText(postInfo.getPostDateTime().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss")));
        tvContent.setText(postInfo.getContent().replace(" ", "\u00A0"));
        tvCommentNum.setText(String.valueOf(postInfo.getCommentNum()));

        tvComment = findViewById(R.id.tv_post_comment);
        tvComment.setVisibility(View.GONE);

        nsvPost = findViewById(R.id.nsv_post);
        RecyclerView rvComment = findViewById(R.id.rv_post_comment);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        rvComment.setLayoutManager(manager);
        commentRecyclerAdapter = new CommentRecyclerAdapter(commentList);
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
            ActivityResultLauncher<Intent> startActivityResultPosting = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
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
                            OttURetrofitClient.getApiService().deletePost(PreferenceManager.getString(this, "jwt"), postIdx).enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                                    if (response.code() == 200) {
                                        Toast.makeText(PostActivity.this, "글 삭제에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                        alertDialog.dismiss();

                                        isDeleted = true;
                                        finish();
                                    }
                                    else if (response.code() == 401) {
                                        Toast.makeText(PostActivity.this, "로그인 기한이 만료되어\n 로그인 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
                                        PreferenceManager.removeKey(PostActivity.this, "jwt");
                                        Intent reLogin = new Intent(PostActivity.this, LoginActivity.class);
                                        reLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(reLogin);
                                        finish();
                                    }
                                    else {
                                        alertDialog.dismiss();
                                        Toast.makeText(PostActivity.this, "해당 글 삭제에 문제가 생겼습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                                    alertDialog.dismiss();
                                    Toast.makeText(PostActivity.this, "서버와 연결되지 않았습니다. 확인해 주세요.", Toast.LENGTH_SHORT).show();
                                }
                            });
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
        tvWriterNick.setOnClickListener(v -> {
            View profileDialogView = View.inflate(this, R.layout.dialog_user_profile, null);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(profileDialogView);
            final AlertDialog alertDialog = builder.create();
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
            Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            Point size = new Point();
            display.getRealSize(size);
            int width = size.x;
            params.width = (int) (width*0.89);
            alertDialog.getWindow().setAttributes(params);

            AppCompatImageButton ibtCancel = profileDialogView.findViewById(R.id.ibt_dialog_profile_cancel);
            ibtCancel.setOnClickListener(view -> alertDialog.dismiss());

            TextView tvUserNick = profileDialogView.findViewById(R.id.tv_dialog_profile_nick);
            TextView tvOttULevel = profileDialogView.findViewById(R.id.tv_dialog_profile_level);
            ProgressBar pbOttULevel = profileDialogView.findViewById(R.id.pb_dialog_profile);
            AppCompatButton btGenre1 = profileDialogView.findViewById(R.id.bt_dialog_profile_genre1);
            AppCompatButton btGenre2 = profileDialogView.findViewById(R.id.bt_dialog_profile_genre2);
            AppCompatButton btGenre3 = profileDialogView.findViewById(R.id.bt_dialog_profile_genre3);

            Long userIdx = postInfo.getWriterInfo().getUserIdx();
            OttURetrofitClient.getApiService().getUser(PreferenceManager.getString(this, "jwt"), userIdx).enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if (response.code() == 200) {
                        try {
                            JSONObject userInfo = new JSONObject(Objects.requireNonNull(response.body()));
                            JSONObject user = userInfo.getJSONObject("user");

                            ArrayList<Genre> interestGenreList = new ArrayList<>();
                            JSONArray genres = user.getJSONArray("genres");
                            for (int i=0; i<genres.length(); i++) {
                                JSONObject genre = genres.getJSONObject(i);
                                interestGenreList.add(new Genre(genre.getInt("genreIdx"), genre.getString("genreName")));
                            }
                            UserInfo otherUserInfo = new UserInfo(userIdx, user.getString("nickname"), user.getInt("reliability"),
                                    user.getBoolean("isFirst"), interestGenreList);

                            tvUserNick.setText(otherUserInfo.getNick());

                            pbOttULevel.setProgress(otherUserInfo.getReliability());
                            tvOttULevel.setText(String.valueOf(otherUserInfo.getReliability()));
                            if (otherUserInfo.isFirst()) {
                                pbOttULevel.setProgressDrawable(AppCompatResources.getDrawable(PostActivity.this, R.drawable.bg_progress_first));
                                tvOttULevel.setTextColor(getColor(R.color.sub_text_color));
                            }

                            if (otherUserInfo.getInterestGenre().size() == 1) {
                                btGenre1.setText(otherUserInfo.getInterestGenre().get(0).getGenreName());
                                btGenre2.setVisibility(View.INVISIBLE);
                                btGenre3.setVisibility(View.INVISIBLE);
                            } else if (otherUserInfo.getInterestGenre().size() == 2) {
                                btGenre1.setText(otherUserInfo.getInterestGenre().get(0).getGenreName());
                                btGenre2.setText(otherUserInfo.getInterestGenre().get(1).getGenreName());
                                btGenre3.setVisibility(View.INVISIBLE);
                            } else if (otherUserInfo.getInterestGenre().size() == 3) {
                                btGenre1.setText(otherUserInfo.getInterestGenre().get(0).getGenreName());
                                btGenre2.setText(otherUserInfo.getInterestGenre().get(1).getGenreName());
                                btGenre3.setText(otherUserInfo.getInterestGenre().get(2).getGenreName());
                            }
                            alertDialog.show();
                        } catch (JSONException e) { e.printStackTrace(); }
                    }
                    else if (response.code() == 401) {
                        Toast.makeText(PostActivity.this, "로그인 기한이 만료되어\n 로그인 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
                        PreferenceManager.removeKey(PostActivity.this, "jwt");
                        Intent reLogin = new Intent(PostActivity.this, LoginActivity.class);
                        reLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(reLogin);
                        finish();
                    }
                    else
                        Toast.makeText(PostActivity.this, "회원 정보 불러오기에 문제가 생겼습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    Toast.makeText(PostActivity.this, "서버와 연결되지 않았습니다. 확인해 주세요.", Toast.LENGTH_SHORT).show();
                }
            });
        });

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
                JsonObject requestData = new JsonObject();
                requestData.addProperty("postIdx", postIdx);
                requestData.addProperty("userIdx", myInfo.getUserIdx());
                requestData.addProperty("content", comment);
                OttURetrofitClient.getApiService().postCommentUpload(PreferenceManager.getString(this, "jwt"), requestData).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        if (response.code() == 201) {
                            isChanged = true;
                            updatePostInfo(true);
                            etAddComment.setEnabled(true);
                            etAddComment.setText("");
                            etAddComment.clearFocus();
                            ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(etAddComment.getWindowToken(), 0);
                        }
                        else if (response.code() == 401) {
                            Toast.makeText(PostActivity.this, "로그인 기한이 만료되어\n 로그인 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
                            PreferenceManager.removeKey(PostActivity.this, "jwt");
                            Intent reLogin = new Intent(PostActivity.this, LoginActivity.class);
                            reLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(reLogin);
                            finish();
                        }
                        else {
                            etAddComment.setEnabled(true);
                            Toast.makeText(PostActivity.this, "댓글 추가에 문제가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        etAddComment.setEnabled(true);
                        Toast.makeText(PostActivity.this, "서버와 연결되지 않았습니다. 확인해 주세요:)", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void updatePostInfo(boolean isCommentAdded) {
        OttURetrofitClient.getApiService().getPost(PreferenceManager.getString(this, "jwt"), postIdx).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.code() == 200) {
                    commentList.clear();
                    try {
                        JSONObject result = new JSONObject(Objects.requireNonNull(response.body()));
                        String nickname = result.getJSONObject("post").getJSONObject("writer").getString("nickname");
                        String content = result.getJSONObject("post").getString("content");
                        int commentNum = (int) result.getJSONObject("post").getLong("commentNum");

                        postInfo.getWriterInfo().setNick(nickname);
                        postInfo.setContent(content);
                        postInfo.setCommentNum(commentNum);

                        tvWriterNick.setText(nickname);
                        tvContent.setText(content.replace(" ", "\u00A0"));
                        tvCommentNum.setText(String.valueOf(commentNum));

                        JSONArray jsonCommentList = result.getJSONArray("commentlist");
                        for (int i=0; i<jsonCommentList.length(); i++) {
                            JSONObject comment = jsonCommentList.getJSONObject(i);
                            Long commentIdx = comment.getLong("commentIdx");

                            JSONObject writer = comment.getJSONObject("writer");
                            UserEssentialInfo writerInfo = new UserEssentialInfo(writer.getLong("userIdx"), writer.getString("nickname"));

                            String commentContent = comment.getString("content");

                            String createdDate = comment.getString("createdDate");
                            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

                            commentList.add(new CommentInfo(commentIdx, writerInfo, commentContent, LocalDateTime.parse(createdDate, dateTimeFormatter)));
                        }
                        commentRecyclerAdapter.notifyDataSetChanged();
                    } catch (JSONException e) { e.printStackTrace(); }

                    if (isCommentAdded)
                        nsvPost.post(() -> nsvPost.fullScroll(View.FOCUS_DOWN));

                    if (commentList.size() == 0)
                        tvComment.setVisibility(View.GONE);
                    else
                        tvComment.setVisibility(View.VISIBLE);
                }
                else if (response.code() == 401) {
                    Toast.makeText(PostActivity.this, "로그인 기한이 만료되어\n 로그인 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
                    PreferenceManager.removeKey(PostActivity.this, "jwt");
                    Intent reLogin = new Intent(PostActivity.this, LoginActivity.class);
                    reLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(reLogin);
                    finish();
                }
                else
                    Toast.makeText(PostActivity.this, "글 로드에 문제가 생겼습니다. 새로 고침을 해주세요.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Toast.makeText(PostActivity.this, "서버와 연결되지 않았습니다. 확인해 주세요:)", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void isCommentDelete() {     //댓글 삭제 됨
        isChanged = true;
    }
}

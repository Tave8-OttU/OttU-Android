package com.tave8.ottu.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.tave8.ottu.LoginActivity;
import com.tave8.ottu.OttURetrofitClient;
import com.tave8.ottu.PostActivity;
import com.tave8.ottu.PreferenceManager;
import com.tave8.ottu.R;
import com.tave8.ottu.data.CommentInfo;
import com.tave8.ottu.data.Genre;
import com.tave8.ottu.data.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tave8.ottu.MainActivity.myInfo;

public class CommentRecyclerAdapter extends RecyclerView.Adapter<CommentRecyclerAdapter.ItemViewHolder> {
    private Context context;
    private ArrayList<CommentInfo> commentList;

    public CommentRecyclerAdapter(ArrayList<CommentInfo> commentList) {
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentRecyclerAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_comment, parent, false);
        CommentRecyclerAdapter.ItemViewHolder viewHolder = new CommentRecyclerAdapter.ItemViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentRecyclerAdapter.ItemViewHolder holder, int position) {
        holder.tvWriterNick.setText(commentList.get(position).getWriterInfo().getNick());
        holder.tvCommentTime.setText(commentList.get(position).getCommentDateTime().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")));
        holder.tvContent.setText(commentList.get(position).getContent().replace(" ", "\u00A0"));

        if (myInfo.getUserIdx().equals(commentList.get(position).getWriterInfo().getUserIdx()))
            holder.ibtDelete.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageButton ibtDelete;
        TextView tvWriterNick, tvCommentTime, tvContent;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            tvWriterNick = itemView.findViewById(R.id.tv_item_comment_nick);
            tvCommentTime = itemView.findViewById(R.id.tv_item_comment_datetime);
            tvContent = itemView.findViewById(R.id.tv_item_comment_content);
            ibtDelete = itemView.findViewById(R.id.ibt_item_comment_delete);

            tvWriterNick.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    View profileDialogView = View.inflate(context, R.layout.dialog_user_profile, null);

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setView(profileDialogView);
                    final AlertDialog alertDialog = builder.create();
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
                    Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
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

                    Long userIdx = commentList.get(pos).getWriterInfo().getUserIdx();
                    OttURetrofitClient.getApiService().getUser(PreferenceManager.getString(context, "jwt"), userIdx).enqueue(new Callback<String>() {
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
                                        pbOttULevel.setProgressDrawable(AppCompatResources.getDrawable(context, R.drawable.bg_progress_first));
                                        tvOttULevel.setTextColor(context.getColor(R.color.sub_text_color));
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
                                Toast.makeText(context, "로그인 기한이 만료되어\n 로그인 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
                                PreferenceManager.removeKey(context, "jwt");
                                Intent reLogin = new Intent(context, LoginActivity.class);
                                reLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(reLogin);
                                ((PostActivity) context).finish();
                            }
                            else
                                Toast.makeText(context, "회원 정보 불러오기에 문제가 생겼습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                            Toast.makeText(context, "서버와 연결되지 않았습니다. 확인해 주세요.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            ibtDelete.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    View deleteCommentDialogView = View.inflate(context, R.layout.dialog_delete_comment, null);

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setView(deleteCommentDialogView);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    alertDialog.show();

                    WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
                    Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
                    Point size = new Point();
                    display.getRealSize(size);
                    int width = size.x;
                    params.width = (int) (width*0.75);
                    alertDialog.getWindow().setAttributes(params);

                    AppCompatButton btDeleteCommentYes = deleteCommentDialogView.findViewById(R.id.bt_dialog_delete_comment_yes);
                    btDeleteCommentYes.setOnClickListener(view -> {
                        OttURetrofitClient.getApiService().deleteComment(PreferenceManager.getString(context, "jwt"), commentList.get(pos).getCommentIdx()).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                                if (response.code() == 200) {
                                    alertDialog.dismiss();
                                    Toast.makeText(context, "댓글 삭제에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                    ((PostActivity) context).isCommentDelete();
                                    ((PostActivity) context).updatePostInfo(false);
                                }
                                else if (response.code() == 401) {
                                    Toast.makeText(context, "로그인 기한이 만료되어\n 로그인 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
                                    PreferenceManager.removeKey(context, "jwt");
                                    Intent reLogin = new Intent(context, LoginActivity.class);
                                    reLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    context.startActivity(reLogin);
                                    ((PostActivity) context).finish();
                                }
                                else {
                                    alertDialog.dismiss();
                                    Toast.makeText(context, "해당 글 삭제에 문제가 생겼습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                                alertDialog.dismiss();
                                Toast.makeText(context, "서버와 연결되지 않았습니다. 확인해 주세요.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    });

                    AppCompatButton btDeleteCommentNo = deleteCommentDialogView.findViewById(R.id.bt_dialog_delete_comment_no);
                    btDeleteCommentNo.setOnClickListener(view -> alertDialog.dismiss());
                }
            });
        }
    }
}

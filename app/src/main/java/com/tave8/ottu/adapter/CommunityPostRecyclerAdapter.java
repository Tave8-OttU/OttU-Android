package com.tave8.ottu.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.tave8.ottu.PostActivity;
import com.tave8.ottu.R;
import com.tave8.ottu.data.CommunityPostInfo;
import com.tave8.ottu.data.Genre;
import com.tave8.ottu.data.UserInfo;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class CommunityPostRecyclerAdapter extends RecyclerView.Adapter<CommunityPostRecyclerAdapter.ItemViewHolder> {
    private Context context;
    private ArrayList<CommunityPostInfo> communityPostInfoList;
    private ActivityResultLauncher<Intent> startActivityResultPost;

    public CommunityPostRecyclerAdapter(ArrayList<CommunityPostInfo> communityPostInfoList, ActivityResultLauncher<Intent> startActivityResultPost) {
        this.communityPostInfoList = communityPostInfoList;
        this.startActivityResultPost = startActivityResultPost;
    }

    @NonNull
    @Override
    public CommunityPostRecyclerAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_community_post, parent, false);
        CommunityPostRecyclerAdapter.ItemViewHolder viewHolder = new CommunityPostRecyclerAdapter.ItemViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommunityPostRecyclerAdapter.ItemViewHolder holder, int position) {
        holder.tvWriterNick.setText(communityPostInfoList.get(position).getWriterInfo().getNick());

        //TODO: 시간 표시!
        LocalDateTime dateTimeNow = LocalDateTime.now();
        //Time이 String일 때 해줘야 함!
        //LocalDateTime postDateTime = LocalDateTime.parse(communityPostInfoList.get(position).getPostDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime postDateTime = communityPostInfoList.get(position).getPostDateTime();
        if (ChronoUnit.SECONDS.between(postDateTime, dateTimeNow) < 60)
            holder.tvPostTime.setText(String.valueOf(ChronoUnit.SECONDS.between(postDateTime, dateTimeNow)).concat("초 전"));
        else if (ChronoUnit.MINUTES.between(postDateTime, dateTimeNow) < 60)
            holder.tvPostTime.setText(String.valueOf(ChronoUnit.MINUTES.between(postDateTime, dateTimeNow)).concat("분 전"));
        else if (ChronoUnit.HOURS.between(postDateTime, dateTimeNow) < 24)
            holder.tvPostTime.setText(String.valueOf(ChronoUnit.HOURS.between(postDateTime, dateTimeNow)).concat("시간 전"));
        else if (ChronoUnit.DAYS.between(postDateTime, dateTimeNow) < 7)
            holder.tvPostTime.setText(String.valueOf(ChronoUnit.DAYS.between(postDateTime, dateTimeNow)).concat("일 전"));
        else if (ChronoUnit.YEARS.between(postDateTime, dateTimeNow) < 1)
            holder.tvPostTime.setText(String.valueOf(postDateTime.getMonthValue()).concat("/").concat(String.valueOf(postDateTime.getDayOfMonth())));
        else
            holder.tvPostTime.setText(String.valueOf(postDateTime.getYear()).substring(2).concat("/").concat(String.valueOf(postDateTime.getMonthValue())).concat("/").concat(String.valueOf(postDateTime.getDayOfMonth())));

        holder.tvContent.setText(communityPostInfoList.get(position).getContent().replace(" ", "\u00A0"));
        holder.tvCommentNum.setText(String.valueOf(communityPostInfoList.get(position).getCommentNum()));
    }

    @Override
    public int getItemCount() {
        return communityPostInfoList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvWriterNick, tvPostTime, tvContent, tvCommentNum;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            tvWriterNick = itemView.findViewById(R.id.tv_item_community_post_nick);
            tvPostTime = itemView.findViewById(R.id.tv_item_community_post_time);
            tvContent = itemView.findViewById(R.id.tv_item_community_post_content);
            tvCommentNum = itemView.findViewById(R.id.tv_item_community_post_comment_num);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    Intent showCommunityPost = new Intent(context, PostActivity.class);
                    Bundle bundle = new Bundle();
                        bundle.putParcelable("postInfo", communityPostInfoList.get(pos));
                    showCommunityPost.putExtras(bundle);
                    startActivityResultPost.launch(showCommunityPost);
                }
            });

            tvWriterNick.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    View profileDialogView = View.inflate(context, R.layout.dialog_user_profile, null);

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setView(profileDialogView);
                    final AlertDialog alertDialog = builder.create();
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    alertDialog.show();

                    WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
                    Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
                    Point size = new Point();
                    display.getRealSize(size);
                    int width = size.x;
                    params.width = (int) (width*0.89);
                    alertDialog.getWindow().setAttributes(params);

                    //TODO: 서버로부터 사용자의 정보 받아옴(communityPostInfoList.get(pos).getWriterInfo().getUserIdx() 전달)
                    //TODO: 임시
                    //
                    ArrayList<Genre> interestGenreList = new ArrayList<>();
                    interestGenreList.add(new Genre(1, "드라마"));
                    interestGenreList.add(new Genre(5, "사극"));
                    UserInfo writerInfo = new UserInfo(1L, "오뜨유", 7, false, interestGenreList);
                    //

                    AppCompatImageButton ibtCancel = profileDialogView.findViewById(R.id.ibt_dialog_profile_cancel);
                    ibtCancel.setOnClickListener(view -> alertDialog.dismiss());

                    TextView tvUserNick = profileDialogView.findViewById(R.id.tv_dialog_profile_nick);
                    tvUserNick.setText(writerInfo.getNick());

                    ProgressBar pbOttULevel = profileDialogView.findViewById(R.id.pb_dialog_profile);
                    TextView tvOttULevel = profileDialogView.findViewById(R.id.tv_dialog_profile_level);

                    pbOttULevel.setProgress(writerInfo.getReliability());
                    tvOttULevel.setText(String.valueOf(writerInfo.getReliability()));
                    if (writerInfo.isFirst()) {
                        pbOttULevel.setProgressDrawable(AppCompatResources.getDrawable(context, R.drawable.bg_progress_first));
                        tvOttULevel.setTextColor(context.getColor(R.color.sub_text_color));
                    }

                    AppCompatButton btGenre1 = profileDialogView.findViewById(R.id.bt_dialog_profile_genre1);
                    AppCompatButton btGenre2 = profileDialogView.findViewById(R.id.bt_dialog_profile_genre2);
                    AppCompatButton btGenre3 = profileDialogView.findViewById(R.id.bt_dialog_profile_genre3);

                    if (writerInfo.getInterestGenre().size() == 1) {
                        btGenre1.setText(writerInfo.getInterestGenre().get(0).getGenreName());
                        btGenre2.setVisibility(View.INVISIBLE);
                        btGenre3.setVisibility(View.INVISIBLE);
                    } else if (writerInfo.getInterestGenre().size() == 2) {
                        btGenre1.setText(writerInfo.getInterestGenre().get(0).getGenreName());
                        btGenre2.setText(writerInfo.getInterestGenre().get(1).getGenreName());
                        btGenre3.setVisibility(View.INVISIBLE);
                    } else {
                        btGenre1.setText(writerInfo.getInterestGenre().get(0).getGenreName());
                        btGenre2.setText(writerInfo.getInterestGenre().get(1).getGenreName());
                        btGenre3.setText(writerInfo.getInterestGenre().get(2).getGenreName());
                    }
                }
            });
        }
    }
}

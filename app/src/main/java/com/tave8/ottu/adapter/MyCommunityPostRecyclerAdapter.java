package com.tave8.ottu.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tave8.ottu.PostActivity;
import com.tave8.ottu.R;
import com.tave8.ottu.data.CommunityPostInfo;
import com.tave8.ottu.data.SingletonPlatform;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class MyCommunityPostRecyclerAdapter extends RecyclerView.Adapter<MyCommunityPostRecyclerAdapter.ItemViewHolder> {
    private Context context;
    private ArrayList<CommunityPostInfo> communityPostInfoList;
    private ActivityResultLauncher<Intent> startActivityResultPost;

    public MyCommunityPostRecyclerAdapter(ArrayList<CommunityPostInfo> communityPostInfoList, ActivityResultLauncher<Intent> startActivityResultPost) {
        this.communityPostInfoList = communityPostInfoList;
        this.startActivityResultPost = startActivityResultPost;
    }

    @NonNull
    @Override
    public MyCommunityPostRecyclerAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_mypage_community, parent, false);
        MyCommunityPostRecyclerAdapter.ItemViewHolder viewHolder = new MyCommunityPostRecyclerAdapter.ItemViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyCommunityPostRecyclerAdapter.ItemViewHolder holder, int position) {
        int platformIdx = communityPostInfoList.get(position).getPlatformIdx();
        if (platformIdx == 3)       //웨이브
            holder.ivPlatform.setImageResource(R.drawable.icon_ott_wavve_white);
        else if (platformIdx == 5)  //디즈니 플러스
            holder.ivPlatform.setImageResource(R.drawable.icon_ott_disney_white);
        else
            holder.ivPlatform.setImageResource(SingletonPlatform.getPlatform().getPlatformLogoList().get(platformIdx));

        LocalDateTime dateTimeNow = LocalDateTime.now();
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
            holder.tvPostTime.setText(String.valueOf(postDateTime.getYear()).substring(2).concat(".").concat(String.valueOf(postDateTime.getMonthValue())).concat(".").concat(String.valueOf(postDateTime.getDayOfMonth())));

        holder.tvContent.setText(communityPostInfoList.get(position).getContent().replace(" ", "\u00A0"));
        holder.tvCommentNum.setText(String.valueOf(communityPostInfoList.get(position).getCommentNum()));
    }

    @Override
    public int getItemCount() {
        return communityPostInfoList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPlatform;
        TextView tvPostTime, tvContent, tvCommentNum;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            ivPlatform = itemView.findViewById(R.id.iv_item_mypage_community_platform);
            tvPostTime = itemView.findViewById(R.id.tv_item_mypage_community_time);
            tvContent = itemView.findViewById(R.id.tv_item_mypage_community_content);
            tvCommentNum = itemView.findViewById(R.id.tv_item_mypage_community_comment_num);

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
        }
    }
}

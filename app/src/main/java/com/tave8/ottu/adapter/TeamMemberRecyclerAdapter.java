package com.tave8.ottu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tave8.ottu.R;
import com.tave8.ottu.data.UserInfo;

import java.util.ArrayList;

public class TeamMemberRecyclerAdapter extends RecyclerView.Adapter<TeamMemberRecyclerAdapter.ItemViewHolder> {
    private Context context;
    private ArrayList<UserInfo> memberInfoList;

    public TeamMemberRecyclerAdapter(ArrayList<UserInfo> memberInfoList) {
        this.memberInfoList = memberInfoList;
    }

    @NonNull
    @Override
    public TeamMemberRecyclerAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_team_member, parent, false);
        TeamMemberRecyclerAdapter.ItemViewHolder viewHolder = new TeamMemberRecyclerAdapter.ItemViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TeamMemberRecyclerAdapter.ItemViewHolder holder, int position) {
        holder.tvNick.setText(memberInfoList.get(position).getNick());
        holder.tvKakaotalkid.setText(memberInfoList.get(position).getKakaotalkId());
    }

    @Override
    public int getItemCount() {
        return memberInfoList.size();
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvNick, tvKakaotalkid;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNick = itemView.findViewById(R.id.tv_item_team_member_nick);
            tvKakaotalkid = itemView.findViewById(R.id.tv_item_team_member_kakaoid);
        }
    }
}

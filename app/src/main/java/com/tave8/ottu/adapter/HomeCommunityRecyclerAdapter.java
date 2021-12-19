package com.tave8.ottu.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tave8.ottu.CommunityActivity;
import com.tave8.ottu.R;
import com.tave8.ottu.data.SimpleCommunityInfo;

import java.util.ArrayList;

public class HomeCommunityRecyclerAdapter extends RecyclerView.Adapter<HomeCommunityRecyclerAdapter.ItemViewHolder> {
    private Context context;
    private ArrayList<SimpleCommunityInfo> simpleOttCommunityList;

    public HomeCommunityRecyclerAdapter(ArrayList<SimpleCommunityInfo> simpleOttCommunityList) {
        this.simpleOttCommunityList = simpleOttCommunityList;
    }

    @NonNull
    @Override
    public HomeCommunityRecyclerAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_home_community, parent, false);
        HomeCommunityRecyclerAdapter.ItemViewHolder viewHolder = new HomeCommunityRecyclerAdapter.ItemViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HomeCommunityRecyclerAdapter.ItemViewHolder holder, int position) {
        holder.tvOttName.setText(simpleOttCommunityList.get(position).getPlatformName());
        holder.tvOttLatestContent.setText(simpleOttCommunityList.get(position).getLatestContent());
    }

    @Override
    public int getItemCount() {
        return simpleOttCommunityList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvOttName, tvOttLatestContent;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            tvOttName = itemView.findViewById(R.id.tv_item_home_commu_platform);
            tvOttLatestContent = itemView.findViewById(R.id.tv_item_home_commu_content);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    Intent showOttCommunity = new Intent(context, CommunityActivity.class);
                    Bundle bundle = new Bundle();
                        bundle.putInt("platformId", simpleOttCommunityList.get(pos).getPlatformIdx());
                    showOttCommunity.putExtras(bundle);
                    context.startActivity(showOttCommunity);
                }
            });
        }
    }
}

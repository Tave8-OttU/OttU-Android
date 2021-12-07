package com.tave8.ottu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tave8.ottu.R;
import com.tave8.ottu.data.RatePlanInfo;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class RatePlanRecyclerAdapter extends RecyclerView.Adapter<RatePlanRecyclerAdapter.ItemViewHolder> {
    private Context context;
    private int selectedRatePlanPosition = -1;
    private ArrayList<RatePlanInfo> ratePlanInfoList;

    public RatePlanRecyclerAdapter(ArrayList<RatePlanInfo> ratePlanInfoList) {
        this.ratePlanInfoList = ratePlanInfoList;
    }

    @NonNull
    @Override
    public RatePlanRecyclerAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_recruiting_rateplan, parent, false);
        RatePlanRecyclerAdapter.ItemViewHolder viewHolder = new RatePlanRecyclerAdapter.ItemViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RatePlanRecyclerAdapter.ItemViewHolder holder, int position) {
        if (selectedRatePlanPosition == position)
            holder.rlBackground.setBackgroundResource(R.drawable.bg_r5black_sblue);
        else
            holder.rlBackground.setBackgroundResource(R.drawable.bg_r5black);

        holder.tvRatePlanName.setText(ratePlanInfoList.get(position).getRatePlanName());
        holder.tvHeadCount.setText(String.valueOf(ratePlanInfoList.get(position).getHeadCount()));

        DecimalFormat chargeFormatter = new DecimalFormat("###,##0");
        holder.tvCharge.setText(chargeFormatter.format(ratePlanInfoList.get(position).getCharge()));
    }

    @Override
    public int getItemCount() {
        return ratePlanInfoList.size();
    }

    public int getSelectedRatePlanPosition() {
        return selectedRatePlanPosition;
    }

    public int getSelectedRatePlanHeadCount() {
        return ratePlanInfoList.get(selectedRatePlanPosition).getHeadCount();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rlBackground;
        TextView tvRatePlanName, tvHeadCount, tvCharge;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            rlBackground = itemView.findViewById(R.id.rl_recruiting_bg);
            tvRatePlanName = itemView.findViewById(R.id.tv_item_recruiting_rateplan);
            tvHeadCount = itemView.findViewById(R.id.tv_item_recruiting_headCount);
            tvCharge = itemView.findViewById(R.id.tv_item_recruiting_charge);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    if (selectedRatePlanPosition != pos) {
                        selectedRatePlanPosition = pos;
                        notifyDataSetChanged();
                    }
                }
            });
        }
    }
}

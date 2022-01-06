package com.tave8.ottu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tave8.ottu.R;
import com.tave8.ottu.data.UserEssentialInfo;

import java.util.ArrayList;

public class EvaluationRecyclerAdapter extends RecyclerView.Adapter<EvaluationRecyclerAdapter.ItemViewHolder> {
    private Context context;
    private ArrayList<UserEssentialInfo> memberList;
    private ArrayList<Integer> reliabilityList;

    public EvaluationRecyclerAdapter(ArrayList<UserEssentialInfo> memberList) {
        this.memberList = memberList;

        reliabilityList = new ArrayList<>(memberList.size());
        for (int i=0; i<memberList.size(); i++)
            reliabilityList.add(10);
    }

    @NonNull
    @Override
    public EvaluationRecyclerAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_team_evaluation, parent, false);
        EvaluationRecyclerAdapter.ItemViewHolder viewHolder = new EvaluationRecyclerAdapter.ItemViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EvaluationRecyclerAdapter.ItemViewHolder holder, int position) {
        holder.tvNick.setText(memberList.get(position).getNick());

        holder.ivMinus.setOnClickListener(v -> {
            int reliability = reliabilityList.get(position);
            if (reliability > 0) {
                reliabilityList.set(position, reliability-1);
                holder.etReliability.setText(String.valueOf(reliability-1));
            }
        });

        holder.ivPlus.setOnClickListener(v -> {
            int reliability = reliabilityList.get(position);
            if (reliability < 10) {
                reliabilityList.set(position, reliability+1);
                holder.etReliability.setText(String.valueOf(reliability+1));
            }
        });
    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }

    public ArrayList<Integer> getReliabilityList() {
        return reliabilityList;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvNick;
        EditText etReliability;
        ImageView ivMinus, ivPlus;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNick = itemView.findViewById(R.id.tv_item_evaluation_nick);
            ivMinus = itemView.findViewById(R.id.iv_item_evaluation_minus);
            etReliability = itemView.findViewById(R.id.et_item_evaluation_reliability);
            ivPlus = itemView.findViewById(R.id.iv_item_evaluation_plus);
        }
    }
}

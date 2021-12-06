package com.tave8.ottu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tave8.ottu.R;
import com.tave8.ottu.data.SimpleOttPayment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class HomePaymentPagerAdapter extends RecyclerView.Adapter<HomePaymentPagerAdapter.ItemViewHolder> {
    private Context context;
    private ArrayList<SimpleOttPayment> simpleOttPaymentList;

    public HomePaymentPagerAdapter(ArrayList<SimpleOttPayment> simpleOttPaymentList) {
        this.simpleOttPaymentList = simpleOttPaymentList;
    }

    @NonNull
    @Override
    public HomePaymentPagerAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_home_payment, parent, false);
        HomePaymentPagerAdapter.ItemViewHolder viewHolder = new HomePaymentPagerAdapter.ItemViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HomePaymentPagerAdapter.ItemViewHolder holder, int position) {
        switch (simpleOttPaymentList.get(position).getPlatformId()) {
            case 1: {
                holder.ivOttIcon.setImageResource(R.drawable.icon_ott_netflix);
                break;
            } case 2: {
                holder.ivOttIcon.setImageResource(R.drawable.icon_ott_tving);
                break;
            } case 3: {
                holder.ivOttIcon.setImageResource(R.drawable.icon_ott_wavve);
                break;
            } case 4: {
                holder.ivOttIcon.setImageResource(R.drawable.icon_ott_watcha);
                break;
            } case 5: {
                holder.ivOttIcon.setImageResource(R.drawable.icon_ott_disney);
                break;
            } case 6: {
                holder.ivOttIcon.setImageResource(R.drawable.icon_ott_coupang_play);
                break;
            }
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREA);
        holder.tvOttPaymentDate.setText(dateFormat.format(simpleOttPaymentList.get(position).getPaymentDate()));
    }

    @Override
    public int getItemCount() {
        return simpleOttPaymentList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView ivOttIcon;
        TextView tvOttPaymentDate;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            ivOttIcon = itemView.findViewById(R.id.iv_item_home_payment_platform);
            tvOttPaymentDate = itemView.findViewById(R.id.tv_item_home_payment_date);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    //TODO: 다이얼로그 띄워줘야 함! -> 상세 결제 예정 내용(dialog_ott_info)
                }
            });
        }
    }
}

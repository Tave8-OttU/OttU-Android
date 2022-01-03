package com.tave8.ottu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tave8.ottu.R;
import com.tave8.ottu.data.Notice;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class NoticeRecyclerAdapter extends RecyclerView.Adapter<NoticeRecyclerAdapter.ItemViewHolder> {
    private Context context;
    private ArrayList<Notice> noticeList;

    public NoticeRecyclerAdapter(ArrayList<Notice> noticeList) {
        this.noticeList = noticeList;
    }

    @NonNull
    @Override
    public NoticeRecyclerAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_notice, parent, false);
        NoticeRecyclerAdapter.ItemViewHolder viewHolder = new NoticeRecyclerAdapter.ItemViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeRecyclerAdapter.ItemViewHolder holder, int position) {
        holder.tvContent.setText(noticeList.get(position).getContent());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        holder.tvCreatedDate.setText(noticeList.get(position).getCreatedDate().format(dateTimeFormatter));
    }

    @Override
    public int getItemCount() {
        return noticeList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvContent, tvCreatedDate;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            tvContent = itemView.findViewById(R.id.tv_item_notice_content);
            tvCreatedDate = itemView.findViewById(R.id.tv_item_notice_date);
        }
    }
}

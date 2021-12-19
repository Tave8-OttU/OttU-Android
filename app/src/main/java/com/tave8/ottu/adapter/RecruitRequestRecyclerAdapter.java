package com.tave8.ottu.adapter;

import android.app.AlertDialog;
import android.content.Context;
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

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.tave8.ottu.R;
import com.tave8.ottu.data.Genre;
import com.tave8.ottu.data.RecruitRequestInfo;
import com.tave8.ottu.data.UserInfo;

import java.util.ArrayList;

public class RecruitRequestRecyclerAdapter extends RecyclerView.Adapter<RecruitRequestRecyclerAdapter.ItemViewHolder> {
    private Context context;
    private ArrayList<RecruitRequestInfo> requestUserList;

    public RecruitRequestRecyclerAdapter(ArrayList<RecruitRequestInfo> requestUserList) {
        this.requestUserList = requestUserList;
    }

    @NonNull
    @Override
    public RecruitRequestRecyclerAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_recruit_request, parent, false);
        RecruitRequestRecyclerAdapter.ItemViewHolder viewHolder = new RecruitRequestRecyclerAdapter.ItemViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecruitRequestRecyclerAdapter.ItemViewHolder holder, int position) {
        holder.tvRequesterNick.setText(requestUserList.get(position).getRequesterInfo().getNick());

        if (requestUserList.get(position).getIsAccepted()) {
            holder.btRequestAccept.setVisibility(View.GONE);
            holder.btRequestRefuse.setVisibility(View.VISIBLE);
        }
        else {
            holder.btRequestAccept.setVisibility(View.VISIBLE);
            holder.btRequestRefuse.setVisibility(View.GONE);
        }
        holder.btRequestAccept.setOnClickListener(v -> {
            //TODO: 서버에 수락함을 전달함
            notifyItemChanged(position);
        });
        holder.btRequestRefuse.setOnClickListener(v -> {
            //TODO: 서버에 수락 취소를 전달함
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return requestUserList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvRequesterNick;
        AppCompatButton btRequestAccept, btRequestRefuse;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            tvRequesterNick = itemView.findViewById(R.id.tv_item_request_nick);
            btRequestAccept = itemView.findViewById(R.id.bt_item_request_accept);
            btRequestRefuse = itemView.findViewById(R.id.bt_item_request_refuse);

            tvRequesterNick.setOnClickListener(v -> {
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

                //TODO: 서버로부터 사용자의 정보 받아옴(userID 전달)
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
            });
        }
    }
}

package com.tave8.ottu.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tave8.ottu.R;
import com.tave8.ottu.data.Genre;
import com.tave8.ottu.data.RecruitInfo;
import com.tave8.ottu.data.RecruitRequestInfo;
import com.tave8.ottu.data.UserEssentialInfo;
import com.tave8.ottu.data.UserInfo;

import java.util.ArrayList;

import static com.tave8.ottu.MainActivity.myInfo;

public class RecruitRecyclerAdapter extends RecyclerView.Adapter<RecruitRecyclerAdapter.ItemViewHolder> {
    private Context context;
    private ArrayList<RecruitInfo> recruitPostList;

    public RecruitRecyclerAdapter(ArrayList<RecruitInfo> recruitPostList) {
        this.recruitPostList = recruitPostList;
    }

    @NonNull
    @Override
    public RecruitRecyclerAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_recruit, parent, false);
        RecruitRecyclerAdapter.ItemViewHolder viewHolder = new RecruitRecyclerAdapter.ItemViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecruitRecyclerAdapter.ItemViewHolder holder, int position) {
        holder.tvWriterNick.setText(recruitPostList.get(position).getWriterInfo().getNick());
        if (recruitPostList.get(position).isCompleted()) {
            holder.llitem.setBackgroundColor(context.getColor(R.color.bg_black_color1_4));
            holder.tvRecruitIng.setVisibility(View.GONE);
            holder.tvRecruitCompleted.setVisibility(View.VISIBLE);
            holder.tvChoiceNum.setTextColor(context.getColor(R.color.sub_text_color));
            holder.tvSlash.setTextColor(context.getColor(R.color.sub_text_color));
            holder.tvHeadCount.setTextColor(context.getColor(R.color.sub_text_color));
        }
        else {
            holder.llitem.setBackgroundColor(context.getColor(R.color.bg_black_color2_4));
            holder.tvRecruitIng.setVisibility(View.VISIBLE);
            holder.tvRecruitCompleted.setVisibility(View.GONE);
            holder.tvChoiceNum.setTextColor(context.getColor(R.color.white));
            holder.tvSlash.setTextColor(context.getColor(R.color.main_color));
            holder.tvHeadCount.setTextColor(context.getColor(R.color.main_color));
        }
        holder.tvChoiceNum.setText(String.valueOf(recruitPostList.get(position).getChoiceNum()));
        holder.tvHeadCount.setText(String.valueOf(recruitPostList.get(position).getHeadCount()));
    }

    @Override
    public int getItemCount() {
        return recruitPostList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llitem;
        TextView tvWriterNick, tvRecruitIng, tvRecruitCompleted, tvChoiceNum, tvSlash, tvHeadCount;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            llitem = itemView.findViewById(R.id.ll_item_recruit_bg);
            tvWriterNick = itemView.findViewById(R.id.tv_item_recruit_nick);
            tvRecruitIng = itemView.findViewById(R.id.tv_item_recruit_ing);
            tvRecruitCompleted = itemView.findViewById(R.id.tv_item_recruit_completed);
            tvChoiceNum = itemView.findViewById(R.id.tv_item_recruit_choice);
            tvSlash = itemView.findViewById(R.id.tv_item_recruit_slash);
            tvHeadCount = itemView.findViewById(R.id.tv_item_recruit_headcount);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION && !recruitPostList.get(pos).isCompleted()) {
                    if (myInfo.getUserId().equals(recruitPostList.get(pos).getWriterInfo().getUserId())) {      //작성자이므로 참여 수락을 할 수 있는 다이얼로그 보임
                        ArrayList<RecruitRequestInfo> userRequestList = new ArrayList<>();
                        //TODO: 임시
                        userRequestList.add(new RecruitRequestInfo(new UserEssentialInfo(2L, "닉네임2"), false));
                        userRequestList.add(new RecruitRequestInfo(new UserEssentialInfo(5L, "닉네임5"), true));
                        userRequestList.add(new RecruitRequestInfo(new UserEssentialInfo(6L, "닉네임6"), false));
                        userRequestList.add(new RecruitRequestInfo(new UserEssentialInfo(3L, "닉네임3"), false));
                        userRequestList.add(new RecruitRequestInfo(new UserEssentialInfo(11L, "닉네임11"), true));
                        userRequestList.add(new RecruitRequestInfo(new UserEssentialInfo(9L, "닉네임9"), false));
                        //

                        //TODO: 서버로부터 참여 정보 받음
                        View requestDialogView = View.inflate(context, R.layout.dialog_recruit_request, null);

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setView(requestDialogView);
                        AlertDialog alertDialog = builder.create();
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        alertDialog.show();

                        WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
                        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
                        Point size = new Point();
                        display.getRealSize(size);
                        int width = size.x;
                        params.width = (int) (width*0.89);
                        alertDialog.getWindow().setAttributes(params);

                        RecyclerView rvRecruitRequest = requestDialogView.findViewById(R.id.rv_dialog_request);
                        if (userRequestList.size() > 3) {
                            int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, context.getResources().getDisplayMetrics());
                            LinearLayout.LayoutParams rvRecruitRequestParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
                            rvRecruitRequest.setLayoutParams(rvRecruitRequestParams);

                        }
                        LinearLayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false);
                        rvRecruitRequest.setLayoutManager(manager);
                        RecruitRequestRecyclerAdapter recruitRequestRecyclerAdapter = new RecruitRequestRecyclerAdapter(userRequestList);
                        rvRecruitRequest.setAdapter(recruitRequestRecyclerAdapter);

                        AppCompatButton btRequestYes = requestDialogView.findViewById(R.id.bt_dialog_request_yes);
                        btRequestYes.setOnClickListener(view -> alertDialog.dismiss());

                        AppCompatButton btRequestConfirmed = requestDialogView.findViewById(R.id.bt_dialog_request_confirmed);
                        btRequestConfirmed.setOnClickListener(view -> {
                            //TODO: 서버에 멤버 확정을 전달함
                            alertDialog.dismiss();
                        });

                        AppCompatButton btRequestNo = requestDialogView.findViewById(R.id.bt_dialog_request_no);
                        btRequestNo.setOnClickListener(view -> {
                            //TODO: 모집글 삭제를 서버에 전달함!(update 해야 함!)
                            alertDialog.dismiss();
                        });
                    }
                    else {  //참여 다이얼로그가 뜸
                        View participateDialogView = View.inflate(context, R.layout.dialog_recruit_participate, null);

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setView(participateDialogView);
                        AlertDialog alertDialog = builder.create();
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        alertDialog.show();

                        WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
                        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
                        Point size = new Point();
                        display.getRealSize(size);
                        int width = size.x;
                        params.width = (int) (width*0.89);
                        alertDialog.getWindow().setAttributes(params);

                        AppCompatButton btParticipateYes = participateDialogView.findViewById(R.id.bt_dialog_participate_yes);
                        btParticipateYes.setOnClickListener(view -> {
                            //TODO: 서버에 참여 요청을 전달함
                            alertDialog.dismiss();
                        });

                        AppCompatButton btParticipateNo = participateDialogView.findViewById(R.id.bt_dialog_participate_no);
                        btParticipateNo.setOnClickListener(view -> alertDialog.dismiss());
                    }
                }
            });

            tvWriterNick.setOnClickListener(v -> {
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

                //TODO: 서버로부터 사용자의 정보 받아옴
                //TODO: 임시
                //
                ArrayList<Genre> interestGenreList = new ArrayList<>();
                interestGenreList.add(Genre.DRAMA);
                interestGenreList.add(Genre.FANTASY);
                UserInfo writerInfo = new UserInfo(1L, "오뜨유", 7, false, interestGenreList);
                //

                AppCompatImageButton ibtCancel = profileDialogView.findViewById(R.id.ibt_dialog_profile_cancel);
                ibtCancel.setOnClickListener(view -> alertDialog.dismiss());

                TextView tvUserNick = profileDialogView.findViewById(R.id.tv_dialog_profile_nick);
                tvUserNick.setText(writerInfo.getNick());

                ProgressBar pbOttULevel = profileDialogView.findViewById(R.id.pb_dialog_profile);
                TextView tvOttULevel = profileDialogView.findViewById(R.id.tv_dialog_profile_level);

                pbOttULevel.setProgress(writerInfo.getLevel());
                tvOttULevel.setText(String.valueOf(writerInfo.getLevel()));
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

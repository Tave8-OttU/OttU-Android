package com.tave8.ottu.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tave8.ottu.LoginActivity;
import com.tave8.ottu.MainActivity;
import com.tave8.ottu.OttURetrofitClient;
import com.tave8.ottu.PreferenceManager;
import com.tave8.ottu.R;
import com.tave8.ottu.data.Notice;
import com.tave8.ottu.data.UserEssentialInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tave8.ottu.MainActivity.myInfo;

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
        if (noticeList.get(position).getIsEvaluated())
            holder.llNotice.setBackgroundColor(context.getColor(R.color.bg_black_color1_4));
        else
            holder.llNotice.setBackgroundColor(context.getColor(R.color.bg_black_color2_4));
        holder.tvContent.setText(noticeList.get(position).getContent());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        holder.tvCreatedDate.setText(noticeList.get(position).getCreatedDate().format(dateTimeFormatter));
    }

    @Override
    public int getItemCount() {
        return noticeList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llNotice;
        TextView tvContent, tvCreatedDate;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            llNotice = itemView.findViewById(R.id.ll_item_notice);
            tvContent = itemView.findViewById(R.id.tv_item_notice_content);
            tvCreatedDate = itemView.findViewById(R.id.tv_item_notice_date);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    if (noticeList.get(pos).getEvaluateTeamIdx() != null && !noticeList.get(pos).getIsEvaluated()) {
                        //TODO: 서버에 보냄
                        OttURetrofitClient.getApiService().getTeamForEvaluation(PreferenceManager.getString(context, "jwt"), noticeList.get(pos).getEvaluateTeamIdx(), myInfo.getUserIdx()).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                                if (response.code() == 200) {
                                    try {
                                        ArrayList<UserEssentialInfo> memberList = new ArrayList<>();

                                        JSONObject result = new JSONObject(Objects.requireNonNull(response.body()));
                                        JSONArray members = result.getJSONArray("userlist");
                                        for (int i=0; i<members.length(); i++) {
                                            JSONObject member = members.getJSONObject(i);
                                            Long userIdx = member.getLong("userIdx");
                                            String nickname = member.getString("nickname");
                                            memberList.add(new UserEssentialInfo(userIdx, nickname));
                                        }
                                        memberList.sort((o1, o2) -> o1.getUserIdx().compareTo(o2.getUserIdx()));    //userIdx 순으로 오름차순 정렬

                                        showEvaluationDialog(pos, memberList);
                                    } catch (JSONException e) { e.printStackTrace(); }
                                }
                                else if (response.code() == 401) {
                                    Toast.makeText(context, "로그인 기한이 만료되어\n 로그인 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
                                    PreferenceManager.removeKey(context, "jwt");
                                    Intent reLogin = new Intent(context, LoginActivity.class);
                                    reLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    context.startActivity(reLogin);
                                    ((MainActivity) context).finish();
                                }
                                else
                                    Toast.makeText(context, "팀원 신뢰도 평가에 문제가 생겼습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                                Toast.makeText(context, "서버와 연결되지 않았습니다. 확인해 주세요.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }

        private void showEvaluationDialog(int pos, ArrayList<UserEssentialInfo> memberList) {
            View evaluationDialogView = View.inflate(context, R.layout.dialog_team_evaluation, null);

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setView(evaluationDialogView);
            AlertDialog alertDialogTeam = builder.create();
            alertDialogTeam.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            WindowManager.LayoutParams params = alertDialogTeam.getWindow().getAttributes();
            Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            Point size = new Point();
            display.getRealSize(size);
            int width = size.x;
            params.width = (int) (width*0.89);
            alertDialogTeam.getWindow().setAttributes(params);

            RecyclerView rvEvaluationMembers = evaluationDialogView.findViewById(R.id.rv_dialog_evaluation);
            LinearLayoutManager teamManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false);
            rvEvaluationMembers.setLayoutManager(teamManager);
            EvaluationRecyclerAdapter evaluationRecyclerAdapter = new EvaluationRecyclerAdapter(memberList);
            rvEvaluationMembers.setAdapter(evaluationRecyclerAdapter);

            alertDialogTeam.show();

            AppCompatButton btSubmit = evaluationDialogView.findViewById(R.id.bt_dialog_evaluation_submit);
            btSubmit.setOnClickListener(viewTeam -> {
                JsonObject requestData = new JsonObject();
                requestData.addProperty("userIdx", myInfo.getUserIdx());
                requestData.add("reliability", new Gson().toJsonTree(evaluationRecyclerAdapter.getReliabilityList()));
                OttURetrofitClient.getApiService().postTeamEvaluation(PreferenceManager.getString(context, "jwt"), noticeList.get(pos).getEvaluateTeamIdx(), requestData).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        if (response.code() == 200) {
                            noticeList.get(pos).setIsEvaluatedTrue();
                            alertDialogTeam.dismiss();
                            Toast.makeText(context, "팀원 평가를 성공적으로 반영했습니다.", Toast.LENGTH_SHORT).show();
                            notifyItemChanged(pos);
                        }
                        else if (response.code() == 401) {
                            Toast.makeText(context, "로그인 기한이 만료되어\n 로그인 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
                            PreferenceManager.removeKey(context, "jwt");
                            Intent reLogin = new Intent(context, LoginActivity.class);
                            reLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(reLogin);
                            ((MainActivity) context).finish();
                        }
                        else
                            Toast.makeText(context, "신뢰도 평가 제출에 문제가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        Toast.makeText(context, "서버와 연결되지 않았습니다. 확인해 주세요:)", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        }
    }
}

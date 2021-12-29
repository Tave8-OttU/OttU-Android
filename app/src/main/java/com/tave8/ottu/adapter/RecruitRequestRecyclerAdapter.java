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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.tave8.ottu.LoginActivity;
import com.tave8.ottu.MyRecruitActivity;
import com.tave8.ottu.OttURetrofitClient;
import com.tave8.ottu.PreferenceManager;
import com.tave8.ottu.R;
import com.tave8.ottu.RecruitActivity;
import com.tave8.ottu.data.Genre;
import com.tave8.ottu.data.RecruitInfo;
import com.tave8.ottu.data.RecruitRequestInfo;
import com.tave8.ottu.data.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecruitRequestRecyclerAdapter extends RecyclerView.Adapter<RecruitRequestRecyclerAdapter.ItemViewHolder> {
    private boolean isMyRecruitRequest = false;
    private RecruitInfo recruitInfo;
    private Context context;
    private ArrayList<RecruitRequestInfo> requestUserList;
    private RecruitRecyclerAdapter.ItemViewHolder recruitItemViewHolder = null;
    private MyRecruitRecyclerAdapter.ItemViewHolder myRecruitItemViewHolder = null;

    public RecruitRequestRecyclerAdapter(ArrayList<RecruitRequestInfo> requestUserList, RecruitRecyclerAdapter.ItemViewHolder recruitItemViewHolder, RecruitInfo recruitInfo) {
        this.requestUserList = requestUserList;
        this.recruitItemViewHolder = recruitItemViewHolder;
        this.recruitInfo = recruitInfo;
    }

    public RecruitRequestRecyclerAdapter(ArrayList<RecruitRequestInfo> requestUserList, MyRecruitRecyclerAdapter.ItemViewHolder myRecruitItemViewHolder, RecruitInfo recruitInfo) {
        isMyRecruitRequest = true;
        this.requestUserList = requestUserList;
        this.myRecruitItemViewHolder = myRecruitItemViewHolder;
        this.recruitInfo = recruitInfo;
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
            JsonObject requestData = new JsonObject();
            requestData.addProperty("waitlistIdx", requestUserList.get(position).getWaitlistIdx());
            OttURetrofitClient.getApiService().patchWaitlistAccept(PreferenceManager.getString(context, "jwt"), requestData).enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if (response.code() == 200) {
                        if (isMyRecruitRequest)
                            myRecruitItemViewHolder.updateRequestInfo(recruitInfo.getRecruitIdx(), recruitInfo.getHeadCount());
                        else
                            recruitItemViewHolder.updateRequestInfo(recruitInfo.getRecruitIdx(), recruitInfo.getHeadCount());
                    }
                    else if (response.code() == 401) {
                        Toast.makeText(context, "로그인 기한이 만료되어\n 로그인 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
                        PreferenceManager.removeKey(context, "jwt");
                        Intent reLogin = new Intent(context, LoginActivity.class);
                        reLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(reLogin);
                        if (isMyRecruitRequest)
                            ((MyRecruitActivity) context).finish();
                        else
                            ((RecruitActivity) context).finish();
                    }
                    else
                        Toast.makeText(context, "해당 모집글 참여 수락에 문제가 생겼습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    Toast.makeText(context, "서버와 연결되지 않았습니다. 확인해 주세요.", Toast.LENGTH_SHORT).show();
                }
            });
        });

        holder.btRequestRefuse.setOnClickListener(v -> {
            JsonObject requestData = new JsonObject();
            requestData.addProperty("waitlistIdx", requestUserList.get(position).getWaitlistIdx());
            OttURetrofitClient.getApiService().patchWaitlistCancel(PreferenceManager.getString(context, "jwt"), requestData).enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if (response.code() == 200) {
                        if (isMyRecruitRequest)
                            myRecruitItemViewHolder.updateRequestInfo(recruitInfo.getRecruitIdx(), recruitInfo.getHeadCount());
                        else
                            recruitItemViewHolder.updateRequestInfo(recruitInfo.getRecruitIdx(), recruitInfo.getHeadCount());
                    }
                    else if (response.code() == 401) {
                        Toast.makeText(context, "로그인 기한이 만료되어\n 로그인 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
                        PreferenceManager.removeKey(context, "jwt");
                        Intent reLogin = new Intent(context, LoginActivity.class);
                        reLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(reLogin);
                        if (isMyRecruitRequest)
                            ((MyRecruitActivity) context).finish();
                        else
                            ((RecruitActivity) context).finish();
                    }
                    else
                        Toast.makeText(context, "해당 모집글 참여 수락 취소에 문제가 생겼습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    Toast.makeText(context, "서버와 연결되지 않았습니다. 확인해 주세요.", Toast.LENGTH_SHORT).show();
                }
            });
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
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    View profileDialogView = View.inflate(context, R.layout.dialog_user_profile, null);

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setView(profileDialogView);
                    final AlertDialog alertDialog = builder.create();
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
                    Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
                    Point size = new Point();
                    display.getRealSize(size);
                    int width = size.x;
                    params.width = (int) (width*0.89);
                    alertDialog.getWindow().setAttributes(params);

                    AppCompatImageButton ibtCancel = profileDialogView.findViewById(R.id.ibt_dialog_profile_cancel);
                    ibtCancel.setOnClickListener(view -> alertDialog.dismiss());

                    TextView tvUserNick = profileDialogView.findViewById(R.id.tv_dialog_profile_nick);
                    TextView tvOttULevel = profileDialogView.findViewById(R.id.tv_dialog_profile_level);
                    ProgressBar pbOttULevel = profileDialogView.findViewById(R.id.pb_dialog_profile);
                    AppCompatButton btGenre1 = profileDialogView.findViewById(R.id.bt_dialog_profile_genre1);
                    AppCompatButton btGenre2 = profileDialogView.findViewById(R.id.bt_dialog_profile_genre2);
                    AppCompatButton btGenre3 = profileDialogView.findViewById(R.id.bt_dialog_profile_genre3);

                    Long userIdx = requestUserList.get(pos).getRequesterInfo().getUserIdx();
                    OttURetrofitClient.getApiService().getUser(PreferenceManager.getString(context, "jwt"), userIdx).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                            if (response.code() == 200) {
                                try {
                                    JSONObject userInfo = new JSONObject(Objects.requireNonNull(response.body()));
                                    JSONObject user = userInfo.getJSONObject("user");

                                    ArrayList<Genre> interestGenreList = new ArrayList<>();
                                    JSONArray genres = user.getJSONArray("genres");
                                    for (int i=0; i<genres.length(); i++) {
                                        JSONObject genre = genres.getJSONObject(i);
                                        interestGenreList.add(new Genre(genre.getInt("genreIdx"), genre.getString("genreName")));
                                    }
                                    UserInfo otherUserInfo = new UserInfo(userIdx, user.getString("nickname"), user.getInt("reliability"),
                                            user.getBoolean("isFirst"), interestGenreList);

                                    tvUserNick.setText(otherUserInfo.getNick());

                                    pbOttULevel.setProgress(otherUserInfo.getReliability());
                                    tvOttULevel.setText(String.valueOf(otherUserInfo.getReliability()));
                                    if (otherUserInfo.isFirst()) {
                                        pbOttULevel.setProgressDrawable(AppCompatResources.getDrawable(context, R.drawable.bg_progress_first));
                                        tvOttULevel.setTextColor(context.getColor(R.color.sub_text_color));
                                    }

                                    if (otherUserInfo.getInterestGenre().size() == 1) {
                                        btGenre1.setText(otherUserInfo.getInterestGenre().get(0).getGenreName());
                                        btGenre2.setVisibility(View.INVISIBLE);
                                        btGenre3.setVisibility(View.INVISIBLE);
                                    } else if (otherUserInfo.getInterestGenre().size() == 2) {
                                        btGenre1.setText(otherUserInfo.getInterestGenre().get(0).getGenreName());
                                        btGenre2.setText(otherUserInfo.getInterestGenre().get(1).getGenreName());
                                        btGenre3.setVisibility(View.INVISIBLE);
                                    } else if (otherUserInfo.getInterestGenre().size() == 3) {
                                        btGenre1.setText(otherUserInfo.getInterestGenre().get(0).getGenreName());
                                        btGenre2.setText(otherUserInfo.getInterestGenre().get(1).getGenreName());
                                        btGenre3.setText(otherUserInfo.getInterestGenre().get(2).getGenreName());
                                    }
                                    alertDialog.show();
                                } catch (JSONException e) { e.printStackTrace(); }
                            }
                            else if (response.code() == 401) {
                                Toast.makeText(context, "로그인 기한이 만료되어\n 로그인 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
                                PreferenceManager.removeKey(context, "jwt");
                                Intent reLogin = new Intent(context, LoginActivity.class);
                                reLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(reLogin);
                                if (isMyRecruitRequest)
                                    ((MyRecruitActivity) context).finish();
                                else
                                    ((RecruitActivity) context).finish();
                            }
                            else
                                Toast.makeText(context, "회원 정보 불러오기에 문제가 생겼습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                            Toast.makeText(context, "서버와 연결되지 않았습니다. 확인해 주세요.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }
}

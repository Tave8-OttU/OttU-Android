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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.tave8.ottu.LoginActivity;
import com.tave8.ottu.MyOTTActivity;
import com.tave8.ottu.OttURetrofitClient;
import com.tave8.ottu.PreferenceManager;
import com.tave8.ottu.R;
import com.tave8.ottu.data.PaymentInfo;
import com.tave8.ottu.data.RatePlanInfo;
import com.tave8.ottu.data.SingletonPlatform;

import java.text.DecimalFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OttPaymentRecyclerAdapter extends RecyclerView.Adapter<OttPaymentRecyclerAdapter.ItemViewHolder> {
    private Context context;
    private ArrayList<PaymentInfo> ottPaymentList;

    public OttPaymentRecyclerAdapter(ArrayList<PaymentInfo> ottPaymentList) {
        this.ottPaymentList = ottPaymentList;
    }

    @NonNull
    @Override
    public OttPaymentRecyclerAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_mypage_ott, parent, false);
        OttPaymentRecyclerAdapter.ItemViewHolder viewHolder = new OttPaymentRecyclerAdapter.ItemViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OttPaymentRecyclerAdapter.ItemViewHolder holder, int position) {
        int imageResourceId = SingletonPlatform.getPlatform().getPlatformLogoList().get(ottPaymentList.get(position).getPlatformIdx());
        holder.ivPlatform.setImageResource(imageResourceId);
    }

    @Override
    public int getItemCount() {
        return ottPaymentList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPlatform;
        AppCompatImageButton ibtOttNext;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            ivPlatform = itemView.findViewById(R.id.iv_item_mypage_platform);
            ibtOttNext = itemView.findViewById(R.id.ibt_item_mypage_ott_next);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    PaymentInfo paymentInfo = ottPaymentList.get(pos);
                    RatePlanInfo ratePlanInfo = SingletonPlatform.getPlatform().getPlatformRatePlanInfo(paymentInfo.getPlatformIdx(), paymentInfo.getHeadCount());

                    View paymentDialogView = View.inflate(context, R.layout.dialog_ott_payment_info, null);

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setView(paymentDialogView);
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

                    ImageView ivPaymentPlatform = paymentDialogView.findViewById(R.id.iv_dialog_payment_platform);
                    ivPaymentPlatform.setImageResource(SingletonPlatform.getPlatform().getPlatformLogoList().get(paymentInfo.getPlatformIdx()));

                    TextView tvPaymentDay = paymentDialogView.findViewById(R.id.tv_dialog_payment_date);
                    tvPaymentDay.setText(String.valueOf(paymentInfo.getPaymentDay()));

                    DecimalFormat chargeFormatter = new DecimalFormat("###,##0");
                    TextView tvRatePlan = paymentDialogView.findViewById(R.id.tv_dialog_payment_rateplan);
                    tvRatePlan.setText(ratePlanInfo.getRatePlanName());
                    TextView tvRatePlanCharge = paymentDialogView.findViewById(R.id.tv_dialog_payment_charge);
                    tvRatePlanCharge.setText(chargeFormatter.format(ratePlanInfo.getCharge()));

                    TextView tvTeamName = paymentDialogView.findViewById(R.id.tv_dialog_payment_team);
                    tvTeamName.setText(paymentInfo.getTeamName());

                    TextView tvTeamNum = paymentDialogView.findViewById(R.id.tv_dialog_payment_team_num);
                    tvTeamNum.setText(String.valueOf(paymentInfo.getHeadCount()));
                    TextView tvMyCharge = paymentDialogView.findViewById(R.id.tv_dialog_payment_my_charge);
                    int myCharge = ratePlanInfo.getCharge()/(paymentInfo.getHeadCount());
                    tvMyCharge.setText(chargeFormatter.format(myCharge));

                    AppCompatButton btPaymentOk = paymentDialogView.findViewById(R.id.bt_dialog_payment_ok);
                    btPaymentOk.setOnClickListener(view -> alertDialog.dismiss());

                    AppCompatButton btPaymentTerminate = paymentDialogView.findViewById(R.id.bt_dialog_payment_terminate);
                    btPaymentTerminate.setOnClickListener(view -> {
                        OttURetrofitClient.getApiService().deleteTeam(PreferenceManager.getString(context, "jwt"), paymentInfo.getPaymentIdx()).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                                if (response.code() == 200) {
                                    alertDialog.dismiss();
                                    Toast.makeText(context, "OTT 서비스 해지에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                    ((MyOTTActivity) context).updateMyOTTList();
                                }
                                else if (response.code() == 401) {
                                    Toast.makeText(context, "로그인 기한이 만료되어\n 로그인 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
                                    PreferenceManager.removeKey(context, "jwt");
                                    Intent reLogin = new Intent(context, LoginActivity.class);
                                    reLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    context.startActivity(reLogin);
                                    ((MyOTTActivity) context).finish();
                                }
                                else
                                    Toast.makeText(context, "해당 OTT 서비스 해지에 문제가 생겼습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                                Toast.makeText(context, "서버와 연결되지 않았습니다. 확인해 주세요.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    });
                }
            });
        }
    }
}

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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.tave8.ottu.R;
import com.tave8.ottu.data.PaymentInfo;
import com.tave8.ottu.data.RatePlanInfo;
import com.tave8.ottu.data.SingletonPlatform;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class HomePaymentPagerAdapter extends RecyclerView.Adapter<HomePaymentPagerAdapter.ItemViewHolder> {
    private Context context;
    private ArrayList<PaymentInfo> ottPaymentList;

    public HomePaymentPagerAdapter(ArrayList<PaymentInfo> ottPaymentList) {
        this.ottPaymentList = ottPaymentList;
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
        holder.ivOttIcon.setImageResource(SingletonPlatform.getPlatform().getPlatformLogoList().get(ottPaymentList.get(position).getPlatformId()));
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        holder.tvOttPaymentDate.setText(ottPaymentList.get(position).getPaymentDate().format(dateTimeFormatter));
    }

    @Override
    public int getItemCount() {
        return ottPaymentList.size();
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
                    PaymentInfo paymentInfo = ottPaymentList.get(pos);
                    RatePlanInfo ratePlanInfo = SingletonPlatform.getPlatform().getPlatformRatePlanInfo(paymentInfo.getPlatformId(), paymentInfo.getHeadCount());

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
                    ivPaymentPlatform.setImageResource(SingletonPlatform.getPlatform().getPlatformLogoList().get(paymentInfo.getPlatformId()));

                    TextView tvPaymentDay = paymentDialogView.findViewById(R.id.tv_dialog_payment_date);
                    tvPaymentDay.setText(String.valueOf(paymentInfo.getPaymentDate().getDayOfMonth()));

                    DecimalFormat chargeFormatter = new DecimalFormat("###,##0");
                    TextView tvRatePlan = paymentDialogView.findViewById(R.id.tv_dialog_payment_rateplan);
                    tvRatePlan.setText(ratePlanInfo.getRatePlanName());
                    TextView tvRatePlanCharge = paymentDialogView.findViewById(R.id.tv_dialog_payment_charge);
                    tvRatePlanCharge.setText(chargeFormatter.format(ratePlanInfo.getCharge()));

                    TextView tvTeamNum = paymentDialogView.findViewById(R.id.tv_dialog_payment_team_num);
                    tvTeamNum.setText(String.valueOf(paymentInfo.getHeadCount()));
                    TextView tvMyCharge = paymentDialogView.findViewById(R.id.tv_dialog_payment_my_charge);
                    int myCharge = ratePlanInfo.getCharge()/(paymentInfo.getHeadCount());
                    tvMyCharge.setText(chargeFormatter.format(myCharge));

                    AppCompatButton btPaymentOk = paymentDialogView.findViewById(R.id.bt_dialog_payment_ok);
                    btPaymentOk.setOnClickListener(view -> alertDialog.dismiss());

                    AppCompatButton btPaymentTerminate = paymentDialogView.findViewById(R.id.bt_dialog_payment_terminate);
                    btPaymentTerminate.setOnClickListener(view -> {
                        //TODO: 요금 결제 해지를 서버에 전달함!(update 해야 함!)
                        alertDialog.dismiss();
                    });
                }
            });
        }
    }
}

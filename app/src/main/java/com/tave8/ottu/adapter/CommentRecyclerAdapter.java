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
import com.tave8.ottu.data.CommentInfo;
import com.tave8.ottu.data.Genre;
import com.tave8.ottu.data.UserInfo;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static com.tave8.ottu.MainActivity.myInfo;

public class CommentRecyclerAdapter extends RecyclerView.Adapter<CommentRecyclerAdapter.ItemViewHolder> {
    private Context context;
    private ArrayList<CommentInfo> commentList;

    public CommentRecyclerAdapter(ArrayList<CommentInfo> commentList) {
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentRecyclerAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_comment, parent, false);
        CommentRecyclerAdapter.ItemViewHolder viewHolder = new CommentRecyclerAdapter.ItemViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentRecyclerAdapter.ItemViewHolder holder, int position) {
        holder.tvWriterNick.setText(commentList.get(position).getWriterInfo().getNick());
        holder.tvCommentTime.setText(commentList.get(position).getCommentDateTime().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss")));
        holder.tvContent.setText(commentList.get(position).getContent().replace(" ", "\u00A0"));

        if (myInfo.getUserId() == commentList.get(position).getWriterInfo().getUserId())
            holder.ibtDelete.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageButton ibtDelete;
        TextView tvWriterNick, tvCommentTime, tvContent;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            tvWriterNick = itemView.findViewById(R.id.tv_item_comment_nick);
            tvCommentTime = itemView.findViewById(R.id.tv_item_comment_datetime);
            tvContent = itemView.findViewById(R.id.tv_item_comment_content);
            ibtDelete = itemView.findViewById(R.id.ibt_item_comment_delete);

            tvWriterNick.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
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

                    //TODO: 서버로부터 사용자의 정보 받아옴(communityPostInfoList.get(pos).getWriterInfo().getUserId() 전달)
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
                }
            });

            ibtDelete.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    View deleteCommentDialogView = View.inflate(context, R.layout.dialog_delete_comment, null);

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setView(deleteCommentDialogView);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    alertDialog.show();

                    WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
                    Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
                    Point size = new Point();
                    display.getRealSize(size);
                    int width = size.x;
                    params.width = (int) (width*0.75);
                    alertDialog.getWindow().setAttributes(params);

                    AppCompatButton btDeleteCommentYes = deleteCommentDialogView.findViewById(R.id.bt_dialog_delete_comment_yes);
                    btDeleteCommentYes.setOnClickListener(view -> {
                        //TODO: 서버에 댓글 삭제함을 전달함
                    });

                    AppCompatButton btDeleteCommentNo = deleteCommentDialogView.findViewById(R.id.bt_dialog_delete_comment_no);
                    btDeleteCommentNo.setOnClickListener(view -> alertDialog.dismiss());
                }
            });
        }
    }
}

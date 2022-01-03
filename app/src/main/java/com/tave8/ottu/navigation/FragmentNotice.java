package com.tave8.ottu.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tave8.ottu.LoginActivity;
import com.tave8.ottu.MyRecruitActivity;
import com.tave8.ottu.OttURetrofitClient;
import com.tave8.ottu.PreferenceManager;
import com.tave8.ottu.R;
import com.tave8.ottu.adapter.NoticeRecyclerAdapter;
import com.tave8.ottu.adapter.RecruitRecyclerAdapter;
import com.tave8.ottu.data.Notice;
import com.tave8.ottu.data.RecruitInfo;
import com.tave8.ottu.data.UserEssentialInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tave8.ottu.MainActivity.myInfo;

public class FragmentNotice extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_notice, container, false);

        ArrayList<Notice> noticeList = new ArrayList<>();

        RecyclerView rvNotice = rootView.findViewById(R.id.rv_frag_notice);
        LinearLayoutManager manager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false);
        rvNotice.setLayoutManager(manager);
        NoticeRecyclerAdapter noticeRecyclerAdapter = new NoticeRecyclerAdapter(noticeList);
        rvNotice.setAdapter(noticeRecyclerAdapter);
        DividerItemDecoration devider = new DividerItemDecoration(requireContext(), 1);
        devider.setDrawable(Objects.requireNonNull(ResourcesCompat.getDrawable(getResources(), R.drawable.item_divide_bar, null)));
        rvNotice.addItemDecoration(devider);

        //서버로부터 Notice 받아옴
        OttURetrofitClient.getApiService().getMyNoticeList(PreferenceManager.getString(requireContext(), "jwt"), myInfo.getUserIdx()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.code() == 200) {
                    try {
                        JSONObject result = new JSONObject(Objects.requireNonNull(response.body()));
                        JSONArray jsonNoticeList = result.getJSONArray("noticelist");
                        for (int i=0; i<jsonNoticeList.length(); i++) {
                            JSONObject notice = jsonNoticeList.getJSONObject(i);
                            Long noticeIdx = notice.getLong("noticeIdx");
                            String content = notice.getString("content");
                            String createdDate = notice.getString("createdDate");

                            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                            noticeList.add(new Notice(noticeIdx, content, LocalDateTime.parse(createdDate, dateTimeFormatter)));
                        }
                        noticeRecyclerAdapter.notifyDataSetChanged();
                    } catch (JSONException e) { e.printStackTrace(); }
                }
                else if (response.code() == 401) {
                    Toast.makeText(requireContext(), "로그인 기한이 만료되어\n 로그인 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
                    PreferenceManager.removeKey(requireContext(), "jwt");
                    Intent reLogin = new Intent(requireContext(), LoginActivity.class);
                    reLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(reLogin);
                    requireActivity().finish();
                }
                else
                    Toast.makeText(requireContext(), "내가 쓴 모집글 로드에 문제가 생겼습니다. 새로 고침을 해주세요.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Toast.makeText(requireContext(), "서버와 연결되지 않았습니다. 확인해 주세요:)", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }
}

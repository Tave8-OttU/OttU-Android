package com.tave8.ottu.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.tave8.ottu.LoginActivity;
import com.tave8.ottu.MyOTTActivity;
import com.tave8.ottu.OttURetrofitClient;
import com.tave8.ottu.PreferenceManager;
import com.tave8.ottu.R;
import com.tave8.ottu.adapter.HomeCommunityRecyclerAdapter;
import com.tave8.ottu.adapter.HomePaymentPagerAdapter;
import com.tave8.ottu.data.PaymentInfo;
import com.tave8.ottu.data.SimpleCommunityInfo;
import com.tave8.ottu.data.SingletonPlatform;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

import me.relex.circleindicator.CircleIndicator3;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.tave8.ottu.MainActivity.myInfo;

public class FragmentHome extends Fragment {
    private ArrayList<PaymentInfo> ottPaymentList = null;
    private ArrayList<SimpleCommunityInfo> ottCommunityList = null;

    private HomePaymentPagerAdapter homePaymentPagerAdapter;
    private HomeCommunityRecyclerAdapter homeCommunityRecyclerAdapter;
    private LinearLayout llNoPayment;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        ottPaymentList = new ArrayList<>();
        ottCommunityList = new ArrayList<>();

        ViewPager2 vpOttPayment = rootView.findViewById(R.id.vp_home_payment);
        homePaymentPagerAdapter = new HomePaymentPagerAdapter(ottPaymentList, this);
        vpOttPayment.setAdapter(homePaymentPagerAdapter);
        vpOttPayment.setOffscreenPageLimit(3);
        vpOttPayment.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);
        vpOttPayment.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        CircleIndicator3 ciOttPayment = rootView.findViewById(R.id.ci_home_payment);
        ciOttPayment.setViewPager(vpOttPayment);
        homePaymentPagerAdapter.registerAdapterDataObserver(ciOttPayment.getAdapterDataObserver());

        ActivityResultLauncher<Intent> startActivityResultOttAdding = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK)
                        updateMyUrgentOttList();
                });
        llNoPayment = rootView.findViewById(R.id.ll_home_payment_no);
        llNoPayment.setOnClickListener(v -> startActivityResultOttAdding.launch(new Intent(requireContext(), MyOTTActivity.class)));

        updateMyUrgentOttList();

        RecyclerView rvOttCommunity= rootView.findViewById(R.id.rv_home_community);
        LinearLayoutManager commuManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        rvOttCommunity.setLayoutManager(commuManager);
        homeCommunityRecyclerAdapter = new HomeCommunityRecyclerAdapter(ottCommunityList);
        rvOttCommunity.setAdapter(homeCommunityRecyclerAdapter);
        updateCurrentCommunityPost();

        return rootView;
    }

    public void updateMyUrgentOttList() {
        llNoPayment.setVisibility(View.GONE);
        OttURetrofitClient.getApiService().getMyUrgentOttList(PreferenceManager.getString(getContext(), "jwt"), myInfo.getUserIdx()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.code() == 200) {
                    ottPaymentList.clear();
                    try {
                        JSONObject result = new JSONObject(Objects.requireNonNull(response.body()));
                        JSONArray jsonOttList = result.getJSONArray("ottlist");
                        if (jsonOttList.length() == 0)
                            llNoPayment.setVisibility(View.VISIBLE);

                        for (int i=0; i<jsonOttList.length(); i++) {
                            JSONObject paymentOtt = jsonOttList.getJSONObject(i);
                            Long paymentIdx = paymentOtt.getLong("teamIdx");
                            int platformIdx = paymentOtt.getJSONObject("platform").getInt("platformIdx");
                            String teamName = paymentOtt.getString("teamName");

                            int headcount = paymentOtt.getInt("headcount");
                            int paymentDay = paymentOtt.getInt("paymentDay");
                            String paymentDate = paymentOtt.getString("paymentDate");
                            ottPaymentList.add(new PaymentInfo(paymentIdx, platformIdx, teamName, headcount, paymentDay, LocalDate.parse(paymentDate, DateTimeFormatter.ISO_DATE)));
                        }
                        homePaymentPagerAdapter.notifyDataSetChanged();
                    } catch (JSONException e) { e.printStackTrace(); }
                }
                else if (response.code() == 401) {
                    Toast.makeText(getContext(), "로그인 기한이 만료되어\n 로그인 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
                    PreferenceManager.removeKey(getContext(), "jwt");
                    Intent reLogin = new Intent(getContext(), LoginActivity.class);
                    reLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(reLogin);
                    requireActivity().finish();
                }
                else
                    Toast.makeText(getContext(), "나의 OTT 서비스 로드에 문제가 생겼습니다. 새로 고침을 해주세요.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "서버와 연결되지 않았습니다. 확인해 주세요:)", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateCurrentCommunityPost() {
        OttURetrofitClient.getApiService().getCurrentCommunityPostList(PreferenceManager.getString(getContext(), "jwt")).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.code() == 200) {
                    ottCommunityList.clear();
                    try {
                        JSONObject result = new JSONObject(Objects.requireNonNull(response.body()));
                        JSONArray jsonCurrentPostList = result.getJSONArray("postlist");
                        /*
                        for (int i=0; i<jsonCurrentPostList.length(); i++) {
                            JSONObject ottCurrentPost = jsonCurrentPostList.getJSONObject(i);

                            int platformIdx = ottCurrentPost.getJSONObject("platform").getInt("platformIdx");
                            String platformName = SingletonPlatform.getPlatform().getPlatformEnglishNameList().get(platformIdx);
                            String content = ottCurrentPost.getString("content");

                            ottCommunityList.add(new SimpleCommunityInfo(platformIdx, platformName, content));
                        }

                        for (int i=jsonCurrentPostList.length(); i<SingletonPlatform.getPlatform().getPlatformNum(); i++) {
                            int platformIdx = i+1;
                            String platformName = SingletonPlatform.getPlatform().getPlatformEnglishNameList().get(platformIdx);
                            String content = "게시글이 존재하지 않음";

                            ottCommunityList.add(new SimpleCommunityInfo(platformIdx, platformName, content));
                        }
                        
                         */

                        if (jsonCurrentPostList.length() < SingletonPlatform.getPlatform().getPlatformNum()) {
                            //중간에 게시글이 없는 플랫폼 커뮤니티가 존재
                            for (int i=0; i<jsonCurrentPostList.length(); i++) {
                                JSONObject ottCurrentPost = jsonCurrentPostList.getJSONObject(i);
                                int platformIdx = ottCurrentPost.getJSONObject("platform").getInt("platformIdx");

                                int pos = ottCommunityList.size();
                                if (platformIdx != 1 && pos+1 != platformIdx) {
                                    //이전에 게시글이 없는 플랫폼 커뮤니티가 존재함!
                                    for (int j=1; j+pos<platformIdx; j++) {
                                        ottCommunityList.add(new SimpleCommunityInfo(pos+1, SingletonPlatform.getPlatform().getPlatformEnglishNameList().get(pos+1), "게시글이 존재하지 않음"));
                                    }
                                }

                                String platformName = SingletonPlatform.getPlatform().getPlatformEnglishNameList().get(platformIdx);
                                String content = ottCurrentPost.getString("content");

                                ottCommunityList.add(new SimpleCommunityInfo(platformIdx, platformName, content));
                            }
                            //전달받은 플랫폼 이후의 글 없는 플랫폼들 저장
                            for (int i=ottCommunityList.size(); i<SingletonPlatform.getPlatform().getPlatformNum(); i++) {
                                int platformIdx = i+1;
                                String platformName = SingletonPlatform.getPlatform().getPlatformEnglishNameList().get(platformIdx);
                                String content = "게시글이 존재하지 않음";

                                ottCommunityList.add(new SimpleCommunityInfo(platformIdx, platformName, content));
                            }
                        }
                        else {
                            for (int i=0; i<jsonCurrentPostList.length(); i++) {
                                JSONObject ottCurrentPost = jsonCurrentPostList.getJSONObject(i);

                                int platformIdx = ottCurrentPost.getJSONObject("platform").getInt("platformIdx");
                                String platformName = SingletonPlatform.getPlatform().getPlatformEnglishNameList().get(platformIdx);
                                String content = ottCurrentPost.getString("content");

                                ottCommunityList.add(new SimpleCommunityInfo(platformIdx, platformName, content));
                            }
                        }

                        homeCommunityRecyclerAdapter.notifyDataSetChanged();
                    } catch (JSONException e) { e.printStackTrace(); }
                }
                else if (response.code() == 401) {
                    Toast.makeText(getContext(), "로그인 기한이 만료되어\n 로그인 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
                    PreferenceManager.removeKey(getContext(), "jwt");
                    Intent reLogin = new Intent(getContext(), LoginActivity.class);
                    reLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(reLogin);
                    requireActivity().finish();
                }
                else
                    Toast.makeText(getContext(), "커뮤니티 최신 글 로드에 문제가 생겼습니다. 새로 고침을 해주세요.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "서버와 연결되지 않았습니다. 확인해 주세요:)", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

package com.tave8.ottu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.gson.JsonObject;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.tave8.ottu.data.Genre;
import com.tave8.ottu.data.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tave8.ottu.MainActivity.myInfo;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AppCompatButton btLogin = findViewById(R.id.bt_login);
        btLogin.setEnabled(false);

        if (!PreferenceManager.getString(LoginActivity.this, "jwt").equals("")) {
            OttURetrofitClient.getApiService().postAutoLogin(PreferenceManager.getString(LoginActivity.this, "jwt")).enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if (response.code() == 200) {
                        try {
                            JSONObject loginInfo = new JSONObject(Objects.requireNonNull(response.body()));
                            long userIdx = loginInfo.getJSONObject("user").getLong("userIdx");

                            if (loginInfo.getJSONObject("user").isNull("nickname")) {
                                Intent showInitialSetting = new Intent(LoginActivity.this, InitialSettingActivity.class);
                                Bundle bundle = new Bundle();
                                    bundle.putLong("userIdx", userIdx);
                                showInitialSetting.putExtras(bundle);
                                startActivity(showInitialSetting);
                            }
                            else {
                                ArrayList<Genre> interestGenreList = new ArrayList<>();
                                JSONObject user = loginInfo.getJSONObject("user");
                                JSONArray genres = user.getJSONArray("genres");
                                for (int i=0; i<genres.length(); i++) {
                                    JSONObject genre = genres.getJSONObject(i);
                                    interestGenreList.add(new Genre(genre.getInt("genreIdx"), genre.getString("genreName")));
                                }
                                myInfo = new UserInfo(userIdx, user.getString("kakaotalkId"), user.getString("nickname"), user.getInt("reliability"),
                                        user.getBoolean("isFirst"), interestGenreList);

                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            }
                            finish();
                        } catch (JSONException e) { e.printStackTrace(); }
                    }
                    else if (response.code() == 401) {
                        btLogin.setEnabled(true);
                        Toast.makeText(LoginActivity.this, "로그인 기한이 만료되어\n 로그인 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
                        PreferenceManager.removeKey(LoginActivity.this, "jwt");
                    }
                    else {
                        btLogin.setEnabled(true);
                        Toast.makeText(LoginActivity.this, "다시 로그인 부탁드립니다.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    btLogin.setEnabled(true);
                    Toast.makeText(LoginActivity.this, "서버와 연결되지 않았습니다. 확인해 주세요:)", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
            btLogin.setEnabled(true);


        btLogin.setOnClickListener(v -> {
            if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(this))
                UserApiClient.getInstance().loginWithKakaoTalk(this, kakaoLoginCallback);
            else
                UserApiClient.getInstance().loginWithKakaoAccount(this, kakaoLoginCallback);
        });
    }

    //카카오 로그인 콜백
    Function2<OAuthToken, Throwable, Unit> kakaoLoginCallback = (oAuthToken, throwable) -> {
        if (oAuthToken != null) {
            JsonObject kakaoToken = new JsonObject();
            kakaoToken.addProperty("access_token", oAuthToken.getAccessToken());
            OttURetrofitClient.getApiService().postLogin(kakaoToken).enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if (response.code() == 200) {
                        try {
                            JSONObject loginInfo = new JSONObject(Objects.requireNonNull(response.body()));
                            long userIdx = loginInfo.getJSONObject("user").getLong("userIdx");
                            PreferenceManager.setString(LoginActivity.this, "jwt", loginInfo.getString("jwt"));

                            if (loginInfo.getJSONObject("user").isNull("nickname")) {
                                Intent showInitialSetting = new Intent(LoginActivity.this, InitialSettingActivity.class);
                                Bundle bundle = new Bundle();
                                    bundle.putLong("userIdx", userIdx);
                                showInitialSetting.putExtras(bundle);
                                startActivity(showInitialSetting);
                            }
                            else {
                                ArrayList<Genre> interestGenreList = new ArrayList<>();
                                JSONObject user = loginInfo.getJSONObject("user");
                                JSONArray genres = user.getJSONArray("genres");
                                for (int i=0; i<genres.length(); i++) {
                                    JSONObject genre = genres.getJSONObject(i);
                                    interestGenreList.add(new Genre(genre.getInt("genreIdx"), genre.getString("genreName")));
                                }
                                myInfo = new UserInfo(userIdx, user.getString("kakaotalkId"), user.getString("nickname"), user.getInt("reliability"),
                                        user.getBoolean("isFirst"), interestGenreList);

                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            }
                            finish();
                        } catch (JSONException e) { e.printStackTrace(); }
                    }
                    else if (response.code() == 201) {
                        try {
                            JSONObject loginInfo = new JSONObject(Objects.requireNonNull(response.body()));
                            long userIdx = loginInfo.getJSONObject("user").getLong("userIdx");

                            Intent showInitialSetting = new Intent(LoginActivity.this, InitialSettingActivity.class);
                            Bundle bundle = new Bundle();
                                bundle.putLong("userIdx", userIdx);
                            showInitialSetting.putExtras(bundle);
                            startActivity(showInitialSetting);
                        } catch (JSONException e) { e.printStackTrace(); }
                    }
                    else
                        Toast.makeText(LoginActivity.this, "다시 로그인 부탁드립니다.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    Toast.makeText(LoginActivity.this, "서버와 연결되지 않았습니다. 확인해 주세요:)", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else if (throwable != null) {
            Toast.makeText(LoginActivity.this, "다시 로그인 부탁드립니다.", Toast.LENGTH_SHORT).show();
        }

        return null;
    };
}

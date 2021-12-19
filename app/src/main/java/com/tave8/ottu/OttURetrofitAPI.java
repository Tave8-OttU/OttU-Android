package com.tave8.ottu;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface OttURetrofitAPI {
    @POST("/auth/kakao")
    Call<String> postAutoLogin(@Header("authorization") String jwt);   //서버로 jwt 전달(자동로그인)

    @POST("/auth/kakao")
    Call<String> postLogin(@Body JsonObject kakaoToken);               //서버로 카카오 토큰 전달(일반로그인)

    @GET("/user/nickname/{nickname}")
    Call<String> getCheckNick(@Header("authorization") String jwt, @Path("nickname") String nick);
}

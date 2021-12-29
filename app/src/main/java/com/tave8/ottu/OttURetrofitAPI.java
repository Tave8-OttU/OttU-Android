package com.tave8.ottu;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface OttURetrofitAPI {
    @POST("/auth/kakao")
    Call<String> postAutoLogin(@Header("authorization") String jwt);   //서버로 jwt 전달(자동로그인)

    @POST("/auth/kakao")
    Call<String> postLogin(@Body JsonObject kakaoToken);               //서버로 카카오 토큰 전달(일반로그인)


    @GET("/user/{idx}")
    Call<String> getUser(@Header("authorization") String jwt, @Path("idx") Long userIdx);

    @GET("/user/nickname/{nickname}")
    Call<String> getCheckNick(@Header("authorization") String jwt, @Path("nickname") String nick);


    //TODO: 주소 변경 요망!!!!!!!!!!
    @GET("/recruit/my/list/{uid}")
    Call<String> getMyRecruitLists(@Header("authorization") String jwt, @Path("uid") Long userIdx);

    @GET("/recruit/list/{pid}")
    Call<String> getRecruitLists(@Header("authorization") String jwt, @Path("pid") int platformIdx);

    @POST("/recruit/upload")
    Call<String> postRecruitUpload(@Header("authorization") String jwt, @Body JsonObject request);

    //TODO: 확인 요망!!!!!!
    @GET("/recruit/{rid}")
    Call<String> getRecruit(@Header("authorization") String jwt, @Path("rid") Long recruitIdx);       //TODO: 정의 안했음

    @DELETE("/recruit/{rid}")
    Call<String> deleteRecruit(@Header("authorization") String jwt, @Path("rid") Long recruitIdx);

    @POST("/recruit/participate")
    Call<String> postRecruitParticipate(@Header("authorization") String jwt, @Body JsonObject request);

    //TODO: 확인 요망!!!!!!
    @POST("/recruit/confirm")
    Call<String> postRecruitConfirm(@Header("authorization") String jwt, @Body JsonObject request);

    @GET("/recruit/waitlist/{rid}")
    Call<String> getRecruitWaitlist(@Header("authorization") String jwt, @Path("rid") Long recruitIdx);

    @PATCH("/recruit/waitlist/accept")
    Call<String> patchWaitlistAccept(@Header("authorization") String jwt, @Body JsonObject request);

    @PATCH("/recruit/waitlist/cancel")
    Call<String> patchWaitlistCancel(@Header("authorization") String jwt, @Body JsonObject request);
}

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
import retrofit2.http.Query;

public interface OttURetrofitAPI {
    @POST("/auth/kakao")
    Call<String> postAutoLogin(@Header("authorization") String jwt);                                //서버로 jwt 전달(자동로그인)

    @POST("/auth/kakao")
    Call<String> postLogin(@Body JsonObject kakaoToken);                                            //서버로 카카오 토큰 전달(일반로그인)


    @GET("/user/{uid}")
    Call<String> getUser(@Header("authorization") String jwt, @Path("uid") Long userIdx);           //사용자 정보 조회

    @GET("/user/nickname/{nickname}")
    Call<String> getCheckNick(@Header("authorization") String jwt, @Path("nickname") String nick);  //닉네임 중복 확인

    @PATCH("/user/{uid}/notice-token")
    Call<String> patchNoticeToken(@Header("authorization") String jwt, @Path("uid") Long userIdx, @Body JsonObject request);//FCM 푸시 알림 토큰 수정

    @GET("/user/{uid}/ott")
    Call<String> getMyOttList(@Header("authorization") String jwt, @Path("uid") Long userIdx);      //나의 OTT 서비스 조회

    @GET("/user/{uid}/urgent-ott")
    Call<String> getMyUrgentOttList(@Header("authorization") String jwt, @Path("uid") Long userIdx);//나의 OTT 서비스 중 2주 내 결제 서비스 조회

    @POST("/user/ott")
    Call<String> postAddMyOtt(@Header("authorization") String jwt, @Body JsonObject request);       //나의 OTT 서비스 추가

    @GET("/user/{uid}/recruit")
    Call<String> getMyRecruitList(@Header("authorization") String jwt, @Path("uid") Long userIdx);  //나의 모집글 조회

    @GET("/user/{uid}/notice")
    Call<String> getMyNoticeList(@Header("authorization") String jwt, @Path("uid") Long userIdx);   //나의 알림 조회


    @GET("/recruit/{pid}/list")
    Call<String> getRecruitList(@Header("authorization") String jwt, @Path("pid") int platformIdx);//해당 플랫폼의 모집글 조회

    //TODO: 연동 요망!!
    @GET("/recruit/{pid}/list")
    Call<String> getHeadcountRecruitList(@Header("authorization") String jwt, @Path("pid") int platformIdx, @Query("headcount") int headcount);//해당 플랫폼의 인원수에 맞는 모집글 조회

    @POST("/recruit/upload")
    Call<String> postRecruitUpload(@Header("authorization") String jwt, @Body JsonObject request);  //모집글 추가

    @DELETE("/recruit/{rid}")
    Call<String> deleteRecruit(@Header("authorization") String jwt, @Path("rid") Long recruitIdx);  //모집글 삭제

    @POST("/recruit/participate")
    Call<String> postRecruitParticipate(@Header("authorization") String jwt, @Body JsonObject request);     //모집글에 참여

    @GET("/recruit/{rid}/members")
    Call<String> getRecruitMembers(@Header("authorization") String jwt, @Path("rid") Long recruitIdx);      //모집글의 수락된 멤버 조회(모집 확정 시)

    @GET("/recruit/{rid}/waitlist")
    Call<String> getRecruitWaitlist(@Header("authorization") String jwt, @Path("rid") Long recruitIdx);     //모집글 참여 목록(대기자 명단) 조회

    @PATCH("/recruit/waitlist/accept")
    Call<String> patchWaitlistAccept(@Header("authorization") String jwt, @Body JsonObject request);//모집글 참여 수락

    @PATCH("/recruit/waitlist/cancel")
    Call<String> patchWaitlistCancel(@Header("authorization") String jwt, @Body JsonObject request);//모집글 참여 수락 취소


    @POST("/team")
    Call<String> postRecruitTeam(@Header("authorization") String jwt, @Body JsonObject request);    //모집글을 통한 팀원 확정(팀 추가)

    @DELETE("/team/{tid}")
    Call<String> deleteTeam(@Header("authorization") String jwt, @Path("tid") Long teamIdx);        //OTT 서비스 해지
}

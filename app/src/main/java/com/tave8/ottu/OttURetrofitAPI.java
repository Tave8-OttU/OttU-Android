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

    @PATCH("/user/{uid}")
    Call<String> patchUser(@Header("authorization") String jwt, @Path("uid") Long userIdx, @Body JsonObject request);       //사용자 정보 수정

    @DELETE("/user/{uid}")
    Call<String> deleteUser(@Header("authorization") String jwt, @Path("uid") Long userIdx);        //사용자 정보 삭제

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

    @GET("/user/{uid}/post")
    Call<String> getMyPostList(@Header("authorization") String jwt, @Path("uid") Long userIdx);     //나의 게시글 조회


    @GET("/recruit/list")
    Call<String> getRecruitList(@Header("authorization") String jwt, @Query("pid") int platformIdx, @Query("headcount") Integer headcount, @Query("completed") Boolean completed);//해당 플랫폼의 모집글 조회(필터 가능)

    @POST("/recruit")
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


    @GET("/team/{tid}/evaluation/{uid}")
    Call<String> getTeamForEvaluation(@Header("authorization") String jwt, @Path("tid") Long teamIdx, @Path("uid") Long userIdx);//팀원 신뢰도 평가를 위한 회원 정보 받기

    @POST("/team/{tid}/evaluation")
    Call<String> postTeamEvaluation(@Header("authorization") String jwt, @Path("tid") Long teamIdx, @Body JsonObject request);   //팀원 신뢰도 평가 전달
    
    
    @GET("/community/post/current")
    Call<String> getCurrentCommunityPostList(@Header("authorization") String jwt);                  //각 플랫폼별 최신 커뮤니티 글 조회

    @GET("/community/post/list")
    Call<String> getCommunityPostList(@Header("authorization") String jwt, @Query("pid") int platformIdx);   //해당 플랫폼의 커뮤니티글 조회

    @GET("/community/post/{cpid}")
    Call<String> getPost(@Header("authorization") String jwt, @Path("cpid") Long postIdx);

    @POST("/community/post")
    Call<String> postPostUpload(@Header("authorization") String jwt, @Body JsonObject request);     //커뮤니티 글 추가

    @PATCH("/community/post/{cpid}")
    Call<String> patchPost(@Header("authorization") String jwt, @Path("cpid") Long postIdx, @Body JsonObject request);  //커뮤니티 글 수정

    @DELETE("/community/post/{cpid}")
    Call<String> deletePost(@Header("authorization") String jwt, @Path("cpid") Long postIdx);       //커뮤니티 글 삭제


    @POST("/community/comment")
    Call<String> postCommentUpload(@Header("authorization") String jwt, @Body JsonObject request);  //댓글 추가

    @DELETE("/community/comment/{ccid}")
    Call<String> deleteComment(@Header("authorization") String jwt, @Path("ccid") Long commentIdx); //댓글 삭제
}

package com.tave8.ottu.data;

import java.util.ArrayList;

public class UserInfo {
    private UserEssentialInfo userEssentialInfo;
    private String email = "";
    private String kakaoID = "";
    private int level;
    private boolean isFirst;
    private ArrayList<Genre> interestGenre;

    public UserInfo(Long userId, String nick, int level, boolean isFirst, ArrayList<Genre> interestGenre) {
        userEssentialInfo = new UserEssentialInfo(userId, nick);
        this.level = level;
        this.isFirst = isFirst;
        this.interestGenre = interestGenre;
    }

    public UserInfo(Long userId, String email, String kakaoID, String nick, int level, boolean isFirst, ArrayList<Genre> interestGenre) {
        userEssentialInfo = new UserEssentialInfo(userId, nick);
        this.email = email;
        this.kakaoID = kakaoID;
        this.level = level;
        this.isFirst = isFirst;
        this.interestGenre = interestGenre;
    }

    public UserEssentialInfo getUserEssentialInfo() {
        return userEssentialInfo;
    }

    public Long getUserId() {
        return userEssentialInfo.getUserId();
    }

    public String getEmail() {
        return email;
    }

    public String getKakaoID() {
        return kakaoID;
    }

    public String getNick() {
        return userEssentialInfo.getNick();
    }

    public int getLevel() {
        return level;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public ArrayList<Genre> getInterestGenre() {
        return interestGenre;
    }
}

package com.tave8.ottu.data;

import java.util.ArrayList;

public class UserInfo {
    private UserEssentialInfo userEssentialInfo;
    private String kakaotalkId = "";
    private int reliability;
    private boolean isFirst;
    private ArrayList<Genre> interestGenre;

    public UserInfo(Long userIdx, String nick, int reliability, boolean isFirst, ArrayList<Genre> interestGenre) {
        userEssentialInfo = new UserEssentialInfo(userIdx, nick);
        this.reliability = reliability;
        this.isFirst = isFirst;
        this.interestGenre = interestGenre;
    }

    public UserInfo(Long userIdx, String kakaotalkId, String nick, int reliability, boolean isFirst, ArrayList<Genre> interestGenre) {
        userEssentialInfo = new UserEssentialInfo(userIdx, nick);
        this.kakaotalkId = kakaotalkId;
        this.reliability = reliability;
        this.isFirst = isFirst;
        this.interestGenre = interestGenre;
    }

    public UserEssentialInfo getUserEssentialInfo() {
        return userEssentialInfo;
    }

    public Long getUserIdx() {
        return userEssentialInfo.getUserIdx();
    }

    public String getKakaotalkId() {
        return kakaotalkId;
    }

    public String getNick() {
        return userEssentialInfo.getNick();
    }

    public int getReliability() {
        return reliability;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public ArrayList<Genre> getInterestGenre() {
        return interestGenre;
    }
}

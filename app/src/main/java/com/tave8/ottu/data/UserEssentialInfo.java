package com.tave8.ottu.data;

public class UserEssentialInfo {
    private Long userId;
    private String nick;

    public UserEssentialInfo(Long userId, String nick) {
        this.userId = userId;
        this.nick = nick;
    }

    public Long getUserId() {
        return userId;
    }

    public String getNick() {
        return nick;
    }
}

package com.tave8.ottu.data;

public class RecruitRequestInfo {
    private UserEssentialInfo requesterInfo;
    private boolean isAccepted;

    public RecruitRequestInfo(UserEssentialInfo requesterInfo, boolean isAccepted) {
        this.requesterInfo = requesterInfo;
        this.isAccepted = isAccepted;
    }

    public UserEssentialInfo getRequesterInfo() {
        return requesterInfo;
    }

    public boolean getIsAccepted() {
        return isAccepted;
    }
}

package com.tave8.ottu.data;

public class RecruitRequestInfo {
    private Long waitlistIdx;
    private UserEssentialInfo requesterInfo;
    private boolean isAccepted;

    public RecruitRequestInfo(Long waitlistIdx, UserEssentialInfo requesterInfo, boolean isAccepted) {
        this.waitlistIdx = waitlistIdx;
        this.requesterInfo = requesterInfo;
        this.isAccepted = isAccepted;
    }

    public Long getWaitlistIdx() {
        return waitlistIdx;
    }

    public UserEssentialInfo getRequesterInfo() {
        return requesterInfo;
    }

    public boolean getIsAccepted() {
        return isAccepted;
    }

    public void changeIsAccepted() {
        isAccepted = !isAccepted;
    }
}

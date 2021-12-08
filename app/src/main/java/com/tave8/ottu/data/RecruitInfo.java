package com.tave8.ottu.data;

import java.time.LocalDateTime;

public class RecruitInfo {
    private Long recruitId;
    private int platformId;
    private UserEssentialInfo writerInfo;
    private boolean isCompleted;
    private int headCount;
    private int choiceNum;
    private LocalDateTime recruitDateTime;

    public RecruitInfo(Long recruitId, int platformId, UserEssentialInfo writerInfo, boolean isCompleted, int headCount, int choiceNum, LocalDateTime recruitDateTime) {
        this.recruitId = recruitId;
        this.platformId = platformId;
        this.writerInfo = writerInfo;
        this.isCompleted = isCompleted;
        this.headCount = headCount;
        this.choiceNum = choiceNum;
        this.recruitDateTime = recruitDateTime;
    }

    public Long getRecruitId() {
        return recruitId;
    }

    public int getPlatformId() {
        return platformId;
    }

    public UserEssentialInfo getWriterInfo() {
        return writerInfo;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public int getHeadCount() {
        return headCount;
    }

    public int getChoiceNum() {
        return choiceNum;
    }

    public LocalDateTime getRecruitDateTime() {
        return recruitDateTime;
    }
}

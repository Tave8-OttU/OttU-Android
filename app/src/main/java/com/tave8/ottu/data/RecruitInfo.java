package com.tave8.ottu.data;

import java.time.LocalDateTime;

public class RecruitInfo {
    private Long recruitIdx;
    private int platformIdx;
    private UserEssentialInfo writerInfo;
    private boolean isCompleted;
    private boolean isApplying = false;
    private int headCount;
    private int choiceNum;
    private LocalDateTime recruitDateTime;

    //MyRecruitActivity에서 사용
    public RecruitInfo(Long recruitIdx, int platformIdx, UserEssentialInfo writerInfo, boolean isCompleted, int headCount, int choiceNum, LocalDateTime recruitDateTime) {
        this.recruitIdx = recruitIdx;
        this.platformIdx = platformIdx;
        this.writerInfo = writerInfo;
        this.isCompleted = isCompleted;
        this.headCount = headCount;
        this.choiceNum = choiceNum;
        this.recruitDateTime = recruitDateTime;
    }

    //RecruitActivity에서 사용
    public RecruitInfo(Long recruitIdx, int platformIdx, UserEssentialInfo writerInfo, boolean isCompleted, boolean isApplying, int headCount, int choiceNum, LocalDateTime recruitDateTime) {
        this.recruitIdx = recruitIdx;
        this.platformIdx = platformIdx;
        this.writerInfo = writerInfo;
        this.isCompleted = isCompleted;
        this.isApplying = isApplying;
        this.headCount = headCount;
        this.choiceNum = choiceNum;
        this.recruitDateTime = recruitDateTime;
    }

    public Long getRecruitIdx() {
        return recruitIdx;
    }

    public int getPlatformIdx() {
        return platformIdx;
    }

    public UserEssentialInfo getWriterInfo() {
        return writerInfo;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public boolean isApplying() {
        return isApplying;
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

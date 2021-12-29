package com.tave8.ottu.data;

public class RecruitInfo {
    private Long recruitIdx;
    private int platformIdx;
    private UserEssentialInfo writerInfo;
    private boolean isCompleted;
    private int headCount;
    private int choiceNum;
    private String recruitDateTime;

    public RecruitInfo(Long recruitIdx, int platformIdx, UserEssentialInfo writerInfo, boolean isCompleted, int headCount, int choiceNum, String recruitDateTime) {
        this.recruitIdx = recruitIdx;
        this.platformIdx = platformIdx;
        this.writerInfo = writerInfo;
        this.isCompleted = isCompleted;
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

    public int getHeadCount() {
        return headCount;
    }

    public int getChoiceNum() {
        return choiceNum;
    }

    public String getRecruitDateTime() {
        return recruitDateTime;
    }
}

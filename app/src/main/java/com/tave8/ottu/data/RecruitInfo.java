package com.tave8.ottu.data;

import java.util.Date;

public class RecruitInfo {
    private Long recruitId;
    private UserEssentialInfo writerInfo;
    private boolean isCompleted;
    private int headCount;
    private int choiceNum;
    private Date createdDate;

    public RecruitInfo(Long recruitId, UserEssentialInfo writerInfo, boolean isCompleted, int headCount, int choiceNum, Date createdDate) {
        this.recruitId = recruitId;
        this.writerInfo = writerInfo;
        this.isCompleted = isCompleted;
        this.headCount = headCount;
        this.choiceNum = choiceNum;
        this.createdDate = createdDate;
    }

    public Long getRecruitId() {
        return recruitId;
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

    public Date getCreatedDate() {
        return createdDate;
    }
}

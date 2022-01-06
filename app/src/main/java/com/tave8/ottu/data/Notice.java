package com.tave8.ottu.data;

import java.time.LocalDateTime;

public class Notice {
    private Long noticeIdx;
    private Long evaluateTeamIdx;
    private String content;
    private LocalDateTime createdDate;
    private boolean isEvaluated;

    public Notice(Long noticeIdx, Long evaluateTeamIdx, String content, LocalDateTime createdDate, boolean isEvaluated) {
        this.noticeIdx = noticeIdx;
        this.evaluateTeamIdx = evaluateTeamIdx;
        this.content = content;
        this.createdDate = createdDate;
        this.isEvaluated = isEvaluated;
    }

    public Long getNoticeIdx() {
        return noticeIdx;
    }

    public Long getEvaluateTeamIdx() {
        return evaluateTeamIdx;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public boolean getIsEvaluated() {
        return isEvaluated;
    }

    public void setIsEvaluatedTrue() {
        isEvaluated = true;
    }
}

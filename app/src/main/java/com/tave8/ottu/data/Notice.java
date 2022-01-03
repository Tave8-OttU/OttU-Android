package com.tave8.ottu.data;

import java.time.LocalDateTime;

public class Notice {
    private Long noticeIdx;
    private String content;
    private LocalDateTime createdDate;

    public Notice(Long noticeIdx, String content, LocalDateTime createdDate) {
        this.noticeIdx = noticeIdx;
        this.content = content;
        this.createdDate = createdDate;
    }

    public Long getNoticeIdx() {
        return noticeIdx;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}

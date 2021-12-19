package com.tave8.ottu.data;

import java.time.LocalDateTime;

public class CommentInfo {
    private Long commentIdx;
    private Long postIdx;
    private UserEssentialInfo writerInfo;
    private String content;
    private LocalDateTime commentDateTime;

    public CommentInfo(Long commentIdx, Long postIdx, UserEssentialInfo writerInfo, String content, LocalDateTime commentDateTime) {
        this.commentIdx = commentIdx;
        this.postIdx = postIdx;
        this.writerInfo = writerInfo;
        this.content = content;
        this.commentDateTime = commentDateTime;
    }

    public Long getCommentIdx() {
        return commentIdx;
    }

    public Long getPostIdx() {
        return postIdx;
    }

    public UserEssentialInfo getWriterInfo() {
        return writerInfo;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCommentDateTime() {
        return commentDateTime;
    }
}

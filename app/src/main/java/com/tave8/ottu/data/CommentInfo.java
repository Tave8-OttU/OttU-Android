package com.tave8.ottu.data;

import java.time.LocalDateTime;

public class CommentInfo {
    private Long commentId;
    private Long postId;
    private UserEssentialInfo writerInfo;
    private String content;
    private LocalDateTime commentDateTime;

    public CommentInfo(Long commentId, Long postId, UserEssentialInfo writerInfo, String content, LocalDateTime commentDateTime) {
        this.commentId = commentId;
        this.postId = postId;
        this.writerInfo = writerInfo;
        this.content = content;
        this.commentDateTime = commentDateTime;
    }

    public Long getCommentId() {
        return commentId;
    }

    public Long getPostId() {
        return postId;
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

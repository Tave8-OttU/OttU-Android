package com.tave8.ottu.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.time.LocalDateTime;

public class CommunityPostInfo implements Parcelable {
    private Long postId;
    private int platformId;
    private UserEssentialInfo writerInfo;
    private String content;
    private LocalDateTime postDateTime;
    private int commentNum;

    public CommunityPostInfo(Long postId, int platformId, UserEssentialInfo writerInfo, String content, LocalDateTime postDateTime, int commentNum) {
        this.postId = postId;
        this.platformId = platformId;
        this.writerInfo = writerInfo;
        this.content = content;
        this.postDateTime = postDateTime;
        this.commentNum = commentNum;
    }

    @Override
    public String toString() {
        return "CommunityPostInfo{" +
                "postId=" + postId +
                ", platformId=" + platformId +
                ", writerInfo=" + writerInfo +
                ", content='" + content + '\'' +
                ", postDateTime=" + postDateTime +
                ", commentNum=" + commentNum +
                '}';
    }

    protected CommunityPostInfo(Parcel in) {
        if (in.readByte() == 0) {
            postId = null;
        } else {
            postId = in.readLong();
        }
        platformId = in.readInt();
        writerInfo = in.readParcelable(UserEssentialInfo.class.getClassLoader());
        content = in.readString();
        postDateTime = LocalDateTime.parse(in.readString());
        commentNum = in.readInt();
    }

    public static final Creator<CommunityPostInfo> CREATOR = new Creator<CommunityPostInfo>() {
        @Override
        public CommunityPostInfo createFromParcel(Parcel in) {
            return new CommunityPostInfo(in);
        }

        @Override
        public CommunityPostInfo[] newArray(int size) {
            return new CommunityPostInfo[size];
        }
    };

    public Long getPostId() {
        return postId;
    }

    public int getPlatformId() {
        return platformId;
    }

    public UserEssentialInfo getWriterInfo() {
        return writerInfo;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getPostDateTime() {
        return postDateTime;
    }

    public int getCommentNum() {
        return commentNum;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (postId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(postId);
        }
        dest.writeInt(platformId);
        dest.writeParcelable(writerInfo, flags);
        dest.writeString(content);
        dest.writeString(postDateTime.toString());
        dest.writeInt(commentNum);
    }
}

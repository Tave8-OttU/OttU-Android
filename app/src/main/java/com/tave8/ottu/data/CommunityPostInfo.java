package com.tave8.ottu.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.time.LocalDateTime;

public class CommunityPostInfo implements Parcelable {
    private Long postIdx;
    private int platformIdx;
    private UserEssentialInfo writerInfo;
    private String content;
    private LocalDateTime postDateTime;
    private int commentNum;

    public CommunityPostInfo(Long postIdx, int platformIdx, UserEssentialInfo writerInfo, String content, LocalDateTime postDateTime, int commentNum) {
        this.postIdx = postIdx;
        this.platformIdx = platformIdx;
        this.writerInfo = writerInfo;
        this.content = content;
        this.postDateTime = postDateTime;
        this.commentNum = commentNum;
    }

    @Override
    public String toString() {
        return "CommunityPostInfo{" +
                "postIdx=" + postIdx +
                ", platformIdx=" + platformIdx +
                ", writerInfo=" + writerInfo +
                ", content='" + content + '\'' +
                ", postDateTime=" + postDateTime +
                ", commentNum=" + commentNum +
                '}';
    }

    protected CommunityPostInfo(Parcel in) {
        if (in.readByte() == 0) {
            postIdx = null;
        } else {
            postIdx = in.readLong();
        }
        platformIdx = in.readInt();
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

    public Long getPostIdx() {
        return postIdx;
    }

    public int getPlatformIdx() {
        return platformIdx;
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

    public void setContent(String content) {
        this.content = content;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (postIdx == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(postIdx);
        }
        dest.writeInt(platformIdx);
        dest.writeParcelable(writerInfo, flags);
        dest.writeString(content);
        dest.writeString(postDateTime.toString());
        dest.writeInt(commentNum);
    }
}

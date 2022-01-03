package com.tave8.ottu.data;

import android.os.Parcel;
import android.os.Parcelable;

public class UserEssentialInfo implements Parcelable {
    private Long userIdx;
    private String nick;

    public UserEssentialInfo(Long userIdx, String nick) {
        this.userIdx = userIdx;
        this.nick = nick;
    }

    protected UserEssentialInfo(Parcel in) {
        if (in.readByte() == 0) {
            userIdx = null;
        } else {
            userIdx = in.readLong();
        }
        nick = in.readString();
    }

    public static final Creator<UserEssentialInfo> CREATOR = new Creator<UserEssentialInfo>() {
        @Override
        public UserEssentialInfo createFromParcel(Parcel in) {
            return new UserEssentialInfo(in);
        }

        @Override
        public UserEssentialInfo[] newArray(int size) {
            return new UserEssentialInfo[size];
        }
    };

    public Long getUserIdx() {
        return userIdx;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (userIdx == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(userIdx);
        }
        dest.writeString(nick);
    }
}

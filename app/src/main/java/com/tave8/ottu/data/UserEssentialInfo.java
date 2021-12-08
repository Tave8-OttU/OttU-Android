package com.tave8.ottu.data;

import android.os.Parcel;
import android.os.Parcelable;

public class UserEssentialInfo implements Parcelable {
    private Long userId;
    private String nick;

    public UserEssentialInfo(Long userId, String nick) {
        this.userId = userId;
        this.nick = nick;
    }

    protected UserEssentialInfo(Parcel in) {
        if (in.readByte() == 0) {
            userId = null;
        } else {
            userId = in.readLong();
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

    public Long getUserId() {
        return userId;
    }

    public String getNick() {
        return nick;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (userId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(userId);
        }
        dest.writeString(nick);
    }
}

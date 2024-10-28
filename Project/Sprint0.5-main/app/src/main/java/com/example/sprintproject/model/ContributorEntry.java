package com.example.sprintproject.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ContributorEntry implements Parcelable {
    private String userID;
    private String notes;


    public ContributorEntry() {
    }

    public ContributorEntry(String id, String note) {
        this.userID = id;
        this.notes = note;
    }
    // Parcelable implementation
    protected ContributorEntry(Parcel in) {
        userID = in.readString();
        notes = in.readString();
    }

    public String getUserId() {
        return userID;
    }

    public String getNotes() {
        return notes;
    }

    public void setUserID(String id) {
        this.userID = id;
    }

    public void setNotes(String note) {
        this.notes = note;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getUserId());
        dest.writeString(getNotes());
    }
    @Override
    public int describeContents() {
        return 0;
    }

    // Parcelable.Creator
    public static final Creator<ContributorEntry> CREATOR = new Creator<ContributorEntry>() {
        @Override
        public ContributorEntry createFromParcel(Parcel in) {
            return new ContributorEntry(in);
        }

        @Override
        public ContributorEntry[] newArray(int size) {
            return new ContributorEntry[size];
        }
    };
}

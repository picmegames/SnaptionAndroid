package com.snaptiongame.snaptionapp.data.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by BrianGouldsberry on 1/20/17.
 */

public class Friend {
    public static final String sId = "id";
    public static final String sStartDate = "start_date";
    public static final String sEndDate = "end_date";
    public static final String sIsPublic = "isPublic";
    public static final String sPickerId = "picker_id";
    public static final String sPickerName = "picker_name";
    public static final String sType = "type";
    public static final String sImage = "pictureEncoded";
    public static final String sImageUrl = "picture";
    public static final String sTopCaption = "topCaption";

    @SerializedName(sId)
    public int id;
    @SerializedName(sStartDate)
    public String firstName;
    @SerializedName(sEndDate)
    public String lastName;
    @SerializedName(sIsPublic)
    public String userName;
    @SerializedName(sIsPublic)
    public String imageUrl;
    @SerializedName(sIsPublic)
    public String phoneNumber;
    @SerializedName(sIsPublic)
    public String email;

    public Friend(int id, String first, String last, String userName, String imageUrl, String
            phoneNumber, String
            email) {
        this.id = id;
        this.firstName = first;
        this.lastName = last;
        this.userName = userName;
        this.imageUrl = imageUrl;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
}

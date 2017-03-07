package com.snaptiongame.app.data.models;

import com.google.gson.annotations.SerializedName;

/**
 * @author Brian Gouldsberry
 */
public class Friend {
    public int id;
    public String firstName;
    public String lastName;
    public String fullName;
    @SerializedName(USERNAME)
    public String userName;
    public String picture;
    public String cover;
    public String email;
    public boolean isSnaptionFriend;

    public static final String ID = "id";
    public static final String USERNAME = "username";
    public static final String FULL_NAME = "fullName";
    public static final String PICTURE = "picture";
    public static final String FIRST = "firstName";
    public static final String LAST = "lastName";
    public static final String COVER = "cover";
    public static final String EMAIL = "email";

    public Friend() {

    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof  Friend) {
            Friend other = (Friend) o;
            return (userName.equals(other.userName));
        }
        return false;
    }

    public Friend(int id, String first, String last, String fullName, String userName, String picture,
                  String cover, String email) {
        this.id = id;
        this.firstName = first;
        this.lastName = last;
        this.fullName = fullName;
        this.userName = userName;
        this.picture = picture;
        this.cover = cover;
        this.email = email;
        isSnaptionFriend = false;
    }
}

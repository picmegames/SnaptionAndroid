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
    public String username;
    public String picture;
    @SerializedName(IMAGE_URL)
    public String imageUrl;
    @SerializedName(IMAGE_WIDTH)
    public int imageWidth;
    @SerializedName(IMAGE_HEIGHT)
    public int imageHeight;
    public String cover;
    public String email;
    public boolean isSnaptionFriend;


    public static final String ID = "id";
    public static final String USERNAME = "username";
    public static final String FULL_NAME = "fullName";
    public static final String PICTURE = "picture";
    public static final String IMAGE_URL = "url";
    public static final String IMAGE_WIDTH = "width";
    public static final String IMAGE_HEIGHT = "height";
    public static final String FIRST = "firstName";
    public static final String LAST = "lastName";
    public static final String COVER = "cover";
    public static final String EMAIL = "email";

    public Friend() {

    }

    //Convert a Friend to a User
    public Friend(User user) {
        this.id = user.id;
        this.picture = user.picture;
        this.username = user.username;
        this.imageWidth = user.imageWidth;
        this.imageHeight = user.imageHeight;
        this.imageUrl = user.imageUrl;
        this.isSnaptionFriend = false;
    }

    public Friend(int id, String first, String last, String fullName, String username, String picture,
                  String cover, String email) {
        this.id = id;
        this.firstName = first;
        this.lastName = last;
        this.fullName = fullName;
        this.username = username;
        this.picture = picture;
        this.cover = cover;
        this.email = email;
        isSnaptionFriend = false;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Friend) {
            Friend other = (Friend) o;
            return (username.equals(other.username));
        }
        return false;
    }

    public void setSnaptionFriend(boolean isFriend) {
        isSnaptionFriend = isFriend;
    }
}

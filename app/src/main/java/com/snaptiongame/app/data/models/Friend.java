package com.snaptiongame.app.data.models;

import com.google.gson.annotations.SerializedName;

/**
 * @author Brian Gouldsberry
 */
public class Friend {
    @SerializedName(ID)
    public int id;
    @SerializedName(FULL_NAME)
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
    @SerializedName(COVER)
    public String cover;
    @SerializedName(EMAIL)
    public String email;

    public boolean isSnaptionFriend;

    public static final String ID = "id";
    public static final String USERNAME = "username";
    public static final String FULL_NAME = "fullName";
    public static final String PICTURE = "picture";
    public static final String IMAGE_URL = "url";
    public static final String IMAGE_WIDTH = "width";
    public static final String IMAGE_HEIGHT = "height";
    public static final String COVER = "cover";
    public static final String EMAIL = "email";

    public Friend() {

    }

    public Friend(int id) {
        this.id = id;
    }

    public Friend(User user) {
        this.id = user.id;
        this.picture = user.picture;
        this.username = user.username;
        this.fullName = user.fullName;
        this.imageWidth = user.imageWidth;
        this.imageHeight = user.imageHeight;
        this.imageUrl = user.imageUrl;
        this.isSnaptionFriend = user.isFriend;
    }

    public Friend(int id, String fullName, String username, String picture,
                  String cover, String email) {
        this.id = id;
        this.fullName = fullName;
        this.username = username;
        this.picture = picture;
        this.cover = cover;
        this.email = email;
        isSnaptionFriend = true;
    }

    @Override
    public boolean equals(Object other) {
        return other.getClass() == Friend.class && this.id == ((Friend) other).id;
    }
}

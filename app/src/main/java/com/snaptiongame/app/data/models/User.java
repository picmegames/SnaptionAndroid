package com.snaptiongame.app.data.models;

import com.google.gson.annotations.SerializedName;

/**
 * @author Tyler Wong
 */

public class User {

    @SerializedName(ID)
    public int id;
    @SerializedName(USERNAME)
    public String username;
    @SerializedName(FULL_NAME)
    public String fullName;
    @SerializedName(PICTURE)
    public String picture;
    @SerializedName(IMAGE_URL)
    public String imageUrl;
    @SerializedName(IMAGE_WIDTH)
    public int imageWidth;
    @SerializedName(IMAGE_HEIGHT)
    public int imageHeight;
    @SerializedName(TYPE)
    public String type;
    @SerializedName(EXP)
    public int exp;
    @SerializedName(IS_FRIEND)
    public boolean isFriend;

    public static final String ID = "id";
    public static final String USERNAME = "username";
    public static final String FULL_NAME = "fullName";
    public static final String PICTURE = "picture";
    public static final String IMAGE_URL = "url";
    public static final String IMAGE_WIDTH = "width";
    public static final String IMAGE_HEIGHT = "height";
    public static final String TYPE = "imgType";
    public static final String EXP = "exp";
    public static final String IS_FRIEND = "isFriend";

    public User() {

    }

    //Convert a Friend to a User
    public User(Friend friend) {
        this.id = friend.id;
        this.picture = friend.picture;
        this.username = friend.username;
        this.imageWidth = friend.imageWidth;
        this.imageHeight = friend.imageHeight;
        this.imageUrl = friend.imageUrl;
    }

    public User(String username) {
        this.username = username;
    }

    public User(String picture, String type) {
        this.picture = picture;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof User) {
            User other = (User) o;
            return (id == ((User) o).id);
        }
        return false;
    }
}

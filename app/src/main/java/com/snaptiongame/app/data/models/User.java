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
    public static final String IS_FRIEND = "isFriend";

    public User() {

    }

    //Convert a Friend to a User
    public User(Friend friend) {
        this.id = friend.getId();
        this.picture = friend.getPicture();
        this.username = friend.getUsername();
        this.imageWidth = friend.getImageWidth();
        this.imageHeight = friend.getImageHeight();
        this.imageUrl = friend.getImageUrl();
    }

    public User(String username) {
        this.username = username;
    }

    public User(String picture, String type) {
        this.picture = picture;
        this.type = type;
    }

    @Override
    public boolean equals(Object obj) {
        return obj.getClass() == User.class && this.id == ((User) obj).id;
    }
}

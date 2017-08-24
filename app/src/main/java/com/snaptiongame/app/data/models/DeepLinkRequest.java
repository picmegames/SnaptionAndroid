package com.snaptiongame.app.data.models;

import com.google.gson.annotations.SerializedName;

/**
 * @author Brian Gouldsberry
 */

public class DeepLinkRequest {
    @SerializedName(GAME_ID)
    public int gameId;
    @SerializedName(EMAIL)
    public String email;
    @SerializedName(PHONE)
    public String phone;
    @SerializedName(FACEBOOK_ID)
    public String facebookId;
    @SerializedName(GOOGLE_ID)
    public String googleId;

    public static final String GAME_ID = "gameId";
    public static final String EMAIL = "email";
    public static final String PHONE = "phone";
    public static final String FACEBOOK_ID = "facebookId";
    public static final String GOOGLE_ID = "googleId";

    public DeepLinkRequest(int gameId) {
        this.gameId = gameId;
        this.email = null;
        this.phone = null;
        this.facebookId = null;
        this.googleId = null;
    }

    public DeepLinkRequest(int gameId, String email, String phone, String facebookId, String googleId) {
        this.gameId = gameId;
        this.email = email;
        this.phone = phone;
        this.facebookId = facebookId;
        this.googleId = googleId;
    }
}

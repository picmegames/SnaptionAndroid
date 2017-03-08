package com.snaptiongame.app.data.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by BrianGouldsberry on 2/27/17.
 */

public class DeepLinkRequest {
    @SerializedName(GAMEID)
    public int gameId;
    @SerializedName(EMAIL)
    public String email;
    @SerializedName(PHONE)
    public String phone;
    @SerializedName(FACEBOOKID)
    public String facebookID;
    @SerializedName(GOOGLEID)
    public String googleID;

    public static final String GAMEID = "gameId";
    public static final String EMAIL = "email";
    public static final String PHONE = "phone";
    public static final String FACEBOOKID = "facebookID";
    public static final String GOOGLEID = "googleID";

    public DeepLinkRequest(int gameId) {
        this.gameId = gameId;
        this.email = null;
        this.phone = null;
        this.facebookID = null;
        this.googleID = null;
    }

    public DeepLinkRequest(int gameId, String email, String phone, String facebookID, String
            googleID) {
        this.gameId = gameId;
        this.email = email;
        this.phone = phone;
        this.facebookID = facebookID;
        this.googleID = googleID;
    }
}

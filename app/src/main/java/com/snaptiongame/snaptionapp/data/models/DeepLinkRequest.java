package com.snaptiongame.snaptionapp.data.models;

/**
 * Created by BrianGouldsberry on 2/27/17.
 */

public class DeepLinkRequest {
    public int gameId;
    public String email;
    public String phone;
    public String facebookID;
    public String googleID;

    public static final String GAMEID = "gameId";
    public static final String EMAIL = "email";
    public static final String PHONE = "phone";
    public static final String FACEBOOKID = "facebookID";
    public static final String GOOGLEID = "googleID";

    public DeepLinkRequest(int gameId, String email, String phone, String facebookID, String
            googleID) {
        this.gameId = gameId;
        this.email = email;
        this.phone = phone;
        this.facebookID = facebookID;
        this.googleID = googleID;
    }
}

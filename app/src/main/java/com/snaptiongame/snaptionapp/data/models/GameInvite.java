package com.snaptiongame.snaptionapp.data.models;

/**
 * Created by BrianGouldsberry on 2/27/17.
 */

public class GameInvite {
    public String inviteToken;
    public int gameId;

    public GameInvite(String token, int gameId) {
        this.inviteToken = token;
        this.gameId = gameId;
    }
}

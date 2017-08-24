package com.snaptiongame.app.data.models;

/**
 * @author Brian Gouldsberry
 */

public class GameInvite {
    public String inviteToken;
    public int gameId;

    public static final String INVITE_TOKEN = "inviteToken";
    public static final String GAME_ID = "gameId";

    public GameInvite(String token, int gameId) {
        this.inviteToken = token;
        this.gameId = gameId;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof GameInvite) {
            GameInvite otherInvite = (GameInvite) other;
            return ((this.gameId == otherInvite.gameId) && (this.inviteToken.equals(otherInvite
                    .inviteToken)));
        }
        return false;
    }
}

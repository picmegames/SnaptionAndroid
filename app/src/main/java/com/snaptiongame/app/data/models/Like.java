package com.snaptiongame.app.data.models;

/**
 * @author Tyler Wong
 */

public class Like {
    public int targetId;
    public boolean choice;
    public String choiceType;
    public String type;

    public static final String GAME_ID = "gameId";
    public static final String CAPTION_ID = "captionId";
    public static final String UPVOTE = "upvote";
    public static final String FLAGGED = "flagged";

    public Like(int targetId, boolean choice, String choiceType, String type) {
        this.targetId = targetId;
        this.choice = choice;
        this.choiceType = choiceType;
        this.type = type;
    }
}

package com.snaptiongame.app.data.models;

import com.google.gson.annotations.SerializedName;

/**
 * @author Tyler Wong
 */
public class Caption {

    @SerializedName(ID)
    public int id;
    @SerializedName(GAME_ID)
    public int gameId;
    @SerializedName(NUM_VOTES)
    public int numVotes;
    @SerializedName(USER_ID)
    public int creatorId;
    @SerializedName(USER_PICTURE)
    public String creatorPicture;
    @SerializedName(USERNAME)
    public String creatorName;
    @SerializedName(FITB_ID_RECEIVE)
    public int fitBId;
    @SerializedName(CAPTION)
    public String caption;
    @SerializedName(FITB_ID_SEND)
    public int fitbIdSend;
    @SerializedName(FITB)
    public FitBCaption assocFitB;
    @SerializedName(BEEN_UPVOTED)
    public boolean beenUpvoted;
    @SerializedName(BEEN_FLAGGED)
    public boolean beenFlagged;

    public static final String ID = "id";
    public static final String GAME_ID = "game_id";
    public static final String NUM_VOTES = "numVotes";
    public static final String USER_ID = "userId";
    public static final String USER_PICTURE = "userPic";
    public static final String PICTURE = "picture";
    public static final String USERNAME = "username";
    public static final String CREATOR = "creator";
    public static final String FITB_ID_RECEIVE = "fitb_id";
    public static final String FITB_ID_SEND = "fitbId";
    public static final String CAPTION = "userEntry";
    public static final String FITB = "fitB";
    public static final String FITB_OTHER = "fitb";
    public static final String BEEN_UPVOTED = "beenUpvoted";
    public static final String BEEN_FLAGGED = "beenFlagged";

    public Caption() {

    }

    public Caption(int fitBId, String caption) {
        this.fitbIdSend = fitBId;
        this.caption = caption;
    }

    public Caption(int id, int gameId, int numVotes, int creatorId, int fitBId,
                   String caption, FitBCaption fitb) {
        this.id = id;
        this.gameId = gameId;
        this.numVotes = numVotes;
        this.creatorId = creatorId;
        this.fitBId = fitBId;
        this.caption = caption;
        this.assocFitB = fitb;
    }

    @Override
    public boolean equals(Object obj) {
        return obj.getClass() == Caption.class
                && this.id == ((Caption) obj).id
                && this.beenUpvoted == ((Caption) obj).beenUpvoted
                && this.beenFlagged == ((Caption) obj).beenFlagged
                && this.numVotes == ((Caption) obj).numVotes
                && this.creatorName.equals(((Caption) obj).creatorName)
                && this.creatorPicture.equals(((Caption) obj).creatorPicture);
    }
}

package com.snaptiongame.app.data.models;

import com.google.gson.annotations.SerializedName;

/**
 * @author Quang Ngo.
 */

public class UserStats {
    @SerializedName(GAMES_CREATED)
    public int gamesCreated;
    @SerializedName(HIGHEST_GAME_UPVOTE)
    public int highestGameUpvote;
    @SerializedName(CAPTIONS_CREATED)
    public int captionsCreated;
    @SerializedName(CAPTION_UPVOTES)
    public int captionUpvotes;
    @SerializedName(TOP_CAPTION_COUNT)
    public int topCaptionCount;

    public static final String GAMES_CREATED = "gamesCreated";
    public static final String HIGHEST_GAME_UPVOTE = "highestGameUpvote";
    public static final String CAPTIONS_CREATED = "captionsCreated";
    public static final String CAPTION_UPVOTES = "captionUpvotes";
    public static final String TOP_CAPTION_COUNT = "topCaptionCount";


}

package com.snaptiongame.app.data.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author Tyler Wong
 */
public class Game {

    @SerializedName(ID)
    public int id;
    @SerializedName(USER_ID)
    public int userId;
    @SerializedName(START_DATE)
    public long startDate;
    @SerializedName(END_DATE)
    public long endDate;
    @SerializedName(IS_PUBLIC)
    public boolean isPublic;
    @SerializedName(IMG_TYPE)
    public String type;
    @SerializedName(CREATOR_NAME)
    public String creatorName;
    @SerializedName(IMAGE_URL)
    public String creatorImage;
    @SerializedName(CREATOR_ID)
    public int creatorId;
    @SerializedName(PICTURE)
    public String picture;
    @SerializedName(IMAGE_URL)
    public String imageUrl;
    @SerializedName(IMAGE_WIDTH)
    public int imageWidth;
    @SerializedName(IMAGE_HEIGHT)
    public int imageHeight;
    @SerializedName(TOP_CAPTION)
    public Caption topCaption;
    @SerializedName(USERS)
    public List<User> users;
    @SerializedName(TAGS)
    public List<Tag> tags;
    public List<String> sendTags;
    @SerializedName(FRIENDS)
    public List<Integer> friendIds;
    @SerializedName(BEEN_UPVOTED)
    public boolean beenUpvoted;
    @SerializedName(BEEN_FLAGGED)
    public boolean beenFlagged;
    @SerializedName(GAME_DURATION)
    public long gameDuration;
    @SerializedName(NUM_UPVOTES)
    public int numUpvotes;
    @SerializedName(IS_CLOSED)
    public boolean isClosed;

    public static final String ID = "id";
    public static final String USER_ID = "userId";
    public static final String START_DATE = "creationDate";
    public static final String END_DATE = "completedDate";
    public static final String IS_PUBLIC = "isPublic";
    public static final String PICKER = "picker";
    public static final String CREATOR_ID = "creator_id";
    public static final String CREATOR_NAME = "picker_name";
    public static final String IMG_TYPE = "imgType";
    public static final String PICTURE = "picture";
    public static final String IMAGE_URL = "url";
    public static final String IMAGE_WIDTH = "width";
    public static final String IMAGE_HEIGHT = "height";
    public static final String TOP_CAPTION = "topCaption";
    public static final String FITB_BEFORE = "fitbBefore";
    public static final String FITB_AFTER = "fitbAfter";
    public static final String TAGS = "tags";
    public static final String FRIENDS = "invitedUsers";
    public static final String USERS = "users";
    public static final String BEEN_UPVOTED = "beenUpvoted";
    public static final String BEEN_FLAGGED = "beenFlagged";
    public static final String GAME_DURATION = "gameDuration";
    public static final String NUM_UPVOTES = "numUpvotes";
    public static final String CREATOR_IMAGE = "creatorImage";
    public static final String IS_CLOSED = "isClosed";


    public Game() {

    }

    public Game(int userId, boolean isPublic, String picture, String type,
                List<String> tags, List<Integer> friendIds, long gameDuration) {
        this.userId = userId;
        this.isPublic = isPublic;
        this.picture = picture;
        this.type = type;
        this.sendTags = tags;
        this.friendIds = friendIds;
        this.gameDuration = gameDuration;
    }

    public Game(int id, long startDate, boolean isPublic, int creatorId, String picture, String type) {
        this.id = id;
        this.startDate = startDate;
        this.isPublic = isPublic;
        this.creatorId = creatorId;
        this.picture = picture;
        this.type = type;
    }

    @Override
    public boolean equals(Object obj) {
        return obj.getClass() == Game.class
                && this.id == ((Game) obj).id
                && this.beenUpvoted == ((Game) obj).beenUpvoted
                && this.beenFlagged == ((Game) obj).beenFlagged
                && ((this.topCaption == null && ((Game) obj).topCaption == null)
                || (this.topCaption != null && this.topCaption.equals(((Game) obj).topCaption)));
    }
}

package com.snaptiongame.app.data.models;

import com.google.gson.annotations.SerializedName;

/**
 * @author Tyler Wong
 */

public class ActivityFeedItem {
    @SerializedName(ID)
    public int id;
    @SerializedName(DATE)
    public long date;
    @SerializedName(TYPE)
    public int type;
    @SerializedName(USER)
    public User user;
    @SerializedName(FRIEND)
    public User friend;
    @SerializedName(GAME)
    public Game game;
    @SerializedName(CAPTION)
    public Caption caption;

    public static final String ID = "id";
    public static final String DATE = "date";
    public static final String TYPE = "type";
    public static final String USER = "user";
    public static final String FRIEND = "friend";
    public static final String GAME = "game";
    public static final String CAPTION = "caption";

    public ActivityFeedItem(int id, long date, int type, User user, User friend, Game game, Caption caption) {
        this.id = id;
        this.date = date;
        this.type = type;
        this.user = user;
        this.friend = friend;
        this.game = game;
        this.caption = caption;
    }
}

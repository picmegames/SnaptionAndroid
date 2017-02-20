package com.snaptiongame.snaptionapp.data.models;

/**
 * @author Tyler Wong
 */

public class Session {
    public int userId;

    public static final String USER = "user_id";

    public Session(int userId) {
        this.userId = userId;
    }
}

package com.snaptiongame.app.data.models;

/**
 * @author Nick Romero
 */

public class AddFriendRequest {
    public int friendId;

    public static final String FRIEND_ID = "friendId";

    public AddFriendRequest(int friendId) {
        this.friendId = friendId;
    }
}

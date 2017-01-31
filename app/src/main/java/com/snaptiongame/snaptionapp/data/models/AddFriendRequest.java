package com.snaptiongame.snaptionapp.data.models;

/**
 * Created by nickromero on 1/30/17.
 */

public class AddFriendRequest {
    public int mFriendId;

    public static final String FRIEND_ID = "friendId";

    public AddFriendRequest(int friendId) {
        mFriendId = friendId;
    }
}

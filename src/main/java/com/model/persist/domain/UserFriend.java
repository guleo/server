package com.model.persist.domain;

/**
 * Created by frank on 2016/3/21.
 */
public class UserFriend {
    private int UserId;
    private int FriendId;

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public int getFriendId() {
        return FriendId;
    }

    public void setFriendId(int friendId) {
        FriendId = friendId;
    }
}

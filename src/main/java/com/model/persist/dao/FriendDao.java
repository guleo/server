package com.model.persist.dao;

import com.model.persist.domain.UserInfo;

import java.util.List;

/**
 * Created by frank on 2016/3/21.
 * 用户好友数据操作相关接口
 */
public interface FriendDao {

    String SELECT_USER_ID1_BY_USERNAME = "SELECT * FROM qui_user_friend WHERE " +
            "user_id1 = ?";
    String SELECT_USER_ID2_BY_USERNAME = "SELECT * FROM qui_user_friend WHERE " +
            "user_id2 = ?";
    String ADD_USER_FRIEND = "INSERT INTO qui_user_friend_add(friend_commit,friend_accept) VALUES (?,?)";
    String ACCEPT_USER_FRIEND = "INSERT INTO qui_user_friend(user_id1,user_id2) VALUES (?,?)";
    String DELETE_USER_FRIEND_BY_ID = "DELETE FROM qui_user_friend WHERE " +
            "user_id1 = ? AND user_id2 = ?";
    String SELECT_USER_ACCEPT_BY_ID = "SELECT friend_commit FROM qui_user_friend_add WHERE friend_accept = ?";
    String DELETE_USER_FRIEND_ADD_BY_ID = "DELETE FROM qui_user_friend_add WHERE " +
            "friend_commit = ? AND friend_accept = ?";
    List<UserInfo> getUserFriendsByUsername(String username);

    boolean addUserFriend(int userId, int friendId);

    boolean deleteUserFriend(int userId, int friendId);

    boolean deleteUserFriendAddById(int commit,int accept);
    boolean acceptUserFriend(int userId, int friendId);

    List<String> getUserAcceptsById(int id);
}

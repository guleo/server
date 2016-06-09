package com.model.persist.dao;

import com.model.persist.domain.UserInfo;
import com.model.persist.domain.UserBase;

/**
 * Created by frank on 2016/3/5.
 */
public interface UserDao {


    String SELECT_USER_INFO_BY_ID = "SELECT * FROM qui_user_info " +
            "WHERE user_id = ?";


    String SELECT_USER_LOGIN_BY_ID = "SELECT * FROM qui_user_base " +
            "WHERE user_id = ?";


    String INSERT_USER_LOGIN = "INSERT INTO qui_user_base (user_name,user_pwd) " +
            "VALUES (?,?)";


    String INSERT_USER_INFO = "INSERT INTO qui_user_info " +
            "VALUES (?,?,?,?)";


    String UPDATE_USER_INFO_BY_ID = "UPDATE qui_user_info " +
            "WHERE user_id = ?";

    String SELECT_USER_BASE_BY_USERNMAE = "SELECT * FROM qui_user_base " +
            "WHERE user_name = ?";

    String UPDATE_USER_INFO_HEAD_BY_USERNAME = "UPDATE qui_user_info SET user_head = ? WHERE user_id = (" +
            "SELECT user_id FROM qui_user_base WHERE user_name = ?)";

    String SELECT_USER_INFO_BY_USERNAME = "SELECT * FROM qui_user_info WHERE user_id = (" +
            "SELECT user_id FROM qui_user_base WHERE user_name = ?)";

    UserBase getUserBaseById(int id);
    boolean insertUserBase(UserBase user);

    UserInfo getUserInfoById(int id);
    UserInfo getUserInfoByName(String username);
    boolean insertUserInfo(UserInfo info);
    UserBase getUserBaseByName(String username);
    boolean updateUserInfo(String head,String username);
}

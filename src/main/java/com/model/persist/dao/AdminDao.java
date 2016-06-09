package com.model.persist.dao;

import com.model.persist.domain.Admin;
import com.model.persist.domain.UserInfo;

/**
 * Created by frank on 2016/5/20.
 * 管理员登录操作
 */
public interface AdminDao {
    String SELECT_ADMIN_BY_ID = "SELECT * FROM qui_admin " +
            "WHERE admin_id = ?";

    String INSERT_ADMIN_LOGIN = "INSERT INTO qui_user_base (user_name,user_pwd) " +
            "VALUES (?,?)";


    String INSERT_ADMIN_INFO = "INSERT INTO qui_user_info " +
            "VALUES (?,?,?,?)";


    String UPDATE_AMDIN_BY_ID = "UPDATE qui_user_info " +
            "WHERE user_id = ?";

    String SELECT_ADMIN_BY_USERNMAE = "SELECT * FROM qui_admin " +
            "WHERE admin_name = ?";


    Admin getAdminById(int id);

    Admin getAdminInfoById(int id);
    Admin getAdminByName(String username);
    boolean insertAdmin(UserInfo info);
    boolean updateAdmin(String head,String username);
}

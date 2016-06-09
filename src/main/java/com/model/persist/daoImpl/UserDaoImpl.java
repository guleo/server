package com.model.persist.daoImpl;

import com.model.persist.dao.UserDao;
import com.model.persist.domain.UserBase;
import com.model.persist.domain.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import com.model.util.Utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by frank on 2016/3/5.
 */
@Service
public class UserDaoImpl implements UserDao {


    @Qualifier("jdbcTemplate")
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public UserBase getUserBaseById(int id) {
        List<UserBase> list = new ArrayList();
        list.addAll(jdbcTemplate.query(SELECT_USER_LOGIN_BY_ID, new Object[]{id}, new UserBaseRowMapper()));
        if (list.size() > 0)
            return list.get(0);
        return null;
    }

    @Override
    public boolean insertUserBase(UserBase user) {
        return jdbcTemplate.update(INSERT_USER_LOGIN, user.getUsername(), user.getPassword()) != 0;
    }

    @Override
    public UserInfo getUserInfoById(int id) {
        List<UserInfo> list = new ArrayList();
        list.addAll(jdbcTemplate.query(SELECT_USER_INFO_BY_ID, new Object[]{id}, new UserInfoRowMapper()));
        if (list.size() > 0)
            return list.get(0);
        return null;
    }

    @Override
    public boolean insertUserInfo(UserInfo info) {
        return jdbcTemplate.update(INSERT_USER_INFO, Utils.toObjects(info)) != 0;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private class UserBaseRowMapper implements RowMapper<UserBase> {

        @Override
        public UserBase mapRow(ResultSet resultSet, int i) throws SQLException {
            UserBase userBase = new UserBase();
            userBase.setUserId(resultSet.getInt("user_id"));
            userBase.setPassword(resultSet.getString("user_pwd"));
            userBase.setUsername(resultSet.getString("user_name"));
            return userBase;
        }
    }

    private class UserInfoRowMapper implements RowMapper<UserInfo> {

        @Override
        public UserInfo mapRow(ResultSet resultSet, int i) throws SQLException {
            UserInfo userInfo = new UserInfo();
            userInfo.setUserId(resultSet.getInt("user_id"));
            userInfo.setUserHead(resultSet.getString("user_head"));
            userInfo.setUserSex(resultSet.getInt("user_sex"));
            userInfo.setUserSchool(resultSet.getString("user_school"));
            return userInfo;
        }
    }

    @Override
    public UserBase getUserBaseByName(String username) {
        List<UserBase> list = new ArrayList();
        list.addAll(jdbcTemplate.query(SELECT_USER_BASE_BY_USERNMAE, new Object[]{username}, new UserBaseRowMapper()));
        if (list.size() > 0)
            return list.get(0);
        return null;
    }

    @Override
    public boolean updateUserInfo(String head, String username) {
        return jdbcTemplate.update(UPDATE_USER_INFO_HEAD_BY_USERNAME, head, username) > 0;
    }

    @Override
    public UserInfo getUserInfoByName(String username) {
        List<UserInfo> list= new ArrayList();
        list.addAll(jdbcTemplate.query(SELECT_USER_INFO_BY_USERNAME,new Object[]{username},new UserInfoRowMapper()));
        if (list.size() > 0)
            return list.get(0);
        return null;
    }
}

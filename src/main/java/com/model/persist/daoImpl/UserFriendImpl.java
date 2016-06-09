package com.model.persist.daoImpl;

import com.model.persist.dao.FriendDao;
import com.model.persist.domain.UserFriend;
import com.model.persist.domain.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by frank on 2016/3/21.
 * 用户好友相关操作
 */

@Service
public class UserFriendImpl implements FriendDao {

    @Qualifier("jdbcTemplate")
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<UserInfo> getUserFriendsByUsername(String username) {

        ApplicationContext ctx = new ClassPathXmlApplicationContext("persist.xml");
        UserDaoImpl userDao = (UserDaoImpl) ctx.getBean("userDao");
        int id = userDao.getUserBaseByName(username).getUserId();
        List<UserFriend> list = new ArrayList();
        list.addAll(jdbcTemplate.query(SELECT_USER_ID1_BY_USERNAME, new Object[]{id}, new UserFriendRowMapper()));
        int index = list.size();
        list.addAll(jdbcTemplate.query(SELECT_USER_ID2_BY_USERNAME, new Object[]{id}, new UserFriendRowMapper()));
        List<UserInfo> friends = new ArrayList();

        for (int i = 0; i < list.size(); i++) {
            UserInfo info;
            UserFriend friend = list.get(i);
            System.out.println("id " + friend.getFriendId());
            if (i < index)
                info = userDao.getUserInfoById(friend.getFriendId());
            else
                info = userDao.getUserInfoById(friend.getUserId());
            System.out.println("info " + info.getUserHead());
            friends.add(info);
        }
        return friends;
    }

    @Override
    public boolean addUserFriend(int userId, int friendId) {
        return jdbcTemplate.update(ADD_USER_FRIEND, userId, friendId) > 0;
    }

    @Override
    public boolean deleteUserFriend(int userId, int friendId) {
        return jdbcTemplate.update(DELETE_USER_FRIEND_BY_ID, userId, friendId) > 0;
    }

    @Override
    public boolean deleteUserFriendAddById(int commit, int accept) {
        return jdbcTemplate.update(DELETE_USER_FRIEND_ADD_BY_ID, commit, accept) > 0;
    }

    @Override
    public boolean acceptUserFriend(int userId, int friendId) {
        return jdbcTemplate.update(ACCEPT_USER_FRIEND, userId, friendId) > 0;
    }

    @Override
    public List<String> getUserAcceptsById(int id) {
        return jdbcTemplate.query(SELECT_USER_ACCEPT_BY_ID, new Object[]{id}, (resultSet, i) -> {
            return resultSet.getString("friend_commit");
        });
    }

    private class UserFriendRowMapper implements RowMapper<UserFriend> {

        @Override
        public UserFriend mapRow(ResultSet resultSet, int i) throws SQLException {
            UserFriend userFriend = new UserFriend();
            userFriend.setUserId(resultSet.getInt("user_id1"));
            userFriend.setFriendId(resultSet.getInt("user_id2"));
            return userFriend;
        }
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}

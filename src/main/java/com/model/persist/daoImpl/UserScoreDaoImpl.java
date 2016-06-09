package com.model.persist.daoImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import com.model.persist.dao.UserScoreDao;
import com.model.persist.domain.UserScore;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by frank on 2016/4/23.
 * 操作用户数据
 */
@Service
public class UserScoreDaoImpl implements UserScoreDao {

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    @Qualifier("jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Override
    public UserScore getUserScore(int id) {
        List<UserScore> list = jdbcTemplate.query(SELECT_USER_SCORE_BY_ID, new Object[]{id}, new UserScoreRowMapper());
        if (list != null && list.size() > 0)
            return list.get(0);
        return null;
    }

    @Override
    public boolean setUserScore(UserScore userScore) {
        System.out.println("id:"+userScore.getUserId());
        return jdbcTemplate.update(UPDATE_USER_SCORE_BY_ID, userScore.getWin(), userScore.getLose(), userScore.
                getDraw(), userScore.getTime(), userScore.getScore(), userScore.getUserId()) != 0;
    }

    private class UserScoreRowMapper implements RowMapper<UserScore> {

        @Override
        public UserScore mapRow(ResultSet resultSet, int i) throws SQLException {
            UserScore userScore = new UserScore();
            userScore.setWin(resultSet.getInt("user_win"));
            userScore.setDraw(resultSet.getInt("user_draw"));
            userScore.setLose(resultSet.getInt("user_lose"));
            userScore.setTime(resultSet.getInt("user_time"));
            userScore.setScore(resultSet.getFloat("user_score"));
            userScore.setUserId(resultSet.getInt("user_id"));
            return userScore;
        }
    }
}

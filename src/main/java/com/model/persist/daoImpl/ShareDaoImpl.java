package com.model.persist.daoImpl;

import com.model.persist.dao.ShareDao;
import com.model.persist.domain.Question;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by frank on 2016/5/22.
 * ²Ù×÷Ìâ¿â
 */
public class ShareDaoImpl implements ShareDao {

    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Question> getQueShare() {
        List<Question> list = jdbcTemplate.query(SELECT_SHARE, new ShareRowMapper());
        if (list.size() > 0)
            return list;
        return null;
    }

    @Override
    public boolean delQueShareById(int id) {
        return jdbcTemplate.update(DELETE_SHARE_BY_ID, id) != 0;
    }

    @Override
    public Question getQueShareById(int id) {
        List<Question> list = jdbcTemplate.query(SELECT_SHARE_BY_ID, new Object[]{id}, new ShareRowMapper());
        if (list.size() > 0)
            return list.get(0);
        return null;
    }

    @Override
    public List<Question> getQueShareByClass(int id, int type) {
        List<Question> list;
        if (type == 0)
            list = jdbcTemplate.query(FIND_SHARE_BY_FIRST_CLASS, new Object[]{id}, new ShareRowMapper());
        else
            list = jdbcTemplate.query(FIND_SHARE_BY_SUB_CLASS, new Object[]{id}, new ShareRowMapper());
        return list;
    }

    private class ShareRowMapper implements RowMapper<Question> {

        @Override
        public Question mapRow(ResultSet resultSet, int i) throws SQLException {
            String text = resultSet.getString("qui_text");
            String wrong1 = resultSet.getString("qui_wrong1");
            String wrong2 = resultSet.getString("qui_wrong2");
            String wrong3 = resultSet.getString("qui_wrong3");
            String right = resultSet.getString("qui_right");
            String time = resultSet.getString("qui_time");
            int firstClass = resultSet.getInt("first_class");
            int subClass = resultSet.getInt("sub_class");
            int commit_id = resultSet.getInt("qui_commit");
            int id = resultSet.getInt("share_id");
            Question que = new Question();
            que.setText(text);
            que.setWrong2(wrong2);
            que.setWrong1(wrong1);
            que.setWrong3(wrong3);
            que.setRight(right);
            que.setFirstClass(firstClass);
            que.setSubClass(subClass);
            que.setTime(time);
            que.setCommit_id(commit_id);
            que.setId(id);
            return que;
        }
    }
}

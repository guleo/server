package com.model.persist.daoImpl;

import com.model.persist.dao.QuestionDao;
import com.model.persist.domain.Question;
import com.model.persist.domain.QuestionClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by frank on 2016/3/5.
 * 题库操作接口
 */

@Service
public class QuestionDaoImpl implements QuestionDao {

    @Qualifier("jdbcTemplate")
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<QuestionClass> findSubClassById(int first_class_id) {
        List<QuestionClass> list = jdbcTemplate.query(FIND_SUB_CLASS_BY_ID, new Object[]{first_class_id}, new SubClassRowMapper());
        if (list.size() > 0)
            return list;
        return null;
    }

    @Override
    public List<QuestionClass> selectFirstClass() {
        List<QuestionClass> list = jdbcTemplate.query(SELECT_FIRST_CLASS, new Object[]{}, new FirstClassRowMapper());
        if (list.size() > 0)
            return list;
        return null;
    }

    @Override
    public List<Question> selectQuestion(String subClassName) {
        List<Question> list = jdbcTemplate.query(SELECT_QUESTION, new Object[] {subClassName},new QuestionRowMapper());
        if (list.size() > 0)
            return list;
        return null;
    }

    @Override
    public boolean insertQuestionShare(Object[] o) {
        return jdbcTemplate.update(INSERT_QUESTION_SHARE,o) != 0;
    }

    @Override
    public boolean insertQuestion(Object[] o) {
        return jdbcTemplate.update(INSERT_QUESTION,o) != 0;
    }

    @Override
    public List<QuestionClass> selectFirstClassByName(String name) {
        List<QuestionClass> list = jdbcTemplate.query(FIND_FIRST_CLASS_BY_NAME,new Object[] {name},new FirstClassRowMapper());
        if (list.size() > 0)
            return list;
        return null;
    }

    @Override
    public List<QuestionClass> selectSubClassByName(String name) {
        List<QuestionClass> list = jdbcTemplate.query(FIND_SUB_CLASS_BY_NAME,new Object[] {name},new SubClassRowMapper());
        if (list.size() > 0)
            return list;
        return null;
    }

    @Override
    public String getFirstClassNameById(int first_id) {
        List<String>list = jdbcTemplate.query(FIND_FIRST_CLASS_NAME_BY_ID, new Object[]{first_id}, (resultSet, i) -> {
            return resultSet.getString("first_class_name");
        });
        if (list.size() > 0)
            return list.get(0);
        return null;
    }

    @Override
    public String getSubClassNameById(int sub_id) {
        List<String>list = jdbcTemplate.query(FIND_SUB_CLASS_NAME_BY_ID, new Object[]{sub_id}, (resultSet, i) -> {
            return resultSet.getString("sub_class_name");
        });
        if (list.size() > 0)
            return list.get(0);
        return null;
    }

    @Override
    public int getFirstClassIdByName(String name) {
        List<Integer>list = jdbcTemplate.query(FIND_FIRST_CLASS_ID_BY_NAME, new Object[]{name}, (resultSet, i) -> {
            return resultSet.getInt("first_class_id");
        });
        if (list.size() > 0)
            return list.get(0);
        return -1;
    }

    private class QuestionRowMapper implements  RowMapper<Question> {

        @Override
        public Question mapRow(ResultSet resultSet, int i) throws SQLException {
            Question question = new Question();
            question.setText(resultSet.getString("qui_text"));
            question.setRight(resultSet.getString("qui_right"));
            question.setWrong1(resultSet.getString("qui_wrong1"));
            question.setWrong2(resultSet.getString("qui_wrong2"));
            question.setWrong3(resultSet.getString("qui_wrong3"));
            return question;
        }
    }
    private class FirstClassRowMapper implements RowMapper<QuestionClass> {

        @Override
        public QuestionClass mapRow(ResultSet resultSet, int i) throws SQLException {
            QuestionClass firstClass = new QuestionClass();
            firstClass.setClassName(resultSet.getString("first_class_name"));
            firstClass.setClassId(resultSet.getInt("first_class_id"));
            return firstClass;
        }
    }

    private class SubClassRowMapper implements RowMapper<QuestionClass> {

        @Override
        public QuestionClass mapRow(ResultSet resultSet, int i) throws SQLException {
            QuestionClass subClass = new QuestionClass();
            subClass.setClassName(resultSet.getString("sub_class_name"));
            subClass.setClassId(resultSet.getInt("sub_class_id"));
            return subClass;
        }
    }
}

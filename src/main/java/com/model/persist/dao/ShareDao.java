package com.model.persist.dao;

import com.model.persist.domain.Question;
import java.util.List;

/**
 * Created by frank on 2016/5/22.
 * 题库操作接口
 */
public interface ShareDao {
    String SELECT_SHARE = "SELECT * FROM qui_question_share";
    String DELETE_SHARE_BY_ID = "DELETE FROM qui_question_share WHERE share_id = ?";
    String SELECT_SHARE_BY_ID = "SELECT * FROM qui_question_share WHERE share_id = ?";
    String FIND_SHARE_BY_FIRST_CLASS = "SELECT * FROM qui_question_share WHERE first_class=?";
    String FIND_SHARE_BY_SUB_CLASS = "SELECT * FROM qui_question_share WHERE sub_class=?";
    List<Question> getQueShare();
    boolean delQueShareById(int id);
    Question getQueShareById(int id);

    List<Question> getQueShareByClass(int id,int type);
}

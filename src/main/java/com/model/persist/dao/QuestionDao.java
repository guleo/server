package com.model.persist.dao;

import com.model.persist.domain.Question;
import com.model.persist.domain.QuestionClass;

import java.util.List;

/**
 * Created by frank on 2016/3/5.
 */
public interface QuestionDao {
    String INSERT_QUESTION = "INSERT INTO qui_question (first_class,sub_class,qui_text,qui_right,qui_wrong1,qui_wrong2,qui_wrong3) " +
            "VALUES (?,?,?,?,?,?,?)";
    String INSERT_QUESTION_SHARE = "INSERT INTO qui_question_share (first_class,sub_class,qui_text,qui_right,qui_wrong1,qui_wrong2,qui_wrong3,qui_time,qui_commit) " +
            "VALUES (?,?,?,?,?,?,?,?,?)";
    String SELECT_QUESTION = "SELECT * FROM qui_question WHERE sub_class=(" +
            "SELECT sub_class_id FROM qui_question_sub_class WHERE sub_class_name=?)";
    String FIND_SUB_CLASS_BY_ID = "SELECT * FROM qui_question_sub_class WHERE first_class_id = ?";
    String SELECT_FIRST_CLASS = "SELECT * FROM  qui_question_first_class";
    String FIND_FIRST_CLASS_BY_NAME = "SELECT * FROM qui_question_first_class WHERE first_class_name = ?";
    String FIND_SUB_CLASS_BY_NAME = "SELECT * FROM qui_question_sub_class WHERE sub_class_name = ?";
    String FIND_FIRST_CLASS_NAME_BY_ID = "SELECT first_class_name FROM qui_question_first_class WHERE first_class_id = ?";
    String FIND_SUB_CLASS_NAME_BY_ID = "SELECT sub_class_name FROM qui_question_sub_class WHERE sub_class_id = ?";
    String FIND_FIRST_CLASS_ID_BY_NAME = "SELECT first_class_id FROM qui_question_sub_class WHERE sub_class_name = ?";

    List<QuestionClass> findSubClassById(int first_class_id);

    List<QuestionClass> selectFirstClass();

    List<Question> selectQuestion(String subClass_Name);

    boolean insertQuestionShare(Object[] o);

    boolean insertQuestion(Object[] o);

    List<QuestionClass> selectFirstClassByName(String name);

    List<QuestionClass> selectSubClassByName(String name);

    String getFirstClassNameById(int first_id);

    String getSubClassNameById(int sub_id);

    int getFirstClassIdByName(String name);
}

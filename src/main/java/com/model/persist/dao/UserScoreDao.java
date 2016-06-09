package com.model.persist.dao;

import com.model.persist.domain.UserScore;

/**
 * Created by frank on 2016/4/23.
 * 用户对战数据接口
 */
public interface UserScoreDao {
    String SELECT_USER_SCORE_BY_ID = "SELECT * FROM qui_user_score WHERE user_id = ?";
    String UPDATE_USER_SCORE_BY_ID = "UPDATE qui_user_score SET user_win = ?, user_lose = ?," +
            "user_draw=?,user_time=?,user_score=? WHERE user_id = ?";

    UserScore getUserScore(int id);
    boolean setUserScore(UserScore userScore);
}

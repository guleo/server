package com.model.servlet;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.model.persist.dao.UserDao;
import com.model.persist.daoImpl.UserScoreDaoImpl;
import com.model.persist.domain.UserBase;
import com.model.persist.domain.UserScore;
import com.model.util.Utils;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by frank on 2016/4/23.
 * 对战结果写进数据库中
 */
public class ResultServlet extends HttpServlet {

    private UserScore userScore;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = new String(req.getParameter("username").getBytes("iso-8859-1"),"utf-8");
        String score = req.getParameter("score");
        String time = req.getParameter("time");
        String result = req.getParameter("result");
        ApplicationContext ctx = new ClassPathXmlApplicationContext("persist.xml");
        UserScoreDaoImpl userScoreDao = (UserScoreDaoImpl) ctx.getBean("userScoreDao");
        UserDao userDao = (UserDao) ctx.getBean("userDao");
        System.out.println("result:username " + result);
        UserBase userBase = userDao.getUserBaseByName(username);
        int id = userBase.getUserId();
        userScore = userScoreDao.getUserScore(id);
        if (userScore != null) {
            addResult(score, time, result);
            boolean isSend = userScoreDao.setUserScore(userScore);
            System.out.println("result:" + result);
            resp.getOutputStream().write((isSend+"").getBytes());
        }
    }

    private void addResult(String score, String time, String result) {
        if (userScore != null) {
            int draw = userScore.getDraw();
            int win = userScore.getWin();
            int lose = userScore.getLose();
            switch (result) {
                case Utils.SHOW_DRAW:
                    draw++;
                    userScore.setDraw(draw);
                    break;
                case Utils.SHOW_FAIL:
                    lose++;
                    userScore.setLose(lose);
                    break;
                case Utils.SHOW_SUC:
                    win++;
                    userScore.setWin(win);
                    break;
            }

            int sum = draw + win + lose - 1;
            float user_score = (Integer.parseInt(score) + sum * userScore.getScore()) / (sum + 1);
            userScore.setScore(user_score);
            float user_time = (Integer.parseInt(time) + sum * userScore.getTime()) / (sum + 1);
            userScore.setTime((int) user_time);
        }
    }
}

package com.model.servlet;

import org.json.JSONStringer;
import org.json.JSONWriter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.model.persist.daoImpl.UserDaoImpl;
import com.model.persist.daoImpl.UserScoreDaoImpl;
import com.model.persist.domain.UserScore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by frank on 2016/4/26.
 * 用户数据请求
 */
public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = new String(req.getParameter("username").getBytes("iso-8859-1"), "utf-8");
        ApplicationContext ctx = new ClassPathXmlApplicationContext("persist.xml");
        UserDaoImpl userDao = (UserDaoImpl) ctx.getBean("userDao");
        int id = userDao.getUserBaseByName(username).getUserId();
        UserScoreDaoImpl userScoreDao = (UserScoreDaoImpl) ctx.getBean("userScoreDao");
        UserScore userScore = userScoreDao.getUserScore(id);
        if (userScore != null) {
            int win = userScore.getWin();
            int lose = userScore.getLose();
            int draw = userScore.getDraw();
            float score = userScore.getScore();
            JSONStringer stringer = new JSONStringer();
            JSONWriter writer = stringer.object();
            writer.key("win").value(win);
            writer.key("lose").value(lose);
            writer.key("score").value(score);
            writer.key("draw").value(draw);
            writer.endObject();
            resp.getWriter().write(stringer.toString());
            System.out.println(stringer.toString());
        }
    }
}

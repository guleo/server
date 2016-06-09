package com.model.servlet;

import com.model.persist.daoImpl.QuestionDaoImpl;
import com.model.persist.daoImpl.UserDaoImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by frank on 2016/5/3.
 * 贡献一题处理请求
 */
public class ShareServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String firstClass = req.getParameter("first");
        if (firstClass != null)
            firstClass = new String(firstClass.getBytes("iso-8859-1"), "utf-8");
        String subClass = new String(req.getParameter("sub").getBytes("iso-8859-1"), "utf-8");
        String text = new String(req.getParameter("text").getBytes("iso-8859-1"), "utf-8");
        String right = new String(req.getParameter("right").getBytes("iso-8859-1"), "utf-8");
        String wrong1 = new String(req.getParameter("wrong1").getBytes("iso-8859-1"), "utf-8");
        String wrong2 = new String(req.getParameter("wrong2").getBytes("iso-8859-1"), "utf-8");
        String wrong3 = new String(req.getParameter("wrong3").getBytes("iso-8859-1"), "utf-8");
        String username = req.getParameter("username");
        if (firstClass != null)
            username = new String(username.getBytes("iso-8859-1"), "utf-8");
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(date);
        ApplicationContext ctx = new ClassPathXmlApplicationContext("persist.xml");
        QuestionDaoImpl questionDao = (QuestionDaoImpl) ctx.getBean("questionDao");
        UserDaoImpl userDao = (UserDaoImpl) ctx.getBean("userDao");
        int userId = -1;
        if (username != null)
            userId = userDao.getUserBaseByName(username).getUserId();
        int firstId;
        if (firstClass != null)
            firstId = questionDao.selectFirstClassByName(firstClass).get(0).getClassId();
        else firstId = questionDao.getFirstClassIdByName(subClass);

        int subId = questionDao.selectSubClassByName(subClass).get(0).getClassId();
        boolean send = questionDao.insertQuestionShare(new Object[]{firstId, subId, text, right, wrong1, wrong2, wrong3, time, userId});
        if(userId == -1) {
            String info = "fail";
            if (send)
                info = "success";

            resp.getOutputStream().write(info.getBytes());
        }
        else
        resp.getOutputStream().write((send + "").getBytes());
    }
}

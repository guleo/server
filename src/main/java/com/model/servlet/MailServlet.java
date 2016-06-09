package com.model.servlet;

import com.model.persist.daoImpl.UserDaoImpl;
import com.model.persist.daoImpl.UserFriendImpl;
import org.json.JSONStringer;
import org.json.JSONWriter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by frank on 2016/5/30.
 * 处理获取消息请求
 */
public class MailServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = new String(req.getParameter("username").getBytes("iso-8859-1"),"utf-8");
        ApplicationContext ctx = new ClassPathXmlApplicationContext("persist.xml");
        UserDaoImpl userDao = (UserDaoImpl) ctx.getBean("userDao");
        int id = userDao.getUserBaseByName(username).getUserId();
        UserFriendImpl friendDao = (UserFriendImpl) ctx.getBean("friendDao");
        List<String> mails = friendDao.getUserAcceptsById(id);
        JSONStringer stringer = new JSONStringer();
        stringer.array();
        for (String mail : mails) {
            JSONWriter writer = stringer.object();
            int accept = Integer.parseInt(mail);
            String name = userDao.getUserBaseById(accept).getUsername();
            writer.key("mail").value(name);
            writer.endObject();
        }
        stringer.endArray();
        System.out.println(stringer.toString());
        resp.setContentType("text/json;charset=utf-8");
        resp.getWriter().write(stringer.toString());
    }
}

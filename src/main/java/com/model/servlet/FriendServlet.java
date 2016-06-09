package com.model.servlet;

import org.json.JSONStringer;
import org.json.JSONWriter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.model.persist.daoImpl.UserDaoImpl;
import com.model.persist.daoImpl.UserFriendImpl;
import com.model.persist.domain.UserInfo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by frank on 2016/3/21.
 * 处理显示用户好友的请求
 */
public class FriendServlet extends HttpServlet {

    private static final int SHOW_USER = 0;
    private static final int ADD_USER = 1;
    private static final int FIND_USER = 2;
    private static final int ACCEPT_USER = 3;
    private static final int CANCEL_USER = 4;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = new String(req.getParameter("username").getBytes("iso_8859-1"), "utf-8");
        int type = Integer.parseInt(req.getParameter("type"));
        ApplicationContext ctx = new ClassPathXmlApplicationContext("persist.xml");
        UserFriendImpl friendDao = (UserFriendImpl) ctx.getBean("friendDao");
        UserDaoImpl userDao = (UserDaoImpl) ctx.getBean("userDao");
        boolean send = false;
        resp.setContentType("text/json");
        resp.setCharacterEncoding("UTF-8");
        JSONStringer stringer = new JSONStringer();
        if (type == SHOW_USER) {
            List<UserInfo> list = friendDao.getUserFriendsByUsername(username);
            stringer.array();

            for (UserInfo info : list) {
                JSONWriter writer = stringer.object();
                writer.key("school").value(info.getUserSchool());
                writer.key("sex").value(info.getUserSex());
                writer.key("head").value(info.getUserHead());
                String name = userDao.getUserBaseById(info.getUserId()).getUsername();
                writer.key("username").value(name);
                writer.endObject();
            }
            stringer.endArray();
            resp.getWriter().write(stringer.toString());

        } else {
            UserInfo info = null;
            if (type == ADD_USER) {
                String user = new String(req.getParameter("user").getBytes("iso_8859-1"), "utf-8");
                int userId = userDao.getUserBaseByName(user).getUserId();
                int friendId = userDao.getUserBaseByName(username).getUserId();
                friendDao.addUserFriend(userId, friendId);
                info = userDao.getUserInfoById(friendId);
            }
            if (type == FIND_USER) {
                int userId = userDao.getUserBaseByName(username).getUserId();
                info = userDao.getUserInfoById(userId);
            }
            if (type == ACCEPT_USER || type == CANCEL_USER) {
                String user = new String(req.getParameter("user").getBytes("iso_8859-1"), "utf-8");
                int userId = userDao.getUserBaseByName(user).getUserId();
                int friendId = userDao.getUserBaseByName(username).getUserId();
                send = friendDao.deleteUserFriendAddById(friendId, userId);
                if (send && type == ACCEPT_USER) {
                    send = friendDao.acceptUserFriend(friendId, userId);
                }
            }
            if (info != null) {
                stringer.array();
                JSONWriter writer = stringer.object();
                writer.key("school").value(info.getUserSchool());
                writer.key("sex").value(info.getUserSex());
                writer.key("head").value(info.getUserHead());
                System.out.println(username);
                writer.key("username").value(username);
                writer.endObject();
                stringer.endArray();
                resp.getWriter().write(stringer.toString());
            } else {

                if (type == ACCEPT_USER || type == CANCEL_USER) {System.out.println(send);
                    resp.getWriter().write(send + "");}
                else
                    resp.getWriter().write("");
            }
        }
        resp.getWriter().flush();
        resp.getWriter().close();
    }
}

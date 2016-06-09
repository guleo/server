package com.model.servlet;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DuplicateKeyException;
import com.model.persist.daoImpl.UserDaoImpl;
import com.model.persist.domain.UserBase;
import com.model.persist.domain.UserInfo;
import com.model.util.Utils;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by frank on 2016/3/5.
 */

public class RegisterServlet extends HttpServlet {
    private static final String USER_DUP = "user duplicate";
    private static final String REG_FAIL = "fail";
    private static final String REG_SUC = "success";
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserInfo userInfo = new UserInfo();
        UserBase user = new UserBase();
        String[] fields = Utils.toString(user);
        String[] params = new String[fields.length];
        user.setUsername(new String(req.getParameter("username").getBytes("iso-8859-1"),"utf-8"));
        user.setPassword(req.getParameter("password"));

        ApplicationContext context = new ClassPathXmlApplicationContext("persist.xml");
        ServletOutputStream writer = resp.getOutputStream();
        UserDaoImpl userDao = (UserDaoImpl) context.getBean("userDao");
        try {
            Boolean success = userDao.insertUserBase(user);

            if (success) {
                user = userDao.getUserBaseByName(new String(req.getParameter("username").getBytes("iso-8859-1"),"utf-8"));
                if (user != null) {
                    userInfo.setUserId(user.getUserId());
                    userInfo.setUserSchool(new String(req.getParameter("userSchool").getBytes("iso-8859-1"),"utf-8"));
                    userInfo.setUserSex(Integer.parseInt(req.getParameter("userSex")));
                    success = success && userDao.insertUserInfo(userInfo);
                    if (success)
                        writer.write(REG_SUC.getBytes());
                    else writer.write(REG_FAIL.getBytes());
                }
            }
        }catch(DuplicateKeyException dup) {
            writer.write(USER_DUP.getBytes());
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
}

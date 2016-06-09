package com.model.servlet;
/**
 * Created by frank on 2016/3/5.
 */

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.model.persist.daoImpl.UserDaoImpl;
import com.model.persist.domain.UserBase;
import com.model.util.Utils;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String username = new String(req.getParameter("username").getBytes("iso-8859-1"), "utf-8");
        String password = new String(req.getParameter("password").getBytes("iso-8859-1"), "utf-8");
        String imgPath = "";
        ApplicationContext context = new ClassPathXmlApplicationContext("persist.xml");
        UserDaoImpl userDao = (UserDaoImpl) context.getBean("userDao");
        UserBase userBase = userDao.getUserBaseByName(username);
        String head = userDao.getUserInfoByName(username).getUserHead();
        Boolean success = false;
        if (userBase != null && userBase.getPassword().equals(password)) {
            success = true;
            if (head != null) {
                String url = req.getRequestURL().substring(0, req.getRequestURL().lastIndexOf("/") + 1);
                imgPath = url + Utils.IMG_PATH + head;
            }
        }
        ServletOutputStream writer = resp.getOutputStream();
        writer.write((success.toString() + "\r\n").getBytes());
        System.out.println(imgPath);
        writer.write(imgPath.getBytes());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}

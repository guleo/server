package com.model.servlet;

import com.model.persist.daoImpl.AdminDaoImpl;
import com.model.persist.domain.Admin;
import org.json.JSONStringer;
import org.json.JSONWriter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by frank on 2016/5/20.
 * 处理管理员登录请求
 */
@Controller
public class AdminLoginServlet {
    @RequestMapping(value = "/Login", method = RequestMethod.GET, produces = {"text/json;charset=utf-8"})
    @ResponseBody
    String SearchResultSe(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = new String(req.getParameter("username").getBytes("iso-8859-1"),"utf-8");
        String password = req.getParameter("password");
        ApplicationContext ctx = new ClassPathXmlApplicationContext("persist.xml");
        AdminDaoImpl adminDao = (AdminDaoImpl) ctx.getBean("adminDao");
        Admin admin = adminDao.getAdminByName(username);
        JSONStringer stringer = new JSONStringer();
        JSONWriter writer = stringer.object();
        if(admin != null) {
            if (password.trim().equals(admin.getPassword().trim())) {
                writer.key("result").value("success");
            }
        }else
            writer.key("result").value("fail");

        stringer.endObject();
        return stringer.toString();
    }
}

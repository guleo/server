package com.model.servlet;

import com.model.MatchNode;
import org.json.JSONStringer;
import org.json.JSONWriter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.model.persist.dao.UserDao;
import com.model.persist.domain.UserInfo;
import com.model.util.Utils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by frank on 2016/4/5.
 * 匹配处理servlet
 */
public class MatchServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/json;charset=utf-8");
        //sevlet 是单实例多线程，所以全局变量每次都要重置。
        MatchThread t = new MatchThread(req);
        t.start();
        String send;
        int count = 0;
        while (true) {

            synchronized (this) {
                try {
                    wait(1000);
                    count++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                send = t.getSend();
                if (send != null) {
                    resp.getWriter().write(send);
                    break;
                }
                if (count == 21)
                    break;
            }
        }
    }

    private class MatchThread extends Thread {
        private int count = 0;
        private boolean isStop = false;
        private String username;
        private String rival;
        private String questions;
        private String url;
        private MatchNode node;
        private String send;
        public MatchThread(HttpServletRequest req) {
            username = (String) req.getAttribute("username");
            url = (String) req.getAttribute("url");
            node = (MatchNode) req.getAttribute("node");
            System.out.println("username123:" + username);
        }

        @Override
        public void run() {
            boolean isPaired = false;
            count = 0;

            while (!isStop) {
                try {
                    if (node.isMatchPaired()) {
                        isStop = true;
                        isPaired = true;
                        questions = node.getQuestions();
                        rival = node.getRival();
                        System.out.println("rival:" + rival);
                    }
                    sleep(100);
                    count++;
                    if (count == 200)
                        isStop = true;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (isPaired) {

                ApplicationContext ctx = new ClassPathXmlApplicationContext("persist.xml");
                UserDao userDao = (UserDao) ctx.getBean("userDao");
                UserInfo userInfo = userDao.getUserInfoByName(rival);
                JSONStringer stringer = new JSONStringer();
                JSONWriter object = stringer.object();
                object.key("rival").value(rival);

                String imgPath = url + Utils.IMG_PATH + userInfo.getUserHead();
                String url_rival = node.getRivalUrl();
                System.out.println("hhh" + imgPath);
                object.key("image").value(imgPath);
                object.key("url_rival").value(url_rival);
                object.key("questions").value(questions);
                stringer.endObject();
                send = stringer.toString();
            }
        }
        public String getSend() {
            return send;
        }
    }

}

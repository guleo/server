package com.model.servlet;

import com.model.servlet.listener.AnswerListener;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;

/**
 * Created by frank on 2016/4/18.
 * 对方答题请求
 */
public class AnswerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String address = req.getRemoteAddr();
        String rival = req.getParameter("rival");
        String right = req.getParameter("right");
        String rivalUrl = req.getParameter("url");
        String score = req.getParameter("score");
        System.out.println("score:" + score);
        System.out.println("address:" + address);
        System.out.println("rivalAddress:" + rivalUrl);
        String send = right + "," + score + "\n";
        String out;
        Socket socket = new Socket(rivalUrl, 10000);
        socket.getOutputStream().write(send.getBytes());
        while (true) {
            Map map = new AnswerListener().getURLs();
            if (map.containsKey(rival)) {
                out = (String) map.get(rival);
                break;
            }
        }
        resp.getOutputStream().write(out.getBytes());
    }
}

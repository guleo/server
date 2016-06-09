package com.model.servlet.listener;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by frank on 2016/4/3.
 * 记录用户登录信息
 */
public class UserListener implements HttpSessionListener {

    private List<String> users = new ArrayList();

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        users.add((String) httpSessionEvent.getSession().getAttribute("username"));
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
       users.remove(httpSessionEvent.getSession().getAttribute("username"));
    }
}

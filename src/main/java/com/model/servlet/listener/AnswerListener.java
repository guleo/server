package com.model.servlet.listener;

import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by frank on 2016/4/18.
 * ¼àÌý¶Ô·½µØÖ·
 */
public class AnswerListener implements ServletRequestListener {

    private static Map<String,String> userURL = new HashMap();
    @Override
    public void requestDestroyed(ServletRequestEvent servletRequestEvent) {
        ServletRequest req = servletRequestEvent.getServletRequest();
        String user = req.getParameter("username");
        userURL.remove(user);
    }

    @Override
    public void requestInitialized(ServletRequestEvent servletRequestEvent) {
        ServletRequest req = servletRequestEvent.getServletRequest();
        String user = req.getParameter("username");
        String url = req.getRemoteAddr();
        System.out.println("remote:"+url);
        userURL.put(user,url);
    }
    public Map<String,String> getURLs() {
        return userURL;
    }
}

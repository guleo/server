package com.model.persist.domain;

import java.lang.reflect.Field;

/**
 * Created by frank on 2016/3/5.
 */
public class UserBase {
    private int userId;
    private String username;
    private String password;

    public void setEntity (String[] params) {
        username = params[1];
        password = params[2];
    }
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

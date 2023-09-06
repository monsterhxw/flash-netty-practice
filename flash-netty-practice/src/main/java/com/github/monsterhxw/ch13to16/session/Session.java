package com.github.monsterhxw.ch13to16.session;

/**
 * @author huangxuewei
 * @since 2023/9/7
 */
public class Session {

    public Session(Integer userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    private Integer userId;

    private String username;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

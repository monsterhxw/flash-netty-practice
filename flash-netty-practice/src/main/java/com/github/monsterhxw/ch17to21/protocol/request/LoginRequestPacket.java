package com.github.monsterhxw.ch17to21.protocol.request;


import com.github.monsterhxw.ch17to21.protocol.Packet;
import com.github.monsterhxw.ch17to21.protocol.command.Command;

/**
 * @author huangxuewei
 * @since 2023/9/6
 */
public class LoginRequestPacket extends Packet {

    private Integer userId;

    private String username;

    private String password;

    @Override
    public byte getCommand() {
        return Command.LOGIN_REQUEST;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginRequestPacket{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

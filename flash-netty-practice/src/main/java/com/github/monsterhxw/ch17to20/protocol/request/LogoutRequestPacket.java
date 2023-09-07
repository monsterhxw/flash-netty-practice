package com.github.monsterhxw.ch17to20.protocol.request;

import com.github.monsterhxw.ch17to20.protocol.Packet;
import com.github.monsterhxw.ch17to20.protocol.command.Command;

/**
 * @author huangxuewei
 * @since 2023/9/7
 */
public class LogoutRequestPacket extends Packet {

    private Integer userId;

    private String username;

    @Override
    public byte getCommand() {
        return Command.LOGOUT_REQUEST;
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

    @Override
    public String toString() {
        return "LogoutRequestPacket{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                '}';
    }
}

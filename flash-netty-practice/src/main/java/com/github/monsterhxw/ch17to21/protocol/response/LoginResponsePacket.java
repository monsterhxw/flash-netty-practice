package com.github.monsterhxw.ch17to21.protocol.response;


import com.github.monsterhxw.ch17to21.protocol.Packet;
import com.github.monsterhxw.ch17to21.protocol.command.Command;

/**
 * @author huangxuewei
 * @since 2023/9/6
 */
public class LoginResponsePacket extends Packet {

    private boolean success;

    private String reason;

    private Integer userId;

    private String username;

    @Override
    public byte getCommand() {
        return Command.LOGIN_RESPONSE;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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
        return "LoginResponsePacket{" +
                "success=" + success +
                ", reason='" + reason + '\'' +
                ", userId=" + userId +
                ", username='" + username + '\'' +
                '}';
    }
}

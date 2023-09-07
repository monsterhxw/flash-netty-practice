package com.github.monsterhxw.ch17to21.protocol.response;

import com.github.monsterhxw.ch17to21.protocol.Packet;
import com.github.monsterhxw.ch17to21.protocol.command.Command;

/**
 * @author huangxuewei
 * @since 2023/9/7
 */
public class LogoutResponsePacket extends Packet {

    private boolean success;

    private String reason;

    @Override
    public byte getCommand() {
        return Command.LOGOUT_RESPONSE;
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
}

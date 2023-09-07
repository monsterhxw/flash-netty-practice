package com.github.monsterhxw.ch17to21.protocol.response;

import com.github.monsterhxw.ch17to21.protocol.Packet;
import com.github.monsterhxw.ch17to21.protocol.command.Command;
import com.github.monsterhxw.ch17to21.session.Session;

/**
 * @author huangxuewei
 * @since 2023/9/7
 */
public class GroupMessageResponsePacket extends Packet {

    private String fromGroupId;

    private Session fromSession;

    private String message;

    public GroupMessageResponsePacket() {
    }

    public GroupMessageResponsePacket(String fromGroupId, Session fromSession, String message) {
        this.fromGroupId = fromGroupId;
        this.fromSession = fromSession;
        this.message = message;
    }

    @Override
    public byte getCommand() {
        return Command.GROUP_MESSAGE_RESPONSE;
    }

    @Override
    public String toString() {
        return "GroupMessageResponsePacket{" +
                "fromGroupId='" + fromGroupId + '\'' +
                ", fromSession=" + fromSession +
                ", message='" + message + '\'' +
                '}';
    }

    public String getFromGroupId() {
        return fromGroupId;
    }

    public void setFromGroupId(String fromGroupId) {
        this.fromGroupId = fromGroupId;
    }

    public Session getFromSession() {
        return fromSession;
    }

    public void setFromSession(Session fromSession) {
        this.fromSession = fromSession;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

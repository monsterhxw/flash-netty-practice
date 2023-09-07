package com.github.monsterhxw.ch17to21.protocol.request;

import com.github.monsterhxw.ch17to21.protocol.Packet;
import com.github.monsterhxw.ch17to21.protocol.command.Command;

/**
 * @author huangxuewei
 * @since 2023/9/7
 */
public class GroupMessageRequestPacket extends Packet {

    private String toGroupId;

    private String message;

    public GroupMessageRequestPacket() {

    }

    public GroupMessageRequestPacket(String toGroupId, String message) {
        this.toGroupId = toGroupId;
        this.message = message;
    }

    @Override
    public byte getCommand() {
        return Command.GROUP_MESSAGE_REQUEST;
    }

    @Override
    public String toString() {
        return "GroupMessageRequestPacket{" +
                "toGroupId='" + toGroupId + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    public String getToGroupId() {
        return toGroupId;
    }

    public void setToGroupId(String toGroupId) {
        this.toGroupId = toGroupId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

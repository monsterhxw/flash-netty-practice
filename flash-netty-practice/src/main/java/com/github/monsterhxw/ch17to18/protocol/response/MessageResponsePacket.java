package com.github.monsterhxw.ch17to18.protocol.response;

import com.github.monsterhxw.ch17to18.protocol.Packet;
import com.github.monsterhxw.ch17to18.protocol.command.Command;

/**
 * @author huangxuewei
 * @since 2023/9/6
 */
public class MessageResponsePacket extends Packet {

    private String message;

    private Integer fromUserId;

    private String fromUserName;

    @Override
    public byte getCommand() {
        return Command.MESSAGE_RESPONSE;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Integer fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    @Override
    public String toString() {
        return "MessageResponsePacket{" +
                "message='" + message + '\'' +
                ", fromUserId=" + fromUserId +
                ", fromUserName='" + fromUserName + '\'' +
                '}';
    }
}

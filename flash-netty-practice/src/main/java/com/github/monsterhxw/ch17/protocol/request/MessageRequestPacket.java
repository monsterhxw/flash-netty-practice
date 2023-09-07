package com.github.monsterhxw.ch17.protocol.request;


import com.github.monsterhxw.ch17.protocol.Packet;
import com.github.monsterhxw.ch17.protocol.command.Command;

/**
 * @author huangxuewei
 * @since 2023/9/6
 */
public class MessageRequestPacket extends Packet {

    public MessageRequestPacket(Integer toUserId, String message) {
        this.message = message;
        this.toUserId = toUserId;
    }

    public MessageRequestPacket() {
    }

    private String message;

    private Integer toUserId;

    @Override
    public byte getCommand() {
        return Command.MESSAGE_REQUEST;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getToUserId() {
        return toUserId;
    }

    public void setToUserId(Integer toUserId) {
        this.toUserId = toUserId;
    }

    @Override
    public String toString() {
        return "MessageRequestPacket{" +
                "message='" + message + '\'' +
                ", toUserId=" + toUserId +
                '}';
    }
}

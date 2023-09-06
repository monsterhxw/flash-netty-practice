package com.github.monsterhxw.ch13to16.proto;

/**
 * @author huangxuewei
 * @since 2023/9/6
 */
public class MessageRequestPacket extends Packet {

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
}

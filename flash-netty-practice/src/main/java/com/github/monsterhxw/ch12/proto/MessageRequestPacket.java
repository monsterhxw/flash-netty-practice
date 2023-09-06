package com.github.monsterhxw.ch12.proto;

/**
 * @author huangxuewei
 * @since 2023/9/6
 */
public class MessageRequestPacket extends Packet {

    private String message;

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
}

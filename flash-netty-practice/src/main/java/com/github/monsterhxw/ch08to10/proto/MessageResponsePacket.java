package com.github.monsterhxw.ch08to10.proto;

/**
 * @author huangxuewei
 * @since 2023/9/6
 */
public class MessageResponsePacket extends Packet {

    private String message;

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
}

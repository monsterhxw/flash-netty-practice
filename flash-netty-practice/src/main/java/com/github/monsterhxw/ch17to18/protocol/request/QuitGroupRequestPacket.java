package com.github.monsterhxw.ch17to18.protocol.request;

import com.github.monsterhxw.ch17to18.protocol.Packet;
import com.github.monsterhxw.ch17to18.protocol.command.Command;

/**
 * @author huangxuewei
 * @since 2023/9/7
 */
public class QuitGroupRequestPacket extends Packet {

    private String groupId;

    public QuitGroupRequestPacket() {

    }

    public QuitGroupRequestPacket(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public byte getCommand() {
        return Command.QUIT_GROUP_REQUEST;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}

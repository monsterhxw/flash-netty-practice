package com.github.monsterhxw.ch17to20.protocol.request;

import com.github.monsterhxw.ch17to20.protocol.Packet;
import com.github.monsterhxw.ch17to20.protocol.command.Command;

/**
 * @author huangxuewei
 * @since 2023/9/7
 */
public class JoinGroupRequestPacket extends Packet {

    private String groupId;

    public JoinGroupRequestPacket() {

    }

    public JoinGroupRequestPacket(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public byte getCommand() {
        return Command.JOIN_GROUP_REQUEST;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}

package com.github.monsterhxw.ch17to21.protocol.request;

import com.github.monsterhxw.ch17to21.protocol.Packet;
import com.github.monsterhxw.ch17to21.protocol.command.Command;

/**
 * @author huangxuewei
 * @since 2023/9/7
 */
public class ListGroupMembersRequestPacket extends Packet {

    private String groupId;

    public ListGroupMembersRequestPacket() {

    }

    public ListGroupMembersRequestPacket(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public byte getCommand() {
        return Command.LIST_GROUP_MEMBERS_REQUEST;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}

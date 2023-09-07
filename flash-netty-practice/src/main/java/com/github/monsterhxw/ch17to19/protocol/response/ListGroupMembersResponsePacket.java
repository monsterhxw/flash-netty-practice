package com.github.monsterhxw.ch17to19.protocol.response;

import com.github.monsterhxw.ch17to19.protocol.Packet;
import com.github.monsterhxw.ch17to19.protocol.command.Command;
import com.github.monsterhxw.ch17to19.session.Session;

import java.util.List;

/**
 * @author huangxuewei
 * @since 2023/9/7
 */
public class ListGroupMembersResponsePacket extends Packet {

    private String groupId;

    private List<Session> sessions;

    public ListGroupMembersResponsePacket() {

    }

    public ListGroupMembersResponsePacket(String groupId, List<Session> sessions) {
        this.groupId = groupId;
        this.sessions = sessions;
    }

    @Override
    public byte getCommand() {
        return Command.LIST_GROUP_MEMBERS_RESPONSE;
    }

    @Override
    public String toString() {
        return "ListGroupMembersResponsePacket{" +
                "groupId='" + groupId + '\'' +
                ", sessions=" + sessions +
                '}';
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }
}

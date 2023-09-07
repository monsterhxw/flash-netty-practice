package com.github.monsterhxw.ch17to19.protocol.request;

import com.github.monsterhxw.ch17to19.protocol.Packet;
import com.github.monsterhxw.ch17to19.protocol.command.Command;

import java.util.List;

/**
 * @author huangxuewei
 * @since 2023/9/7
 */
public class CreateGroupRequestPacket extends Packet {

    private List<Integer> userIdList;

    @Override
    public byte getCommand() {
        return Command.CREATE_GROUP_REQUEST;
    }

    public List<Integer> getUserIdList() {
        return userIdList;
    }

    public void setUserIdList(List<Integer> userIdList) {
        this.userIdList = userIdList;
    }

    @Override
    public String toString() {
        return "CreateGroupRequestPacket{" +
                "userIdList=" + userIdList +
                '}';
    }
}

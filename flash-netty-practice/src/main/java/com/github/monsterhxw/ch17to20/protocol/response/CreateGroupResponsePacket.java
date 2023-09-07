package com.github.monsterhxw.ch17to20.protocol.response;

import com.github.monsterhxw.ch17to20.protocol.Packet;
import com.github.monsterhxw.ch17to20.protocol.command.Command;

import java.util.List;

/**
 * @author huangxuewei
 * @since 2023/9/7
 */
public class CreateGroupResponsePacket extends Packet {

    private boolean success;

    private String groupId;

    private List<String> userNameList;

    @Override
    public byte getCommand() {
        return Command.CREATE_GROUP_RESPONSE;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public List<String> getUserNameList() {
        return userNameList;
    }

    public void setUserNameList(List<String> userNameList) {
        this.userNameList = userNameList;
    }

    @Override
    public String toString() {
        return "CreateGroupResponsePacket{" +
                "success=" + success +
                ", groupId='" + groupId + '\'' +
                ", userNameList=" + userNameList +
                '}';
    }
}

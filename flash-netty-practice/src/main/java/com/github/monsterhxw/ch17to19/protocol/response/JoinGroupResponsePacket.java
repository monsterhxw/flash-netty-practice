package com.github.monsterhxw.ch17to19.protocol.response;

import com.github.monsterhxw.ch17to19.protocol.Packet;
import com.github.monsterhxw.ch17to19.protocol.command.Command;

/**
 * @author huangxuewei
 * @since 2023/9/7
 */
public class JoinGroupResponsePacket extends Packet {

    private String groupId;

    private boolean success;

    private String reason;

    public JoinGroupResponsePacket() {

    }

    public JoinGroupResponsePacket(String groupId, boolean success, String reason) {
        this.groupId = groupId;
        this.success = success;
        this.reason = reason;
    }

    @Override
    public byte getCommand() {
        return Command.JOIN_GROUP_RESPONSE;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "JoinGroupResponsePacket{" +
                "groupId='" + groupId + '\'' +
                ", success=" + success +
                ", reason='" + reason + '\'' +
                '}';
    }
}

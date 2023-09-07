package com.github.monsterhxw.ch17to18.protocol.response;

import com.github.monsterhxw.ch17to18.protocol.Packet;
import com.github.monsterhxw.ch17to18.protocol.command.Command;

/**
 * @author huangxuewei
 * @since 2023/9/7
 */
public class QuitGroupResponsePacket extends Packet {

    private String groupId;

    private boolean success;

    private String reason;

    public QuitGroupResponsePacket() {

    }

    public QuitGroupResponsePacket(String groupId, boolean success, String reason) {
        this.groupId = groupId;
        this.success = success;
        this.reason = reason;
    }

    @Override
    public byte getCommand() {
        return Command.QUIT_GROUP_RESPONSE;
    }

    @Override
    public String toString() {
        return "QuitGroupResponsePacket{" +
                "groupId='" + groupId + '\'' +
                ", success=" + success +
                ", reason='" + reason + '\'' +
                '}';
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
}

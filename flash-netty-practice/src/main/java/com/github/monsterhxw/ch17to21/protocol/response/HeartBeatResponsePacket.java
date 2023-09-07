package com.github.monsterhxw.ch17to21.protocol.response;

import com.github.monsterhxw.ch17to21.protocol.Packet;
import com.github.monsterhxw.ch17to21.protocol.command.Command;

/**
 * @author huangxuewei
 * @since 2023/9/7
 */
public class HeartBeatResponsePacket extends Packet {

    @Override
    public byte getCommand() {
        return Command.HEARTBEAT_RESPONSE;
    }
}

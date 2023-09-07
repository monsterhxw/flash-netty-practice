package com.github.monsterhxw.ch17.protocol;

/**
 * @author huangxuewei
 * @since 2023/9/6
 */

public abstract class Packet {
    private byte version = 1;

    public abstract byte getCommand();

    public byte getVersion() {
        return version;
    }

    public void setVersion(byte version) {
        this.version = version;
    }
}

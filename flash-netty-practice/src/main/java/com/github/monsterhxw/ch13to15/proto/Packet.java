package com.github.monsterhxw.ch13to15.proto;

/**
 * @author huangxuewei
 * @since 2023/9/6
 */

public abstract class Packet {
    private Byte version = 1;

    public abstract byte getCommand();

    public Byte getVersion() {
        return version;
    }

    public void setVersion(Byte version) {
        this.version = version;
    }
}

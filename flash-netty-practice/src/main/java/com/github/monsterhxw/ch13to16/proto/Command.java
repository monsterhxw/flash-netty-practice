package com.github.monsterhxw.ch13to16.proto;

/**
 * @author huangxuewei
 * @since 2023/9/6
 */
public interface Command {

    byte LOGIN_REQUEST = 1;

    byte LOGIN_RESPONSE = 2;

    byte MESSAGE_REQUEST = 3;

    byte MESSAGE_RESPONSE = 4;
}

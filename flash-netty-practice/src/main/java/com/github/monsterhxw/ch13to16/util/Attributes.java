package com.github.monsterhxw.ch13to16.util;

import com.github.monsterhxw.ch13to16.session.Session;
import io.netty.util.AttributeKey;

/**
 * @author huangxuewei
 * @since 2023/9/6
 */
public interface Attributes {

    AttributeKey<Boolean> LOGIN = AttributeKey.newInstance("LOGIN");

    AttributeKey<Session> SESSION = AttributeKey.newInstance("SESSION");
}
